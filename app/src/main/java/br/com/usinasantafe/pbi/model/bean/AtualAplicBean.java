package br.com.usinasantafe.pbi.model.bean;

/**
 * Created by anderson on 24/07/2017.
 */

public class AtualAplicBean {

    private Long nroAparelhoAtual;
    private String versaoAtual;
    private String versaoNova;

    public AtualAplicBean() {
    }

    public Long getNroAparelhoAtual() {
        return nroAparelhoAtual;
    }

    public void setNroAparelhoAtual(Long nroAparelhoAtual) {
        this.nroAparelhoAtual = nroAparelhoAtual;
    }

    public String getVersaoAtual() {
        return versaoAtual;
    }

    public void setVersaoAtual(String versaoAtual) {
        this.versaoAtual = versaoAtual;
    }

    public String getVersaoNova() {
        return versaoNova;
    }

    public void setVersaoNova(String versaoNova) {
        this.versaoNova = versaoNova;
    }
}
