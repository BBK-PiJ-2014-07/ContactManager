import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;

/**
 * Implementation of ContactManager
 * @author Sophie Koonin
 * @see ContactManager
 *
 */
public class ContactManagerImpl implements ContactManager {

	private Calendar todaysDate;
	private List<Contact> contactList;
	private int newContactId;
	private int newMeetingId;
	private List<PastMeeting> pastMeetingList;
	private List<Meeting> futureMeetingList;
	private List<List<?>> contactManagerObjects; //the List that holds the other lists, for easy I/O
	private ObjectInputStream inputStream;
	private File contactsFile;


	public ContactManagerImpl(){
		contactsFile = new File("contacts.txt");
		todaysDate = new GregorianCalendar(); //initialise a new calendar to today's date for comparison
		try {
			if (contactsFile.exists()) {
				inputStream = new ObjectInputStream(new FileInputStream(contactsFile));

				//if the file exists and there's data in it, use that to repopulate the classes
				contactManagerObjects = (ArrayList) inputStream.readObject();
				futureMeetingList = (ArrayList) contactManagerObjects.get(0);
				pastMeetingList = (ArrayList) contactManagerObjects.get(1);
				contactList = (ArrayList) contactManagerObjects.get(2);
			} else {
				// otherwise, make new empty objects
				pastMeetingList = new ArrayList<PastMeeting>();
				futureMeetingList = new ArrayList<Meeting>();
				contactList = new ArrayList<Contact>();
				contactManagerObjects = new ArrayList<List<?>>();
				contactManagerObjects.add(futureMeetingList);
				contactManagerObjects.add(pastMeetingList);
				contactManagerObjects.add(contactList);

			}
		} catch (ClassNotFoundException | IOException ex) {
			ex.printStackTrace();
		}
		newContactId = contactList.size()+1; //should be one larger than the size of the contacts list
		newMeetingId = (futureMeetingList.size() + pastMeetingList.size()) + 1; //should be one larger than size of both meeting lists combined

	}

