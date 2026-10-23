package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Sorter {
	private final Servo sorterServo;
	private final Telemetry telemetry;

	public Sorter(HardwareMap hardwareMap, Telemetry telemetry) {
		this.sorterServo=hardwareMap.get(Servo.class, Constants.Sorter.SORTER_ID);
		this.telemetry=telemetry;
	}

	public void sort() {
		sorterServo.setPosition(Constants.Sorter.SORTER_EXTEND);
	}

	public void home() {
		sorterServo.setPosition(Constants.Sorter.SORTER_RETRACT);
	}
}
