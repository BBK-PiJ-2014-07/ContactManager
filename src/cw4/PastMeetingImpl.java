package cw4;

import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	private String notes;

	public PastMeetingImpl(int id, Set<Contact> contacts, Calendar date, String notes){
		super(id, contacts,date);
		this.notes = notes;
	}
	
	public String getNotes() {
		return notes;
	}

}
