package com.flashcard.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.sampled.*;

public class Message extends javax.swing.JPanel {
    private int test;
    private Timer autoCloseTimer; // Timer để tự động đóng thông báo
    private MessageType messageType = MessageType.SUCCESS;
    private boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Message() {
        initComponents();
        setOpaque(false);
        setVisible(false);
    }

    public void showMessage(MessageType messageType, String title, String message, int durationInMillis) {
        this.messageType = messageType;
        lbTitle.setText(title);
        lbMessage.setText(message);

        // Set icon tương ứng
        if (messageType == MessageType.SUCCESS) {
            setIcon(lbMessage, "src/com/flashcard/icon/pass.png");
            playSound("src/com/flashcard/sounds/success.wav");
        } else if (messageType == MessageType.ERROR) {
            setIcon(lbMessage, "src/com/flashcard/icon/error.png");
            playSound("src/com/flashcard/sounds/error.wav");
        } else if (messageType == MessageType.INFO) {
            setIcon(lbMessage, "src/com/flashcard/icon/info.png");
            playSound("src/com/flashcard/sounds/info.wav");
        }

        // Hiển thị thông báo
        setVisible(true);

        // Tự động tắt sau thời gian durationInMillis
        if (autoCloseTimer != null && autoCloseTimer.isRunning()) {
            autoCloseTimer.stop();
        }
        autoCloseTimer = new Timer(durationInMillis, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                autoCloseTimer.stop();
            }
        });
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();
    }


    private void setIcon(javax.swing.JLabel label, String path) {
        File file = new File(path);
        if (file.exists()) {
            label.setIcon(new ImageIcon(file.getAbsolutePath()));
        } else {
            System.out.println("Icon not found at " + path);
        }
    }

    private void playSound(String soundFilePath)  {
        try (FileInputStream fileInputStream = new FileInputStream(soundFilePath);
             BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream)) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            try {
            	clip.start();
				Thread.sleep(3000);
				clip.stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        lbMessage = new javax.swing.JLabel();

        lbTitle.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(248, 248, 248));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Title");

        lbMessage.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        lbMessage.setForeground(new java.awt.Color(248, 248, 248));
        lbMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbMessage.setText("Message");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(lbMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        if (messageType == MessageType.SUCCESS) {
            g2.setColor(new Color(15, 174, 37)); // Màu xanh lá cho SUCCESS
        } else if (messageType == MessageType.ERROR) {
            g2.setColor(new Color(240, 52, 53)); // Màu đỏ cho ERROR
        } else if (messageType == MessageType.INFO) {
            g2.setColor(new Color(0, 123, 255)); // Màu xanh dương cho INFO
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(245, 245, 245));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        super.paintComponent(grphcs);
    }

    public static enum MessageType {
        SUCCESS, ERROR, INFO
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbMessage;
    // End of variables declaration//GEN-END:variables
}
