package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Config
public class Constants {
	/*
	 * General Constants
	 */
	public static final class General {
		//Debug mode
		public static final boolean DEBUG_MODE=false;

		//Controller
		public static final double CONTROLLER_DEADBAND=0.05;
		public static final double CONTROLLER_AUTO_ERROR=Math.PI/10;

		//Alliance types
		public enum Alliance {
			UNKNOWN,
			BLUE,
			RED
		}

		//Hive coordinates (for turret auto-aim)
		public static List<Double> hiveBlueClose = Arrays.asList(83.5,55.0);
		public static List<Double> hiveBlueFar = Arrays.asList(83.5,86.5);
		public static List<Double> hiveRedClose = Arrays.asList(58.0,55.0);
		public static List<Double> hiveRedFar = Arrays.asList(58.0,86.5);

		//Aiming mode
		public enum AimMode {
			TURRET,
			TURN,
			MANUAL
		}

		public static final AimMode defaultAimMode=AimMode.TURRET;
	}

	/*
	 * Autonomous Constants
	 */
	public static final class Auton {
		public static double AUTO_SHOOT_VELOCITY=1570;
		public static double AUTO_HOOD_POSITION=0.77;
	}

	/*
	 * Sensor Constants
	 */
	public static final class Sensors {
		//Limelight
		public static final String LIMELIGHT_ID = "limelight";

		//Beam Break Receiver
		public static final String BEAM_BREAK_ID = "beam_break";

		//LED Indicator Lights
		public static final String LED_LEFT_GREEN_ID = "led_left_green";
		public static final String LED_LEFT_RED_ID = "led_left_red";
		public static final String LED_RIGHT_GREEN_ID = "led_right_green";
		public static final String LED_RIGHT_RED_ID = "led_right_red";
	}

	/*
	 * Drivetrain Constants
	 */
	public static final class Drive {
		//Motor device IDs
		public static final String DRIVE_LEFT_FRONT_ID = "drive_left_front";
		public static final String DRIVE_LEFT_BACK_ID = "drive_left_back";
		public static final String DRIVE_RIGHT_FRONT_ID = "drive_right_front";
		public static final String DRIVE_RIGHT_BACK_ID = "drive_right_back";

		//Maximum speed multiplier (0-1.0)
		public static final double DRIVE_SPEED = 0.80;
		public static final double TURN_POWER_MULTIPLIER = 0.45;

		//Teleop Heading Correction
		public static final double TURN_KP = 0.8;           // power per radian of error (orig=1.2)
		public static final double TURN_KD = 0.1;          // power per rad/s of angular velocity (orig=0.15)
		public static final double TURN_KS = 0.12;          // minimum power to overcome friction (your TURN_POWER_FF)
		public static final double TURN_ERROR = 0.03; //Allowed auto-turn error (teleop only)
		public static final double TURN_RATE_TOL = Math.toRadians(3.0);   // rad/s, "stopped enough"

		//Strafing multiplier
		public static final double STRAFE_MULTIPLIER = 1.2;
	}

	/*
	 * Intake Constants
	 */
	public static final class Intake {
		public static final String INTAKE_ID = "intake";
		public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.FORWARD;

		public static final double INTAKE_IN_SPEED=1700;
		public static final double INTAKE_OUT_SPEED=-1200;

		public static final double INTAKE_EXPEL_TIME=1.5; //Seconds to expel after intake button is released
	}

	/*
	 * Flower Ramp Constants
	 */
	public static final class Ramp {
		public static final String RAMP_LEFT_ID = "ramp_left"; //Axon Max
		public static final String RAMP_RIGHT_ID = "ramp_right"; //Axon Max

		public static final double RAMP_UP_POS=0.10;
		public static final double RAMP_AGITATE_POS=0.62;
		public static final double RAMP_DOWN_POS=0.70;
		public static final double RAMP_INCREMENT=0.015;
		public static final double RAMP_SLOWDOWN_DISTANCE=0.10;
		public static final double RAMP_MIN_SCALE=0.002;
	}

	/*
	 * Shooter Constants
	 */
	@Config
	public static class Shooter {
		//Shooter (flywheel)
		public static String SHOOTER_LEFT_ID = "shooter_left";
		public static DcMotorSimple.Direction SHOOTER_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static String SHOOTER_RIGHT_ID = "shooter_right";
		public static DcMotorSimple.Direction SHOOTER_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;

		public static double SHOOTER_PID_kS=0.135,SHOOTER_PID_kV=0.000307,SHOOTER_PID_kP=0.003;
		public static double SHOOTER_RAMP_RATE=0.20;
		public static double SHOOTER_VELOCITY=1100; //Speed for pollen shot (default)
		public static double SHOOTER_ERROR_ALLOWED=25; //Allowed error for shot (ticks/sec)
		public static boolean SHOOTER_SPEED_AUTO=true; //Automatically set speed and hood angle based on alliance and current field position

		// Voltage Compensation
		public static boolean ENABLE_VOLTAGE_COMPENSATION = true;
		public static double NOMINAL_VOLTAGE = 12.0;
		public static final double VELOCITY_PER_VOLT = -70.0;

		//Hood
		public static final String HOOD_ID = "hood";
		public static final double HOOD_DEFAULT=0.45; //Default position
		public static final double HOOD_MIN=0.39; //Minimum down position
		public static final double HOOD_MAX=0.77; //Maximum up position

		//Ball stop
		public static String BALLSTOP_ID = "ball_stop"; //Axon Max+
		public static double BALLSTOP_BLOCK=0.360; //Minimum down position
		public static double BALLSTOP_CLEAR=0.470; //Maximum up position
	}

	/*
	 * Turret Constants
	 */
	public static final class Turret {
		public static final String TURRET_LEFT_ID = "turret_left";
		public static final String TURRET_RIGHT_ID = "turret_right";

		public static final double TURRET_ZERO_POS=0.63; //Orig: 0.639
		public static final double TURRET_POS_PER_DEGREE=0.002635; //Tuned -- 0.002908 too large, 0.0025 too low, 0.00265 too high
		public static final double TURRET_UPDATE_ERROR=0.003; //Allowed error before position is updated

		public static final double TURRET_MIN=0.010;
		public static final double TURRET_MAX=0.990;
		public static final double TURRET_DEADZONE_MIN=120.0; //Math.floor(TURRET_MAX-TURRET_ZERO_POS)/TURRET_POS_PER_DEGREE; //120 degrees
		public static final double TURRET_DEADZONE_MAX=144.0; //Math.ceil(((TURRET_ZERO_POS-TURRET_MIN)/TURRET_POS_PER_DEGREE)-360.0); //-144 degrees
	}

	/*
	 * Indexer Constants
	 */
	public static final class Indexer {
		public static final String INDEXER_ID = "indexer";
		public static final DcMotorSimple.Direction INDEXER_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static final double INDEXER_INTAKE_SPEED=1200;
		public static final double INDEXER_SHOOT_SPEED=2000;
		public static final double INDEXER_REVERSE_SPEED=-800;
	}
}