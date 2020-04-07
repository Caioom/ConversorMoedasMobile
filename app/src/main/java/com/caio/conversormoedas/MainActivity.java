package com.caio.conversormoedas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.caio.conversormoedas.models.Price;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "caio";
    private ViewHolder mViewHolder = new ViewHolder();

    double bid_dolar;
    double bid_euro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewHolder.editValue = findViewById(R.id.edit_value);
        this.mViewHolder.textDolar = findViewById(R.id.text_dolar);
        this.mViewHolder.textEuro = findViewById(R.id.text_euro);
        this.mViewHolder.buttonCalculate = findViewById(R.id.button_calculate);

        this.mViewHolder.buttonCalculate.setOnClickListener(this);
        this.clearValues();
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PriceService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PriceService service = retrofit.create(PriceService.class);
        Call<Price> price = service.getPrice();

        price.enqueue(new Callback<Price>() {
            @Override
            public void onResponse(Call<Price> call, Response<Price> response) {
                if(response.isSuccessful()){
                    Price price = response.body();

                    bid_dolar = Double.parseDouble(price.USD.bid);
                    bid_euro = Double.parseDouble(price.EUR.bid);

                }
            }

            @Override
            public void onFailure(Call<Price> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });

        if(v.getId() == R.id.button_calculate) {
            String value = this.mViewHolder.editValue.getText().toString();

            if("".equals(value)) {
                Toast.makeText(this, this.getString(R.string.informe_valor), Toast.LENGTH_LONG).show();
            } else {
                Double real = Double.valueOf(value);

                this.mViewHolder.textDolar.setText(String.format("%.2f", (real / bid_dolar)));
                this.mViewHolder.textEuro.setText(String.format("%.2f", (real / bid_euro)));
            }
        }
    }

    private void clearValues() {
        this.mViewHolder.textDolar.setText("");
        this.mViewHolder.textEuro.setText("");
    }


    private static class ViewHolder {
        EditText editValue;
        TextView textDolar;
        TextView textEuro;
        Button buttonCalculate;
    }

}
