package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Autonomous(name = "test_bench_autonomous")
//@Disabled

public class TestBenchAutonomous extends LinearOpMode {
    //DcMotor rightDriveMotor;
    //DcMotor leftDriveMotor;
    //DcMotor baseArmMotor;
    //DcMotor midArmMotor;
    //DcMotor frontScoopDogMotor;
    //DcMotor backScoopDogMotor;
    DcMotor cascadingLift;

    DigitalChannel liftHomeSensor;  // Hardware Device Object


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
        //rightDriveMotor = hardwareMap.dcMotor.get("Right drive motor");         // port 0
        //leftDriveMotor = hardwareMap.dcMotor.get("Left drive motor");           // port 1

        //baseArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //midArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //frontScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        //backScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        cascadingLift.setDirection(DcMotor.Direction.FORWARD);
        //rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        //leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int hdHex40CountsPerRev = 2240;   // HD Hex 40:1 encoder output is 2240 counts per shaft revolution
        int coreHexCountsPerRev = 288;    // Core Hex encoder output is 288 counts per shaft revolution

        int[] countsPerRev = new int[] {coreHexCountsPerRev, hdHex40CountsPerRev};
        int coreHexMotor = 0;
        int hdHex40Motor = 1;

        float liftTravelDistanceInches = 2.0f;    // change this to adjust travel distance for lift

        // select the motor type here: creexMotor or hdHex40motor
        int countsPerInch = (int)(countsPerRev[coreHexMotor] * 1.0f);
        int posn_CascadingLiftExended = (int)(countsPerInch * liftTravelDistanceInches);

        boolean liftHomeKnown = false;

        telemetry.addData("Status", "waitForStart");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Starting...");
        telemetry.update();

        if (liftHomeSensor.getState() == true) {
            telemetry.addData("cascadingLift sensor: ", "lift sensor not detected :-(   Searching...");
            telemetry.update();
            cascadingLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // Retract until the sensor is located
            cascadingLift.setPower(-0.2);
            // continue to run until we find the home sensor, then reset the encoder and proceed
            while (liftHomeSensor.getState() == true);
            cascadingLift.setPower(0);
        }
        // the lift should now be at the home sensor
        if (liftHomeSensor.getState() == false) {
            cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            cascadingLift.setTargetPosition(posn_CascadingLiftExended);
            cascadingLift.setPower(0.2);
            telemetry.addData("cascadingLift to: ", posn_CascadingLiftExended);
            telemetry.update();
            // wait until the lift position is reached and the motor stops
            while (cascadingLift.isBusy() && opModeIsActive());
        }

        // now drive away...
    }
}
