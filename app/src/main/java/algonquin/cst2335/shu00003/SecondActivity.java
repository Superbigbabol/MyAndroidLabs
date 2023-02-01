package algonquin.cst2335.shu00003;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    TextView welcomeText;
    Button callButton;
    Button picButton;
    EditText phone;
    ImageView profileImage;
    Intent call;
    Intent cameraIntent;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        welcomeText = findViewById(R.id.textView);
        welcomeText.setText("Welcome back "+emailAddress);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        phone = findViewById(R.id.editTextPhone);
        phone.setText(prefs.getString("PhoneNumber", ""));

        callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener( clk -> {
            call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = phone.getText().toString();
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            profileImage.setImageBitmap(thumbnail);
                        }
                    }
                }
        );
        picButton = findViewById(R.id.picButton);
        profileImage = findViewById(R.id.profileImage);
        picButton.setOnClickListener( clk -> {

            cameraResult.launch(cameraIntent);
        });

        //test if a file exists, then load the file
        String filename = "Picture.png";
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(filename);
            profileImage.setImageBitmap(theImage);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phone.getText().toString());
        editor.apply();
    }
}