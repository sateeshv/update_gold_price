package sateesh.com.updategoldprice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button Prices, Cities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Prices = (Button) findViewById(R.id.price_insert);
        Cities = (Button) findViewById(R.id.city_insert);

        Prices.setOnClickListener(this);
        Cities.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.price_insert:
                Intent goldPrices = new Intent(this, GoldPrices.class);
                startActivity(goldPrices);
                break;
            case R.id.city_insert:
                Intent CitiesData = new Intent(this, Cities.class);
                startActivity(CitiesData);
                break;
        }
    }
}
