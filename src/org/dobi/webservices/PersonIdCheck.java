package org.dobi.webservices;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import org.dobi.utils.PersonIdLogic;
import org.w3c.dom.Document;

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
			
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			jaxbMarshaller.marshal(result, document);
			
			SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
			soapMessage.getSOAPBody().addDocument(document);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			soapMessage.writeTo(outputStream);
			
			xmlResult = new String(outputStream.toByteArray());
		}
		catch (Exception e) {
			xmlResult = "Service failed! Please, try again later...";
		}
		
		return xmlResult;		
	}
}
