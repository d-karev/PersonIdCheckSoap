package org.dobi.webservices;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="PersonIdData")
public class PersonIdCheckResponse {
	@XmlElement(name="Id")
	private String id;
	
	@XmlElement(name="BirthDate")
	private String birthDate;
	
	@XmlElement(name="Gender")
	private String gender;
	
	@XmlElement(name="BirthNumber")
	private int birthNumber;
	
	@XmlElement(name="SummaryDigit")
	private int summaryDigit;

	@XmlElement(name="Status")
	private String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getBirthNumber() {
		return birthNumber;
	}

	public void setBirthNumber(int birthNumber) {
		this.birthNumber = birthNumber;
	}

	public int getSummaryDigit() {
		return summaryDigit;
	}

	public void setSummaryDigit(int summaryDigit) {
		this.summaryDigit = summaryDigit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
