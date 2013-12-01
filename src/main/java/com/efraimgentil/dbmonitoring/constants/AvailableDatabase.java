package com.efraimgentil.dbmonitoring.constants;


/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public enum AvailableDatabase {
	
	POSTGRES(1 , "Postgres" , "jdbc:postgresql://" , "org.postgresql.Driver"),
	MYSQL(2 , "MySQL" , ""  , "com.mysql.jdbc.Driver"),
	H2(3 , "H3" , "jdbc:h2:"  , "org.h2.Driver");
	
	private Integer id;
	
	private String description;
    
	private String connectionUrl;
	
	private String driver;
	
	private AvailableDatabase(Integer id, String description,
			String connectionUrl , String driver) {
		this.id = id;
		this.description = description;
		this.connectionUrl = connectionUrl;
		this.driver = driver;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

    
}
