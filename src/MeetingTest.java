import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;

import static org.junit.Assert.*;

public class MeetingTest {
	private Meeting meet;
	private Set<Contact> contactSet;

	@Before
	public void buildUp(){
		Contact alan = new ContactImpl(1, "Alan", "Likes cheese");
		Contact sarah = new ContactImpl(2, "Sarah", "Lives in London");
		contactSet = new HashSet<Contact>();
		contactSet.add(alan);
		contactSet.add(sarah);
		Calendar cal = new GregorianCalendar(2011,11,01);
		meet = new MeetingImpl(contactSet, cal);
	}
	
	@Test
	public void testGetId(){
		assertEquals(meet.getId(), 1);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetDate(){
		Calendar newCal = meet.getDate();
		assertEquals(newCal.get(newCal.YEAR),2011);
	}

	@Test
	public void testGetContacts(){
		Set<Contact> newSet = meet.getContacts();
		assertTrue(newSet.containsAll(contactSet));
	}
}
