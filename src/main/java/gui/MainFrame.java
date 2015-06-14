package gui;

import actions.OpenFormAction;
import messages.WarningMessages;
import meta.mainframe.MainFrameMetaData;
import meta.mainframe.MetaMenu;
import meta.mainframe.MetaMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Created by Nikola on 11.6.2015..
 */
public class MainFrame extends JFrame {

    public MainFrame(MainFrameMetaData mfmt) {
        this.setTitle(mfmt.getTitle());
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                int result = JOptionPane.showConfirmDialog(MainFrame.this, WarningMessages.MAINFRAME_CLOSING,
//                        WarningMessages.TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//
//                if (result == JOptionPane.YES_OPTION) {
//                    System.exit(0);
//                }
//            }
//        });
        // TODO fill

        initWindow();
        initToolbar(mfmt.getMenus());
    }

    private void initToolbar(List<MetaMenu> menus) {
        JMenuBar menuBar = new JMenuBar();
        this.add(menuBar, BorderLayout.NORTH);

        for (MetaMenu metaMenu : menus) {
            JMenu menu = new JMenu(metaMenu.getText());
            menuBar.add(menu);

            for (MetaMenuItem metaMenuItem : metaMenu.getMenuItems()) {
                OpenFormAction formAction = new OpenFormAction(metaMenuItem.getText(), metaMenuItem.getFormName());
                JMenuItem menuItem = new JMenuItem(formAction);
                menu.add(menuItem);
            }
        }
    }

    private void initWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);

        this.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        this.setLocation(new Point(middle.x - (getWidth() / 2), middle.y - (getHeight() / 2)));
    }
}
