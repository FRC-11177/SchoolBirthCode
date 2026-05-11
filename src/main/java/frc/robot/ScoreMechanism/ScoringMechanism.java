package frc.robot.ScoreMechanism;

import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.wpilibj2.command.Command;
import frc.utils.Positions.ElevatorPosition;

public class ScoringMechanism {
    public Elevator elevator;
    public Shooter shooter;
    private static ScoringMechanism inst;

    private ScoringMechanism(){
        elevator = Elevator.getInstace();
        shooter = Shooter.getInstance();
    }

    public Command score(ElevatorPosition pos){
        return elevator.elevate(pos.getPosition()).andThen(shooter.run(pos == ElevatorPosition.Intake ? true : false).withTimeout(Seconds.of(2)));
    }

    public static ScoringMechanism getInstance(){
        inst = inst == null ? new ScoringMechanism() : inst;
        return inst;
    }
}
