package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name="[A] RED-Park", preselectTeleOp = "RED-Manual")
public class AutonParkRed extends AutonParkBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.RED;
	}
}
