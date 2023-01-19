package algonquin.cst2335.shu00003.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import algonquin.cst2335.shu00003.data.MainViewModel;
import algonquin.cst2335.shu00003.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

//        TextView mytext = findViewById(R.id.textview);
        TextView mytext = variableBinding.textview;

//        Button btn = findViewById(R.id.mybutton);
        Button btn = variableBinding.mybutton;

//        EditText myedit = findViewById(R.id.myedittext);
        EditText myedit = variableBinding.myedittext;

        CheckBox mycheckbox = variableBinding.mycheckbox;
        Switch myswitch = variableBinding.myswitch;
        RadioButton myradiobutton = variableBinding.myradiobutton;

        ImageView myimage = variableBinding.myimage;
        ImageButton myimagebtn = variableBinding.myimagebtn;

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String editString = myedit.getText().toString();
//                mytext.setText("Your edit text has:"+editString);
//            }
//        });
        model.editString.observe(this,
                s -> mytext.setText("Your edit text has:"+s)
        );

        btn.setOnClickListener( vw -> {
            model.editString.postValue(myedit.getText().toString());
        });

//        model.editString.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                mytext.setText("Your edit text has:"+ s);
//            }
//        });

        model.isSelected.observe(this,
                selected -> {
                    mycheckbox.setChecked(selected);
                    myswitch.setChecked(selected);
                    myradiobutton.setChecked(selected);
                    Context context = getApplicationContext();
                    CharSequence text = "The value is now:"+selected;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                }
        );

        mycheckbox.setOnCheckedChangeListener((cb, isChecked) -> {
            model.isSelected.postValue(mycheckbox.isChecked());
        });

        myswitch.setOnCheckedChangeListener((cb, isChecked) -> {
            model.isSelected.postValue(myswitch.isChecked());
        });

        myradiobutton.setOnCheckedChangeListener((cb, isChecked) -> {
            model.isSelected.postValue(myradiobutton.isChecked());
        });

        myimagebtn.setOnClickListener( vw -> {
            Context context = getApplicationContext();
            CharSequence text = "The width ="+myimagebtn.getWidth()+" and height="+myimagebtn.getHeight();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
        });
    }
}