package br.senai.sp.android.buscarendereco;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText txtCep;
    private EditText cepText, logradouroText, bairroText, localidadeText, estadoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCep = (EditText) findViewById(R.id.edtCep);
        logradouroText = (EditText) findViewById(R.id.edtEndereco);
        bairroText = (EditText) findViewById(R.id.edtBairro);
        localidadeText = (EditText) findViewById(R.id.edtCidade);
        estadoText = (EditText) findViewById(R.id.edtEstado);

        StrictMode.ThreadPolicy politicaDeAcesso =
                new StrictMode.ThreadPolicy.
                        Builder().permitAll().build();
        StrictMode.setThreadPolicy(politicaDeAcesso);

    }

    private String makeRequest(String enderecoUrl) {
        HttpURLConnection conexao = null;
        URL url = null;
        String resposta = "";

        try {
            url = new URL(enderecoUrl);
            conexao = (HttpURLConnection)
                    url.openConnection();
            resposta = readStream(conexao.
                    getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexao.disconnect();
        }

        return resposta;
    }

    private String readStream(InputStream in) {
        BufferedReader leitor = null;
        StringBuilder builder = new StringBuilder();

        try {
            leitor = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = leitor.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (leitor != null) {
                try {
                    leitor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString();
    }

    public void pesquisarCepClick(View view) {
        String nroCep = txtCep.getText().toString();
        String response = makeRequest("http://viacep.com.br/ws/"
                + nroCep + "/json/");

        try {
            JSONObject json = new JSONObject(response);
            String logradouro = json.getString("logradouro");
            String bairro = json.getString("bairro");
            String localidade = json.getString("localidade");
            String estado = json.getString("uf");

            logradouroText.setText(logradouro);
            bairroText.setText(bairro);
            localidadeText.setText(localidade);
            estadoText.setText(estado);
            limparCampo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void limparCampo(){
        txtCep.setText("");
        txtCep.requestFocus();
    }
}


