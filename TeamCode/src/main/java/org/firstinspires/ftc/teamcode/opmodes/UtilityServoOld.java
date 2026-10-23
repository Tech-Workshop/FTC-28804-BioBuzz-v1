package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility[OLD]: Servo Reset/Tuning")
public class UtilityServoOld extends LinearOpMode {
	private Servo ballstopNectar,ballstopPollen,armRight,armLeft,sorter;
	private ServoImplEx turretNectar,turretPollen;

	double turretPos=0.50;
	double ballstopNectarPos=0.50;
	double ballstopPollenPos=0.50;
	double armRightPos=0.40;
	double armLeftPos=0.60;
	double sorterPos=0.50;

	@Override
	public void runOpMode() throws InterruptedException {
		turretNectar=hardwareMap.get(ServoImplEx.class, Constants.Turret.TURRET_NECTAR_ID);
		turretNectar.setPwmRange(new PwmControl.PwmRange(500, 2500));
		turretPollen=hardwareMap.get(ServoImplEx.class, Constants.Turret.TURRET_POLLEN_ID);
		turretPollen.setPwmRange(new PwmControl.PwmRange(500, 2500));

		ballstopNectar=hardwareMap.get(Servo.class, Constants.Shooter.BALLSTOP_NECTAR_ID);
		ballstopPollen=hardwareMap.get(Servo.class, Constants.Shooter.BALLSTOP_POLLEN_ID);

		armRight=hardwareMap.get(Servo.class, Constants.Intake.INTAKE_ARM_RIGHT_ID);
		armLeft=hardwareMap.get(Servo.class, Constants.Intake.INTAKE_ARM_LEFT_ID);

		sorter=hardwareMap.get(Servo.class, Constants.Sorter.SORTER_ID);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			//------------------------------------------------------
			//TURRET

			//Turret - Reset to 50%
			if(gamepad1.a) {
				turretNectar.setPosition(0.50);
				turretPollen.setPosition(0.50);
			}

			//Turret - Tuning
			if (gamepad1.dpad_right) {
				if (turretPos < 1.0) {
					turretPos+=0.001;
					turretNectar.setPosition(turretPos);
					turretPollen.setPosition(turretPos);
				}
			} else if (gamepad1.dpad_left) {
				if (turretPos > 0.0) {
					turretPos-=0.001;
					turretNectar.setPosition(turretPos);
					turretPollen.setPosition(turretPos);
				}
			}

			telemetry.addLine("Turrets (Reset 50%) - Press A");
			telemetry.addLine("Turrets (Tuning) - D-Pad Right/Left");
			telemetry.addData("Turrets (Pos):",turretNectar.getPosition());

			//------------------------------------------------------
			//BALL STOPS

			if(gamepad1.x) {
				ballstopNectar.setPosition(0.50);
				ballstopPollen.setPosition(0.50);
			}

			//Ball Stops - Tuning
			if (gamepad1.left_trigger>0.05) {
				if (ballstopNectarPos < 1.0) {
					ballstopNectarPos+=0.0001;
					//ballstopNectar.setPosition(ballstopNectarPos);
					ballstopPollen.setPosition(ballstopNectarPos);
				}
			} else if (gamepad1.right_trigger>0.05) {
				if (ballstopNectarPos > 0.0) {
					ballstopNectarPos-=0.0001;
					//ballstopNectar.setPosition(ballstopNectarPos);
					ballstopPollen.setPosition(ballstopNectarPos);
				}
			}

			telemetry.addLine("Ball Stops (Reset 50%) - Press X");
			telemetry.addLine("Ball Stops (Tuning) - Triggers");
			telemetry.addData("Ball Stops (Pos):",ballstopNectarPos);

			//------------------------------------------------------
			//INTAKE ARMS

			if(gamepad1.b) {
				armRight.setPosition(0.40);
				armLeft.setPosition(0.60);
			}

			//Intake Arms - Tuning
			if (gamepad1.dpad_up) {
				if (armRightPos < 1.0) {
					armRightPos+=0.0002;
					armLeftPos-=0.0002;

					armRight.setPosition(armRightPos);
					armLeft.setPosition(armLeftPos);
				}
			} else if (gamepad1.dpad_down) {
				if (armRightPos > 0.0) {
					armRightPos-=0.0002;
					armLeftPos+=0.0002;

					armRight.setPosition(armRightPos);
					armLeft.setPosition(armLeftPos);
				}
			}

			telemetry.addLine("Intake Arms (Reset 40%) - Press B");
			telemetry.addLine("Intake Arms (Tuning) - D-Pad Up/Down");
			telemetry.addData("Intake Arms - Right (Pos):",armRightPos);
			telemetry.addData("Intake Arms - Left (Pos):",armLeftPos);

			//------------------------------------------------------
			//SORTER

			if(gamepad1.y) {
				sorter.setPosition(0.50);
			}

			//Sorter - Tuning
			if (gamepad1.left_bumper) {
				if (sorterPos < 1.0) {
					sorterPos+=0.0001;
					sorter.setPosition(sorterPos);
				}
			} else if (gamepad1.right_bumper) {
				if (sorterPos > 0.0) {
					sorterPos-=0.0001;
					sorter.setPosition(sorterPos);
				}
			}

			telemetry.addLine("Sorter (Reset 50%) - Press Y");
			telemetry.addLine("Sorter (Tuning) - Bumpers");
			telemetry.addData("Sorter (Pos):",sorterPos);

			telemetry.update();
		}
	}
}