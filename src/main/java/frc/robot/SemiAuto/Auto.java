package frc.robot.SemiAuto;

import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.utils.Positions.CoralPosition;
import frc.utils.Positions.ElevatorPosition;

public class Auto {
    public static Command getAutoCommand(){
        return new SequentialCommandGroup(
            SemiAuto.score(CoralPosition.D, ElevatorPosition.L4),
            SemiAuto.intakeCoral(),
            SemiAuto.score(CoralPosition.K, ElevatorPosition.L4),
            SemiAuto.intakeCoral(),
            SemiAuto.score(CoralPosition.J, ElevatorPosition.L4),
            SemiAuto.intakeCoral(),
            SemiAuto.score(CoralPosition.J, ElevatorPosition.L3),
            SemiAuto.intakeCoral(),
            SemiAuto.score(CoralPosition.J, ElevatorPosition.L3)
        ).withTimeout(Seconds.of(15));
    }
}
