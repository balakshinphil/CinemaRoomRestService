package cinema.model;

import java.util.UUID;

public class Ticket {
    private final UUID token;
    private final Seat seat;

    public Ticket(Seat seat) {
        this.token = UUID.randomUUID();
        this.seat = seat;
    }

    public UUID getToken() {
        return token;
    }

    public Seat getSeat() {
        return seat;
    }
}
