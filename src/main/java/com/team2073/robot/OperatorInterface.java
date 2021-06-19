package com.team2073.robot;

import com.team2073.robot.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperatorInterface {

    private final ApplicationContext appCTX = ApplicationContext.getInstance();
    public final Joystick controller =  appCTX.getController();
    private final Joystick driveStick = appCTX.getDriveStick();

    private final JoystickButton a = new JoystickButton(controller, 1);

    //Need to change the button numbers later
    private JoystickButton L1 = new JoystickButton(controller, 5);
    private JoystickButton y = new JoystickButton(controller, 4);
    private JoystickButton b = new JoystickButton(controller, 2);
    private JoystickButton x = new JoystickButton(controller, 3);

    public void init() {
        a.whileHeld(new HalfPowerCommand());
        L1.whileHeld(new PulseMotorOutput());
        y.toggleWhenPressed(new CruiseControl());
        b.whenPressed(new ThreeThousandRevolutions());
        x.whenPressed(new StartingPosition());
    }

}
