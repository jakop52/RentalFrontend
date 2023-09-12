package com.jakop52.apartmentrent.android.interfaces;
import android.view.View;

public interface IClickListener<T> {

    void onClick(View view, T data, int position);
}
