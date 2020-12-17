package ro.vvs.upt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    int count = 0;
    JLabel label;
    JFrame frame;
    JPanel panel;
    JTextField textField1;

    public GUI() {

        frame = new JFrame();

        textField1 = new JTextField();

        JButton button = new JButton("Set");
        button.addActionListener(this);

        label = new JLabel("No of cliks 0");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout());
        panel.add(textField1);
        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Server Status");
        frame.pack();
        frame.setVisible(true);

    }

    public static void main (String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        count++;
        label.setText(textField1.getText());
    }
}
