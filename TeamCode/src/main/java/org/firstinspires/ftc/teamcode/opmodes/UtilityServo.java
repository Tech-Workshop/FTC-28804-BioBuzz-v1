package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Servo Reset/Tuning")
public class UtilityServo extends LinearOpMode {
	private Servo turretRotationLeft,turretRotationRight,ballStop;

	double turretPos=0.50;
	double ballStopPos=0.50;


	@Override
	public void runOpMode() throws InterruptedException {
		turretRotationLeft=hardwareMap.get(Servo.class, "turret_rotation_left");
		turretRotationRight=hardwareMap.get(Servo.class, "turret_rotation_right");
		ballStop=hardwareMap.get(Servo.class, "ball_stop");

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			//------------------------------------------------------
			//TURRET

			//Turret - Reset to 50%
			if(gamepad1.a) {
				turretRotationLeft.setPosition(0.50);
				turretRotationRight.setPosition(0.50);
				turretPos=0.5000;

			}

			//Turret - Tuning
			if (gamepad1.dpad_right) {
				if (turretPos < 1.0) {
					turretPos+=0.001;
					turretRotationLeft.setPosition(turretPos);
					turretRotationRight.setPosition(turretPos);
				}
			} else if (gamepad1.dpad_left) {
				if (turretPos > 0.0) {
					turretPos-=0.001;
					turretRotationLeft.setPosition(turretPos);
					turretRotationRight.setPosition(turretPos);
				}
			}

			telemetry.addLine("Turrets (Reset 50%) - Press A");
			telemetry.addLine("Turrets (Tuning) - D-Pad Right/Left");
			telemetry.addData("Turrets (Pos):",turretRotationLeft.getPosition());

			//------------------------------------------------------
			//BALL STOP

			//Ball Stop - Reset to 50%
			if(gamepad1.b) {
				ballStop.setPosition(0.50);
				ballStopPos=0.5000;
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

			telemetry.addLine("Ball Stop (Reset 50%) - Press B");
			telemetry.addLine("Ball Stop (Tuning) - D-Pad Up/Down");
			telemetry.addData("Ball Stop (Pos):",ballStop.getPosition());

			telemetry.update();
		}
	}
}