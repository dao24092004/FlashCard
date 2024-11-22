package com.flashcard.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PanelSlideWithTesting extends JPanel {

    private final List<Component> list;
    private final Timer timer;
    private Component comExit;
    private Component comShow;
    private int currentShowing;
    private boolean animateRight;
    private int animate = 1;
    private String slideTitle;
    private Color backgroundColor;

    public PanelSlideWithTesting() {
        this("Default Slide", Color.GRAY); // Set default title and color
    }

    public PanelSlideWithTesting(String title, Color color) {
        this.slideTitle = title;
        this.backgroundColor = color;
        setBackground(backgroundColor); // Set the background color

        // Create a label for the title
        JLabel label = new JLabel(slideTitle, SwingConstants.CENTER); // Add the title
        label.setForeground(Color.WHITE); // Set the label text color
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust font size
        add(label, BorderLayout.CENTER);

        list = new ArrayList<>();
        currentShowing = -1;
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                animate();
            }
        });
    }

    public void init(Component... com) {
        if (com.length > 0) {
            for (Component c : com) {
                list.add(c);
                c.setSize(getSize());
                c.setVisible(false);
                this.add(c);
            }
            // Show the first component when initialized
            Component show = list.get(0);
            show.setVisible(true);
            show.setLocation(0, 0);
            currentShowing = 0;
        }
    }

    public void show(int index) {
        if (!timer.isRunning()) {
            if (list.size() > 0 && index < list.size() && index != currentShowing) {
                if (currentShowing == -1) {
                    currentShowing = index;
                }

                comShow = list.get(index);
                comExit = list.get(currentShowing);

                animateRight = index < currentShowing;
                currentShowing = index;
                comShow.setVisible(true);

                if (animateRight) {
                    comShow.setLocation(-comShow.getWidth(), 0);
                } else {
                    comShow.setLocation(getWidth(), 0);
                }
                timer.start();
            }
        }
    }

    private void animate() {
        if (animate <= 0) {
            animate = 1;
        }

        if (animateRight) {
            if (comShow.getLocation().x < 0) {
                comShow.setLocation(comShow.getLocation().x + animate, 0);
                comExit.setLocation(comExit.getLocation().x + animate, 0);
            } else {
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        } else {
            if (comShow.getLocation().x > 0) {
                comShow.setLocation(comShow.getLocation().x - animate, 0);
                comExit.setLocation(comExit.getLocation().x - animate, 0);
            } else {
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        }
    }

    public String getSlideTitle() {
        return slideTitle;
    }

    public void setSlideTitle(String slideTitle) {
        this.slideTitle = slideTitle;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 336, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 283, Short.MAX_VALUE));
    }
}
