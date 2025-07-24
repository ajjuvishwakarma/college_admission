package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("Admin Login");
        setSize(600, 850);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Theme Colors
        Color bgColor = new Color(135, 206, 235); // Sky Blue
        Color buttonColor = new Color(30, 144, 255); // Dodger Blue
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Panel with layout and background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // User name Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(font);
        panel.add(userLabel, gbc);

        // User name Field
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(font);
        panel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(font);
        panel.add(passLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(font);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton, gbc);

        // Add Action Listener
        loginButton.addActionListener(e -> checkLogin());

        // Add panel to frame
        add(panel);
        setVisible(true);
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:4306/college_admission_db", 
                "root", "Ajay2001*" 
            );

            String query = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role"); // Fetch role

                if ("admin".equalsIgnoreCase(role)) {
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome, Admin.");
                    dispose(); // Close login form
                    new Dashboard(); // Open AdminDashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Access Denied! Only Admins are allowed.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }

            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}