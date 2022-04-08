package cinema.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Cinema {
    private final int totalRows = 9;
    private final int totalColumns = 9;
    private final List<Seat> seats = new ArrayList<>();
    private final List<Ticket> soldTickets = new ArrayList<>();

    public Cinema() {
        initializeSeats();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream().filter(Seat::isAvailable).collect(Collectors.toList());
    }


    
    public boolean isSeatExists(int row, int column) {
        return seats.stream().anyMatch(seat -> seat.getRow() == row && seat.getColumn() == column);
    }

    public boolean isSeatAvailable(int row, int column) {
        return seats.stream()
                .filter(seat -> seat.getRow() == row && seat.getColumn() == column)
                .anyMatch(Seat::isAvailable);
    }

    public ResponseEntity<?> buyTicket(int row, int column) {
        Optional<Seat> seat = getSeatByRowColumn(row, column);

        if (seat.isEmpty()) {
            throw new NoSuchElementException();
        }

        seat.get().setAvailable(false);

        Ticket ticket = new Ticket(seat.get());
        soldTickets.add(ticket);

        return new ResponseEntity<>(Map.of(
                "token", ticket.getToken(),
                "ticket", seat.get()
        ), HttpStatus.OK);
    }

    public boolean isTicketExists(UUID token) {
        return soldTickets.stream().anyMatch(ticket -> Objects.equals(token, ticket.getToken()));
    }

    public ResponseEntity<?> returnTicket(UUID token) {
        Optional<Ticket> ticket = getTicketByToken(token);

        if (ticket.isEmpty()) {
            throw new NoSuchElementException();
        }

        Seat seat = ticket.get().getSeat();
        seat.setAvailable(true);
        soldTickets.remove(ticket.get());

        return new ResponseEntity<>(Map.of(
                "returned_ticket", seat
        ), HttpStatus.OK);
    }

    public int getCurrentIncome() {
        return soldTickets.stream().map(Ticket::getSeat).mapToInt(Seat::getPrice).sum();
    }

    public int getNumberOfAvailableSeats() {
        return getAvailableSeats().size();
    }

    public int getNumberOfPurchasedTickets() {
        return soldTickets.size();
    }



    private Optional<Ticket> getTicketByToken(UUID token) {
        return soldTickets.stream().filter(ticket -> Objects.equals(token, ticket.getToken())).findFirst();
    }

    private Optional<Seat> getSeatByRowColumn(int row, int column) {
        return seats.stream().filter(seat -> seat.getRow() == row && seat.getColumn() == column).findFirst();
    }

    private void initializeSeats() {
        for (int row = 1; row <= totalRows; row++) {
            for (int column = 1; column <= totalColumns; column++) {
                seats.add(new Seat(row, column));
            }
        }
    }
}
