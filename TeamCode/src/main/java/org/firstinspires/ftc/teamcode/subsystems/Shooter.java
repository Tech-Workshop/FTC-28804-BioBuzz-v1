package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kP;
import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kS;
import static org.firstinspires.ftc.teamcode.Constants.Shooter.SHOOTER_PID_kV;
import static org.firstinspires.ftc.teamcode.utility.MathUtility.clamp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Shooter {
	private final HardwareMap hardwareMap;
	private final DcMotorEx shooterLeftMotor,shooterRightMotor;
	private final Servo ballstopServo,hoodServo;
	private final Telemetry telemetry;

	private double ballstopClosedPos,ballstopOpenPos,shotVelocity;
	private double targetVelocity=0.0;
	private double rampRate=Constants.Shooter.SHOOTER_RAMP_RATE;
	private double hoodPos=Constants.Shooter.HOOD_DEFAULT;
	private double lastPower=0.0; //Used for power ramping
	private double restingBatteryVoltage=Constants.Shooter.NOMINAL_VOLTAGE;

	public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
		this.hardwareMap = hardwareMap;

		//Get shooters
		this.shooterLeftMotor = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_LEFT_ID);
		shooterLeftMotor.setDirection(Constants.Shooter.SHOOTER_LEFT_DIRECTION);

		this.shooterRightMotor = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_RIGHT_ID);
		shooterRightMotor.setDirection(Constants.Shooter.SHOOTER_RIGHT_DIRECTION);

		shotVelocity=Constants.Shooter.SHOOTER_VELOCITY;

		//Get ball stop
		this.ballstopServo = hardwareMap.get(Servo.class, Constants.Shooter.BALLSTOP_ID);
		ballstopClosedPos=Constants.Shooter.BALLSTOP_BLOCK;
		ballstopOpenPos=Constants.Shooter.BALLSTOP_CLEAR;

		//Get hood
		this.hoodServo = hardwareMap.get(Servo.class, Constants.Shooter.HOOD_ID);
		this.telemetry=telemetry;

		//Initialize resting battery voltage
		double initialVoltage = getRawBatteryVoltage();
		if (initialVoltage > 0) {
			this.restingBatteryVoltage = initialVoltage;
		}
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
		shooterLeftMotor.setPower(0.0);
		shooterRightMotor.setPower(0.0);
	}

	public double getTargetVelocity() {
		return targetVelocity;
	}

	public void setShotVelocity(double velocity) {
		shotVelocity=velocity;
	}

	public double getShotVelocity() {
		return shotVelocity;
	}

	public void adjustShotVelocity(double increment) {
		setShotVelocity(shotVelocity+increment);
	}

	//s=sideways distance from target, v=vertical distance from target
	public void setShotVelocityUsingDistancesFromTarget(double s, double v) {
		double velocity = 1529.77 + 2.583 * v - 0.13931 * s + 0.06297 * s * s; //Formula from Claude, given optimized observed values
		setShotVelocity(velocity);
	}

	public void setTargetVelocity(double velocity) {
		targetVelocity=velocity;
	}

	public double getVelocity() {
		return shooterLeftMotor.getVelocity();
	}

	public double getRawBatteryVoltage() {
		double minVoltage = Double.MAX_VALUE;
		if (hardwareMap != null && hardwareMap.voltageSensor != null) {
			for (VoltageSensor sensor : hardwareMap.voltageSensor) {
				double voltage = sensor.getVoltage();
				if (voltage > 0) {
					minVoltage = Math.min(minVoltage, voltage);
				}
			}
		}
		return (minVoltage == Double.MAX_VALUE) ? Constants.Shooter.NOMINAL_VOLTAGE : minVoltage;
	}

	public void updateBatteryVoltage() {
		double rawV = getRawBatteryVoltage();
		if (rawV <= 0) return;

		if (restingBatteryVoltage <= 0 || rawV > restingBatteryVoltage) {
			restingBatteryVoltage = rawV;
		} else if (targetVelocity == 0 && rawV >= 0.92 * restingBatteryVoltage) {
			// Update resting voltage when shooter is idle and not under heavy motor sag
			restingBatteryVoltage = 0.95 * restingBatteryVoltage + 0.05 * rawV;
		}
	}

	public double getBatteryVoltage() {
		if (restingBatteryVoltage <= 0) {
			return getRawBatteryVoltage();
		}
		return restingBatteryVoltage;
	}

	public double getVoltageCompensatedVelocity(double baseVelocity) {
		if (!Constants.Shooter.ENABLE_VOLTAGE_COMPENSATION) {
			return baseVelocity;
		}
		double currentVoltage = getBatteryVoltage();
		if (currentVoltage <= 0) {
			return baseVelocity;
		}
		// Scale factor = NOMINAL_VOLTAGE / currentVoltage
		// If currentVoltage > NOMINAL_VOLTAGE (e.g., 13.5V > 12.0V), factor < 1.0 -> slower speed
		// If currentVoltage < NOMINAL_VOLTAGE (e.g., 10.5V < 12.0V), factor > 1.0 -> faster speed
		double factor = Constants.Shooter.NOMINAL_VOLTAGE / currentVoltage;

		return baseVelocity * factor;
	}

	public double getCompensatedTargetVelocity() {
		return getVoltageCompensatedVelocity(targetVelocity);
	}

	public void lowerHood() {
		if(hoodPos>Constants.Shooter.HOOD_MIN)  {
			hoodPos-=0.01;
			hoodServo.setPosition(hoodPos);
		}
	}
	public void raiseHood() {
		if(hoodPos<Constants.Shooter.HOOD_MAX)  {
			hoodPos+=0.01;
			hoodServo.setPosition(hoodPos);
		}
	}

	public double getHoodPosition() {
		return hoodPos;
	}

	public void setHoodPosition(double position) {
		hoodServo.setPosition(Math.clamp(position,Constants.Shooter.HOOD_MIN,Constants.Shooter.HOOD_MAX));
	}

	public void setHoodPositionUsingDistancesFromTarget(double s, double v) {
		double position = 0.36150 + 0.000715 * v - 0.004788 * s + 0.00001349 * s * s + 0.0001978 * v * s;
		position=Math.clamp(position,Constants.Shooter.HOOD_MIN,Constants.Shooter.HOOD_MAX);
		setHoodPosition(position);
	}

	public void periodic() {
		updateBatteryVoltage();

		if (telemetry != null && Constants.General.DEBUG_MODE) {
			telemetry.addData("Shooter Rest Voltage", "%.2f V", getBatteryVoltage());
			telemetry.addData("Shooter Raw Voltage", "%.2f V", getRawBatteryVoltage());
		}

		telemetry.addData("Shooter Subsystem Target:",targetVelocity);

		if(targetVelocity>0) {
			double effectiveTarget=targetVelocity;
			if(Constants.Shooter.ENABLE_VOLTAGE_COMPENSATION) {
				effectiveTarget = getCompensatedTargetVelocity();
				telemetry.addData("Shooter Comp Target", "%.1f", effectiveTarget);
			}
			double shooterPower=clamp(shooterPIDCalculate(getVelocity(),effectiveTarget),0,1);

			//Ramp power
			if(rampRate>0.0 && shooterPower>lastPower) {
				shooterPower=clamp(shooterPower,lastPower-rampRate,lastPower+rampRate);
			}
			lastPower=shooterPower;

			//Set motor power
			shooterLeftMotor.setPower(shooterPower);
			shooterRightMotor.setPower(shooterPower);
		}
	}

	public double shooterPIDCalculate(double currentVelocity,double goalVelocity) {
		double feedForward=(SHOOTER_PID_kV*goalVelocity)+SHOOTER_PID_kS;
		double error=goalVelocity-currentVelocity;

		return (error*SHOOTER_PID_kP)+feedForward;
	}

	public boolean speedWithinError() {
		double effectiveTarget=targetVelocity;
		if(Constants.Shooter.ENABLE_VOLTAGE_COMPENSATION) {
			effectiveTarget = getCompensatedTargetVelocity();
		}

		if(Math.abs(getVelocity()-effectiveTarget)<Constants.Shooter.SHOOTER_ERROR_ALLOWED) {
			return true;
		} else {
			return false;
		}
	}
}
