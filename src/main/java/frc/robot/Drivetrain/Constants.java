package frc.robot.Drivetrain;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecondPerSecond;
import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.util.List;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import frc.utils.utils.MoudleConfig;

public class Constants {
    public static final CANBus bus = new CANBus("rio");
    public static final double DriveGearRatio = 1/(14.0/50*28/16*15/45);
    public static final double SteerGearRatio = 150.0/7;
    public static final Current SlipCurrent = Amps.of(120);
    public static final Distance WheelRadius = Inches.of(2);
    public static final Distance WheelCirc = WheelRadius.times(2*Math.PI);
    public static final PathConstraints AutoConstraints = new PathConstraints(MetersPerSecond.of(5), MetersPerSecondPerSecond.of(9.8), RadiansPerSecond.of(5), RadiansPerSecondPerSecond.of(25));
    public static final LinearVelocity MaxVelocity = MetersPerSecond.of(4.5);
    public static final AngularVelocity MaxOmega = RotationsPerSecond.of(1.5);

    public static final Slot0Configs DrivePID = new Slot0Configs()
        .withKP(0).withKD(0)
        .withKS(0).withKV(0).withKA(0)
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign);
    public static final MotionMagicConfigs DriveMagic = new MotionMagicConfigs()
        .withMotionMagicAcceleration(0)
        .withMotionMagicJerk(0);
    public static final Slot0Configs SteerPID = new Slot0Configs()
        .withKP(0).withKD(0)
        .withKS(0).withKV(0).withKA(0)
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign);
    public static final MotionMagicConfigs SteerMagic = new MotionMagicConfigs()
        .withMotionMagicExpo_kV(0)
        .withMotionMagicExpo_kA(0);

    public static final List<MoudleConfig> ModuleConfigs = List.of(
        new MoudleConfig( //FL
            11, 
            12, 
            10, 
            Rotation.of(0), 
            false),
        new MoudleConfig( //FR
            21, 
            22, 
            2, 
            Rotation.of(0), 
            true),
        new MoudleConfig( //BL
            31, 
            32, 
            3, 
            Rotation.of(0), 
            false),
        new MoudleConfig( //BR
            41, 
            42, 
            4, 
            Rotation.of(0), 
            false)
    );

    public static final List<Translation2d> ModuelLocations = List.of(
        new Translation2d(0,0),
        new Translation2d(0,0),
        new Translation2d(0,0),
        new Translation2d(0,0)
    );

    public static final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(ModuelLocations.toArray(Translation2d[]::new));

}
