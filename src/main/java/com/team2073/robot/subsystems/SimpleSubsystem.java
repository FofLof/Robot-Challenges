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
    Encoder encode = new Encoder(0, 1);
    double startPos = encode.getDistance();
    double LeftTriggerPressure = 0;
    double RightTriggerPressure = 0;
    Timer timer = new Timer();

    private SimpleSubsystemState currentState = SimpleSubsystemState.STOP;

    public SimpleSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    public double getAxis(int axis) {
        return controller.getRawAxis(axis);
    }

    public boolean buttonPressed(int bP) {
        return controller.getRawButton(bP);
    }

    public void CheckButton() {
        if(buttonPressed(4)) {
            if (!isPressed) {
                isOn = true;
                isPressed = true;
            } else {
                isPressed = false;
                isOn = false;
            }
        }
    }

    public void lbIncrease(){
        output = output - LeftTriggerPressure;
    }

    public void rbDecrease() {
        output = output + RightTriggerPressure;
    }

    @Override
    public void onPeriodicAsync() {
        encode.setDistancePerPulse(1/1260);
        double LeftTriggerPressure = getAxis(2);
        double RightTriggerPressure = getAxis(3);
        System.out.println(output);
        switch(currentState) {
            case STOP:
                output = 0;
                break;
            case HALF_POWER:
                output = 0.5;
                break;
            //case LEFT_TRIGGER_DECREASE:
                //double LeftTriggerPressure = getAxis(2);
                //output = output - LeftTriggerPressure;
                //try {
                    //Thread.sleep(10);
                //} catch (InterruptedException e) {
                    //e.printStackTrace();
                //}
                //break;
            //case RIGHT_TRIGGER_INCREASE:
                //double RightTriggerPressure = getAxis(3);
                //output = output + RightTriggerPressure;
                //try {
                    //Thread.sleep(10);
                //} catch (InterruptedException e) {
                    //e.printStackTrace();
                //}
                //break;
            case PULSEMODE:
                System.out.println(output);
                motor.set(0.25);
                timer.start();
                if (timer.hasWaited(1000)) {
                    motor.set(0);
                }
                if (timer.hasWaited(1000)) {
                    motor.set(0);
                }
                break;
            case CRUISE_CONTROL:
                System.out.println(output);
                CheckButton();
                if (isOn) {
                    motor.set(output);
                } else {
                    break;
                }
                double checkJoystick = getAxis(1);
                if (checkJoystick > output) {
                    motor.set(checkJoystick);
                } else if (checkJoystick < output) {
                    motor.set(output);
                }
            case THREE_THOUSAND_REVOLUTIONS:
                System.out.println(output);
                timer.start();
                motor.set(0.5); //I used the free speed (I think that means max speed) of 11000 RPM/60/2 (divide by 2 cause i put at half
                //speed) to get 91.6 RPS then did 3000/91.6 = 32.75 seconds for it to run and get to 3000 revolutions which i converted
                //to milliseconds
                if(timer.hasWaited(32750)){
                    motor.set(0);
                }
//                try {
//                    wait(32750);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                break;
            case STARTING_POSITION:
                System.out.println(output);
                //This has a lot of repeating code might need to fix later
                double newPos = encode.getDistance();
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
                        newPos = encode.getDistance();
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
                        newPos = encode.getDistance();
                    }
                }
                break;
            case SET_RETURN_TO_POSITION:
                System.out.println(output);
                startPos = encode.getDistance();
            default:
                output = 0;
                break;
        }
        if (controller.getRawButtonPressed(1) == false) {
            double AxisPos = getAxis(1);
            output = -AxisPos;
            if (LeftTriggerPressure != 0) {
                lbIncrease();
            } else if (RightTriggerPressure != 0) {
                rbDecrease();
            }
        }
        if (output > 0.8) {
            output = 0.8;
        } else if (output < -0.8){
            output = -0.8; //Theres probably a way to use the math class for this
        } else if (abs(output) < 0.2) {
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
        PULSEMODE,
        CRUISE_CONTROL,
        THREE_THOUSAND_REVOLUTIONS,
        STARTING_POSITION,
        SET_RETURN_TO_POSITION
    }
}