package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Servo Reset/Tuning")
public class UtilityServo extends LinearOpMode {
	private Servo turretLeft,turretRight,ballStop,hood,rampRight,rampLeft;

	double turretPos=0.50;
	double ballStopPos=0.50;
	double rampPos=0.50;
	double hoodPos=0.50;

	@Override
	public void runOpMode() throws InterruptedException {
		turretLeft=hardwareMap.get(Servo.class, Constants.Turret.TURRET_LEFT_ID);
		turretRight=hardwareMap.get(Servo.class, Constants.Turret.TURRET_RIGHT_ID);
		ballStop=hardwareMap.get(Servo.class, Constants.Shooter.BALLSTOP_ID);

		hood=hardwareMap.get(Servo.class, Constants.Shooter.HOOD_ID);
		rampRight=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_RIGHT_ID);
		rampLeft=hardwareMap.get(Servo.class, Constants.Ramp.RAMP_LEFT_ID);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			//------------------------------------------------------
			//TURRET

			//Turret - Reset to 50%
			if(gamepad1.a) {
				//turretRight.setPosition(0.50);
				turretLeft.setPosition(0.50);
				turretPos=0.5000;
			}

			//Turret - Tuning
			if (gamepad1.dpad_right) {
				if (turretPos < 1.0) {
					turretPos+=0.0001;
					turretLeft.setPosition(turretPos);
					//turretRight.setPosition(turretPos);
				}
			} else if (gamepad1.dpad_left) {
				if (turretPos > 0.0) {
					turretPos-=0.0001;
					turretLeft.setPosition(turretPos);
					//turretRight.setPosition(turretPos);
				}
			}

			telemetry.addLine("Turrets (Reset 50%) - Press A");
			telemetry.addLine("Turrets (Tuning) - D-Pad Right/Left");
			telemetry.addData("Turrets (Pos):",turretLeft.getPosition());

			//------------------------------------------------------
			//BALL STOP

			//Ball Stop - Reset to 40%
			if(gamepad1.b) {
				ballStop.setPosition(0.40);
				ballStopPos=0.4000;
			}

			//Ball Stop - Tuning
			if (gamepad1.dpad_up) {
				if (ballStopPos < 1.0) {
					ballStopPos+=0.001;
					ballStop.setPosition(ballStopPos);
				}
			} else if (gamepad1.dpad_down) {
				if (ballStopPos > 0.0) {
					ballStopPos-=0.001;
					ballStop.setPosition(ballStopPos);
				}
			}

			telemetry.addLine("Ball Stop (Reset 40%) - Press B");
			telemetry.addLine("Ball Stop (Tuning) - D-Pad Up/Down");
			telemetry.addData("Ball Stop (Pos):",ballStop.getPosition());

			//------------------------------------------------------
			//RAMP

			//Ramp - Reset to 50%
			if(gamepad1.x) {
				rampLeft.setPosition(0.50);
				rampRight.setPosition(0.50);
				rampPos=0.5000;
			}

			//Ramp - Tuning
			if (gamepad1.right_trigger>0.05) {
				if (rampPos < 1.0) {
					rampPos+=0.0002;
					rampLeft.setPosition(rampPos);
					rampRight.setPosition(rampPos);
				}
			} else if (gamepad1.left_trigger>0.05) {
				if (rampPos > 0.0) {
					rampPos-=0.0002;
					rampLeft.setPosition(rampPos);
					rampRight.setPosition(rampPos);
				}
			}

			telemetry.addLine("Ramp (Reset 50%) - Press X");
			telemetry.addLine("Ramp (Tuning) - Triggers");
			telemetry.addData("Ramp (Pos):",rampLeft.getPosition());

			//------------------------------------------------------
			//HOOD

			//Hood - Reset to 50%
			if(gamepad1.y) {
				hood.setPosition(0.50);
				hoodPos=0.5000;
			}

			//Hood - Tuning
			if (gamepad1.right_bumper) {
				if (hoodPos < 1.0) {
					hoodPos+=0.001;
					hood.setPosition(hoodPos);
				}
			} else if (gamepad1.left_bumper) {
				if (hoodPos > 0.0) {
					hoodPos-=0.001;
					hood.setPosition(hoodPos);
				}
			}

			telemetry.addLine("Hood (Reset 50%) - Press Y");
			telemetry.addLine("Hood (Tuning) - Bumpers");
			telemetry.addData("Hood (Pos):",hood.getPosition());


			telemetry.update();
		}
	}
}