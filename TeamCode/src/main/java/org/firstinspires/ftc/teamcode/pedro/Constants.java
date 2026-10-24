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
      c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
      c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
      c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
      c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
   });

   public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
      c.name.set("pinpoint");
      c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
      c.xPodOffset.set(-5.749832513764149);
      c.yPodOffset.set(0.7567341872087614);
      c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
      c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
      c.globalDistanceUnit.set(DistanceUnit.INCH);
      c.offsetUnits.set(DistanceUnit.INCH);
   });

    /*
    == MANUAL ==

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(5.8575); //Forward/Back pod - how far to side of center? (left is positive)
        c.yPodOffset.set(-1.0705); //Strafe pod - how far forward/back of center? (forward is positive)
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED); //xPod=Forward/Back - Should show positive numbers when bot pushed forward
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED); //yPod=Strafe - Should show positive when bot pushed left
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
*/

   public static ForesightConfig foresightConfig = new ForesightConfig(
      c -> {
         Controller primaryTranslationalForward = Controller.proportional(0.3233880786098464);
         Controller secondaryTranslationalForward = Controller.proportional(0.1194832539577326);
         Controller primaryTranslationalLateral = Controller.proportional(0.41402115176263915);
         Controller secondaryTranslationalLateral = Controller.proportional(0.15296975272737268);

         c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
         c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

         c.coast.set(Controller.proportionalFeedforward(0.010089954326760651));
         c.brake.set(Controller.proportionalFeedforward(0.008576461177746554));

         c.headingFeedback.set(Controller.proportional(3.747957163237077));
         c.headingBrakeCoefficients.set(Vector2D.cartesian(0.063197947439259, 0.004704747806542661));

         c.linearBrakeCoefficients.set(Matrix.diag(0.14747014591799912, 0.06469868701883022));
         c.quadraticBrakeCoefficients.set(Matrix.diag(9.534839865827512E-4, 0.0018028789498622552));

         c.maxAchievableForwardVelocity.set(88.3046270722696);
         c.maxAchievableStrafeVelocity.set(73.05170970286329);
         c.naturalForwardDeceleration.set(24.969907373374543);
         c.naturalStrafeDeceleration.set(54.83988017988694);
      }
   );

}