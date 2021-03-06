import org.junit.*;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Unit tests for ContactManagerImpl.
 * @see ContactManager
 */

public class ContactManagerTest {
	private ContactManager cm;
	private Set<Contact> contacts;
	private Contact alan;
	private Contact sarah;
	private Calendar todaysDate;

	/**
	 * Method to run once at start of tests to back up existing contacts.txt.
	 * Contacts.txt is copied to contactsCOPY.txt, and deleted
	 * When all tests are finished, file is copied back, and copy is deleted.
	 */
	@BeforeClass
	public static void backupContactsTxt(){
		try {
			Files.copy(Paths.get("contacts.txt"),Paths.get("contactsCOPY.txt"));
			Files.delete(Paths.get("contacts.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Before
	public void buildUp() throws IOException {
		cm = new ContactManagerImpl();
		contacts = new HashSet<Contact>();
		alan = new ContactImpl(1, "Alan", "nice");
		contacts.add(alan);
		sarah = new ContactImpl(2, "Sarah", "horrible");
		contacts.add(sarah);
		cm.addNewContact("Alan", "nice");
		cm.addNewContact("Sarah", "horrible");

		todaysDate = new GregorianCalendar();

	}

	/**
	 * Test that objects are serialized and deserialized correctly
	 */
	@Test
	public void testSerialization() throws IOException{
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream objOutput = new ObjectOutputStream(byteOutput);
		objOutput.writeObject(alan);
		objOutput.close();
		ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteOutput.toByteArray()));
		Contact readCont = null;
		try {
			readCont = (Contact) objInput.readObject();
		} catch (ClassNotFoundException ex){
			ex.printStackTrace();
		}
		assertEquals(readCont,alan);
	}

	/**
	 * Test that output file exists
	 */
	@Test
	public void testOutputFileExists(){
		File outputFile = new File("contacts.txt");
		assertTrue(outputFile.exists());
	}
	/**
	 * Test that output file contains correct contacts
	 */
	@Test
	public void testOutputContacts() throws IOException {
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting");
		cm.flush();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.txt"));
		List inputData = null;
		try {
			inputData = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}

		List<Contact> inputContactList = (ArrayList) inputData.get(2);

		assertEquals(inputContactList.get(0), alan);
	}

	/**
	 * Test that output file contains correct future meetings
	 */
	@Test
	public void testOutputFutureMeetings() throws IOException {
		Calendar thisDate = new GregorianCalendar(2015,3,2);
		cm.addFutureMeeting(contacts, thisDate);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting");
		cm.flush();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.txt"));
		List inputData = null;
		try {
			inputData = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}

		List<Meeting> futureMeetingList = (ArrayList) inputData.get(0);

		assertTrue(futureMeetingList.get(0).getDate().compareTo(thisDate) == 0);
	}

	/**
	 * Test that output file contains correct past meetings
	 */
	@Test
	public void testOutputPastMeetings() throws IOException {
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting one");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,6,3), "meeting two");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,7,3), "meeting three");

		cm.flush();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.txt"));
		List inputData = null;
		try {
			inputData = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}

		List<PastMeeting> pastMeetingList = (ArrayList) inputData.get(1);

		assertEquals(pastMeetingList.get(1).getNotes(), "meeting two");
	}


	/**
	 * Test that ContactManager can actually read contacts from an existing file
	 */
	@Test
	public void testCMExistingFileContacts(){
		ContactManager cm1 = new ContactManagerImpl();
		cm1.flush();
		assertTrue(cm1.getContacts("Alan").contains(alan));
	}

	/**
	 * Test that ID numbers are successfully retained and updated
	 */
	@Test
	public void testIDNumbersInputFile(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting one");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,6,3), "meeting two");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,7,3), "meeting three");
		ContactManager cm1 = new ContactManagerImpl();
		cm1.addNewPastMeeting(contacts, new GregorianCalendar(2012,4,5), "meeting four");
		cm1.flush();
		assertEquals(cm1.getPastMeeting(4).getNotes(), "meeting four");

	}

	/**
	 * Test that ContactManager can actually read future meetings from an existing file
	 */
	@Test
	public void testCMExistingFileFutureMeeting(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		ContactManager cm1 = new ContactManagerImpl();
		assertEquals(cm1.getFutureMeetingList(new GregorianCalendar(2015,3,2)).get(0).getId(),1);
	}

	/**
	 * Test that ContactManager can actually read past meetings from an existing file
	 */
	@Test
	public void testCMExistingFilePastMeeting(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting one");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,6,3), "meeting two");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,7,3), "meeting three");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		ContactManager cm1 = new ContactManagerImpl();
		assertEquals(cm1.getPastMeeting(2).getNotes(), "meeting two");
	}



	/**
	 * Test that ContactManager is actually writing something to file, and check it's the right class
	 */
	@Test
	public void testClassOfObjectWrittenToFile() throws IOException{
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.txt"));
		List inputData = null;
		try {
			inputData = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		assertTrue(inputData.get(0).getClass().equals(inputData.getClass())); //check that the object is a List
	}

	/**
	 * Test that the List written to file contains the right number of objects
	 */
	@Test
	public void testSizeOfListInFile() throws IOException {
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,2));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,3), "meeting");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.txt"));
		List inputData = null;
		try {
			inputData = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(3, inputData.size());
	}
	/**
	 * Test that addFutureMeeting works as expected
	 */
	@Test
	public void testAddFutureMeeting(){
		int newFutureMeetingId = cm.addFutureMeeting(contacts, new GregorianCalendar(2015,1,12));	//13 feb 2015
		assertTrue(newFutureMeetingId == 1);
	}

