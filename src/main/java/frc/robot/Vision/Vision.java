package frc.robot.Vision;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Vision {
    public PhotonCamera FrontTagCam, RearTagCam;

    public PhotonPoseEstimator FrontEstimator, RearEstimator;
    public DoubleSubscriber CoralOffset;

    private static Vision inst;

    private Vision(){
        FrontTagCam = new PhotonCamera(Constants.FrontTagCamName);
        RearTagCam = new PhotonCamera(Constants.RearTagCamName);

        FrontEstimator = new PhotonPoseEstimator(Constants.Field, Constants.FrontTagPlace);
        RearEstimator = new PhotonPoseEstimator(Constants.Field, Constants.RearTagPlace);
        CoralOffset = NetworkTableInstance.getDefault().getDoubleTopic("YOLO/CoralOffset").subscribe(114514);
    }

    public Optional<Pose2d> getPose(){
        List<PhotonPipelineResult> FrontResult, RearResult;
        FrontResult = FrontTagCam.getAllUnreadResults();
        RearResult = RearTagCam.getAllUnreadResults();
        if(FrontResult.isEmpty() && RearResult.isEmpty()) return Optional.empty();
        Pair<Optional<EstimatedRobotPose>, Optional<EstimatedRobotPose>> results = Pair.of( FrontEstimator.estimateLowestAmbiguityPose(FrontResult.get(FrontResult.size() - 1)), RearEstimator.estimateLowestAmbiguityPose(RearResult.get(RearResult.size() - 1)));
        if(results.getFirst().isEmpty() && results.getSecond().isEmpty()) return Optional.empty();
        else{
            if(!results.getFirst().isEmpty()) return Optional.of(results.getFirst().get().estimatedPose.toPose2d());
            else return Optional.of(results.getSecond().get().estimatedPose.toPose2d());
        }
    
    }

    public Optional<Double> getCoralError(){
        return CoralOffset.get(114514) == 114514 ? Optional.empty() : Optional.of(CoralOffset.get());
    }

    public static Vision getInstace(){
        inst = inst == null ? new Vision() : inst;
        return inst;
    }

}
