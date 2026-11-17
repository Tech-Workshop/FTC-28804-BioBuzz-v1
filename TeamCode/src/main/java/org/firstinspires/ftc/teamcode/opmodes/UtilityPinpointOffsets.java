package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Locale;

/**
 * Measures the goBILDA Pinpoint odometry pod offsets by spinning the robot in place by hand.
 *
 * How it works: when the robot turns by an angle theta (radians), a pod that sits a distance d
 * from the point the robot rotates about travels an arc of d * theta. Dividing the pod's travel
 * by theta gives d. Six full rotations each way makes the measurement large and averages out
 * small wobbles; the left and right spins are computed separately and then averaged.
 *
 * Pinpoint convention (X forward, Y left, heading counterclockwise-positive):
 *   X pod offset: + = pod is LEFT of the tracking point, - = RIGHT of it
 *   Y pod offset: + = pod is FORWARD of the tracking point, - = BEHIND it
 *
 * Procedure:
 *   1. Before pressing start, check the live X/Y ticks (push forward: X up, push left: Y up).
 *   2. Press start, spin the robot LEFT (counterclockwise) 6 rotations, hold still, press A.
 *   3. Spin the robot RIGHT (clockwise) 6 rotations, hold still, press A.
 *   4. Read the offsets from telemetry.
 *
 * Spin the robot about the point you want Pinpoint to track (normally the robot's center).
 */
@TeleOp(name = "Utility: Pinpoint Offsets", group = "Utility")
public class UtilityPinpointOffsets extends LinearOpMode {

	// ------------------------------ Configuration ------------------------------
	private static final String PINPOINT_NAME = "pinpoint";   // name in the robot configuration
	private static final int ROTATIONS = 6;                   // full rotations in each direction

	// Encoder ticks per mm of wheel travel:
	//   goBILDA 4-bar odometry pod  = 19.89436789
	//   goBILDA swingarm pod        = 13.26291192
	private static final double TICKS_PER_MM = 19.89436789;

	// Flip these if the live ticks go the wrong way (X should rise pushing the robot
	// forward, Y should rise pushing the robot left).
	private static final boolean INVERT_X = true;
	private static final boolean INVERT_Y = true;
	// ---------------------------------------------------------------------------

	private GoBildaPinpointDriver pinpoint;

	@Override
	public void runOpMode() {
		pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, PINPOINT_NAME);

		// Before start: show live ticks so the X/Y signs can be verified.
		while (!isStarted() && !isStopRequested()) {
			double[] t = readTicks();
			telemetry.addLine("SIGN CHECK (before pressing start)");
			telemetry.addLine("Push the robot FORWARD: X ticks must increase.");
			telemetry.addLine("Push the robot LEFT: Y ticks must increase.");
			telemetry.addLine("If not, flip INVERT_X / INVERT_Y in the code.");
			telemetry.addData("X ticks", "%.0f", t[0]);
			telemetry.addData("Y ticks", "%.0f", t[1]);
			telemetry.update();
		}
		if (isStopRequested()) return;

		double[] start = readTicks();

		waitForA("STEP 1 of 2",
			"Spin the robot LEFT (counterclockwise, viewed from above) exactly "
				+ ROTATIONS + " full rotations about its center.",
			"Hold it still, then press A on gamepad1.");
		double[] afterLeft = readTicks();

		waitForA("STEP 2 of 2",
			"Now spin the robot RIGHT (clockwise) exactly "
				+ ROTATIONS + " full rotations, back to where it started.",
			"Hold it still, then press A on gamepad1.");
		double[] afterRight = readTicks();

		if (isStopRequested()) return;

		// Travel of each pod during each spin, in mm.
		double dxLeft = (afterLeft[0] - start[0]) / TICKS_PER_MM;
		double dyLeft = (afterLeft[1] - start[1]) / TICKS_PER_MM;
		double dxRight = (afterRight[0] - afterLeft[0]) / TICKS_PER_MM;
		double dyRight = (afterRight[1] - afterLeft[1]) / TICKS_PER_MM;

		double theta = ROTATIONS * 2.0 * Math.PI;   // size of each spin in radians

		// Left spin is +theta (counterclockwise): X pod reads -xOffset * theta, Y pod reads +yOffset * theta.
		// Right spin is -theta (clockwise):       X pod reads +xOffset * theta, Y pod reads -yOffset * theta.
		double xOffsetFromLeft = -dxLeft / theta;
		double xOffsetFromRight = dxRight / theta;
		double yOffsetFromLeft = dyLeft / theta;
		double yOffsetFromRight = -dyRight / theta;

		double xOffset = (xOffsetFromLeft + xOffsetFromRight) / 2.0;
		double yOffset = (yOffsetFromLeft + yOffsetFromRight) / 2.0;

		String xSide = xOffset >= 0 ? "left of center" : "right of center";
		String ySide = yOffset >= 0 ? "forward of center" : "behind center";

		telemetry.addLine("=== RESULTS ===");
		telemetry.addData("X pod offset", "%.2f mm (%.3f in), %s", xOffset, xOffset / 25.4, xSide);
		telemetry.addData("Y pod offset", "%.2f mm (%.3f in), %s", yOffset, yOffset / 25.4, ySide);
		telemetry.addLine("");
		telemetry.addLine("Left spin vs right spin (should agree within ~2 mm, else redo):");
		telemetry.addData("X", "%.2f / %.2f mm", xOffsetFromLeft, xOffsetFromRight);
		telemetry.addData("Y", "%.2f / %.2f mm", yOffsetFromLeft, yOffsetFromRight);
		telemetry.addLine("");
		telemetry.addLine(String.format(Locale.US,
			"pinpoint.setOffsets(%.2f, %.2f, DistanceUnit.MM);", xOffset, yOffset));
		telemetry.update();

		// Keep the results on screen until the OpMode is stopped.
		while (opModeIsActive()) {
			idle();
		}
	}

	/** Waits until A is pressed on gamepad1, showing the instructions and live ticks meanwhile. */
	private void waitForA(String title, String... instructions) {
		// Let go of the button from the previous step first.
		while (opModeIsActive() && gamepad1.a) {
			idle();
		}
		while (opModeIsActive() && !gamepad1.a) {
			double[] t = readTicks();
			telemetry.addLine(title);
			for (String line : instructions) {
				telemetry.addLine(line);
			}
			telemetry.addData("X ticks", "%.0f", t[0]);
			telemetry.addData("Y ticks", "%.0f", t[1]);
			telemetry.update();
		}
	}

	/** Reads the raw X and Y encoder ticks from the Pinpoint, applying the invert flags. */
	private double[] readTicks() {
		pinpoint.update();
		double x = pinpoint.getEncoderX();
		double y = pinpoint.getEncoderY();
		return new double[]{INVERT_X ? -x : x, INVERT_Y ? -y : y};
	}
}