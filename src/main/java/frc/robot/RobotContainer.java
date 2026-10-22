// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;

/**
 * Drive controls carried over from the 2025 REEFSCAPE robot (pathplanner branch). The coral and climber
 * mechanisms and the PathPlanner autos are left out; this is the swerve chassis only.
 */
public class RobotContainer
{

  final CommandXboxController driverXbox = new CommandXboxController(0);

  // Same config folder as 2025: src/main/deploy/swerve/neo
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve/neo"));

  private boolean slowDrive = false;

  /**
   * Field-relative drive controlled by angular velocity. Axis signs are the ones the 2025 robot used.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverXbox.getLeftY() * +1,
                                                                () -> driverXbox.getLeftX() * +1)
                                                            .withControllerRotationAxis(() -> driverXbox.getRightX() * -1)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(OperatorConstants.SCALE_CONSTANT)
                                                            .allianceRelativeControl(true);

  /**
   * Slow version (2025 "climb mode"), toggled with the Start button.
   */
  SwerveInputStream driveAngularVelocitySlow = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                    () -> driverXbox.getLeftY() * +1,
                                                                    () -> driverXbox.getLeftX() * +1)
                                                                .withControllerRotationAxis(() -> driverXbox.getRightX() * -0.5)
                                                                .deadband(OperatorConstants.DEADBAND)
                                                                .scaleTranslation(OperatorConstants.SCALE_CONSTANT_SLOW)
                                                                .allianceRelativeControl(true);

  public RobotContainer()
  {
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
  }

  private void setDefaultDriveCommand()
  {
    if (slowDrive)
    {
      drivebase.setDefaultCommand(drivebase.driveFieldOriented(driveAngularVelocitySlow));
    } else
    {
      drivebase.setDefaultCommand(drivebase.driveFieldOriented(driveAngularVelocity));
    }
    SmartDashboard.putBoolean("SlowDrive", slowDrive);
  }

  private void configureBindings()
  {
    setDefaultDriveCommand();

    // Start: toggle slow drive
    driverXbox.start().onTrue(Commands.runOnce(() -> {
      slowDrive = !slowDrive;
      setDefaultDriveCommand();
    }, drivebase));

    // Y: zero the gyro (robot front is now "away from the driver")
    driverXbox.y().onTrue(Commands.runOnce(drivebase::zeroGyro));

    // Test mode only, for the on-blocks checks:
    //   Back (hold) = point all four wheels straight ahead
    //   X (hold)    = lock wheels in an X
    // 2025 bound these inside "if (DriverStation.isTest())", which runs only once at startup
    // while the robot is disabled, so they never worked. Mode triggers fix that.
    RobotModeTriggers.test().and(driverXbox.back()).whileTrue(drivebase.centerModulesCommand());
    RobotModeTriggers.test().and(driverXbox.x())
                     .whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
  }

  /**
   * No autonomous yet. PathPlanner autos come back after manual driving works (plan step 9).
   */
  public Command getAutonomousCommand()
  {
    return Commands.none();
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }

  public void resetGyro_inverted()
  {
    drivebase.getSwerveDrive().setGyro(new Rotation3d(0, 0, Math.PI));
  }
}
