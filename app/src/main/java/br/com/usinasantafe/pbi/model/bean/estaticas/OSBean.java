package br.com.usinasantafe.pbi.model.bean.estaticas;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.usinasantafe.pbi.model.pst.Entidade;

@DatabaseTable(tableName="tbosest")
public class OSBean extends Entidade {

    private static final long serialVersionUID = 1L;

    @DatabaseField(id=true)
    private Long idOS;
    @DatabaseField
    private Long nroOS;
    @DatabaseField
    private Long equipOS;
    @DatabaseField
    private String descrEquipOS;

    public OSBean() {
    }

    public Long getIdOS() {
        return idOS;
    }

    public void setIdOS(Long idOS) {
        this.idOS = idOS;
    }

    public Long getNroOS() {
        return nroOS;
    }

    public void setNroOS(Long nroOS) {
        this.nroOS = nroOS;
    }

    public Long getEquipOS() {
        return equipOS;
    }

    public void setEquipOS(Long equipOS) {
        this.equipOS = equipOS;
    }

    public String getDescrEquipOS() {
        return descrEquipOS;
    }

    public void setDescrEquipOS(String descrEquipOS) {
        this.descrEquipOS = descrEquipOS;
    }

}
