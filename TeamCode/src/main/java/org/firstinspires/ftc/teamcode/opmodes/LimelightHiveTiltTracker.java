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

@TeleOp(name = "Limelight Hive Tilt Tracker", group = "TeleOp")
public class LimelightHiveTiltTracker extends LinearOpMode {

	private Limelight3A limelight;

	// Adjust these threshold angles (in degrees) based on actual field testing
	private static final double TIPPING_THRESHOLD_MIN = 5.0;  // Starts to wobble
	private static final double TIPPED_THRESHOLD_MAX = 25.0; // Completely flipped

	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize the Limelight from the hardware configuration
		limelight = hardwareMap.get(Limelight3A.class, "limelight");
		telemetry.addData("Status", "Initializing Limelight...");
		telemetry.update();

		telemetry.setMsTransmissionInterval(11);
		limelight.pipelineSwitch(1); // Ensure index 1 matches your AprilTag pipeline
		limelight.start();

		telemetry.addData("Status", "Ready! Press Play.");
		telemetry.update();
		waitForStart();

		while (opModeIsActive()) {
			com.qualcomm.hardware.limelightvision.LLResult result = limelight.getLatestResult();

//			limelight.
//			LLStatus status = limelight.getStatus();
//			telemetry.addData("Status ", "Index is " + status.getPipelineIndex());

			if (result != null && result.isValid()) {
				List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();

				if (!aprilTags.isEmpty()) {
					LLResultTypes.FiducialResult targetTag = aprilTags.get(0);

					// FIXED: Corrected type retrieval from List<Double> to Pose3D
					Pose3D cameraSpacePose = targetTag.getTargetPoseCameraSpace();

					if (cameraSpacePose != null) {
						// Extract orientation angles
						YawPitchRollAngles orientation = cameraSpacePose.getOrientation();
						double currentPitch = Math.abs(orientation.getPitch(AngleUnit.DEGREES));
						int tagId = targetTag.getFiducialId();

						// --- Threshold State Logic ---
						String hiveState;
						if (currentPitch >= TIPPED_THRESHOLD_MAX) {
							hiveState = "⚠️ TIPPED!";
						} else if (currentPitch >= TIPPING_THRESHOLD_MIN) {
							hiveState = "🔄 TIPPING / WOBBLING";
						} else {
							hiveState = "✅ Stable (Not Tipped)";
						}

						// Send status to the driver station
						telemetry.addData("Tracking Tag ID", tagId);
						telemetry.addData("Hive State", hiveState);
						telemetry.addData("Measured Tilt (Pitch)", "%.2f°", currentPitch);
					} else {
						telemetry.addData("Status", "AprilTag seen, but 3D Pose data is unavailable.");
					}
				} else {
					telemetry.addData("Status", "No AprilTags currently in view.");
				}
			} else {
				if (result == null) {
					telemetry.addData("Status", "Limelight is searching for targets...");
				} else {
					telemetry.addData("Status", "Limelight result: " + result);
				}

			}

			telemetry.update();
			idle();
		}

		limelight.stop();
	}
}
