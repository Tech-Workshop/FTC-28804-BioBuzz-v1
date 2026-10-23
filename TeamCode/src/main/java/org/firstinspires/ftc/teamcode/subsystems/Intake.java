package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Intake {
	private final DcMotorEx intakeMotor;
	private final Telemetry telemetry;

	public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
		this.intakeMotor=hardwareMap.get(DcMotorEx.class, Constants.Intake.INTAKE_ID);
		intakeMotor.setDirection(Constants.Intake.INTAKE_DIRECTION);
		intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		this.telemetry=telemetry;
	}

	public void intake() {
		intakeMotor.setVelocity(Constants.Intake.INTAKE_IN_SPEED);
	}

	public void expel() {
		intakeMotor.setVelocity(Constants.Intake.INTAKE_OUT_SPEED);
	}

	public void stop() {
		intakeMotor.setPower(0.0);
	}

	public double getVelocity() {
		return intakeMotor.getVelocity();
	}
}
