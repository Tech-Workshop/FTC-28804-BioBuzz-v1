package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="BLUE-Manual")
public class ManualBlue extends ManualBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.BLUE;
		defaultStart=new Pose(132.5,7.8,Math.toRadians(180.0));
		//overridePose=new Pose(132.5,7.8, Math.toRadians(180.0));
		overridePose=new Pose(132.8,83.5, Math.toRadians(180.0));
	}
}
