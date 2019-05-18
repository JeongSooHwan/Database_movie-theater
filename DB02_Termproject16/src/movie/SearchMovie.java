package movie;

import java.sql.*;

public class SearchMovie {
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";

	PreparedStatement prestmt = null;
	Statement stmt = null;
	Connection conn = null;


}
