package com.team2073.robot.commands;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import com.team2073.robot.subsystems.SimpleSubsystem;

public class RightTriggerIncrease extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();
    private OperatorInterface oi = appCtx.getOperatorInterface();

    @Override
    protected void initializeDelegate() {
        //Change axis numbers for later
        double speed = oi.getRawAxis(1);
        double pressure = oi.getRawAxis(2);
        for (double i = 1; speed <= i && speed >= 0; speed = speed + pressure) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simpleSubsystem.setMotor(speed);
        }

    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
