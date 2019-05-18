package movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AuditoriumManagement {
	Scanner sc = new Scanner(System.in);
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";
	static int colSize, rowSize;
	Connection conn = null;
	PreparedStatement prestmt = null;
	Statement stmt = null;

	Theater theater = new Theater();
	Administrator admin = new Administrator();
	Auditorium at = new Auditorium();

	public void setAuditoriumToTheater() throws ClassNotFoundException, SQLException { // 9번
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		System.out.println("영화관 목록입니다.");

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select name from theater order by name");
		while (rs.next()) { // theater 테이블에 있는 영화관 목록 출력
			System.out.println("-" + rs.getString("name"));
		}
		// conn.close();
		System.out.print("상영관을 등록하고 싶은 영화관 이름을 입력해주세요.\n>>");
		at.setTheater_name(sc.next());
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select name from theater" + " where name='" + at.getTheater_name() + "'"); // 있는
																											// 영화관인지
																											// 검색

		if (rs.next()) { // theater name exist

			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			rs2 = stmt.executeQuery( // '00관-영화이름' 출력
					"select distinct auditorium.number,movie.title from movie,auditorium where movie.id = auditorium.movie_id and auditorium.theater_name='"
							+ at.getTheater_name() + "' order by auditorium.number");

			if (rs2.next()) {
				rs2.previous();
				System.out.println(at.getTheater_name() + " 영화관에 현재 점유되어 있는 상영관 목록입니다.");
				while (rs2.next()) {
					System.out.println(rs2.getInt(1) + "관 - " + rs2.getString(2));
				}
			} else {
				System.out.println("선택하신 영화관에 점유된 상영관이 없습니다.");
			}
			rs2.close();
			// conn.close();
			while (true) {
				System.out.print("등록할 상영관 번호를 입력해주세요.\n>>");
				at.setNmuber(sc.nextInt());
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
				stmt = conn.createStatement();
				rs2 = stmt.executeQuery("select number from auditorium where theater_name='" + at.getTheater_name()
						+ "' and number = " + at.getNumber());

				if (rs2.next()) {
					System.out.println("이미 점유중인 상영관입니다.");
				} else {
					System.out.println("'" + at.getTheater_name() + "-" + at.getNumber() + "관' 의 사이즈를 설정해주세요.");
					do {
						System.out.print("가로사이즈를 입력해주세요.(5~10)\n>>");
						at.setCol_size(sc.nextInt());
					} while (!(5 <= at.getCol_size() && at.getCol_size() <= 10));
					do {
						System.out.print("세로사이즈를 입력해주세요.(5~10)\n>>");
						at.setRow_size(sc.nextInt());
					} while (!(5 <= at.getRow_size() && at.getRow_size() <= 10));

					// conn.close();

					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

					System.out.println("rowSize:" + at.getRow_size());
					System.out.println("colSize:" + at.getCol_size());

					prestmt = conn.prepareStatement( // auditorium에 영화관 이름,상영관,
														// 번호, 가로, 세로 크기 set
							"INSERT INTO auditorium(theater_name,number,col_size,row_size) VALUES (?,?,?,?)");

					prestmt.setString(1, at.getTheater_name());
					prestmt.setInt(2, at.getNumber());
					prestmt.setInt(3, at.getCol_size());
					prestmt.setInt(4, at.getRow_size());
					int count = prestmt.executeUpdate();
					System.out.println("'" + at.getTheater_name() + "-" + at.getNumber() + "관' 등록이 완료되었습니다.");
					break;
				}
			}
		} else {
			System.out.println("없는 영화관 입니다.");
			setAuditoriumToTheater();
		}
		admin.movie_menu();
	}

	/* 여기서부터 보기 !!!! */
	public void register_movieToAuditorium() throws ClassNotFoundException, SQLException { // 10번
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rowcolRs = null;

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select distinct theater_name from auditorium order by theater_name");
		if (rs.next()) {
			rs.previous();
			System.out.println("상영관에 영화 등록페이지입니다. 상영관이 존재하는 영화관 목록입니다.");
			while (rs.next()) {
				System.out.println("-" + rs.getString("theater_name"));
			}
		} else {
			System.out.println("열려있는 상영관이 없습니다.");
		}
		// conn.close();
		stmt.close();
		while (true) {
			System.out.print("영화를 등록할 영화관 이름을 입력해주세요.\n>>");
			at.setTheater_name(sc.next());
			stmt = conn.createStatement();
			rs = stmt.executeQuery( // 상영관이 있는 영화관인지 검색
					"select distinct theater_name from auditorium" + " where theater_name='" + at.getTheater_name()
							+ "'");
			if (rs.next()) { // theater_name exist
				break;
			} else
				System.out.println("영화관을 잘못 입력하셨습니다.");
		}
		stmt.close();
		stmt = conn.createStatement();
		rs1 = stmt.executeQuery("select distinct number from auditorium where theater_name= '" + at.getTheater_name()
				+ "' order by number");
		while (rs1.next()) {
			System.out.println("-" + rs1.getString(1) + "관");
		}

		while (true) {
			boolean flag = false;
			System.out.print("영화를 등록할 상영관 번호를 입력해주세요.\n>>");
			at.setNmuber(sc.nextInt());

			stmt = conn.createStatement();
			rs1 = stmt.executeQuery("select number from auditorium where theater_name='" + at.getTheater_name() + "'");
			while (rs1.next()) {
				if (rs1.getInt(1) == at.getNumber()) {
					flag = true;
					break;
				}
			}
			if (flag == true) {
				break;
			} else {
				System.out.println("없는 상영관입니다.");
			}
		}

		stmt = conn.createStatement();
		rs1 = stmt.executeQuery("select distinct col_size,row_size from auditorium where theater_name='"
				+ at.getTheater_name() + "' and number='" + at.getNumber() + "'");
		rs1.next();
		colSize = rs1.getInt(1);
		rowSize = rs1.getInt(2);
		stmt.close();

		stmt = conn.createStatement();
		rs1 = stmt.executeQuery("select distinct auditorium.number,auditorium.movie_id from auditorium,movie "
				+ "where auditorium.theater_name='" + at.getTheater_name()
				+ "' and auditorium.movie_id is not null and auditorium.number='" + at.getNumber()
				+ "' order by auditorium.number");

		if (rs1.next()) { // 기존에 있는 영화에 날짜,시간만 추가

			String movie_id = rs1.getString(2);
			System.out.println("상영 일자를 입력해주세요.");
			String inputYear = "", inputMonth = "", inputDay = "";
			do {
				System.out.print("년도:");
				inputYear = sc.next();
			} while (2018 > Integer.parseInt(inputYear));
			do {
				System.out.print("월:");
				inputMonth = sc.next();
			} while (!(1 <= Integer.parseInt(inputMonth) && Integer.parseInt(inputMonth) <= 12));
			if (inputMonth.length() == 1)
				inputMonth = "0" + inputMonth;
			do {
				System.out.print("일:");
				inputDay = sc.next();
			} while (!(1 <= Integer.parseInt(inputDay) && Integer.parseInt(inputDay) <= 31));
			if (inputDay.length() == 1)
				inputDay = "0" + inputDay;
			at.setScreen_date(inputYear + "-" + inputMonth + "-" + inputDay);
			System.out.println(at.getScreen_date());
			System.out.print("시작 시각을 입력해주세요.(ex:0920)\n>>");
			at.setStart_time(sc.next());
			System.out.print("종료 시각을 입력해주세요.(ex:1345)\n>>");
			at.setEnd_time(sc.next());
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) cnt from auditorium where screen_date='" + at.getScreen_date() + "'"
					+ " and number='" + at.getNumber() + "'" + " and screen_start_time <='" + at.getEnd_time() + "'"
					+ " and screen_end_time >='" + at.getStart_time() + "'");
			int cnt = 0;
			if (rs.next()) {
				cnt = rs.getInt("cnt");
				rs.previous();
			}
			if (cnt != 0) {
				System.out.println("영화시간이 중복됩니다.");
				register_movieToAuditorium();
			} else {
				prestmt = conn.prepareStatement(
						"INSERT INTO auditorium(theater_name,number,screen_date,screen_start_time,screen_end_time,movie_id,col_size,row_size) VALUES (?,?,?,?,?,?,?,?)");
				prestmt.setString(1, at.getTheater_name());
				prestmt.setInt(2, at.getNumber());
				prestmt.setString(3, at.getScreen_date());
				prestmt.setString(4, at.getStart_time());
				prestmt.setString(5, at.getEnd_time());
				prestmt.setString(6, movie_id);
				prestmt.setInt(7, colSize);
				prestmt.setInt(8, rowSize);
				int count = prestmt.executeUpdate();
				System.out.println("등록이 완료되었습니다.");

				for (int i = 0; i < rowSize; i++) { // A~
					for (int j = 1; j <= colSize; j++) { // 1~
						prestmt = conn.prepareStatement(
								"INSERT INTO seat (theater_name,auditorium_number,row,col,screen_date,screen_start_time,isSelected) VALUES(?,?,?,?,?,?,?)");
						prestmt.setString(1, at.getTheater_name());
						prestmt.setInt(2, at.getNumber());
						prestmt.setString(3, String.valueOf((char) (i + 65)));
						prestmt.setString(4, String.valueOf(j));
						prestmt.setString(5, at.getScreen_date());
						prestmt.setString(6, at.getStart_time());
						prestmt.setInt(7, 0);
						int count1 = prestmt.executeUpdate();
					}
				}

				admin.movie_menu();
			}
		}
		stmt = conn.createStatement();
		rs2 = stmt.executeQuery("select auditorium.number from auditorium,movie " + "where auditorium.theater_name='"
				+ at.getTheater_name() + "' and auditorium.movie_id is null and auditorium.number='" + at.getNumber()
				+ "' order by auditorium.number");

		if (rs2.next()) { // 새로운 영화 추가
			System.out.println("---------------------------------");
			System.out.println("추가할 수 있는 영화 목록입니다.(현재 상영 중)");
			stmt = conn.createStatement();
			rs1 = stmt.executeQuery("select id, title from movie where showing = 1");
			System.out.printf("%-15s", "ID");
			System.out.printf("%-15s", "TITLE");
			System.out.println();
			while (rs1.next()) {
				System.out.printf("%-15s", rs1.getString("id"));
				System.out.printf("%-15s", rs1.getString("title"));
				System.out.println();
			}
			System.out.println("---------------------------------");
			while (true) {
				System.out.print("추가할 영화 아이디를 입력해주세요.\n>>");
				at.setMovie_id(sc.next());
				stmt = conn.createStatement();
				rs1 = stmt.executeQuery("select movie_id, theater_name from auditorium" + " where movie_id='"
						+ at.getMovie_id() + "'" + " and theater_name= '" + at.getTheater_name() + "'");
				stmt = conn.createStatement();
				rs2 = stmt.executeQuery("select * from movie where id='" + at.getMovie_id() + "' and showing = 0");

				stmt = conn.createStatement();
				rs3 = stmt.executeQuery("select * from movie where id='" + at.getMovie_id() + "'");
				if (rs1.next()) {
					System.out.print("이미 해당 영화관에 상영중인 영화입니다.\n 다른 ");
				} else if (rs2.next()) {
					System.out.println("상영중이지 않는 영화 아이디입니다.");
				} else if (!rs3.next()) {
					System.out.println("없는 영화 아이디 입니다.");
				} else {
					break;
				}
			}
			System.out.println("상영 일자를 입력해주세요.");
			String inputYear = "", inputMonth = "", inputDay = "";
			do {
				System.out.print("년도:");
				inputYear = sc.next();
			} while (2018 > Integer.parseInt(inputYear));
			do {
				System.out.print("월:");
				inputMonth = sc.next();
			} while (!(1 <= Integer.parseInt(inputMonth) && Integer.parseInt(inputMonth) <= 12));
			if (inputMonth.length() == 1)
				inputMonth = "0" + inputMonth;
			do {
				System.out.print("일:");
				inputDay = sc.next();
			} while (!(1 <= Integer.parseInt(inputDay) && Integer.parseInt(inputDay) <= 31));
			if (inputDay.length() == 1)
				inputDay = "0" + inputDay;
			at.setScreen_date(inputYear + "-" + inputMonth + "-" + inputDay);
			System.out.println(at.getScreen_date());
			System.out.print("시작 시각을 입력해주세요.(ex:0920)\n>>");
			at.setStart_time(sc.next());
			System.out.print("종료 시각을 입력해주세요.(ex:1345)\n>>");
			at.setEnd_time(sc.next());
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) cnt from auditorium where screen_date='" + at.getScreen_date() + "'"
					+ " and number='" + at.getNumber() + "'" + " and screen_start_time <='" + at.getEnd_time() + "'"
					+ " and screen_end_time >='" + at.getStart_time() + "'");
			int cnt = 0;
			if (rs.next()) {
				cnt = rs.getInt("cnt");
				rs.previous();
			}
			if (cnt != 0) {
				System.out.println("영화시간이 중복됩니다.");
				register_movieToAuditorium();
			} else {
				prestmt = conn.prepareStatement(
						"INSERT INTO auditorium(theater_name,number,screen_date,screen_start_time,screen_end_time,movie_id,col_size,row_size) VALUES (?,?,?,?,?,?,?,?)");
				prestmt.setString(1, at.getTheater_name());
				prestmt.setInt(2, at.getNumber());
				prestmt.setString(3, at.getScreen_date());
				prestmt.setString(4, at.getStart_time());
				prestmt.setString(5, at.getEnd_time());
				prestmt.setString(6, at.getMovie_id());
				prestmt.setInt(7, colSize);
				prestmt.setInt(8, rowSize);
				int count = prestmt.executeUpdate();
				prestmt.close();

				for (int i = 0; i < rowSize; i++) { // A~
					for (int j = 1; j <= colSize; j++) { // 1~
						prestmt = conn.prepareStatement(
								"INSERT INTO seat (theater_name,auditorium_number,row,col,screen_date,screen_start_time,isSelected) VALUES(?,?,?,?,?,?,?)");
						prestmt.setString(1, at.getTheater_name());
						prestmt.setInt(2, at.getNumber());
						prestmt.setString(3, String.valueOf((char) (i + 65)));
						prestmt.setString(4, String.valueOf(j));
						prestmt.setString(5, at.getScreen_date());
						prestmt.setString(6, at.getStart_time());
						prestmt.setInt(7, 0);
						int count1 = prestmt.executeUpdate();
					}
				}
				System.out.println("등록이 완료되었습니다.");
			}
			admin.movie_menu();

		}
	}

	public void remove_movieFromAuditorium() throws ClassNotFoundException, SQLException {
		ResultSet rs = null;
		ResultSet rs1 = null;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select DISTINCT name from theater order by name");
		System.out.println("영화관 목록입니다.");
		while (rs.next()) {
			System.out.println("-" + rs.getString("name"));
		}

		System.out.print("영화관 이름을 입력해주세요.\n>>");
		String inputTheaterName = sc.next();

		// conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from theater where name ='" + inputTheaterName + "'");
		
		if (rs.next()) {
			// conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			rs1 = stmt.executeQuery("select distinct movie.title from movie,auditorium where (auditorium.theater_name='"
					+ inputTheaterName + "'" + " and auditorium.movie_id = movie.id)" + " order by movie.title");
			if (rs1.next()) {
				System.out.println(inputTheaterName + "에서 상영하는 영화입니다.");
				rs1.previous();
				while (rs1.next()) {
					System.out.println("-" + rs1.getString("title"));
				}
				System.out.print("삭제하고 싶은 영화 이름을 입력해주세요.\n>>");
				String temp1 = sc.next();
				String temp2 = sc.nextLine();
				String inputTitle = temp1 + temp2;

				System.out.println("영화관 <" + inputTheaterName + ">에서 '" + inputTitle + "'을 삭제하시겠습니까? (y/n)");
				String yn = sc.next();
				if (yn.equals("y")) {
					// conn = DriverManager.getConnection(DB_URL, USERNAME,
					// PASSWORD);
					stmt = conn.createStatement();
					int count = stmt.executeUpdate(
							"delete auditorium from movie,auditorium where(movie.id = auditorium.movie_id and movie.title='"
									+ inputTitle + "') and auditorium.theater_name='" + inputTheaterName + "'");
					System.out.println("삭제가 완료되었습니다.");
					admin.movie_menu();
				} else {
					System.out.println("삭제가 취소되었습니다.");
					admin.movie_menu();
				}
			} else {
				System.out.println("상영하는 영화가 없습니다.");
			}
		} else {
			System.out.println("목록에 없는 영화관입니다.");
			admin.movie_menu();
		}
	}
}
