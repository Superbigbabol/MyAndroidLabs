package algonquin.cst2335.shu00003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Intent nextPage;
    EditText email;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        SharedPreferences.Editor editor = prefs.edit();

        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.editTextEmail);

        email.setText(emailAddress);

        loginButton.setOnClickListener( clk -> {
            editor.putString("LoginName", email.getText().toString());
            editor.apply();
            nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email.getText().toString());
            startActivity(nextPage);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "In onStart - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In onResume - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause - No longer respond to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onStop - No longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onDestory - Free memory");
    }
}
