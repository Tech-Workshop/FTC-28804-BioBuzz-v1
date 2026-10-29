package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Drive.DRIVE_SPEED;
import static org.firstinspires.ftc.teamcode.Constants.Drive.TURN_ERROR;
import static org.firstinspires.ftc.teamcode.Constants.Drive.TURN_POWER_CLOSE;
import static org.firstinspires.ftc.teamcode.Constants.Drive.TURN_POWER_FF;
import static org.firstinspires.ftc.teamcode.Constants.Drive.TURN_POWER_MULTIPLIER;
import static org.firstinspires.ftc.teamcode.Constants.General.CONTROLLER_AUTO_ERROR;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.utility.MathUtility;

public class Drive {
	public Follower follower; //Pedropathing follower
	private final Telemetry telemetry;
	private final org.firstinspires.ftc.teamcode.Constants.General.Alliance alliance;

	//private final double[] autoCorrectAngles = {-Math.PI, -Math.PI * 3 / 4, -Math.PI / 2, -Math.PI / 4, 0.0, Math.PI / 4, Math.PI / 2, Math.PI * 3 / 4, Math.PI};
	private final double[] autoCorrectAngles = {-Math.PI, -Math.PI / 2,  0.0, Math.PI / 2, Math.PI};
	private double headingTarget = 0.0,poseHeadingOffset=0.0;

	public double cacheX,cacheY,cacheHeading,cacheSpeed,cacheT;

	public Drive(HardwareMap hardwareMap, Telemetry telemetry, Pose startPose, org.firstinspires.ftc.teamcode.Constants.General.Alliance alliance) {
		this.follower= Constants.create(hardwareMap);
		follower.setPose(startPose);
		follower.update();

		if(alliance==org.firstinspires.ftc.teamcode.Constants.General.Alliance.BLUE) {
			poseHeadingOffset=180.0;
		}

		this.alliance=alliance;
		this.telemetry = telemetry;
	}

	/**
	 * Save location information to cache
	 */
	public void setCache() {
		this.cacheX=follower.pose().x();
		this.cacheY=follower.pose().y();
		this.cacheHeading=follower.pose().heading();
	}

	/**
	 * Save location information to cache

	public void setAlliance(org.firstinspires.ftc.teamcode.Constants.General.Alliance alliance) {
		this.alliance=alliance;
	}
	 */

	/**
	 * Teleop drive method
	 *
	 * @param gamepad1 Driver gamepad
	 */
	public void drive(Gamepad gamepad1) {
		//Check for low-speed mode
		double SPEED_MULTIPLIER = DRIVE_SPEED;
		if (gamepad1.left_bumper) {
			SPEED_MULTIPLIER *= 0.3;
		}

		double turnPower = -gamepad1.right_stick_x*TURN_POWER_MULTIPLIER*SPEED_MULTIPLIER;

		DrivePowers powers;

		telemetry.addData("alliance",alliance);
		telemetry.addData("heading",Math.toDegrees(follower.pose().heading()));
		//telemetry.update();


		/*
		double turnPower=0.0;
		double headingCurrent=follower.pose().heading();

		//Low-speed - Right analog stick turns (only)
		if (gamepad1.left_bumper) {
			turnPower = -gamepad1.right_stick_x*TURN_POWER_MULTIPLIER*0.3;
			headingTarget=headingCurrent;

		} else{

			//Get target heading if right analog stick is fully engaged
			if (Math.sqrt(gamepad1.right_stick_x * gamepad1.right_stick_x + gamepad1.right_stick_y * gamepad1.right_stick_y) > 0.90) {
				if(poseHeadingOffset==180.0) {
					headingTarget = MathUtility.normalizeAngle(Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x) + Math.PI / 2);
				} else {
					headingTarget = MathUtility.normalizeAngle(Math.atan2(gamepad1.right_stick_y, -gamepad1.right_stick_x) + Math.PI / 2);
				}

				//Auto correct to nearest 90 degrees if not in slow mode (heading target always -pi to pi)
				for (double autoCorrectAngle : autoCorrectAngles) {
					if (Math.abs(headingTarget - autoCorrectAngle) < CONTROLLER_AUTO_ERROR) {
						headingTarget = autoCorrectAngle;
						break;
					}
				}
			}

			//Identify error between current heading and maintain heading
			double headingError = MathUtility.normalizeAngle(headingTarget - headingCurrent);

			//Calculate heading correction power (note error in radians)
			if (Math.abs(headingError) < TURN_ERROR) {
				turnPower = 0.0;
			} else if (headingError > 1.5) {
				turnPower = 1;
			} else if (headingError < -1.5) {
				turnPower = -1;
			} else {
				turnPower = MathUtility.clamp((headingError / Math.PI) * TURN_POWER_CLOSE + (Math.signum(headingError) * TURN_POWER_FF), -1.0, 1.0);
			}

			turnPower *= TURN_POWER_MULTIPLIER;
		}
		*/

		//Driving from Blue Alliance
		if(alliance == org.firstinspires.ftc.teamcode.Constants.General.Alliance.BLUE) {
			powers = ManualDrive.fieldCentric(
				gamepad1.left_stick_y * SPEED_MULTIPLIER, //Default is (-)
				gamepad1.left_stick_x * SPEED_MULTIPLIER,
				turnPower,   //Default is (+)
				follower.pose().heading()
			);

		//Driving from Red Alliance
		} else {
			powers = ManualDrive.fieldCentric(
				-gamepad1.left_stick_y * SPEED_MULTIPLIER, //Default is (-)
				-gamepad1.left_stick_x * SPEED_MULTIPLIER,
				turnPower,   //Default is (+)
				follower.pose().heading()
			);
		}

		follower.manual(powers);
		follower.update();

		telemetry.addData("Robot X", follower.pose().x());
		telemetry.addData("Robot Y", follower.pose().y());
		telemetry.addData("Robot Heading", Math.toDegrees(follower.pose().heading()));
	}
}
