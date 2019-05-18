package movie;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Administrator admin = new Administrator();
		MemberManagement management = new MemberManagement();
		int n;
		System.out.println("1.회원가입  2.로그인");
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		switch (n) {
		case 1:
			management.register_member();
			break;
		case 2:
			System.out.println("1.회원  2.관리자");
			int in = sc.nextInt();
			if (in == 1) {
				management.user_login();
			}
			if (in == 2) {
				admin.admin_login();
			}
			break;
		}
	}

}
