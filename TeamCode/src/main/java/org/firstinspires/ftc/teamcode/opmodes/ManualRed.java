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

	ElapsedTime shooterNectarDelay=new ElapsedTime();
	ElapsedTime shooterPollenDelay=new ElapsedTime();
	boolean shooterNectarFlag=false,shooterPollenFlag=false;

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
		robot.shooterNectar.closeGate();
		robot.shooterPollen.closeGate();
		robot.turretNectar.turnToDegrees(0.0);
		robot.turretPollen.turnToDegrees(0.0);
		robot.sorter.home();

		/*
		telemetry.addLine("Retract arm -- START --");
		telemetry.update();
		robot.intake.retractArm();
		while(!robot.intake.isArmDone()) {
			telemetry.addData("Retract arm pos:",robot.intake.getArmPosition());
			telemetry.update();
			robot.intake.armPeriodic();
		}
		telemetry.addLine("Retract arm -- END --");
		telemetry.update();
		*/

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			if(firstLoop) {
				robot.intake.extendArm();
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
				robot.turretNectar.turnToDegrees(0.0);
				robot.turretPollen.turnToDegrees(0.0);
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

			//Shoot Nectar
			if(gamepad1.right_trigger>=Constants.General.CONTROLLER_DEADBAND) {

				//Wait 1 second, open gate and index
				if(!shooterNectarFlag) {
					robot.shooterNectar.spinFlywheel();
					shooterNectarDelay.reset();
					shooterNectarFlag=true;

					//Spun up - open gate and index
				} else if(shooterNectarDelay.time()>0.5 && robot.shooterNectar.speedWithinError()) {
					robot.shooterNectar.openGate();
					robot.indexer.index();
				}

			} else {
				shooterNectarFlag=false;
				robot.shooterNectar.stopFlywheel();
				robot.shooterNectar.closeGate();
			}

			//Shoot Pollen
			if(gamepad1.left_trigger>=Constants.General.CONTROLLER_DEADBAND) {
				//Wait 1 second, open gate and index
				if(!shooterPollenFlag) {
					robot.shooterPollen.spinFlywheel();
					shooterPollenDelay.reset();
					shooterPollenFlag=true;

					//Spun up - open gate and index
				} else if(shooterPollenDelay.time()>0.5 && robot.shooterPollen.speedWithinError()) {
					robot.shooterPollen.openGate();
					robot.indexer.index();
					telemetry.addLine("Opened gate");
				}

				telemetry.addData("Shoot pollen - shoot target=",robot.shooterPollen.getTargetVelocity());
				telemetry.addData("Shoot pollen - shoot actual=",robot.shooterPollen.getVelocity());

			} else {
				shooterPollenFlag=false;
				robot.shooterPollen.stopFlywheel();
				robot.shooterPollen.closeGate();
			}

			//------------------------------------------
			//SORTER

			if(gamepad2.a) {
				robot.indexer.sortSpeed();
				robot.sorter.sort();
			} else {
				robot.sorter.home();
			}

			//------------------------------------------
			//TURRETS

			if(gamepad2.back) {
				robot.turretNectar.turnToDegrees(0.0);
				robot.turretPollen.turnToDegrees(0.0);
				disableTurrets=true;
			}

			//Run periodic methods
			robot.intake.armPeriodic();
			robot.shooterPollen.periodic();
			robot.shooterNectar.periodic();

			if(!disableTurrets) {
				robot.turretNectar.turnToTarget();
				robot.turretPollen.turnToTarget();
			}

			//Telemetry
			telemetry.addData("PP (x)",robot.drive.follower.pose().x());
			telemetry.addData("PP (y)",robot.drive.follower.pose().y());
			telemetry.addData("PP (heading-deg)",Math.toDegrees(robot.drive.follower.pose().heading()));

			telemetry.update();
		}
	}
}
