package dev.hv.db.init;

public interface IConnectionFactory {
	
	IConnection createConnection(String url, String user, String pw);

}
