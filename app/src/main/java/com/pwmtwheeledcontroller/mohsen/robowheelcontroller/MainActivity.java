package com.pwmtwheeledcontroller.mohsen.robowheelcontroller;

import android.app.Activity;
import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class MainActivity extends IOIOActivity{

    private TextView SeekBarNumber;
    private SeekBar seeekBar;
    private Button xit, xStop,xReset;
    private int iSeekBar;
    private int motorLeft, motorRight;
    private PwmOutput pwm3, pwm4;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private double touchedX, touchedY, readFromTouchX,readFromTouchY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchedX = event.getX();
        touchedY = event.getY();

        if(event.getAction()==MotionEvent.ACTION_MOVE){

            if(touchedX>110 & touchedX <980 & touchedY>400 & touchedY<1260){
                readFromTouchX =  (touchedX - 540) /4.3;
                readFromTouchY =  (830 - touchedY) /4.3 ;
            }
            calcMotorSpeed();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    String str = "x is "+new DecimalFormat("##.##").format(readFromTouchX)+"\t\t ,Y is "+ new DecimalFormat("##.##").format(readFromTouchY);

                    String str = "Left Motor is "+new DecimalFormat("##.##").format(motorLeft)
                            +"\t\t ,Right Motor Speed is "+ new DecimalFormat("##.##").format(motorRight);
//                    SeekBarNumber.setText(String.valueOf("x is "+readFromTouchX++readFromTouchY));
                    SeekBarNumber.setText(str);
                }
            });
        }


        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBarNumber = (TextView)findViewById(R.id.textView);
        seeekBar = (SeekBar)findViewById(R.id.seekBar);
        seeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iSeekBar = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iSeekBar = seekBar.getProgress();
            }
        });
        xStop = (Button)findViewById(R.id.xStop);
        xStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Stop button Action here
            }
        });
        xReset = (Button)findViewById(R.id.xReset);
        xReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Reset button Action Here
            }
        });
        xit = (Button)findViewById(R.id.xButton);
        xit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }


    class Looper extends BaseIOIOLooper{
        @Override
        protected void setup() throws ConnectionLostException, InterruptedException {
            super.setup();
            pwm3 = ioio_.openPwmOutput(3, 50);
            pwm4 = ioio_.openPwmOutput(5, 50);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            super.loop();

            refreshTheScreen();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    @Override
    public IOIOLooper createIOIOLooper(String connectionType, Object extra) {
        return super.createIOIOLooper(connectionType, extra);

    }

    void calcMotorSpeed(){
        motorLeft = (int) (readFromTouchX+readFromTouchY)/2;
        motorRight =(int) (readFromTouchY-readFromTouchX)/2;

    }

    void updateMotor() throws ConnectionLostException {
        pwm3.setDutyCycle( ((float) motorRight)/100);
        pwm4.setDutyCycle( ((float) motorLeft )/100);
    }


    void refreshTheScreen(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SeekBarNumber.setText(String.valueOf(iSeekBar));
            }
        });
    }

}
