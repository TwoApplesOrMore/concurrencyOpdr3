package messages;

public class PaymentResponse {
    private String seats;

    public PaymentResponse(String seats) {
        this.seats = seats;
    }

    public String getSeats() {
        return seats;
    }
}
