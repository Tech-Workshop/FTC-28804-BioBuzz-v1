package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Ramp;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

public class ManualBase extends LinearOpMode {
	private final Robot robot = Robot.getInstance();

	boolean firstLoop=true, disableTurrets=false, driverOnly=false;
	int loopCnt=0;
	double shooterTarget=1650,loopTimeLast=0.0,shooterCorrection=0.0;

	ElapsedTime shooterDelay=new ElapsedTime();
	ElapsedTime resetShooterDelay=new ElapsedTime();
	ElapsedTime intakeTimer=new ElapsedTime();

	boolean intakeFlag=false,shooterFlag=false,shooterCorrectionFlag=false,rampMoving=false,resetShooterFlag=false;

	//Alliance-specific
	protected Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;
	protected Pose defaultStart=new Pose(57.0,10.0,Math.toRadians(90.0));
	protected Pose overridePose=new Pose(72.0, 72.0, Math.toRadians(0.0));

	//Override class to change alliance-specific variables
	protected void updateSettings() { } //Nothing in base class

	@Override
	public void runOpMode() throws InterruptedException {

		//Enable dashboard
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

		//TODO: Verify below works
		updateSettings(); //Update alliance-specific settings

		//Retrieve end pose from auton
		if(PoseStorage.currentPose!=null) {
			defaultStart= PoseStorage.currentPose;
		}

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry, defaultStart, thisAlliance);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.turret.turnToZero();

		robot.sensors.setLedDisable();
		telemetry.update();

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			if(firstLoop) {
				robot.drive.setInitialHeadingTarget();
				firstLoop=false;
			}

			//------------------------------------------
			//Change Aim Mode
			if(gamepad2.backWasPressed()) {
				if(robot.aimMode==Constants.General.AimMode.TURRET) {
					robot.aimMode=Constants.General.AimMode.TURN;
				} else if(robot.aimMode==Constants.General.AimMode.TURRET) {
					robot.aimMode=Constants.General.AimMode.MANUAL;
				} else {
					robot.aimMode=Constants.General.AimMode.TURRET;
				}
			}

			if(driverOnly) { telemetry.addLine("=== DRIVER ONLY CONTROL ==="); }

			//Display Aim Mode
			telemetry.addData("Aim Mode:",robot.aimMode);
			telemetry.addLine("-----------------------------------");
			//telemetry.addData("Turret status:",robot.turret.getStatus());
			//telemetry.addData("Angular velocity:",robot.drive.follower.velocity().omega);

			//Change to driver only mode
			if(gamepad1.a && gamepad1.b) {
				driverOnly=true;
			}

			//------------------------------------------
			//DRIVETRAIN

			//Driver override heading
			if(gamepad1.backWasPressed()) {
				robot.drive.overridePose(overridePose);

			//Standard driver controls
			} else {
				robot.drive.drive(gamepad1);
			}

			//------------------------------------------
			//FLOWER RAMP

			if(gamepad2.dpadUpWasPressed() || (driverOnly && Constants.Shooter.SHOOTER_SPEED_AUTO && gamepad1.dpadUpWasPressed())) {
				if(robot.ramp.getStatus()!= Ramp.RAMP_STATUS.UP) {
					robot.ramp.raise();
					rampMoving=true;
				}
			} else if(gamepad2.dpadDownWasPressed() || (driverOnly && Constants.Shooter.SHOOTER_SPEED_AUTO && gamepad1.dpadDownWasPressed())) {
				if(robot.ramp.getStatus()!= Ramp.RAMP_STATUS.DOWN) {
					robot.ramp.lower();
					rampMoving=true;
				}
			}

			if(rampMoving) {
				robot.ramp.periodic();
				if(robot.ramp.getStatus()!=Ramp.RAMP_STATUS.MOVING) {
					rampMoving=false;
				}
			}

			//------------------------------------------
			//SHOOTER

			//Manually set correction
			/*
			if(gamepad1.dpadRightWasPressed()) {
				shooterCorrection+=10;
				robot.shooter.setCorrection(shooterCorrection);
			} else if(gamepad1.dpadLeftWasPressed()) {
				shooterCorrection-=10;
				robot.shooter.setCorrection(shooterCorrection);
			}

			telemetry.addData("Shooter Correction:",shooterCorrection);
			telemetry.addData("Resting Battery Voltage:",robot.shooter.getRestingBatteryVoltage());
			*/

