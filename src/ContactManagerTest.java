import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

public class ContactManagerTest {
	private ContactManagerImpl cm; 		//not using interface as impl has more methods
	private Set<Contact> contacts;
	private FutureMeeting fm;
	private Contact alan;
	private Contact sarah;
	
	@Before
	public void buildUp(){
		cm = new ContactManagerImpl();
		contacts = new HashSet<Contact>();
		alan = new ContactImpl(1, "Alan", "nice");
		contacts.add(alan);
		sarah = new ContactImpl(2, "Sarah", "horrible");
		contacts.add(sarah);
		cm.addNewContact("Alan", "nice");
		cm.addNewContact("Sarah", "horrible");
	}

	/**
	 * Test that addFutureMeeting works as expected
	 */
	@Test
	public void testAddFutureMeeting(){
		int newFutureMeetingId = cm.addFutureMeeting(contacts, new GregorianCalendar(2015,1,12));	//13 feb 2015
		assertTrue(newFutureMeetingId == 1);
	}

	/**
	 * Test that addFutureMeeting() throws IllegalArgumentException
	 * if date is in the past
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddFutureMeetingPastDate(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2013,2,24));
	}

	/**
	 * Test that addFutureMeeting() throws IllegalArgumentException
	 * if contacts don't exist
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddFutureMeetingIllegalContacts(){
		Set<Contact> badCont = new HashSet<Contact>();
		badCont.add(new ContactImpl(55, "Mary", "made up"));
		cm.addFutureMeeting(badCont, new GregorianCalendar(2013,2,24));
	}

	/**
	 * Test that getContacts(String) works
	 */
	@Test
	public void testGetContactsWithString(){
		Set<Contact> getCont = cm.getContacts("Alan");
		assertTrue(getCont.contains(alan));
	}

	/**
	 * Test that getContacts(int) works with single int argument
	 */
	@Test
	public void testGetContactsWithSingleId(){
		Set<Contact> getCont = cm.getContacts(1);
		assertTrue(getCont.contains(alan));
	}

	/**
	 * Test that getContacts(int) works with multiple int arguments
	 */
	@Test
	public void testGetContactsWithMultipleIds(){
		Set<Contact> getCont = cm.getContacts(1,2);
		assertTrue(getCont.containsAll(contacts));
	}

	/**
	 * Check that getContacts(int) throws IllegalArgumentException
	 * if the id number doesn't belong to any contacts
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsWithIllegalId(){
		Set<Contact> getCont = cm.getContacts(12);
	}

	/**
	 * Test that getContacts(String) throws NullPointerException
	 * if argument is null
	 */
	/*
	Commented out, as ambiguous reference
	@Test(expected = NullPointerException.class)
	public void testGetContactsWithNullArg(){
		Set<Contact> getCont = cm.getContacts(null);
	}
	*/

	/**
	 * Test normal functionality of addNewContact()
	 */
	@Test
	public void testAddNewContact(){
		cm.addNewContact("Buffy", "vampire slayer");
		Set<Contact> getCont = cm.getContacts("Buffy");
		assertFalse(getCont.isEmpty());
	}

	/**
	 * Test normal functionality of addNewPastMeeting
	 */
	@Test
	public void testNewPastMeeting() {
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,4,5), "board meeting");
		assertFalse(cm.getPastMeetingList(alan).isEmpty());
	}

	/**
	 * Test to check that addNewPastMeeting() throws NullPointerException
	 * if any of its arguments are null
	 */
	@Test(expected= NullPointerException.class)
	public void testNewPastMeetingNullArg(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,6), null);
	}

	/**
	 * Test to check that addNewPastMeeting() throws IllegalArgumentException
	 * if any of the contacts in the set passed as an argument don't exist
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNewPastMeetingIllegalContacts(){
		Set<Contact> badCont = new HashSet<Contact>();
		badCont.add(new ContactImpl(55, "Mary", "made up"));
		cm.addNewPastMeeting(badCont, new GregorianCalendar(2013,4,2), "this should break");
	}

	/**
	 * Test to check that addNewPastMeeting() throws IllegalArgumentException
	 * if the contact set passed as an argument is empty
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNewPastMeetingEmptyContactSet(){
		Set<Contact> emptyCont = new HashSet<Contact>();
		cm.addNewPastMeeting(emptyCont, new GregorianCalendar(2014,02,2), "this should also break");
	}

	/**
	 * Test normal functionality of getPastMeetingList
	 */
	@Test
	public void testGetPastMeetingList(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,12,12), "test notes");
		List<PastMeeting> pml = cm.getPastMeetingList(alan);
		assertTrue(!pml.isEmpty());
	}

	/**
	 * Test that getPastMeetingList throws IllegalArgumentException
	 * if contact passed as argument is nonexistent
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPastMeetingListIllegalContact(){
		Contact nigella = new ContactImpl(99, "Nigella","doesn't exist");
		List<PastMeeting> pml = cm.getPastMeetingList(nigella);
	}

	/**
	 * Test that getFutureMeetingList(Calendar date) works as expected
	 */
	@Test
	public void testGetFutureMeetingListDate(){
		Calendar myCal = new GregorianCalendar(2016,3,4);
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,3,4));
		List<Meeting> fml = cm.getFutureMeetingList(myCal);
		assertTrue(fml.get(0).getDate().compareTo(myCal)==0);
	}

	/**
	 * Test that getFutureMeetingList(Contact contact) works as expected)
	 *
	 */
	@Test
	public void testGetFutureMeetingListContact(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,3,4));
		List<Meeting> fml = cm.getFutureMeetingList(alan);
		assertTrue(fml.get(0).getContacts().equals(contacts));
	}

	/**
	 * Test that getFutureMeetingList(Contact contact) throws IllegalArgumentException
	 * if contact does not exist
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFutureMeetingListIllegalContact(){
		Contact nigella = new ContactImpl(99, "Nigella","doesn't exist");
		List<Meeting> fml = cm.getFutureMeetingList(nigella);
	}
	/**
	 * Test getMeeting works as expected with a Future meeting
	 */
	@Test
	public void testGetMeetingWithFutureMeeting(){
		Calendar myCal = new GregorianCalendar(2015,3,2);
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,3,4));
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		assertTrue(cm.getMeeting(2).getDate().compareTo(myCal)==0);
	}
	/**
	 * Test getMeeting works as expected with a past meeting
	 */

	@Test
	public void testGetMeetingWithPastMeeting(){
		Calendar myCal = new GregorianCalendar(2012,4,2);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,5,5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,4,5));
		assertTrue(cm.getMeeting(3).getDate().compareTo(myCal)==0);
	}

	/**
	 * Test that getFutureMeeting correctly returns a FutureMeeting
	 */
	@Test
	public void testGetFutureMeetingInt(){
		Calendar myCal = new GregorianCalendar(2015,5,5);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012, 4, 2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 4, 5));
		assertTrue(cm.getFutureMeeting(2).getDate().compareTo(myCal)==0);
	}
 }

