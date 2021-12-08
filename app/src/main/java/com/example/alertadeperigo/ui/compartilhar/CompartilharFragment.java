package com.example.alertadeperigo.ui.compartilhar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.alertadeperigo.R;

public class CompartilharFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_compartilhar, container, false);
        Button button = (Button) root.findViewById(R.id.btnEnviar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = root.findViewById(R.id.radioGroup);

                int id = radioGroup.getCheckedRadioButtonId();
                Intent intent = null;
//                String text = String.valueOf(editTextMensagem.getText());
                Log.i("clicar", String.valueOf(id));
                switch (id) {
                    case R.id.radioButtonWhatsApp:
                        Log.i("clicar", "whatsapp");
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "Alerta");
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");
                        startActivity(intent);
                        break;
                    case R.id.radioButtonGmail:
                        Log.i("clicar", "gmail");
                        String email = "teste@gmail.com";
                        String subject = "Alerta de Perigo!";
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, "Alerta de Perigo!");
                        intent.setType("text/plain");
                        intent.setPackage("com.google.android.gm");
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getContext(), "Escolha uma opção de envio.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        return root;
    }
}