package src;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.List;

public class Main {
    private static ATM atm;

    private static List<Integer> readBanknotesType(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF8"))) {

            final StringBuilder stringBuilder = new StringBuilder();
            for (String s = ""; (s = bufferedReader.readLine()) != null; ) {
                stringBuilder.append(s);
            }

            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser saxParser = factory.newSAXParser();

            final SaxHandler handler = new SaxHandler();
            saxParser.parse(new InputSource(new StringReader(stringBuilder.toString())), handler);
            return handler.getBanknotes();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("OverlyComplexMethod")
    private static boolean interpreter(String command, boolean hasRequestedQuit) {
        final String[] allCommand = command.split(" ");
        System.out.println("> " + command);
        switch (allCommand[0]) {
            case "put":
                if (allCommand.length == 3 && StringUtils.isNumeric(allCommand[1])
                        && StringUtils.isNumeric(allCommand[2])) {
                    try {
                        atm.putMoney(Integer.parseInt(allCommand[1]), Integer.parseInt(allCommand[2]));
                    } catch (NumberFormatException e) {
                        System.out.println("Введенно неверное число");
                    }
                } else {
                    System.out.println("Неверно введены данные");
                }
                break;
            case "get":
                if (allCommand.length == 2 && StringUtils.isNumeric(allCommand[1])) {
                    try {
                        atm.getMoney(Integer.parseInt(allCommand[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Введенно неверное число");
                    }
                } else {
                    System.out.println("Неверно введена сумма");
                }
                break;
            case "dump":
                atm.getDump();
                break;
            case "state":
                atm.getState();
                break;
            case "quit":
                System.out.println("Выход из банкомата");
                return true;
            default:
                System.out.println("Введенной команды не существует");
                break;
        }
        return false;
    }

    public static void main(String[] args) {
        final String filename = "banknotes.xml";
        final List<Integer> banknotes = readBanknotesType(filename);
        if (banknotes == null) {
            System.out.println("Невозможно инициализировать банкноты");
            return;
        }
        atm = new ATM(banknotes);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean hasRequestedQuit = false;
            while (!hasRequestedQuit) {
                final String command = reader.readLine();
                hasRequestedQuit = interpreter(command, hasRequestedQuit);
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
