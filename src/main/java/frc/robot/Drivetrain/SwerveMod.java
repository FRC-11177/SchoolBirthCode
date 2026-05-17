package frc.robot.Drivetrain;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.utils.utils.MoudleConfig;
public class SwerveMod {
    public TalonFX DriveMotor, SteerMotor;
    public CANcoder Encoder;

    public MotionMagicVelocityTorqueCurrentFOC DriveRequest;
    public MotionMagicExpoTorqueCurrentFOC SteerRequest;

    private TalonFXConfiguration DriveConfig, SteerConfig;
    private CANcoderConfiguration EncoderConfig;

    public SwerveMod(MoudleConfig config){
        DriveMotor = new TalonFX(config.DriveID());
        SteerMotor = new TalonFX(config.SteerID());
        Encoder = new CANcoder(config.EncoderID());

        DriveRequest = new MotionMagicVelocityTorqueCurrentFOC(0);
        SteerRequest = new MotionMagicExpoTorqueCurrentFOC(0);

        DriveConfig = new TalonFXConfiguration();
        SteerConfig = new TalonFXConfiguration();
        EncoderConfig = new CANcoderConfiguration();

        DriveConfig.MotorOutput
            .withInverted(config.getDriveInvertion())
            .withNeutralMode(NeutralModeValue.Brake);
    }
}
