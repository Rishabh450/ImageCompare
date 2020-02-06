package com.rishabh.imagecompare;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView img1,img2;
    int flag1=0,flag2=0;
    Button upload1,upload2,compare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        upload1=findViewById(R.id.upload1);
        upload2=findViewById(R.id.upload2);
        compare=findViewById(R.id.compare);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Log.e(TAG, "setxml: peremission prob");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);


        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Log.e(TAG, "setxml: peremission prob");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);


        }
        upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag1=1;
                flag2=0;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(MainActivity.this);
            }
        });
        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag1=0;
                flag2=1;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(MainActivity.this);
            }
        });


    compare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final File sdCard = Environment.getExternalStorageDirectory();
            // File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/ComparedPhoto/"+fileName);
            File file = new File(sdCard.getAbsolutePath() + "/HashContact/ComparedPhoto/imgone.jpg");
            File file1 = new File(sdCard.getAbsolutePath() + "/HashContact/ComparedPhoto/imgtwo.jpg");
            if(file.exists()&&file1.exists()) {
                ImageCompare example = new ImageCompare(MainActivity.this, file, file1, 2);
                example.execute();
            }
            else
                Toast.makeText(MainActivity.this,"Add image first",Toast.LENGTH_SHORT).show();


        }
    });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Uri imageUri = data.getData();
                FileOutputStream outStream = null;
               // FileOutputStream outStream = null;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                   // long t = System.currentTimeMillis();
                    String time="imgone";
                    if(flag1==1){
                     time = "imgone";
                    img1.setImageBitmap(bitmap);}
                    else {
                        img2.setImageBitmap(bitmap);
                        time = "imgtwo";
                    }

                    final File sdCard = Environment.getExternalStorageDirectory();
                    //File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/ComparedPhoto/"+time+".jpg");

                    //Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.chatback);
                    Log.d("pathssss", "onPictureTaken - wrote to ");


                    File dir = new File(sdCard.getAbsolutePath() + "/HashContact" + "/ComparedPhoto");
                    dir.mkdirs();

                    String fileName = time+".jpg";
                    fileName.trim();
                    //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    Log.d("pathsssss", "onPictureTaken - wrote to " + fileName);

                    File outFile = new File(dir, fileName);
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
                    outStream.flush();
                    outStream.close();
                    Log.d("pathsssss", "onPictureTaken - wrote to " + fileName + dir);
                   // File file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/ComparedPhoto/"+fileName);


                } catch (IOException e) {
                    Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("pathserr", "onPictureTaken - wrote to " + e);
                }





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
