package movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ReservationManagement {
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
	Auditorium at = new Auditorium();
//	Seat seat = new Seat();
	AuditoriumManagement am = new AuditoriumManagement();

	public void search_movie() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;

		System.out.println("-------현재 상영중인 영화 목록-------");
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from movie" + " where showing=" + 1 + " order by reservation_count desc");
		int cnt = 1;
		while (rs.next()) {
			System.out.println("--------------" + cnt++ + "위 영화" + "--------------");
			System.out.println("제목: " + rs.getString("title"));
			System.out.println("감독: " + rs.getString("director"));
			System.out.println("출연: " + rs.getString("casts"));
			System.out.println("등급: " + rs.getInt("grade"));
			System.out.println("주요정보: " + rs.getString("details"));
			System.out.println("예매수: " + rs.getInt("reservation_count"));
		}

	}

//	public void reservation_movie() throws SQLException, ClassNotFoundException {
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		Class.forName(JDBC_DRIVER);
//		System.out.println("------------------------------------");
//		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//		stmt = conn.createStatement();
//		rs = stmt.executeQuery("select title from movie where showing = 1 order by reservation_count desc");
//		while (rs.next()) {
//			System.out.println("-" + rs.getString("title"));
//		}
//		System.out.println("------------------------------------");
//		System.out.print("예매할 영화이름을 입력해주세요\n>>");
//		String temp1 = sc.next();
//		String temp2 = sc.nextLine();
//		String inputMovieTitle = temp1 + temp2;
//
//		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//		stmt = conn.createStatement();
//		rs1 = stmt.executeQuery(
//				"select auditorium.theater_name from movie,auditorium where movie.id = auditorium.movie_id and movie.title='"
//						+ inputMovieTitle + "'");
//
//		while (rs1.next()) { // 입력받은 영화를 상영시켜주는 영화관 출력
//			System.out.println(rs1.getString(1));
//		}
//		// 여기서부터 보기
//		int AudiNumber = 0;
//		System.out.print("영화관 이름을 입력해주세요.\n>>");
//		String inputTheaterName = sc.next();
//		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//		stmt = conn.createStatement();
//		rs = stmt.executeQuery(
//				"select auditorium.number from auditorium,movie where movie.id = auditorium.movie_id and auditorium.theater_name='"
//						+ inputTheaterName + "' and movie.title='" + inputMovieTitle + "'");
//		if (rs.next()) {
//			AudiNumber = rs.getInt(1);
//			System.out.println(inputMovieTitle + "은(는) " + rs.getInt(1) + "관에서 상영합니다.");
//		}
//
//		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//		stmt = conn.createStatement();
//		rs = stmt.executeQuery("select auditorium.screen_start_time, auditorium.screen_end_time from auditorium,movie"
//				+ " where movie.id = auditorium.movie_id and movie.title = '" + inputMovieTitle
//				+ "' and auditorium.number = '" + AudiNumber + "' and" + " auditorium.theater_name= '"
//				+ inputTheaterName + "'");
//		System.out.println("----------" + AudiNumber + "관 시간표----------");
//		System.out.printf("%-8s", "시작시간");
//		System.out.printf("%-8s", "종료시간");
//		System.out.println();
//		int cnt = 0;
//		while (rs.next()) {
//			System.out.print(++cnt + ".");
//			System.out.printf("%-8s", rs.getString(1));
//			System.out.printf("%-8s", rs.getString(2));
//			System.out.println();
//		}
//		System.out.println("---------------------------");
//		System.out.print("예매하려는 영화 번호를 입력해주세요\n>>");
//		int inputN = sc.nextInt();
//
//		rs = stmt.executeQuery("select auditorium.screen_start_time, auditorium.screen_end_time from auditorium,movie"
//				+ " where movie.id = auditorium.movie_id and movie.title = '" + inputMovieTitle
//				+ "' and auditorium.number = '" + AudiNumber + "' and" + " auditorium.theater_name= '"
//				+ inputTheaterName + "'");
//	}
}
