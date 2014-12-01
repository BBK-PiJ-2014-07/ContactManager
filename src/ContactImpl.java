
public class ContactImpl implements Contact {
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(int id, String name, String notes) {
		this.name = name;
		this.notes = notes;
		this.id = id;
	}
	

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	
	public String getNotes() {
		return notes;
	}

	public void addNotes(String note) {
		//concatenate old notes with new notes
		notes = notes + ", " +  note;
	}

}
