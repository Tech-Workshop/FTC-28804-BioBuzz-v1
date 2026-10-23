package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Intake {
	private final DcMotorEx intakeMotor;
	private final Servo leftIntakeArm,rightIntakeArm;
	private final Telemetry telemetry;

	private double armPos; //Note this is for right only, left is inverse (1-armPos)
	private double armTarget=-1.0; //Purposely a bad value to start, also for right only

	enum ARM_STATUS {
		IDLE,
		MOVING
	}

	private ARM_STATUS armStatus=ARM_STATUS.IDLE;

	public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
		this.intakeMotor=hardwareMap.get(DcMotorEx.class, Constants.Intake.INTAKE_ID);
		intakeMotor.setDirection(Constants.Intake.INTAKE_DIRECTION);
		intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		this.leftIntakeArm=hardwareMap.get(Servo.class, Constants.Intake.INTAKE_ARM_LEFT_ID);
		this.rightIntakeArm=hardwareMap.get(Servo.class, Constants.Intake.INTAKE_ARM_RIGHT_ID);

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

	public void retractArm() {
		if(armTarget!=armPos) {
			armTarget = Constants.Intake.INTAKE_ARM_RETRACTED_POS;
			armStatus=ARM_STATUS.MOVING;
		}
	}

	public void extendArm() {
		if(armTarget!=armPos) {
			armTarget = Constants.Intake.INTAKE_ARM_EXTENDED_POS;
			armStatus=ARM_STATUS.MOVING;
		}
	}

	public void armPeriodic() {
		if(Math.abs(armTarget-armPos)<0.01) {
			armStatus=ARM_STATUS.IDLE;
		} else {
			double increment = Math.signum(armTarget - armPos);
			armPos+=increment*Constants.Intake.INTAKE_ARM_INCREMENT;

			leftIntakeArm.setPosition((1.0-armPos));
			rightIntakeArm.setPosition(armPos);
		}
	}

	public double getArmPosition() {
		return armPos;
	}
	public double getArmTarget() {
		return armTarget;
	}

	public boolean isArmDone() {
		if(armStatus==ARM_STATUS.IDLE) {
			return true;
		} else {
			return false;
		}
	}

}
