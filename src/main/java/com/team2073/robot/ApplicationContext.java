package com.team2073.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.team2073.robot.subsystems.SimpleSubsystem;
import edu.wpi.first.wpilibj.Joystick;

public class ApplicationContext {
    private static ApplicationContext instance;
    private static CANSparkMax motor;
    private static Joystick controller;
    private static SimpleSubsystem simpleSubsystem;
    private static Joystick driveStick;
    private static OperatorInterface oi;

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public CANSparkMax getMotor() {
        if (motor == null) {
            motor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
        }
        return motor;
    }

    public Joystick getController() {
        if (controller == null) {
            controller = new Joystick(2);
        }
        return controller;
    }

    public SimpleSubsystem getSimpleSubsystem() {
        if (simpleSubsystem == null) {
            simpleSubsystem = new SimpleSubsystem();
        }
        return simpleSubsystem;
    }

    public Joystick getDriveStick() {
        if (driveStick == null) {
            driveStick = new Joystick(1);
        }
        return driveStick;
    }

    public OperatorInterface getOperatorInterface() {
        if (oi == null) {
            oi = new OperatorInterface();
        }
        return oi;
    }
}
