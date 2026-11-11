package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

@Disabled
@TeleOp(group = "UTILITY",name="Utility: Shooter kS")
public class UtilityShooterTunerKs extends OpMode {

	private DcMotorEx shooterLeft,shooterRight;
	double[] increments = { 0.000001, 0.00001, 0.0001, 0.001, 0.01 };
	int incrementIndex = 4;
	double kS=0.00;

	@Override
	public void init() {
		shooterLeft = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_LEFT_ID);
		shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		shooterLeft.setDirection(Constants.Shooter.SHOOTER_LEFT_DIRECTION);

		shooterRight = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_RIGHT_ID);
		shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		shooterRight.setDirection(Constants.Shooter.SHOOTER_RIGHT_DIRECTION);
	}

	@Override
	public void loop() {
		if(gamepad1.dpadRightWasPressed() && incrementIndex<4) {
			incrementIndex++;
		} else if(gamepad1.dpadLeftWasPressed() && incrementIndex>0) {
			incrementIndex--;
		}

		double currentStep=increments[incrementIndex];

		if(gamepad1.dpadUpWasPressed()) { kS += currentStep; }
		if(gamepad1.dpadDownWasPressed()) { kS -= currentStep; }

		shooterLeft.setPower(kS);
		shooterRight.setPower(kS);

		telemetry.addData("kS","%.6f",kS);
		telemetry.addData("Flywheel Velocity",shooterLeft.getVelocity());
		telemetry.addData("Increment Index",incrementIndex);
		telemetry.addData("Increment",increments[incrementIndex]);
	}
}