			if(robot.aimMode==Constants.General.AimMode.MANUAL) {
				if (gamepad1.dpadUpWasPressed()) {
					shooterTarget += 10;
					robot.shooter.setShotVelocity(shooterTarget);
				} else
					if (gamepad1.dpadDownWasPressed()) {
						shooterTarget -= 10;
						robot.shooter.setShotVelocity(shooterTarget);
				}

				if(gamepad1.dpadLeftWasPressed()) {
					robot.shooter.lowerHood();
				} else if(gamepad1.dpadRightWasPressed()) {
					robot.shooter.raiseHood();
				}
			}

			telemetry.addData("Shooter (target):", shooterTarget);
			telemetry.addData("Shooter (actual):", robot.shooter.getVelocity());
			telemetry.addData("Hood Position:",robot.shooter.getHoodPosition());

			//Shoot - TURRET
			if(robot.aimMode==Constants.General.AimMode.TURRET) {
				//Spin flywheel and adjust hood based on distances
				if (gamepad1.left_trigger >= Constants.General.CONTROLLER_DEADBAND) {
					//Get vertical and horizontal distances from target
					double s = robot.turret.getSidewaysDistance();
					double v = robot.turret.getVerticalDistance();

					//Set shooter velocity and hood positions
					robot.shooter.setShotVelocityUsingDistancesFromTarget(s, v);
					robot.shooter.setHoodPositionUsingDistancesFromTarget(s, v);
					robot.shooter.spinFlywheel();

					if (gamepad1.right_trigger >= Constants.General.CONTROLLER_DEADBAND && (shooterFlag || (robot.shooter.speedWithinError() && robot.turret.headingWithinError()))) {
						robot.shooter.openGate();
						robot.indexer.shootSpeed();
						shooterFlag=true;
					} else {
						robot.shooter.closeGate();
						robot.indexer.stop();
						shooterFlag=false;
					}

				} else {
					robot.shooter.stopFlywheel();

					if(shooterFlag) {
						robot.shooter.closeGate();
						robot.indexer.stop();
						shooterFlag = false;
					}
				}

			//Shoot - TURN/MANUAL
			} else {

				if (gamepad1.right_trigger >= Constants.General.CONTROLLER_DEADBAND) {
					//Start spinning flywheel
					if (!shooterFlag) {
						//Set shooter speed and hood angle based on vertical and sideways distances
						if (Constants.Shooter.SHOOTER_SPEED_AUTO) {
							//Get vertical and horizontal distances from target
							double s = robot.turret.getSidewaysDistance();
							double v = robot.turret.getVerticalDistance();

							//Set shooter velocity and hood positions
							robot.shooter.setShotVelocityUsingDistancesFromTarget(s, v);
							robot.shooter.setHoodPositionUsingDistancesFromTarget(s, v);
						}

						robot.shooter.spinFlywheel();
						shooterDelay.reset();
						shooterFlag = true;

						//Aim Mode is Turn to Target
						if (robot.aimMode == Constants.General.AimMode.TURN) {
							robot.drive.setHeadingTarget(Math.toRadians(robot.turret.getHeadingToTarget()));
						}

						//Minor speed/heading corrections
					} else
						if (shooterDelay.time() > 0.5 && shooterDelay.time() < 1.0 && loopCnt % 5 == 0) {
							if (Constants.Shooter.SHOOTER_SPEED_AUTO) {
								//Get vertical and horizontal distances from target
								double s = robot.turret.getSidewaysDistance();
								double v = robot.turret.getVerticalDistance();

								//Set shooter velocity and hood positions
								robot.shooter.setShotVelocityUsingDistancesFromTarget(s, v);
								robot.shooter.setHoodPositionUsingDistancesFromTarget(s, v);
							}

							//Aim Mode is Turn to Target
							if (robot.aimMode == Constants.General.AimMode.TURN) {
								robot.drive.setHeadingTarget(Math.toRadians(robot.turret.getHeadingToTarget()));
							}

							//Spun up - open gate and index
						} else
							if (shooterDelay.time() > 1.0 && robot.shooter.speedWithinError() && (robot.aimMode != Constants.General.AimMode.TURN || (robot.drive.follower.velocity().omega < 0.1 && robot.drive.getHeadingErrorInDegrees() < 5))) {
								robot.shooter.openGate();
								robot.indexer.shootSpeed();
								telemetry.addLine("Opened gate");
							}

					if (Constants.General.DEBUG_MODE) {
						telemetry.addData("Shoot - target=", robot.shooter.getTargetVelocity());
						telemetry.addData("Shoot - comp target=", "%.1f", robot.shooter.getCompensatedTargetVelocity());
						telemetry.addData("Shoot - actual=", robot.shooter.getVelocity());
						telemetry.addData("Shoot - rest voltage=", "%.2f V", robot.shooter.getBatteryVoltage());
						telemetry.addData("Shoot - raw voltage=", "%.2f V", robot.shooter.getRawBatteryVoltage());
					}

				} else if (shooterFlag) {
					shooterFlag = false;
					robot.shooter.stopFlywheel();
					robot.indexer.reverse();
					resetShooterFlag = true;
					resetShooterDelay.reset();
				} else
					if (resetShooterFlag) {
						//Stop indexing reverse and close ball gate
						if (resetShooterDelay.time() > 1.0) {
							robot.shooter.closeGate();
							robot.indexer.stop();
							resetShooterFlag = false;
						}
				}
			}

