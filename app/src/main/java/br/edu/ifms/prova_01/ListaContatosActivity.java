package br.edu.ifms.prova_01;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaContatosActivity extends ListActivity {

    private static final String[] CONTATOS = new String[]{"Professor", "Vagnata", "Jão Bobo"};
    private ArrayAdapter<String> meuAdaptador;
    private String fuelName;
    private String eachValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        this.fuelName = (String) bd.get("fuel_name");
        this.eachValue = (String) bd.get("each_value");

        meuAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CONTATOS);
        setListAdapter(meuAdaptador);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String destinatario;
        String assunto;
        String corpo;

        switch (position) {
            case (0):
                destinatario = "atividadesjonathas@gmail.com";
                assunto = "Valores da " + this.fuelName + " da Viagem";
                corpo = "Olá, segue o valor da gasolina na viagem: R$ " + this.eachValue;
                break;

            case (1):
                destinatario = "garciavagnata.vg@gmail.com";
                assunto = "Valores da " + this.fuelName + " da Viagem";
                corpo = "Olá, segue o valor da gasolina na viagem: R$ " + this.eachValue;
                break;

            case (2):
                destinatario = "jaobobo@gmail.com";
                assunto = "Valores da " + this.fuelName + " da Viagem";
                corpo = "Olá, segue o valor da gasolina na viagem: R$ " + this.eachValue;
                break;
            default:
                destinatario = "garciavagnata.vg@gmail.com";
                assunto = "Valores da " + this.fuelName + " da Viagem";
                corpo = "Olá, segue o valor da gasolina na viagem: R$ " + this.eachValue;
                break;
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});
        i.putExtra(Intent.EXTRA_SUBJECT, assunto);
        i.putExtra(Intent.EXTRA_TEXT, corpo);
        try {
            startActivity(Intent.createChooser(i, "Enviando email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ListaContatosActivity.this, "Configurar o app cliente de email,", Toast.LENGTH_SHORT).show();
        }

    }
}
