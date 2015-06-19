package gui.dialog;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Toast {
    private static final int MILLISECONDS = 2500;

    public static void show(Window parent, String message) {
        final JDialog dialog = new JDialog();

        dialog.setUndecorated(true);
        dialog.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
//        panel.setBackground(Color.decode("#2572EB"));
//        panel.setBorder(new LineBorder(Color.decode("#56C5FF"), 2));
        dialog.getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel("");
        toastLabel.setText(message);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);

        dialog.setBounds(100, 100, toastLabel.getPreferredSize().width + 20, 31);

        dialog.setAlwaysOnTop(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height / 2 - dialog.getSize().height / 2;
        int half = y / 2;
        dialog.setLocation(dim.width / 2 - dialog.getSize().width / 2, y + half);
        panel.add(toastLabel);
        dialog.setVisible(true);
        parent.requestFocus();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(Toast.MILLISECONDS);
                    dialog.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Toast() {
    }
}