	/**filter(m -> m.getDate().get(1) - date.get(1) ==0).filter(m -> m.getDate().get(2) - date.get(2) ==0).filter(m -> m.getDate().get(5) - date.get(5)==0).
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
	 * Test that getContacts(String) doesn't give anything if contact not found
	 */
	@Test
	public void testGetContactsEmptyString(){
		Set<Contact> getCont = cm.getContacts("");
		assertTrue(getCont.isEmpty());
	}

	@Test
	public void testGetContactsBadName(){
		Set<Contact> getCont = cm.getContacts("Gandalf");
		assertTrue(getCont.isEmpty());
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
	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsWithIDZero(){
		Set<Contact> getCont = cm.getContacts(0);
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
	 * Test normal functionality of addMeetingNotes() - this is causing problems
	 */
	//TODO
/*
	@Test
	public void testAddMeetingNotesConvertsToPastMeeting() {
		PastMeeting examplePastMeeting = new PastMeetingImpl(3, contacts, new GregorianCalendar(2013,2,5), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2014,11,17,16,32));
		cm.addMeetingNotes(1, "now a past meeting");
		assertTrue(cm.getMeeting(1).getClass()==examplePastMeeting.getClass());
	}
*/
	@Test
	public void testAddMeetingNotesActuallyAddsMeetingNotes(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013, 4, 5), "board meeting");
		cm.addMeetingNotes(1, "rescheduled");
		assertEquals(cm.getPastMeeting(1).getNotes(),"board meeting, rescheduled");
	}

