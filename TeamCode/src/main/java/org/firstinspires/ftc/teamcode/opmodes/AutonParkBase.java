package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitMs;
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
import org.firstinspires.ftc.teamcode.utility.PoseStorage;
import org.firstinspires.ftc.teamcode.utility.PathUtility;

import java.util.Objects;

public class AutonParkBase extends OpMode {

	private final Robot robot = Robot.getInstance();
	private final PoseFactory poseFactory = PoseFactory.degrees();

	private String optionDelay="NO";
	private String optionSide="LEFT";

	//Path points, defined from RED alliance
	private final Pose startPose = poseFactory.of(9.8, 131.5, 0);
	private final Pose parkPoseLeft = poseFactory.of(12.0, 119.0, 0);
	private final Pose parkPoseRight = poseFactory.of(12.0, 89.0, 0);

	public Path path1a() { return PathUtility.linearPath(startPose,parkPoseLeft,thisAlliance); } //line(start, path1).linear(start, path1);
	public Path path1b() { return PathUtility.linearPath(startPose,parkPoseRight,thisAlliance); } //line(start, path1).linear(start, path1);

	private Command autoRoutine() {
		return sequential(
			conditional(
				()->optionDelayIsNo(),
				waitMs(0),
				waitMs(27000)
			),

			conditional(
				()->optionSideIsLeft(),
				follow(robot.drive.follower, path1a()),
				follow(robot.drive.follower, path1b())
			)
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
		robot.init(hardwareMap, telemetry, translatePose(startPose,thisAlliance), thisAlliance);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.ramp.initializeUp();
		//robot.turret.turnToDegrees(0.0);

		telemetry.update();
	}

	@Override
	public void init_loop() {
		if(gamepad1.aWasPressed()) {
			if(Objects.equals(optionSide, "LEFT")) {
				optionSide="RIGHT";
			} else {
				optionSide="LEFT";
			}
		}
		if(gamepad1.bWasPressed()) {
			if(Objects.equals(optionDelay, "NO")) {
				optionDelay="YES";
			} else {
				optionDelay="NO";
			}
		}

		telemetry.addLine("Park Auton Options");
		telemetry.addLine("----------------------");
		telemetry.addData("Park Side (A/X):",optionSide);
		telemetry.addData("Delay (B/O):",optionDelay);
		telemetry.addLine("----------------------");
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

	public boolean optionDelayIsNo() {
		return Objects.equals(optionDelay, "NO");
	}

	public boolean optionSideIsLeft() {
		return Objects.equals(optionSide, "LEFT");
	}
}
