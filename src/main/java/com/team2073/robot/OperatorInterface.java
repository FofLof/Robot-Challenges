package com.team2073.robot;

import com.team2073.robot.commands.HalfPowerCommand;
import com.team2073.robot.commands.LeftTriggerDecrease;
import com.team2073.robot.commands.PulseMotorOutput;
import com.team2073.robot.commands.RightTriggerIncrease;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperatorInterface {
    private final ApplicationContext appCTX = ApplicationContext.getInstance();

    public final Joystick controller =  appCTX.getController();
    private final Joystick driveStick = appCTX.getDriveStick();

    private final JoystickButton a = new JoystickButton(controller, 1);

    private final JoystickButton leftJoystick = new JoystickButton(driveStick, 1);
    //Need to change the button numbers later
    private JoystickButton lb = new JoystickButton(controller, 5);
    private JoystickButton rb = new JoystickButton(controller, 6);
    private JoystickButton L1 = new JoystickButton(controller, 1);

    public void init() {
        a.whileHeld(new HalfPowerCommand());
        lb.whileHeld(new LeftTriggerDecrease());
        rb.whileHeld(new RightTriggerIncrease());
        L1.whileHeld(new PulseMotorOutput());
    }

}
