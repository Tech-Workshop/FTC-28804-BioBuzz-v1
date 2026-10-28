package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Indexer {
	private final DcMotorEx indexerMotor;
	private final Telemetry telemetry;

	public Indexer(HardwareMap hardwareMap, Telemetry telemetry) {
		this.indexerMotor=hardwareMap.get(DcMotorEx.class, Constants.Indexer.INDEXER_ID);
		indexerMotor.setDirection(Constants.Indexer.INDEXER_DIRECTION);
		indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		indexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		this.telemetry=telemetry;
	}

	public void shootSpeed() {
		indexerMotor.setVelocity(Constants.Indexer.INDEXER_SHOOT_SPEED);
	}

	public void intakeSpeed() {
		indexerMotor.setVelocity(Constants.Indexer.INDEXER_INTAKE_SPEED);
	}

	public void reverse() {
		indexerMotor.setVelocity(Constants.Indexer.INDEXER_REVERSE_SPEED);
	}

	public void stop() {
		indexerMotor.setPower(0.0);
	}

	public double getVelocity() {
		return indexerMotor.getVelocity();
	}
}
