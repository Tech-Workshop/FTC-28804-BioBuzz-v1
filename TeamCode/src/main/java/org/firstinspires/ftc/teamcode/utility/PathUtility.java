package org.firstinspires.ftc.teamcode.utility;

import static com.pedropathing.api.Paths.line;

import static org.firstinspires.ftc.teamcode.utility.MathUtility.translateRedPoseToBlue;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.Constants;

public class PathUtility {
	public static Path linearPath(Pose start, Pose end, Constants.General.Alliance alliance) {
		if(alliance==Constants.General.Alliance.BLUE) {
			start = translateRedPoseToBlue(start);
			end = translateRedPoseToBlue(end);
		}

		return line(start, end).linear(start, end);
	}

	public static Path linearPathWithSpeed(Pose start, Pose end, Constants.General.Alliance alliance,double speed) {
		if(alliance==Constants.General.Alliance.BLUE) {
			start = translateRedPoseToBlue(start);
			end = translateRedPoseToBlue(end);
		}

		return line(start, end).linear(start, end).with(org.firstinspires.ftc.teamcode.pedro.Constants.foresightConfig.maxPathSpeed.at(speed));
	}

	public static Pose translatePose(Pose point, Constants.General.Alliance alliance) {
		if(alliance==Constants.General.Alliance.BLUE) {
			point = translateRedPoseToBlue(point);
		}

		return point;
	}
}
