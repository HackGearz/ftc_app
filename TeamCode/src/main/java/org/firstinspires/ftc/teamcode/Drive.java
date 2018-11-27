package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "drive")
//@Disabled

public class Drive extends LinearOpMode {
    DcMotor rightDriveMotor;
    DcMotor leftDriveMotor;
    //DcMotor baseArmMotor;
    //DcMotor midArmMotor;
    //DcMotor frontScoopDogMotor;
    DcMotor backScoopDogMotor;
    DcMotor cascadingLift;

    //    @Override
    public void runOpMode() {

        telemetry.addData("Status", "starting");
        telemetry.update();

        //baseArmMotor = hardwareMap.dcMotor.get("Base arm motor");
        //midArmMotor = hardwareMap.dcMotor.get("Mid arm motor");
        //frontScoopDogMotor = hardwareMap.dcMotor.get("Front scoop dog motor");
        backScoopDogMotor = hardwareMap.dcMotor.get("Back scoop dog motor");
        cascadingLift = hardwareMap.dcMotor.get("Cascading lift");
        rightDriveMotor = hardwareMap.dcMotor.get("Right drive motor");
        leftDriveMotor = hardwareMap.dcMotor.get("Left drive motor");

        //baseArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //midArmMotor.setDirection(DcMotor.Direction.FORWARD);
        //frontScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        backScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        cascadingLift.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        cascadingLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cascadingLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Status", "waitForStart");
        telemetry.update();

        int hdHex40CountsPerRev = 2240;   // HD Hex 40:1 encoder output is 2240 counts per shaft revolution
        int coreHexCountsPerRev = 288;    // Core Hex encoder output is 288 counts per shaft revolution
        int posn_CascadingLiftExended = hdHex40CountsPerRev * 3;

        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("Status", "Initialized");
            telemetry.update();

            rightDriveMotor.setPower(gamepad1.right_stick_y*1);
            leftDriveMotor.setPower(gamepad1.left_stick_y*1);

            // cascading lift control:
            if (gamepad1.right_bumper == true){
                cascadingLift.setTargetPosition(posn_CascadingLiftExended);
                cascadingLift.setPower(1);
                telemetry.addData("cascadingLift to: ", posn_CascadingLiftExended);
                telemetry.update();
                while (cascadingLift.isBusy()) {}
            }
            else{
                // motor hold if extended
                cascadingLift.setPower(1);
            }
            if (gamepad1.left_bumper == true){
                cascadingLift.setTargetPosition(0);
                cascadingLift.setPower(-1);
                telemetry.addData("cascadingLift to: ", "0");
                telemetry.update();
                while (cascadingLift.isBusy()) {}
            }
            else{
                cascadingLift.setPower(0);
            }

            // Scoop dog motor cotrol:
            if (gamepad2.right_bumper == true){
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

        }
    }

}
