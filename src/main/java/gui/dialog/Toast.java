package gui.dialog;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Toast {
    private static final int MILLISECONDS = 2500;

    private static JDialog dialog = new JDialog();
    private static JLabel toastLabel;

    static {
        dialog.setUndecorated(true);
        dialog.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#2572EB"));
        panel.setBorder(new LineBorder(Color.decode("#2572EB"), 2));
        dialog.getContentPane().add(panel, BorderLayout.CENTER);

        toastLabel = new JLabel("");
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        toastLabel.setForeground(Color.WHITE);

        dialog.setAlwaysOnTop(true);

        panel.add(toastLabel);
    }

    public static void show(Window parent, String message) {
        toastLabel.setText(message);
        dialog.setBounds(100, 100, toastLabel.getPreferredSize().width + 20,
                toastLabel.getPreferredSize().height + 16);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height / 2 - dialog.getSize().height / 2;
        int offset = y * 2 / 3;
        dialog.setLocation(dim.width / 2 - dialog.getSize().width / 2, y + offset);

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