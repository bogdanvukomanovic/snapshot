package servent;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record Servent(int ID, String address, int port, List<Integer> neighbours) implements Serializable {

    @Serial
    private static final long serialVersionUID = -1956257782057819494L;

    @Override
    public String toString() {
        return "[" + ID + "|" + address + ":" + port + "]";
    }

}