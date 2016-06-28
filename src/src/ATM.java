package src;


import java.util.*;

public class ATM {
    private int totalAmonth;
    private TreeMap<Integer, Integer> cashDump;

    public ATM(List<Integer> banknotes) {
        totalAmonth = 0;
        cashDump = new TreeMap<>();
        for (Integer note : banknotes) {
            cashDump.put(note, 0);
        }
    }

    public boolean putMoney(int denomination, int num) {
        if (!cashDump.containsKey(denomination)) {
            System.out.printf("Купюр такого наминала не существует\n");
            return false;
        }
        cashDump.put(denomination, cashDump.get(denomination) + num);
        totalAmonth += denomination * num;
        System.out.printf("Всего %s\n", totalAmonth);
        return true;
    }

    public void getMoney(int sumOfMoney) {
        if (sumOfMoney <= 0) {
            System.out.printf("Введите корректную сумму");
        } else if (totalAmonth <= sumOfMoney) {
            for (Map.Entry<Integer, Integer> entry : cashDump.descendingMap().entrySet()) {
                if (entry.getValue() > 0) {
                    System.out.printf("%s = %s,", entry.getKey(), entry.getValue());
                    cashDump.put(entry.getKey(), 0);
                }
            }
            System.out.printf("Всего %s\n", sumOfMoney);
            totalAmonth -= sumOfMoney;
            if (totalAmonth < 0) {
                System.out.printf("Без %s\n", sumOfMoney - totalAmonth);
                totalAmonth = 0;
            }
        } else {
            final List<Integer> result = new ArrayList<>();
            if (getBanknotesCombination(result, sumOfMoney, 0)) {
                System.out.printf("Всего %s\n", sumOfMoney);
                totalAmonth -= sumOfMoney;
            } else {
                System.out.printf("Невозможно снять данную сумму");
            }
        }
    }

    private boolean getBanknotesCombination(List<Integer> result, int total, int position) {
        if (total == 0) {
            final TreeMap<Integer, Integer> toWithdraw = isEnoughBanknotes(result);
            if (toWithdraw != null) {
                for (Map.Entry<Integer, Integer> entry : toWithdraw.descendingMap().entrySet()) {
                    System.out.printf("%s = %s\n", entry.getKey(), entry.getValue());
                    final int update = cashDump.get(entry.getKey()) - entry.getValue();
                    cashDump.put(entry.getKey(), update);
                }
                return true;
            }
        }
        for (int i = position; i < cashDump.keySet().size(); i++) {
            final int curr = (int) cashDump.keySet().toArray()[i];
            if (total >= curr) {
                result.add(curr);
                if (isEnoughBanknotes(result) != null && getBanknotesCombination(result, total - curr, i)) return true;
                result.remove(result.size() - 1);
            }
        }
        return false;
    }

    private TreeMap<Integer, Integer> isEnoughBanknotes(List<Integer> result) {
        final TreeMap<Integer, Integer> toWithdraw = new TreeMap<>();
        if (result.isEmpty()) return null;
        if (result.size() == 1) {
            if (1 > cashDump.get(result.get(0))) return null;
            else {
                toWithdraw.put(result.get(0), 1);
                return toWithdraw;
            }
        }
        int denominationCounter = 0;
        for (int i = 0; i + 1 < result.size(); ++i) {
            denominationCounter++;
            if (!Objects.equals(result.get(i), result.get(i + 1))) {
                if (denominationCounter > cashDump.get(result.get(i))) return null;
                toWithdraw.put(result.get(i), denominationCounter);
                denominationCounter = 0;
            }
        }
        denominationCounter++;
        if (denominationCounter > cashDump.get(result.get(result.size() - 1))) return null;
        toWithdraw.put(result.get(result.size() - 1), denominationCounter);
        return toWithdraw;
    }

    public void getDump() {
        for (Map.Entry<Integer, Integer> entry : cashDump.descendingMap().entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }
    }

    public void getState() {
        System.out.printf("Всего %s\n", totalAmonth);
    }

}
