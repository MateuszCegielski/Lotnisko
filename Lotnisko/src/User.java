import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa uzytkownika programu, ktory opisywany jest przez imie i nazwisko, posiada rowniez liste dokonanych rezerwacji
 */
public class User implements Serializable {
    String name = "";
    String surname = "";
    /**
     *  rezerwacje - lista zawierajaca zarezerwowane miejsca na dane loty
     */
    List<Seat> rezerwacje;

    /**
     * Konstruktor klasy USER
     * @param name - imie uzytkownika
     * @param surname - nazwisko tworzonego uzytkownika
     */
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.rezerwacje = new ArrayList<Seat>();
    }

    User() {

    }
}
