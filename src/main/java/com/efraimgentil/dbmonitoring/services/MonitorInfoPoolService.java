package com.efraimgentil.dbmonitoring.services;

import java.util.HashMap;

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
	
	/**
	 * 
	 * @param monitorInfo
	 * @return
	 * @throws WrongTokenFormatException 
	 * @throws NoMonitorTokenException 
	 */
	public MonitorInfo updateMappedMonitorInfo( MonitorInfo monitorInfo ) throws NoMonitorTokenException, WrongTokenFormatException{
		MonitorInfo mappedMonitor = monitors.get( monitorInfo.getToken() );
		mappedMonitor.setQuery( monitorInfo.getQuery() );
		mappedMonitor.setMonitorTitle( monitorInfo.getMonitorTitle() );
		mappedMonitor.setRefreshTime( monitorInfo.getRefreshTime() );
		String monitorToken = getStringUtils().md5( monitorInfo.getQuery() );
		monitorInfo.setMonitorToken(monitorToken);
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
