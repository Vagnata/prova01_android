package br.edu.ifms.prova_01;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    static final int ID_BUTTON_LIMPAR = R.id.button4;
    static final int ID_BUTTON_CALCULAR = R.id.button2;
    static final int ID_BUTTON_ENVIAR_EMAIL = R.id.button3;
    static final int ID_SPINNER_COMBUSTIVEL = R.id.spinnerCombustivel;
    static final int ID_INPUT_DISTANCIA = R.id.inputDistancia;
    static final int ID_INPUT_QTD_AMIGOS = R.id.inputQtdAmigos;
    static final int ID_INPUT_TOTAL = R.id.inputTotalPessoas;
    static final int ID_RADIO_IDA = R.id.radioIda;
    static final int ID_RADIO_IDA_VOLTA = R.id.radioIdaVolta;
    static final double GAS_PRICE = 4.35;
    static final double ETHANOL_PRICE = 3.35;
    static final double DIESEL_PRICE = 3.39;
    static final int CONSUMPTION = 10;

    private Spinner spinnerCombustiveis;
    private Button limpar;
    private Button calcular;
    private Button enviarEmail;
    private EditText inputDistancia;
    private EditText inputQtdAmigos;
    private EditText inputTotalPessoas;
    private RadioButton radioIda;
    private RadioButton radioIdaVolta;
    private int multiplicador;
    private double actual_fuel;
    private String actual_fuel_name;
    private boolean calculated;
    private double forEachPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startElements();
        this.fillSpinnerField();

        limpar.setOnClickListener(this);
        calcular.setOnClickListener(this);
        enviarEmail.setOnClickListener(this);
        spinnerCombustiveis.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (ID_BUTTON_LIMPAR):
                this.clearForm();
                break;

            case (ID_BUTTON_CALCULAR):
                if (!this.handleRequest()){
                    break;
                }
                double total = this.calculateTotal();
                this.forEachPerson = total / Integer.parseInt(inputQtdAmigos.getText().toString());
                String formatedValue = new DecimalFormat("0.00").format(forEachPerson);
                this.inputTotalPessoas.setText(formatedValue);
                this.calculated = true;
                break;

            case (ID_BUTTON_ENVIAR_EMAIL):
                if(!this.calculated){
                    this.warningBox("Você ainda não calculou o valor da viagem!!!");
                    break;
                }
                Intent intent = new Intent(MainActivity.this, ListaContatosActivity.class);
                intent.putExtra("fuel_name", this.actual_fuel_name);
                intent.putExtra("each_value", Double.toString(this.forEachPerson));
                startActivity(intent);
                break;
            default:
                this.warningBox("Opção Inválida");
                break;
        }
    }

    public double calculateTotal() {
        if (radioIda.isChecked()) {
            this.multiplicador = 1;
        } else {
            this.multiplicador = 2;
        }
        double litros = (Double.parseDouble(this.inputDistancia.getText().toString()) / CONSUMPTION) * this.multiplicador;

        return litros * this.actual_fuel;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case (0):
                this.actual_fuel = ETHANOL_PRICE;
                this.actual_fuel_name = "Etanol";
                break;
            case (1):
                this.actual_fuel = DIESEL_PRICE;
                this.actual_fuel_name = "Diesel";
                break;
            case (2):
                this.actual_fuel = GAS_PRICE;
                this.actual_fuel_name = "Gasolina";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void clearForm() {
        this.inputDistancia.setText("");
        this.inputQtdAmigos.setText("");
        this.inputTotalPessoas.setText("");
        this.radioIda.setChecked(false);
        this.radioIdaVolta.setChecked(false);
    }

    private boolean handleRequest() {
        if (this.inputDistancia.getText().toString().length() <= 0) {
            this.inputDistancia.setError("Campo em Branco");
            return false;
        }

        if(!this.radioIda.isChecked() && !this.radioIdaVolta.isChecked()) {
            this.radioIda.setError("Campo obrigatório");
            this.radioIdaVolta.setError("Campo obrigatório");
            return false;
        }

        if (this.inputQtdAmigos.getText().toString().length() <= 0) {
            this.inputQtdAmigos.setError("Campo em Branco");
            return false;
        }

        return true;
    }

    private void startElements() {
        this.limpar = findViewById(ID_BUTTON_LIMPAR);
        this.calcular = findViewById(ID_BUTTON_CALCULAR);
        this.enviarEmail = findViewById(ID_BUTTON_ENVIAR_EMAIL);
        this.spinnerCombustiveis = findViewById(ID_SPINNER_COMBUSTIVEL);
        this.inputDistancia = findViewById(ID_INPUT_DISTANCIA);
        this.inputQtdAmigos = findViewById(ID_INPUT_QTD_AMIGOS);
        this.inputTotalPessoas = findViewById(ID_INPUT_TOTAL);
        this.radioIda = findViewById(ID_RADIO_IDA);
        this.radioIdaVolta = findViewById(ID_RADIO_IDA_VOLTA);
        this.multiplicador = 1;
        this.actual_fuel = ETHANOL_PRICE;
        this.actual_fuel_name = "Etanol";
        this.calculated = false;
    }

    private void fillSpinnerField() {
        ArrayList<String> dadosSpinner = new ArrayList<>();
        dadosSpinner.add("Etanol");
        dadosSpinner.add("Diesel");
        dadosSpinner.add("Gasolina");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dadosSpinner);
        this.spinnerCombustiveis.setAdapter(adapter);
    }

    private void warningBox(String message) {
        AlertDialog.Builder wrongButton;
        wrongButton = new AlertDialog.Builder(MainActivity.this);
        wrongButton.setTitle("Gas Raches");
        wrongButton.setMessage(message);
        wrongButton.setNeutralButton("OK", null);

        wrongButton.show();
    }
}
