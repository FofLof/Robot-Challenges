package com.team2073.robot.subsystems;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import com.team2073.robot.PID;

import static java.lang.Math.abs;

public class SimpleSubsystem extends OperatorInterface implements AsyncPeriodicRunnable {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();

    private final CANSparkMax motor = appCTX.getMotor();
    private double output = 0;
    private boolean pulsed = false;
    public double cruiseOutput = 0;
    public boolean needRotate = false;
    public double startPosition = 0;
    public double beginningPosition = 0;
    public boolean rotateToBeginning = false;
    Timer timer = new Timer();
    PID pid = new PID(0.5, 0.001, 0.00008);


    private SimpleSubsystemState currentState = SimpleSubsystemState.STOP;
    private SimpleSubsystemState previousState = SimpleSubsystemState.STOP;
    public SimpleSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    public double getAxis(int axis) {
        return controller.getRawAxis(axis);
    }

    public double setCruiseOutput() {
        cruiseOutput = output;
        return cruiseOutput;
    }

    public double setBeginningPosition() {
        beginningPosition = motor.getEncoder().getPosition();
        return beginningPosition;
    }

    public void returnToStart() {
        pid.set(beginningPosition);
        pid.calculateOutput(motor.getEncoder().getPosition());
        output = pid.getOutput();
        if (motor.getEncoder().getPosition() < beginningPosition + 10 && motor.getEncoder().getPosition() > beginningPosition - 10) {
            rotateToBeginning = false;
        }
    }

    public void threeKRevCode() {
        pid.set(100);
        pid.calculateOutput(motor.getEncoder().getPosition());
        output = pid.getOutput();
        if (motor.getEncoder().getPosition() > 100) {
            needRotate = false;
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

    private void pulseMode() {
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
    }

    public void motorPosToZero() {
        motor.getEncoder().setPosition(0);
    }

    private void cruiseControl() {
        output = cruiseOutput;
        double checkJoystick = getAxis(1);
        if (abs(checkJoystick) > abs(output)) {
            output = checkJoystick;
        }
    }
    private void threeKRev() {
        needRotate = true;
    }
    private void startPosition() {
        rotateToBeginning = true;
    }

    private void setReturnToPosition() {
        setBeginningPosition();
    }

    private void halfPower() {
        output = 0.5;
    }

    @Override
    public void onPeriodicAsync() {
        output = getAxisOutput();
        System.out.println(output);
        switch (currentState) {
            case HALF_POWER:
                halfPower();
                break;
            case PULSEMODE:
                pulseMode();
                break;
            case CRUISE_CONTROL:
               cruiseControl();
                break;
            case THREE_THOUSAND_REVOLUTIONS:
                threeKRev();
                break;
            case STARTING_POSITION:
                startPosition();
                break;
            case SET_RETURN_TO_POSITION:
                setReturnToPosition();
                break;
        }
        triggerControl();

        //double newPosition = motor.getEncoder().getPosition();
        System.out.println("Position: " + motor.getEncoder().getPosition());
        if (needRotate) {
            threeKRevCode();
        }

        if (rotateToBeginning && Math.abs(getAxisOutput()) < 0.05) {
            returnToStart();
        } else {
            rotateToBeginning = false;
        }

//        if (rotateToBeginning && Math.abs(output) < 0.05) {
//            if (beginningPosition + 10 < newPosition ) {
//                output = -0.2;
//            } else if (beginningPosition - 10 > newPosition) {
//                output = 0.2;
//            } else {
//                rotateToBeginning = false;
//            }
//        } else {
//                rotateToBeginning = false;
//        }
            if (output > 0.8) {
                output = 0.8;
            } else if (output < -0.8) {
                output = -0.8;
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