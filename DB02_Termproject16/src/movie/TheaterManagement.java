package movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TheaterManagement {
	Scanner sc = new Scanner(System.in);
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";

	PreparedStatement prestmt = null;
	Statement stmt = null;
	Connection conn = null;

	Theater theater = new Theater();
	Administrator admin = new Administrator();

	/* 영화관 등록 */
	public void register_theater() throws ClassNotFoundException, SQLException {
		boolean find = false;
		boolean flag = false;

		ResultSet rs = null;
		System.out.println("----영화관 등록입니다----");
		System.out.println("1.신규 영화관 생성");
		int n = sc.nextInt();

		if (n == 1) {
			while (true) {
				System.out.print("영화관 이름: ");
				theater.setName(sc.next());
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select * from theater" + " where name='" + theater.getName() + "'");
				if (rs.next()) {
					System.out.println("기존 등록되어 있는 영화관입니다.");
				} else
					break;
			}
			System.out.print("주소: ");
			theater.setAddress(sc.next());
			System.out.print("전화번호 : ");
			theater.setTel(sc.next());
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

			prestmt = conn.prepareStatement("INSERT INTO theater(name,address,tel) VALUES (?,?,?)");

			prestmt.setString(1, theater.getName());
			prestmt.setString(2, theater.getAddress());
			prestmt.setString(3, theater.getTel());
			int count = prestmt.executeUpdate();
			System.out.println("영화관 등록이 완료되었습니다.");
		}
	}

	/* 영화관 정보 수정 */
	public void update_theater() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
		boolean exitSwitch = false;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select distinct name from theater order by name");
		int cnt = 1;
		System.out.println("--------영화관 목록(name)--------");
		while (rs.next()) {
			System.out.println(cnt++ + ". " + rs.getString("name"));
		}

		System.out.println("변경할 영화관 이름을 입력하세요.(ex:cinema1)");
		String s = sc.next();

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select name,address,tel from theater");
		while (rs.next()) {
			if (s.equals(rs.getString("name"))) {

				String newName = rs.getString("name");
				String newAddress = rs.getString("address");
				String newTel = rs.getString("tel");

				while (true) {
					System.out.println("-----변경할 내용을 선택해주세요-----");
					System.out.println("1.영화관 이름: " + newName);
					System.out.println("2.영화관 주소: " + newAddress);
					System.out.println("3.영화관 전화번호: " + newTel);
					System.out.println("4.변경사항 저장");

					int n = sc.nextInt();

					switch (n) {
					case 1:
						while (true) {
							System.out.println("수정하고자 하는 영화관 이름을 입력해주세요.");
							theater.setName(sc.next());
							newName = theater.getName();
							Class.forName(JDBC_DRIVER);
							conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
							stmt = conn.createStatement();
							rs = stmt.executeQuery(
									"select distinct name from theater" + " where name ='" + newName + "'");
							if (rs.next()) {
								System.out.println("중복되는 영화관 이름이 있습니다.");
							} else
								break;
						}
						break;
					case 2:
						System.out.println("수정하고자 하는 영화관 주소를 입력해주세요.");
						newAddress = sc.next();
						break;
					case 3:
						System.out.println("수정하고자 하는 영화관 전화번호를 입력해주세요.");
						newTel = sc.next();
						break;
					case 4:
						System.out.println("변경사항을 저장하시겠습니까?(y/n)");
						String yn = sc.next();
						if (yn.equals("y")) {
							Class.forName(JDBC_DRIVER); // 로딩

							conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
							prestmt = conn.prepareStatement( // sql문 전달
									"update theater set" + " name=?,address=?,tel=?" + " where name=?");
							prestmt.setString(1, newName);
							prestmt.setString(2, newAddress);
							prestmt.setString(3, newTel);
							prestmt.setString(4, s);
							int count = prestmt.executeUpdate();
							System.out.println("name: " + theater.getName());
							System.out.println("영화정보 수정이 완료되었습니다.");
							exitSwitch = true;
						} else {
							System.out.println("영화정보 수정이 취소되었습니다.");
							exitSwitch = true;
						}
						break;
					}
					if (exitSwitch == true)
						break;
				}
			}
		}

		admin.movie_menu();
	}

	/* 영화관 삭제 */
	public void remove_theater() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;

		System.out.println("----영화관 목록-----.");

		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select distinct name from theater order by name");
		int cnt = 1;
		while (rs.next()) {
			System.out.println(cnt++ + ". " + rs.getString("name"));
		}
		System.out.println("삭제하려는 영화관 이름을 입력하세요.");
		String s = sc.next();
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from theater");
		while (rs.next()) {
			if (s.equals(rs.getString("name"))) {
				System.out.println("name: " + rs.getString("name") + "을(를) 삭제하시겠습니까?(y/n)");
				String yn = sc.next();
				if (yn.equals("y")) {
					Class.forName(JDBC_DRIVER);
					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					stmt = conn.createStatement();
					int count = stmt.executeUpdate("delete from theater" + " where name='" + s + "'");
					System.out.println("영화관이 삭제 되었습니다.");
					break;
				}
			}
		}
		admin.movie_menu();
	}

	public void printTheaterList() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from theater");
		System.out.printf("%-30s", "  영화관 이름");
		System.out.printf("%-30s", "주소");
		System.out.printf("%-30s", "전화번호");
		System.out.println();
		while (rs.next()) {
			System.out.printf("%-30s", "-" + rs.getString(1));
			System.out.printf("%-30s", rs.getString(2));
			System.out.printf("%-30s", rs.getString(3));
			System.out.println();
		}
		admin.movie_menu();
	}
}
