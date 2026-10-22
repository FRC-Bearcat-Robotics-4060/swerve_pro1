// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * Based on the 2025 REEFSCAPE Robot.java, with the coral and climber calls removed.
 */
public class Robot extends TimedRobot
{

  private static Robot   instance;
  private        Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  private final Timer disabledTimer;

  public Robot()
  {
    instance = this;
    m_robotContainer = new RobotContainer();

    // Timer used to release the drive brakes a few seconds after disable, so the robot
    // stops quickly but can still be pushed by hand afterwards.
    disabledTimer = new Timer();

    if (isSimulation())
    {
      DriverStation.silenceJoystickConnectionWarning(true);
    }
  }

  public static Robot getInstance()
  {
    return instance;
  }

  @Override
  public void robotPeriodic()
  {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit()
  {
    // Lock motors temporarily when disabled to stop the robot quickly
    m_robotContainer.setMotorBrake(true);
    disabledTimer.reset();
    disabledTimer.start();
  }

  @Override
  public void disabledPeriodic()
  {
    // After a few seconds, release the brakes so the robot can be moved by hand
    if (disabledTimer.hasElapsed(Constants.DrivebaseConstants.WHEEL_LOCK_TIME))
    {
      m_robotContainer.setMotorBrake(false);
      disabledTimer.stop();
      disabledTimer.reset();
    }
  }

  @Override
  public void autonomousInit()
  {
    m_robotContainer.setMotorBrake(true);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null)
    {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic()
  {
  }

  @Override
  public void teleopInit()
  {
    // 2025 convention: the robot always starts the match with its front facing the driver.
    // If the team changes how the robot is placed, change or remove this line.
    m_robotContainer.resetGyro_inverted();

    if (m_autonomousCommand != null)
    {
      m_autonomousCommand.cancel();
    } else
    {
      CommandScheduler.getInstance().cancelAll();
    }
  }

  @Override
  public void teleopPeriodic()
  {
  }

  @Override
  public void testInit()
  {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic()
  {
  }

  @Override
  public void simulationInit()
  {
  }

  @Override
  public void simulationPeriodic()
  {
  }
}
