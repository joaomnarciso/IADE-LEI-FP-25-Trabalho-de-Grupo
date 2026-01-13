import java.sql.Date;

public class Reservation {
    private int id;
    private int roomID;
    private int guestID;
    private int guestCount;
    private Date beginDate;
    private Date endDate;
    private boolean active;

    public static boolean CreateReservation(int guestID) {
        return true;
    }
}