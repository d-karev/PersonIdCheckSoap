package org.dobi.webservices;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dobi.utils.PersonIdLogic;

@WebService
public class PersonIdCheck {
	public static final String formatDate = "dd-MM-yyyy";
	
	@WebMethod
	public String checkId(String personId) {
		PersonIdCheckResponse result = new PersonIdCheckResponse();
		
		PersonIdLogic pCheck = new PersonIdLogic(personId);
		
		result.setStatus(pCheck.getStatus().toString());
		result.setGender(pCheck.getGender().toString());
		result.setBirthNumber(pCheck.getBirthNumber());
		result.setSummaryDigit(pCheck.getSummaryDigit());
		result.setBirthDate(new SimpleDateFormat(formatDate).format(pCheck.getBirthDate()));
		result.setId(pCheck.getId());
		
		String xmlResult = "";
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(PersonIdCheckResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(result, sw);
			
			xmlResult = sw.toString();
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return xmlResult;		
	}
}
