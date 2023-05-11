package snapshot;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionManager {

    private final AtomicInteger balance = new AtomicInteger(1000);

    public void add(int amount) {
        /* ... */
    }

    public void subtract(int amount) {
        /* ... */
    }

}
