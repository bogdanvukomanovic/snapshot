package snapshot;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionManager {

    private static AtomicInteger balance = new AtomicInteger(1000);

    public void add(int amount) {
        balance.getAndAdd(+amount);
    }

    public void subtract(int amount) {
        balance.getAndAdd(-amount);
    }

    public static int getCurrentBalance() {
        return balance.get();
    }

}
