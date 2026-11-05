package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utility.PoseStorage;

@TeleOp(group = "UTILITY",name="Utility: Pose Reset")
public class UtilityPoseReset extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		PoseStorage.currentPose=null;
		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {
			telemetry.addLine("Pose  has been reset");
			telemetry.update();

			requestOpModeStop();
		}
	}
}
