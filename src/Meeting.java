
import java.io.Serializable;
import java.util.*;

class Meeting implements Serializable {

	private static final long serialVersionUID = 1L;
	private int meetingId;
	private String[] users;
	private String title;
	private Date start;
	private Date end;
	
	public Meeting(int meetingId,String firstUser,String secondUser,
					String title,Date start,Date end) {
		users = new String[2];
		users[0] = firstUser;
		users[1] = secondUser;
		this.meetingId = meetingId;
		
		this.title = title;
		this.start = start;
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}
	
	public String[] getUsers() {
		return users;
	}
	
	public int getMeetingId() {
		return meetingId;
	}

	@Override
	public String toString() {
		return "Meeting [title=" + title + ", start=" + start + ", end=" + end
				+ "]";
	}
}
