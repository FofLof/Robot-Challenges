package com.team2073.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;

public class SimpleSubsystem extends OperatorInterface implements AsyncPeriodicRunnable {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();
    private OperatorInterface oi = appCTX.getOperatorInterface();

    private final CANSparkMax motor = appCTX.getMotor();

    double AxisPos = getAxis(1);
    double LeftTriggerPressure = getAxis(2);
    double RightTriggerPressure = getAxis(3);
// Need Axis Num for Left Analog and Left/Right trigger
    private double output = AxisPos;

    private SimpleSubsystemState currentState = SimpleSubsystemState.STOP;

    public SimpleSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    public double getAxis(int axis) {
        return controller.getRawAxis(axis);
    }

    @Override
    public void onPeriodicAsync() {
        switch(currentState) {
            case STOP:
                output = 0;
                break;
            case HALF_POWER:
                output = 0.5;
                break;
            case LEFT_TRIGGER_DECREASE:
                for (double i = 0; output >= i; output = output - LeftTriggerPressure) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setMotor(output);
                }
            case RIGHT_TRIGGER_INCREASE:
                for (double i = 1; output <= i; output = output + RightTriggerPressure) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setMotor(output);
                }

            default:
                output = 0;
                break;
        }
        if (output > 0.8) {
            output = 0.8;
        } else if (output < 0.2) {
            output = 0;
        }
        motor.set(output);
    }

    public void setCurrentState(SimpleSubsystemState currentState){
        this.currentState = currentState;
    }

    public enum SimpleSubsystemState {
        STOP,
        HALF_POWER,
        LEFT_TRIGGER_DECREASE,
        RIGHT_TRIGGER_INCREASE

    }

    public void setMotor(double speed) {
        motor.set(speed);
    }

}