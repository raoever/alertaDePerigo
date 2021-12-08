package com.example.alertadeperigo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.alertadeperigo.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

//    public void setTextAlerta(String text){
//        TextView textView = (TextView) getView().findViewById(R.id.textViewAlerta);
//        textView.setText("Alerta de "+text);
//    }
}