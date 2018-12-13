package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import static java.lang.Math.PI;

@Autonomous(name = "test_bench_autonomous")
//@Disabled

public class TestBenchAutonomous extends LinearOpMode {
    DcMotor rightDriveMotor;
    DcMotor leftDriveMotor;
    //DcMotor baseArmMotor;
    //DcMotor midArmMotor;
    //DcMotor frontScoopDogMotor;
    //DcMotor backScoopDogMotor;
    DcMotor cascadingLift;

    DigitalChannel liftHomeSensor;  // Hardware Device Object

    //static final float MOTOR_TO_WHEEL_RATIO = 3.0f;
    //static final float WHEEL_DIAMETER_INCHES = 6.0f;
    //static final float DRIVE_COUNTS_PER_INCH = (float) (COUNTS_PER_REV[HDHEX40_MOTOR] * WHEEL_DIAMETER_INCHES * PI / MOTOR_TO_WHEEL_RATIO);

    static final int HDHEX40COUNTS_PER_REV = 2240;   // HD Hex 40:1 encoder output is 2240 counts per shaft revolution
    static final int COREHEXCOUNTS_PER_REV = 288;    // Core Hex encoder output is 288 counts per shaft revolution

    static final int[] COUNTS_PER_REV = new int[] {COREHEXCOUNTS_PER_REV, HDHEX40COUNTS_PER_REV};
    static final int COREHEX_MOTOR = 0;
    static final int HDHEX40_MOTOR = 1;

    static final float LIFT_TRAVEL_DISTANCE_INCHES = 2.0f;    // change this to adjust travel distance for lift

    static final int COUNTS_PER_INCH = (int)(COUNTS_PER_REV[HDHEX40COUNTS_PER_REV] * 1.0f);
    static final int POSN_CASCADING_LIFT_EXENDED = (int)(COUNTS_PER_INCH * LIFT_TRAVEL_DISTANCE_INCHES);

    //    @Override
    public void runOpMode() {

        telemetry.addData("Status", "starting");
        telemetry.update();

        liftHomeSensor = hardwareMap.get(DigitalChannel.class, "sensor_lift");
        // set the digital channel to input.
        liftHomeSensor.setMode(DigitalChannel.Mode.INPUT);

        //baseArmMotor = hardwareMap.dcMotor.get("Base arm motor");
        //midArmMotor = hardwareMap.dcMotor.get("Mid arm motor");
        //frontScoopDogMotor = hardwareMap.dcMotor.get("Front scoop dog motor");
        //backScoopDogMotor = hardwareMap.dcMotor.get("Back scoop dog motor");    // port 3
        cascadingLift = hardwareMap.dcMotor.get("Cascading lift");              // port 2
        rightDriveMotor = hardwareMap.dcMotor.get("Right drive motor");         // port 0
        leftDriveMotor = hardwareMap.dcMotor.get("Left drive motor");           // port 1

        //baseArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //midArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //frontScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        //backScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        cascadingLift.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        boolean liftHomeKnown = false;

        telemetry.addData("Status", "waitForStart");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Starting...");
        telemetry.update();

        if (liftHomeSensor.getState() == true) {
            telemetry.addData("cascadingLift sensor: ", "Lift sensor not detected :-(   Searching...");
            telemetry.update();
            cascadingLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // Retract until the sensor is located
            cascadingLift.setPower(-0.2);
            // continue to run until we find the home sensor, then reset the encoder and proceed
            while (liftHomeSensor.getState() == true);
            cascadingLift.setPower(0);
            telemetry.addData("cascadingLift sensor: ", "Lift home position located!");
            telemetry.update();
        }
        // the lift should now be at the home sensor
        if (liftHomeSensor.getState() == false) {
            cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            cascadingLift.setTargetPosition(POSN_CASCADING_LIFT_EXENDED);
            cascadingLift.setPower(0.2);
            telemetry.addData("cascadingLift to: ", POSN_CASCADING_LIFT_EXENDED);
            telemetry.update();
            // wait until the lift position is reached and the motor stops
            while (cascadingLift.isBusy() && opModeIsActive());
        }

        // now drive away...
 /*       rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightDriveMotor.setTargetPosition(-100);
        leftDriveMotor.setTargetPosition(600);

        rightDriveMotor.setPower(0.1);
        leftDriveMotor.setPower(0.25);

        // wait until we reach the set positions
        while ((leftDriveMotor.isBusy() || rightDriveMotor.isBusy()) && opModeIsActive());
        telemetry.addData("Reached drive position: ", "%4d, %4d",rightDriveMotor.getCurrentPosition(), leftDriveMotor.getCurrentPosition());
        telemetry.update();

        rightDriveMotor.setPower(0.2);
        leftDriveMotor.setPower(0.2);
        rightDriveMotor.setTargetPosition(1000);
        leftDriveMotor.setTargetPosition(1000);

        while ((leftDriveMotor.isBusy() || rightDriveMotor.isBusy()) && opModeIsActive());
        telemetry.addData("Reached drive position: ", "%4d, %4d",rightDriveMotor.getCurrentPosition(), leftDriveMotor.getCurrentPosition());
        telemetry.update();
*/
    }
}
