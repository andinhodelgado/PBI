package br.com.usinasantafe.pbi.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.usinasantafe.pbi.R;
import br.com.usinasantafe.pbi.control.MecanicoCTR;
import br.com.usinasantafe.pbi.model.bean.estaticas.ParadaBean;
import br.com.usinasantafe.pbi.model.bean.variaveis.ApontIndBean;
import br.com.usinasantafe.pbi.util.Tempo;

/**
 * Created by anderson on 08/03/2018.
 */

public class AdapterListHistorico extends BaseAdapter {

    private List itens;
    private LayoutInflater layoutInflater;

    public AdapterListHistorico(Context context, List itens) {
        this.itens = itens;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.activity_item_historico, null);
        TextView textViewHistApont = (TextView) view.findViewById(R.id.textViewHistApont);
        TextView textViewHistHrInicial = (TextView) view.findViewById(R.id.textViewHistHrInicial);
        TextView textViewHistHrFinal = (TextView) view.findViewById(R.id.textViewHistHrFinal);

        ApontIndBean apontIndBean = (ApontIndBean) itens.get(position);

        textViewHistHrInicial.setText("HORÁRIO INICIAL: " + Tempo.getInstance().manipDHComTZ(apontIndBean.getDthrInicialApont()));
    if(!apontIndBean.getDthrFinalApont().equals("")){
            textViewHistHrFinal.setText("HORÁRIO FINAL: " + Tempo.getInstance().manipDHComTZ(apontIndBean.getDthrFinalApont()));
        }
        else{
            textViewHistHrFinal.setText("HORÁRIO FINAL: ");
        }

        if(apontIndBean.getParadaApont() == 0) {
            textViewHistApont.setText("TRABALHANDO: OS " + apontIndBean.getOsApont() + " - ITEM " + apontIndBean.getItemOSApont());
            textViewHistApont.setTextColor(Color.BLUE);
        }
        else{

            MecanicoCTR mecanicoCTR = new MecanicoCTR();
            ParadaBean paradaBean = mecanicoCTR.getParadaId(apontIndBean.getParadaApont());
            textViewHistApont.setText("PARADA: " + paradaBean.getCodParada() + " - " + paradaBean.getDescrParada());
            textViewHistApont.setTextColor(Color.RED);
        }

        return view;
    }
}
