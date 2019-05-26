package pe.edu.cibertec.permissios;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnCamera;
    private ImageView imageViewPhoto;
    private Intent cameraIntent;
    private static final int REQUEST_CAMERA = 1, REQUEST_TAKE_PICTURE = 2;
    private File photoFile, storageDir, image;
    private String timeStapm, imageFileName, currentPathImage;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCamera);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Validar si tiene cámara
        if(cameraIntent.resolveActivity(getPackageManager())!= null){
            //Verificar si ya esta validado la cámara
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermission();
            }else{
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermission();
                }else{
                    photoFile = null;
                    try {
                        photoFile = createImage();
                    } catch (IOException e) {
                        Log.e("ERROR", "Mensaje: "+e.getMessage()+", Causa: "+e.getCause());
                        e.printStackTrace();
                    }
                    startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
                }
            }
        }else{
            Toast.makeText(MainActivity.this, "¡No hay cámara!", Toast.LENGTH_LONG).show();
        }
    }

    private File createImage() throws IOException {
        timeStapm = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_"+timeStapm+"_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPathImage = image.getAbsolutePath();
        return image;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA){
            if((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                takePicture();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageViewPhoto.setImageBitmap(bitmap);
        }
    }
}
