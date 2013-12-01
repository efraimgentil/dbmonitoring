package com.efraimgentil.dbmonitoring.constants;


/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public enum AvailableDatabase {
	
	POSTGRES(1 , "Postgres" , "jdbc:postgresql://" ),
	MYSQL(2 , "MySQL" , "" ),
	H2(3 , "H3" , "jdbc:h2:" );
	
	private Integer id;
	
	private String description;
    
	private String connectionUrl;
	
	private AvailableDatabase(Integer id, String description,
			String connectionUrl) {
		this.id = id;
		this.description = description;
		this.connectionUrl = connectionUrl;
	}
	
	public static AvailableDatabase getAvailableDatabase(Integer id){
		for( AvailableDatabase availableDatabase : AvailableDatabase.values() ){
			if ( id.equals( availableDatabase.id ) ) 
			   return availableDatabase;
		}
		return null;
	}
	
	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

    
}
