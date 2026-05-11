package frc.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class Positions {
    public enum ElevatorPosition{
        Intake(new SwerveModulePosition(0,Rotation2d.fromDegrees(0))),
        L1(new SwerveModulePosition(0.5,Rotation2d.fromDegrees(40))),
        L2(new SwerveModulePosition(0.8, Rotation2d.fromDegrees(40))),
        L3(new SwerveModulePosition(1.2, Rotation2d.fromDegrees(40))),
        L4(new SwerveModulePosition(1.8, Rotation2d.fromDegrees(90)));

        SwerveModulePosition pos;
        ElevatorPosition(SwerveModulePosition pos){
            this.pos = pos;
        }

        public SwerveModulePosition getPosition(){
            return pos;
        }
    }

    public enum CoralPosition{
        A(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        B(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        C(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        D(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        E(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        F(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        G(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        H(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        I(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        J(new Pose2d(11,4,Rotation2d.fromDegrees(51.4))),
        K(new Pose2d(11,4,Rotation2d.fromDegrees(51.4)));

        Pose2d p;
        CoralPosition(Pose2d pose){
            p = pose;
        }

        public Pose2d getPose(){
            return p;
        }
    }
}
