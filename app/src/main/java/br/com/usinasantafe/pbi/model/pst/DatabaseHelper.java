package br.com.usinasantafe.pbi.model.pst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import br.com.usinasantafe.pbi.model.bean.estaticas.ColabBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.ComponenteBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.EscalaTrabBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.ItemOSBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.OSBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.ParadaBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.ParametroBean;
import br.com.usinasantafe.pbi.model.bean.estaticas.ServicoBean;
import br.com.usinasantafe.pbi.model.bean.variaveis.ApontBean;
import br.com.usinasantafe.pbi.model.bean.variaveis.BoletimBean;
import br.com.usinasantafe.pbi.model.bean.variaveis.ConfigBean;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String FORCA_DB_NAME = "pbi_db";
	public static final int FORCA_BD_VERSION = 1;

	private static DatabaseHelper instance;

	public static DatabaseHelper getInstance(){
		return instance;
	}
	
	public DatabaseHelper(Context context) {

		super(context, FORCA_DB_NAME, null, FORCA_BD_VERSION);;
		instance = this;
		
	}

	@Override
	public void close() {

		super.close();
		instance = null;
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs) {

		try{

			TableUtils.createTable(cs, ColabBean.class);
			TableUtils.createTable(cs, ComponenteBean.class);
			TableUtils.createTable(cs, EscalaTrabBean.class);
			TableUtils.createTable(cs, ItemOSBean.class);
			TableUtils.createTable(cs, OSBean.class);
			TableUtils.createTable(cs, ParadaBean.class);
			TableUtils.createTable(cs, ParametroBean.class);
			TableUtils.createTable(cs, ServicoBean.class);

			TableUtils.createTable(cs, ApontBean.class);
			TableUtils.createTable(cs, BoletimBean.class);
			TableUtils.createTable(cs, ConfigBean.class);

		}
		catch(Exception e){
			Log.e(DatabaseHelper.class.getName(),
					"Erro criando banco de dados...",
					e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,
			ConnectionSource cs,
			int oldVersion,
			int newVersion) {
		
		try {
			
			if(oldVersion == 1 && newVersion == 2){
//				TableUtils.createTable(cs, ConfiguracaoTO.class);
				oldVersion = 2;
			}
			
			
		} catch (Exception e) {
			Log.e(DatabaseHelper.class.getName(), "Erro atualizando banco de dados...", e);
		}
		
	}

}