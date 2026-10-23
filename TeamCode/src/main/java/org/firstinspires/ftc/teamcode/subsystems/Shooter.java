package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kP;
import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kS;
import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kV;
import static org.firstinspires.ftc.teamcode.utility.MathUtility.clamp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Shooter {
	private final DcMotorEx shooterMotor;
	private final Servo ballstopServo;
	private final Telemetry telemetry;

	private double ballstopClosedPos,ballstopOpenPos,shotVelocity;
	private double targetVelocity=0.0;
	private double rampRate=Constants.Shooter.SHOOTER_RAMP_RATE;
	private double lastPower=0.0; //Used for power ramping

	public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
		//Get shooter
		this.shooterMotor = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_ID);
		shooterMotor.setDirection(Constants.Shooter.SHOOTER_DIRECTION);
		shotVelocity=Constants.Shooter.SHOOTER_VELOCITY;

		//Get ball stop
		this.ballstopServo = hardwareMap.get(Servo.class, Constants.Shooter.BALLSTOP_ID);
		ballstopClosedPos=Constants.Shooter.BALLSTOP_BLOCK;
		ballstopOpenPos=Constants.Shooter.BALLSTOP_CLEAR;

		this.telemetry=telemetry;
	}

	public void closeGate() {
		ballstopServo.setPosition(ballstopClosedPos);
	}

	public void openGate() {
		ballstopServo.setPosition(ballstopOpenPos);
	}

	public void spinFlywheel() {
		setTargetVelocity(shotVelocity);
	}

	public void spinFlywheelAtVelocity(double velocity) {
		setTargetVelocity(velocity);
	}

	public void stopFlywheel() {
		setTargetVelocity(0.0);
		shooterMotor.setPower(0.0);
	}

	public double getTargetVelocity() {
		return targetVelocity;
	}

	public void setTargetVelocity(double velocity) {
		targetVelocity=velocity;
	}

	public double getVelocity() {
		return shooterMotor.getVelocity();
	}

	public void periodic() {
		if(targetVelocity>0) {
			double shooterPower=clamp(shooterPIDCalculate(getVelocity(),targetVelocity),0,1);

			//Ramp power
			if(rampRate>0.0 && shooterPower>lastPower) {
				shooterPower=clamp(shooterPower,lastPower-rampRate,lastPower+rampRate);
			}
			lastPower=shooterPower;

			//Set motor power
			shooterMotor.setPower(shooterPower);
		}
	}

	public double shooterPIDCalculate(double currentVelocity,double goalVelocity) {
		double feedForward=(SHOOTER_PID_kV*goalVelocity)+SHOOTER_PID_kS;
		double error=goalVelocity-currentVelocity;

		return (SHOOTER_PID_kP)+feedForward;
	}

	public boolean speedWithinError() {
		if(Math.abs(getVelocity()-targetVelocity)<200) {
			return true;
		} else {
			return false;
		}
	}
}
