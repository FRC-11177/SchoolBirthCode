package frc.utils;

import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.units.measure.Angle;

public class utils {
    public record MoudleConfig(int DriveID, int SteerID, int EncoderID, Angle offset, boolean isRightSide) {
        public InvertedValue getDriveInvertion(){
            return isRightSide ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive;
        }
    }
}
