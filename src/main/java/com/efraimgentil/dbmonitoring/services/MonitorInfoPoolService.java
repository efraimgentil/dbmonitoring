package com.efraimgentil.dbmonitoring.services;

import java.util.HashMap;
import java.util.Map;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.utils.StringUtils;

/**
 * 
 * @author Efraim Gentil
 * @email efraim.gentil@gmail.com
 * @date Dec 14, 2013
 */
public class MonitorInfoPoolService {
	
	private static MonitorInfoPoolService monitorInfoPoolService;
	
	private HashMap<String, MonitorInfo> monitors = new HashMap<>();
	
	private StringUtils stringUtils;
	
	private MonitorInfoPoolService() { }
	
	public static MonitorInfoPoolService getInstance(){
		return monitorInfoPoolService == null ?
				monitorInfoPoolService = new MonitorInfoPoolService() : monitorInfoPoolService;
	}
	
	public void addMonitor( MonitorInfo monitorInfo ){
		monitors.put( monitorInfo.getToken()  , monitorInfo );
	}
	
	public MonitorInfo getMonitor( String token ){
		return  monitors.get( token );
	}
	
	public MonitorInfo updateMonitorInfoAndGet(String token , Map<String, Object> jsonMap ) throws NoMonitorTokenException, WrongTokenFormatException{
		MonitorInfo mappedMonitor = monitors.get( token );
		mappedMonitor.setQuery( (String) jsonMap.get("query") );
		mappedMonitor.setMonitorTitle( (String) jsonMap.get("monitorTitle") );
		String stringRefreshTime = (String) jsonMap.get("refreshTime");
		if(stringRefreshTime  != null && !stringRefreshTime.isEmpty())
			mappedMonitor.setRefreshTime( Integer.parseInt( stringRefreshTime ) );
		return mappedMonitor;
	}

	public StringUtils getStringUtils() {
		if(stringUtils == null){
			stringUtils = new StringUtils();
		}
		return stringUtils;
	}

	public void setStringUtils(StringUtils stringUtils) {
		this.stringUtils = stringUtils;
	}
	
	
}
