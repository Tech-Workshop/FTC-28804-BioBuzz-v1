package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

@TeleOp(name="RED-Manual")
public class ManualRed extends LinearOpMode {
	private final Robot robot = Robot.getInstance();

	boolean firstLoop=true;
	boolean disableTurrets=false;

	ElapsedTime shooterDelay=new ElapsedTime();
	boolean shooterFlag=false;

	Pose defaultStart=new Pose(57.0,10.0,Math.toRadians(90.0));

	@Override
	public void runOpMode() throws InterruptedException {
		//Retrieve end pose from auton
		if(PoseStorage.currentPose!=null) {
			defaultStart= PoseStorage.currentPose;
		}

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry, defaultStart, Constants.General.Alliance.RED);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.turret.turnToDegrees(0.0);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			if(firstLoop) {
				firstLoop=false;
			}

			//------------------------------------------
			//DRIVETRAIN

			robot.drive.drive(gamepad1);

			//Driver override heading
			if(gamepad1.back) {
				Pose forcedPose=new Pose(72.0, 72.0, Math.toRadians(0.0)); //RED Alliance-specific
				robot.drive.follower.setPose(forcedPose);

				//Disable turrets
				robot.turret.turnToDegrees(0.0);
				disableTurrets=true;
			}

			//------------------------------------------
			//INTAKE

			//Intake
			if(gamepad2.right_trigger>=Constants.General.CONTROLLER_DEADBAND) {
				robot.intake.intake();
				robot.indexer.sortSpeed(); //NEW

				//Expel
			} else if(gamepad2.right_bumper) {
				robot.intake.expel();

				//Stop
			} else {
				robot.intake.stop();
			}

			//------------------------------------------
			//INDEXER

			//Index
			if(gamepad2.left_trigger>=Constants.General.CONTROLLER_DEADBAND) {
				robot.indexer.index();

				//Expel
			} else if(gamepad2.left_bumper) {
				robot.indexer.reverse();

				//Stop
			} else {
				robot.indexer.stop();
			}

			//------------------------------------------
			//SHOOTERS

			//Shoot Pollen
			if(gamepad1.left_trigger>=Constants.General.CONTROLLER_DEADBAND) {
				//Wait 1 second, open gate and index
				if(!shooterFlag) {
					robot.shooter.spinFlywheel();
					shooterDelay.reset();
					shooterFlag=true;

					//Spun up - open gate and index
				} else if(shooterDelay.time()>0.5 && robot.shooter.speedWithinError()) {
					robot.shooter.openGate();
					robot.indexer.index();
					telemetry.addLine("Opened gate");
				}

				telemetry.addData("Shoot pollen - shoot target=",robot.shooter.getTargetVelocity());
				telemetry.addData("Shoot pollen - shoot actual=",robot.shooter.getVelocity());

			} else {
				shooterFlag=false;
				robot.shooter.stopFlywheel();
				robot.shooter.closeGate();
			}

			//------------------------------------------
			//TURRETS

			if(gamepad2.back) {
				robot.turret.turnToDegrees(0.0);
				disableTurrets=true;
			}

			if(!disableTurrets) {
				robot.turret.turnToTarget();
			}

			//Run periodic methods
			robot.ramp.periodic();
			robot.shooter.periodic();

			//------------------------------------------
			//Telemetry
			telemetry.addData("PP (x)",robot.drive.follower.pose().x());
			telemetry.addData("PP (y)",robot.drive.follower.pose().y());
			telemetry.addData("PP (heading-deg)",Math.toDegrees(robot.drive.follower.pose().heading()));

			telemetry.update();
		}
	}
}
