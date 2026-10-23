package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Ramp {
	private final Servo leftRampServo,rightRampServo;
	private final Telemetry telemetry;

	private double rampPos; //Note this is for right only, left is inverse (1-armPos)
	private double rampTarget=-1.0; //Purposely a bad value to start, also for right only

	enum RAMP_STATUS {
		UNKNOWN,
		UP,
		DOWN,
		MOVING
	}

	private RAMP_STATUS rampStatus= RAMP_STATUS.UNKNOWN;

	public Ramp(HardwareMap hardwareMap, Telemetry telemetry) {
		this.leftRampServo=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_LEFT_ID);
		this.rightRampServo=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_RIGHT_ID);

		this.telemetry=telemetry;
	}

	public void raise() {
		if(rampTarget!=rampPos) {
			rampTarget = Constants.Ramp.RAMP_UP_POS;
			rampStatus= RAMP_STATUS.MOVING;
		}
	}

	public void lower() {
		if(rampTarget!=rampPos) {
			rampTarget = Constants.Ramp.RAMP_DOWN_POS;
			rampStatus= RAMP_STATUS.MOVING;
		}
	}

	public void periodic() {
		if(Math.abs(rampTarget-rampPos)<0.01) {
			if(rampTarget==Constants.Ramp.RAMP_DOWN_POS) {
				rampStatus = RAMP_STATUS.DOWN;
			} else {
				rampStatus = RAMP_STATUS.UP;
			}
		} else {
			double increment = Math.signum(rampTarget - rampPos);
			rampPos+=increment*Constants.Ramp.RAMP_INCREMENT;

			leftRampServo.setPosition((1.0-rampPos));
			rightRampServo.setPosition(rampPos);
		}
	}

	public double getPosition() {
		return rampPos;
	}
	public double getTarget() {
		return rampTarget;
	}

	public boolean isDone() {
		if(rampStatus==RAMP_STATUS.UP || rampStatus==RAMP_STATUS.DOWN) {
			return true;
		} else {
			return false;
		}
	}
}
