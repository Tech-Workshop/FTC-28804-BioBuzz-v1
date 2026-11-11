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

public class AutonFlowerBase extends OpMode {
	private final Robot robot = Robot.getInstance();

	private int loopCnt=0;
	private String optionPark="LEFT"; //LEFT, RIGHT, PUSH

	private final PoseFactory poseFactory = PoseFactory.degrees();
	private final Pose start = poseFactory.of(38.6, 9.0, 90.0); //Start position

	//TODO: Changed heading from 50 deg
	private final Pose upShoot = poseFactory.of(38.6, 14, 70.0); //Shoot position
	//private final Pose upShoot = poseFactory.of(22.78, 15.9, 52.0); //Shoot position
	private final Pose flowerReady = poseFactory.of(30.0, 48.0, 180.0);//In front of flower
	private final Pose flowerIn = poseFactory.of(15.1, 48.0, 180.0);//De-pollenate flower
	private final Pose flowerAgitate = poseFactory.of(17.0, 48.0, 180.0);//De-pollenate flower
	private final Pose runDown = poseFactory.of(22.0, 30, 90.0); //Run start position

	//TODO: Changed heading from -50 to -47
	private final Pose downShoot = poseFactory.of(38.6, (144-14), -67.0); //Shoot position
	//private final Pose downShoot = poseFactory.of(22.78, (144-15.9), -52.0); //Shoot position
	private final Pose flowerDownReadyA = poseFactory.of(40.0, 120.0, 90.0);//In front of flower
	private final Pose flowerDownReadyB = poseFactory.of(47.0, 120.0, 90.0);//In front of flower
	private final Pose flowerDownIn = poseFactory.of(47.0, 126.5, 90.0);//De-pollenate flower
	private final Pose flowerDownAgitate = poseFactory.of(47.0, 124.5, 90.0);//De-pollenate flower

	//OLD -- private final Pose park = poseFactory.of(14.0, 93.0, 0.0); //Park
	private final Pose parkLeft = poseFactory.of(15.0, 120.0, 0.0); //Park

	private final Pose parkRightPre = poseFactory.of(32.0, 91.0, 0);
	private final Pose parkRight = poseFactory.of(15.0, 91.0, 0);

	private final Pose parkPushPre = poseFactory.of(28.0, 66.0, 0);
	private final Pose parkPushReadyA = poseFactory.of(9.5, 66.0, 0);
	private final Pose parkPushReadyB = poseFactory.of(9.5, 80.0, 0);
	private final Pose parkPush = poseFactory.of(12.0, 93.0, -6);

	public Path path1() {
		return PathUtility.linearPathWithSpeed(start, upShoot, thisAlliance,1.0);
	} //To shoot position

	public Path path2() {
		return PathUtility.linearPath(upShoot, flowerReady, thisAlliance);
	} //To front of flower

	public Path path3() {
		return PathUtility.linearPathWithSpeed(flowerReady, flowerIn, thisAlliance, 1.0);
	} //Move into flower

	public Path path3a() {
		return PathUtility.linearPathWithSpeed(flowerIn, flowerAgitate, thisAlliance, 1.0);
	} //Move into flower

	public Path path3b() {
		return PathUtility.linearPath(flowerAgitate, flowerIn, thisAlliance);
	} //Move into flower

	public Path path4() {
		return PathUtility.linearPath(flowerIn, upShoot, thisAlliance);
	} //To shoot position

	public Path path5() {
		return PathUtility.linearPath(upShoot, runDown, thisAlliance);
	} //To shoot position

	//From Up Shot position to Down Shot position
	public Path path6() {
		return PathUtility.linearPath(runDown, downShoot, thisAlliance);
	}

	//From Down Shot position to Down Flower Ready
	public Path path7a() {
		return PathUtility.linearPathWithSpeed(downShoot, flowerDownReadyA, thisAlliance, 1.0);
	}

	//From Up Shot position to Down Flower Ready
	public Path path7b() {
		return PathUtility.linearPathWithSpeed(runDown, flowerDownReadyA, thisAlliance, 1.0);
	}

	public Path path8pre() {
		return PathUtility.linearPathWithSpeed(flowerDownReadyA, flowerDownReadyB, thisAlliance, 1.0);
	}

	public Path path8() {
		return PathUtility.linearPathWithSpeed(flowerDownReadyB, flowerDownIn, thisAlliance, 1.0);
	} //Move into flower

	public Path path8a() {
		return PathUtility.linearPathWithSpeed(flowerDownIn, flowerDownAgitate, thisAlliance, 1.0);
	}

	public Path path8b() {
		return PathUtility.linearPath(flowerDownAgitate, flowerDownIn, thisAlliance);
	} //Move into flower

	public Path path9() {
		return PathUtility.linearPath(flowerDownIn, downShoot, thisAlliance);
	}

	public Path parkLeft() {
		return PathUtility.linearPath(downShoot, parkLeft, thisAlliance);
	}

	public Path parkRightA() {
		return PathUtility.linearPath(downShoot, parkRightPre, thisAlliance);
	}

