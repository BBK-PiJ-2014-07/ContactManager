import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.*;
import static org.junit.Assert.*;

public class PastMeetingTest {
	private PastMeeting pm;

	@Before
	public void buildUp(){
		Calendar cal = new GregorianCalendar(2013,12,01);
		pm = new PastMeetingImpl(cal, "Meeting notes");
	}
	

}
