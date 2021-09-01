package com.app.flashlightapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton toggleButton;
    boolean hasCameraFlash = false;
    boolean flashOn = false;
    private Vibrator vibrator;
    private ConstraintLayout backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toggleButton -> Assigns variable to button
        // hasCameraFlash -> Checks if the device has flash
        // vibrator -> Assigns variable to vibration service
        // The backgroundImage has been set in the activity_main.xml.
        toggleButton = findViewById(R.id.toggleButton);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        backgroundImage = findViewById(R.id.ConstraintLayoutID);

        // When button is "clicked"
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enables vibration
                vibrator.vibrate(100);

                if (hasCameraFlash) {
                    // If Camera Flash is available, turn it ON or OFF
                    if (flashOn) {
                        flashOn = false;

                        backgroundImage.setBackgroundResource(R.drawable.dark); // Dark background
                        toggleButton.setImageResource(R.drawable.off); // Switch to "OFF" image
                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        flashOn = true;

                        backgroundImage.setBackgroundResource(R.drawable.light); // Light background
                        toggleButton.setImageResource(R.drawable.on); // Switch to "ON" image
                        try {
                            flashLightOn();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Displays a Toast if flash not available
                    Toast.makeText(MainActivity.this, "No flash available on your device.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Access the camera flashlight & turns it on
    // Note: Minimum API 23 is required
    private void flashLightOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, true);
        Toast.makeText(MainActivity.this, "Flashlight is ON", Toast.LENGTH_SHORT).show();
    }

    // Access the camera flashlight & turns it off
    private void flashLightOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, false);
        Toast.makeText(MainActivity.this, "Flashlight is OFF", Toast.LENGTH_SHORT).show();
    }
}
