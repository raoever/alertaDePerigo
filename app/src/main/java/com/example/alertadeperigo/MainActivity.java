package com.example.alertadeperigo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.alertadeperigo.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextToSpeech textToSpeech;
    private String msg;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private DatabaseReference database;
    private String tipoDeAlerta;
    private String localizacao;
    private boolean inicio = true;
    private ArrayList<String> alertas = new ArrayList<>();
    private List<Integer> numeros;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numeros = new ArrayList<>();
        textToSpeech = new TextToSpeech(this, this);
        if (numeros.size()<1){
            database = FirebaseDatabase.getInstance().getReference().child("alertas");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    alertas.clear();
                    for (DataSnapshot alerta :
                            snapshot.getChildren()) {
                        alertas.add(alerta.getValue().toString());
                    }
                    Log.i("onDataChange: ", alertas.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Mapa de abrigos.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                startActivity(i);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_compartilhar, R.id.nav_agendar)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        createNotificationChannel();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent2 = new Intent(this, AlertaReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,
                1, intent2, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10 * 1000, 10 * 1000, pendingIntent);
    }

    private void gerarAleatorios() {
        numeros.clear();
        Random random = new Random();
        for (int i = 0; i < 7; i++){
            Integer num = random.nextInt(7) + 1;
            numeros.add(num);
        }
        Log.i("gerarAleatorios: ", numeros.toString());
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//        finishAffinity();
//        System.exit(0);
//        return false;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = "Alerta de Enchente!";
        String description = "Clique para saber mais.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("notificacao", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void falar(String tipo) {
        Log.i("onInit: ", "aqui");
        msg = "Alerta de " + tipo + ", Clique na notificação para saber mais!";
        Locale locale = new Locale("pt", "br");
        int result = textToSpeech.setLanguage(locale);
        textToSpeech.setSpeechRate(1f);

        if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("problemasI", "problemas com o idioma");
        } else {
            textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH,
                    null, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            } else {
                textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH,
                        null);
            }
        }
    }

    @Override
    public void onInit(int i) {
        if (alertas.size()>0){
            tipoDeAlerta = alertas.get(pegaAleatorio());
            Log.i("onInit: ", tipoDeAlerta);
        }


        if (alertas.size()<1) {
            if (i == TextToSpeech.SUCCESS) {
                msg = "Sem alertas no momento!";
                Locale locale = new Locale("pt", "br");
                int result = textToSpeech.setLanguage(locale);
                textToSpeech.setSpeechRate(1f);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("problemasI", "problemas com o idioma");
                } else {
                    textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH,
                            null, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    } else {
                        textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH,
                                null);
                    }
                }
            } else {
                Log.e("problemasT", "problemas com o textToSpeech");
            }
        }
    }

    private int pegaAleatorio() {
        Random random = new Random();
        int response = random.nextInt(6) + 1;
        return response;
    }

    public void setDoacao(int hora, int minuto) {


//        Log.i("setNotificacao: ", hora + " " + minuto);
//
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent2 = new Intent(this, AlertaReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this,
//                1, intent2, 0);
//
//        calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hora);
//        calendar.set(Calendar.MINUTE, minuto);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                pendingIntent);
    }
}