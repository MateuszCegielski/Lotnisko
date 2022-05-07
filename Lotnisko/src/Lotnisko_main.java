import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * Klasa glowna aplikacji, umozliwiajaca jej wywolanie
 * Aplikacja zaliczajaca zadanie projektowe z przedmiotu PJAVA, wykonana przez zespol:
 * @author Kacper Kilianek i Mateusz Cegielski
 */
public class Lotnisko_main {
    final static String filePath
            = "Lotnisko\\kierunek_linia.csv";

    /**
     * @return - zwraca losowo wygenerowany numer rejsu ktory sklada sie z dwoch literek i dwoch cyfr
     */
    public static String setNrRejsu() {
        String znaki = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String znaki1 = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 2) { // length of the random string.
            int index = (int) (rnd.nextFloat() * znaki.length());
            salt.append(znaki.charAt(index));
        }
        while (salt.length() < 4) {
            int index1 = (int) (rnd.nextFloat() * znaki1.length());
            salt.append(znaki1.charAt(index1));
        }
        String rejsStr = salt.toString();
        return rejsStr;
    }

    /**
     * @return zwraca losowo wygenerowany gate
     */
    public static String setGate() {
        return String.valueOf((int) (Math.random() * (50 - 1)) + 1);
    }

    /**
     * @return - zwraca losowo wygenerowany terminal
     */
    public static String setTerminal() {
        String znaki = "ABCDEF";
        StringBuilder terminal = new StringBuilder();
        Random rnd = new Random();
        while (terminal.length() < 1) { // length of the random string.
            int index = (int) (rnd.nextFloat() * znaki.length());
            terminal.append(znaki.charAt(index));
        }

        return String.valueOf(terminal);
    }

    /**
     * @param lista - lista z wszystkimi lotami
     * @return zwraca liste z lotami gdzie zostal juz przypisany im czas odlotu
     */

    public static List<Lot> timeSetting(List<Lot> lista) {
        int rozmiar = lista.size();
        List<Lot> temp = new ArrayList<>();
        Random random = new Random();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 15;
        calendar.add(Calendar.MINUTE, mod < 8 ? -mod : (15 - mod));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        LocalTime time = LocalTime.parse(formatter.format(calendar.getTime()));
        String timeString = String.valueOf(time);
        int counter = 0;
        while (!timeString.equals("00:00")) {
            temp.add(lista.get(random.nextInt(rozmiar)));
            temp.get(counter).setCzasOdlotu(time);
            calendar.add(Calendar.MINUTE, 15);
            time = LocalTime.parse(formatter.format(calendar.getTime()));
            timeString = String.valueOf(time);
            counter++;
        }

        return temp;
    }

    /**
     * funkcja przypisujaca status lotom, na podstawie roznicy czasu od aktualnej godziny
     *
     * @param lista - lista lotow, ktorym ma zostac przypisany status
     */
    public static void setStatus(List<Lot> lista) {
        Instant obecny = Instant.now();
        Instant czasLotu;
        Duration diff;
        for (Lot lot : lista) {
            LocalDateTime ldt = lot.getCzasOdlotu().atDate(LocalDate.now());
            czasLotu = ldt.atZone(ZoneId.systemDefault()).toInstant();
            diff = Duration.between(obecny, czasLotu);
            if (diff.toMinutes() <= 0) {
                lot.setStatus("wystartował");
            } else if (diff.toMinutes() <= 5) {
                lot.setStatus("gate closed");
            } else if (diff.toMinutes() <= 15) {
                lot.setStatus("final call");
            } else if (diff.toMinutes() <= 45) {
                lot.setStatus("boarding");
            } else {
                lot.setStatus("planowy");
            }
        }
    }

    /**
     * Pierwsze generowanie obiektow Lot
     *
     * @return zwraca liste utworzonych lotow
     */
    public static List<Lot> generujLoty() {
        BufferedReader br = null;
        List<Lot> loty = new ArrayList<>();
        try {
            int index = 0;
            File file = new File(filePath);
            List<String> kierunki = new ArrayList<>();
            List<String> linie = new ArrayList<>();
            br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                String[] kierunekLinia = line.split(" ");

                String kierunek = kierunekLinia[0].trim();
                String linia = kierunekLinia[1].trim();

                kierunki.add(kierunek);
                linie.add(linia);
                loty.add(new Lot(setNrRejsu(), "boarding", setGate(), linie.get(index), kierunki.get(index), setTerminal(), new Samolot((int) ((Math.random() * (4 - 1)) + 1)))); //Tu na początek jeszcze bedzie przy prawdziwym generowaniu lotow: new Lot(LocalTime.MIN.plusSeconds(random.nextLong()),...
                index++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        zapisLotów(loty);
        return loty;
    }

    /**
     * Funkcja zapisujaca liste lotow do pliku (serializacja)
     * @param loty - loty ktore zostana zapisane do pliku
     */
    public static void zapisLotów(List<Lot> loty) {
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream("loty.ser"));
            stream.writeObject(loty);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Druga opcja zapisywania listy lotow, tym razem do innego pliku z powodu zapisania statusu aplikacji, a nie wszystkich mozliwych lotow jak to jest w przypadku funkcji zapislotow
     *
     * @param loty - przekazywana lista lotow do zapisania
     */
    public static void zapisLotów2(List<Lot> loty) {
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream("loty_final.ser"));
            stream.writeObject(loty);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * funkcja wczytujaca liste lotow z pliku
     * @return zwraca liste wczytanych lotow
     */
    public static List<Lot> wczytajLoty() {
        List<Lot> loty = new ArrayList<>();
        try (ObjectInputStream pl2 = new ObjectInputStream(new FileInputStream("loty.ser"))) {
            loty = (ArrayList<Lot>) pl2.readObject();
        } catch (EOFException ex) {
            System.out.println("Koniec pliku");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loty;
    }

    /**
     * funkcja wczytujaca stan lotow z poprzedniego uruchomienia aplikacji
     * @return zwraca liste wczytanych lotow
     */
    public static List<Lot> wczytajLoty2() {
        List<Lot> loty = new ArrayList<>();
        try (ObjectInputStream pl2 = new ObjectInputStream(new FileInputStream("loty_final.ser"))) {
            loty = (ArrayList<Lot>) pl2.readObject();
        } catch (EOFException ex) {
            System.out.println("Koniec pliku");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loty;
    }

    /**
     * funkcja wywolujaca cala nasza aplikacje
     */
    public static void main(String[] args) throws IOException {
//        List<Lot> loty = generujLoty(); // To uruchom tylko za pierwszym razem (wstępna generacja lotów i zapis ich do pliku)
        List<Lot> currLoty;
//        currLoty = timeSetting(wczytajLoty()); //tu wypełnianie zgodnie z pozostałym czasem do północy
//        setStatus(currLoty);
//        zapisLotów2(currLoty);
        currLoty = wczytajLoty2();
        List<Lot> currLoty2;
        currLoty2 = currLoty;
        String[][] tempLoty = new String[currLoty.size()][8];
        for (String[] strings : tempLoty) {
            Arrays.fill(strings, " ");
        }
        for (int i = 0; i < currLoty.size(); i++) {
            tempLoty[i][0] = String.valueOf(currLoty.get(i).czasOdlotu);
            tempLoty[i][1] = currLoty.get(i).nrRejsu;
            tempLoty[i][2] = currLoty.get(i).status;
            tempLoty[i][3] = currLoty.get(i).gate;
            tempLoty[i][4] = currLoty.get(i).liniaLotnicza;
            tempLoty[i][5] = currLoty.get(i).kierunek;
            tempLoty[i][6] = currLoty.get(i).terminal;
            tempLoty[i][7] = currLoty.get(i).samolot.wielkosc;
        }
        String[] columns = new String[]{
                "Czas odlotu",
                "Nr rejsu",
                "Status",
                "Bramka",
                "Linia lotnicza",
                "Kierunek",
                "Terminal",
                "Samolot"
        };
        JTable odlotyTable = new JTable(tempLoty, columns);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI(odlotyTable, currLoty2);

            }
        });


    }

}

