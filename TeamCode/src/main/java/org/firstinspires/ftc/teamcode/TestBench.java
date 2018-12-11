package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "test_bench")
//@Disabled

public class TestBench extends LinearOpMode {
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

        //cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int hdHex40CountsPerRev = 2240;   // HD Hex 40:1 encoder output is 2240 counts per shaft revolution
        int coreHexCountsPerRev = 288;    // Core Hex encoder output is 288 counts per shaft revolution

        int[] countsPerRev = new int[] {coreHexCountsPerRev, hdHex40CountsPerRev};
        int coreHexMotor = 0;
        int hdHex40Motor = 1;

        float liftTravelDistanceInches = 2.0f;    // change this to adjust travel distance for lift

        // select the motor
        // int countsPerInch = (int)(hdHex40CountsPerRev * 1.0f);

        // select the motor type here: creexMotor or hdHex40motor
        int countsPerInch = (int)(countsPerRev[coreHexMotor] * 1.0f);
        int posn_CascadingLiftExended = (int)(countsPerInch * liftTravelDistanceInches);

        boolean liftHomeKnown = false;

        telemetry.addData("Status", "waitForStart");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Starting...");
        telemetry.update();

        while (opModeIsActive()){

            // drive motor controls: joysticks
            //rightDriveMotor.setPower(gamepad1.right_stick_y * 1);
            //leftDriveMotor.setPower(gamepad1.left_stick_y * 1);

            if (!liftHomeKnown) {
                if (liftHomeSensor.getState() == false) {
                    telemetry.addData("cascadingLift sensor: ", "lift sensor detected!");
                    telemetry.update();
                    cascadingLift.setPower(0);
                    cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftHomeKnown = true;
                } else {
                    telemetry.addData("cascadingLift sensor: ", "lift sensor not detected :-(");
                    telemetry.update();
                    cascadingLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    cascadingLift.setPower(-0.25);
                }
            }

            else {
                // cascading lift control:
                // when the lift home has been located and the right bumper pressed
                if (gamepad1.right_bumper == true) {
                    cascadingLift.setTargetPosition(posn_CascadingLiftExended);
                    cascadingLift.setPower(0.2);
                    telemetry.addData("cascadingLift to: ", posn_CascadingLiftExended);
                    telemetry.update();
                }
                // as long as the lift is not at the home position then it's OK to move that direction
                else if ((gamepad1.left_bumper == true) && (liftHomeSensor.getState() == true)) {
                    cascadingLift.setTargetPosition(0);
                    cascadingLift.setPower(-0.3);
                    telemetry.addData("cascadingLift to: ", "0");
                    telemetry.update();
                }

                if (gamepad1.y == true) {
                    cascadingLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    cascadingLift.setPower(-0.25);
                    while (gamepad1.y == true) ;
                }
                if (gamepad1.a == true) {
                    cascadingLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    cascadingLift.setPower(0.25);
                    while (gamepad1.a == true) ;
                }

                if (gamepad1.x == true) {
                    cascadingLift.setPower(0);
                    cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }


                // Scoop dog motor control:
                /*if (gamepad2.right_bumper == true){
                    backScoopDogMotor.setPower(1);
                }
                else {
                    backScoopDogMotor.setPower(0);
                }
                if (gamepad2.left_bumper == true){
                    backScoopDogMotor.setPower(-1);
                }
                else {
                    backScoopDogMotor.setPower(0);
            }
*/
            }
        }
    }
}
