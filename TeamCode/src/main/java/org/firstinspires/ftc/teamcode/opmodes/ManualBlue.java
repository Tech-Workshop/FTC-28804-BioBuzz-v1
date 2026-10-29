package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="BLUE-Manual")
public class ManualBlue extends ManualBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.BLUE;
		//poseHeadingOffset = 180.0; //Offset heading in degrees
		defaultStart=new Pose(57.0,10.0,Math.toRadians(90.0));
		overridePose=new Pose(72.0, 72.0, Math.toRadians(180.0));
	}
}
