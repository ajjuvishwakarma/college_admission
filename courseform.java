package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class courseform extends JFrame {
    private JTextField idField, nameField, durationField, feeField;
    private JButton submitButton, showButton;
    private JTextArea outputArea;

    public courseform() {
        setTitle("Course Form");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sky Blue theme
        Color backgroundColor = new Color(135, 206, 250); // Light Sky Blue
        Color labelColor = Color.BLACK;
        Color fieldBg = Color.WHITE;
        Color fieldFg = Color.BLACK;
        Color buttonColor = new Color(70, 130, 180); // Steel Blue
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Main panel using GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1 - Course ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Course ID:");
        idLabel.setFont(font);
        idLabel.setForeground(labelColor);
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setFont(font);
        idField.setBackground(fieldBg);
        idField.setForeground(fieldFg);
        panel.add(idField, gbc);

        // Row 2 - Course Name
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Course Name:");
        nameLabel.setFont(font);
        nameLabel.setForeground(labelColor);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(font);
        nameField.setBackground(fieldBg);
        nameField.setForeground(fieldFg);
        panel.add(nameField, gbc);

        // Row 3 - Duration
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setFont(font);
        durationLabel.setForeground(labelColor);
        panel.add(durationLabel, gbc);

        gbc.gridx = 1;
        durationField = new JTextField(15);
        durationField.setFont(font);
        durationField.setBackground(fieldBg);
        durationField.setForeground(fieldFg);
        panel.add(durationField, gbc);

        // Row 4 - Total Fee
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel feeLabel = new JLabel("Total Fee:");
        feeLabel.setFont(font);
        feeLabel.setForeground(labelColor);
        panel.add(feeLabel, gbc);

        gbc.gridx = 1;
        feeField = new JTextField(15);
        feeField.setFont(font);
        feeField.setBackground(fieldBg);
        feeField.setForeground(fieldFg);
        panel.add(feeField, gbc);

        // Row 5 - Submit and Show Buttons
        submitButton = new JButton("Submit");
        showButton = new JButton("Show All");

        submitButton.setFont(font);
        submitButton.setBackground(buttonColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        showButton.setFont(font);
        showButton.setBackground(buttonColor);
        showButton.setForeground(Color.WHITE);
        showButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(submitButton);
        buttonPanel.add(showButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Wrapper for panel
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(backgroundColor);
        wrapper.add(panel);
        add(wrapper, BorderLayout.NORTH);

        // Output area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Course Records"));
        add(scroll, BorderLayout.CENTER);

        // Submit button logic
        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String duration = durationField.getText();
                double fee = Double.parseDouble(feeField.getText());

                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO course(course_id, course_name, duration, total_fee) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setString(3, duration);
                pst.setDouble(4, fee);

                int result = pst.executeUpdate();
                outputArea.setText(result > 0 ? "Course saved successfully!" : "Failed to save course.");
                pst.close();
                conn.close();
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        // Show All button logic
        showButton.addActionListener(e -> {
            outputArea.setText("");
            try {
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM course");

                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    course c = new course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("duration"),
                        rs.getDouble("total_fee")
                    );
                    sb.append(c.toString());
                }

                outputArea.setText(sb.toString().isEmpty() ? " No course records found." : sb.toString());

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception ex) {
                outputArea.setText("DB Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new courseform();
    }
}