package frc.robot.Vision;

import static edu.wpi.first.units.Units.Centimeters;
import static edu.wpi.first.units.Units.Degrees;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;

public class Constants {
    public static final String FrontTagCamName = "FrontTagCam";
    public static final String RearTagCamName = "RearTagCam";
    public static final String CoralTagCamName = "CoralTagCam";
    public static final Transform3d FrontTagPlace = new Transform3d(Centimeters.of(0),Centimeters.of(0),Centimeters.of(0),new Rotation3d(Degrees.zero(), Degrees.zero(), Degrees.zero()));
    public static final Transform3d RearTagPlace = new Transform3d(Centimeters.of(0),Centimeters.of(0),Centimeters.of(0),new Rotation3d(Degrees.zero(), Degrees.zero(), Degrees.zero()));
    public static final AprilTagFieldLayout Field = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark);
}
