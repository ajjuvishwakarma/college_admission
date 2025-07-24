package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentForm extends JFrame {
    private JTextField nameField, emailField, dobField, courseIdField, contactNumberField, photoPathField;
    private JTextField fatherNameField, motherNameField;
    private JTextArea addressArea;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;
    private JRadioButton maleRadio, femaleRadio;
    private JButton submitButton, showButton, updateButton, deleteButton, searchButton, choosePhotoButton;
    private ButtonGroup genderGroup;
    private JTextArea outputArea;
    private JLabel photoPreviewLabel;

    public StudentForm() {
        setTitle("Student Admission Form");
        setSize(700, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(173, 216, 230));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(173, 216, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Father's Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel fatherLabel = new JLabel("Father's Name:");
        fatherLabel.setFont(labelFont);
        formPanel.add(fatherLabel, gbc);
        gbc.gridx = 1;
        fatherNameField = new JTextField(20);
        formPanel.add(fatherNameField, gbc);

        // Mother's Name
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel motherLabel = new JLabel("Mother's Name:");
        motherLabel.setFont(labelFont);
        formPanel.add(motherLabel, gbc);
        gbc.gridx = 1;
        motherNameField = new JTextField(20);
        formPanel.add(motherNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // DOB
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel dobLabel = new JLabel("DOB (dd-MM-yyyy):");
        dobLabel.setFont(labelFont);
        formPanel.add(dobLabel, gbc);
        gbc.gridx = 1;
        dobField = new JTextField(20);
        formPanel.add(dobField, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        formPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        genderPanel.setBackground(new Color(173, 216, 230));
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(genderPanel, gbc);

        // Course ID
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel courseLabel = new JLabel("Course ID:");
        courseLabel.setFont(labelFont);
        formPanel.add(courseLabel, gbc);
        gbc.gridx = 1;
        courseIdField = new JTextField(20);
        formPanel.add(courseIdField, gbc);

        // Contact Number
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(labelFont);
        formPanel.add(contactLabel, gbc);
        gbc.gridx = 1;
        contactNumberField = new JTextField(20);
        formPanel.add(contactNumberField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressScroll, gbc);

        // Photo Upload
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel photoLabel = new JLabel("Photo:");
        photoLabel.setFont(labelFont);
        formPanel.add(photoLabel, gbc);
        gbc.gridx = 1;
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPathField = new JTextField();
        photoPathField.setEditable(false);
        choosePhotoButton = new JButton("Choose");
        photoPanel.add(photoPathField, BorderLayout.CENTER);
        photoPanel.add(choosePhotoButton, BorderLayout.EAST);
        formPanel.add(photoPanel, gbc);

        // Photo Preview
        gbc.gridy = 10;
        photoPreviewLabel = new JLabel();
        photoPreviewLabel.setPreferredSize(new Dimension(150, 150));
        photoPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(photoPreviewLabel, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
        submitButton = new JButton("Submit");
        showButton = new JButton("Show All");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");

        Color buttonColor = new Color(100, 149, 237);
        Color textColor = Color.WHITE;
        Font buttonFont = new Font("Arial", Font.BOLD, 13);

        JButton[] buttons = {submitButton, showButton, updateButton, deleteButton, searchButton};
        for (JButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setFont(buttonFont);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230));
        buttonPanel.add(submitButton);
        buttonPanel.add(showButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel, gbc);

        // Search Panel
        gbc.gridy = 12;
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(173, 216, 230));
        String[] criteriaOptions = {"Name", "Email", "Student ID", "Course ID"};
        searchCriteriaBox = new JComboBox<>(criteriaOptions);
        searchField = new JTextField(15);
        searchPanel.add(new JLabel("Search By:"));
        searchPanel.add(searchCriteriaBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        formPanel.add(searchPanel, gbc);

        // Output Area
        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Output"));
        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        // 🆕 Scrollable form panel
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        add(formScrollPane, BorderLayout.CENTER);
        add(outputScrollPane, BorderLayout.SOUTH);

        setupListeners();

        setVisible(true);
    }

    private void setupListeners() {
        choosePhotoButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                photoPathField.setText(selected.getAbsolutePath());
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(selected.getAbsolutePath())
                        .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                photoPreviewLabel.setIcon(imageIcon);
            }
        });

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String fatherName = fatherNameField.getText();
            String motherName = motherNameField.getText();
            String email = emailField.getText();
            String dobInput = dobField.getText();
            String dob;
            String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "");
            String contact = contactNumberField.getText();
            String photoPath = photoPathField.getText();
            String address = addressArea.getText();

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = inputFormat.parse(dobInput);
                dob = dbFormat.format(parsedDate);
            } catch (Exception ex) {
                outputArea.setText("Please enter valid date in dd-MM-yyyy format.");
                return;
            }

            int courseId;
            try {
                courseId = Integer.parseInt(courseIdField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Please enter valid Course ID.");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO student (name, father_name, mother_name, email, dob, gender, course_id, contact_number, address, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, fatherName);
                pst.setString(3, motherName);
                pst.setString(4, email);
                pst.setString(5, dob);
                pst.setString(6, gender);
                pst.setInt(7, courseId);
                pst.setString(8, contact);
                pst.setString(9, address);
                pst.setString(10, photoPath);

                int result = pst.executeUpdate();
                outputArea.setText(result > 0 ? " Student saved successfully!" : "❌ Failed to save student.");
                pst.close(); conn.close();
            } catch (Exception ex) {
                outputArea.setText("DB Error: " + ex.getMessage());
            }
        });

        showButton.addActionListener(e -> {
            outputArea.setText("");
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT * FROM student";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id")).append("\n")
                      .append("Name: ").append(rs.getString("name")).append("\n")
                      .append("Father: ").append(rs.getString("father_name")).append("\n")
                      .append("Mother: ").append(rs.getString("mother_name")).append("\n")
                      .append("Email: ").append(rs.getString("email")).append("\n")
                      .append("DOB: ").append(rs.getString("dob")).append("\n")
                      .append("Gender: ").append(rs.getString("gender")).append("\n")
                      .append("Course ID: ").append(rs.getInt("course_id")).append("\n")
                      .append("Contact: ").append(rs.getString("contact_number")).append("\n")
                      .append("Address: ").append(rs.getString("address")).append("\n")
                      .append("Photo: ").append(rs.getString("photo_path")).append("\n")
                      .append("--------------------------\n");
                }

                outputArea.setText(sb.toString().isEmpty() ? "No student records found." : sb.toString());

                rs.close(); stmt.close(); conn.close();
            } catch (Exception ex) {
                outputArea.setText("DB Error: " + ex.getMessage());
            }
        });

        updateButton.addActionListener(e -> {
            String name = nameField.getText();
            String fatherName = fatherNameField.getText();
            String motherName = motherNameField.getText();
            String email = emailField.getText();
            String dobInput = dobField.getText();
            String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "");
            String contact = contactNumberField.getText();
            String photoPath = photoPathField.getText();
            String address = addressArea.getText();

            String dob;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = inputFormat.parse(dobInput);
                dob = dbFormat.format(parsedDate);
            } catch (Exception ex) {
                outputArea.setText("Invalid date format.");
                return;
            }

            int courseId;
            try {
                courseId = Integer.parseInt(courseIdField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid course ID.");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "UPDATE student SET father_name=?, mother_name=?, email=?, dob=?, gender=?, course_id=?, contact_number=?, address=?, photo_path=? WHERE name=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, fatherName);
                pst.setString(2, motherName);
                pst.setString(3, email);
                pst.setString(4, dob);
                pst.setString(5, gender);
                pst.setInt(6, courseId);
                pst.setString(7, contact);
                pst.setString(8, address);
                pst.setString(9, photoPath);
                pst.setString(10, name);

                int result = pst.executeUpdate();
                outputArea.setText(result > 0 ? " Student updated successfully." : "❌ Student not found.");
                pst.close(); conn.close();
            } catch (Exception ex) {
                outputArea.setText("Update Error: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            String name = nameField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "DELETE FROM student WHERE name=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, name);
                    int result = pst.executeUpdate();
                    outputArea.setText(result > 0 ? "Student deleted." : "❌ Student not found.");
                    pst.close(); conn.close();
                } catch (Exception ex) {
                    outputArea.setText("Delete Error: " + ex.getMessage());
                }
            }
        });

        searchButton.addActionListener(e -> {
            String selectedCriteria = (String) searchCriteriaBox.getSelectedItem();
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                outputArea.setText("Please enter a search value.");
                return;
            }

            String column = switch (selectedCriteria) {
                case "Name" -> "name";
                case "Email" -> "email";
                case "Student ID" -> "id";
                case "Course ID" -> "course_id";
                default -> "";
            };

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT * FROM student WHERE " + column + " LIKE ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                if (column.equals("id") || column.equals("course_id")) {
                    pst.setString(1, searchText);
                } else {
                    pst.setString(1, "%" + searchText + "%");
                }

                ResultSet rs = pst.executeQuery();
                StringBuilder sb = new StringBuilder();

                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id")).append("\n")
                      .append("Name: ").append(rs.getString("name")).append("\n")
                      .append("Father: ").append(rs.getString("father_name")).append("\n")
                      .append("Mother: ").append(rs.getString("mother_name")).append("\n")
                      .append("Email: ").append(rs.getString("email")).append("\n")
                      .append("DOB: ").append(rs.getString("dob")).append("\n")
                      .append("Gender: ").append(rs.getString("gender")).append("\n")
                      .append("Course ID: ").append(rs.getInt("course_id")).append("\n")
                      .append("Contact: ").append(rs.getString("contact_number")).append("\n")
                      .append("Address: ").append(rs.getString("address")).append("\n")
                      .append("Photo: ").append(rs.getString("photo_path")).append("\n")
                      .append("--------------------------\n");
                }

                outputArea.setText(sb.toString().isEmpty() ? "No matching student found." : sb.toString());
                rs.close(); pst.close(); conn.close();
            } catch (Exception ex) {
                outputArea.setText("Search Error: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new StudentForm();
    }
}