			robot.shooter.periodic();

			//------------------------------------------
			//INTAKE

			//Intake (only when not shooting)
			if(gamepad1.left_trigger<=Constants.General.CONTROLLER_DEADBAND && !resetShooterFlag && !shooterFlag) {
				if (gamepad2.right_trigger>=Constants.General.CONTROLLER_DEADBAND || (driverOnly && gamepad1.right_bumper)) {
					robot.intake.intake();
					robot.indexer.intakeSpeed();
					intakeFlag = true;

				//Expel
				} else
					if (gamepad2.left_trigger >= Constants.General.CONTROLLER_DEADBAND || (driverOnly && gamepad1.y)) {
						robot.intake.expel();
						intakeFlag = false;

						//Stop
					} else {
						robot.indexer.stop();

						if (intakeFlag) {
							intakeTimer.reset();
							intakeFlag = false;
						}

						if (intakeTimer.time() < Constants.Intake.INTAKE_EXPEL_TIME) {
							robot.intake.expel();
						} else {
							robot.intake.stop();
						}
					}
			} else {
				robot.intake.stop();
			}

			//------------------------------------------
			//TURRETS

			//Auto-aim turret
			if(robot.aimMode==Constants.General.AimMode.TURRET) {
				//Enable only when not in center strip of field and driver is ramping up shooter --or-- every 20 loops
				if((gamepad1.left_trigger >= Constants.General.CONTROLLER_DEADBAND || loopCnt%20==0) && (robot.drive.follower.pose().y()<48.0 || robot.drive.follower.pose().y()>92.0)) {
					robot.turret.turnToTarget();
				}

			//Non-turret mode - keep turret at zero (every 20 loops)
			} else if(robot.aimMode!=Constants.General.AimMode.TURRET && loopCnt%20==0) {
				robot.turret.turnToZero();
			}

			//Turret Position Testing
			/*
			if(gamepad1.x) {
				robot.turret.turnToDegrees(90);
			} else if(gamepad1.a) {
				robot.turret.turnToDegrees(0);
			} else if(gamepad1.b) {
				robot.turret.turnToDegrees(-90);
			}

			telemetry.addData("Turret position:",robot.turret.getPosition());
			*/

			//------------------------------------------
			//Telemetry
			telemetry.addData("PP (x)",robot.drive.follower.pose().x());
			telemetry.addData("PP (y)",robot.drive.follower.pose().y());
			telemetry.addData("PP (heading-deg)",Math.toDegrees(robot.drive.follower.pose().heading()));

			double loopTimeNow = System.nanoTime();
			telemetry.addData("Loop Speed (hz): ", 1000000000 / (loopTimeNow - loopTimeLast));
			telemetry.update();

			loopTimeLast = loopTimeNow; //Save timer for loop time calculation
			loopCnt++;
		}
	}
}

