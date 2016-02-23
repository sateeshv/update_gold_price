package sateesh.com.updategoldprice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GoldPrices extends AppCompatActivity {

    TextView Date_txt, Gold_1_Gram_txt, Silver_1_Gram_text, City_Name;
    EditText Gold_8_Gram_EditText, Silver_1_Gram_EditText;
    TextView selectedDate, Gold_1_Gram_TextView, Gold_Change_TextView, Silver_Change_TextView;
    Spinner Cities_spinner;
    Button Submit_btn, Load_btn, Date_btn;
    String[] Month_Names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    ArrayList<String> cities_list;
    ArrayAdapter<String> citiesAdapter;
    String insertUrl = "http://192.168.1.128/gold_smart_updates/insertPrice.php";
    String showUrl = "http://192.168.1.128/gold_smart_updates/showLastPrice.php";
    String showCitiesUrl = "http://192.168.1.128/gold_smart_updates/showCities.php";
    String show_Data_City_level_Url = "http://192.168.1.128/gold_smart_updates/showLastPriceWithCity.php";
    RequestQueue requestQueue;
    String selectedCity;

    StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_prices);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        showCities();
        if (cities_list != null) {
            citiesAdapter = new ArrayAdapter<String>(GoldPrices.this, R.layout.list_item, cities_list);
//            citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Cities_spinner.setAdapter(citiesAdapter);

        }

        City_Name = (TextView) findViewById(R.id.city_name);
        Gold_8_Gram_EditText = (EditText) findViewById(R.id.gold_22ct_8_grams);
        Gold_1_Gram_TextView = (TextView) findViewById(R.id.gold_22ct_1_gram);
        Silver_1_Gram_EditText = (EditText) findViewById(R.id.silver_1_gram);
        Gold_Change_TextView = (TextView) findViewById(R.id.gold_change);
        Silver_Change_TextView = (TextView) findViewById(R.id.silver_change);

        Gold_1_Gram_txt = (TextView) findViewById(R.id.one_gram_gold_value);
        Silver_1_Gram_text = (TextView) findViewById(R.id.one_gram_silver_value);

        Date_btn = (Button) findViewById(R.id.choose_Date);
        Date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment2 = new StartDate_DatePickerFragment ();
                newFragment2.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Load_btn = (Button) findViewById(R.id.load_button);
        Load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Gold_8_Grams_string = Gold_8_Gram_EditText.getText().toString();
                int Gold_8_Grams_int = Integer.parseInt(Gold_8_Grams_string);

                Gold_1_Gram_TextView.setText(String.valueOf(Gold_8_Grams_int / 8));

                int Y_Gold_1_Gram = Integer.parseInt(Gold_1_Gram_txt.getText().toString());
                int Y_Silver_1_Gram = Integer.parseInt(Silver_1_Gram_text.getText().toString());

                int T_Gold_1_Gram = Integer.parseInt(Gold_1_Gram_TextView.getText().toString());
                int T_Silver_1_Gram = Integer.parseInt(Silver_1_Gram_EditText.getText().toString());

                if (Y_Gold_1_Gram == T_Gold_1_Gram) {
                    Gold_Change_TextView.setText("No Change");
                } else if (Y_Gold_1_Gram > T_Gold_1_Gram) {
                    Gold_Change_TextView.setText("Decreased");
                } else if (Y_Gold_1_Gram < T_Gold_1_Gram) {
                    Gold_Change_TextView.setText("Increased");
                }

                if (Y_Silver_1_Gram == T_Silver_1_Gram) {
                    Silver_Change_TextView.setText("No Change");
                } else if (Y_Silver_1_Gram > T_Silver_1_Gram) {
                    Silver_Change_TextView.setText("Decreased");
                } else if (Y_Silver_1_Gram < T_Silver_1_Gram) {
                    Silver_Change_TextView.setText("Increased");
                }


            }
        });


        Submit_btn = (Button) findViewById(R.id.submit_button);

        Submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
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
                        parameters.put("PriceDate", selectedDate.getText().toString());
                        parameters.put("City", City_Name.getText().toString());
                        parameters.put("Gold_22ct_8_gram", Gold_8_Gram_EditText.getText().toString());
                        parameters.put("Gold_22ct_1_gram", Gold_1_Gram_TextView.getText().toString());
                        parameters.put("Silver_1_gram", Silver_1_Gram_EditText.getText().toString());
                        parameters.put("Gold_Change", Gold_Change_TextView.getText().toString());
                        parameters.put("Silver_Change", Silver_Change_TextView.getText().toString());
                        Log.v("Sateesh", "submit getParams");
                        return parameters;

                    }
                };
                requestQueue.add(stringRequest);

            }
        });


        Cities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Cities_spinner != null && Cities_spinner.getChildAt(0) != null) {
                    TextView selectedTextView = (TextView) view.findViewById(R.id.item); // You may need to replace android.R.id.text1 whatever your TextView label id is
                    selectedCity = selectedTextView.getText().toString();
                    Log.v("Sateesh: ", "*** Selected option is: " + selectedCity);

                    showData(selectedCity);
                    City_Name.setText(selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void showCities() {
        Date_txt = (TextView) findViewById(R.id.date_value);
        Gold_1_Gram_txt = (TextView) findViewById(R.id.one_gram_gold_value);
        Silver_1_Gram_text = (TextView) findViewById(R.id.one_gram_silver_value);
        Cities_spinner = (Spinner) findViewById(R.id.spinner_cities_list);
        cities_list = new ArrayList<String>();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showCitiesUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("Sateesh ", "*** Cities Response is working");
                    JSONArray cities = response.getJSONArray("Cities");
                    for (int i = 0; i < cities.length(); i++) {
                        Log.v("Sateesh ", "*** Cities length of data is: " + cities.length());
                        JSONObject student = cities.getJSONObject(i);


                        String City = student.getString("CityName");
                        Log.v("Sateesh", "*** CitNames are : " + City);
                        cities_list.add(City);

                    }
                    citiesAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Sateesh ", "**** Error is " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void show() {
        Date_txt = (TextView) findViewById(R.id.date_value);
        Gold_1_Gram_txt = (TextView) findViewById(R.id.one_gram_gold_value);
        Silver_1_Gram_text = (TextView) findViewById(R.id.one_gram_silver_value);
        Log.v("Sateesh ", "*** Show started working");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("Sateesh ", "*** Response is working");
                    JSONArray students = response.getJSONArray("Cities");
                    for (int i = 0; i < students.length(); i++) {
                        Log.v("Sateesh ", "*** length of data is: " + students.length());
                        JSONObject student = students.getJSONObject(i);

                        String PriceDate = student.getString("PriceDate");
                        String City = student.getString("City");
                        int Gold_22ct_8_gram = student.getInt("Gold_22ct_8_gram");
                        int Gold_22ct_1_gram = student.getInt("Gold_22ct_1_gram");
                        int Silver_1_gram = student.getInt("Silver_1_gram");
                        String Gold_Change = student.getString("Gold_Change");
                        String Silver_Change = student.getString("Silver_Change");

                        Date_txt.setText(PriceDate);
                        Gold_1_Gram_txt.setText(String.valueOf(Gold_22ct_1_gram));
                        Silver_1_Gram_text.setText(String.valueOf(Silver_1_gram));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Sateesh ", "**** Error is " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void showData(final String userSelectedCity) {
        Date_txt = (TextView) findViewById(R.id.date_value);
        Gold_1_Gram_txt = (TextView) findViewById(R.id.one_gram_gold_value);
        Silver_1_Gram_text = (TextView) findViewById(R.id.one_gram_silver_value);
        Log.v("Sateesh ", "*** ShowData started working");


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                show_Data_City_level_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Sateesh ", "*** Response is working");

                try {

                    JSONObject data = new JSONObject(response);
                    JSONArray students = data.getJSONArray("Cities");
                    Log.v("Sateesh ", "*** JSON Array data is :" + students);
                    Log.v("Sateesh ", "*** JSON Array Length is :" + students.length());
                    if (students.length() > 0) {
                        JSONObject student = students.getJSONObject(0);

                        String PriceDate = student.getString("PriceDate");
                        String City = student.getString("City");

                        int Gold_22ct_1_gram = student.getInt("Gold_22ct_1_gram");
                        int Silver_1_gram = student.getInt("Silver_1_gram");

                        Log.v("Sateesh", "*** From DB: Date is: " + PriceDate);
                        Log.v("Sateesh", "*** From DB: City is: " + City);
                        Log.v("Sateesh", "*** From DB: Yesterday Gold 1 Gram is : " + String.valueOf(Gold_22ct_1_gram));
                        Log.v("Sateesh", "*** From DB: Yesterday Silver 1 Gram is : " + String.valueOf(Silver_1_gram));

                        Date_txt.setText(PriceDate);
                        Gold_1_Gram_txt.setText(String.valueOf(Gold_22ct_1_gram));
                        Silver_1_Gram_text.setText(String.valueOf(Silver_1_gram));

//                        for Testing Purpose
                        Log.v("Sateesh", "*** Date is: " + Date_txt.getText().toString());
                        Log.v("Sateesh", "*** Yesterday Gold 1 Gram is : " + Gold_1_Gram_txt.getText().toString());
                        Log.v("Sateesh", "*** Yesterday Silver 1 Gram is : " + Silver_1_Gram_text.getText().toString());
                    } else {

                        Date_txt.setText("");
                        Gold_1_Gram_txt.setText(String.valueOf(""));
                        Silver_1_Gram_text.setText(String.valueOf(""));

//                        for Testing Purpose
                        Log.v("Sateesh", "*** Date is: " + Date_txt.getText().toString());
                        Log.v("Sateesh", "*** Yesterday Gold 1 Gram is : " + Gold_1_Gram_txt.getText().toString());
                        Log.v("Sateesh", "*** Yesterday Silver 1 Gram is : " + Silver_1_Gram_text.getText().toString());
                    }
                } catch (JSONException e1) {
                    Log.v("Sateesh ", "**** Error is " + e1.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Sateesh ", "**** Error is " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("City", userSelectedCity);
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }




    public class StartDate_DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //populateSetDate(year, month, day);
            selectedDate = (TextView) findViewById(R.id.enter_date_value);
            String StartDate_Month_Name = Month_Names[month];
            selectedDate.setText(day + "-" + StartDate_Month_Name + "-" + year);
//            Start_Date_Years = year;
//            Start_Date_Months = month + 1;
//            Start_Date_Days = day;
        }
    }
}
