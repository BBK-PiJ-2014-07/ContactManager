
public class ContactImpl implements Contact {
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(String name, String notes) {
		//how to generate a unique ID number? 
		//this.id = id;
		this.name = name;
		this.notes = notes;
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
		notes = notes + "; " +  note;
	}

}
