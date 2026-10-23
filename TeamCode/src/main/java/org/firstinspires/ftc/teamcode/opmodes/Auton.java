package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "RedAuton", group = "Autonomous")
public class Auton extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 10, 90);
    private final Pose path1 = poseFactory.of(56, 23, 90);
    private final Pose point2 = poseFactory.of(56, 18, -90);
    private final Pose point3 = poseFactory.of(15, 18, 180);
    private final Pose point4 = poseFactory.of(36, 18, 0);
    private final Pose point5 = poseFactory.of(36, 120, 90);
    private final Pose point6 = poseFactory.of(56, 120, -90);
    private final Pose point7 = poseFactory.of(36, 120, 180);
    private final Pose point8 = poseFactory.of(12, 120, 180);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4()),
                follow(follower, path5()),
                follow(follower, path6()),
                follow(follower, path7()),
                follow(follower, path8())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path path1() {
        return line(start, path1).tangent();
    }

    public Path path2() {
        return line(path1, point2).tangent();
    }

    public Path path3() {
        return line(point2, point3).tangent();
    }

    public Path path4() {
        return line(point3, point4).reverseTangent();
    }

    public Path path5() {
        return line(point4, point5).reverseTangent();
    }

    public Path path6() {
        return line(point5, point6).tangent();
    }

    public Path path7() {
        return line(point6, point7).tangent();
    }

    public Path path8() {
        return line(point7, point8).tangent();
    }
}