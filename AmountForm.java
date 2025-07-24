package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AmountForm extends JFrame {
    private JTextField descriptionField, amountField;
    private JButton submitButton;

    public AmountForm() {
        setTitle("Amount Entry Form");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with background color
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(135, 206, 235)); // Sky Blue

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Description Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Description:"), gbc);

        // Description Field
        gbc.gridx = 1;
        descriptionField = new JTextField(15);
        panel.add(descriptionField, gbc);

        // Amount Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Amount (₹):"), gbc);

        // Amount Field
        gbc.gridx = 1;
        amountField = new JTextField(15);
        panel.add(amountField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("Submit");
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> insertAmount());

        add(panel);
        setVisible(true);
    }

    private void insertAmount() {
        String desc = descriptionField.getText();
        double amt;

        try {
            amt = Double.parseDouble(amountField.getText());

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");
            PreparedStatement pst = con.prepareStatement("INSERT INTO amount(description, amount) VALUES (?, ?)");
            pst.setString(1, desc);
            pst.setDouble(2, amt);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Amount Added Successfully!");
                descriptionField.setText("");
                amountField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add amount.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Enter a number.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AmountForm();
    }
}