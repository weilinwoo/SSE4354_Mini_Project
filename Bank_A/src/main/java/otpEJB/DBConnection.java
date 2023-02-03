package otpEJB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	public Connection createConnection() {
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm", "username", "password");
		return conn;
		}catch(Exception e) {
			System.out.println("Couldn't connect Database!");
		}
		
		return null;
	}
}
