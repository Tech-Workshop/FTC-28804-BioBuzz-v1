package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;
import org.firstinspires.ftc.teamcode.utility.PathUtility;

public class AutonParkBase extends OpMode {

	private final Robot robot = Robot.getInstance();
	private final PoseFactory poseFactory = PoseFactory.degrees();

	//Path points, defined from RED alliance
	private final Pose startPose = poseFactory.of(9.8, 131.5, 0);
	private final Pose parkPose = poseFactory.of(15, 119, 0);

	public Path path1() { return PathUtility.linearPath(startPose,parkPose,thisAlliance); } //line(start, path1).linear(start, path1);

	private Command autoRoutine() {
		return sequential(
			follow(robot.drive.follower, path1())
		);
	}

	//Alliance-specific settings
	protected Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;

	//Override class to change alliance-specific variables
	protected void updateSettings() { } //Nothing in base class

	@Override
	public void init() {
		updateSettings(); //Update alliance-specific settings
		Scheduler.reset(); //Reset Ivy scheduler

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry, startPose, thisAlliance);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.ramp.initializeUp();
		//robot.turret.turnToDegrees(0.0);

		telemetry.update();
	}

	@Override
	public void start() {
		//Schedule autonomous routine
		schedule(autoRoutine());
	}

	@Override
	public void loop() {
		robot.drive.follower.update();
		Scheduler.execute();

		//Periodic
		robot.ramp.periodic();

		//Save pose
		PoseStorage.currentPose = robot.drive.follower.pose();
	}
}
