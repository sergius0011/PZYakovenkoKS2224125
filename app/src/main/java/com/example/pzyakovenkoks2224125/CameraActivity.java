package com.example.pzyakovenkoks2224125;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private Button btnCapture, btnBack;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        textureView = findViewById(R.id.textureView);
        btnCapture = findViewById(R.id.btnCapture);
        btnBack = findViewById(R.id.btnBack);

        textureView.setSurfaceTextureListener(this);

        btnCapture.setOnClickListener(v -> capturePhoto());

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCamera();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void openCamera() {
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);

            camera.setPreviewTexture(textureView.getSurfaceTexture());
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка открытия камеры", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }
    }

    private void capturePhoto() {
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Toast.makeText(CameraActivity.this, "Фото сделано!", Toast.LENGTH_SHORT).show();

                    camera.startPreview();
                }
            });
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textureView.isAvailable()) {
            openCamera();
        }
    }
}