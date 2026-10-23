package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
           new PinpointLocalizer(h, localizerConfig),
           new Mecanum(h, drivetrainConfig),
           new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("drive_left_front");
        c.frontRightName.set("drive_right_front");
        c.backLeftName.set("drive_left_back");
        c.backRightName.set("drive_right_back");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(6.778); //Forward/Back pod - how far to side of center? (left is positive)
        c.yPodOffset.set(2.218); //Strafe pod - how far forward/back of center? (forward is positive)
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED); //xPod=Forward/Back - Should show positive numbers when bot pushed forward
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD); //yPod=Strafe - Should show positive when bot pushed left
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

   public static ForesightConfig foresightConfig = new ForesightConfig(
      c -> {
         Controller primaryTranslationalForward = Controller.proportional(0.26187515129153144);
         Controller secondaryTranslationalForward = Controller.proportional(0.09675587096930478);
         Controller primaryTranslationalLateral = Controller.proportional(0.38598414997846636);
         Controller secondaryTranslationalLateral = Controller.proportional(0.14261082973060604);

         c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
         c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

         c.coast.set(Controller.proportionalFeedforward(0.011251012947229362));
         c.brake.set(Controller.proportionalFeedforward(0.009563361005144957));

         c.headingFeedback.set(Controller.proportional(5.397057617932478));
         c.headingBrakeCoefficients.set(Vector2D.cartesian(0.058579891359913, 0.00673565400560476));

         c.linearBrakeCoefficients.set(Matrix.diag(0.1402894780174325, 0.08103235942697788));
         c.quadraticBrakeCoefficients.set(Matrix.diag(8.437402672612938E-4, 0.0016492958327959932));

         c.maxAchievableForwardVelocity.set(80.94115209911564);
         c.maxAchievableStrafeVelocity.set(64.08392809926758);
         c.naturalForwardDeceleration.set(26.619976695565025);
         c.naturalStrafeDeceleration.set(54.339626798052045);
      }
   );
}