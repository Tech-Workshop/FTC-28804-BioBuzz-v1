package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name="[A] RED-Default", preselectTeleOp = "RED-Manual")
public class AutonDefaultRed extends AutonDefaultBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.RED;
	}
}
