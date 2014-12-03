import java.util.Calendar;
import java.util.List;
import java.util.Set;
//import XML parsers

/**
 * Implementation of ContactManager
 *
 */
public class ContactManagerImpl implements ContactManager {

	/**
	 * 
	 */
	public ContactManagerImpl() {
		// TODO Auto-generated constructor stub
	}


	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		// TODO Auto-generated method stub
		return 0;
	}

	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public FutureMeeting getFutureMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		// TODO Auto-generated method stub

	}

	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub

	}

	public void addNewContact(String name, String notes) {
		// TODO Auto-generated method stub

	}

	public Set<Contact> getContacts(int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Contact> getContacts(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void flush() {
		// TODO Auto-generated method stub

	}

}
