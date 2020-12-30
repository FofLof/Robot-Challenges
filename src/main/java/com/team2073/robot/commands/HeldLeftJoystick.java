package com.team2073.robot.commands;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import com.team2073.robot.subsystems.SimpleSubsystem;

public class HeldLeftJoystick extends AbstractLoggingCommand{
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();
    private OperatorInterface oi = appCtx.getOperatorInterface();

    @Override
    protected void executeDelegate() {
        //idk what the axis number is so i put 1
        double speed = oi.getRawAxis(1);
        simpleSubsystem.setMotor(speed);
        if (speed > 0.8) {
            speed = 0.8;
        } else if (speed < 0.2){
            speed = 0;
        }
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
