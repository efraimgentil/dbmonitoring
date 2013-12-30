package com.efraimgentil.dbmonitoring.websocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class WSPool {
	
	private static WSPool instance;
	private Map<Integer, WSMonitor> pool;
	private AtomicInteger atomicInteger;
	
	private WSPool() {
		pool = new HashMap<Integer, WSMonitor>();
		atomicInteger = new AtomicInteger(0);
	}
	
	public static WSPool getInstance(){
		if(instance == null){
			instance = new WSPool();
		}
		return instance;
	}
	
	public void add(WSMonitor wsMonitor){
		wsMonitor.setId( atomicInteger.incrementAndGet() );
		pool.put( wsMonitor.getId() , wsMonitor );
	}
	
	public WSMonitor get(Integer id){
		return pool.get(id);
	}
	
	public void remove(Integer id){
		pool.remove(id);
	}
	
	public List<WSMonitor> getAll(){
		List<WSMonitor> monitors = new ArrayList<WSMonitor>();
		monitors.addAll(pool.values());
		return monitors;
	}
	
}