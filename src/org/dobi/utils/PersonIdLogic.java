package org.dobi.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PersonIdLogic {
	private static final int IndexYearFirst = 0;
	private static final int IndexMonthFirst = 2;
	private static final int IndexDayFirst = 4;
	private static final int IndexGender = 8;
	private static final int IndexSummary = 9;
	private static final int IndexBirthNumberFirst = 6;
	private static final int LengthPersonId = 10;
	private static final int SummaryDivision = 11;
	private static final int[] idDigitWeights = new int[] { 2, 4, 8, 5, 10, 9, 7, 3, 6 };
	
	private String pId;
	private Date birthDate;
	private GenderEnum gender = GenderEnum.notavailable;
	private int birthNumber = 0;
	private int summaryDigit = 0;
	private PersonIdCheckStatus status;
	
	public PersonIdLogic(String id) {		
		pId = id;		
		status = readId();
		
		if (birthDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(1900, 1, 1);
			birthDate = cal.getTime();
		}
		
		if (pId == null)
			pId = "";
	}
	
	public String getId() {
		return pId;
	}
	
	public PersonIdCheckStatus getStatus() {
		return status;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public GenderEnum getGender() {
		return gender;
	}
	
	public int getBirthNumber() {
		return birthNumber;
	}
	
	public int getSummaryDigit() {
		return summaryDigit;
	}
	
	private PersonIdCheckStatus readId() {
		//EGNto trqbva da se sustoi ot to4no 10 simvola
		if (pId == null || pId.length() != LengthPersonId) {
			return PersonIdCheckStatus.WrongLength;
		}
		
		//tezi 10 simvola teqbva da sa cifri
		for (int k = 0; k < pId.length(); k++) {
			char c = pId.charAt(k);
			if (!Character.isDigit(c)) {
				return PersonIdCheckStatus.NotDigits;
			}
		}
				
		//vzimane na mesec
		String monthId = pId.substring(IndexMonthFirst, IndexMonthFirst+2);
		int monthActual = 0;
		
		if (monthId.charAt(0) == '0')
			monthActual = Character.getNumericValue(monthId.charAt(1));
		else
			monthActual = Integer.parseInt(monthId);
		
		//za sujalenie, dolnite proverki ne sa dostatu4ni za da e sigurno 4e ne e vuvedeno izmisleno EGN...
		//ako liceto e rodeno predi 01.01.1900, kum meseca se dobavq 20
		int yearBase = 0;
		if ((monthActual - 20) >= 1 && (monthActual - 20) <= 12) {
			yearBase = 1800;
			monthActual = monthActual - 20;
		}
		//ako liceto e rodeno sled 31.12.1999, kum meseca se dobavq 40
		else if ((monthActual - 40) >= 1 && (monthActual - 40) <= 12) {
			yearBase = 2000;
			monthActual = monthActual - 40;
		}
		else if (monthActual >= 1 && monthActual <= 12)
			yearBase = 1900;
		else {
			return PersonIdCheckStatus.InvalidDate;
		}
		
		//vzimane na godina
		String yearId = pId.substring(IndexYearFirst, IndexYearFirst+2);
		int yearIdInt = 0;
		
		if (yearId.charAt(0) == '0')
			yearIdInt = Character.getNumericValue(yearId.charAt(1));
		else
			yearIdInt = Integer.parseInt(yearId);
		
		int yearActual = yearBase + yearIdInt;
		
		//vzimane na den
		String dayId = pId.substring(IndexDayFirst, IndexDayFirst+2);
		int dayActual = 0;
		
		if (dayId.charAt(0) == '0')
			dayActual = Character.getNumericValue(dayId.charAt(1));
		else
			dayActual = Integer.parseInt(dayId);
		
		if (dayActual == 0 || dayActual > 31) {
			return PersonIdCheckStatus.InvalidDate;
		}
			
		//proverqvam dali izvlechenata data e validna data (primer - 31.02.1993 ne e validno i 6te grumne na getTime)
		//Bulgaria priema gregoriiskiq calendar prez WW1, a sistemata za EGN e vuvedena prez 1977, priemam 4e rojdenite dati predi 1916 sa bili konvertirani 
		GregorianCalendar birthDateTest = new GregorianCalendar();
		birthDateTest.setLenient(false);
		birthDateTest.set(yearActual, monthActual - 1, dayActual);
		try {
			birthDate = birthDateTest.getTime();
		}
		catch (Exception ex) {
			return PersonIdCheckStatus.InvalidDate;
		}
		
		//vzimam pol
		char genderId = pId.charAt(IndexGender);
		int genderIdInt = Character.getNumericValue(genderId);

		if((genderIdInt % 2) == 0)
			gender = GenderEnum.male;
		else
			gender = GenderEnum.female;

		//vzimam nomer na rajdane. Principno, ot tuk bih mogul da izvadq i grad na rajdane, no me murzi
		String birthNumberId = pId.substring(IndexBirthNumberFirst, IndexBirthNumberFirst+3);
		birthNumber = Integer.parseInt(birthNumberId);
		
		char idSumDigit = pId.charAt(IndexSummary);
		summaryDigit = Character.getNumericValue(idSumDigit);
		
		//validiram finalnata cifra ot EGNto
		int idCheckSummary = 0;
		for (int k = 0; k < pId.length() - 1; k++) {
			int idDigit = Character.getNumericValue(pId.charAt(k));
			idCheckSummary = idCheckSummary + (idDigit * idDigitWeights[k]);
		}
		int SummaryRem = idCheckSummary % SummaryDivision;
		
		if ((SummaryRem < 10 && summaryDigit == SummaryRem)
				|| (SummaryRem >= 10 && summaryDigit == 0)) {
			return PersonIdCheckStatus.Valid;
		}
		else {
			return PersonIdCheckStatus.WrongSummaryDigit;
		}
	}
}
