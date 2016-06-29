package src;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class SaxHandler extends DefaultHandler {
    private List<Integer> banknotes = new ArrayList<>();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        final String banknote = new String(ch, start, length);
        if (StringUtils.isNumeric(banknote)) {
            banknotes.add(Integer.parseInt(banknote));
        }
    }

    public List<Integer> getBanknotes() {
        return banknotes;
    }

}