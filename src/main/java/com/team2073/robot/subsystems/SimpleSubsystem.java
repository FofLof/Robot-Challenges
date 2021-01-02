package com.team2073.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import edu.wpi.first.wpilibj.Encoder;

public class SimpleSubsystem extends OperatorInterface implements AsyncPeriodicRunnable {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();

    private final CANSparkMax motor = appCTX.getMotor();
    //Axis is pos is for the Left analog
    double AxisPos = getAxis(1);
    double LeftTriggerPressure = getAxis(2);
    double RightTriggerPressure = getAxis(3);
// Need Axis Num for Left Analog and Left/Right trigger
    private double output = AxisPos;
    private boolean isOn = false;
    private boolean isPressed = false;
    Encoder encode = new Encoder(0, 1);



    private SimpleSubsystemState currentState = SimpleSubsystemState.STOP;

    public SimpleSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    public double getAxis(int axis) {
        return controller.getRawAxis(axis);
    }

    public boolean buttonPressed(int bP) {
        return controller.getRawButtonPressed(bP);
    }

    public void CheckButton() {
        if(buttonPressed(4)) {
            isPressed = !isPressed;
            if (isPressed = true) {
                isOn = true;
            } else {
                isOn = false;
            }
        }
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
                }
                break;
            case RIGHT_TRIGGER_INCREASE:
                for (double i = 1; output <= i; output = output + RightTriggerPressure) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PULSEMODE:
                output = 0.25;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                output = 0;
                break;
            case CRUSIE_CONTROL:
                CheckButton();
                if (isOn) {
                    motor.set(output);
                } else {
                    break;
                }
            case THREE_THOUSAND_REVOLUTIONS:
                encode.setDistancePerPulse(1/3000);
                if (encode.getDistance() <= 1) {
                    motor.set(0.5);
                } else {
                    break;
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
        CheckButton(); //This might break it but im worried that when you press y again it will stop Cruise control before it has a chance to change
        // the isOn and isPressed value to false which resets the code so it can go again when y is pressed again
        motor.set(output);
    }

    public void setCurrentState(SimpleSubsystemState currentState){
        this.currentState = currentState;
    }

    public enum SimpleSubsystemState {
        STOP,
        HALF_POWER,
        LEFT_TRIGGER_DECREASE,
        RIGHT_TRIGGER_INCREASE,
        PULSEMODE,
        CRUSIE_CONTROL,
        THREE_THOUSAND_REVOLUTIONS

    }
}