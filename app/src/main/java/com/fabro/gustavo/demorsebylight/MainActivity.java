/*
Copyright (C) 2015  Gustavo Fabro

        This program is free software; you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation; either version 2 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program; if not, write to the Free Software
        Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.fabro.gustavo.demorsebylight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private float timeDit = 0.884955752f;//1;
    private float timeDah = 0.376666667f;//3;
    private float timeEspace = 0.161428571f; //7;
    private float timeWord = 0.884955752f;//1;

    private int levelLightOn = 2999;
    private double lightLevel = 0, finalCounter = 0;
    private long timeOn = 0, timeOff = 0;
    private boolean isBeginOff = false, isBeginOn = true;

    Decoder decode = new Decoder();
    TextView textLight_reading, morseOut, textSensibilityChange, textSpeedReading, reading;

    SensorManager mySensorManager;
    Sensor LightSensor;
    Button btnBegin;
    SeekBar seekBarSensibility, seekBarSpeedReading;

    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        btnBegin = (Button) findViewById(R.id.BtnBegin);
        textLight_reading = (TextView) findViewById(R.id.Light_reading);
        textSensibilityChange = (TextView) findViewById(R.id.SensibilityChange);
        textSpeedReading = (TextView) findViewById((R.id.SpeedReading));
        morseOut = (TextView) findViewById(R.id.morseOutText);
        reading = (TextView) findViewById(R.id.reading_alert);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        reading.setTextColor(Color.RED);

        anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(200);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        if (LightSensor == null) {
            AlertDialog.Builder dialogOut = new AlertDialog.Builder(MainActivity.this);
            dialogOut.setTitle(getString(R.string.error));
            dialogOut.setMessage(getString(R.string.null_sensor));
            dialogOut.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg1, int arg2) {
                    finish();
                }
            });
            dialogOut.show();
        }

        mySensorManager.unregisterListener(LightSensorListener);

        textSensibilityChange.setText(getString(R.string.light_sensibility) + ": " + (levelLightOn + 1));

        seekBarSensibility = (SeekBar) findViewById(R.id.SeekBarSensibility);
        seekBarSpeedReading = (SeekBar) findViewById(R.id.SeekBarSpeedReading);

        setNewSpeedReading(2);

        seekBarSensibility.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setNewSensibilityChange(progress+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarSpeedReading.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setNewSpeedReading(progress+1);
                textSpeedReading.setText(getString(R.string.speed) + ": " + (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final AlertDialog.Builder dialogOut = new AlertDialog.Builder(MainActivity.this);
            dialogOut.setMessage(getString(R.string.develop_by));
            dialogOut.setPositiveButton("OK", null);
            dialogOut.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setNewSensibilityChange(int progress){
        this.levelLightOn = progress;
        textSensibilityChange.setText(getString(R.string.light_sensibility) + ": " + (levelLightOn));
    }

    public void setNewSpeedReading(int progress){
        switch(progress){
            case 1: setSpeed(2.0f);break;
            case 2: setSpeed(1.5f);break;
            case 3: setSpeed(1);break;
            case 4: setSpeed(0.5f);break;
            case 5: setSpeed(0.1f);break;
        }

        textSpeedReading.setText(getString(R.string.speed) + ": " + (progress+1) );
    }

    public void setSpeed(float level) {
        timeDit = level;
        timeDah = timeDit * 2;
        timeEspace = timeDit * 7;
        timeWord = timeDit;
    }

    public void registerListener(){
        mySensorManager.registerListener(LightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onPause(){
        mySensorManager.unregisterListener(LightSensorListener);
        super.onPause();
    }

    public void beginReading(View view){
        if(seekBarSensibility.isEnabled()) { //se estiver como "Iniciar"
            seekBarSensibility.setEnabled(false);
            seekBarSpeedReading.setEnabled(false);
            btnBegin.setText(R.string.stop);
            registerListener();
            reading.setText(R.string.reading);
            reading.startAnimation(anim);
        }else {
            seekBarSensibility.setEnabled(true);
            seekBarSpeedReading.setEnabled(true);
            btnBegin.setText(R.string.begin);
            decode.setLetter();
            morseOut.setText(decode.getConvertido());
            textLight_reading.setText("");
            mySensorManager.unregisterListener(LightSensorListener);
            decode.setConvertido("");
            reading.setText("");
            isBeginOff = false;
            isBeginOn = true;
            timeOff = 0;
            timeOn = 0;
        }
    }

    private final SensorEventListener LightSensorListener = new SensorEventListener(){ //called when sensor values have changed.
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                lightLevel = event.values[0];
                textLight_reading.setText(getString(R.string.level_light) + ": " + lightLevel);
                checksSign(lightLevel);

            }
        }
    };

    public void checksSign(double lightLevel){

        if((lightLevel >= levelLightOn) && isBeginOn){
            timeOn = System.currentTimeMillis();
            isBeginOn = false;
            isBeginOff = true;

            if(timeOff!=0) {
                finalCounter = ((System.currentTimeMillis() - timeOff)/1000);

                if (finalCounter > timeEspace) {//
                    decode.setSpace();
                } else {
                    if (finalCounter > timeWord) {
                        decode.setLetter();
                    }
                }
            }
        }else {
            if ((lightLevel < levelLightOn) && isBeginOff) {
                timeOff = System.currentTimeMillis();
                isBeginOff = false;
                isBeginOn = true;

                finalCounter = ((System.currentTimeMillis() - timeOn) / 1000);
                if (finalCounter < timeDit) { // (DIT)
                    decode.morseToAlfa(".");
                } else {
                    if (finalCounter < timeDah) { // (DAH)
                        decode.morseToAlfa("-");
                    }
                }
            }
        }
    }
}
