package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Ramp;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

public class ManualBase extends LinearOpMode {

	private final Robot robot = Robot.getInstance();

	boolean firstLoop=true;
	boolean disableTurrets=false;
	int loopCnt=0;
	double shooterTarget=1450;

	ElapsedTime shooterDelay=new ElapsedTime();
	ElapsedTime resetShooterDelay=new ElapsedTime();
	ElapsedTime intakeTimer=new ElapsedTime();

	boolean intakeFlag=false,shooterFlag=false,rampMoving=false,resetShooterFlag=false;

	//Alliance-specific
	protected Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;
	protected Pose defaultStart=new Pose(57.0,10.0,Math.toRadians(90.0));
	protected Pose overridePose=new Pose(72.0, 72.0, Math.toRadians(0.0));

	//Override class to change alliance-specific variables
	protected void updateSettings() { } //Nothing in base class

	@Override
	public void runOpMode() throws InterruptedException {

		//TODO: Verify below works
		updateSettings(); //Update alliance-specific settings

		//Retrieve end pose from auton
		if(PoseStorage.currentPose!=null) {
			defaultStart= PoseStorage.currentPose;
		}

		//Initialize robot hardware
		//robot.init(hardwareMap, telemetry, defaultStart, thisAlliance);
		robot.init(hardwareMap, telemetry, defaultStart, thisAlliance);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.turret.turnToZero();

		telemetry.update();

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			if(firstLoop) {
				robot.drive.setInitialHeadingTarget();
				firstLoop=false;
			}

			//------------------------------------------
			//DRIVETRAIN

			//Driver override heading
			if(gamepad1.back) {
				robot.drive.overridePose(overridePose);
			}

			//Standard driver controls
			robot.drive.drive(gamepad1);

			//------------------------------------------
			//FLOWER RAMP

			if(gamepad2.dpadUpWasPressed()) {
				if(robot.ramp.getStatus()!= Ramp.RAMP_STATUS.UP) {
					robot.ramp.raise();
					rampMoving=true;
				}
			} else if(gamepad2.dpadDownWasPressed()) {
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

			if(gamepad1.dpadUpWasPressed()) {
				shooterTarget+=10;
				robot.shooter.setShotVelocity(shooterTarget);
			} else if(gamepad1.dpadDownWasPressed()) {
				shooterTarget-=10;
				robot.shooter.setShotVelocity(shooterTarget);
			}

			telemetry.addData("Shooter Velocity:",shooterTarget);

			if(gamepad1.dpadLeftWasPressed()) {
				robot.shooter.lowerHood();
			} else if(gamepad1.dpadRightWasPressed()) {
				robot.shooter.raiseHood();
			}

			telemetry.addData("Hood Position:",robot.shooter.getHoodPosition());

			//Shoot
			if(gamepad1.right_trigger>=Constants.General.CONTROLLER_DEADBAND) {
				//Start spinning flywheel and counter rollers
				if(!shooterFlag) {
					robot.shooter.spinFlywheel();
					robot.shooter.spinCounter();
					shooterDelay.reset();
					shooterFlag=true;

					//Spun up - open gate and index
				} else if(shooterDelay.time()>1.0 && robot.shooter.speedWithinError()) {
					robot.shooter.openGate();
					robot.indexer.shootSpeed();
					telemetry.addLine("Opened gate");
				}

				telemetry.addData("Shoot - target=",robot.shooter.getTargetVelocity());
				telemetry.addData("Shoot - comp target=", "%.1f", robot.shooter.getCompensatedTargetVelocity());
				telemetry.addData("Shoot - actual=",robot.shooter.getVelocity());
				telemetry.addData("Shoot - rest voltage=", "%.2f V", robot.shooter.getBatteryVoltage());
				telemetry.addData("Shoot - raw voltage=", "%.2f V", robot.shooter.getRawBatteryVoltage());

			} else if(shooterFlag) {
				shooterFlag=false;
				robot.shooter.stopFlywheel();
				robot.shooter.stopCounter();
				robot.indexer.reverse();
				resetShooterFlag=true;
				resetShooterDelay.reset();

			} else if(resetShooterFlag) {
				//Stop indexing reverse and close ball gate
				if(resetShooterDelay.time()>1.0) {
					robot.shooter.closeGate();
					robot.indexer.stop();
					resetShooterFlag=false;
				}
			}

			robot.shooter.periodic();

			//------------------------------------------
			//INTAKE

			//Intake
			if(gamepad1.right_trigger<=Constants.General.CONTROLLER_DEADBAND && !resetShooterFlag && !shooterFlag) {
				if (gamepad2.right_trigger >= Constants.General.CONTROLLER_DEADBAND) {
					robot.intake.intake();
					robot.indexer.intakeSpeed();
					intakeFlag = true;

					//Expel
				} else
					if (gamepad2.left_trigger >= Constants.General.CONTROLLER_DEADBAND) {
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
			//HOOD

			/*
			if(gamepad1.dpadUpWasPressed()) {
				robot.shooter.
			} else if(gamepad1.dpadDownWasPressed()) {

			}
			*/

			//------------------------------------------
			//TURRETS

			//Temp: Keep turret at zero (every 20 loops)
			if(loopCnt%20==0) {
				robot.turret.turnToZero();
			}

			/*
			if(gamepad2.back) {
				robot.turret.turnToDegrees(0.0);
				disableTurrets=true;
			}
			*/

			/*
			if(!disableTurrets) {
				robot.turret.turnToTarget();
			}
			*/

			//------------------------------------------
			//Telemetry
			telemetry.addData("PP (x)",robot.drive.follower.pose().x());
			telemetry.addData("PP (y)",robot.drive.follower.pose().y());
			telemetry.addData("PP (heading-deg)",Math.toDegrees(robot.drive.follower.pose().heading()));

			telemetry.update();
			loopCnt++;
		}
	}
}

