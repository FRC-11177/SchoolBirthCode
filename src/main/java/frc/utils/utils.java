package frc.utils;

import edu.wpi.first.units.measure.Angle;

public class utils {
    public record MoudleConfig(int DriveID, int SteerID, int EncoderID, Angle offset, boolean isRightSide) {
    }
}
