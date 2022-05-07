import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Klasa progressbaru, uzywana przez GUI do wyswietlenia okna dialogowego imitujacego "loading-screen"
 */
public class ProgressBar extends JDialog {
    JProgressBar progress;

    ProgressBar() {
        progress = new JProgressBar(0, 1000);
        progress.setBounds(35, 80, 320, 30);
        progress.setValue(0);
        progress.setStringPainted(true);
        add(progress);
        setSize(400, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        ProgressBar frame = new ProgressBar();
//        frame.loop();
    }

    /**
     * Funkcja zwiekszajaca stan progressbaru
     */
    public void loop() throws Exception {
        int i = 0;
        while (i <= 1000) {
            progress.setValue(i);
            i = i + 10;
            try {
                Thread.sleep(10);
            } catch (Exception ignored) {
            }
        }
        this.dispose();
        throw new Exception("Skończono");
    }

}
