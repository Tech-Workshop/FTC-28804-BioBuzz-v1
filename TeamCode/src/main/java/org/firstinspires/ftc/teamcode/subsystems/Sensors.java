package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Sensors {
	private final DigitalChannel beamBreakSensor;
	private final LED ledLeftGreen,ledLeftRed,ledRightGreen,ledRightRed;
	private final Limelight3A limelight;
	private final Telemetry telemetry;

	private boolean limelightHitFlag=false;
	private boolean limelightCollectFlag=false;

	public Sensors(HardwareMap hardwareMap, Telemetry telemetry) {
		this.beamBreakSensor=hardwareMap.get(DigitalChannel.class, Constants.Sensors.BEAM_BREAK_ID);
		beamBreakSensor.setMode(DigitalChannel.Mode.INPUT);

		this.ledLeftGreen=hardwareMap.get(LED.class, Constants.Sensors.LED_LEFT_GREEN_ID);
		this.ledLeftRed=hardwareMap.get(LED.class, Constants.Sensors.LED_LEFT_RED_ID);
		this.ledRightGreen=hardwareMap.get(LED.class, Constants.Sensors.LED_RIGHT_GREEN_ID);
		this.ledRightRed=hardwareMap.get(LED.class, Constants.Sensors.LED_RIGHT_RED_ID);

		this.limelight = hardwareMap.get(Limelight3A.class, Constants.Sensors.LIMELIGHT_ID);

		this.telemetry=telemetry;
	}

	//Enable Limelight
	public void limelightEnable() {
		limelight.start();
	}

	//Limelight - Set pipeline
	public void setLimelightPipeline(int pipeline) {
		limelight.pipelineSwitch(pipeline);
	}

	//Stop Limelight
	public void limelightStop() {
		limelight.stop();
		limelightCollectFlag=false;
	}

	//Start collecting april tag data
	public void limelightStartCollecting() {
		limelightCollectFlag = true;
	}

	public boolean limelightGetCollectStatus() {
		return limelightCollectFlag;
	}

	//Limelight - collect April Tag data and set flag if at least one is gathered
	public void limelightCollect() {

		telemetry.addLine("================================");

		LLStatus status = limelight.getStatus();
		telemetry.addData("Status ", "Index is " + status.getPipelineIndex());

		telemetry.addLine("Limelight - Collecting Data...");
		LLResult result = limelight.getLatestResult();
		if (result != null && result.isValid()) {
			telemetry.addLine("HIT - TRUE DAT!");
			limelightHitFlag=true;
		} else {
			if(result==null) {
				telemetry.addLine("MISS - result is null");
			} else {
				telemetry.addLine("MISS - result is not valid");
			}
		}
		telemetry.addLine("================================");
	}

	public boolean limelightHit() {
		return limelightHitFlag;
	}

	public void setLedDisable() {
		ledLeftGreen.off();
		ledLeftRed.off();
		ledRightGreen.off();
		ledRightRed.off();
	}

	public void setLedGreen() {
		ledLeftGreen.on();
		ledLeftRed.off();
		ledRightGreen.on();
		ledRightRed.off();
	}

	public void setLedRed() {
		ledLeftGreen.off();
		ledLeftRed.on();
		ledRightGreen.off();
		ledRightRed.on();
	}

	public boolean getBeamBreakState() {
		return beamBreakSensor.getState();
	}
}
