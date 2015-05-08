package com.research.phil.wififingerprinting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.research.phil.wififingerprinting.dao.WifiApDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class MainActivity extends ActionBarActivity {

	protected static long FREQUENCY = 2000;
	private WifiManager wifiManager = null;
	private List<WifiAP> wifiAPList = new ArrayList<WifiAP>();
    private IntentFilter intentfilter=new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    private WifiApDao wifiApDao;
	private EditText locationEditText;
	private TextView countTextView;
	private ToggleButton locationToggleButton1;
	private int count = 0;
	private int totalcount = 0;
	private String locationString;

    private String locationList[] = {
            "Bedroom",
            "Bathroom",
            "LivingRoom",
            "Kitchen",
            "DiningTable"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationEditText = (EditText) findViewById(R.id.locationEditText1);
        countTextView = (TextView)findViewById(R.id.count);
        wifiApDao = new WifiApDao(getBaseContext());
        locationToggleButton1 = (ToggleButton)findViewById(R.id.locationToggleButton1);
      
        locationEditText.setOnCreateContextMenuListener(new OnCreateContextMenuListener() 
        {
        	
        	public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenuInfo info)
        	{ //conMenu.setHeaderTitle("menu"); 
        		for (int i = 0; i < locationList.length; i++) {
        			conMenu.add(0, i, i, locationList[i]);
        		}
        	}
        });
      
        locationToggleButton1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							locationString = locationEditText.getText()
									.toString();
						} else {
							locationString = null;
							locationEditText.setText("");
							count=0;
						}
					}
				});
        
    }

	  @Override
	    protected void onResume() {
	    	super.onResume();  
	    	InitWifi();
	    }
	  
		void InitWifi()
		{
	        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			if (!wifiManager.isWifiEnabled()) {
	    		wifiManager.setWifiEnabled(true);    			
			}
			EnableWifi(); 
		}
	    
	    void EnableWifi() {
			registerReceiver(mReceiver, intentfilter);
			wifiManager.startScan(); 
	    }
	    
		
		
    private BroadcastReceiver mReceiver=new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    	if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

    		wifiAPList.clear();  
	    	for(final ScanResult result:wifiManager.getScanResults()) {	    		
	    		WifiAP wifi = new WifiAP(result.BSSID,result.SSID,result.level);
	    		wifiAPList.add(wifi);
	    	}
			/* sort listed APs first */
	    	Collections.sort(wifiAPList, new Comparator<WifiAP>() {
	            @Override
	            public int compare(WifiAP list1, WifiAP list2) {
	            	return (list1.level>list2.level ? -1 : (list1.level==list2.level ? 0 : 1));
	            }
	        });

	    	//=============================================
            Log.i("phil", "--------------------");
            /*
			for(WifiAP wifiAP :wifiAPList){
				Log.i("phil", wifiAP.BSSID+" -- "+ wifiAP.SSID+ " -- " + wifiAP.level + "dBm");
			}
			*/

			if (locationString != null && wifiAPList.size() >0) {
				Log.i("phil", locationString+"--------------------");
				addToDb(wifiAPList,locationString);
			}
	    	//=============================================
			try {
				Thread.sleep(FREQUENCY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ScanWifi();	
			Log.e("phil","----------------" + count);

    	}
    }

		private void addToDb(List<WifiAP> wifiAPList, String locationString) {
			wifiApDao.add(wifiAPList,locationString);
			count++;
			totalcount++;
			countTextView.setText(count+" / "+totalcount);
		}};
    
    void ScanWifi() {
  		wifiManager.startScan(); 
      }

    @Override
    protected void onPause() {
    	super.onPause(); 
//    	DeinitWifi();
    }
    
	void DeinitWifi()
	{
		DisableWifi();
	}
    void DisableWifi() {
		unregisterReceiver(mReceiver);
    }
    
    
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
		switch (item.getItemId()) {
		case R.id.bedroom:
			locationEditText.setText(locationList[0]);
			break;
		case R.id.bathroom:
			locationEditText.setText(locationList[1]);
			break;
		case R.id.livingroom:
			locationEditText.setText(locationList[2]);
			break;
		case R.id.kitchen:
			locationEditText.setText(locationList[3]);
			break;
		case R.id.dining:
			locationEditText.setText(locationList[4]);
			break;
		case R.id.action_settings:
			//
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }
    
	 public boolean onContextItemSelected(MenuItem aItem) 
	 { 
         int i =  aItem.getItemId();
	 switch (i) 
	 {
	 default : 
         locationEditText.setText(locationList[i]);
         break;
	 }
	 return true;
	 }
}
