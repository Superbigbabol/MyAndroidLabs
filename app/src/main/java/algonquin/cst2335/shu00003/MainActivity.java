package algonquin.cst2335.shu00003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    // initial a RequestQueue for JsonObjectRequest
    RequestQueue queue;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(MainActivity.this);

        binding.forecastButton.setOnClickListener(clk -> {
            cityName = binding.cityText.getText().toString();
            String url = null;
            try {
                url = new StringBuilder()
                        .append("https://api.openweathermap.org/data/2.5/weather?q=")
                        .append(URLEncoder.encode(cityName, "UTF-8"))
                        .append("&appid=847786e933e992540a9fa255f639a62d&units=metric")
                        .toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONObject coord = response.getJSONObject("coord");

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            String imgUrl = "https://openweathermap.org/img/w/" + iconName + ".png";
                            //check if image exists
                            String imgPath = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(imgPath);
                            if (file.exists()) {
                                image = BitmapFactory.decodeFile(imgPath);
                            } else {
                                ImageRequest imgReq = new ImageRequest(imgUrl,
                                        response1 -> {
                                            image = response1;
                                            FileOutputStream fOut = null;
                                            try {
                                                fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                                fOut.flush();
                                                fOut.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        error -> {
                                            Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                                });
                                queue.add(imgReq);
                            }
                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            int vis = response.getInt("visibility");
                            String name = response.getString("name");

                            runOnUiThread(()->{
                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The humidity is " + humidity + "%");
                                binding.humidity.setVisibility(View.VISIBLE);
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {});
            queue.add(request);
        });
    }

}