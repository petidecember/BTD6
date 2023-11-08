package com.btd6;

public interface IConnectionFactory {
	
	IConnection createConnection(String url, String user, String pw);

}
