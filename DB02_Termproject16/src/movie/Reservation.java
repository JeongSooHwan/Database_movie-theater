package movie;

public class Reservation {
	private int count, amount;
	private String number, member_id, movie_id, date;

	public String getNumber() {
		return number;
	}

	public void setNumer(String n) {
		this.number = n;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int c) {
		this.count = c;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int a) {
		this.amount = a;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String s) {
		this.member_id = s;
	}

	public String getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(String s) {
		this.movie_id = s;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String s){
		this.date = s;
	}
}
