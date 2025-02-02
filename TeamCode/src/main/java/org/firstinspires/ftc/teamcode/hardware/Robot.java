package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemAutoCapstone;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemAutoIntakeGrabber;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemDriveTrainMecanum;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemFoundationGrabber;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemIntake;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemLift;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemLighting;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemOdometry;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemStoneGuide;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemVirtual4Bar;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

public class Robot extends Subsystem {
  ExpansionHubEx expansionHubRight;
  ExpansionHubEx expansionHubLeft;

  private RevBulkData bulkDataRight = null;
  private RevBulkData bulkDataLeft = null;

  private boolean bulkDataUpdatedThisCycleRight = false;
  private boolean bulkDataUpdatedThisCycleLeft = false;

  public HardwareMap hwMap;

  public final SubsystemDriveTrainMecanum subsystemDriveTrainMecanum;
  public final SubsystemIntake subsystemIntake;
  public final SubsystemFoundationGrabber subsystemFoundationGrabber;
  public final SubsystemVirtual4Bar subsystemVirtual4Bar;
  public final SubsystemLift subsystemLift;
  public final SubsystemAutoIntakeGrabber subsystemAutoIntakeGrabber;
  public final SubsystemStoneGuide subsystemStoneGuide;
  public final SubsystemAutoCapstone subsystemAutoCapstone;
  public final SubsystemLighting subsystemLighting;
  public final SubsystemOdometry subsystemOdometry;
//  public final SubsystemIMU subsystemIMU;

  private double lastTime = 0.0;

  public Robot(HardwareMap hwMap, RadicalOpMode opMode) {
    this.hwMap = hwMap;

    expansionHubRight = hwMap.get(ExpansionHubEx.class, "Expansion Hub 1");
    expansionHubLeft = hwMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    subsystemDriveTrainMecanum = new SubsystemDriveTrainMecanum(this, opMode);
    subsystemIntake = new SubsystemIntake(this, opMode);
    subsystemFoundationGrabber = new SubsystemFoundationGrabber(this, opMode);
    subsystemVirtual4Bar = new SubsystemVirtual4Bar(this, opMode);
    subsystemLift = new SubsystemLift(this, opMode);
    subsystemAutoIntakeGrabber = new SubsystemAutoIntakeGrabber(this, opMode);
    subsystemStoneGuide = new SubsystemStoneGuide(this, opMode);
    subsystemAutoCapstone = new SubsystemAutoCapstone(this, opMode);
    subsystemLighting = new SubsystemLighting(this, opMode);
    subsystemOdometry = new SubsystemOdometry(this, opMode);
//    subsystemIMU = new SubsystemIMU(this, opMode);

    // Keep the IMU off by default to avoid 'problem with imu' errors
//    subsystemIMU.turnOff();
    subsystemOdometry.turnOff(); // Not needed in teleop

    getSubsystemHandler().add(subsystemDriveTrainMecanum);
    getSubsystemHandler().add(subsystemIntake);
    getSubsystemHandler().add(subsystemFoundationGrabber);
    getSubsystemHandler().add(subsystemVirtual4Bar);
    getSubsystemHandler().add(subsystemLift);
    getSubsystemHandler().add(subsystemAutoIntakeGrabber);
    getSubsystemHandler().add(subsystemStoneGuide);
    getSubsystemHandler().add(subsystemAutoCapstone);
    getSubsystemHandler().add(subsystemLighting);
    getSubsystemHandler().add(subsystemOdometry);
//    getSubsystemHandler().add(subsystemIMU);
  }

  @Override
  public void update() {
//    if(lastTime == 0.0) {
//      lastTime = System.currentTimeMillis();
//    } else {
//      double now = System.currentTimeMillis();

//      Log.i("looptime", Double.toString(now - lastTime));
//
//      lastTime = now;
//    }

    bulkDataUpdatedThisCycleRight = false;
    bulkDataUpdatedThisCycleLeft = false;
  }

  public RevBulkData getBulkDataRight() {
    if(!bulkDataUpdatedThisCycleRight) {
      bulkDataRight = expansionHubRight.getBulkInputData();
      bulkDataUpdatedThisCycleRight = true;
    }

    return bulkDataRight;
  }

  public RevBulkData getBulkDataLeft() {
    if(!bulkDataUpdatedThisCycleLeft) {
      bulkDataLeft = expansionHubLeft.getBulkInputData();
      bulkDataUpdatedThisCycleLeft = true;
    }

    return bulkDataLeft;
  }
}
