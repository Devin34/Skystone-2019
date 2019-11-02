package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

public class MecanumDrive {

  public ExpansionHubMotor motorFrontLeft;
  public ExpansionHubMotor motorFrontRight;
  public ExpansionHubMotor  motorBackLeft;
  public ExpansionHubMotor motorBackRight;

  private List<ExpansionHubMotor> motorList = new ArrayList<>();

  public ExpansionHubEx expansionHub;

  private RevBulkData bulkData;

  private int lastMotorVelFrontLeft = 0;
  private int lastMotorVelFrontRight = 0;
  private int lastMotorVelBackLeft = 0;
  private int lastMotorVelBackRight = 0;

  public int motorVelFrontLeft = 0;
  public int motorVelFrontRight = 0;
  public int motorVelBackLeft = 0;
  public int motorVelBackRight = 0;

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  public MecanumDrive(HardwareMap hwMap, ExpansionHubEx expansionHub, String motorFrontLeftId, String motorFrontRightId,
      String motorBackLeftId, String motorBackRightId, boolean runWithEncoders) {
    motorFrontLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontLeftId);
    motorFrontRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontRightId);
    motorBackLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackLeftId);
    motorBackRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackRightId);

    this.expansionHub = expansionHub;

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    if (runWithEncoders) {
      for (ExpansionHubMotor motor : motorList) {
        motor.setMode(RunMode.RUN_USING_ENCODER);
      }
    } else {
      for (ExpansionHubMotor motor : motorList) {
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      }
    }

    for(ExpansionHubMotor motor: motorList) {
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void stopMotors() {
    for (ExpansionHubMotor motor : motorList) {
      motor.setPower(0);
    }
  }

  public void resetEncoders() {
    for(ExpansionHubMotor motor : motorList) {
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    for(ExpansionHubMotor motor : motorList) {
      motor.setMode(RunMode.RUN_USING_ENCODER);
    }
  }

  public void update() {
    bulkData = expansionHub.getBulkInputData();

    motorVelFrontLeft = bulkData.getMotorCurrentPosition(motorFrontLeft) - lastMotorVelFrontLeft;
    motorVelFrontRight = bulkData.getMotorCurrentPosition(motorFrontRight) - lastMotorVelFrontRight;
    motorVelBackLeft = bulkData.getMotorCurrentPosition(motorBackLeft) - lastMotorVelBackLeft;
    motorVelBackRight = bulkData.getMotorCurrentPosition(motorBackRight) - lastMotorVelBackRight;

    lastMotorVelFrontLeft = bulkData.getMotorCurrentPosition(motorFrontLeft);
    lastMotorVelFrontRight = bulkData.getMotorCurrentPosition(motorFrontRight);
    lastMotorVelBackLeft = bulkData.getMotorCurrentPosition(motorBackLeft);
    lastMotorVelBackRight = bulkData.getMotorCurrentPosition(motorBackRight);

    if (dirty) {
      refreshMotors();
    }
  }

  public void setAngle(double angle) {
    this.angle = angle;
    this.dirty = true;
  }

  public void setPower(double power) {
    this.power = power;
    refreshMotors();
    this.dirty = true;
  }

  public void setTurn(double turn) {
    this.turn = turn;
    this.dirty = true;
  }

  public void refreshMotors() {
    double motorPowerFrontLeft = Math.cos(angle);
    double motorPowerFrontRight = Math.sin(angle);
    double motorPowerBackLeft = Math.sin(angle);
    double motorPowerBackRight = Math.cos(angle);

    // Normalize values
    double maxValue = Math.abs(Math.max(motorPowerFrontLeft,
        Math.max(motorPowerFrontRight, Math.max(motorPowerBackLeft, motorPowerBackRight))));

    motorPowerFrontLeft /= maxValue;
    motorPowerFrontRight /= maxValue;
    motorPowerBackLeft /= maxValue;
    motorPowerBackRight /= maxValue;

    motorPowerFrontLeft = motorPowerFrontLeft * power + turn;
    motorPowerFrontRight = motorPowerFrontRight * power - turn;
    motorPowerBackLeft = motorPowerBackLeft * power + turn;
    motorPowerBackRight = motorPowerBackRight * power - turn;

    motorFrontLeft.setPower(motorPowerFrontLeft);
    motorFrontRight.setPower(motorPowerFrontRight);
    motorBackLeft.setPower(motorPowerBackLeft);
    motorBackRight.setPower(motorPowerBackRight);

    this.dirty = false;
  }

  public double getAngle() {
    return angle;
  }

  public double getPower() {
    return power;
  }

  public double getTurn() {
    return turn;
  }
}
