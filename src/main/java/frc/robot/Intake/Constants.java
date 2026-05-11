package frc.robot.Intake;

import static edu.wpi.first.units.Units.Centimeters;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

public class Constants {
    public static final CANBus bus = new CANBus("rio");
    public static final int LiftID = 56;
    public static final int RollID = 57;
    public static final int IndexID = 58;
    public static final int LiftEncoderID = 59;
    public static final double LiftGearRatio = 1;
    public static final Distance RollCirc = Centimeters.of(3).times(Math.PI);
    public static final Angle LiftOffset = Rotations.of(0);

    public static final Slot0Configs LiftPID = new Slot0Configs()
        .withKP(0).withKD(0)
        .withKS(0).withKV(0).withKA(0).withKG(0)
        .withGravityType(GravityTypeValue.Arm_Cosine)
        .withGravityArmPositionOffset(Degrees.of(0))
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
    public static final MotionMagicConfigs LiftMagic = new MotionMagicConfigs()
        .withMotionMagicExpo_kV(0)
        .withMotionMagicExpo_kA(0);

    public static final Slot0Configs RollPID = new Slot0Configs()
        .withKP(0).withKD(0)
        .withKS(0).withKV(0).withKA(0)
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign);
    public static final MotionMagicConfigs RollMagic = new MotionMagicConfigs()
        .withMotionMagicAcceleration(0)
        .withMotionMagicJerk(0);

}
