package cinema.controller;

import cinema.dictionary.ErrorMessages;
import cinema.service.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
public class CinemaController {
    private final Booking booking;

    @Autowired
    public CinemaController(Booking booking) {
        this.booking = booking;
    }

    @GetMapping("/seats")
    public ResponseEntity<?> getSeats() {
        return booking.getCinemaSeatsInfo();
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> postPurchase(@RequestBody Map<String, Integer> seatData) {
        int seatRow = seatData.get("row");
        int seatColumn = seatData.get("column");

        return booking.buyTicket(seatRow, seatColumn);
    }

    @PostMapping("/return")
    public ResponseEntity<?> postReturn(@RequestBody Map<String, UUID> tokenData) {
        UUID token = tokenData.get("token");

        return booking.returnTicket(token);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> postStats(@RequestParam(value = "password", required = false) String password) {
        if (!isPasswordValid(password)) {
            return new ResponseEntity<>(Map.of("error", ErrorMessages.WRONG_PASSWORD.toString()), HttpStatus.UNAUTHORIZED);
        }

        return booking.getStatistics();
    }



    private boolean isPasswordValid(String password) {
        return password != null && password.hashCode() == -1910141964;// Password: super_secret
    }
}
