package com.caio.conversormoedas;

import com.caio.conversormoedas.models.Price;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PriceService {
    public static final String BASE_URL = "https://economia.awesomeapi.com.br/all/";

    @GET("USD-BRL,EUR-BRL")
    Call<Price> getPrice();
}
