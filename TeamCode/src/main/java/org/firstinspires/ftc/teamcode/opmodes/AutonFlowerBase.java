package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
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
import org.firstinspires.ftc.teamcode.utility.PathUtility;
import org.firstinspires.ftc.teamcode.utility.PoseStorage;

public class AutonFlowerBase extends OpMode {
	private final Robot robot = Robot.getInstance();
	private final PoseFactory poseFactory = PoseFactory.degrees();

	private final Pose start = poseFactory.of(57.0, 10.0, 90.0); //Start position
	private final Pose point1 = poseFactory.of(61.0, 14.0, 90.0); //Shoot position

	private final Pose point2 = poseFactory.of(27.6, 50.0, 180.0);//In front of flower
	private final Pose point3 = poseFactory.of(19.0, 50.0, 180.0);//De-pollenate flower
	private final Pose point3a = poseFactory.of(20.0, 50.0, 180.0);//De-pollenate flower
	private final Pose point3b = poseFactory.of(19.0, 50.0, 180.0);//De-pollenate flower

	private final Pose point4 = poseFactory.of(61.0, 14.0, 90.0);//Shoot position
	private final Pose point5 = poseFactory.of(28.5238, 38.1122, 90.0); //Park ready position (side of hive)
	private final Pose park = poseFactory.of(15.0, 93.0, 0.0); //Park

	public Path path1() {
		return PathUtility.linearPathWithSpeed(start, point1, thisAlliance,1.0);
	} //To shoot position
	public Path path2() {
		return PathUtility.linearPath(point1, point2, thisAlliance);
	} //To front of flower
	public Path path3() {
		return PathUtility.linearPathWithSpeed(point2, point3, thisAlliance, 0.4);
	} //Move into flower
	public Path path3a() {
		return PathUtility.linearPathWithSpeed(point3, point3a, thisAlliance, 1.0);
	} //Move into flower
	public Path path3b() {
		return PathUtility.linearPath(point3a, point3b, thisAlliance);
	} //Move into flower

	public Path path4() {
		return PathUtility.linearPath(point3b, point4, thisAlliance);
	} //To shoot position
	public Path path5() {
		return PathUtility.linearPath(point4, point5, thisAlliance);
	} //To side of hive
	public Path path6() {
		return PathUtility.linearPath(point5, park, thisAlliance);
	} //Park

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
			waitMs(2000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			instant(() -> robot.shooter.stopCounter()),
			waitMs(500),
			instant(() -> robot.ramp.lower()),
			follow(robot.drive.follower, path2()),
			instant(() -> robot.intake.intake()),
			instant(() -> robot.indexer.intakeSpeed()),

			//TODO: slow down
			follow(robot.drive.follower, path3()),
			follow(robot.drive.follower, path3a()),
			follow(robot.drive.follower, path3b()),

			waitMs(2200),
			instant(() -> robot.intake.stop()),
			instant(() -> robot.indexer.stop()),
			follow(robot.drive.follower, path4()),

			//TODO: start flywheel/counter and raise ramp mid-path
			instant(() -> robot.ramp.raise()),
			instant(() -> robot.shooter.spinFlywheel()),
			instant(() -> robot.shooter.spinCounter()),

			waitMs(2000),
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(2000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			instant(() -> robot.shooter.stopCounter()),
			waitMs(500),

			//Park
			follow(robot.drive.follower, path5()),
			follow(robot.drive.follower, path6())
		);
	}

	//Alliance-specific settings
	protected org.firstinspires.ftc.teamcode.Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;

	//Override class to change alliance-specific variables
	protected void updateSettings() { } //Nothing in base class

	@Override
	public void init() {
		updateSettings(); //Update alliance-specific settings
		Scheduler.reset();

		//Initialize robot hardware
		robot.init(hardwareMap, telemetry,  translatePose(start,thisAlliance), thisAlliance);

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
