package snapshot;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public record Snap(Map<Integer, Integer> sent, Map<Integer, Integer> received, Integer currentBalance) implements Serializable {

    @Serial
    private static final long serialVersionUID = 6538898188930669737L;

}