package movie;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.swing.text.SimpleAttributeSet;

public class MemberManagement {
	Scanner sc = new Scanner(System.in);
	final String USERNAME = "root";
	final String PASSWORD = "201402430";
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/movie?useUnicode=true&characterEncoding=utf8";

	String reservationArr[] = new String[7];
	String seatArr[][];
	PreparedStatement prestmt = null;
	Statement stmt = null;
	Connection conn = null;

	User user = new User();
	// Seat seat = new Seat();

	public void member_menu() throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		ReservationManagement rm = new ReservationManagement();
		System.out.println("----------MENU----------");
		System.out.println("1.회원 정보 조회  2.회원 정보 수정  3.회원 탈퇴");
		System.out.println("4.상영중인 영화 검색  5.영화 예매  6.예매 확인  7.예매 취소");
		System.out.println("8.결제  9.종료");

		int n = sc.nextInt();
		switch (n) {
		case 1:
			print_profile();
			break;
		case 2:
			update_profile();
			break;
		case 3:
			remove_profile();
			break;
		case 4:
			search_movie();
			break;
		case 5:
			reservation_movie();
			break;
		case 6:
			print_reservationList();
			break;
		case 7:
			cancel_reservation();
			break;
		case 8:
			pay_ticket();
			break;
		case 9:
			System.exit(0);
		}
	}

	/* 회원 등록 */
	public void register_member() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		ResultSet rs = null;
		System.out.print("ID: ");
		while (true) {
			user.setId(sc.next());
			boolean find = false;
			boolean flag = false;

			stmt = conn.createStatement();
			rs = stmt.executeQuery("select id from member");

			while (rs.next()) {
				if (rs.getString("id").equals(user.getId())) {
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
		System.out.print("PW: ");
		user.setPw(sc.next());
		System.out.print("이름 : ");
		user.setName(sc.next());
		System.out.print("전화번호 : ");
		user.setTel(sc.next());
		System.out.print("생년월일 : ");
		user.setBirth(sc.next());
		System.out.print("주소 : ");
		user.setAddress(sc.next());
		user.setGrade("NORMAL");
		user.setPoint(0);
		user.setPurchase(0);

		prestmt = conn.prepareStatement(
				"INSERT INTO member(id,password,name,tel,birthday,address,grade,point,purchases) VALUES (?,?,?,?,?,?,?,?,?)");

		prestmt.setString(1, user.getId());
		prestmt.setString(2, user.getPw());
		prestmt.setString(3, user.getName());
		prestmt.setString(4, user.getTel());
		prestmt.setString(5, user.getBirth());
		prestmt.setString(6, user.getAddress());
		prestmt.setString(7, user.getGrade());
		prestmt.setInt(8, user.getPoint());
		prestmt.setInt(9, user.getPurchase());
		int count = prestmt.executeUpdate();
		System.out.println("회원가입이 완료되었습니다.");
		System.out.println("로그인 하시겠습니까?(y/n)");
		String yn = sc.next();
		if (yn.equals("y")) {
			user_login();
		} else if (yn.equals("n")) {
			System.exit(0);
		}
	}

	public void user_login() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		ResultSet rs = null;
		boolean find = false;
		System.out.print("ID: ");
		user.setId(sc.next());
		System.out.print("PW: ");
		user.setPw(sc.next());

		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from member");

		while (rs.next()) {
			if (rs.getString("id").equals(user.getId()) && rs.getString("password").equals(user.getPw())) {
				System.out.println("로그인이 완료되었습니다.");
				user.setName(rs.getString("name"));
				user.setTel(rs.getString("tel"));
				user.setBirth(rs.getString("birthday"));
				user.setAddress(rs.getString("address"));
				user.setGrade(rs.getString("grade"));
				user.setPoint(rs.getInt("point"));
				user.setPurchase(rs.getInt("purchases"));
				find = true;
				member_menu();
				break;
			}
		}
		if (find == false) {
			System.out.println("ID 또는 Password를 확인해주세요.");
		}

	}

	public void update_profile() throws ClassNotFoundException, SQLException {
		String newName = user.getName(), newPw = user.getPw(), newTel = user.getTel(), newAddr = user.getAddress();
		boolean exitSwitch = false;
		while (true) {
			System.out.println("-----변경할 내용을 선택해주세요-----");
			System.out.println("1.이름: " + newName);
			System.out.println("2.비밀번호: " + newPw);
			System.out.println("3.전화번호: " + newTel);
			System.out.println("4.주소: " + newAddr);
			System.out.println("5.변경사항 저장");

			int n = sc.nextInt();

			switch (n) {
			case 1:
				System.out.println("수정하고자 하는 이름을 입력해주세요.");
				newName = sc.next();
				break;
			case 2:
				System.out.println("수정하고자 하는 비밀번호를 입력해주세요.");
				newPw = sc.next();
				break;
			case 3:
				System.out.println("수정하고자 하는 전화번호를 입력해주세요.");
				newTel = sc.next();
				break;
			case 4:
				System.out.println("수정하고자하는 주소를 입력해주세요.");
				newAddr = sc.next();
				break;

			case 5:
				System.out.println("변경사항을 저장하시겠습니까?(y/n)");
				String yn = sc.next();
				if (yn.equals("y")) {
					Class.forName(JDBC_DRIVER); // 로딩

					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					prestmt = conn.prepareStatement( // sql문 전달.
							"update member set" + " password=?,name=?,tel=?,address=?" + " where id=?");
					prestmt.setString(1, newPw);
					prestmt.setString(2, newName);
					prestmt.setString(3, newTel);
					prestmt.setString(4, newAddr);
					prestmt.setString(5, user.getId());
					int count = prestmt.executeUpdate();
					System.out.println("정보수정이 완료되었습니다.");
					exitSwitch = true;
				} else {
					System.out.println("정보수정이 취소되었습니다.");
					exitSwitch = true;
				}
				break;
			}
			if (exitSwitch == true)
				break;
		}
		member_menu();
	}

	public void remove_profile() throws ClassNotFoundException, SQLException {
		ResultSet rs = null;
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select password from member where id='" + user.getId() + "'");
		rs.next();
		System.out.print("회원탈퇴를 하시려면 비밀번호를 다시 입력해주세요.\nPW: ");
		String rePw = sc.next();
		if (rePw.equals(rs.getString(1))) {
			Class.forName(JDBC_DRIVER); // 로딩
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			int count = stmt.executeUpdate("delete from member" + " where id='" + user.getId() + "'");
			System.out.println("회원탈퇴 되었습니다.");
		}
	}

	public void print_profile() throws ClassNotFoundException, SQLException {
		ResultSet rs = null;
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select * from member where id='" + user.getId() + "'");
		rs.next();
		System.out.println("아이디: " + rs.getString(1));
		System.out.println("비밀번호: " + rs.getString(2));
		System.out.println("이름: " + rs.getString(3));
		System.out.println("전화번호: " + rs.getString(4));
		System.out.println("생년월일: " + rs.getString(5));
		System.out.println("주소: " + rs.getString(6));
		System.out.println("등급: " + rs.getString(7));
		System.out.println("포인트: " + rs.getString(8));
		System.out.println("구매 실적: " + rs.getString(9));
		member_menu();
	}

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
		member_menu();
	}

	public void reservation_movie() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
		ResultSet rs1 = null;
		Class.forName(JDBC_DRIVER);
		System.out.println("------------------------------------");
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select title from movie where showing = 1 order by reservation_count desc");
		while (rs.next()) {
			System.out.println("-" + rs.getString("title"));
		}

		String inputMovieTitle = "";
		System.out.println("------------------------------------");
		while (true) {
			System.out.print("예매할 영화이름을 입력해주세요.\n>>");
			String temp1 = sc.next();
			String temp2 = sc.nextLine();
			inputMovieTitle = temp1 + temp2;
			stmt = conn.createStatement();
			rs1 = stmt.executeQuery("select title from movie where title='" + inputMovieTitle + "' and showing = 1");
			if (rs1.next())
				break;
			else
				System.out.print("잘못 입력하셨습니다. 다시 ");
		}

		stmt = conn.createStatement();
		rs1 = stmt.executeQuery(
				"select distinct auditorium.theater_name from movie,auditorium where movie.id = auditorium.movie_id and movie.title='"
						+ inputMovieTitle + "'");
		if (rs1.next()) {
			rs1.previous();
			System.out.println(inputMovieTitle + "이(가) 상영하는 영화관 목록입니다.");
			while (rs1.next()) { // 입력받은 영화를 상영시켜주는 영화관 출력
				System.out.println("-" + rs1.getString(1));
			}
		} else {
			System.out.println("잘못 입력하셨습니다.");
		}

		int AudiNumber = 0;
		String MovieId = "";
		System.out.print("영화관 이름을 입력해주세요.\n>>");
		String inputTheaterName = sc.next();
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select auditorium.number,movie.id from auditorium,movie"
				+ " where movie.id = auditorium.movie_id" + " and auditorium.theater_name='" + inputTheaterName + "'"
				+ " and movie.title='" + inputMovieTitle + "'");
		if (rs.next()) {
			AudiNumber = rs.getInt(1);
			MovieId = rs.getString(2);
			System.out.println(inputMovieTitle + "은(는) " + rs.getInt(1) + "관에서 상영합니다.");
		}

		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select auditorium.screen_date,auditorium.screen_start_time, auditorium.screen_end_time from auditorium,movie"
						+ " where movie.id = auditorium.movie_id and movie.title = '" + inputMovieTitle
						+ "' and auditorium.number = '" + AudiNumber + "' and" + " auditorium.theater_name= '"
						+ inputTheaterName + "' order by auditorium.screen_date");
		System.out.println("--------------------" + AudiNumber + "관 시간표--------------------");
		System.out.print("   ");
		System.out.printf("%-40s", "날짜");
		System.out.printf("%-35s", "시작시간");
		System.out.printf("%-20s", "종료시간");
		System.out.println();
		int cnt = 0;
		String date = "", start = "", end = "";
		while (rs.next()) {
			System.out.print(++cnt + ".");
			System.out.printf("%-20s", rs.getString(1));
			System.out.printf("%-19s", rs.getString(2));
			System.out.printf("%-20s", rs.getString(3));
			System.out.println();
		}

		System.out.println("-----------------------------------------------");
		System.out.print("예매하려는 영화 번호를 입력해주세요.\n>>");
		int inputN = sc.nextInt();

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select * from auditorium,movie" + " where movie.id = auditorium.movie_id and movie.title = '"
						+ inputMovieTitle + "' and auditorium.number = '" + AudiNumber + "' and"
						+ " auditorium.theater_name= '" + inputTheaterName + "' order by auditorium.screen_date");

		/* 해당 행에 접근하여 정보를 배열에 저장 */
		for (int i = 0; i < inputN; i++) {
			rs.next();
		}
		for (int i = 0; i < reservationArr.length; i++) {
			reservationArr[i] = "";
		}

		reservationArr[0] = rs.getString(1); // 영화관 이름
		reservationArr[1] = String.valueOf(rs.getInt(2)); // 상영관
		reservationArr[2] = rs.getString(6); // 영화 아이디
		reservationArr[3] = rs.getString(3); // 날짜
		reservationArr[4] = rs.getString(4); // 시작시간
		reservationArr[5] = rs.getString(5); // 종료시간
		/* 좌석번호는 밑에있음 */

		/* 여기서부터 보기 12.07 22:56 */
		conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		stmt = conn.createStatement(); // 해당 영화의 상영관 사이즈
		rs1 = stmt.executeQuery("select col_size,row_size from auditorium where theater_name = '" + reservationArr[0]
				+ "' and number='" + Integer.parseInt(reservationArr[1]) + "'");
		rs1.next();
		int colSize = Integer.parseInt(rs1.getString(1));
		int rowSize = Integer.parseInt(rs1.getString(2));

		/* seatArr 초기화 */
		seatArr = new String[rowSize][colSize];
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				seatArr[i][j] = " 0 ";
			}
		}

		stmt = conn.createStatement(); // 좌석배정 1 찾기
		rs1 = stmt.executeQuery("select row,col,isSelected from seat where theater_name = '" + reservationArr[0]
				+ "' and auditorium_number='" + Integer.parseInt(reservationArr[1]) + "' and screen_date='"
				+ reservationArr[3] + "' and screen_start_time ='" + reservationArr[4] + "' and isSelected=1");

		int rowIndex;
		int colIndex;
		while (rs1.next()) {
			rowIndex = (int) (rs1.getString(1).charAt(0)) - 65;
			colIndex = rs1.getInt(2) - 1;
			seatArr[rowIndex][colIndex] = " 1 ";
		}

		System.out.println("상영관의 예매 내역입니다.");
		int n = 1;
		System.out.print("  ");
		for (int i = 0; i < colSize; i++) { // 1,2,3,4
			System.out.printf("%3d", n++);
		}
		System.out.println();
		System.out.print("   ");
		for (int i = 0; i < colSize; i++) {
			System.out.print("---");
		}
		System.out.println();
		for (int i = 0; i < rowSize; i++) { // 좌석 배정 여부
			System.out.print((char) (i + 65) + " |"); // A,B,C,D
			for (int j = 0; j < colSize; j++) {
				System.out.print(seatArr[i][j]); // 0 0 0 1 0 1 0 1
			}
			System.out.println();
		}

		System.out.print("예매할 인원을 입력해주세요\n>>");
		int peopleCount = sc.nextInt();
		for (int i = 1; i <= peopleCount; i++) {
			while (true) {
				System.out.print(i + "번째 좌석을 입력해주세요.ex(A1,B4,C9,...)\n>>");
				String inputSeat = sc.next();

				int inputSeatRowIndex = (int) (inputSeat.substring(0, 1).charAt(0)) - 65;
				String rowStringValue = inputSeat.substring(0, 1);
				int inputSeatColIndex = Integer.parseInt(inputSeat.substring(1, inputSeat.length())) - 1;
				String colStringValue = inputSeat.substring(1, inputSeat.length());

				if (inputSeatRowIndex < 0 || inputSeatRowIndex >= seatArr.length || inputSeatColIndex < 0
						|| inputSeatColIndex >= seatArr[0].length) {
					System.out.print("없는 좌석입니다. 다시 ");
				} else if (seatArr[inputSeatRowIndex][inputSeatColIndex].equals(" 1 ")) {
					System.out.println("이미 예약된 좌석입니다. 다시");
				} else {
					System.out.println();
					seatArr[inputSeatRowIndex][inputSeatColIndex] = " 1 "; // arr에
																			// 저장
					reservationArr[6] += inputSeat + "/"; // 좌석번호
					/* db에 저장 12.08 22:21 */
					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					prestmt = conn.prepareStatement( // sql문 전달.
							"update seat set" + " isSelected=?" + " where theater_name=? and auditorium_number=?"
									+ " and row=? and col=? and screen_date=? and screen_start_time=?");
					prestmt.setInt(1, 1);
					prestmt.setString(2, reservationArr[0]);
					prestmt.setInt(3, Integer.valueOf(reservationArr[1]));
					prestmt.setString(4, rowStringValue);
					prestmt.setString(5, colStringValue);
					prestmt.setString(6, reservationArr[3]);
					prestmt.setString(7, reservationArr[4]);
					int count = prestmt.executeUpdate();
					System.out.println("예매가 완료되었습니다.");
					break;
				}
			}

		}

		String reserveNumber = "";

		/* 예매번호를 만들어 내는 반복문 */
		for (int i = 0; i < reservationArr.length - 1; i++) {
			reserveNumber += reservationArr[i] + "_";
		}

		reserveNumber += reservationArr[reservationArr.length - 1];
		String NewReserverNumber = reserveNumber.substring(0, reserveNumber.length() - 1);
		prestmt = conn.prepareStatement(
				"INSERT INTO reservation(number,member_id,movie_id,count,amount,isPaid) VALUES (?,?,?,?,?,?)");

		prestmt.setString(1, NewReserverNumber);
		prestmt.setString(2, user.getId());
		prestmt.setString(3, reservationArr[2]);
		prestmt.setInt(4, peopleCount);
		prestmt.setInt(5, peopleCount * 8000);
		prestmt.setInt(6, 0);
		int count = prestmt.executeUpdate();
		prestmt.close();

		prestmt = conn.prepareStatement(
				"update member set purchases = purchases + " + peopleCount * 8000 + " where id='" + user.getId() + "'");
		int count1 = prestmt.executeUpdate();
		prestmt.close();

		prestmt = conn.prepareStatement(
				"update member set point = point + " + peopleCount * 100 + " where id='" + user.getId() + "'");
		int count2 = prestmt.executeUpdate();
		prestmt.close();

		prestmt = conn.prepareStatement("update movie set reservation_count = reservation_count + " + peopleCount
				+ " where id='" + reservationArr[2] + "'");
		int count3 = prestmt.executeUpdate();

		member_menu();
	}

	public void print_reservationList() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		ResultSet rs1 = null;
		String reservation_number;
		String[] parser; // 0: 영화관이름, 1:상영관 번호, 2:영화 아이디, 3:상영 날짜, 4:시작시간,
							// 5:종료시간, 6:좌석
		System.out.println("'" + user.getName() + "'님께서 예매하신 내역입니다.");

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member,reservation where member.id = reservation.member_id and member.name = '"
						+ user.getName() + "'");
		int cnt = 0;
		if (rs.next()) {
			rs.previous();
			while (rs.next()) {
				reservation_number = rs.getString(1);
				parser = reservation_number.split("_");
				System.out.println("------------" + ++cnt + "번 내역------------");
				System.out.println("영화관: " + parser[0]);
				System.out.println("상영관: " + parser[1]);
				Statement stmt1 = conn.createStatement();
				rs1 = stmt1.executeQuery("select title from movie where id='" + parser[2] + "'");
				rs1.next();
				System.out.println("영화제목: " + rs1.getString(1));
				rs1.close();
				System.out.println("상영날짜: " + parser[3]);
				String startT, endT;
				startT = parser[4].substring(0, 2) + ":" + parser[4].substring(2, 4);
				endT = parser[5].substring(0, 2) + ":" + parser[5].substring(2, 4);
				System.out.println("상영 시간: " + startT + " ~ " + endT);
				System.out.println("좌석: " + parser[6]);
			}
		}
		rs.close();
		member_menu();
	}

	public void cancel_reservation() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		String reservation_number;
		String[] parser; // 0: 영화관이름, 1:상영관 번호, 2:영화 아이디, 3:상영 날짜, 4:시작시간,
							// 5:종료시간, 6:좌석

		System.out.print("예약 취소입니다.\n고객님의 예약 현황입니다.\n");
		Date now = new Date();

		SimpleDateFormat formatType = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(formatType.format(now));

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member,reservation where member.id = reservation.member_id and member.name = '"
						+ user.getName() + "'");
		rs.next();
		parser = rs.getString(1).split("_");

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select reservation.number from member, reservation where member.id='" + user.getId() + "'");
		int cnt = 0;
		ArrayList<String> arr = new ArrayList<String>();

		if (rs.next()) {
			rs.previous();
			while (rs.next()) {
				arr.add(rs.getString(1));
				System.out.println(++cnt + "." + rs.getString(1));
			}
		}
		System.out.print("취소하고 싶은 번호를 입력해주세요.\n>>");
		int n = sc.nextInt();
		System.out.print("정말 취소하시겠습니까?(y/n)\n>>");
		String yn = sc.next();
		if (yn.equals("y")) {
			stmt = conn.createStatement();
			int count = stmt.executeUpdate("delete from reservation" + " where number='" + arr.get(n - 1) + "'");

			String reservation_parser[] = arr.get(n - 1).split("_"); // 0:영화관 이름
																		// 1:상영관
																		// 2.영화
																		// id
																		// 3:날짜
																		// 4:시작시간
																		// 5:좌석

			String seatParser[] = reservation_parser[6].split("/");

			for (int i = 0; i < seatParser.length; i++) {
				String row = seatParser[i].substring(0, 1);
				String col = seatParser[i].substring(1, 2);
				prestmt = conn.prepareStatement( // sql문 전달.
						"update seat set" + " isSelected=?"
								+ " where theater_name=? and auditorium_number=? and row=? and col=? and screen_date=? and screen_start_time=?");
				prestmt.setInt(1, 0);
				prestmt.setString(2, reservation_parser[0]);
				prestmt.setInt(3, Integer.parseInt(reservation_parser[1]));
				prestmt.setString(4, row);
				prestmt.setString(5, col);
				prestmt.setString(6, reservation_parser[3]);
				prestmt.setString(7, reservation_parser[4]);

				int count1 = prestmt.executeUpdate();
				prestmt.close();

			}

			prestmt = conn.prepareStatement("update member set purchases= purchases-" + seatParser.length * 8000
					+ " where id = '" + user.getId() + "'");

			int count2 = prestmt.executeUpdate();
			prestmt.close();

			prestmt = conn.prepareStatement("update member set point = point - " + seatParser.length * 100
					+ " where id='" + user.getId() + "'");
			int count3 = prestmt.executeUpdate();
			prestmt.close();

			prestmt = conn.prepareStatement("update movie set reservation_count = reservation_count - "
					+ seatParser.length + " where id='" + reservation_parser[2] + "'");
			int count4 = prestmt.executeUpdate();

			System.out.println("예매취소가 정상적으로 처리되었습니다.");
			member_menu();

		} else {
			System.out.println("예매취소가 처리되지 않았습니다.");
			member_menu();
		}
	}

	public void pay_ticket() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		ResultSet rs = null;
		String[] parser; // 0: 영화관이름, 1:상영관 번호, 2:영화 아이디, 3:상영 날짜, 4:시작시간,
							// 5:종료시간, 6:좌석

		System.out.print("결제 페이지입니다.\n고객님의 예매 현황입니다.\n");

		stmt = conn.createStatement();
		rs = stmt.executeQuery(
				"select number from reservation where reservation.member_id='" + user.getId() + "' and isPaid=0");
		int cnt = 0;
		ArrayList<String> arr = new ArrayList<String>();

		if (rs.next()) {
			rs.previous();
			while (rs.next()) {
				arr.add(rs.getString(1));
				System.out.println(++cnt + "." + rs.getString(1));
			}
		}

		System.out.print("결제하고 싶은 번호를 입력해주세요.\n>>");
		int n = sc.nextInt();

		parser = arr.get(n - 1).split("_");
		String seatParser[] = parser[6].split("/");
		int seatCount = seatParser.length;
		int price = seatCount * 8000;
		/* 고객의 포인트 */
		int remainingPoint = 0; // 초기화
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select name,point from member where id='" + user.getId() + "'");
		if (rs.next()) {
			System.out.println(rs.getString(1) + "님은 현재 " + rs.getInt(2) + "point를 가지고 계십니다.");
			remainingPoint = rs.getInt(2);
		}
		int inputPoint = 0; // 초기화
		if (remainingPoint >= 1000) {
			System.out.println("1000point 이상 가지고 계시므로 포인트 사용이 가능합니다.");
			while (true) {
				System.out.print("사용하실 포인트를 입력해주세요.\n>>");
				inputPoint = sc.nextInt();
				if (inputPoint > remainingPoint) {
					System.out.print("가지고 계신 포인트보다 큰 값을 입력하셨습니다. 다시 ");
				} else if (inputPoint > price) {
					System.out.print("금액보다 더 큰 포인트 값을 입력하셨습니다. 다시");
				} else {
					break;
				}
			}
		} else {
			System.out.println("1000point 미만 가지고 계시므로 포인트 사용이 불가능합니다.");
		}

		System.out.println("총 결제 금액: " + price + "원");
		System.out.println("포인트 사용 금액: " + inputPoint + "원");
		System.out.println("실제 결제 금액: " + (price - inputPoint) + "원");

		System.out.println("---------------결제 수단 선택---------------");
		System.out.print("1.현장 결제  2.카드 결제\n>>");
		int inputPaynumber = sc.nextInt();

		ResultSet payRS = null;
		String reservationNumber = arr.get(n - 1);

		stmt = conn.createStatement();
		payRS = stmt.executeQuery("select member_id from reservation where number='" + reservationNumber + "'");
		payRS.next();
		String reservation_memberId = payRS.getString(1);

		if (inputPaynumber == 1) {
			prestmt = conn.prepareStatement("INSERT INTO payment(reservation_number,pay_method) VALUES (?,?)");

			prestmt.setString(1, reservationNumber);
			prestmt.setString(2, "cash");
			int count = prestmt.executeUpdate();
			System.out.println("현장에서 " + (price - inputPoint) + "원을 결제한 후, 티켓을 수령하세요.");
		} else if (inputPaynumber == 2) {

			System.out.print("카드 회사를 입력해주세요.\n>>");
			String inputCardCorp = sc.next();
			System.out.print("카드 번호를 입력해주세요.\n>>");
			String inputCardNumber = sc.next();

			System.out.print(
					inputCardCorp + "(" + inputCardNumber + ")로 " + (price - inputPoint) + "원을 정말 결제하시겠습니까?(y/n)\n>>");
			String yn = sc.next();
			if (yn.equals("y")) {

				prestmt = conn.prepareStatement("INSERT INTO payment(reservation_number,pay_method) VALUES (?,?)");

				prestmt.setString(1, reservationNumber);
				prestmt.setString(2, "card");
				int count = prestmt.executeUpdate();

				prestmt = conn.prepareStatement( // sql문 전달.
						"update reservation set isPaid=?" + " where number='" + arr.get(n - 1) + "'");
				prestmt.setInt(1, 1);
				int count1 = prestmt.executeUpdate();

				prestmt = conn.prepareStatement(
						"update member set point = point - " + inputPoint + " where id='" + user.getId() + "'");
				int count4 = prestmt.executeUpdate();

				System.out.println("결제가 완료되었습니다.");
			}
		}

	}
}
