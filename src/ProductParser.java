/**
 * Created by Joakim on 2016-06-09.
 */
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProductParser {
    public static void main(String [] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        StringBuilder xmlStringBuilder = new StringBuilder();
        try {
            xmlStringBuilder.append(readFile("produkter.xml", StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream input = null;
        Document doc = null;
        try {
            input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            doc = builder.parse(input);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = doc.getDocumentElement();
        NodeList item = root.getElementsByTagName("item");
        for(int i = 0; i<item.getLength(); i++){
            Node currentNode = item.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                String name = null, price = null, sku = null;
                Element e = (Element) item.item(i);
                name = e.getElementsByTagName("title").item(0).getTextContent();
                String status = e.getElementsByTagName("wp:status").item(0).getTextContent();
                if(status.equals("draft")){
                    name = "UTKAST | " + name;
                }
                NodeList metaElements = e.getElementsByTagName("wp:postmeta");
                for(int j = 0; j<metaElements.getLength(); j++){
                    Element metaElement = (Element) metaElements.item(j);
                    if(metaElement.getElementsByTagName("wp:meta_key").item(0).getTextContent().equals("_sku")){
                        price = metaElement.getElementsByTagName("wp:meta_value").item(0).getTextContent();
                    }
                    if(metaElement.getElementsByTagName("wp:meta_key").item(0).getTextContent().equals("_price")){
                        sku = metaElement.getElementsByTagName("wp:meta_value").item(0).getTextContent();
                    }
                }
                if(name != null && price != null && sku != null){
                    System.out.println(name + ";" + price + ";" + sku);
                }
            }
        }
        System.out.println(item.getLength());
    }
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
