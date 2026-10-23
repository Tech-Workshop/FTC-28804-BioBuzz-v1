package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;

public class Turret {
	private final ServoImplEx turretLeftServo,turretRightServo;
	private final Telemetry telemetry;
	private final Constants.General.Alliance alliance;
	private Robot robot;

	public Turret(HardwareMap hardwareMap, Telemetry telemetry, Constants.General.Alliance alliance) {
		this.turretLeftServo=hardwareMap.get(ServoImplEx.class, Constants.Turret.TURRET_LEFT_ID);
		turretLeftServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

		this.turretRightServo=hardwareMap.get(ServoImplEx.class, Constants.Turret.TURRET_RIGHT_ID);
		turretRightServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

		this.alliance=alliance;
		this.telemetry=telemetry;

		//Get robot reference
		robot = Robot.getInstance();
	}

	//Turn turret to specified degrees (0 deg=straight forward)
	public void turnToDegrees(double degrees) {
		//Normalize given angle
		double position = (degrees + 85.0) / 350.0; //TODO: Get new calculation (normalize?)
		turretLeftServo.setPosition(Math.max(0.0, Math.min(1.0, position)));
		turretRightServo.setPosition(Math.max(0.0, Math.min(1.0, position)));

		//telemetry.addData("--> servo position:",Math.max(0.0, Math.min(1.0, position)));
	}

	public void turnToTarget() {
		//Get position on field to determine target
		double targetX,targetY;

		//Far
		if(robot.drive.follower.pose().y()>70.75) {
			if(alliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueFar.get(0);
				targetY=Constants.General.hiveBlueFar.get(1);
				telemetry.addLine("TARGET=BLUE Far");
			} else {
				targetX=Constants.General.hiveRedFar.get(0);
				targetY=Constants.General.hiveRedFar.get(1);
				telemetry.addLine("TARGET=RED Far");
			}

		//Close
		} else {
			if(alliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueClose.get(0);
				targetY=Constants.General.hiveBlueClose.get(1);
				telemetry.addLine("TARGET=BLUE Close");
			} else {
				targetX=Constants.General.hiveRedClose.get(0);
				targetY=Constants.General.hiveRedClose.get(1);
				telemetry.addLine("TARGET=RED Close");
			}
		}

		//Get robot angle to target
		double targetAngle=angleToTarget(robot.drive.follower.pose().x(),robot.drive.follower.pose().y(),targetX,targetY);

		//Get adjusted robot heading
		double robotAngle=Math.toDegrees(robot.drive.follower.pose().heading())-90;

		turnToDegrees(targetAngle-robotAngle);
	}

	public static double angleToTarget(double robotX, double robotY, double targetX, double targetY) {
		double dx = targetX - robotX;
		double dy = targetY - robotY;
		return Math.toDegrees(Math.atan2(-dx, dy));
	}
}
