package br.com.usinasantafe.pbi.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import br.com.usinasantafe.pbi.view.MenuInicialActivity;
import br.com.usinasantafe.pbi.control.ConfigCTR;
import br.com.usinasantafe.pbi.control.MecanicoCTR;
import br.com.usinasantafe.pbi.model.pst.GenericRecordable;
import br.com.usinasantafe.pbi.util.conHttp.PostVerGenerico;
import br.com.usinasantafe.pbi.model.bean.AtualAplicBean;
import br.com.usinasantafe.pbi.util.conHttp.UrlsConexaoHttp;

/**
 * Created by anderson on 16/11/2015.
 */
public class VerifDadosServ {

    private static VerifDadosServ instance = null;
    private GenericRecordable genericRecordable;
    private UrlsConexaoHttp urlsConexaoHttp;
    private Context telaAtual;
    private Class telaProx;
    private ProgressDialog progressDialog;
    private String dado;
    private String tipo;
    private AtualAplicBean atualAplicBean;
    private MenuInicialActivity menuInicialActivity;
    private PostVerGenerico postVerGenerico;
    private boolean verTerm;
    private boolean finalManutPneu;

    public VerifDadosServ() {
    }

    public static VerifDadosServ getInstance() {
        if (instance == null)
            instance = new VerifDadosServ();
        return instance;
    }

    public void manipularDadosHttp(String result) {

        try {

            if (!result.equals("")) {

                if (this.tipo.equals("Atualiza")) {

                    String verAtual = result.trim();

                    if (verAtual.equals("SIM")) {
                        AtualizarAplicativo atualizarAplicativo = new AtualizarAplicativo();
                        atualizarAplicativo.setContext(this.menuInicialActivity);
                        atualizarAplicativo.execute();
                    } else {
                        this.menuInicialActivity.startTimer(verAtual);
                    }

                }
                else if(this.tipo.equals("OS")) {
                    MecanicoCTR mecanicoCTR = new MecanicoCTR();
                    mecanicoCTR.recDadosOS(result);
                }


            }

        } catch (Exception e) {
            Log.i("PMM", "Erro Manip atualizar = " + e);
        }

    }

    public void verDados(String dado, String tipo, Context telaAtual, Class telaProx, ProgressDialog progressDialog) {

        verTerm = false;
        urlsConexaoHttp = new UrlsConexaoHttp();
        this.telaAtual = telaAtual;
        this.telaProx = telaProx;
        this.progressDialog = progressDialog;
        this.dado = dado;
        this.tipo = tipo;

        envioDados();

    }

    public void verDadosPneu(String dado, String tipo, Context telaAtual, Class telaProx, ProgressDialog progressDialog, boolean finalManutPneu) {

        verTerm = false;
        urlsConexaoHttp = new UrlsConexaoHttp();
        this.telaAtual = telaAtual;
        this.telaProx = telaProx;
        this.progressDialog = progressDialog;
        this.dado = dado;
        this.tipo = tipo;
        this.finalManutPneu = finalManutPneu;

        envioDados();

    }

    public void verAtualAplic(String versaoAplic, MenuInicialActivity menuInicialActivity, ProgressDialog progressDialog) {

        urlsConexaoHttp = new UrlsConexaoHttp();
        this.progressDialog = progressDialog;
        this.tipo = "Atualiza";
        this.menuInicialActivity = menuInicialActivity;

        AtualAplicBean atualAplicBean = new AtualAplicBean();
        atualAplicBean.setVersaoAtual(versaoAplic);

        ConfigCTR configCTR = new ConfigCTR();

        atualAplicBean.setNroAparelhoAtual(configCTR.getConfig().getAparelhoConfig());

        JsonArray jsonArray = new JsonArray();

        Gson gson = new Gson();
        jsonArray.add(gson.toJsonTree(atualAplicBean, atualAplicBean.getClass()));

        JsonObject json = new JsonObject();
        json.add("dados", jsonArray);

        Log.i("PMM", "LISTA = " + json.toString());

        String[] url = {urlsConexaoHttp.urlVerifica(tipo)};
        Map<String, Object> parametrosPost = new HashMap<String, Object>();
        parametrosPost.put("dado", json.toString());

        postVerGenerico = new PostVerGenerico();
        postVerGenerico.setParametrosPost(parametrosPost);
        postVerGenerico.execute(url);

    }

    public void envioAtualizacao() {

        JsonArray jsonArray = new JsonArray();

        Gson gson = new Gson();
        jsonArray.add(gson.toJsonTree(atualAplicBean, atualAplicBean.getClass()));

        JsonObject json = new JsonObject();
        json.add("dados", jsonArray);

        Log.i("PMM", "LISTA = " + json.toString());

        String[] url = {urlsConexaoHttp.urlVerifica(tipo)};
        Map<String, Object> parametrosPost = new HashMap<String, Object>();
        parametrosPost.put("dado", json.toString());

        postVerGenerico = new PostVerGenerico();
        postVerGenerico.setParametrosPost(parametrosPost);
        postVerGenerico.execute(url);

    }

    public void envioDados() {

        String[] url = {urlsConexaoHttp.urlVerifica(tipo)};
        Map<String, Object> parametrosPost = new HashMap<String, Object>();
        parametrosPost.put("dado", String.valueOf(dado));

        Log.i("PMM", "VERIFICA = " + String.valueOf(dado));

        postVerGenerico = new PostVerGenerico();
        postVerGenerico.setParametrosPost(parametrosPost);
        postVerGenerico.execute(url);

    }


    public void cancelVer() {
        verTerm = true;
        if (postVerGenerico.getStatus() == AsyncTask.Status.RUNNING) {
            postVerGenerico.cancel(true);
        }
    }

    public boolean isVerTerm() {
        return verTerm;
    }

    public void pulaTelaComTerm(){
        if(!verTerm){
            this.progressDialog.dismiss();
            this.verTerm = true;
            Intent it = new Intent(telaAtual, telaProx);
            telaAtual.startActivity(it);
        }
    }

    public void msgComTerm(String texto){
        if(!verTerm){
            this.progressDialog.dismiss();
            this.verTerm = true;
            AlertDialog.Builder alerta = new AlertDialog.Builder(telaAtual);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage(texto);
            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alerta.show();
        }
    }

}
