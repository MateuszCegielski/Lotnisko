import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * Klasa wyszukiwarki umozliwajaca dynamiczne przeszukiwanie JTablow,
 * wykorzystana w GUI do przeszukiwania bazy lotow
 */
public class Wyszukiwarka extends JPanel {
    JPanel p;

    public Wyszukiwarka(JTable table) {
        TableRowSorter<TableModel> sort = new TableRowSorter<>(table.getModel());
        JTextField textField = new JTextField();
        TableColumnModel columnModel = table.getColumnModel();
        //ustawianie rozmiaru jednej z kolumn
        columnModel.getColumn(5).setPreferredWidth(100);
//początek sortowania
        table.setRowSorter(sort);
        p = new JPanel(new BorderLayout());
        p.add(new JLabel("Wyszukaj po dowolnym terminie:"), BorderLayout.WEST);
        p.add(textField, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(p, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            /**
             * Funkcja odpowiedzialna za update filtra, zmieniajaca przez to liczbe wyswietlanych elementow JTable
             * @param e - event dokumentowy przekazywany do funkcji
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = textField.getText();
                if (str.trim().length() == 0) {
                    sort.setRowFilter(null);
                } else {
                    sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            /**
             * Funkcja zerujaca filtr, przywracajaca domyslny widok wyswietlania JTable
             * @param e - event dokumentowy przekazywany do funkcji
             */
            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = textField.getText();
                if (str.trim().length() == 0) {
                    sort.setRowFilter(null);
                } else {
                    sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
}