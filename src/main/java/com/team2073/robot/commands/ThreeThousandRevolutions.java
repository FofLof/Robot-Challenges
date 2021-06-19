package com.team2073.robot.commands;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystems.SimpleSubsystem;

public class ThreeThousandRevolutions extends AbstractLoggingCommand{

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();

    @Override
    protected void initializeDelegate() {
        simpleSubsystem.setStartPosition();
    simpleSubsystem.setCurrentState(SimpleSubsystem.SimpleSubsystemState.THREE_THOUSAND_REVOLUTIONS);
    }

    @Override
    protected void endDelegate() {
        simpleSubsystem.setCurrentState(SimpleSubsystem.SimpleSubsystemState.STOP);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
