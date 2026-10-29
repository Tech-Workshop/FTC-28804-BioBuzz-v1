package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

@Autonomous(name="RED-Auton", preselectTeleOp = "RED-Manual")
public class AutonRed extends OpMode {
	private final Robot robot = Robot.getInstance();
	private final PoseFactory poseFactory = PoseFactory.degrees();

	private final Pose start = poseFactory.of(57.0, 10.0, 90.0);
	private final Pose path1 = poseFactory.of(61.0, 14.0, 90.0);
	private final Pose point2 = poseFactory.of(28.5238, 38.1122, 90.0);
	private final Pose park = poseFactory.of(15.0, 93.0, 0.0);

	public Path path1() {
		return line(start, path1).linear(start, path1);
	}

	public Path path2() {
		return line(path1, point2).linear(path1, point2);
	}

	public Path path3() {
		return line(point2, park).linear(point2, park);
	}

	private Command autoRoutine() {
		//TODO: Add mechanism actions inline below

		return sequential(
			instant(() -> robot.shooter.spinFlywheel()),
			instant(() -> robot.shooter.spinCounter()),
			waitMs(500),
			follow(robot.drive.follower, path1()),
			waitMs(500),
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(3000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			instant(() -> robot.shooter.stopCounter()),
			waitMs(500),
			follow(robot.drive.follower, path2()),
			follow(robot.drive.follower, path3())
		);
	}

	@Override
	public void init() {
		Scheduler.reset();

		//Initialize robot hardware
		//robot.init(hardwareMap, telemetry, start, org.firstinspires.ftc.teamcode.Constants.General.Alliance.RED);
		robot.init(hardwareMap, telemetry, start, org.firstinspires.ftc.teamcode.Constants.General.Alliance.RED);

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
		robot.shooter.periodic();
		robot.ramp.periodic();

		//Save pose
		PoseStorage.currentPose = robot.drive.follower.pose();
	}
}
