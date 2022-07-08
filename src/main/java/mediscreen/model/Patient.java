package mediscreen.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class Patient {
	private int id;
	private String family;
	private String given;
	@DateTimeFormat(iso = ISO.DATE)
	private Date dob;
	private String sex;
	private String address;
	private String phone;

	public Patient() {
	}

	public Patient(int id, String family, String given, Date dob, String sex, String address, String phone) {
		this.id = id;
		this.family = family;
		this.given = given;
		this.dob = dob;
		this.sex = sex;
		this.address = address;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGiven() {
		return given;
	}

	public void setGiven(String given) {
		this.given = given;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, dob, family, sex, given, phone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		return Objects.equals(address, other.address) && Objects.equals(dob, other.dob)
				&& Objects.equals(family, other.family) && sex == other.sex && Objects.equals(given, other.given)
				&& Objects.equals(phone, other.phone);
	}
}
