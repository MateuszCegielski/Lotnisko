import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa odpowiedzialna za caly interfejs graficzny uzytkownika (GUI)
 */
public class GUI {
    /**
     * Tworzenie zmiennych wykorzystywanych i inicjalizowanych w pozniejszych fragmentach kodu
     */
    JPanel panel, panelTablica, panel3;
    JFrame frame;
    ProgressBar progress;
    User user;
    JTextField imie, nazwisko;
    JLabel powitanie, czekaj, ktoryLot, brakMiejsc;
    JButton login, powrót, rezerwacja, odlotyBttn, exit, potwierdz, powrot, rezerwacje, odwolaj;
    JScrollPane pane;
    JTextArea text;
    JTable table, listaRezerwacji;
    List<Lot> list;
    List<JCheckBox> listaCheckBoxow;
    Lot wybrany;
    Seat zarezerwowany;
    JPanel panelNaKoniec;
    File file = new File("users.ser");
    FileInputStream inputStream;
    FileOutputStream fileOutputStream;
    List<User> userlist;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public GUI(JTable table, List<Lot> list) {
        this.table = table;
        this.list = list;
        MyHandler handler = new MyHandler();
        panel = new JPanel();
        frame = new JFrame();
        frame.add(panel);
        /**
         * WINDOWSOWY LOOK
         */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        /**
         * DODANIE ZEGARA
         */
        new Clock(panel, frame);
        /**
         * Tworzenie pierwszej powitalnej strony
         */
        JLabel imieLabel = new JLabel("Imię:");
        JLabel nazwiskoLabel = new JLabel("Nazwisko:");
        imie = new JTextField();
        nazwisko = new JTextField();
        imieLabel.setBounds(20, 30, 750, 10);
        imie.setBounds(20, 50, 750, 30);
        nazwiskoLabel.setBounds(20, 90, 750, 10);
        nazwisko.setBounds(20, 110, 750, 30);
        panel.add(imieLabel);
        panel.add(imie);
        panel.add(nazwiskoLabel);
        panel.add(nazwisko);
        login = new JButton("ZALOGUJ");
        login.setBounds(275, 200, 200, 90);
        panel.add(login);
        panel.setLayout(null);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Aplikacja LOTNISKO");
        frame.setResizable(false);
        frame.setVisible(true);
        powitanie = new JLabel();
        powitanie.setBounds(10, 10, 200, 20);
        czekaj = new JLabel("Poczekaj na załadowanie aplikacji.");
        czekaj.setBounds(10, 40, 200, 20);
        progress = new ProgressBar();
        progress.add(powitanie);
        progress.add(czekaj);
        final ImageIcon icon = new ImageIcon("background.jpg"); /**TODO uzupełnić swoja scieżką **/
        /**
         * BACKGROUND
         */
        text = new JTextArea() {
            final Image img = icon.getImage();

            {
                setOpaque(false);
            }

            public void paintComponent(Graphics graphics) {
                graphics.drawImage(img, 0, 0, this);
                super.paintComponent(graphics);
            }
        };
        pane = new JScrollPane(text);


        /**
         * PRZEJSCIE DO TABELI ODLOTÓW
         */
        odlotyBttn = new JButton("Tabela odlotów");
        rezerwacja = new JButton("Rezerwacja lotów");
        exit = new JButton("Wyjście z programu");
        login.addActionListener(handler);
        odlotyBttn.addActionListener(handler);
        rezerwacja.addActionListener(handler);
        exit.addActionListener(handler);
        powrót = new JButton("Powrót do MENU");
        potwierdz = new JButton("Potwierdź rezerwacje");
        potwierdz.addActionListener(handler);
        powrot = new JButton("Powrót");
        powrot.addActionListener(handler);
        powrót.addActionListener(handler);
        rezerwacje = new JButton("Dokonane rezerwacje");
        rezerwacje.addActionListener(handler);
        odwolaj = new JButton("Odwołaj rezerwację");
        odwolaj.addActionListener(handler);
        //-------------------------------------------------------

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Aplikacja LOTNISKO");
        frame.setResizable(false);
        frame.setVisible(true);

    }

