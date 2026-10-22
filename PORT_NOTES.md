# 2025 swerve code, ported to WPILib 2026

This branch brings the 2025 REEFSCAPE swerve drive (repo `4060_REEFSCAPE_2025`, branch `pathplanner`) to WPILib 2026.

## Where each part came from

| File | Source | Changes |
| --- | --- | --- |
| `src/main/deploy/swerve/neo/` | 2025 `pathplanner` branch | Steering ramp rate 0.25 -> 0 (it made the wheels overshoot) |
| `subsystems/swervedrive/SwerveSubsystem.java` | YAGSL 2026.4.1 official example | Same as the 2025 file minus PathPlanner and PhotonVision |
| `RobotContainer.java` | 2025 `pathplanner` branch | Coral, climber and autos removed; test-mode buttons fixed |
| `Robot.java` | 2025 `pathplanner` branch | Coral and climber calls removed |
| `Constants.java` | 2025 `pathplanner` branch | Joystick scale lowered to 0.3 for first tests |
| `vendordeps/` | Same 2026 versions that already built on the team laptop | YAGSL pinned to `yagsl-2026.4.1.json` |

## Why YAGSL 2026.4.1 and not the newest

YAGSL 2026.8.05 and later changed both the Java API and the JSON format. The 2025 code only compiles on 2026.4.1 or earlier. Do not accept VS Code's offer to update YAGSL.

## Controls

| Button | Mode | Action |
| --- | --- | --- |
| Left stick | Teleop | Drive (field-relative) |
| Right stick X | Teleop | Rotate |
| Y | Teleop | Zero gyro |
| Start | Teleop | Toggle slow drive |
| Back (hold) | Test | Point all wheels straight ahead |
| X (hold) | Test | Lock wheels in an X |

`Robot.teleopInit()` sets the gyro to 180 degrees, as in 2025 (robot starts facing the driver).

## Before enabling

The config values are from 2025. Do the Step 0 checklist first: CAN IDs, gear ratios, offsets and inversions must match the robot as it is now. Keep the robot on blocks.
