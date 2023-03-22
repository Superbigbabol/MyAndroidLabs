package algonquin.cst2335.shu00003;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import algonquin.cst2335.shu00003.databinding.ActivityMainBinding;

/**
 * @author Bo Shu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen*/
    private TextView tv = null;
    
    /** This holds the password being entered */
    private EditText et = null;

    /** This holds login button at the bottom */
    private Button btn = null;

    protected String cityName;

    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tv = binding.textView;
        et = binding.cityText;
        btn = binding.forecastButton;

        btn.setOnClickListener(clk -> {
            cityName = binding.cityText.getText().toString();

//            JsonObjectRequest request = new JsonObjectRequest()
        });
    }

    /**
     * check if the password has an upper Case, a lower case letter, a number, and a special symbol
     * show a toast message if it is not missing requirement.
     * @param pw a string password to be verified
     * @return  returns true or false
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        for (int i = 0; i < pw.length(); i++){
            if(Character.isDigit(pw.charAt(i))){foundNumber=true;}
            if(Character.isLowerCase(pw.charAt(i))){foundLowerCase=true;}
            if(Character.isUpperCase(pw.charAt(i))){foundUpperCase=true;}
            if(isSpecialCharacter(pw.charAt(i))){foundSpecial=true;}
        }
        if (!foundLowerCase){
            Toast.makeText(MainActivity.this,"Missing a lower case letter!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundNumber){
            Toast.makeText(MainActivity.this,"Missing a number!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundUpperCase){
            Toast.makeText(MainActivity.this,"Missing an upper case letter!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundSpecial){
            Toast.makeText(MainActivity.this,"Missing a special symbol!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * check if a character is one of the special characters
     * @param c a char to check
     * @return returns true or false
     */
    boolean isSpecialCharacter(char c){
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}