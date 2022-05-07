import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Klasa samolotu, ktorej obiekty sa przypisywane do kazdego lotu. Obiekty o losowanej wielkosci, zawierajace liste miejsc
 */
public class Samolot implements Serializable {
    private final int nr_wielkosci;
    String wielkosc;
    private int rzad;
    private int miejsce;
    ArrayList<Seat>listaMiejsc = new ArrayList<>();
    String literkiRzedow="ABCDEFGHIJKLMNOP";

    /**
     * KLASA SAMOLOT - kazdy lot ma przypisany samolot o losowanej wielkosci
     *
     * listaMiejsc - wszyskie miejsca na dany lot zapisywane są do tej listy, ułatwia to wyszukiwanie i modyfikacje miejsc
     * @param nr_wielkosci - opisuje wielkosc samolotu, przewidzielismy trzy. Tutaj przypisywane są samolotom miejsca (seat)i ich nazwy
     */
    public Samolot(int nr_wielkosci) {
        this.nr_wielkosci = nr_wielkosci;
        switch (nr_wielkosci) {
            case 1 -> {
                this.wielkosc = "mały";
                this.rzad = 8;
                this.miejsce = 4;
                for (int i = 0; i < rzad; i++) {
                    for (int j = 0; j < miejsce; j++) {
                        StringBuilder nazwaSiedzenia= new StringBuilder();
                        nazwaSiedzenia.append(literkiRzedow.charAt(i));
                        nazwaSiedzenia.append(j+1);
                        listaMiejsc.add(new Seat(nazwaSiedzenia.toString()));
                        nazwaSiedzenia.delete(0,nazwaSiedzenia.length()-1);
                    }
                }
            }
            case 2 -> {
                this.wielkosc = "średni";
                this.rzad = 12;
                this.miejsce = 4;
                for (int i = 0; i < rzad; i++) {
                    for (int j = 0; j < miejsce; j++) {
                        StringBuilder nazwaSiedzenia= new StringBuilder();
                        nazwaSiedzenia.append(literkiRzedow.charAt(i));
                        nazwaSiedzenia.append(j+1);
                        listaMiejsc.add(new Seat(nazwaSiedzenia.toString()));
                        nazwaSiedzenia.delete(0,nazwaSiedzenia.length()-1);
                    }
                }
            }
            case 3 -> {
                this.wielkosc = "duży";
                this.rzad = 15;
                this.miejsce = 6;
                for (int i = 0; i < this.rzad; i++) {
                    for (int j = 0; j < this.miejsce; j++) {
                        StringBuilder nazwaSiedzenia= new StringBuilder();
                        nazwaSiedzenia.append(literkiRzedow.charAt(i));
                        nazwaSiedzenia.append(j+1);
                        listaMiejsc.add(new Seat(nazwaSiedzenia.toString()));
                        nazwaSiedzenia.delete(0,nazwaSiedzenia.length()-1);
                    }
                }
            }

        }

    }

    public int getRzad() {
        return rzad;
    }

    public int getMiejsce() {
        return miejsce;
    }

    void wyswietlMiejsca() {
        System.out.println("Wyswietlany samolot jest : " + this.wielkosc);
        for (Seat seat : listaMiejsc) {
            System.out.println("To nazwa mojego miejsca: "+seat.getNazwaMiejsca()+" a to jego status"+seat.isReservation());
        }
    }

    @Override
    public String toString() {
        return "Samolot{" +
                "wielkosc='" + wielkosc + '\'' +
                '}';
    }
}
