package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton submitButton;

    public AdminForm() {
        setTitle("Admin Registration");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sky Blue theme colors
        Color bgColor = new Color(135, 206, 250); // Light Sky Blue
        Color labelColor = Color.BLACK;
        Color fieldBg = Color.WHITE;
        Color fieldFg = Color.BLACK;
        Color buttonColor = new Color(70, 130, 180); // Steel Blue for better button visibility
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // user name Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(font);
        usernameLabel.setForeground(labelColor);
        panel.add(usernameLabel, gbc);

        // user name Field
        gbc.gridx = 1;
        usernameField = new JTextField(18);
        usernameField.setFont(font);
        usernameField.setBackground(fieldBg);
        usernameField.setForeground(fieldFg);
        panel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        passwordLabel.setForeground(labelColor);
        panel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        passwordField.setFont(font);
        passwordField.setBackground(fieldBg);
        passwordField.setForeground(fieldFg);
        panel.add(passwordField, gbc);

        // Submit Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("Register");
        submitButton.setFont(font);
        submitButton.setBackground(buttonColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        panel.add(submitButton, gbc);

        // Add Action Listener
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertAdmin();
            }
        });

        // Add panel to frame
        add(panel);
        setVisible(true);
    }

    private void insertAdmin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*"
            );

            String query = "INSERT INTO admin(username, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Admin registered successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register admin.");
            }

            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminForm();
    }
}