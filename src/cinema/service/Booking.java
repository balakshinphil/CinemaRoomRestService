package cinema.service;

import cinema.dictionary.ErrorMessages;
import cinema.model.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class Booking {
    private final Cinema cinema;

    @Autowired
    public Booking(Cinema cinema) {
        this.cinema = cinema;
    }

    public ResponseEntity<?> getCinemaSeatsInfo() {
        return new ResponseEntity<>(Map.of(
                "total_rows", cinema.getTotalRows(),
                "total_columns", cinema.getTotalColumns(),
                "available_seats", cinema.getAvailableSeats()
        ), HttpStatus.OK);
    }

    public ResponseEntity<?> buyTicket(int seatRow, int seatColumn) {
        if (!cinema.isSeatExists(seatRow, seatColumn)) {
            return new ResponseEntity<>(Map.of("error", ErrorMessages.OUT_OF_BOUNDS.toString()), HttpStatus.BAD_REQUEST);
        }

        if (!cinema.isSeatAvailable(seatRow, seatColumn)) {
            return new ResponseEntity<>(Map.of("error", ErrorMessages.ALREADY_PURCHASED.toString()), HttpStatus.BAD_REQUEST);
        }

        return cinema.buyTicket(seatRow, seatColumn);
    }

    public ResponseEntity<?> returnTicket(UUID token) {
        if (!cinema.isTicketExists(token)) {
            return new ResponseEntity<>(Map.of("error", ErrorMessages.WRONG_TOKEN.toString()), HttpStatus.BAD_REQUEST);
        }

        return cinema.returnTicket(token);
    }

    public ResponseEntity<?> getStatistics() {
        return new ResponseEntity<>(Map.of(
                "current_income", cinema.getCurrentIncome(),
                "number_of_available_seats", cinema.getNumberOfAvailableSeats(),
                "number_of_purchased_tickets", cinema.getNumberOfPurchasedTickets()
        ), HttpStatus.OK);
    }
}
