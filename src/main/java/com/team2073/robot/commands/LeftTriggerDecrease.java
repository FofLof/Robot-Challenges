package com.team2073.robot.commands;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import com.team2073.robot.subsystems.SimpleSubsystem;

public class LeftTriggerDecrease extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();

    @Override
    protected void initializeDelegate() {
        simpleSubsystem.setCurrentState(SimpleSubsystem.SimpleSubsystemState.LEFT_TRIGGER_DECREASE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
