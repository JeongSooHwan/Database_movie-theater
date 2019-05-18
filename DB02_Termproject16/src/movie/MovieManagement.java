package movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MovieManagement {
	Scanner sc = new Scanner(System.in);
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";

	PreparedStatement prestmt = null;
	Statement stmt = null;
	Connection conn = null;
	Administrator admin = new Administrator();
	Movie movie = new Movie();

	public void register_movie() throws SQLException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);

		ResultSet rs = null;

		System.out.print("ID: ");
		while (true) {
			movie.setId(sc.next());
			boolean find = false;
			boolean flag = false;
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select id from movie");

			while (rs.next()) {
				if (rs.getString("id").equals(movie.getId())) {
					find = true;
					System.out.print("이미 있는 아이디 입니다. 다시 입력해주세요.\nID: ");
					break;
				} else {
					flag = true;
				}
			}
			if (flag == true && find == false) {
				break;
			}
		}
		System.out.print("제목: ");
		movie.setTitle(sc.next());
		System.out.print("감독: ");
		movie.setDirector(sc.next());
		System.out.print("출연: ");
		movie.setCasts(sc.next());
		System.out.print("등급: ");
		movie.setGrade(sc.nextInt());
		System.out.print("주요정보: ");
		movie.setDetails(sc.next());
		movie.setReservation_count(0);

		Class.forName(JDBC_DRIVER);

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

		prestmt = conn.prepareStatement(
				"INSERT INTO movie(id,title,director,casts,grade,details,reservation_count) VALUES (?,?,?,?,?,?,?)");

		prestmt.setString(1, movie.getId());
		prestmt.setString(2, movie.getTitle());
		prestmt.setString(3, movie.getDirector());
		prestmt.setString(4, movie.getCasts());
		prestmt.setInt(5, movie.getGrade());
		prestmt.setString(6, movie.getDetails());
		prestmt.setInt(7, movie.getReservation_count());
		int count = prestmt.executeUpdate();
		System.out.println("영화 등록이 완료되었습니다.");
		admin.movie_menu();
	}

	public void update_movie() throws ClassNotFoundException, SQLException {
		ResultSet rs = null;
		boolean exitSwitch = false;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select id,title from movie");
		int cnt = 1;
		System.out.println("------영화 목록(id + title)------");

		System.out.printf("%-15s", "  ID");
		System.out.printf("%-15s", "title");
		System.out.println();
		while (rs.next()) {
			System.out.printf("%-15s", ++cnt + "." + rs.getString(1));
			System.out.printf("%-15s", rs.getString(2));
			System.out.println();
		}

		System.out.print("변경할 영화 아이디를 입력하세요.(ex:1111)\n>>");
		String s = sc.next();

		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from movie");
		while (rs.next()) {
			if (s.equals(rs.getString("id"))) {

				String newTitle = rs.getString("title");
				String newDir = rs.getString("director");
				String newCasts = rs.getString("casts");
				String newDetails = rs.getString("details");
				int newGrade = rs.getInt("grade");

				while (true) {
					System.out.println("-----변경할 내용을 선택해주세요-----");
					System.out.println("1.제목: " + newTitle);
					System.out.println("2.감독: " + newDir);
					System.out.println("3.출연: " + newCasts);
					System.out.println("4.등급: " + newGrade);
					System.out.println("5.주요정보: " + newDetails);
					System.out.println("6.변경사항 저장");

					int n = sc.nextInt();
					switch (n) {
					case 1:
						System.out.println("수정하고자 하는 영화 제목을 입력해주세요.");
						newTitle = sc.next();
						break;
					case 2:
						System.out.println("수정하고자 하는 영화 감독을 입력해주세요.");
						newDir = sc.next();
						break;
					case 3:
						System.out.println("수정하고자 하는 영화 출연자를 입력해주세요.");
						newCasts = sc.next();
						break;
					case 4:
						System.out.println("수정하고자 하는 등급을 입력해주세요.");
						newGrade = sc.nextInt();
						break;

					case 5:
						System.out.println("수정하고자 하는 주요정보를 입력해주세요.");
						newDetails = sc.next();
						break;
					case 6:
						System.out.println("변경사항을 저장하시겠습니까?(y/n)");
						String yn = sc.next();
						if (yn.equals("y")) {
							Class.forName(JDBC_DRIVER); // �ε�

							conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
							prestmt = conn.prepareStatement( // sql�� ����.
									"update movie set" + " title=?,director=?,casts=?,grade=?,details=?"
											+ " where id=?");
							prestmt.setString(1, newTitle);
							prestmt.setString(2, newDir);
							prestmt.setString(3, newCasts);
							prestmt.setInt(4, newGrade);
							prestmt.setString(5, newDetails);
							prestmt.setString(6, s);
							int count = prestmt.executeUpdate();

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

	public void remove_movie() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;

		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select id,title from movie");
		int cnt = 1;
		System.out.println("------영화 목록(id + title)------");

		System.out.printf("%-15s", "  ID");
		System.out.printf("%-15s", "title");
		System.out.println();
		while (rs.next()) {
			System.out.printf("%-15s", ++cnt + "." + rs.getString(1));
			System.out.printf("%-15s", rs.getString(2));
			System.out.println();
		}
		System.out.println("삭제하려는 영화 아이디를 입력하세요.");
		String s = sc.next();
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from movie");
		while (rs.next()) {
			if (s.equals(rs.getString("id"))) {
				System.out.println(
						"id: " + rs.getString("id") + ", title: " + rs.getString("title") + "을(를) 삭제하시겠습니까?(y/n)");
				String yn = sc.next();
				if (yn.equals("y")) {
					Class.forName(JDBC_DRIVER);

					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					stmt = conn.createStatement();
					int count = stmt.executeUpdate("delete from movie" + " where id='" + s + "'");
					System.out.println("삭제되었습니다..");
				}
			}
		}
		admin.movie_menu();
	}

	public void printMovieList() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;

		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select id,title from movie");
		int cnt = 0;
		System.out.printf("%-15s", "  ID");
		System.out.printf("%-15s", "title");
		System.out.println();
		while (rs.next()) {
			System.out.printf("%-15s", ++cnt + "." + rs.getString(1));
			System.out.printf("%-15s", rs.getString(2));
			System.out.println();
		}
		admin.movie_menu();
	}
	
}