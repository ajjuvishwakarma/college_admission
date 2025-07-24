package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AdmissionForm extends JFrame {
    private JTextField studentIdField, dateField;
    private JComboBox<String> statusBox;
    private JButton submitButton;

    public AdmissionForm() {
        setTitle("Admission Form");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel with sky blue background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230)); // Sky Blue
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentLabel = new JLabel("Student ID:");
        studentLabel.setForeground(new Color(0, 51, 102)); // Dark Blue
        panel.add(studentLabel, gbc);

        gbc.gridx = 1;
        studentIdField = new JTextField(15);
        panel.add(studentIdField, gbc);

        // Row 2: Application Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Application Date (DD-MM-YYYY):");
        dateLabel.setForeground(new Color(0, 51, 102));
        panel.add(dateLabel, gbc);

        gbc.gridx = 1;
        dateField = new JTextField(15);
        panel.add(dateField, gbc);

        // Row 3: Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(new Color(0, 51, 102));
        panel.add(statusLabel, gbc);

        gbc.gridx = 1;
        String[] statusOptions = {"Pending", "Approved", "Rejected"};
        statusBox = new JComboBox<>(statusOptions);
        panel.add(statusBox, gbc);

        // Row 4: Submit button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 153, 204)); // Blue button
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        panel.add(submitButton, gbc);

        add(panel);

        // Button Action
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertAdmissionData();
            }
        });

        setVisible(true);
    }

    private void insertAdmissionData() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            String applicationDate = dateField.getText().trim();
            String status = (String) statusBox.getSelectedItem();

            // Validate date format
            if (!isValidDate(applicationDate)) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Please enter date as DD-MM-YYYY.");
                return;
            }

            // Convert date to MySQL format
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = inputFormat.parse(applicationDate);
            String formattedDate = dbFormat.format(parsedDate);

            // Insert into DB
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO admission_form(student_id, application_date, status) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setString(2, formattedDate);
            pstmt.setString(3, status);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Admission form submitted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit form.");
            }

            conn.close();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID! Please enter a valid number.");
        } catch (ParseException pex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use DD-MM-YYYY.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // strict date checking
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        new AdmissionForm();
    }
}