package com.example.alertadeperigo.ui.agendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        Button buttonAgendar = root.findViewById(R.id.buttonAgendar);
        buttonAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minutos = calendar.get(Calendar.MINUTE);
                textViewAgendar = root.findViewById(R.id.textViewAgendar);

                timePickerDialog = new TimePickerDialog(getContext(),
                        AgendarFragment.this,
                        hora,
                        minutos,
                        true);
                timePickerDialog.show();
            }
        });
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
        time = "Doação agendada para às " + hourOfDay + ":" + textoMinuto + "h";
        Log.i("onTimeSet: ", time);
        textViewAgendar.setText(time);
        ((MainActivity)getActivity()).setDoacao(hourOfDay, minute);
    }
}