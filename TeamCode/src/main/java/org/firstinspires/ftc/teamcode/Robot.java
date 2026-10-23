package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants.General.Ball;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Sorter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

public class Robot {
	//Subsystems
	public Drive drive;
	public Intake intake;
	public Indexer indexer;
	public Shooter shooterNectar,shooterPollen;
	public Turret turretNectar,turretPollen;
	public Sorter sorter;

	//Global variables
	public Telemetry telemetry;
	public Constants.General.Alliance alliance;

	private static Robot instance = null;
	private boolean enabled; //Required for instance

	public static Robot getInstance() {
		if(instance==null) {
			instance=new Robot();
		}
		instance.enabled=true;
		return instance;
	}

	public void init(final HardwareMap hardwareMap, Telemetry telemetry, Pose startPose, Constants.General.Alliance alliance) {
		this.alliance=alliance;

		//Initialize all subsystems
		this.drive=new Drive(hardwareMap,telemetry,(startPose==null ? new Pose(72.0,72.0) : startPose),alliance);
		this.intake=new Intake(hardwareMap,telemetry);
		this.indexer=new Indexer(hardwareMap,telemetry);
		this.sorter=new Sorter(hardwareMap,telemetry);
		this.shooterNectar=new Shooter(hardwareMap,telemetry, Ball.NECTAR);
		this.shooterPollen=new Shooter(hardwareMap,telemetry,Ball.POLLEN);
		this.turretNectar=new Turret(hardwareMap,telemetry,Ball.NECTAR,alliance);
		this.turretPollen=new Turret(hardwareMap,telemetry,Ball.POLLEN,alliance);
	}
}
