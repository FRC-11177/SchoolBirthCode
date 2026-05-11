package frc.robot.ScoreMechanism;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Centimeters;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.NewtonMeter;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import dev.doglog.DogLog;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.ScoreMechanism.Constants.ArmConstants;
import frc.robot.ScoreMechanism.Constants.ElevatorConstants;
import frc.utils.Positions.ElevatorPosition;

public class Elevator implements Subsystem{
    public TalonFX LeftMotor, RightMotor, ArmMotor;
    public CANcoder ArmEncoder;
    
    public TalonFXConfiguration LeftConfig, RightConfig, ArmConfig;
    public CANcoderConfiguration EncoderConfig;
    
    public MotionMagicExpoTorqueCurrentFOC request, ArmRequest;
    public Follower follower;

    private static Elevator inst;

    private Elevator(){
        LeftMotor = new TalonFX(ElevatorConstants.LeftID, Constants.bus);
        RightMotor = new TalonFX(ElevatorConstants.RightID, Constants.bus);
        ArmMotor = new TalonFX(ArmConstants.DeviceID, Constants.bus);
        ArmEncoder = new CANcoder(ArmConstants.EncoderID, Constants.bus);
    
        
        LeftConfig = new TalonFXConfiguration();
        RightConfig = new TalonFXConfiguration();
        ArmConfig = new TalonFXConfiguration();
        EncoderConfig = new CANcoderConfiguration();

        LeftConfig.MotorOutput
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake);
        LeftConfig.Feedback
            .withSensorToMechanismRatio(ElevatorConstants.GearRatio);
        LeftConfig.withSlot0(ElevatorConstants.PID);
        LeftConfig.withMotionMagic(ElevatorConstants.MotionMagic);
        LeftConfig.CurrentLimits
            .withStatorCurrentLimit(Amps.of(60))
            .withSupplyCurrentLimit(Amps.of(40))
            .withSupplyCurrentLimitEnable(true)
            .withStatorCurrentLimitEnable(true);

        ArmConfig.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.CounterClockwise_Positive);
        ArmConfig.CurrentLimits
            .withStatorCurrentLimit(40)
            .withStatorCurrentLimitEnable(true);
        ArmConfig.withSlot0(ArmConstants.PID);
        ArmConfig.withMotionMagic(ArmConstants.MotionMagic);
        ArmConfig.Feedback
            .withRotorToSensorRatio(ArmConstants.GearRatio)
            .withFusedCANcoder(ArmEncoder);
        EncoderConfig.MagnetSensor
            .withMagnetOffset(ArmConstants.offset)
            .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive);

        LeftMotor.getConfigurator().apply(LeftConfig);
        RightMotor.getConfigurator().apply(RightConfig);
        ArmMotor.getConfigurator().apply(ArmConfig);
        ArmEncoder.getConfigurator().apply(EncoderConfig);

        request = new MotionMagicExpoTorqueCurrentFOC(0);
        follower = new Follower(LeftMotor.getDeviceID(), MotorAlignmentValue.Opposed);
        ArmRequest = new MotionMagicExpoTorqueCurrentFOC(0);

        this.register();
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            Centimeters.of(LeftMotor.getPosition().getValue().in(Rotations)*ElevatorConstants.DrumCirc.in(Centimeters)),
            new Rotation2d(ArmMotor.getPosition().getValue())
        );
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            Centimeters.per(Seconds).of(LeftMotor.getVelocity().getValue().in(RotationsPerSecond)*ElevatorConstants.DrumCirc.in(Centimeters)),
            new Rotation2d(ArmMotor.getPosition().getValue())
        );
    }
    
    public Command elevate(SwerveModulePosition target){
        return run(() -> {
            LeftMotor.setControl(request.withPosition(Rotations.of(target.distanceMeters/ElevatorConstants.DrumCirc.in(Meters))));
            RightMotor.setControl(follower);
            ArmMotor.setControl(ArmRequest.withPosition(target.angle.getMeasure()));
        }).until(() -> Meters.of(getPosition().distanceMeters).isNear(Meters.of(target.distanceMeters), 0.05));
    }

    public Command elevate(ElevatorPosition pos){
        return elevate(pos.getPosition());
    }

    @Override
    public void periodic(){
        DogLog.log("Elevator/ElevatorState", getState());
        DogLog.log("Elevator/ElevatorPosition", getPosition());
        DogLog.log("Elevator/ElevatorForce", LeftMotor.getMotorKT().getValueAsDouble()*LeftMotor.getTorqueCurrent().getValueAsDouble(), NewtonMeter);
        DogLog.log("Elevator/ArmForce", ArmMotor.getMotorKT().getValueAsDouble()*ArmMotor.getTorqueCurrent().getValueAsDouble(), NewtonMeter);
        
    }

    public static Elevator getInstace(){
        inst = inst == null ? new Elevator() : inst;
        return inst;
    }
}
