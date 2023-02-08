package algonquin.cst2335.shu00003;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.loginButton);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if (checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
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