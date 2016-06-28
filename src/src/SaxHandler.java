package src;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class SaxHandler extends DefaultHandler {
    private List<Integer> banknotes = new ArrayList<Integer>();

    private String element = null;
    private Object object = null;

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        final String banknote = new String(ch, start, length);
        if (StringUtils.isNumeric(banknote)) {
            banknotes.add(Integer.parseInt(banknote));
        }
    }

    public List<Integer> getBanknotes() {
        for (Integer note : banknotes) {
            System.out.print(note + " ");
        }
        System.out.print("\n");
        return banknotes;
    }

    public Object getObject() {
        return object;
    }
}