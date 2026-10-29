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
		public static final boolean CONTROLLER_AUTO_HEADING=true;

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
	}

	/*
	 * Autonomous Constants
	 */
	public static final class Auton {
	}

	/*
	 * Sensor Constants
	 */
	public static final class Sensors {
		//Pinpoint Odometry Computer
		public static final String ODOMETRY_ID = "pinpoint";

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
		public static final double TURN_POWER_CLOSE = 1.0; //Power multiplier when within 1 radian (ORIG=1.0)
		public static final double TURN_POWER_FF = 0.12; //Feedforward drive power when correcting small heading errors (ORIG=0.03)
		public static final double TURN_ERROR = 0.06; //Allowed auto-turn error (teleop only)

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

		public static final double RAMP_UP_POS=0.10; //Note, this is the position for both since it is exactly the middle
		public static final double RAMP_DOWN_POS=0.70; //Note, this is for right servo only (left is opposite 1.0-0.17=0.83)
		public static final double RAMP_INCREMENT=0.007; //Note, values above must exactly match (n)*increment
		public static final double RAMP_SLOWDOWN_DISTANCE=0.10;
		public static final double RAMP_MIN_SCALE=0.001;
	}

	/*
	 * Shooter Constants
	 */
	public static final class Shooter {
		public static final String SHOOTER_ID = "shooter";
		public static final DcMotorSimple.Direction SHOOTER_DIRECTION = DcMotorSimple.Direction.REVERSE;
		public static final double SHOOTER_PID_kS=0.14,SHOOTER_PID_kV=0.0003,SHOOTER_PID_kP=0.003;
		public static final double SHOOTER_RAMP_RATE=0.20;
		public static final double SHOOTER_VELOCITY=1450; //Speed for pollen shot

		public static final String SHOOTER_COUNTER_ID = "shooter_counter";
		public static final DcMotorSimple.Direction SHOOTER_COUNTER_DIRECTION = DcMotorSimple.Direction.REVERSE;
		public static final double SHOOTER_COUNTER_PID_kS=0.18,SHOOTER_COUNTER_PID_kV=0.00042,SHOOTER_COUNTER_PID_kP=0.0003;
		public static final double SHOOTER_COUNTER_RAMP_RATE=0.20;
		public static final double SHOOTER_COUNTER_VELOCITY=800; //Speed for pollen shot

		public static final String HOOD_ID = "hood";
		public static final double HOOD_MIN=0.68; //Minimum down position
		public static final double HOOD_MAX=0.26; //Maximum up position


		public static final String BALLSTOP_ID = "ball_stop"; //Axon Max+
		public static final double BALLSTOP_BLOCK=0.360; //Minimum down position
		public static final double BALLSTOP_CLEAR=0.470; //Maximum up position
	}

	/*
	 * Turret Constants
	 */
	public static final class Turret {
		public static final String TURRET_LEFT_ID = "turret_left";
		public static final String TURRET_RIGHT_ID = "turret_right";

		public static final double TURRET_ZERO_POS=0.555;
		public static final double TURRET_MIN=0.07;
		public static final double TURRET_MAX=1.00;
	}

	/*
	 * Indexer Constants
	 */
	public static final class Indexer {
		public static final String INDEXER_ID = "indexer";
		public static final DcMotorSimple.Direction INDEXER_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static final double INDEXER_INTAKE_SPEED=1200;
		public static final double INDEXER_SHOOT_SPEED=1700;
		public static final double INDEXER_REVERSE_SPEED=-800;
	}
}
