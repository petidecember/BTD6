package com.btd6;

public interface IConnection {
	
	IConnection openConnection(String url, String user, String password);
	
	IFacilityService getFacilityService();
	
	ICustomerDAO getCustomerDAO();
	
	IReadingDAO getReadingDAO();
	
	void createAllTables();
	
	void removeAllTables();
	
	void truncateAllTables();
	
	void closeConnection();
	

}
