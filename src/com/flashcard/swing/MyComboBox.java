package com.flashcard.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MyComboBox<E> extends JComboBox<E> {
    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";
    
    public MyComboBox() {
        super();
        setRenderer(new ComboBoxRenderer());
        setBackground(new Color(0, 0, 0, 0));  // Transparent background
        setForeground(Color.decode("#7A8C8D")); // Text color
        setFont(new Font("sansserif", Font.PLAIN, 13)); // Font style
        setFocusable(true);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Set padding
        
        // Item listener to handle combo box changes
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                repaint(); // Repaint when item is selected
            }
        });
    }
    
    // Custom rendering to add hint and icons (similar to MyTextField)
    private class ComboBoxRenderer extends JLabel implements ListCellRenderer<E> {
        public ComboBoxRenderer() {
            setOpaque(true);
            setBackground(new Color(230, 245, 241));  // Background color
            setFont(new Font("sansserif", Font.PLAIN, 13));
            setForeground(Color.decode("#7A8C8D"));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value != null ? value.toString() : hint);
            setBackground(isSelected ? new Color(67, 179, 193) : new Color(230, 245, 241)); // Highlight on selection
            setForeground(isSelected ? Color.WHITE : new Color(67, 179, 193)); // Text color
            return this;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getSelectedItem() == null || getSelectedItem().toString().isEmpty()) {
            int h = getHeight();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(200, 200, 200));
            g.drawString(hint, 10, h / 2 + fm.getAscent() / 2 - 2); // Draw hint if no item is selected
        }
    }

    // Getter and Setter for hint
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }

    // Getter and Setter for prefixIcon
    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        repaint();
    }

    // Getter and Setter for suffixIcon
    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        repaint();
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(75, 175, 152));  // Border color
        g2.setStroke(new BasicStroke(2));  // Border thickness
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);  // Draw rounded border
    }

    // Override method to paint prefix and suffix icons
    private void paintIcon(Graphics g) {
        if (prefixIcon != null) {
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            prefixIcon.paintIcon(this, g, 10, y);  // Draw prefix icon on the left
        }
        if (suffixIcon != null) {
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            suffixIcon.paintIcon(this, g, getWidth() - suffixIcon.getIconWidth() - 10, y);  // Draw suffix icon on the right
        }
    }
}
