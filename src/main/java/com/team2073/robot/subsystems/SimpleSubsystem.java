package com.team2073.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.OperatorInterface;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import static java.lang.Math.abs;

public class SimpleSubsystem extends OperatorInterface implements AsyncPeriodicRunnable {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();

    private final CANSparkMax motor = appCTX.getMotor();
    private double output = 0;
// Need Axis Num for Left Analog and Left/Right trigger
    private boolean isOn = false;
    private boolean isPressed = false;
    private boolean pulsed = false;
    public double cruiseOutput = 0;
    private boolean needRotate = false;
    public double startPosition = 0;
    double startPos = 0;
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
                System.out.println(output);
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
            //if (abs(output) < abs(cruiseControl)) { output = cruise; }
            case THREE_THOUSAND_REVOLUTIONS:
                System.out.println(output);
                needRotate = true;
                break;
            case STARTING_POSITION:
                System.out.println(output);
                //This has a lot of repeating code might need to fix later
                double newPos = motor.getEncoder().getPosition();
                double movementDetector = 0;
                if (newPos > startPos) {
                    while (newPos != startPos) {
                        movementDetector = getAxis(1);
                        if (movementDetector != 0) {
                            while (movementDetector != 0) {
                                movementDetector = getAxis(1);
                                motor.set(movementDetector);
                            }
                        }
                        motor.set(-0.4);
                        newPos = motor.getEncoder().getPosition();
                    }
                } else if (newPos < startPos) {
                    while (newPos != startPos) {
                        movementDetector = getAxis(1);
                        if (movementDetector != 0) {
                            while (movementDetector != 0) {
                                movementDetector = getAxis(1);
                                motor.set(movementDetector);
                            }
                        }
                        motor.set(0.4);
                        newPos = motor.getEncoder().getPosition();
                    }
                }
                break;
            case SET_RETURN_TO_POSITION:
                System.out.println(output);
                startPos = motor.getEncoder().getPosition();
                break;
        }
//        if (controller.getRawButtonPressed(1) == false) {
//            double AxisPos = getAxis(1);
//            output = -AxisPos;
//            if (LeftTriggerPressure != 0) {
//                lbIncrease();
//            } else if (RightTriggerPressure != 0) {
//                rbDecrease();
//            }
//        }
        triggerControl();

        if (output > 0.8) {
            output = 0.8;
        } else if (output < -0.8){
            output = -0.8; //Theres probably a way to use the math class for this
        } else if (abs(output) < 0.2) {
            output = 0;
        }
        double newPosition = motor.getEncoder().getPosition();
        System.out.println("Start Position: " + startPosition + "\t Current Position: " + newPosition);
        if (needRotate) {
            if (startPosition + 1000 > newPosition) {
                output = 0.5;
            } else {
                needRotate = false;
            }
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