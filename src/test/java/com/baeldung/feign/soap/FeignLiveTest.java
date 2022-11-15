package com.baeldung.feign.soap;

import feign.Feign;
import feign.Logger;
import feign.hc5.ApacheHttp5Client;
import feign.jaxb.JAXBContextFactory;
import feign.slf4j.Slf4jLogger;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.*;
import javax.xml.ws.soap.SOAPFaultException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FeignLiveTest {
    /*
    @Test
    void givenSOAPPayload_whenStringRequest_thenReturnSOAPResponse() {
        //@formatter:off
        String successMessage="Success! Created the user with id";
        SoapClient client = Feign.builder()
          .client(new ApacheHttp5Client())
          .logger(new Slf4jLogger(SoapClient.class))
          .logLevel(Logger.Level.FULL)
          .target(SoapClient.class, "http://localhost:18080/ws/users/");

        assertDoesNotThrow(() -> client.createUserWithPlainText(soapPayload()));

        String soapResponse= client.createUserWithPlainText(soapPayload());

        assertNotNull(soapResponse);
        assertTrue(soapResponse.contains(successMessage));
        //@formatter:on
    }

    @Test
    void whenSoapRequest_thenReturnSoapResponse() {
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();
        SoapClient client = Feign.builder()
          .encoder(new SOAPEncoder(jaxbFactory))
          .errorDecoder(new SOAPErrorDecoder())
          .logger(new Slf4jLogger())
          .logLevel(Logger.Level.FULL)
          .decoder(new SOAPDecoder(jaxbFactory))
          .target(SoapClient.class, "http://localhost:18080/ws/users/");
        CreateUserRequest request = new CreateUserRequest();

        User user = new User();
        user.setId("501");
        user.setName("John Doe");
        user.setEmail("john.doe@gmail");
        request.setUser(user);
        try {
            CreateUserResponse response = client.createUserWithSoap(request);
            assertNotNull(response);
            assertNotNull(response.getMessage());
            assertTrue(response.getMessage().contains("Success"));
        } catch (SOAPFaultException soapFaultException) {
            fail();
        }

    }

    @Test
    void whenSoapFault_thenThrowSOAPFaultException() {
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();
        SoapClient client = Feign.builder()
          .encoder(new SOAPEncoder(jaxbFactory))
          .errorDecoder(new SOAPErrorDecoder())
          .logger(new Slf4jLogger())
          .logLevel(Logger.Level.FULL)
          .decoder(new SOAPDecoder(jaxbFactory))
          .target(SoapClient.class, "http://localhost:18080/ws/users/");
        CreateUserRequest request = new CreateUserRequest();

        User user = new User();
        user.setId("500");
        user.setName("John Doe");
        user.setEmail("john.doe@gmail");
        request.setUser(user);
        try {
            client.createUserWithSoap(request);
        } catch (SOAPFaultException soapFaultException) {
            assertNotNull(soapFaultException.getMessage());
            assertTrue(soapFaultException.getMessage().contains("This is a reserved user id"));
        }

    }

    private String soapPayload() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:feig=\"http://www.baeldung.com/springbootsoap/feignclient\">\n" + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <feig:createUserRequest>\n"
          + "         <feig:user>\n" + "            <feig:id>1</feig:id>\n" + "            <feig:name>john doe</feig:name>\n" + "            <feig:email>john.doe@gmail.com</feig:email>\n" + "         </feig:user>\n" + "      </feig:createUserRequest>\n"
          + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
    }

    */


    @Test
    void name() {
    }

    @SneakyThrows
    @Test
    void insisFindPerson() {
        //@formatter:off
        String successMessage="FindPersonByPIDRs";
        SoapClient client = Feign.builder()
                .client(new ApacheHttp5Client())
                .logger(new Slf4jLogger(SoapClient.class))
                .logLevel(Logger.Level.FULL)
                .target(SoapClient.class, "http://10.1.4.201:9179/insisws/InsisPDMPort");

        assertDoesNotThrow(() -> client.createUserWithPlainText(findPersonSoapPayload()));

        String soapResponse= client.createUserWithPlainText(findPersonSoapPayload());

        //InputStream targetStream = new ByteArrayInputStream(findPersonSoapPayload().getBytes());
        StringReader input = new StringReader(soapResponse);
        BufferedReader bReader = new BufferedReader(input);
        String line;
        StringBuilder sb = new StringBuilder();

        while((line=bReader.readLine())!= null){
            sb.append(line.trim());
        }
        input = new StringReader(sb.toString());

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(input);

        FileWriter output = new FileWriter("out.json");
        XMLEvent previousElement = null;

        Integer level = 0;
        while (reader.hasNext()) {
            XMLEvent currentEvent = reader.nextEvent();
            if (currentEvent.isStartElement()) {
                StartElement startElement = currentEvent.asStartElement();
                String name = startElement.getName().getLocalPart();
                System.out.println("Start element name: " + name);
            }
            if (currentEvent.isEndElement()) {
                EndElement endElement = currentEvent.asEndElement();
                String name = endElement.getName().getLocalPart();
                System.out.println("End element name: " + name);
            }
            if (currentEvent.isCharacters()) {
                Characters characterElement = currentEvent.asCharacters();
                String data = characterElement.getData();
                System.out.println("Data element value: " + data);
            }

            if (previousElement == null) {
                output.write("{\n");
                level++;
            }

            if (currentEvent.isStartElement() && previousElement != null && previousElement.isStartElement()) {
                StartElement startElement = previousElement.asStartElement();
                String name = startElement.getName().getLocalPart();
                output.write(addTabs(level) + "\"" + name + "\"" + ": {\n");
                level++;
            }
            if (currentEvent.isCharacters() && previousElement != null && previousElement.isStartElement()) {
                StartElement startElement = previousElement.asStartElement();
                String name = startElement.getName().getLocalPart();
                Characters characterElement = currentEvent.asCharacters();
                String data = characterElement.getData();
                output.write(addTabs(level) + "\"" + name + "\"" + ": \"" + data + "\"");
            }
            if (currentEvent.isEndElement() && previousElement != null && previousElement.isEndElement()) {
                EndElement endElement = currentEvent.asEndElement();
                String name = endElement.getName().getLocalPart();
                level--;
                output.write("\n" + addTabs(level) +  "}");
            }

            if (currentEvent.isStartElement() && previousElement != null && previousElement.isEndElement()) {
                output.write(",\n");
            }


            //output.write("\n");
            previousElement = currentEvent;
        }
        output.write("\n}");
        output.close();


        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(sb.toString().getBytes()));
        doc.getDocumentElement().normalize();

        //NodeList nodeList = doc.getElementsByTagName("/env:Envelope/env:Header");
        //Node first = nodeList.item(0);

        //System.out.println("Length: " + nodeList.getLength());
        //System.out.println("Node type: " + first.getNodeType());
        //System.out.println("Node name: " + first.getNodeName());

        assertNotNull(soapResponse);
        assertTrue(soapResponse.contains(successMessage));
        //@formatter:on
    }

    private String findPersonSoapPayload() {
        return "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "\t<env:Header>\n" +
                "\t\t<wsse:Security env:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                "\t\t\t<wsse:UsernameToken wsu:Id=\"UsernameToken-3ACD3F1C6881D1B1B316509039777023\">\n" +
                "\t\t\t\t<wsse:Username>insis_gen_v10</wsse:Username>\n" +
                "\t\t\t\t<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">Uy-Dev2022</wsse:Password>\n" +
                "\t\t\t</wsse:UsernameToken>\n" +
                "\t\t</wsse:Security>\n" +
                "\t</env:Header>\n" +
                "\t<env:Body>\n" +
                "\t\t<FindPersonByPIDRq xmlns=\"http://www.fadata.bg/Insurance_Messages/v3.0/xml/\">\n" +
                "\t\t\t<PID>RUT6000001283</PID>\n" +
                "\t\t\t<EntityType>2</EntityType>\n" +
                "\t\t</FindPersonByPIDRq>\n" +
                "\t</env:Body>\n" +
                "</env:Envelope>";
    }

    private String addTabs(Integer level) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < level; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
