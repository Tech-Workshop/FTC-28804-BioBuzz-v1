package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Drive.DRIVE_SPEED;
import static org.firstinspires.ftc.teamcode.Constants.Drive.TURN_POWER_MULTIPLIER;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drive {
	public Follower follower; //Pedropathing follower
	private final Telemetry telemetry;
	private final org.firstinspires.ftc.teamcode.Constants.General.Alliance alliance;

	public double cacheX,cacheY,cacheHeading,cacheSpeed,cacheT;

	public Drive(HardwareMap hardwareMap, Telemetry telemetry, Pose startPose, org.firstinspires.ftc.teamcode.Constants.General.Alliance alliance) {
		this.follower= Constants.create(hardwareMap);
		follower.setPose(startPose);
		follower.update();

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

		DrivePowers powers;

		//Driving from Blue Alliance
		if(alliance== org.firstinspires.ftc.teamcode.Constants.General.Alliance.BLUE) {
			powers = ManualDrive.fieldCentric(
				gamepad1.left_stick_y * SPEED_MULTIPLIER, //Default is (-)
				gamepad1.left_stick_x * SPEED_MULTIPLIER,
				-gamepad1.right_stick_x * TURN_POWER_MULTIPLIER,   //Default is (+)
				follower.pose().heading()
			);

		//Driving from Red Alliance
		} else {
			powers = ManualDrive.fieldCentric(
				-gamepad1.left_stick_y * SPEED_MULTIPLIER, //Default is (-)
				-gamepad1.left_stick_x * SPEED_MULTIPLIER,
				-gamepad1.right_stick_x * TURN_POWER_MULTIPLIER,   //Default is (+)
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