	public Path parkRightB() {
		return PathUtility.linearPath(parkRightPre, parkRight, thisAlliance);
	}

	public Path parkPushPre() {
		return PathUtility.linearPath(downShoot, parkPushPre, thisAlliance);
	}

	public Path parkPushReadyA() {
		return PathUtility.linearPath(parkPushPre, parkPushReadyA, thisAlliance);
	}

	public Path parkPushReadyB() {
		return PathUtility.linearPathWithSpeed(parkPushReadyA, parkPushReadyB, thisAlliance,0.7);
	}

	public Path parkPush() {
		return PathUtility.linearPathWithSpeed(parkPushReadyB, parkPush, thisAlliance,1.0);
	}

	private Command autoRoutine() {
		return sequential(
			//Spin up shooter and move to shoot position
			//instant(()->robot.shooter.setShotVelocityUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(upShoot,thisAlliance),robot.turret.getVerticalDistanceFromPose(upShoot,thisAlliance))),
			//instant(()->robot.shooter.setHoodPositionUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(upShoot,thisAlliance),robot.turret.getVerticalDistanceFromPose(upShoot,thisAlliance))),
			instant(()->robot.shooter.adjustShotVelocity(-20.0)), //TODO: Bad solution
			instant(() -> robot.shooter.spinFlywheel()),
			follow(robot.drive.follower, path1()),
			waitMs(700),

			//Open ball gate and start indexer
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(1500),

			//Stop indexer & shooter, close ball gate, lower flower ramp
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			instant(() -> robot.ramp.lower()),
			waitMs(200),

			//Move to flower, start intake and indexer
			follow(robot.drive.follower, path2()),
			instant(() -> robot.intake.intake()),
			instant(() -> robot.indexer.intakeSpeed()),

			//Enter flower and agitate
			follow(robot.drive.follower, path3()).raceWith(waitMs(800)),
			instant(() -> robot.ramp.agitate()),
			waitMs(200),
			follow(robot.drive.follower, path3a()),
			instant(() -> robot.ramp.lower()),
			//follow(robot.drive.follower, path3b()).raceWith(waitMs(400)),
			//instant(() -> robot.ramp.agitate()),
			//waitMs(100),
			//follow(robot.drive.follower, path3a()),
			//instant(() -> robot.ramp.lower()),
			follow(robot.drive.follower, path3b()).raceWith(waitMs(400)),
			instant(() -> robot.ramp.agitate()),
			waitMs(200),
			instant(() -> robot.ramp.lower()),
			instant(()->robot.sensors.limelightEnable()),
			waitMs(1000),

			//Stop intake, indexer
			instant(() -> robot.intake.stop()),
			instant(() -> robot.indexer.stop()),

			//Move out of flower and start raising ramp (after short delay)
			parallel(
				follow(robot.drive.follower, path4()),
				sequential(
					waitMs(500),
					instant(() -> robot.ramp.raise())
				)
			),

			//Start up flywheel
			instant(() -> robot.shooter.spinFlywheel()),

			//Limelight - start collecting data
			instant(() -> robot.sensors.limelightStartCollecting()),
			waitMs(800),
			instant(() -> robot.sensors.limelightStop()),

			//Check to see if april tag was seen
			conditional(
				()->robot.sensors.limelightHit(),
				parallel(
					waitMs(500), //Do nothing - wait for flywheel to get to speed
					instant(() -> robot.sensors.setLedRed())
				),
				//No hit - shoot from other side
				sequential(
					parallel(
						sequential(
							follow(robot.drive.follower, path5()),
							follow(robot.drive.follower, path6())
						),
						instant(() -> robot.sensors.setLedGreen())
						//instant(()-> robot.shooter.setShotVelocity(Constants.Auton.AUTO_SHOOT_VELOCITY+20))
					),
					waitMs(200)
				)
			),

			//Shoot
			waitMs(500),
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(1500),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			waitMs(500),
			instant(() -> robot.sensors.setLedDisable()),

			//Move to down flower
			instant(() -> robot.ramp.lower()),
			conditional(
				()->robot.sensors.limelightHit(),
				sequential(
					follow(robot.drive.follower, path5()),
					follow(robot.drive.follower, path7b())
				),
				follow(robot.drive.follower, path7a())
			),

			//Enter flower and agitate
			instant(() -> robot.intake.intake()),
			instant(() -> robot.indexer.intakeSpeed()),
			follow(robot.drive.follower, path8pre()),
			follow(robot.drive.follower, path8()).raceWith(waitMs(800)),
			instant(() -> robot.ramp.agitate()),
			waitMs(300),
			instant(() -> robot.ramp.lower()),
			follow(robot.drive.follower, path8a()),
			//follow(robot.drive.follower, path8b()).raceWith(waitMs(400)),
			//instant(() -> robot.ramp.agitate()),
			//waitMs(100),
			//instant(() -> robot.ramp.lower()),
			//follow(robot.drive.follower, path8a()),
			follow(robot.drive.follower, path8b()).raceWith(waitMs(400)),
			instant(() -> robot.ramp.agitate()),
			waitMs(200),
			instant(() -> robot.ramp.lower()),
			waitMs(1000),

			//Stop intake, indexer, raise ramp, and move to shoot position
			instant(() -> robot.intake.stop()),
			instant(() -> robot.indexer.stop()),

			//Move out of flower and start raising ramp (after short delay)
			parallel(
				follow(robot.drive.follower, path9()),
				sequential(
					waitMs(500),
					instant(() -> robot.ramp.raise())
				)
			),

			//Spin up flywheel and shoot
			//instant(()-> robot.shooter.setShotVelocity(Constants.Auton.AUTO_SHOOT_VELOCITY+20)),
			instant(() -> robot.shooter.spinFlywheel()),
			waitMs(2000),
			instant(() -> robot.shooter.openGate()),
			instant(() -> robot.indexer.shootSpeed()),
			waitMs(2000),
			instant(() -> robot.indexer.stop()),
			instant(() -> robot.shooter.closeGate()),
			instant(() -> robot.shooter.stopFlywheel()),
			waitMs(500),

			//Park
			conditional(
				()->optionParkIsLeft(),

				//Left park
				follow(robot.drive.follower, parkLeft()),
				conditional(
					()->optionParkIsRight(),

					//Right park
					sequential(
						follow(robot.drive.follower, parkRightA()),
						follow(robot.drive.follower, parkRightB())
					),

					//Push park
					sequential(
						follow(robot.drive.follower, parkPushPre()),
						follow(robot.drive.follower, parkPushReadyA()),
						follow(robot.drive.follower, parkPushReadyB()).raceWith(waitMs(1000)),
						follow(robot.drive.follower, parkPush())
					)
				)
			)
		);
	}

