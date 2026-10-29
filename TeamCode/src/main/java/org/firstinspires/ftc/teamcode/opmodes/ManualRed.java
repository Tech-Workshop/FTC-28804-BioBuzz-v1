package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="RED-Manual")
public class ManualRed extends ManualBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.RED;
		//tAlliance="RED";
		//poseHeadingOffset = 0.0; //Offset heading in degrees
		defaultStart=new Pose(57.0,10.0,Math.toRadians(90.0));
		overridePose=new Pose(72.0, 72.0, Math.toRadians(0.0));
	}
}
