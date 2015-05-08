package com.research.phil.wififingerprinting.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.research.phil.wififingerprinting.WifiAP;

import java.util.List;

public class WifiApDao {
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	private int[] level = new int[9];
	
	public WifiApDao(Context context) {
//		Log.i("DBphil", "SensorsPackageDao19");
		helper = new DBOpenHelper(context);
//		Log.i("DBphil", "SensorsPackageDao21");
	}

	public void add(List<WifiAP> wifiAPList, String location) {
//		Log.i("SensorsPackageDao","add begin--28_...");
		db = helper.getWritableDatabase();
		String tailingAp;
		String bSSIDString;
		
		for(WifiAP wifiAP :wifiAPList){
			bSSIDString = wifiAP.BSSID;
			tailingAp = bSSIDString.substring(bSSIDString.lastIndexOf(":")+1, bSSIDString.length());
//			Log.i("phil", tailingAp);
			if(tailingAp.equals("c2")){
				level[0] = wifiAP.level;
			}else if(tailingAp.equals("ac")){
				level[1] = wifiAP.level;
			}else if(tailingAp.equals("a4")){
				level[2] = wifiAP.level;
			}else if(tailingAp.equals("45")){
				level[3] = wifiAP.level;
			}else if(tailingAp.equals("80")){
				level[4] = wifiAP.level;
			}else if(tailingAp.equals("84")){
				level[5] = wifiAP.level;
			}else if(tailingAp.equals("40")){
				level[6] = wifiAP.level;
			}else if(tailingAp.equals("8c")){
				level[7] = wifiAP.level;
			}else if(tailingAp.equals("95")){
				level[8] = wifiAP.level;
            }
		}
		
		try {
			db.execSQL(
					"insert into wifirssi (" 
					+"ORION140,NETGEAR54,Cheese," 
				    +"HPPrint45, Judhajeet,NETGEAR17," 
				    +"DespicableLadies,Baking, unit204," 
			        +"location,time) "
							+ "values (?,?,?,?,?,?,?,?,?,?,datetime('now','localtime'))",
					new Object[] { level[0],level[1],level[2],level[3],level[4],
							level[5],level[6],level[7],level[8],
							location});
			Log.i("SensorsPackageDao","add done--59");
		} catch (SQLException esq) {
			Log.e(TagString.DEBUGPOINT, esq.toString());
		}
		db.close();
	}

}
