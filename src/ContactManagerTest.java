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
		fm = new FutureMeetingImpl(contacts, new GregorianCalendar(2015,1,12));
		cm.addNewContact("Alan", "nice");
		cm.addNewContact("Sarah", "horrible");
	}

	@Test
	public void testAddFutureMeeting(){
		int newFutureMeetingId = cm.addFutureMeeting(contacts, new GregorianCalendar(2015,1,12));	//13 feb 2015
		assertEquals(fm.getId(), newFutureMeetingId);
	}

	@Test
	public void testGetContactsWithString(){
		Set<Contact> getCont = cm.getContacts("Alan");
		assertTrue(getCont.contains(alan));
	}

	@Test
	public void testGetContactsWithSingleId(){
		Set<Contact> getCont = cm.getContacts(1);
		assertTrue(getCont.contains(alan));
	}

	@Test
	public void testGetContactsWithMultipleIds(){
		Set<Contact> getCont = cm.getContacts(1,2);
		assertTrue(getCont.containsAll(contacts));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsWithIllegalId(){
		Set<Contact> getCont = cm.getContacts(12);
	}
/*
	Commented out, as ambiguous reference
	@Test(expected = NullPointerException.class)
	public void testGetContactsWithNullArg(){
		Set<Contact> getCont = cm.getContacts(null);
	}
	*/
	@Test
	public void testAddNewContact(){
		cm.addNewContact("Buffy", "vampire slayer");
		Set<Contact> getCont = cm.getContacts("Buffy");
		assertFalse(getCont.isEmpty());
	}

	@Test
	public void testNewPastMeeting() {
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,4,5), "board meeting");
		assertFalse(cm.getPastMeetingList().isEmpty());
	}

	@Test(expected= NullPointerException.class)
	public void testNewPastMeetingNullArg(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,6), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewPastMeetingIllegalContacts(){
		Set<Contact> badCont = new HashSet<Contact>();
		badCont.add(new ContactImpl(55, "Mary", "made up"));
		cm.addNewPastMeeting(badCont, new GregorianCalendar(2013,4,2), "this should break");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewPastMeetingEmptyContactSet(){
		Set<Contact> emptyCont = new HashSet<Contact>();
		cm.addNewPastMeeting(emptyCont, new GregorianCalendar(2014,02,2), "this should also break");
	}

	@Test
	public void testGetPastMeetingList(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,12,12), "test notes");
		List<PastMeeting> pml = cm.getPastMeetingList(alan);
		assertTrue(!pml.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPastMeetingListIllegalContact(){
		Contact nigella = new ContactImpl(99, "Nigella","doesn't exist");
		List<PastMeeting> pml = cm.getPastMeetingList(nigella);
	}
}

