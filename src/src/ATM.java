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
            System.out.println("Купюр такого наминала не существует\n");
            return false;
        }
        cashDump.put(denomination, cashDump.get(denomination) + num);
        totalAmonth += denomination * num;
        System.out.printf("Всего %s\n", totalAmonth);
        return true;
    }

    public void getMoney(int sumOfMoney) {
        if (sumOfMoney <= 0) {
            System.out.println("Введите корректную сумму");
        } else if (totalAmonth == 0) {
            System.out.println("Банкомат пуст");
        } else {
            final List<Integer> result = new ArrayList<>();
            final BestResult bestResult = getBanknotesCombination(result, sumOfMoney, 0);
            if (bestResult.getTotal() < sumOfMoney) {
                withdrawMoney(isEnoughBanknotes(bestResult.getResult()));
                if (bestResult.getTotal() == 0) {
                    totalAmonth -= sumOfMoney;
                    System.out.printf("Всего %s\n", sumOfMoney);
                } else if (bestResult.getTotal() < sumOfMoney) {
                    totalAmonth -= sumOfMoney - bestResult.getTotal();
                    System.out.printf("Всего %s\n", sumOfMoney - bestResult.getTotal());
                    System.out.printf("Без %s\n", bestResult.getTotal());
                }
            } else {
                System.out.println("Невозможно снять данную сумму");
            }
        }
    }

    private void withdrawMoney(TreeMap<Integer, Integer> toWithdraw) {
        if (toWithdraw != null) {
            for (Map.Entry<Integer, Integer> entry : toWithdraw.descendingMap().entrySet()) {
                System.out.printf("%s = %s,", entry.getKey(), entry.getValue());
                final int update = cashDump.get(entry.getKey()) - entry.getValue();
                cashDump.put(entry.getKey(), update);
            }
        }
    }

    private BestResult getBanknotesCombination(List<Integer> result, int total, int position) {
        final BestResult absolute = new BestResult(total, result);
        for (int i = position; i < cashDump.keySet().size(); i++) {
            final int curr = (int) cashDump.keySet().toArray()[i];
            if (total >= curr) {
                result.add(curr);
                if (isEnoughBanknotes(result) != null) {
                    final BestResult newCombination = getBanknotesCombination(result, total - curr, i);
                    assert newCombination != null;
                    if (newCombination.getTotal() >= 0 && newCombination.getTotal() < absolute.getTotal()) {
                        absolute.update(newCombination.getTotal(), newCombination.getResult());
                    }
                }
                result.remove(result.size() - 1);
            }
        }
        return absolute;
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

    public static class BestResult {
        private int total;
        private List<Integer> result;

        public BestResult(int total, List<Integer> res) {
            this.setTotal(total);
            this.setResult(res);
        }

        public void update(int tot, List<Integer> res) {
            this.setTotal(tot);
            this.setResult(res);
        }

        public List<Integer> getResult() {
            return result;
        }

        public void setResult(List<Integer> result) {
            this.result = new ArrayList<>(result);
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
