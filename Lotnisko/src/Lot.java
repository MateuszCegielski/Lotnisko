import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa lot, umozliwiajaca tworzenie obiektow-lotow, opisywane przez zespol cech
 */
public class Lot implements Serializable, ListSelectionListener {
    LocalTime czasOdlotu;
    String nrRejsu;
    String status;
    String gate;
    String liniaLotnicza;
    Samolot samolot;

    /**
     *
     * @param nrRejsu - nr rejsu lotu
     * @param status - status lotu
     * @param gate - bramka, z ktorej odbywa sie wejscie na samolot
     * @param liniaLotnicza - linia lotnicza lotu
     * @param kierunek - kierunek, do ktorego zmierza lot
     * @param terminal - terminal, z ktorego wylatuje lot
     * @param samolot - obiekt samolotu, zawierajacy siedzenia, opisywany przez jego wielkosc
     */
    public Lot(String nrRejsu, String status, String gate, String liniaLotnicza, String kierunek, String terminal,Samolot samolot) {
        this.nrRejsu = nrRejsu;
        this.status = status;
        this.gate = gate;
        this.liniaLotnicza = liniaLotnicza;
        this.kierunek = kierunek;
        this.terminal = "Strefa " + terminal;
        this.samolot = samolot;
        for (Seat seat : this.samolot.listaMiejsc ) {
            seat.setLot(this);
        }
    }

    String kierunek;
    String terminal;

    public Lot(LocalTime czasOdlotu, String nrRejsu, String status, String gate, String liniaLotnicza, String kierunek, String terminal,Samolot samolot) {
        this.czasOdlotu = LocalTime.parse(czasOdlotu.format(DateTimeFormatter.ofPattern("HH:mm")));
        this.nrRejsu = nrRejsu;
        this.status = status;
        this.gate = gate;
        this.liniaLotnicza = liniaLotnicza;
        this.kierunek = kierunek;
        this.terminal = "Strefa " + terminal;
        this.samolot = samolot;
    }

    public String getNrRejsu() {
        return nrRejsu;
    }

    public void setNrRejsu(String nrRejsu) {
        this.nrRejsu = nrRejsu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getLiniaLotnicza() {
        return liniaLotnicza;
    }

    public void setLiniaLotnicza(String liniaLotnicza) {
        this.liniaLotnicza = liniaLotnicza;
    }

    public String getKierunek() {
        return kierunek;
    }

    public void setKierunek(String kierunek) {
        this.kierunek = kierunek;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "czasOdlotu=" + czasOdlotu +
                ", nrRejsu='" + nrRejsu + '\'' +
                ", status='" + status + '\'' +
                ", gate='" + gate + '\'' +
                ", liniaLotnicza='" + liniaLotnicza + '\'' +
                ", kierunek='" + kierunek + '\'' +
                ", terminal='" + terminal + '\'' +
                ", samolot='" + samolot + '\'' +
                '}';
    }

    public String toString2() {
        return "Lot{" +
                "nrRejsu='" + nrRejsu + '\'' +
                ", status='" + status + '\'' +
                ", gate='" + gate + '\'' +
                ", liniaLotnicza='" + liniaLotnicza + '\'' +
                ", kierunek='" + kierunek + '\'' +
                ", terminal='" + terminal + '\'' +
                ", samolot='" + samolot + '\'' +
                '}';
    }

    /**
     * Funkcja wyswietlająca szczegoly lotu, wykorzystywana w GUI
     */
    public String wyswietlLot()
    {
        return "Lot o "+
                "numerze rejsu '" + nrRejsu + '\'' +
                ", linii lotniczej  '" + liniaLotnicza + '\'' +
                " do " + kierunek + '\'' +
                "z terminalu '" + terminal + '\'' ;
    }

    public LocalTime getCzasOdlotu() {
        return czasOdlotu;
    }

    public void setCzasOdlotu(LocalTime czasOdlotu) {
        this.czasOdlotu = czasOdlotu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lot lot = (Lot) o;

        if (!getCzasOdlotu().equals(lot.getCzasOdlotu())) return false;
        if (!getNrRejsu().equals(lot.getNrRejsu())) return false;
        if (!getStatus().equals(lot.getStatus())) return false;
        if (!getGate().equals(lot.getGate())) return false;
        if (!getLiniaLotnicza().equals(lot.getLiniaLotnicza())) return false;
        if (!samolot.equals(lot.samolot)) return false;
        if (!getKierunek().equals(lot.getKierunek())) return false;
        return getTerminal().equals(lot.getTerminal());
    }

    @Override
    public int hashCode() {
        int result = getCzasOdlotu().hashCode();
        result = 31 * result + getNrRejsu().hashCode();
        result = 31 * result + getStatus().hashCode();
        result = 31 * result + getGate().hashCode();
        result = 31 * result + getLiniaLotnicza().hashCode();
        result = 31 * result + samolot.hashCode();
        result = 31 * result + getKierunek().hashCode();
        result = 31 * result + getTerminal().hashCode();
        return result;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
