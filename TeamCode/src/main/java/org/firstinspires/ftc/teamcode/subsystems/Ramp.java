package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Ramp {
	private final Servo leftRampServo,rightRampServo;
	private final Telemetry telemetry;

	private double rampPos;
	private double rampTarget=-1.0; //Purposely a bad value to start, also for right only

	public enum RAMP_STATUS {
		UNKNOWN,
		UP,
		DOWN,
		MOVING
	}

	private RAMP_STATUS rampStatus=RAMP_STATUS.UNKNOWN;

	public Ramp(HardwareMap hardwareMap, Telemetry telemetry) {
		this.leftRampServo=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_LEFT_ID);
		this.rightRampServo=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_RIGHT_ID);

		this.telemetry=telemetry;
	}

	public void initializeUp() {
		rampStatus=RAMP_STATUS.UP;
		rampTarget=rampPos=Constants.Ramp.RAMP_UP_POS;
		setPosition(rampPos);
	}

	public void raise() {
		rampTarget = Constants.Ramp.RAMP_UP_POS;
		if(Math.abs(rampTarget-rampPos)>=0.01) {
			rampStatus= RAMP_STATUS.MOVING;
		}
	}

	public void lower() {
		rampTarget = Constants.Ramp.RAMP_DOWN_POS;
		if(Math.abs(rampTarget-rampPos)>=0.01) {
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
			double error = rampTarget - rampPos;
			double distance = Math.abs(error);

			double speedScale = Math.min(1.0, distance / Constants.Ramp.RAMP_SLOWDOWN_DISTANCE);
			double step = Constants.Ramp.RAMP_INCREMENT * Math.max(speedScale, Constants.Ramp.RAMP_MIN_SCALE);

			if (distance <= step) {
				rampPos = rampTarget;
			} else {
				rampPos += Math.signum(error) * step;
			}
			setPosition(rampPos); //Set the position of both ramp servos
		}
	}

	public double getPosition() {
		return rampPos;
	}

	public double getTarget() {
		return rampTarget;
	}

	public RAMP_STATUS getStatus() {
		return rampStatus;
	}

	public boolean isDone() {
		if(rampStatus==RAMP_STATUS.UP || rampStatus==RAMP_STATUS.DOWN) {
			return true;
		} else {
			return false;
		}
	}

	public void setPosition(double position) {
		leftRampServo.setPosition(position);
		rightRampServo.setPosition(position);
	}
}
