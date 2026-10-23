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

	private final Pose start = poseFactory.of(57, 10, 90);
	private final Pose path1 = poseFactory.of(57, 14, 90);
	private final Pose point2 = poseFactory.of(28.5238, 38.1122, 90);
	private final Pose point3 = poseFactory.of(10.8656, 90.5136, 0);

	public Path path1() {
		return line(start, path1).linear(start, path1);
	}

	public Path path2() {
		return line(path1, point2).linear(path1, point2);
	}

	public Path path3() {
		return line(point2, point3).linear(point2, point3);
	}

	private Command autoRoutine() {
		//TODO: Add mechanism actions inline below

		return sequential(
			instant(() -> robot.shooterPollen.spinFlywheel()),
			waitMs(500),
			follow(robot.drive.follower, path1()),
			waitMs(2000),
			instant(() -> robot.shooterPollen.openGate()),
			instant(() -> robot.indexer.index()),
			waitMs(3000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooterPollen.closeGate()),
			waitMs(500),
			instant(() -> robot.indexer.sortSpeed()),
			instant(() -> robot.sorter.sort()),
			waitMs(2000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.sorter.home()),
			waitMs(2000),
			instant(() -> robot.indexer.sortSpeed()),
			instant(() -> robot.sorter.sort()),
			waitMs(2000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.sorter.home()),
			waitMs(2000),
			instant(() -> robot.shooterPollen.openGate()),
			instant(() -> robot.indexer.index()),
			waitMs(3000),
			instant(() -> robot.shooterPollen.stopFlywheel()),
			instant(() -> robot.shooterPollen.closeGate()),
			instant(() -> robot.indexer.stop()),
			waitMs(2000),
			follow(robot.drive.follower, path2()),
			follow(robot.drive.follower, path3())
		);
	}

	@Override
	public void init() {
		Scheduler.reset();

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry, start, org.firstinspires.ftc.teamcode.Constants.General.Alliance.RED);

		//Initialize servo positions
		robot.shooterNectar.closeGate();
		robot.shooterPollen.closeGate();
		robot.turretNectar.turnToDegrees(0.0);
		robot.turretPollen.turnToDegrees(0.0);
		robot.sorter.home();

		telemetry.addLine("Retract arm -- START --");
		telemetry.update();

		robot.intake.retractArm();
		while(!robot.intake.isArmDone()) {
			telemetry.addData("Retract arm pos:",robot.intake.getArmPosition());
			telemetry.update();

			robot.intake.armPeriodic();
		}

		telemetry.addLine("Retract arm -- END --");
		telemetry.update();

	}

	@Override
	public void start() {
		robot.intake.extendArm();

		//Schedule autonomous routine
		schedule(autoRoutine());
	}

	@Override
	public void loop() {
		robot.drive.follower.update();
		Scheduler.execute();

		//Periodic
		robot.intake.armPeriodic();
		robot.shooterPollen.periodic();

		//Save pose
		PoseStorage.currentPose = robot.drive.follower.pose();
	}
}
