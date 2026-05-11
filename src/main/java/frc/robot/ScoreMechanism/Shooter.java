package frc.robot.ScoreMechanism;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.ScoreMechanism.Constants.ShootingConstants;

public class Shooter implements Subsystem{
    public TalonFXS ShootingMotor;
    public DigitalInput TouchSensor;

    public DutyCycleOut output;
    
    private TalonFXSConfiguration config;


    private static Shooter inst;

    private Shooter(){
        ShootingMotor = new TalonFXS(ShootingConstants.DeviceID,Constants.bus);
        TouchSensor = new DigitalInput(0);
        config = new TalonFXSConfiguration();

        config.MotorOutput
            .withNeutralMode(NeutralModeValue.Coast)
            .withInverted(InvertedValue.Clockwise_Positive);
        config.CurrentLimits
            .withStatorCurrentLimit(Amps.of(20))
            .withStatorCurrentLimitEnable(true);
        
        ShootingMotor.getConfigurator().apply(config);
        this.register();
    }

    public Command run(boolean isIN){
        return runEnd(() -> ShootingMotor.setControl(output.withOutput((isIN ? 1 : -1) * 0.5)), ShootingMotor::stopMotor);
    }

    public boolean hasCoral(){
        return TouchSensor.get();
    }

    public static Shooter getInstance(){
        inst = inst == null ? new Shooter() : inst;
        return inst;
    }
}
