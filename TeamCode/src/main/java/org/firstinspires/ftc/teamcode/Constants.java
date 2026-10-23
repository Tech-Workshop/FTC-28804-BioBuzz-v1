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
		public static final boolean CONTROLLER_AUTO_HEADING=true;
		public static final double CONTROLLER_AUTO_ERROR=Math.PI/12;  //Allowed error before auto correct

		//Ball types
		public enum Ball {
			POLLEN,
			NECTAR
		}

		//Alliance types
		public enum Alliance {
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

		//Direction
		public static final DcMotorSimple.Direction DRIVE_LEFT_FRONT_DIRECTION = DcMotorSimple.Direction.FORWARD;
		public static final DcMotorSimple.Direction DRIVE_LEFT_BACK_DIRECTION = DcMotorSimple.Direction.REVERSE;
		public static final DcMotorSimple.Direction DRIVE_RIGHT_FRONT_DIRECTION = DcMotorSimple.Direction.FORWARD;
		public static final DcMotorSimple.Direction DRIVE_RIGHT_BACK_DIRECTION = DcMotorSimple.Direction.FORWARD;

		//Maximum speed multiplier (0-1.0)
		public static final double DRIVE_SPEED = 1.00;
		public static final double TURN_POWER_MULTIPLIER = 0.6;
		public static final double TURN_POWER_EXTENDED = 0.45;

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
		public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.REVERSE;

		public static final String INTAKE_ARM_LEFT_ID = "intake_arm_left"; //Axon Max
		public static final String INTAKE_ARM_RIGHT_ID = "intake_arm_right"; //Axon Max

		public static final double INTAKE_ARM_RETRACTED_POS=0.50; //Note, this is the position for both since it is exactly the middle
		public static final double INTAKE_ARM_EXTENDED_POS=0.17; //Note, this is for right servo only (left is opposite 1.0-0.17=0.83)
		public static final double INTAKE_ARM_INCREMENT=0.01; //Note, values above must exactly match (n)*increment

		public static final double INTAKE_IN_SPEED=1200;
		public static final double INTAKE_OUT_SPEED=-1000;
	}

	/*
	 * Shooter Constants
	 */
	public static final class Shooter {
		public static final String SHOOTER_NECTAR_ID = "shooter_nectar";
		public static final DcMotorSimple.Direction SHOOTER_NECTAR_DIRECTION = DcMotorSimple.Direction.FORWARD;

		public static final String SHOOTER_POLLEN_ID = "shooter_pollen";
		public static final DcMotorSimple.Direction SHOOTER_POLLEN_DIRECTION = DcMotorSimple.Direction.FORWARD;

		public static final double SHOOTER_PID_kS=0.05,SHOOTER_PID_kV=0.00034,SHOOTER_PID_kP=0.0009;
		public static final double SHOOTER_RAMP_RATE=0.20;

		public static final double SHOOTER_NECTAR_VELOCITY=2175; //Speed for nectar ball shot (2075)
		public static final double SHOOTER_POLLEN_VELOCITY=2100; //Speed for pollen ball shot (1900)

		public static final String BALLSTOP_NECTAR_ID = "ballstop_nectar"; //Axon Max+
		public static final double BALLSTOP_NECTAR_BLOCK=0.63; //Minimum down position
		public static final double BALLSTOP_NECTAR_CLEAR=0.42; //Maximum up position

		public static final String BALLSTOP_POLLEN_ID = "ballstop_pollen"; //Axon Mini+
		public static final double BALLSTOP_POLLEN_BLOCK=0.59; //Minimum down position
		public static final double BALLSTOP_POLLEN_CLEAR=0.25; //Maximum up position
	}

	/*
	 * Turret Constants
	 */
	public static final class Turret {
		public static final String TURRET_NECTAR_ID = "turret_nectar";
		public static final String TURRET_POLLEN_ID = "turret_pollen";
	}

	/*
	 * Indexer Constants
	 */
	public static final class Indexer {
		public static final String INDEXER_ID = "indexer";
		public static final DcMotorSimple.Direction INDEXER_DIRECTION = DcMotorSimple.Direction.FORWARD;

		public static final double INDEXER_INDEX_SPEED=600;
		public static final double INDEXER_SORT_SPEED=200;
		public static final double INDEXER_REVERSE_SPEED=-600;
	}

	/*
	 * Sorter Constants
	 */
	public static final class Sorter {
		public static final String SORTER_ID = "sorter";
		public static final double SORTER_EXTEND=0.38; //Extended (sort) position
		public static final double SORTER_RETRACT=0.497; //Retracted (home) position
	}
}