    public static JTable setStatus(List<Lot> lista) {
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
        String[][] tempLoty = new String[lista.size()][8];
        for (String[] strings : tempLoty) {
            Arrays.fill(strings, " ");
        }
        for (int i = 0; i < lista.size(); i++) {
            tempLoty[i][0] = String.valueOf(lista.get(i).czasOdlotu);
            tempLoty[i][1] = lista.get(i).nrRejsu;
            tempLoty[i][2] = lista.get(i).status;
            tempLoty[i][3] = lista.get(i).gate;
            tempLoty[i][4] = lista.get(i).liniaLotnicza;
            tempLoty[i][5] = lista.get(i).kierunek;
            tempLoty[i][6] = lista.get(i).terminal;
            tempLoty[i][7] = lista.get(i).samolot.wielkosc;
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
        return new JTable(tempLoty, columns);
    }

    public static JTable fillReservations(List<Seat> lista) {
        String[][] tempRes = new String[lista.size()][8];
        for (String[] tempRe : tempRes) {
            Arrays.fill(tempRe, " ");
        }
        for (int i = 0; i < lista.size(); i++) {
            tempRes[i][0] = lista.get(i).getNazwaMiejsca();
            tempRes[i][1] = String.valueOf(lista.get(i).lot.czasOdlotu);
            tempRes[i][2] = lista.get(i).lot.nrRejsu;
            tempRes[i][3] = lista.get(i).lot.status;
            tempRes[i][4] = lista.get(i).lot.gate;
            tempRes[i][5] = lista.get(i).lot.liniaLotnicza;
            tempRes[i][6] = lista.get(i).lot.kierunek;
            tempRes[i][7] = lista.get(i).lot.terminal;

        }
        String[] columns = new String[]{
                "Miejsce",
                "Czas odlotu",
                "Nr rejsu",
                "Status",
                "Bramka",
                "Linia lotnicza",
                "Kierunek",
                "Terminal",
        };
        return new JTable(tempRes, columns);
    }

    /**
     * Klasa handlera eventow reagujaca na wszystkie zdarzenia w GUI
     */
    class MyHandler implements ActionListener, ItemListener {

        /**
         * Funkcja reagujaca na zdarzenia GUI, obslugująca zdarzenia od wszystkich obiektow, pod ktory podpiety jest handler
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String arg = e.getActionCommand();
            switch (arg) {
                case "ZALOGUJ":
                    powitanie.setText("Witaj " + imie.getText() + " " + nazwisko.getText() + "!");
                    frame.setVisible(false);
                    frame.setEnabled(false);
                    progress.setVisible(true);
                    Thread t = new Thread(() -> {
                        try {
                            progress.loop();
                        } catch (Exception ex) {
                            frame.setVisible(true);
                            frame.setEnabled(true);
                        }

                    });
                    t.start();
                    if (file.exists()) {
                        try {
                            inputStream = new FileInputStream(file);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            objectInputStream = new ObjectInputStream(inputStream);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    userlist = new ArrayList<>();
                    if (file.exists()) {
                        if (file.length() != 0) {
                            try {
                                userlist = (List<User>) objectInputStream.readObject();
                                objectInputStream.close();
                                inputStream.close();
                            } catch (IOException | ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (!userlist.isEmpty()) {
                        for (User user1 : userlist) {
                            if (user1.name.equals(imie.getText()) && user1.surname.equals(nazwisko.getText())) {
                                user = user1;
                                break;
                            }
                        }
                    }
                    if (user == null) {
                        user = new User(imie.getText(), nazwisko.getText());
                        userlist.add(user);
                        try {
                            fileOutputStream = new FileOutputStream(file);
                            objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(userlist);
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            fileOutputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (!user.rezerwacje.isEmpty()) {
                        for (Seat siedzenie : user.rezerwacje) {
                            LOTLOOP:
                            for (Lot lot : list) {
                                for (Seat seat : lot.samolot.listaMiejsc) {
                                    if (siedzenie.lot.getNrRejsu().equals(lot.getNrRejsu())) {
                                        if (siedzenie.getNazwaMiejsca().equals(seat.getNazwaMiejsca())) {
                                            siedzenie=seat;
                                            System.out.println("Udało się przypisać.");
                                            break LOTLOOP;
                                        }
                                    }
                                }
                            }
                        }

                    }
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());
                    JPanel panel2 = new JPanel(new FlowLayout());
                    panel.add(pane, BorderLayout.CENTER);
                    panel.add(panel2, BorderLayout.NORTH);
                    panel2.add(odlotyBttn);
                    panel2.add(rezerwacja);
                    panel2.add(rezerwacje);
                    panel2.add(exit);
                    panel.revalidate();
                    panel.repaint();
                    break;
                case "Tabela odlotów":
                    frame.dispose();
                    SwingUtilities.invokeLater(() -> {
                        frame = new JFrame("Tabela lotów");
                        try {
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                            SwingUtilities.updateComponentTreeUI(frame);
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
                            e1.printStackTrace();
                        }
                        table = setStatus(list);
                        table.setEnabled(false);
                        Wyszukiwarka w = new Wyszukiwarka(table);
                        JPanel wpanel = w.p;
                        wpanel.add(powrót, BorderLayout.EAST);
                        frame.add(w);
                        frame.setSize(800, 600);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    });
                    break;
                case "Rezerwacja lotów":
                case "Powrót":
                    frame.dispose();
                    frame = new JFrame("Rezerwacja lotów");
                    try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        SwingUtilities.updateComponentTreeUI(frame);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
                        e1.printStackTrace();
                    }
                    frame.add(panel);
                    panel.removeAll();
                    panel3 = new JPanel(new BorderLayout());
                    panel.setLayout(new BorderLayout());
                    table = setStatus(list);
                    panel.add(new JScrollPane(table), BorderLayout.CENTER);
                    panel3.add(powrót, BorderLayout.EAST);
                    panel.add(panel3, BorderLayout.SOUTH);


                    frame.setSize(800, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    table.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JTable source = (JTable) e.getSource();
                            int row = source.rowAtPoint(e.getPoint());
                            int column = source.columnAtPoint(e.getPoint());
                            String s = source.getModel().getValueAt(row, column) + "";
                            wybrany = list.get(row);

                            System.out.println("Wybrany: " + wybrany);
                            if (wybrany != null) {
                                frame.setLayout(new CardLayout());
//                                Panel głowny
                                panel.removeAll();
                                panel.setVisible(false);
                                panel.setLayout(new GridLayout(wybrany.samolot.getRzad(), wybrany.samolot.getMiejsce()));
//                                wybrany.samolot.wyswietlMiejsca();
                                listaCheckBoxow = new ArrayList<>();  //lista checków
                                for (Seat seat : wybrany.samolot.listaMiejsc) {
                                    listaCheckBoxow.add(new JCheckBox(seat.getNazwaMiejsca()));
                                    JCheckBox temp = listaCheckBoxow.get(listaCheckBoxow.toArray().length - 1);
                                    temp.setEnabled(seat.isFree());
                                    panel.add(temp);
                                }
                                panel.repaint();
                                panel.revalidate();
                                panel.setVisible(true);
//                                Label

                                ktoryLot = new JLabel("Rezerwujesz lot: " + wybrany.wyswietlLot() + " \nWybierz wolne miejsce: ");
                                ktoryLot.setFont(new Font("Serif", Font.BOLD, 12));
//                              Przciski

                                JPanel panelNaPrzciski = new JPanel(new GridLayout(2, 0));
                                panelNaPrzciski.add(potwierdz);
                                panelNaPrzciski.add(powrot);
                                panelNaPrzciski.repaint();
                                panelNaPrzciski.revalidate();
                                panelNaPrzciski.setVisible(true);
//                                Panel końcowy
                                panelNaKoniec = new JPanel(new BorderLayout());
                                panelNaKoniec.add(panel, BorderLayout.CENTER);
                                panelNaKoniec.add(panelNaPrzciski, BorderLayout.EAST);
                                panelNaKoniec.repaint();
                                panelNaKoniec.revalidate();
                                panelNaKoniec.setVisible(true);
                                panelNaKoniec.add(ktoryLot, BorderLayout.NORTH);
                                frame.add(panelNaKoniec);

                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    break;
                case "Powrót do MENU":
                    frame.dispose();
                    frame = new JFrame("MENU");
                    frame.add(panel);
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());
                    panel2 = new JPanel(new FlowLayout());
                    panel.add(pane, BorderLayout.CENTER);
                    panel.add(panel2, BorderLayout.NORTH);
                    panel2.add(odlotyBttn);
                    panel2.add(rezerwacja);
                    panel2.add(rezerwacje);
                    panel2.add(exit);
                    panel.revalidate();
                    panel.repaint();
                    frame.setSize(800, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    break;
                case "Wyjście z programu":
                    System.exit(0);
                    break;
                case "Potwierdź rezerwacje":
                    System.out.println("Potwierdź rezerwacje");
                    for (JCheckBox checkBox : listaCheckBoxow) {
                        if (wybrany.status.equals("planowy")) {

                            if (checkBox.isSelected()) {
                                String nazwaM = checkBox.getText();
                                for (Seat seat : wybrany.samolot.listaMiejsc) {
                                    if (seat.getNazwaMiejsca().equals(nazwaM)) {
                                        seat.changeReservation();
                                        user.rezerwacje.add(seat);
                                    }
                                }
                                checkBox.setEnabled(false);
                            }
                        } else {
                            ktoryLot.setText("Nie można zarezerować lotu o statusie " + wybrany.status);
                            frame.repaint();

                        }
                    }
                    Lotnisko_main.zapisLotów2(list);
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(userlist);
                        objectOutputStream.flush();
                        objectOutputStream.close();
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "Dokonane rezerwacje":
                    frame.dispose();
                    frame = new JFrame("Lista dokonanych rezerwacji");
                    try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        SwingUtilities.updateComponentTreeUI(frame);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
                        e1.printStackTrace();
                    }
                    frame.add(panel);
                    panel.removeAll();
                    panel3 = new JPanel(new BorderLayout());
                    panel.setLayout(new BorderLayout());
                    listaRezerwacji = fillReservations(user.rezerwacje);
                    panel.add(new JScrollPane(listaRezerwacji), BorderLayout.CENTER);
                    panel3.add(powrót, BorderLayout.EAST);
                    panel.add(panel3, BorderLayout.SOUTH);
                    panel3.add(odwolaj, BorderLayout.WEST);
                    panel.repaint();
                    panel.revalidate();
                    panel.setVisible(true);
                    frame.setSize(800, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    listaRezerwacji.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JTable source = (JTable) e.getSource();
                            int row = source.rowAtPoint(e.getPoint());
                            int column = source.columnAtPoint(e.getPoint());
                            String s = source.getModel().getValueAt(row, column) + "";
                            zarezerwowany = user.rezerwacje.get(row);
                            System.out.println("To jest to: " + zarezerwowany);
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });


                    break;
                case "Odwołaj rezerwację":
                    System.out.println("Wybrany: " + zarezerwowany);
                    if (zarezerwowany != null) {
                        zarezerwowany.changeReservation();
                        user.rezerwacje.remove(zarezerwowany);
                        Lotnisko_main.zapisLotów2(list);
                        try {
                            fileOutputStream = new FileOutputStream(file);
                            objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(userlist);
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            fileOutputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        rezerwacje.doClick();

                    }


                    frame.setSize(800, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    break;

            }
        }


        @Override
        public void itemStateChanged(ItemEvent e) {

        }
    }


}
