package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystems.SimpleSubsystem;
import edu.wpi.first.wpilibj.Joystick;

public class RobotDelegate extends AbstractRobotDelegate {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();

    public RobotDelegate(double period) {
        super(period);
    }

    @Override
    public void robotInit() {
        simpleSubsystem.setBeginningPosition();
        OperatorInterface oi = new OperatorInterface();
        oi.init();
    }

    @Override
    public void robotPeriodic() {

    }
}
