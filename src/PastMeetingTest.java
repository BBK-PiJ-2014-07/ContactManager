import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.*;
import static org.junit.Assert.*;

public class PastMeetingTest {
	private PastMeeting pm;

	@Before
	public void buildUp(){
		Calendar cal = new GregorianCalendar(2011,11,01);
		pm = new PastMeetingImpl(cal, "Meeting notes");
	}
	
	@Test
	public void testGetId(){
		assertEquals(pm.getId(), 1);
	}
	
	@Test
	public void testGetDate(){
		Calendar newCal = pm.getDate();
		assertEquals(newCal.get(newCal.YEAR),2011);
	}
	
}
