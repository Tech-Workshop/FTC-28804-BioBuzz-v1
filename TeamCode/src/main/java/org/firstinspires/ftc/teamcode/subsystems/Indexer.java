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
		indexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		this.telemetry=telemetry;
	}

	public void index() {
		indexerMotor.setVelocity(Constants.Indexer.INDEXER_INDEX_SPEED);
	}

	public void sortSpeed() {
		indexerMotor.setVelocity(Constants.Indexer.INDEXER_SORT_SPEED);
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
