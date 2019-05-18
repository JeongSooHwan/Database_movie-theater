package movie;

public class Auditorium {
	private String theater_name, screen_date, movie_id, screen_start_time, screen_end_time;
	private int row_size, col_size, number;

	public int getRow_size() {
		return row_size;
	}

	public void setRow_size(int row_size) {
		this.row_size = row_size;
	}

	public int getCol_size() {
		return col_size;
	}

	public void setCol_size(int col_size) {
		this.col_size = col_size;
	}

	public String getTheater_name() {
		return theater_name;
	}

	public void setTheater_name(String n) {
		this.theater_name = n;
	}

	public String getScreen_date() {
		return screen_date;
	}

	public void setScreen_date(String d) {
		this.screen_date = d;
	}

	public String getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(String m) {
		this.movie_id = m;
	}

	public int getNumber() {
		return number;
	}

	public void setNmuber(int n) {
		this.number = n;
	}

	public String getStart_time() {
		return screen_start_time;
	}

	public void setStart_time(String n) {
		this.screen_start_time = n;
	}

	public String getEnd_time() {
		return screen_end_time;
	}

	public void setEnd_time(String n) {
		this.screen_end_time = n;
	}
}
