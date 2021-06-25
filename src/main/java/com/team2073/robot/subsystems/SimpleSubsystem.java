package com.team2073.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;

import static java.lang.Math.abs;

public class SimpleSubsystem extends OperatorInterface implements AsyncPeriodicRunnable {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();

    private final CANSparkMax motor = appCTX.getMotor();
    private double output = 0;
    private boolean isOn = false;
    private boolean isPressed = false;
    private boolean pulsed = false;
    public double cruiseOutput = 0;
    private boolean needRotate = false;
    public double startPosition = 0;
    public double beginningPosition = 0;
    double startPos = 0;
    public boolean rotateToBeginning = false;
    Timer timer = new Timer();

    private SimpleSubsystemState currentState = SimpleSubsystemState.STOP;
    private SimpleSubsystemState previousState = SimpleSubsystemState.STOP;
    public SimpleSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    public double getAxis(int axis) {
        return controller.getRawAxis(axis);
    }

    public boolean buttonPressed(int bP) {
        return controller.getRawButton(bP);
    }

    public double setCruiseOutput() {
        cruiseOutput = output;
        return cruiseOutput;
    }

    public double setBeginningPosition() {
        beginningPosition = motor.getEncoder().getPosition();
        return beginningPosition;
    }

    public void CheckButton() {
        if(buttonPressed(4)) {
            isOn = !isOn;
        }
    }

    private void triggerControl() {
        output *= 1 + (-controller.getRawAxis(2) + controller.getRawAxis(3));
    }

    public void setStartPosition() {
        startPosition = motor.getEncoder().getPosition();
    }

    private double getAxisOutput() {
        return -controller.getRawAxis(1);
    }

    @Override
    public void onPeriodicAsync() {
        output = getAxisOutput();
        System.out.println(output);
        switch(currentState) {
            case HALF_POWER:
                output = 0.5;
                break;
            case PULSEMODE:
                if (previousState != currentState) {
                    timer.start();
                }
                if (timer.hasWaited(1000)) {
                    pulsed = !pulsed;
                    timer.start();
                }
                if (pulsed) {
                    output = 0;
                } else {
                    output = 0.25;
                }
                break;
            case CRUISE_CONTROL:
                output = cruiseOutput;
                double checkJoystick = getAxis(1);
                if (abs(checkJoystick) > abs(output)) {
                    output = checkJoystick;
                }
                break;
            case THREE_THOUSAND_REVOLUTIONS:
                System.out.println(output);
                needRotate = true;
                break;
            case STARTING_POSITION:
                rotateToBeginning = true;
                break;
            case SET_RETURN_TO_POSITION:
                System.out.println(output);
                setBeginningPosition();
                break;
        }
        triggerControl();

        double newPosition = motor.getEncoder().getPosition();
        if (needRotate) {
            if (startPosition + 200 > newPosition) {
                output = 0.5;
            } else {
                needRotate = false;
            }
        }
        if (rotateToBeginning) {
            if (getAxis(1) != 0) {
                rotateToBeginning = false;
                output = getAxis(1);
            }
            if (!(beginningPosition - 10 < newPosition && newPosition < beginningPosition + 10)) {
                if (beginningPosition < newPosition) {
                    output = -0.2;
                } else if (beginningPosition > newPosition) {
                    output = 0.2;
                }
            } else {
                rotateToBeginning = false;
            }
        }
        if (output > 0.8) {
            output = 0.8;
        } else if (output < -0.8){
            output = -0.8; //Theres probably a way to use the math class for this
        } else if (abs(output) < 0.2) {
            output = 0;
        }
        motor.set(output);
        previousState = currentState;
    }

    public void setCurrentState(SimpleSubsystemState currentState){
        this.currentState = currentState;
    }

    public enum SimpleSubsystemState {
        STOP,
        HALF_POWER,
        PULSEMODE,
        CRUISE_CONTROL,
        THREE_THOUSAND_REVOLUTIONS,
        STARTING_POSITION,
        SET_RETURN_TO_POSITION
    }
}