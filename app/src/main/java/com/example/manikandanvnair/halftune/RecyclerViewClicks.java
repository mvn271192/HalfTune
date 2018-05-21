package com.example.manikandanvnair.halftune;

import android.view.View;
import android.widget.Button;

/**
 * Created by manikandanvnair on 17/05/18.
 */

public interface RecyclerViewClicks {

    void onItemSelected(int position, View v);
    void onButtonClicked(int position, View cell, Button button);
    void onLongClicked(int position);
}
