package com.research.phil.wififingerprinting;

public class WifiAP
{
  public String BSSID;
  public String SSID;
  public int level;
  
  public WifiAP(String paramString1, String paramString2, int paramInt)
  {
    this.BSSID = paramString1;
    this.SSID = paramString2;
    this.level = paramInt;
  }
}
