package com.nostyling.wcms.test.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import lombok.Data;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * @author shiliang
 * @Classname XStreamTest1
 * @Date 2021/7/6 11:39
 * @Description TODO
 */
public class XStreamTest1 {

    public static void main(String args[]) {
        // test1();
        test2();

    }

    private static void test2() {
        XStream xstream = new XStream();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("key1", "value1");
        stringStringHashMap.put("key2", "value2");
        stringStringHashMap.put("key3", "value3");
        stringStringHashMap.put("key4", "value4");
        //Object to XML Conversion
        String xml = xstream.toXML(stringStringHashMap);
        System.out.println(xml);
    }


    private static void test1() {
        XStreamTest1 tester = new XStreamTest1();
        XStream xstream = new XStream(new StaxDriver());

        Student student = tester.getStudentDetails();

        //Object to XML Conversion
        String xml = xstream.toXML(student);
        System.out.println(formatXml(xml));

        //XML to Object Conversion
        Student student1 = (Student) xstream.fromXML(xml);
        System.out.println(student1);
    }

    private Student getStudentDetails() {
        Student student = new Student();
        student.setFirstName("Mahesh");
        student.setLastName("Parashar");
        student.setRollNo(1);
        student.setClassName("1st");

        Address address = new Address();
        address.setArea("H.No. 16/3, Preet Vihar.");
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setPincode(110012);

        student.setAddress(address);
        return student;
    }


    public static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }


}

@Data
class Student {
    private String firstName;
    private String lastName;
    private int rollNo;
    private String className;
    private Address address;
}


@Data
class Address {
    private String area;
    private String city;
    private String state;
    private String country;
    private int pincode;
}

