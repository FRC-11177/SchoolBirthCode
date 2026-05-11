package frc.robot.Drivetrain;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import dev.doglog.DogLog;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Vision.Vision;

public class Drivetrain implements Subsystem{
    public List<SwerveMod> modules;
    public Pigeon2 gyro;
    
    public SwerveDrivePoseEstimator PoseEstimator;
    private Vision vision;
    
    private static Drivetrain inst;

    private Drivetrain(){
        modules = Constants.ModuleConfigs.stream().map(SwerveMod::new).toList();
        gyro = new Pigeon2(0);
        PoseEstimator = new SwerveDrivePoseEstimator(Constants.kinematics, gyro.getRotation2d(), getPosition(), new Pose2d());
        this.register();
        autoInit();
        vision = Vision.getInstace();
    }

    public SwerveModulePosition[] getPosition(){
        return modules.stream().map(SwerveMod::getPosition).toArray(SwerveModulePosition[]::new);
    }

    public SwerveModuleState[] getState(){
        return modules.stream().map(SwerveMod::getState).toArray(SwerveModuleState[]::new);
    }

    public Command drive(Supplier<LinearVelocity> vx, Supplier<LinearVelocity> vy, Supplier<AngularVelocity> omega){
        return drive(new ChassisSpeeds(vx.get(), vy.get(),omega.get()));
    }

    public ChassisSpeeds getSpeeds(){
        return Constants.kinematics.toChassisSpeeds(getState());
    }

    public Command drive(ChassisSpeeds speeds){
        return run(() -> {
            ChassisSpeeds spds =  ChassisSpeeds.discretize(speeds, 0.02);
            SwerveModuleState[] states = Constants.kinematics.toSwerveModuleStates(speeds);
            DogLog.log("Drivetrain/TargetSpeeds", spds);
            DogLog.log("Drivetrain/TargetState", states);

            IntStream.range(0, 4).forEach(i -> modules.get(i).setState(states[i]));
        });
    }

    public Command drive(Pose2d pose){
        try{
            return new PathfindingCommand(
            pose, 
            Constants.AutoConstraints, 
            this::getPose, 
            this::getSpeeds,
            (speeds, ff) -> drive(speeds), 
            new PPHolonomicDriveController(
                new PIDConstants(0,0), 
                new PIDConstants(0,0)), 
            RobotConfig.fromGUISettings(), 
            this);
        }catch(Exception e){
            DriverStation.reportWarning(e.getMessage(), e.getStackTrace());
            return idle();
        }
    }

    @Override
    public void periodic(){
        PoseEstimator.update(gyro.getRotation2d(), getPosition());
        log();
    }

    private void autoInit(){
        try{
            AutoBuilder.configure(
                this::getPose, 
                (pose) -> PoseEstimator.resetPose(pose),
                this::getSpeeds, 
                (speeds, ff) -> drive(speeds), 
                new PPHolonomicDriveController(
                    new PIDConstants(0,0,0), 
                    new PIDConstants(0,0,0)), 
                RobotConfig.fromGUISettings(), 
                () -> false, 
                this);
        }catch(Exception e){
            DriverStation.reportError(e.getLocalizedMessage(), e.getStackTrace());
        }
    }

    private void updateVisionPose(Pose2d pose){
        PoseEstimator.addVisionMeasurement(pose, Timer.getFPGATimestamp());
    }

    public Pose2d getPose(){
        return PoseEstimator.getEstimatedPosition();
    }

    private void log(){
        DogLog.log("Drivetrain/CurrentState", getState());
        DogLog.log("Drivetrain/CurrentSpeeds", Constants.kinematics.toChassisSpeeds(getState()));
        DogLog.log("Drivetrain/CurrentPose", PoseEstimator.getEstimatedPosition());

        vision.getPose().ifPresent(this::updateVisionPose);
    }
    
    public static Drivetrain getInstance(){
        inst = inst == null ? new Drivetrain() : inst;
        return inst;
    }
}
