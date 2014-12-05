import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting{
	private Calendar meetingDate;
	private int id;
	private Set<Contact> contactList;
	
	public MeetingImpl(int id, Set<Contact> contacts, Calendar date){
		this.meetingDate = date;
		this.contactList = contacts;
		//need to change this
		this.id = id;
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

}
