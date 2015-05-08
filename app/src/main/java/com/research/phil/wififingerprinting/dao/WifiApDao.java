package com.research.phil.wififingerprinting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.research.phil.wififingerprinting.WifiAP;

import java.util.List;

public class WifiApDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    //private int[] level = new int[9];

    public WifiApDao(Context context) {
//		Log.i("DBphil", "SensorsPackageDao19");
        helper = new DBOpenHelper(context);
//		Log.i("DBphil", "SensorsPackageDao21");
    }

    public void add(List<WifiAP> wifiAPList, String location) {
        add2(wifiAPList,  location);
        add3(wifiAPList,  location);
    }


    private void add3(List<WifiAP> wifiAPList, String location) {

        String bSSIDString;
        String sSIDString;

        boolean exist = false;

        StringBuffer allBSsids = new StringBuffer();

        for (WifiAP wifiAP : wifiAPList) {
            bSSIDString = wifiAP.BSSID;
            sSIDString = wifiAP.SSID;

            if (emptyByBssid(bSSIDString)) {
                Log.i("WifiApDao", "add3 done--45" +emptyByBssid(bSSIDString));
                try {
                    db = helper.getWritableDatabase();
                    db.execSQL(
                            "insert into wifissid ("
                                    + "wifiap,"
                                    + "ssid,location,time) "
                                    + "values (?,?,?,datetime('now','localtime'))",
                            new Object[]{bSSIDString,
                                    sSIDString, location});
                    Log.i("WifiApDao", "add3 done--53");
                } catch (SQLException esq) {
                    Log.e(TagString.DEBUGPOINT, esq.toString());
                }
                db.close();

            }
        }
    }

    private void add2(List<WifiAP> wifiAPList, String location) {
//		Log.i("SensorsPackageDao","add begin--28_...");
        db = helper.getWritableDatabase();
        String bSSIDString;
        String sSIDString;
        String temp;
        String levelString;

        StringBuffer allBSsids = new StringBuffer();

        for (WifiAP wifiAP : wifiAPList) {
            bSSIDString = wifiAP.BSSID;
            temp=bSSIDString.replace(":","");
            temp = temp.substring(temp.length()-5,temp.length());
            Log.e("substring", temp);
            sSIDString = wifiAP.SSID;
            levelString = String.valueOf(wifiAP.level);

            allBSsids.append(temp);
//            allBSsids.append(",");
//            allBSsids.append(sSIDString);
            allBSsids.append(";");
            allBSsids.append(levelString);
            allBSsids.append(" & ");
        }
            try {
                db.execSQL(
                        "insert into wifiaps ("
                                + "wifiaplist,"
                                + "location,time) "
                                + "values (?,?,datetime('now','localtime'))",
                        new Object[]{allBSsids,
                                location});
                Log.i("SensorsPackageDao", "add done--59");
            } catch (SQLException esq) {
                Log.e(TagString.DEBUGPOINT, esq.toString());
            }
            db.close();

    }
    /*
    public void add1(List<WifiAP> wifiAPList, String location) {
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
    }*/


    public boolean emptyByBssid(String bSSIDString) {
        db = helper.getWritableDatabase();
        Cursor cursor =
                db.rawQuery("select * from wifissid where wifiap = ?", new
                        String[]{bSSIDString});
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }


}
