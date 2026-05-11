package frc.robot.Intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Drivetrain.Drivetrain;

public class Intake implements Subsystem{
    public TalonFX LiftMotor, RollMotor, IndexMotor;
    public CANcoder LiftEncoder;
    public DigitalInput CoralDetector;
    
    private TalonFXConfiguration LiftConfig, RollConfig, IndexConfig;
    private CANcoderConfiguration EncoderConfig;
    private static Intake inst;

    public MotionMagicExpoTorqueCurrentFOC LiftPID;
    public MotionMagicVelocityTorqueCurrentFOC RollPID;
    public DutyCycleOut IndexPID;

    private Intake(){
        LiftMotor = new TalonFX(Constants.LiftID, Constants.bus);
        RollMotor = new TalonFX(Constants.RollID, Constants.bus);
        IndexMotor = new TalonFX(Constants.IndexID, Constants.bus);
        CoralDetector = new DigitalInput(1);

        LiftPID = new MotionMagicExpoTorqueCurrentFOC(0);
        RollPID = new MotionMagicVelocityTorqueCurrentFOC(0);

        LiftConfig = new TalonFXConfiguration();
        RollConfig = new TalonFXConfiguration();
        IndexConfig = new TalonFXConfiguration();

        LiftConfig.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.CounterClockwise_Positive);
        LiftConfig.Feedback
            .withRotorToSensorRatio(Constants.LiftGearRatio)
            .withFusedCANcoder(LiftEncoder);
        LiftConfig.CurrentLimits
            .withStatorCurrentLimit(Amps.of(50))
            .withSupplyCurrentLimit(Amps.of(30))
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimitEnable(true);
        LiftConfig.withSlot0(Constants.LiftPID);
        LiftConfig.withMotionMagic(Constants.LiftMagic);
        
        RollConfig.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.CounterClockwise_Positive);
        RollConfig.CurrentLimits
            .withStatorCurrentLimit(Amps.of(30))
            .withStatorCurrentLimitEnable(true);
        RollConfig.withSlot0(Constants.RollPID);
        RollConfig.withMotionMagic(Constants.RollMagic);
        
        IndexConfig.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive);
        IndexConfig.CurrentLimits
            .withStatorCurrentLimit(10)
            .withStatorCurrentLimitEnable(true);

        EncoderConfig.MagnetSensor
            .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive)
            .withMagnetOffset(Constants.LiftOffset);
        
        LiftMotor.getConfigurator().apply(LiftConfig);
        RollMotor.getConfigurator().apply(RollConfig);
        IndexMotor.getConfigurator().apply(IndexConfig);
        LiftEncoder.getConfigurator().apply(EncoderConfig);

        this.register();
    }

    public Command intake(){
        return run(() -> {
            LiftMotor.setControl(LiftPID.withPosition(Degrees.of(60)));
            RollMotor.setControl(RollPID.withVelocity(RotationsPerSecond.of(2*(Math.hypot(Drivetrain.getInstance().getSpeeds().vxMetersPerSecond, Drivetrain.getInstance().getSpeeds().vyMetersPerSecond)/Constants.RollCirc.div(2*Math.PI).in(Meters)))));
            IndexMotor.setControl(IndexPID.withOutput(0.6));
        }).until(this::hasCoral).finallyDo(() -> {
            LiftMotor.setControl(LiftPID.withPosition(0));
            RollMotor.stopMotor();
            IndexMotor.stopMotor();
        });
    }

    public boolean hasCoral(){
        return CoralDetector.get();
    }

    public static Intake getInstance(){
        inst = inst == null ? new Intake() : inst;
        return inst;
    }
}
