package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Constants;

@Autonomous(name="[A] BLUE-Park", preselectTeleOp = "BLUE-Manual")
public class AutonParkBlue extends AutonParkBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.BLUE;
	}
}
