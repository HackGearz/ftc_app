package org.firstinspires.ftc.teamcode;

//import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "game piece manipulator")
//@Disabled

public class GamepieceManipulator extends LinearOpMode {
    DcMotor baseArmMotor;
    DcMotor midArmMotor;
    DcMotor frontScoopDogMotor;
    DcMotor backScoopDogMotor;
    //DcMotor rightDriveMotor;
    //DcMotor leftDriveMotor;
    DcMotor cascadingLift;

//    @Override
    public void runOpMode() {

        telemetry.addData("Status", "starting");
        telemetry.update();

        baseArmMotor = hardwareMap.dcMotor.get("Base arm motor");
        midArmMotor = hardwareMap.dcMotor.get("Mid arm motor");
        frontScoopDogMotor = hardwareMap.dcMotor.get("Front scoop dog motor");
        backScoopDogMotor = hardwareMap.dcMotor.get("Back scoop dog motor");
        //rightDriveMotor = hardwareMap.dcMotor.get("Right drive motor");
        //leftDriveMotor = hardwareMap.dcMotor.get("Left drive motor");
        //cascadingLift = hardwareMap.dcMotor.get("Cascading lift");

        baseArmMotor.setDirection(DcMotor.Direction.FORWARD);
        midArmMotor.setDirection(DcMotor.Direction.FORWARD);
        frontScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        backScoopDogMotor.setDirection(DcMotor.Direction.FORWARD);
        //rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        //leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        //cascadingLift.setDirection(DcMotor.Direction.FORWARD);

        midArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        midArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Status", "waitForStart");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("Status", "Initialized");
            telemetry.update();

            /*rightDriveMotor.setPower(gamepad1.right_stick_y*1);
            leftDriveMotor.setPower(gamepad1.left_stick_y*1);
            if (gamepad1.right_bumper == true){
                cascadingLift.setPower(0.5);
            }
            /*else{
                cascadingLift.setPower(0);
            }
            if (gamepad1.left_bumper == true){
                cascadingLift.setPower(-0.25);
            }
            else{
                cascadingLift.setPower(0);
            }*/
            if (gamepad2.y == true){
                baseArmMotor.setPower(0.05);
            }
            else{
                baseArmMotor.setPower(0);
            }
            if (gamepad2.a == true){
                baseArmMotor.setPower(-0.05);
            }
            else{
                baseArmMotor.setPower(0);
            }
            if (gamepad2.x == true){
                midArmMotor.setTargetPosition(200);
                midArmMotor.setPower(1);
                telemetry.addData("MotorPosition: ", "200");
                telemetry.update();

                //while(midArmMotor.isBusy()){}
            }
            //else{
            //    midArmMotor.setPower(0);
            //}
            if (gamepad2.b == true){
                midArmMotor.setTargetPosition(0);
                midArmMotor.setPower(1);
                telemetry.addData("MotorPosition: ", "0");
                telemetry.update();
                //while(midArmMotor.isBusy()){}
            }
            //else{
            //    midArmMotor.setPower(0);
            //}
            if (gamepad2.right_bumper == true){
                frontScoopDogMotor.setPower(1);
            }
            else{
                frontScoopDogMotor.setPower(0);
            }
            if (gamepad2.left_bumper == true){
                backScoopDogMotor.setPower(1);
            }
            else{
                backScoopDogMotor.setPower(0);
            }


        }
    }

}
