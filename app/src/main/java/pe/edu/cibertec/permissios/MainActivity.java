package pe.edu.cibertec.permissios;

import android.Manifest;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnCamera;
    private ImageView imageViewPhoto;
    private Intent cameraIntent;

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
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)){

            }
        }else{
            Toast.makeText(MainActivity.this, "¡No hay cámara!", Toast.LENGTH_LONG).show();
        }
    }
}
