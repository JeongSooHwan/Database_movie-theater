package movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Administrator {
	Scanner sc = new Scanner(System.in);
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";

	PreparedStatement prestmt = null;
	Statement stmt = null;
	Connection conn = null;

	/* 관리자 로그인 */
	public void admin_login() throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		String id, pwd;
		System.out.print("ID: ");
		while (true) {
			id = sc.next();
			if (id.equals("admin"))
				break;
			else
				System.out.print("아이디가 틀렸습니다.\nID: ");

		}
		System.out.print("PW: ");
		while (true) {
			pwd = sc.next();
			if (pwd.equals("0000"))
				break;
			else {
				System.out.print("비밀번호가 틀렸습니다.\nID: ");
			}
		}
		movie_menu();
	}

	public void movie_menu() throws ClassNotFoundException, SQLException {
		TheaterManagement tm = new TheaterManagement();
		MovieManagement mm = new MovieManagement();
		AuditoriumManagement am = new AuditoriumManagement();
		// SeatManagement seatm = new SeatManagement();
		System.out.println("----------MENU----------");
		System.out.println("1.영화 등록  2.영화 정보 수정  3.영화 삭제  4.영화목록 출력");
		System.out.println("5.영화관 등록  6.영화관 정보 수정  7.영화관 삭제  8. 영화관 목록 출력");
		System.out.println("9.영화관에 상영관 등록  10.상영관에 영화 등록  11.상영관의 영화 삭제");
		System.out.println("12.VIP 목록 출력  13.티켓 출력  14.현장 결제");
		System.out.println("100.종료");

		int n = sc.nextInt();
		switch (n) {
		case 1:
			mm.register_movie();
			break;
		case 2:
			mm.update_movie();
			break;
		case 3:
			mm.remove_movie();
			break;
		case 4:
			mm.printMovieList();
			break;
		case 5:
			tm.register_theater();
			break;
		case 6:
			tm.update_theater();
			break;
		case 7:
			tm.remove_theater();
			break;
		case 8:
			tm.printTheaterList();
			break;
		case 9:
			am.setAuditoriumToTheater();
			break;
		case 10:
			am.register_movieToAuditorium();
			break;
		case 11:
			am.remove_movieFromAuditorium();
			break;
		case 12:
			printVipList();
			break;
		case 13:
			print_ticket();
			break;
		case 14:
			onsitePay();
			break;
		case 100:
			System.exit(0);
		}
	}

	public void printVipList() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		Date now = new Date();

		SimpleDateFormat formatType = new SimpleDateFormat("yyyy-MM");
		int MonthInt = Integer.parseInt(formatType.format(now).substring(5, formatType.format(now).length()));
		int YearInt = Integer.parseInt(formatType.format(now).substring(0, 4));
		int LastMonthInt;
		int LastYearInt;
		String LastMonth = "";

		if (MonthInt == 1) {
			LastYearInt = YearInt - 1;
			LastMonthInt = 12;
			LastMonth = String.valueOf(LastYearInt) + "-" + String.valueOf(LastMonthInt);
		} else if (2 <= MonthInt && MonthInt <= 10) {
			LastYearInt = YearInt;
			LastMonthInt = MonthInt - 1;
			LastMonth = String.valueOf(LastYearInt) + "-0" + String.valueOf(LastMonthInt);
		} else if (MonthInt == 11 || MonthInt == 12) {
			LastYearInt = YearInt;
			LastMonthInt = MonthInt - 1;
			LastMonth = String.valueOf(LastYearInt) + "-" + String.valueOf(LastMonthInt);
		}

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select member.id,member.name, sum(reservation.amount) from reservation,member "
				+ "where member.id = reservation.member_id and reservation.number LIKE '%" + LastMonth
				+ "%' and reservation.member_id= member.id "
				+ "group by member.id order by sum(reservation.amount) desc");
		for (int i = 1; i <= 10; i++) {
			if (rs.next()) {
				System.out.println(i + "위-" + rs.getString(1) + ": " + rs.getString(2));
			} else
				break;
		}
	}

	public void print_ticket() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		ResultSet movieTitleRs = null;

		System.out.print("티켓 출력할 고객의 아이디를 입력해주세요.\n>>");
		String inputId = sc.next();
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member, reservation where reservation.member_id = member.id and member.id='"
						+ inputId + "' and reservation.isPaid=1");
		if (rs.next()) {
			rs.previous();
			while (rs.next()) {
				System.out.println("---------------Ticket---------------");
				String parser[] = rs.getString(1).split("_");
				stmt = conn.createStatement();
				movieTitleRs = stmt.executeQuery("select title from movie where id='" + parser[2] + "'");
				movieTitleRs.next();
				System.out.println("영화제목: " + movieTitleRs.getString(1));
				System.out.println("영화관 : " + parser[0]);
				System.out.println("상영관: " + parser[1] + "관");
				System.out.println("상영 날짜: " + parser[3]);
				System.out.println("상영 시간: " + parser[4] + " ~ " + parser[5]);
				System.out.println("좌석: " + parser[6]);

			}
		} else {
			System.out.println("출력할 티켓이 없습니다.");
		}

	}

	public void onsitePay() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		ResultSet movieTitleRs = null;

		System.out.print("현장 결제를 할 아이디를 입력해주세요.\n>>");
		String inputId = sc.next();
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member, reservation where reservation.member_id = member.id and member.id='"
						+ inputId + "' and reservation.isPaid=0");
		if (!rs.next()) {
			System.out.println("결제할 내역이 없습니다.");
			movie_menu();
		}
		int cnt = 0;
		while (rs.next()) {
			System.out.println(++cnt + "." + rs.getString(1));
		}
		
		System.out.print("결제하실 번호를 선택해주세요.\n>>");
		int inputN = sc.nextInt();

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member, reservation where reservation.member_id = member.id and member.id='"
						+ inputId + "' and reservation.isPaid=0");
		for (int i = 0; i < inputN; i++) {
			rs.next();
		}

		String reservationN = rs.getString(1);

		System.out.print(reservationN + "에 대하여 결제하시겠습니까?(y/n)\n>>");
		String yn = sc.next();
		if (yn.equals("y")) {

			prestmt = conn.prepareStatement( // sql문 전달.
					"update reservation set isPaid=?" + " where number='" + reservationN + "'");
			prestmt.setInt(1, 1);
			int count1 = prestmt.executeUpdate();

			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"select reservation.number from member, reservation where reservation.member_id = member.id and member.id='"
							+ inputId + "' and reservation.isPaid=0");
			if (rs.next()) {
				rs.previous();
				while (rs.next()) {
					System.out.println("---------------Ticket---------------");
					String parser[] = rs.getString(1).split("_");
					stmt = conn.createStatement();
					movieTitleRs = stmt.executeQuery("select title from movie where id='" + parser[2] + "'");
					movieTitleRs.next();
					System.out.println("영화제목: " + movieTitleRs.getString(1));
					System.out.println("영화관 : " + parser[0]);
					System.out.println("상영관: " + parser[1] + "관");
					System.out.println("상영 날짜: " + parser[3]);
					System.out.println("상영 시간: " + parser[4] + " ~ " + parser[5]);
					System.out.println("좌석: " + parser[6]);

				}
			}
		} else {
			System.out.println("결제가 취소되었습니다.");
		}

	}

}
