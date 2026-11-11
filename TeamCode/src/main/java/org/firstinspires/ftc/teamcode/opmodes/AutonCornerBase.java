package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.utility.PathUtility.translatePose;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.PathUtility;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

import java.util.Objects;

public class AutonCornerBase extends OpMode {
	private final Robot robot = Robot.getInstance();

	private int loopCnt=0;

	private String optionSide="LEFT";

	//Path points, defined from RED alliance
	private final PoseFactory poseFactory = PoseFactory.degrees();
	private final Pose startPose = poseFactory.of(9.8, 131.5, 0);
	private final Pose shootPose = poseFactory.of(18.7, 126.0, 315);
	private final Pose parkPose = poseFactory.of(12.0, 119.0, 0);

	public Path path1() {
		return PathUtility.linearPathWithSpeed(startPose, shootPose, thisAlliance,1.0);
	}

	public Path path2() {
		return PathUtility.linearPath(shootPose, parkPose, thisAlliance);
	}

	private Command autoRoutine() {
		return sequential(
			waitMs(20000),
			instant(() -> robot.shooter.spinFlywheel()),
			follow(robot.drive.follower, path1()),
			waitMs(1000),

			//Open ball gate and start indexer
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(1800),

			//Stop indexer & shooter, close ball gate, lower flower ramp
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),

			follow(robot.drive.follower, path2())
		);
	}

	//Alliance-specific settings
	protected org.firstinspires.ftc.teamcode.Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;
	//protected int limelightUpPipeline=0;
	//protected int limelightDownPipeline=1;

	//Override class to change alliance-specific variables
	protected void updateSettings() { } //Nothing in base class

	@Override
	public void init() {
		updateSettings(); //Update alliance-specific settings
		Scheduler.reset();

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry,  translatePose(startPose,thisAlliance), thisAlliance);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.ramp.initializeUp();
		robot.turret.turnToZero();

		//TODO:Temp
		robot.sensors.setLedDisable();

		//Set target shooter speed and hood angle for auto
		robot.shooter.setShotVelocityUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(shootPose,thisAlliance),robot.turret.getVerticalDistanceFromPose(shootPose,thisAlliance));
		robot.shooter.setHoodPositionUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(shootPose,thisAlliance),robot.turret.getVerticalDistanceFromPose(shootPose,thisAlliance));

		telemetry.addData("Distance from target (horiz):",robot.turret.getSidewaysDistanceFromPose(shootPose,thisAlliance));
		telemetry.addData("Distance from target (vert):",robot.turret.getVerticalDistanceFromPose(shootPose,thisAlliance));

		telemetry.addData("Shot Velocity (target):",robot.shooter.getShotVelocity());
		telemetry.addData("Hood Position (target):",robot.shooter.getHoodPosition());

		//Set limelight pipeline
		//robot.sensors.setLimelightPipeline(limelightUpPipeline);
		//telemetry.addData("Limelight - Pipeline:",limelightUpPipeline);

		telemetry.update();
	}

	@Override
	public void start() {
		//Schedule autonomous routine
		schedule(autoRoutine());
	}

	@Override
	public void loop() {
		if(loopCnt%20==0) {
			robot.turret.turnToZero();
		}

		robot.drive.follower.update();
		Scheduler.execute();

		//Periodic
		robot.ramp.periodic();
		robot.shooter.periodic();

//		if(robot.sensors.limelightGetCollectStatus()) {
//			telemetry.addLine("Checking Limelight Status");
//			robot.sensors.limelightCollect();
//
//			if(robot.sensors.limelightHit()) {
//				telemetry.addLine("HIT!");
//				//robot.sensors.setLedGreen();
//			} else {
//				telemetry.addLine("MISS!");
//				//robot.sensors.setLedRed();
//			}
//		}

		telemetry.update();

		//Save pose
		PoseStorage.currentPose = robot.drive.follower.pose();
		loopCnt++;
	}
}
