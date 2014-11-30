import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl implements PastMeeting {
	private Calendar meetingDate;
	private int id;
	private String notes;
	private Set<Contact> contactList;
	
	public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes){
		this.meetingDate = date;
		this.notes = notes;
		this.contactList = contacts;
		//need to change this
		this.id = 1;
	}
	
	public int getId() {
		return id;
	}

	public Calendar getDate() {
		return meetingDate;
	}

	public Set<Contact> getContacts() {
		return contactList;
	}

	public String getNotes() {
		return notes;
	}

}
