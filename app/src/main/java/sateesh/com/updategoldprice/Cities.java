package sateesh.com.updategoldprice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Cities extends AppCompatActivity {

    String IPAddress = "192.168.1.113";
    EditText CityName;
    Button submit_button;
    RequestQueue requestQueue;
    String insertCityUrl = "http://" + IPAddress + "/gold_smart_updates/insertCity.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        CityName = (EditText) findViewById(R.id.city_name);
        submit_button = (Button) findViewById(R.id.submit_city);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CityName.getText().toString() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter all Details", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, insertCityUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("Sateesh", "submit onResponse");
                            Toast toast = Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("Sateesh ", "**** Submit Insert Error is " + error.toString());

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("CityName", CityName.getText().toString());
                            Log.v("Sateesh", "submit getParams");
                            return parameters;

                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
