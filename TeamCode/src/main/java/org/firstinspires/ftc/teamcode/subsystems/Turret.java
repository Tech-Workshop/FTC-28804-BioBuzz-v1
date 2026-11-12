package org.firstinspires.ftc.teamcode.subsystems;

import static android.os.SystemClock.sleep;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.MathUtility;

import dev.frozenmilk.util.collections.Cons;

public class Turret {
	private final ServoImplEx turretLeftServo,turretRightServo;
	private final Telemetry telemetry;
	private final Constants.General.Alliance alliance;
	private Robot robot;

	private double lastPosUpdate=-1.0,thisPosition=0.0; //Bad initial value forces first update
	private boolean deadzoneFlag=false;

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

	public void turnToZero() {
		setPosition(Constants.Turret.TURRET_ZERO_POS);
	}

	//Turn turret to specified degrees (0 deg=straight forward)
	public void turnToDegrees(double degrees) {
		//Normalize angle (-180 to 180 degrees)
		degrees= MathUtility.normalizeAngleDegrees(degrees);

telemetry.addData("Turn to degrees (norm)",degrees);

		//Verify within bounds
		if(degrees>Constants.Turret.TURRET_DEADZONE_MIN && degrees<Constants.Turret.TURRET_DEADZONE_MAX) {
telemetry.addLine("TURRET - DEADZONE!");
			if(!deadzoneFlag) {
				robot.sensors.setLedRed();
				deadzoneFlag = true;
			}
		} else {
			//Wrap degrees to other side of deadzone
			if(degrees>Constants.Turret.TURRET_DEADZONE_MAX) {
				degrees-=360.0;
			}
			double position=Constants.Turret.TURRET_ZERO_POS + degrees * Constants.Turret.TURRET_POS_PER_DEGREE;
			setPosition(position);
telemetry.addData("Turret Position:",position);

			if(deadzoneFlag) {
				deadzoneFlag=false;
				robot.sensors.setLedDisable();
			}
		}
	}

	public boolean headingWithinError() {
		return !deadzoneFlag;
	}

	public void setPosition(double position) {
		position=Math.clamp(position,Constants.Turret.TURRET_MIN,Constants.Turret.TURRET_MAX);

		if(Math.abs(position-lastPosUpdate)> Constants.Turret.TURRET_UPDATE_ERROR) {
			thisPosition=position;
			turretLeftServo.setPosition(position);
			turretRightServo.setPosition(position);
			lastPosUpdate = position;
		}
	}

	public double getPosition() {
		return thisPosition;
	}

	public void turnToTarget() {
		//Get heading to target (based on alliance and field position)
		double targetAngle=getHeadingToTarget();

telemetry.addData("Heading to Target:",targetAngle);

		//Get adjusted robot heading
		//double robotAngle=Math.toDegrees(robot.drive.follower.pose().heading())-90;
		double robotAngle=Math.toDegrees(robot.drive.follower.pose().heading());
telemetry.addData("Robot Heading:",robotAngle);
telemetry.addData("Turn to Degrees:",(targetAngle-robotAngle));

		turnToDegrees(targetAngle-robotAngle);
	}

	public double getHeadingToTarget() {
		//Get position on field to determine target
		double targetX,targetY;

		//Far
		if(robot.drive.follower.pose().y()>70.75) {
			if(alliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueFar.get(0);
				targetY=Constants.General.hiveBlueFar.get(1);
			} else {
				targetX=Constants.General.hiveRedFar.get(0);
				targetY=Constants.General.hiveRedFar.get(1);
			}

			//Close
		} else {
			if(alliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueClose.get(0);
				targetY=Constants.General.hiveBlueClose.get(1);
			} else {
				targetX=Constants.General.hiveRedClose.get(0);
				targetY=Constants.General.hiveRedClose.get(1);
			}
		}

		//Return robot angle to target
		return angleToTarget(robot.drive.follower.pose().x(),robot.drive.follower.pose().y(),targetX,targetY);
	}

	public static double angleToTarget(double robotX, double robotY, double targetX, double targetY) {
		double dx = targetX - robotX;
		double dy = targetY - robotY;
		//return Math.toDegrees(Math.atan2(-dx, dy));
		return Math.toDegrees(Math.atan2(dy, dx));
	}

	public double getSidewaysDistance() {
		return getSidewaysDistanceFromPose(robot.drive.follower.pose(),alliance);
	}

	public double getSidewaysDistanceFromPose(Pose localPose, Constants.General.Alliance localAlliance) {
		//Get position on field to determine target
		double targetX;

		//Far
		if(localPose.y()>70.75) {
			if(localAlliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueFar.get(0);
			} else {
				targetX=Constants.General.hiveRedFar.get(0);
			}

		//Close
		} else {
			if(localAlliance==Constants.General.Alliance.BLUE) {
				targetX=Constants.General.hiveBlueClose.get(0);
			} else {
				targetX=Constants.General.hiveRedClose.get(0);
			}
		}

		return Math.abs(targetX-localPose.x());
	}

	public double getVerticalDistance() {
		return getVerticalDistanceFromPose(robot.drive.follower.pose(),alliance);
	}

	public double getVerticalDistanceFromPose(Pose localPose, Constants.General.Alliance localAlliance) {
		//Get position on field to determine target
		double targetY;

		//Far
		if(localPose.y()>70.75) {
			if(localAlliance==Constants.General.Alliance.BLUE) {
				targetY=Constants.General.hiveBlueFar.get(1);
			} else {
				targetY=Constants.General.hiveRedFar.get(1);
			}

			//Close
		} else {
			if(localAlliance==Constants.General.Alliance.BLUE) {
				targetY=Constants.General.hiveBlueClose.get(1);
			} else {
				targetY=Constants.General.hiveRedClose.get(1);
			}
		}

		return Math.abs(targetY-localPose.y());
	}

	public String getStatus() {
		return "Alliance="+this.alliance;
	}
}