	//Alliance-specific settings
	protected org.firstinspires.ftc.teamcode.Constants.General.Alliance thisAlliance=Constants.General.Alliance.UNKNOWN;
	protected int limelightUpPipeline=0;
	protected int limelightDownPipeline=1;

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
		robot.turret.turnToZero();
		//robot.turret.turnToDegrees(0.0);

		//TODO:Temp
		robot.sensors.setLedDisable();

		//Set target shooter speed and hood angle for auto
		robot.shooter.setShotVelocityUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(upShoot,thisAlliance),robot.turret.getVerticalDistanceFromPose(upShoot,thisAlliance));
		robot.shooter.setHoodPositionUsingDistancesFromTarget(robot.turret.getSidewaysDistanceFromPose(upShoot,thisAlliance),robot.turret.getVerticalDistanceFromPose(upShoot,thisAlliance));

		telemetry.addData("Shot Velocity (target):",robot.shooter.getShotVelocity());
		telemetry.addData("Hood Position (target):",robot.shooter.getHoodPosition());

		//OLD -- robot.shooter.setShotVelocity(Constants.Auton.AUTO_SHOOT_VELOCITY);
		//OLD -- robot.shooter.setHoodPosition(Constants.Auton.AUTO_HOOD_POSITION);

		//Set limelight pipeline
		robot.sensors.setLimelightPipeline(limelightUpPipeline);
		telemetry.addData("Limelight - Pipeline:",limelightUpPipeline);

		telemetry.update();
	}

	@Override
	public void init_loop() {
		if(gamepad1.dpadUpWasPressed()) {
			if(Objects.equals(optionPark, "PUSH")) {
				optionPark="RIGHT";
			} else {
				optionPark="LEFT";
			}
		}
		if(gamepad1.dpadDownWasPressed()) {
			if(Objects.equals(optionPark, "LEFT")) {
				optionPark="RIGHT";
			} else {
				optionPark="PUSH";
			}
		}

		telemetry.addLine("Flower Auton Options");
		telemetry.addLine("----------------------");

		if(Objects.equals(optionPark, "LEFT")) {
			telemetry.addLine(">>> Left-side Park");
		} else {
			telemetry.addLine("    Left-side Park");
		}

		if(Objects.equals(optionPark, "RIGHT")) {
			telemetry.addLine(">>> Right-side Park");
		} else {
			telemetry.addLine("    Right-side Park");
		}

		if(Objects.equals(optionPark, "PUSH")) {
			telemetry.addLine(">>> Push Partner");
		} else {
			telemetry.addLine("    Push Partner");
		}

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

		if(robot.sensors.limelightGetCollectStatus()) {
			telemetry.addLine("Checking Limelight Status");
			robot.sensors.limelightCollect();

			if(robot.sensors.limelightHit()) {
				telemetry.addLine("HIT!");
				//robot.sensors.setLedGreen();
			} else {
				telemetry.addLine("MISS!");
				//robot.sensors.setLedRed();
			}
		}

		telemetry.update();

		//Save pose
		PoseStorage.currentPose = robot.drive.follower.pose();
		loopCnt++;
	}

	public boolean optionParkIsLeft() {
		return Objects.equals(optionPark, "LEFT");
	}

	public boolean optionParkIsRight() {
		return Objects.equals(optionPark, "RIGHT");
	}
}
