package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Single Joycon", group="Linear Opmode")
public class TeleOp_SingleJoycon extends OpMode
{
    // Declare OpMode members.
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor lift = null;
    private Servo lFinger = null;
    private Servo rFinger = null;
    //private PIDController liftController;

    double maxPosition = 160;
    double minPosition = 0;
    //constants for pid control
    //increasing kP will increase overshoot, increase rise time, and increase instability
    //increasing kI will reduce steady state error(how off from the target that you settle at), increase rise time & increase overshoot a little
    //increasing kD will reduce overshoot, slightly slow down rise time, and if it gets too high will cause the mechanism to jitter. If you have problems with overshoot, bring this up.
    double kP = 0.025;
    double kI = 0;
    double kD = 0.25;

    //constants for arm motion
    double finalSetPoint = 50;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    //@Override
    public void init() {
        telemetry.addData("Status", "Init - Start");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "FL");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FR");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BR");
        backRightMotor = hardwareMap.get(DcMotor.class, "BL");
        //lift = hardwareMap.get(DcMotor.class, "lift");
        rFinger = hardwareMap.get(Servo.class, "rFinger");
        lFinger = hardwareMap.get(Servo.class, "lFinger");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Init - Complete");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }



    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //Read the GamePad joystick values, use left stick y for Drive, left stick x for strafe and right stick x for rotate
        //want the negative of the Y stick value as pushing up is a negative value and we want it to be positive up and negative going down

        float drive = gamepad1.left_stick_y;
        float strafe = gamepad1.left_stick_x;
        float rotate = gamepad1.right_stick_x;
        float lTrigger = gamepad1.left_trigger;
        float rTrigger = gamepad1.right_trigger;

        //Since motor power setting need to be between -1 and 1, if the sum of the absolute values of the 3 inputs is great than one, will need to divide by that sum to keep the values between -1 and 1, if the sum is less than 1, then we divide by 1

        float normalize = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(rotate), 1);
        float frontLeftPower = -(drive + strafe + rotate) / normalize;
        float backLeftPower = -(drive - strafe + rotate) / normalize;
        float frontRightPower = -(-drive + strafe + rotate) / normalize;
        float backRightPower = -(-drive - strafe + rotate) / normalize;

        //then just set each of the motor powers to the corresponding motor power variable, you will need to use the name for each of the motors that you have in your configuration file.

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
        // Servos are ALMOST opposite (meet in middle at R0.5, L1)
        if (lTrigger>0.1) { //Lefty-loosey
            rFinger.setPosition(1);
            lFinger.setPosition(0.5);
        } else if (rTrigger>0.1) { //Righty-tighty
            rFinger.setPosition(0.6);
            lFinger.setPosition(1);
        }

        //loop to adjust set point based on current position
		/*
        if(gamepad2.right_stick_y > 0 && lift.getCurrentPosition()<maxPosition){
            finalSetPoint += gamepad2.right_stick_y;
        } else if(gamepad2.right_stick_y < 0 && lift.getCurrentPosition()>minPosition){
            finalSetPoint -= gamepad2.right_stick_y;
        }
		*/

        //code to set in case it goes over
        /*
		if(lift.getCurrentPosition()<minPosition){
            finalSetPoint = minPosition;
        } else if(lift.getCurrentPosition()>maxPosition){
            finalSetPoint = maxPosition;
        }
         setArmPID(finalSetPoint);
		 */

        telemetry.addData("FR", frontRightPower);
        telemetry.addData("FL", frontLeftPower);
        telemetry.addData("BR", backRightPower);
        telemetry.addData("BL", backLeftPower);
        telemetry.addData("NORMALIZE", normalize);
        //telemetry.addData("LiftPosition", lift.getCurrentPosition());
        //telemetry.addData("setPosition", finalSetPoint);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
    private void setArmPID(double setpoint) {
        //double output = liftController.calculate(lift.getCurrentPosition(), setpoint);
        //lift.setPower(output);
    }

}
