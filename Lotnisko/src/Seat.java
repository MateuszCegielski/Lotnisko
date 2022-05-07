import java.io.Serializable;
import java.util.Random;
/**
 * KLASA SEAT - jest to klasa odpowiadajaca za siedzenia, przechowuje informacje o nazwie i statstusie dostepnosci
 * pasazer - kazde siedzeie ma swojego uzytkownika, bylo to konieczne aby wiedzieć jakie loty zostaly zarezerwowane przez konkretne osoby
 *
 *            Ponizej znajduja sie elementarne funkcje i gettery ktore nie powinny wzbudzac watpliwosci :)
 */
public class Seat implements Serializable {
    private boolean reservation;
    private String nazwaMiejsca;
    Lot lot;
    User pasazer;

    /**
     *
     * @param lot - kazde siedzenie ma swoj lot.
     */

    public void setLot(Lot lot){
     this.lot = lot;
    }

    public void setNazwaMiejsca(String nazwaMiejsca) {
        this.nazwaMiejsca = nazwaMiejsca;
    }

    public String getNazwaMiejsca() {
        return nazwaMiejsca;
    }

    public boolean isReservation() {
        return reservation;
    }
    public boolean isFree() {
        return !reservation;
    }

    /**
     * W momencie utworzenia nowego siedzenia musimy przekazać jego nazwe, ktora jest automatycznie generowana poprzez rzad i miejsce do ktorego
     * przypisujemy dane siedzenie, ponad to kazde siedzenie ma swojego uzytkowanika (User) do ktorego nalezy miejsce.
     * Aby uwierygodnic nasza aplikacje napisalismy algorytm losujacy czy nowopowstałe miejsce bedzie wolne czy zajęte
     *
     */
    Seat(String nazwaMiejsca) {
        Random rand = new Random();
        this.reservation = rand.nextBoolean();
        if(isFree()){
            pasazer=new User();
        }else pasazer=null;
        this.nazwaMiejsca=nazwaMiejsca;
    }


    void ReserveSeat() {
        this.reservation = true;
        System.out.println("Your reservation is confirmed ");
    }

    void changeReservation() {
        this.reservation = !(this.reservation);
        System.out.println("Reservation has been changed ");
    }

    @Override
    public String toString() {
        return "Seat{" +
                "nazwaMiejsca='" + nazwaMiejsca + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (isReservation() != seat.isReservation()) return false;
        return getNazwaMiejsca().equals(seat.getNazwaMiejsca());
    }

    @Override
    public int hashCode() {
        int result = (isReservation() ? 1 : 0);
        result = 31 * result + getNazwaMiejsca().hashCode();
        return result;
    }
}
