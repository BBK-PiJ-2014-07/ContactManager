import java.util.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import XML parsers

/**
 * Implementation of ContactManager
 *
 */
public class ContactManagerImpl implements ContactManager {
	private FileWriter writer;
	private Calendar todaysDate;
	private Set<Contact> contactList;
	private int newContactId;
	private int newMeetingId;
	private List<PastMeeting> pastMeetingList;
	private List<Meeting> futureMeetingList;

	public ContactManagerImpl() {
		try {
			this.writer = new FileWriter("contacts.xml");
		} catch (IOException e) {
			// FileWriter throws exception so need to catch it
			e.printStackTrace();
		}
		newContactId = 1;
		newMeetingId = 1;
		pastMeetingList = new ArrayList<PastMeeting>();
		futureMeetingList = new ArrayList<Meeting>();
		todaysDate = new GregorianCalendar();
		contactList = new HashSet<Contact>(); //need to populate this from contacts.xml

	}

	/**
	 * The method that writes to the file. It needs to check whether the file exists, and if so, append
	 * rather than overwrite. 
	 * @param str - the string to be written
	 */
	public void writeToFile(String str){
		//TODO
	}
	
	/**
	* Add a new meeting to be held in the future.
	*
	* @param contacts a list of contacts that will participate in the meeting
	* @param date the date on which the meeting will take place
	* @return the ID for the meeting
	* @throws IllegalArgumentException if the meeting is set for a time in the past,
	* of if any contact is unknown / non-existent
	*/
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		FutureMeeting fm = null;
		if (date.before(todaysDate) || !contactList.containsAll(contacts)) {	//check date + contacts
			throw new IllegalArgumentException();
		} else {
			fm = new FutureMeetingImpl(newMeetingId, contacts, date);
			futureMeetingList.add(fm);
			//write to file
			newMeetingId++;
		}
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
		// IF id matches getId() of FutureMeeting, then throw illegal argument exception
		// ELSE look through pastMeetingList to see if it exists. 
		// If it exists, then return it.
		// ELSE return null.
		return null;
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
		for (Meeting fm: futureMeetingList){
			if (fm.getContacts().contains(contact)){
				result.add(fm);
			}
		}
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
		for (Meeting fm : futureMeetingList){
			if (fm.getDate().compareTo(date)==0){
				result.add(fm);
			}
		}

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
		for (PastMeeting pm: pastMeetingList) {
			if (pm.getContacts().contains(contact)){
				result.add(pm);
			}
		}
		return result;
	}

	/**
	 * Debugging method for testing, will be removed once all interface methods implemented.
	 * @return the internal List of pastMeetings
	 */
	//public List<PastMeeting> getPastMeetingList() {
	//	return pastMeetingList;
	//}
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

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		if (contacts == null || date == null || text == null) {
			throw new NullPointerException();
		}
		if (contacts.isEmpty() || !contactList.containsAll(contacts)){
			throw new IllegalArgumentException();
		}
		PastMeeting pm = new PastMeetingImpl(newMeetingId,contacts, date, text);
		pastMeetingList.add(pm);
		newMeetingId++;
		//write to file
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
		// TODO Auto-generated method stub

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
		//write new contact to file
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
			int contactsFound=0;	//flag to indicate whether matching contacts have been found
			for (Contact c : contactList) {
				if (c.getId() == thisId) {
					result.add(c);
					contactsFound++;
				}
			}
			if (contactsFound==0){
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
		for (Contact c: contactList){
			if (c.getName().equals(name)){
				result.add(c);
			}
		}
		return result;
	}
	
	/**
	* Save all data to disk.
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush() {
		// TODO Auto-generated method stub

	}
}
