// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * Robot-wide constants. Values carried over from the 2025 REEFSCAPE robot (pathplanner branch).
 */
public final class Constants
{

  public static final double ROBOT_MASS = 84 * 0.453592; // 84 lb in kg (2025 value; re-weigh in Step 0)
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; // s, 20 ms + 110 ms SPARK MAX velocity lag
  public static final double MAX_SPEED  = Units.feetToMeters(14.5); // m/s, NEO + SDS L2

  public static final class DrivebaseConstants
  {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants
  {

    // Joystick deadband
    public static final double DEADBAND = 0.1;

    // Joystick scaling. Start low for the first on-blocks and floor tests, raise to 1.0 later.
    public static final double SCALE_CONSTANT      = 0.3;
    public static final double SCALE_CONSTANT_SLOW = 0.1;
  }
}
