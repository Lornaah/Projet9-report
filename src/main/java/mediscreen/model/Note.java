package mediscreen.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class Note {
	String id;
	String note;
	int patId;
	@DateTimeFormat(iso = ISO.DATE)
	Date date;

	public Note() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getPatientId() {
		return patId;
	}

	public void setPatientId(int patientId) {
		this.patId = patientId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, note, patId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		return Objects.equals(date, other.date) && id == other.id && Objects.equals(note, other.note)
				&& patId == other.patId;
	}

	@Override
	public String toString() {
		return "NoteDTO [id=" + id + ", note=" + note + ", patientId=" + patId + ", date=" + date + "]";
	}

}
