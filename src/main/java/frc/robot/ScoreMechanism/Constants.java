package frc.robot.ScoreMechanism;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Millimeters;
import static edu.wpi.first.units.Units.Rotation;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

public class Constants {
    public static final CANBus bus = new CANBus("rio");
    public class ElevatorConstants {
        public static final int LeftID = 51;
        public static final int RightID = 52;
        public static final double GearRatio = 10.71;
        public static final Distance DrumCirc = Millimeters.of(50).times(Math.PI);

        public static final Slot0Configs PID = new Slot0Configs()
            .withKP(0).withKD(0)
            .withKS(0).withKV(0).withKA(0)
            .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        public static final MotionMagicConfigs MotionMagic = new MotionMagicConfigs()
            .withMotionMagicExpo_kV(0)
            .withMotionMagicExpo_kA(0);
    }

    public class ArmConstants{
        public static final CANBus bus = new CANBus("rio");
        public static final int DeviceID = 53;
        public static final int EncoderID = 54;
        public static final double GearRatio = 10;
        public static final Angle offset = Rotation.of(0);
        public static final Slot0Configs PID = new Slot0Configs()
            .withKP(0).withKD(0)
            .withKS(0).withKV(0).withKA(0).withKG(0)
            .withGravityArmPositionOffset(Degrees.of(0))
            .withGravityType(GravityTypeValue.Arm_Cosine)
            .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        public static final MotionMagicConfigs MotionMagic = new MotionMagicConfigs()
            .withMotionMagicExpo_kV(0)
            .withMotionMagicExpo_kA(0);
            
    }

    public class ShootingConstants {
        public static final CANBus bus = new CANBus("rio");
        public static final int DeviceID = 55;
        
    }
}
