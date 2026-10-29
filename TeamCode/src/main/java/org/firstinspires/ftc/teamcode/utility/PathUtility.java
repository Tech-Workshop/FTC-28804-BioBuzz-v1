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
}
