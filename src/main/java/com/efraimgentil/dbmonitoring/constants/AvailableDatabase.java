package com.efraimgentil.dbmonitoring.constants;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public enum AvailableDatabase {
	
	POSTGRES(1 , "Postgres" , "" , false),
	MYSQL(2 , "Postgres" , "" , false),
	H2(3 , "Postgres" , "" , true);
	
	private Integer id;
	
	private String description;
    
	private String connectionUrl;
	
    private Boolean testOnly;

	private AvailableDatabase(Integer id, String description,
			String connectionUrl, Boolean testOnly) {
		this.id = id;
		this.description = description;
		this.connectionUrl = connectionUrl;
		this.testOnly = testOnly;
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

	public Boolean getTestOnly() {
		return testOnly;
	}
    
}
