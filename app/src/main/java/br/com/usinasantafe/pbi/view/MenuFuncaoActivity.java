package br.com.usinasantafe.pbi.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.usinasantafe.pbi.PBIContext;
import br.com.usinasantafe.pbi.R;
import br.com.usinasantafe.pbi.util.EnvioDadosServ;
import br.com.usinasantafe.pbi.util.Tempo;

public class MenuFuncaoActivity extends ActivityGeneric {

    private ListView menuFuncaoListView;
    private PBIContext pbiContext;

    private TextView textViewProcesso;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_funcao);

        pbiContext = (PBIContext) getApplication();

        textViewProcesso = (TextView) findViewById(R.id.textViewProcesso);

        customHandler.postDelayed(updateTimerThread, 0);

        ArrayList<String> itens = new ArrayList<String>();

        if((pbiContext.getConfigCTR().getColab().getFlagApontIndColab() > 0L)
            || (pbiContext.getConfigCTR().getColab().getFlagApontIndColab() > 0L) ){
            itens.add("APONTAMENTO");
            itens.add("FINALIZAR/INTERROPER");
            itens.add("FINALIZAR TURNO");
            itens.add("HISTÓRICO");
        }

        if(pbiContext.getConfigCTR().getColab().getFlagSolicColab() == 1L){
            itens.add("REQUISITAR PRODUTO");
        }

        AdapterList adapterList = new AdapterList(this, itens);
        menuFuncaoListView = (ListView) findViewById(R.id.listViewMenuFuncao);
        menuFuncaoListView.setAdapter(adapterList);

        menuFuncaoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position,
                                    long id) {

                TextView textView = (TextView) v.findViewById(R.id.textViewItemList);
                String text = textView.getText().toString();

                pbiContext.getMecanicoCTR().atualSalvarBoletim();

                if (text.equals("APONTAMENTO")) {

                    Intent it;
                    if (!pbiContext.getMecanicoCTR().verApont()) {
                        if (Tempo.getInstance().verifDataHoraParada(Tempo.getInstance().dataSHoraComTZ() + " " + pbiContext.getMecanicoCTR().getEscalaTrab(pbiContext.getMecanicoCTR().getColabApont().getIdEscalaTrabColab()).getHorarioEntEscalaTrab())) {
                            pbiContext.setVerTela(3);
                            it = new Intent(MenuFuncaoActivity.this, OSActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            pbiContext.setVerTela(1);
                            it = new Intent(MenuFuncaoActivity.this, ListaParadaActivity.class);
                            startActivity(it);
                            finish();
                        }
                    } else {

//                        if(Tempo.getInstance().verifDataHoraFechBoletim(pbiContext.getMecanicoCTR().getUltApont().getDthrInicialApont())) {

                            if (pbiContext.getMecanicoCTR().getUltApont().getDthrInicialApont().equals(Tempo.getInstance().dataHora())) {
                                Toast.makeText(MenuFuncaoActivity.this, "POR FAVOR! ESPERE 1 MINUTO PARA REALIZAR UM NOVO APONTAMENTO.",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                if (pbiContext.getMecanicoCTR().getUltApont().getDthrFinalApont().equals("")) {
                                    pbiContext.setVerTela(3);
                                    it = new Intent(MenuFuncaoActivity.this, OSActivity.class);
                                    startActivity(it);
                                    finish();
                                } else {
                                    if (Tempo.getInstance().verifDataHoraParada(pbiContext.getMecanicoCTR().getUltApont().getDthrFinalApont())) {
                                        pbiContext.setVerTela(3);
                                        it = new Intent(MenuFuncaoActivity.this, OSActivity.class);
                                        startActivity(it);
                                        finish();
                                    } else {
                                        pbiContext.setVerTela(1);
                                        it = new Intent(MenuFuncaoActivity.this, ListaParadaActivity.class);
                                        startActivity(it);
                                        finish();
                                    }
                                }

                            }

                    }

                } else if (text.equals("FINALIZAR/INTERROPER")) {

                    if (pbiContext.getMecanicoCTR().verApont()) {

                        if(Tempo.getInstance().verifDataHoraFechBoletim(pbiContext.getMecanicoCTR().getUltApont().getDthrInicialApont())) {

                            if (pbiContext.getMecanicoCTR().getUltApont().getParadaApont() == 0L) {
                                Intent it = new Intent(MenuFuncaoActivity.this, OpcaoInterFinalActivity.class);
                                startActivity(it);
                                finish();
                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(MenuFuncaoActivity.this);
                                alerta.setTitle("ATENÇÃO");
                                alerta.setMessage("NÃO EXISTE APONTAMENTO PARA FINALIZAR/INTERROMPER.");
                                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                alerta.show();
                            }

                        }
                        else{

                            AlertDialog.Builder alerta = new AlertDialog.Builder(MenuFuncaoActivity.this);
                            alerta.setTitle("ATENÇÃO");
                            alerta.setMessage("O BOLETIM SERÁ ENCERRADO POR FALTA DE ENCERRAMENTO DO TURNO ANTERIOR!");
                            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    pbiContext.getMecanicoCTR().forcarFechBoletim();

                                    Intent it = new Intent(MenuFuncaoActivity.this, MenuInicialActivity.class);
                                    startActivity(it);
                                    finish();

                                }
                            });

                            alerta.show();

                        }


                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuFuncaoActivity.this);
                        alerta.setTitle("ATENÇÃO");
                        alerta.setMessage("NÃO EXISTE APONTAMENTO PARA FINALIZAR/INTERROMPER.");
                        alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alerta.show();
                    }

                } else if (text.equals("FINALIZAR TURNO")) {

                    AlertDialog.Builder alerta = new AlertDialog.Builder(MenuFuncaoActivity.this);
                    alerta.setTitle("ATENÇÃO");

                    String label = "DESEJA REALMENTE FINALIZAR O TURNO?";

                    alerta.setMessage(label);
                    alerta.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent it;

                            if (pbiContext.getMecanicoCTR().verApont()) {

                                if (pbiContext.getMecanicoCTR().getUltApont().getParadaApont() == 0L) {

                                    pbiContext.getMecanicoCTR().fecharBoletim();

                                    it = new Intent(MenuFuncaoActivity.this, MenuInicialActivity.class);
                                    startActivity(it);
                                    finish();

                                } else {

                                    if (Tempo.getInstance().verifDataHoraParada(pbiContext.getMecanicoCTR().getUltApont().getDthrFinalApont())) {

                                        pbiContext.getMecanicoCTR().fecharBoletim();

                                        it = new Intent(MenuFuncaoActivity.this, MenuInicialActivity.class);
                                        startActivity(it);
                                        finish();

                                    } else {
                                        pbiContext.setVerTela(2);
                                        it = new Intent(MenuFuncaoActivity.this, ListaParadaActivity.class);
                                        startActivity(it);
                                        finish();
                                    }
                                }

                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(MenuFuncaoActivity.this);
                                alerta.setTitle("ATENÇÃO");
                                alerta.setMessage("O BOLETIM NÃO PODE SER ENCERRADO SEM APONTAMENTO! POR FAVOR, APONTE O MESMO.");
                                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                alerta.show();
                            }

                        }

                    });


                    alerta.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    });

                    alerta.show();

                } else if (text.equals("HISTÓRICO")) {

                    Intent it = new Intent(MenuFuncaoActivity.this, ListaHistoricoActivity.class);
                    startActivity(it);
                    finish();

                } else if (text.equals("REQUISITAR PRODUTO")) {

                    pbiContext.getReqProdutoCTR().setCabecReqProduto();
                    pbiContext.setVerTela(4);
                    Intent it = new Intent(MenuFuncaoActivity.this, OSActivity.class);
                    startActivity(it);
                    finish();
                }

            }

        });


    }


    public void onBackPressed() {
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            if (EnvioDadosServ.getInstance().getStatusEnvio() == 1) {
                textViewProcesso.setTextColor(Color.YELLOW);
                textViewProcesso.setText("Enviando Dados...");
            } else if (EnvioDadosServ.getInstance().getStatusEnvio() == 2) {
                textViewProcesso.setTextColor(Color.RED);
                textViewProcesso.setText("Existem Dados para serem Enviados");
            } else if (EnvioDadosServ.getInstance().getStatusEnvio() == 3) {
                textViewProcesso.setTextColor(Color.GREEN);
                textViewProcesso.setText("Todos os Dados já foram Enviados");
            }

            customHandler.postDelayed(this, 10000);

        }
    };

}