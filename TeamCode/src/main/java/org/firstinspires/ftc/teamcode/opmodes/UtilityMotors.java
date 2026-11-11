package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Motors Test")
public class UtilityMotors extends LinearOpMode {
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,shooterLeft,shooterRight,shooterCounter,intake,indexer;
	double shooterCounterPower=0.50,indexerPower=0.50;

	@Override
	public void runOpMode() throws InterruptedException {
		leftFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_FRONT_ID);
		leftBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_BACK_ID);
		rightFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_FRONT_ID);
		rightBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_BACK_ID);

		shooterLeft = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_LEFT_ID);
		shooterLeft.setDirection(Constants.Shooter.SHOOTER_LEFT_DIRECTION);
		shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		shooterRight = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_RIGHT_ID);
		shooterRight.setDirection(Constants.Shooter.SHOOTER_RIGHT_DIRECTION);
		shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		/*
		shooterCounter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_COUNTER_ID);
		shooterCounter.setDirection(Constants.Shooter.SHOOTER_COUNTER_DIRECTION);
		shooterCounter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooterCounter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		*/

		intake = hardwareMap.get(DcMotorEx.class, Constants.Intake.INTAKE_ID);
		intake.setDirection(Constants.Intake.INTAKE_DIRECTION);
		indexer = hardwareMap.get(DcMotorEx.class, Constants.Indexer.INDEXER_ID);
		indexer.setDirection(Constants.Indexer.INDEXER_DIRECTION);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			/*
			if(gamepad1.a) {
				leftFrontDrive.setPower(0.2);
				leftBackDrive.setPower(0.2);
				rightFrontDrive.setPower(0.2);
				rightBackDrive.setPower(0.2);
			} else {
				leftFrontDrive.setPower(0.0);
				leftBackDrive.setPower(0.0);
				rightFrontDrive.setPower(0.0);
				rightBackDrive.setPower(0.0);
			}
			*/

			/*
			if(gamepad1.dpad_up && shooterCounterPower<1.0) {
				shooterCounterPower+=0.001;
			} else if(gamepad1.dpad_down && shooterCounterPower>0.0) {
				shooterCounterPower-=0.001;
			}

			if(gamepad1.right_trigger>0.05) {
				//shooter.setVelocity(1450);
				shooterCounter.setPower(shooterCounterPower);
			} else {
				//shooter.setVelocity(0);
				shooterCounter.setPower(0.0);
			}
			*/

			if(gamepad1.x) {
				shooterLeft.setVelocity(1100);
				shooterRight.setVelocity(1100);
			} else {
				shooterLeft.setVelocity(0);
				shooterRight.setVelocity(0);
			}

			if(gamepad1.right_bumper) {
				intake.setPower(0.7);
			} else {
				intake.setPower(0.0);
			}

			if(gamepad1.dpad_right && indexerPower<1.0) {
				indexerPower+=0.001;
			} else if(gamepad1.dpad_left && indexerPower>0.0) {
				indexerPower-=0.001;
			}

			if(gamepad1.left_bumper) {
				indexer.setPower(indexerPower);
			} else {
				indexer.setPower(0.0);
			}

			telemetry.addData("Indexer Power:",indexerPower);
			telemetry.addData("Indexer Velocity",indexer.getVelocity());

			telemetry.addData("Shooter Velocity:",shooterLeft.getVelocity());
			telemetry.addData("Intake Velocity",intake.getVelocity());
			telemetry.update();
		}
	}
}