	/**
	* Test that passing addMeetingNotes() a meeting id that doesn't exist throws an
	 * IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddMeetingNotesWithIllegalMeeting(){
		cm.addFutureMeeting(contacts, todaysDate);
		cm.addMeetingNotes(12,"illegal meeting");
	}

	/**
	 * Test that passing a meeting with a future date into addMeetingNotes() throws an IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testAddMeetingNotesWithFutureDate(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,11,12));
		cm.addMeetingNotes(1,"illegal date");
	}

	/**
	 * Test that addMeetingNotes() throws a NullPointerException if notes are null
	 */
	@Test(expected = NullPointerException.class)
	public void testAddMeetingNotesWithNullNotes(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013, 4, 5), "board meeting");
		cm.addMeetingNotes(1,null);
	}
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
	 * Test that addNewContact throws null pointer exception with null name or notes
	 */
	@Test(expected = NullPointerException.class)
	public void testAddNewContactNullName(){
		cm.addNewContact(null, "notes");
	}

	@Test(expected = NullPointerException.class)
	public void testAddNewContactNullNotes(){
		cm.addNewContact("Name", null);
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
	public void testNewPastMeetingNullNotes(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,6), null);
	}
	@Test(expected= NullPointerException.class)
	public void testNewPastMeetingNullContacts(){
		cm.addNewPastMeeting(null, new GregorianCalendar(2013,5,6), "notes");
	}
	@Test(expected= NullPointerException.class)
	public void testNewPastMeetingNullDate(){
		cm.addNewPastMeeting(contacts, null, "notes");
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
	 * Test that getFutureMeetingList(Calendar date) works as expected with a future date
	 */
	@Test
	public void testGetFutureMeetingListFutureDate(){
		Calendar myCal = new GregorianCalendar(2016,3,4);
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,3,4));
		List<Meeting> fml = cm.getFutureMeetingList(myCal);
		assertTrue(fml.get(0).getDate().compareTo(myCal)==0);
	}
	/**
	 * Test that getFutureMeetingList(Calendar date) works as expected with a past date
	 */
	@Test
	public void testGetFutureMeetingListPastDate(){
		Calendar myCal = new GregorianCalendar(2013,3,4);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013, 3, 4), "meeting one");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,3,4), "meeting two");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,5,2), "meeting three");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,6));
		List<Meeting> pastList = cm.getFutureMeetingList(myCal);
		assertTrue(pastList.get(0).getDate().compareTo(myCal)==0);
	}

	/**
	 * Test that getFutureMeetingList(Calendar date) sorts lists chronologically
	 */
	@Test
	public void testGetFutureMeetingListChronologicalSort(){
		Calendar myCal = new GregorianCalendar(2013,3,4);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013, 3, 4,13,0), "meeting two");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,3,4,11,0), "meeting one");
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2013,3,4,19,30), "meeting three");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,3,6));
		List<Meeting> pastList = cm.getFutureMeetingList(myCal);
		PastMeeting pm = (PastMeeting) pastList.get(0);
		assertEquals(pm.getNotes(), "meeting one");
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
	 * Test that getFutureMeetingList(Date date) works with a past meeting)
	 *
	 */
	@Test
	public void testGetFutureMeetingListPastMeetingsDate(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2016,3,4));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2014,5,3), "past meeting");
		List<Meeting> fml = cm.getFutureMeetingList(new GregorianCalendar(2014,5,3));
		assertTrue(fml.size()==1);
	}

	/**
	 * Test that chronological sort works
	 */
	@Test
	public void testChronologicalSort(){
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 3, 4, 14, 30));
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 3, 4, 16, 30));
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 3, 4, 8, 30));
		List<Meeting> fml = cm.getFutureMeetingList(alan);
		assertEquals(fml.get(1).getId(),1);


	}

	/**
	 * Test that init with an empty contacts.txt won't break ContactManager
	 */
	@Test
	public void testEmptyContactsTxtInit() throws IOException {
		Files.delete(Paths.get("contacts.txt"));
		Files.createFile(Paths.get("contacts.txt"));
		ContactManager cm1 = new ContactManagerImpl();
		Contact fred = new ContactImpl(1, "Fred", "notes");
		cm1.addNewContact("Fred", "notes");
		assertTrue(cm1.getContacts(1).contains(fred));
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
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012,4,2), "third meeting");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015,4,5));
		assertTrue(cm.getMeeting(3).getDate().compareTo(myCal)==0);
	}

	/**
	 * Test that getFutureMeeting correctly returns a FutureMeeting
	 */
	@Test
	public void testGetFutureMeeting(){
		Calendar myCal = new GregorianCalendar(2015,5,5);
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012, 4, 2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 4, 5));
		assertTrue(cm.getFutureMeeting(2).getDate().compareTo(myCal)==0);
	}



	/**
	 * Test that getFutureMeeting(int) throws an IllegalArgumentException when given
	 * the ID of a past meeting
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetFutureMeetingWithPastMeeting(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012, 4, 2), "test");
		cm.getFutureMeeting(3);
	}

	/**
	 * Test that getFutureMeeting(int) returns null if meeting id does not exist
	 */
	@Test
	public void testGetFutureMeetingReturnsNull(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012, 4, 2), "test");
		assertNull(cm.getFutureMeeting(6));
	}

	/**
	 * Test that getPastMeeting(int id) correctly returns a past meeting
	 */
	@Test
	public void testGetPastMeeting(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2012, 4, 2), "correct meeting");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 4, 5));
		assertEquals(cm.getPastMeeting(3).getNotes(), "correct meeting");
	}

	/**
	 * Test that getPastMeeting throws an IllegalArgumentException if passed the ID of a future meeting
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPastMeetingWithFutureMeeting(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		cm.getPastMeeting(2);
	}

	/**
	 * Test that getPastMeeting returns null if ID doesn't exist
	 */
	@Test
	public void testGetPastMeetingReturnsNull(){
		cm.addNewPastMeeting(contacts, new GregorianCalendar(2011,4,2), "test");
		cm.addFutureMeeting(contacts, new GregorianCalendar(2015, 5, 5));
		assertNull(cm.getPastMeeting(88));
	}

	@After
	public void tearDown(){
		cm.flush();
		try {
			Files.delete(Paths.get("contacts.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to be run at very end after all tests have completed, to restore original contacts.txt
	 */
	@AfterClass
	public static void restoreContactsTxt(){
		try {
			Files.copy(Paths.get("contactsCOPY.txt"),Paths.get("contacts.txt"));
			Files.delete(Paths.get("contactsCOPY.txt"));
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

}

