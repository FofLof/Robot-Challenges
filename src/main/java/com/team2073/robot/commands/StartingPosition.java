package com.team2073.robot.commands;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystems.SimpleSubsystem;

public class StartingPosition extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private SimpleSubsystem simpleSubsystem = appCtx.getSimpleSubsystem();

    @Override
    protected void initializeDelegate() {
    simpleSubsystem.setCurrentState(SimpleSubsystem.SimpleSubsystemState.STARTING_POSITION);
    }

    @Override
    protected void endDelegate() {
        simpleSubsystem.setCurrentState((SimpleSubsystem.SimpleSubsystemState.STOP));
    }

    @Override
    protected boolean isFinishedDelegate() {
        simpleSubsystem.rotateToBeginning = false;
        return false;
    }
}
