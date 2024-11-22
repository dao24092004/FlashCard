package com.flashcard.swing;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.logging.Logger;

public class WinButton extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(WinButton.class.getName());
    private final JFrame adminFrame;
    private final com.flashcard.swing.Button close;
    private final com.flashcard.swing.Button hide;
    private final com.flashcard.swing.Button size;

    public WinButton(JFrame adminFrame) {
        this.adminFrame = adminFrame;
        setOpaque(false);

        close = createButton("X", new Color(255, 0, 0), "Close Application");
        hide = createButton("_", new Color(199, 252, 2), "Minimize Application");
        size = createButton("⬜", new Color(82, 165, 57), "Maximize/Restore Application");

        initEventHandlers();
        setupLayout();
    }

    private com.flashcard.swing.Button createButton(String text, Color bgColor, String tooltip) {
        com.flashcard.swing.Button button = new com.flashcard.swing.Button(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setToolTipText(tooltip);
        addHoverEffect(button, bgColor);
        return button;
    }

    private void addHoverEffect(com.flashcard.swing.Button button, Color originalColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void initEventHandlers() {
        close.addActionListener(ae -> {
            LOGGER.info("Admin requested to close the application.");
            System.exit(0);
        });

        hide.addActionListener(ae -> {
            LOGGER.info("Admin minimized the application window.");
            adminFrame.setState(JFrame.ICONIFIED);
        });

        size.addActionListener(ae -> {
            if (adminFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                LOGGER.info("Admin restored the application window to normal size.");
                adminFrame.setExtendedState(JFrame.NORMAL);
            } else {
                LOGGER.info("Admin maximized the application window.");
                adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    private void setupLayout() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(hide, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(hide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }

    // Getters for buttons
    public com.flashcard.swing.Button getCloseButton() {
        return close;
    }

    public com.flashcard.swing.Button getHideButton() {
        return hide;
    }

    public com.flashcard.swing.Button getSizeButton() {
        return size;
    }
}
