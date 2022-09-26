package com.example.healify.view.detail;


import android.support.annotation.NonNull;

import com.example.healify.Utils;
import com.example.healify.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter {
    private DetailView view;

    public DetailPresenter(DetailView view) {
        this.view = view;
    }

    void getMealById(String mealName) {

        view.showLoading();

        Utils.getApi().getMealByName(mealName).enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(@NonNull Call<Meals> call,@NonNull Response<Meals> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null){

                    view.setMeal(response.body().getMeals().get(0));
                }
                if(response.isSuccessful() && response.body() != null){

                    view.onErrorLoading(response.message());
                }


                else {

                    view.onErrorLoading(response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<Meals> call,@NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });

    }
}
