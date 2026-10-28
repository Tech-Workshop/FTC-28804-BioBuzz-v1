package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Motors Test")
public class UtilityMotors extends LinearOpMode {
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,shooter,shooterCounter,intake,indexer;

	@Override
	public void runOpMode() throws InterruptedException {
		leftFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_FRONT_ID);
		leftBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_BACK_ID);
		rightFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_FRONT_ID);
		rightBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_BACK_ID);

		shooter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_ID);
		shooter.setDirection(Constants.Shooter.SHOOTER_DIRECTION);
		shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		shooterCounter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_COUNTER_ID);
		shooterCounter.setDirection(Constants.Shooter.SHOOTER_COUNTER_DIRECTION);
		shooterCounter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooterCounter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		intake = hardwareMap.get(DcMotorEx.class, Constants.Intake.INTAKE_ID);
		intake.setDirection(Constants.Intake.INTAKE_DIRECTION);
		indexer = hardwareMap.get(DcMotorEx.class, Constants.Indexer.INDEXER_ID);
		indexer.setDirection(Constants.Indexer.INDEXER_DIRECTION);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

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

			if(gamepad1.b) {
				shooter.setVelocity(1450);
				//shooter.setPower(0.75);
			} else {
				shooter.setVelocity(0);
				//shooter.setPower(0.0);
			}

			if(gamepad1.x) {
				shooterCounter.setVelocity(800);
				//shooterCounter.setPower(0.80);
			} else {
				shooterCounter.setVelocity(0);
				//shooterCounter.setPower(0.0);
			}

			if(gamepad1.right_bumper) {
				intake.setPower(0.7);
			} else {
				intake.setPower(0.0);
			}

			if(gamepad1.left_bumper) {
				indexer.setPower(0.7);
			} else {
				indexer.setPower(0.0);
			}

			telemetry.addData("Shooter Velocity:",shooter.getVelocity());
			telemetry.addData("Counter Velocity:",shooterCounter.getVelocity());
			telemetry.addData("Intake Velocity",intake.getVelocity());
			telemetry.addData("Indexer Velocity",indexer.getVelocity());
			telemetry.update();
		}
	}
}