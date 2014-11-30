import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl implements PastMeeting {
	private Calendar meetingDate;
	private int id;
	private String notes;
	private Set<Contact> contactList;
	
	public PastMeetingImpl(Calendar date, String notes){
		this.meetingDate = date;
		this.notes = notes;
		this.contactList = null;
		this.id = 1;
	}
	
	public int getId() {
		return id;
	}

	public Calendar getDate() {
		return meetingDate;
	}

	public Set<Contact> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

}
