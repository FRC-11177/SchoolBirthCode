package frc.robot.SemiAuto;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Drivetrain.Drivetrain;
import frc.robot.Intake.Intake;
import frc.robot.ScoreMechanism.ScoringMechanism;
import frc.robot.Vision.Vision;
import frc.utils.Positions.CoralPosition;
import frc.utils.Positions.ElevatorPosition;

public class SemiAuto {
    private static Drivetrain drivetrain = Drivetrain.getInstance();
    private static ScoringMechanism scorer = ScoringMechanism.getInstance();
    private static Intake intake = Intake.getInstance();
    private static Vision vision = Vision.getInstace();
    private static PIDController FindCoral = new PIDController(0, 0, 0);

    public static Command intakeCoral(){
        return new SequentialCommandGroup(
            drivetrain.drive(new Pose2d(11, 4, Rotation2d.fromDegrees(51.4))).alongWith(intake.intake()),
            drivetrain.drive(() -> MetersPerSecond.of(1), () -> MetersPerSecond.of(1),() -> RotationsPerSecond.of(FindCoral.calculate(drivetrain.getPose().getRotation().getRotations(), vision.getCoralError().get())))
        ).until(intake::hasCoral);
    }

    public static Command score(CoralPosition pose, ElevatorPosition height){
        return new SequentialCommandGroup(
            drivetrain.drive(pose.getPose()),
            scorer.score(height)
        );
    }
}
