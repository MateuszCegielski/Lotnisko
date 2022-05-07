import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * Klasa zegara, wyswietlajaca aktualna godzine w interfejsie GUI
 */
public class Clock {
    public Clock(JPanel panel, JFrame frame) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JLabel time = new JLabel();
                time.setText(DateFormat.getDateTimeInstance().format(new Date()));
                Dimension size = time.getPreferredSize();
                time.setBounds(325, 20, size.width, size.height);
                Timer t = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        time.setText(DateFormat.getDateTimeInstance().format(new Date()));
                    }
                });
                t.setRepeats(true);
                t.setCoalesce(true);
                t.setInitialDelay(0);
                t.start();
                panel.add(time);
            }
        });
    }
}
