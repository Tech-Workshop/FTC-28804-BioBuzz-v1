package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Sensors {
	private final DigitalChannel beamBreakSensor,ledLeftGreen,ledLeftRed,ledRightGreen,ledRightRed;
	private final Telemetry telemetry;

	public Sensors(HardwareMap hardwareMap, Telemetry telemetry) {
		this.beamBreakSensor=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		beamBreakSensor.setMode(DigitalChannel.Mode.INPUT);

		this.ledLeftGreen=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		ledLeftGreen.setMode(DigitalChannel.Mode.OUTPUT);
		this.ledLeftRed=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		ledLeftRed.setMode(DigitalChannel.Mode.OUTPUT);
		this.ledRightGreen=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		ledRightGreen.setMode(DigitalChannel.Mode.OUTPUT);
		this.ledRightRed=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		ledRightRed.setMode(DigitalChannel.Mode.OUTPUT);

		this.telemetry=telemetry;
	}

	public void setLedDisable() {
		ledLeftGreen.setState(false);
		ledLeftRed.setState(false);
		ledRightGreen.setState(false);
		ledRightRed.setState(false);
	}

	public void setLedGreen() {
		ledLeftGreen.setState(true);
		ledLeftRed.setState(false);
		ledRightGreen.setState(true);
		ledRightRed.setState(false);
	}

	public void setLedRed() {
		ledLeftGreen.setState(false);
		ledLeftRed.setState(true);
		ledRightGreen.setState(false);
		ledRightRed.setState(true);
	}

	public boolean getBeamBreakState() {
		return beamBreakSensor.getState();
	}
}
