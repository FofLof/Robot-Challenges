package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.team2073.common.robot.AbstractRobotDelegate;
import edu.wpi.first.wpilibj.Joystick;

public class RobotDelegate extends AbstractRobotDelegate {

    public RobotDelegate(double period) {
        super(period);
    }

    @Override
    public void robotInit() {
        OperatorInterface oi = new OperatorInterface();
        oi.init();
    }

    @Override
    public void robotPeriodic() {

    }
}
