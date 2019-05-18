package movie;

public class Movie {
	private String id, title, director, casts, details;
	private int grade, reservation_count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String d) {
		this.director = d;
	}

	public String getCasts() {
		return casts;
	}

	public void setCasts(String c) {
		this.casts = c;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String d) {
		this.details = d;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int g) {
		this.grade = g;
	}

	public int getReservation_count() {
		return reservation_count;
	}

	public void setReservation_count(int r) {
		this.reservation_count = r;
	}
}
