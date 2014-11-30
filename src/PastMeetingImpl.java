import java.util.Calendar;
import java.util.Set;
import java.util.GregorianCalendar;

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
		// TODO Auto-generated method stub
		return 0;
	}

	public Calendar getDate() {
		// TODO Auto-generated method stub
		return null;
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
