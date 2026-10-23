package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.utility.MathUtility.translateRedPoseToBlue;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

@Autonomous(name="BLUE-Auton (Park)", preselectTeleOp = "BLUE-Manual")
public class AutonBluePark extends OpMode {
	private final Robot robot = Robot.getInstance();
	private final PoseFactory poseFactory = PoseFactory.degrees();

	private final Pose start = translateRedPoseToBlue(poseFactory.of(9.8, 131.5, 0));
	private final Pose path1 = translateRedPoseToBlue(poseFactory.of(10.5, 119, 0));

	public Path path1() {
		return line(start, path1).linear(start, path1);
	}

	private Command autoRoutine() {
		//TODO: Add mechanism actions inline below

		return sequential(
			follow(robot.drive.follower, path1())
		);
	}

	@Override
	public void init() {
		Scheduler.reset();

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry, start, org.firstinspires.ftc.teamcode.Constants.General.Alliance.BLUE);

		//Initialize servo positions
		robot.shooter.closeGate();
		robot.turret.turnToDegrees(0.0);

		telemetry.addLine("Retract arm -- START --");
		telemetry.update();

		telemetry.addLine("Retract arm -- END --");
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
