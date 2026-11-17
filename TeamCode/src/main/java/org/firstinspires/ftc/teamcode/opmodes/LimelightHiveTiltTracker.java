package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.List;

@TeleOp(name = "Utility: Limelight April Tags", group = "UTILITY")
public class LimelightHiveTiltTracker extends LinearOpMode {

	private Limelight3A limelight;
	private int pipelineIndex=0;

	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize the Limelight from the hardware configuration
		limelight = hardwareMap.get(Limelight3A.class, "limelight");
		telemetry.addData("Status", "Initializing Limelight...");
		telemetry.update();

		telemetry.setMsTransmissionInterval(11);
		limelight.pipelineSwitch(pipelineIndex); // Ensure index 0 matches your AprilTag pipeline
		limelight.start();

		telemetry.addData("Status", "Ready! Press Play.");
		telemetry.update();
		waitForStart();

		while (opModeIsActive()) {
			if(gamepad1.dpadUpWasPressed()) {
				pipelineIndex++;
				limelight.pipelineSwitch(pipelineIndex); // Ensure index 0 matches your AprilTag pipeline
			} else if(gamepad1.dpadDownWasPressed()) {
				pipelineIndex--;
				limelight.pipelineSwitch(pipelineIndex); // Ensure index 0 matches your AprilTag pipeline
			}

			com.qualcomm.hardware.limelightvision.LLResult result = limelight.getLatestResult();

			LLStatus status = limelight.getStatus();
			telemetry.addData("Pipeline Index: ", status.getPipelineIndex());

			telemetry.addLine("Checking for results...");
			if (result != null && result.isValid()) {
				List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();

				if (!aprilTags.isEmpty()) {
					LLResultTypes.FiducialResult targetTag = aprilTags.get(0);
					int tagId = targetTag.getFiducialId();

					telemetry.addLine("At least one April Tag found");
					telemetry.addData("April Tag ID:",tagId);
				} else {
					telemetry.addData("Status", ">> no AprilTags in view <<");
				}
			} else {
				telemetry.addLine(">> Result is null or invalid <<");
				if (result == null) {
					telemetry.addLine("NULL");
				} else {
					telemetry.addLine("RESULT NOT VALID");
					telemetry.addData("Status", "Limelight result: " + result);
				}

			}

			telemetry.update();
			idle();
		}

		limelight.stop();
	}
}
