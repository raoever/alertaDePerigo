package com.example.alertadeperigo.ui.agendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertadeperigo.AlertaReceiver;
import com.example.alertadeperigo.MainActivity;
import com.example.alertadeperigo.R;

import java.util.Calendar;

public class AgendarFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    String time;
    TextView textViewAgendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_agendar, container, false);
        calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        textViewAgendar = root.findViewById(R.id.textViewAgendar);

        timePickerDialog = new TimePickerDialog(getContext(),
                this,
                hora,
                minutos,
                true);
        timePickerDialog.show();
        return root;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String textoMinuto;
        if (minute < 10) {
            textoMinuto = "0" + minute;
        } else {
            textoMinuto = String.valueOf(minute);
        }
        time = "Agendado para às " + hourOfDay + ":" + textoMinuto + "h";
        Log.i("onTimeSet: ", time);
        textViewAgendar.setText(time);
        ((MainActivity)getActivity()).setDoacao(hourOfDay, minute);
    }
}