	/**
	* Add a new meeting to be held in the future.
	* @param contacts a list of contacts that will participate in the meeting
	* @param date the date on which the meeting will take place
	* @return the ID for the meeting
	* @throws IllegalArgumentException if the meeting is set for a time in the past,
	* of if any contact is unknown / non-existent
	*/
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		if (date.before(todaysDate) || !contactList.containsAll(contacts)) {	//check date + contacts
			throw new IllegalArgumentException();
		}
		FutureMeeting fm = new FutureMeetingImpl(newMeetingId, contacts, date);
		futureMeetingList.add(fm);
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(contactsFile));
			outputStream.reset();
			outputStream.writeObject(contactManagerObjects);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		newMeetingId++;

		return fm.getId();
	}

	/**
	* Returns the PAST meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the future
	*/
	public PastMeeting getPastMeeting(int id) {
		PastMeeting result = null;
		for (Meeting fm: futureMeetingList){
			if (fm.getId() == id) {
				throw new IllegalArgumentException();
			}
		}
		for (PastMeeting pm: pastMeetingList){
			if (pm.getId() == id){
				result = pm;
			}
		}
		return result;
	}

	/**
	* Returns the FUTURE meeting with the requested ID, or null if there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the past
	*/
	public FutureMeeting getFutureMeeting(int id) {
		FutureMeeting result = null;
		for (PastMeeting pm: pastMeetingList){
			if (pm.getId() == id){
				throw new IllegalArgumentException();
			}
		}
		for (Meeting fm: futureMeetingList){
			if (fm.getId()==id){
				result = (FutureMeeting) fm;
			}
		}
		return result;
	}

	/**
	* Returns the meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	*/
	public Meeting getMeeting(int id) {
		Meeting result = null;
		for (Meeting fm: futureMeetingList) {
			if (fm.getId() == id) {
				result = fm;
			}
		}
		for (PastMeeting pm: pastMeetingList){
			if (pm.getId()==id){
				result = pm;
			}
		}
		return result;
	}

	/**
	* Returns the list of future meetings scheduled with this contact.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param contact one of the user's contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<Meeting> getFutureMeetingList(Contact contact) {
		if (!contactList.contains(contact)){
			throw new IllegalArgumentException();
		}
		List<Meeting> result = new ArrayList<Meeting>();
		futureMeetingList.stream().filter(m -> m.getContacts().contains(contact)).forEach(result::add);
		result.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		return result;
	}
	
	/**
	* Returns the list of meetings that are scheduled for, or that took
	* place on, the specified date
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param date the date
	* @return the list of meetings
	*/
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> result = new ArrayList<Meeting>();
		if (date.before(todaysDate)) {
			pastMeetingList.stream().filter(m -> m.getDate().compareTo(date) == 0).forEach(result::add);
		} else {
			futureMeetingList.stream().filter(m->m.getDate().compareTo(date)==0).forEach(result::add);
		}
		result.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		return result;
	}


	/**
	* Returns the list of past meetings in which this contact has participated.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	2* duplicates.
	*
	* @param contact one of the user�s contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		if (!contactList.contains(contact)){
			throw new IllegalArgumentException();
		}
		List<PastMeeting> result = new ArrayList<PastMeeting>();
		pastMeetingList.stream().filter(pm->pm.getContacts().contains(contact)).forEach(result::add);
		result.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		return result;
	}

	/**
	* Create a new record for a meeting that took place in the past.
	*
	* @param contacts a list of participants
	* @param date the date on which the meeting took place
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the list of contacts is
	* empty, or any of the contacts does not exist
	* @throws NullPointerException if any of the arguments is null
	*/

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		if (contacts == null || date == null || text == null) {
			throw new NullPointerException();
		}
		if (contacts.isEmpty() || !contactList.containsAll(contacts)){
			throw new IllegalArgumentException();
		}

		PastMeeting pm = new PastMeetingImpl(newMeetingId,contacts, date, text);
		pastMeetingList.add(pm);
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(contactsFile));
			outputStream.reset();
			outputStream.writeObject(contactManagerObjects);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		newMeetingId++;
	}


	/**
	* Add notes to a meeting.
	*
	* This method is used when a future meeting takes place, and is
	* then converted to a past meeting (with notes).
	*
	* It can be also used to add notes to a past meeting at a later date.
	*
	* @param id the ID of the meeting
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the meeting does not exist
	* @throws IllegalStateException if the meeting is set for a date in the future
	* @throws NullPointerException if the notes are null
	*/
	public void addMeetingNotes(int id, String text) {
		if (text == null) {
			throw new NullPointerException();
		}
		Meeting thisMeeting = null;
		if (futureMeetingList.stream().anyMatch(m->m.getId()==id)){
			thisMeeting = futureMeetingList.stream().filter(m->m.getId()==id).findFirst().get();
			futureMeetingList.removeIf(m -> m.getId() == id);	//look through futureMeetingList for this meeting and remove it
			PastMeeting pm = new PastMeetingImpl(id, thisMeeting.getContacts(), thisMeeting.getDate(), text);
			pastMeetingList.add(pm); //Doing this manually rather than calling addPastMeeting to keep ID the same

		} else if (pastMeetingList.stream().anyMatch(m->m.getId()==id)){
			thisMeeting = pastMeetingList.stream().filter(m->m.getId()==id).findFirst().get();
			pastMeetingList.removeIf(m->m.getId() == id);
			PastMeeting newPm = new PastMeetingImpl(id, thisMeeting.getContacts(), thisMeeting.getDate(), ((PastMeeting) thisMeeting).getNotes() + ", "+text);
			pastMeetingList.add(newPm);

		} else {
			throw new IllegalArgumentException();
		}

		if (thisMeeting.getDate().after(todaysDate)){
			throw new IllegalStateException();
		}

		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(contactsFile));
			outputStream.reset();
			outputStream.writeObject(contactManagerObjects);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Create a new contact with the specified name and notes.
	*
	* @param name the name of the contact.
	* @param notes notes to be added about the contact.
	* @throws NullPointerException if the name or the notes are null
	*/
	public void addNewContact(String name, String notes) {
		if (name == null || notes == null){
			throw new NullPointerException();		//check that neither notes or name is null
		}
		Contact newContact = new ContactImpl(newContactId, name, notes); //instantiate contact with ID
		contactList.add(newContact); //add it to the internal contact list
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(contactsFile));
			outputStream.reset();
			outputStream.writeObject(contactManagerObjects);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		newContactId++; //increment newContactId for next contact
	}

	/**
	* Returns a list containing the contacts that correspond to the IDs.
	*
	* @param ids an arbitrary number of contact IDs
	* @return a list containing the contacts that correspond to the IDs.
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	*/
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> result = new HashSet<Contact>();
		for (int thisId: ids) {
			contactList.stream().filter(c->c.getId() == thisId).forEach(result::add);
			if (result.isEmpty()){
				throw new IllegalArgumentException(); 	//if no contacts with that ID are found
			}
		}
		return result;
	}

	
	/**
	* Returns a list with the contacts whose name contains that string.
	3*
	* @param name the string to search for
	* @return a list with the contacts whose name contains that string.
	* @throws NullPointerException if the parameter is null
	*/
	public Set<Contact> getContacts(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		Set<Contact> result = new HashSet<Contact>();
		contactList.stream().filter(c -> c.getName().equals(name)).forEach(result::add);
		return result;
	}
	
	/**
	* Save all data to disk.
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush() {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
