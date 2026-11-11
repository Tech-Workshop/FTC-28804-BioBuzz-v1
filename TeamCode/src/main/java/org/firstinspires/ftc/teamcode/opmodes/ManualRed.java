package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="RED-Manual")
public class ManualRed extends ManualBase {
	@Override
	protected void updateSettings() {
		thisAlliance = Constants.General.Alliance.RED;
		defaultStart=new Pose(8.8,133.6,Math.toRadians(0.0));
		//overridePose=new Pose(8.8,133.6, Math.toRadians(0.0));
		overridePose=new Pose(8.5,57.6, Math.toRadians(0.0));
	}
}
