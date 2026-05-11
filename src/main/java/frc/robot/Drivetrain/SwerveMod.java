package frc.robot.Drivetrain;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.Current;
import frc.utils.utils.MoudleConfig;

public class SwerveMod {
    public TalonFX DriveMotor, SteerMotor;
    public CANcoder Encoder;
    
    private TalonFXConfiguration DriveConfig, SteerConfig;
    private CANcoderConfiguration EncoderConfig;

    public MotionMagicVelocityTorqueCurrentFOC DriveRequest;
    public MotionMagicExpoTorqueCurrentFOC SteerRequest;

    public SwerveMod(MoudleConfig config){
        DriveMotor = new TalonFX(config.DriveID(), Constants.bus);
        SteerMotor = new TalonFX(config.SteerID(), Constants.bus);
        Encoder = new CANcoder(config.EncoderID(), Constants.bus);

        DriveConfig = new TalonFXConfiguration();
        SteerConfig = new TalonFXConfiguration();
        EncoderConfig = new CANcoderConfiguration();

        DriveConfig.MotorOutput
            .withInverted(config.isRightSide() ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake);
        DriveConfig.Feedback
            .withSensorToMechanismRatio(Constants.DriveGearRatio);
        DriveConfig.CurrentLimits
            .withStatorCurrentLimit(Constants.SlipCurrent)
            .withSupplyCurrentLimit(Amps.of(70))
            .withStatorCurrentLimitEnable(true)
            .withStatorCurrentLimitEnable(true);
        DriveConfig.withSlot0(Constants.DrivePID);
        DriveConfig.withMotionMagic(Constants.DriveMagic);

        SteerConfig.MotorOutput
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake);
        SteerConfig.Feedback
            .withRotorToSensorRatio(Constants.SteerGearRatio)
            .withFusedCANcoder(Encoder);
        SteerConfig.CurrentLimits
            .withStatorCurrentLimit(Amps.of(60))
            .withStatorCurrentLimitEnable(true);
        SteerConfig.withSlot0(Constants.SteerPID);
        SteerConfig.withMotionMagic(Constants.SteerMagic);

        EncoderConfig.MagnetSensor
            .withSensorDirection(SensorDirectionValue.Clockwise_Positive)
            .withMagnetOffset(config.offset());

        DriveMotor.getConfigurator().apply(DriveConfig);
        SteerMotor.getConfigurator().apply(SteerConfig);
        Encoder.getConfigurator().apply(EncoderConfig);

        DriveRequest = new MotionMagicVelocityTorqueCurrentFOC(0);
        SteerRequest = new MotionMagicExpoTorqueCurrentFOC(0);
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            DriveMotor.getVelocity().getValue().in(RotationsPerSecond)*Constants.WheelCirc.in(Meters),
            new Rotation2d(SteerMotor.getPosition().getValue())
        );
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            DriveMotor.getPosition().getValue().in(Rotations)*Constants.WheelCirc.in(Meters),
            new Rotation2d(SteerMotor.getPosition().getValue())
        );
    }

    public void setState(SwerveModuleState state){
        state.optimize(getState().angle);

        DriveMotor.setControl(DriveRequest.withVelocity(RotationsPerSecond.of(state.speedMetersPerSecond/Constants.WheelCirc.in(Meters))));
        SteerMotor.setControl(SteerRequest.withPosition(state.angle.getMeasure()));
    }

    public void setState(SwerveModuleState state, Current ff){
        state.optimize(getState().angle);

        DriveMotor.setControl(DriveRequest.withVelocity(RotationsPerSecond.of(state.speedMetersPerSecond/Constants.WheelCirc.in(Meters))).withFeedForward(ff));
        SteerMotor.setControl(SteerRequest.withPosition(state.angle.getMeasure()));
    }
}
