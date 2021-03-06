package br.com.usinasantafe.pbi.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import br.com.usinasantafe.pbi.PBIContext;
import br.com.usinasantafe.pbi.R;
import br.com.usinasantafe.pbi.model.bean.variaveis.ApontIndBean;
import br.com.usinasantafe.pbi.util.ConexaoWeb;
import br.com.usinasantafe.pbi.util.VerifDadosServ;

public class OSActivity extends ActivityGeneric {

    private ProgressDialog progressBar;
    private PBIContext pbiContext;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        pbiContext = (PBIContext) getApplication();

        Button buttonOkOS = (Button) findViewById(R.id.buttonOkPadrao);
        Button buttonCancOS = (Button) findViewById(R.id.buttonCancPadrao);

        buttonOkOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextPadrao.getText().toString().equals("")) {

                    boolean verOS;

                    if(pbiContext.getVerTela() == 3) {

                        pbiContext.getMecanicoCTR().setApontIndBean(new ApontIndBean());
                        pbiContext.getMecanicoCTR().getApontIndBean().setOsApont(Long.parseLong(editTextPadrao.getText().toString()));

                    }
                    else{

                        pbiContext.getReqProdutoCTR().getCabecReqProdBean().setNroOSCabecReqProd(Long.parseLong(editTextPadrao.getText().toString()));

                    }

                    if(pbiContext.getMecanicoCTR().verOSApont(Long.parseLong(editTextPadrao.getText().toString()))) {

                        Intent it = new Intent(OSActivity.this, ItemOSListaActivity.class);
                        startActivity(it);
                        finish();

                    }
                    else{

                        ConexaoWeb conexaoWeb = new ConexaoWeb();
                        if (conexaoWeb.verificaConexao(OSActivity.this)) {

                            progressBar = new ProgressDialog(v.getContext());
                            progressBar.setCancelable(true);
                            progressBar.setMessage("Pequisando a OS...");
                            progressBar.show();

                            customHandler.postDelayed(updateTimerThread, 10000);

                            pbiContext.getMecanicoCTR().verOS(editTextPadrao.getText().toString().trim()
                                    , OSActivity.this, ItemOSListaActivity.class, progressBar);

                        } else {

                            Intent it = new Intent(OSActivity.this, ItemOSDigActivity.class);
                            startActivity(it);
                            finish();

                        }

                    }
                }
            }
        });

        buttonCancOS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editTextPadrao.getText().toString().length() > 0) {
                    editTextPadrao.setText(editTextPadrao.getText().toString().substring(0, editTextPadrao.getText().toString().length() - 1));
                }
            }
        });

    }

    public void onBackPressed() {
        Intent it = new Intent(OSActivity.this, MenuFuncaoActivity.class);
        startActivity(it);
        finish();
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            if(!VerifDadosServ.getInstance().isVerTerm()) {

                VerifDadosServ.getInstance().cancelVer();
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }

                Intent it = new Intent(OSActivity.this, ItemOSDigActivity.class);
                startActivity(it);
                finish();

            }

        }
    };

}