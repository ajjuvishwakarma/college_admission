package collegeadmission.com;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.awt.Desktop;

public class DocumentsForm extends JFrame {
    private JTextField studentIdField, filePathField, uploadDateField;
    private JComboBox<String> documentTypeCombo;
    private JButton submitButton, browseButton, openFileButton, updateButton, deleteButton;
    private DefaultListModel<String> listModel;
    private JList<String> documentList;
    private HashMap<String, String> fileMap;

    public DocumentsForm() {
        setTitle("Uploaded Documents – College Admission System");
        setSize(700, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel studentLabel = new JLabel("Student ID:");
        studentLabel.setFont(labelFont);
        studentIdField = new JTextField(20);
        studentIdField.setFont(fieldFont);

        ImageIcon icon = new ImageIcon("doc_icon.png"); // optional icon
        JLabel documentTypeLabel = new JLabel("Document Type:", icon, JLabel.LEFT);
        documentTypeLabel.setFont(labelFont);
        String[] documentTypes = {
            "Select Document Type", "Aadhaar Card", "Marksheet", "PAN Card", "Samagra ID",
            "Transfer Certificate", "Migration Certificate", "Domicile",
            "Income Certificate", "Caste Certificate", "Others"
        };
        documentTypeCombo = new JComboBox<>(documentTypes);
        documentTypeCombo.setFont(fieldFont);
        documentTypeCombo.setPreferredSize(new Dimension(200, 25));

        JLabel filePathLabel = new JLabel("File Path:");
        filePathLabel.setFont(labelFont);
        filePathField = new JTextField(15);
        filePathField.setFont(fieldFont);
        browseButton = new JButton("Browse");

        JLabel uploadDateLabel = new JLabel("Upload Date (dd-MM-yyyy):");
        uploadDateLabel.setFont(labelFont);
        uploadDateField = new JTextField(20);
        uploadDateField.setFont(fieldFont);

        Color btnColor = new Color(70, 130, 180);
        Color btnTextColor = Color.WHITE;
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        submitButton = new JButton("Upload");
        openFileButton = new JButton("Open File");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        JButton[] buttons = {submitButton, openFileButton, updateButton, deleteButton, browseButton};
        for (JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setForeground(btnTextColor);
            btn.setFocusPainted(false);
            btn.setFont(btnFont);
        }

        listModel = new DefaultListModel<>();
        documentList = new JList<>(listModel);
        documentList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(documentList);
        scrollPane.setPreferredSize(new Dimension(300, 120));
        fileMap = new HashMap<>();

        gbc.gridx = 0; gbc.gridy = 0;
        add(studentLabel, gbc);
        gbc.gridx = 1;
        add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(documentTypeLabel, gbc);
        gbc.gridx = 1;
        add(documentTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(filePathLabel, gbc);
        gbc.gridx = 1;
        add(filePathField, gbc);
        gbc.gridx = 2;
        add(browseButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(uploadDateLabel, gbc);
        gbc.gridx = 1;
        add(uploadDateField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(240, 248, 255));
        actionPanel.add(submitButton);
        actionPanel.add(openFileButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        add(actionPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel listLabel = new JLabel("Uploaded Documents:");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(listLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        add(scrollPane, gbc);

        // 👇 UPDATED: Browse button with file filter
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Supported Files (jpg, jpeg, png, pdf, docx)", 
                "jpg", "jpeg", "png", "pdf", "docx"
            );
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(DocumentsForm.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        submitButton.addActionListener(e -> {
            if (validateStudentId()) uploadDocument();
        });

        openFileButton.addActionListener(e -> openSelectedFile());

        studentIdField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (validateStudentId()) loadDocumentsForStudent();
            }
        });

        updateButton.addActionListener(e -> {
            if (validateStudentId()) updateDocument();
        });

        deleteButton.addActionListener(e -> {
            if (validateStudentId()) deleteDocument();
        });

        setVisible(true);
    }

    private boolean validateStudentId() {
        String input = studentIdField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID.");
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number.");
            return false;
        }
    }

    private boolean doesFileMatchDocumentType(String docType, String filePath) {
        filePath = filePath.toLowerCase();
        docType = docType.toLowerCase();

        switch (docType) {
            case "aadhaar card":
                return filePath.contains("aadhaar");
            case "pan card":
                return filePath.contains("pan");
            case "marksheet":
                return filePath.contains("marksheet") || filePath.contains("mark");
            case "samagra id":
                return filePath.contains("samagra");
            case "transfer certificate":
                return filePath.contains("transfer");
            case "migration certificate":
                return filePath.contains("migration");
            case "domicile":
                return filePath.contains("domicile");
            case "income certificate":
                return filePath.contains("income");
            case "caste certificate":
                return filePath.contains("caste");
            case "others":
                return true;
            default:
                return false;
        }
    }

    private void uploadDocument() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            String documentType = documentTypeCombo.getSelectedItem().toString();
            String filePath = filePathField.getText().trim();
            String inputDate = uploadDateField.getText().trim();

            if (documentType.equals("Select Document Type")) {
                JOptionPane.showMessageDialog(this, "Please select a document type.");
                return;
            }

            if (filePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a file.");
                return;
            }

            if (!doesFileMatchDocumentType(documentType, filePath)) {
                JOptionPane.showMessageDialog(this, "Selected file does not match the document type: " + documentType);
                return;
            }

            SimpleDateFormat userFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = userFormat.parse(inputDate);
            String formattedDate = dbFormat.format(date);

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");

            if (!studentExists(conn, studentId)) {
                JOptionPane.showMessageDialog(this, "Student ID " + studentId + " does not exist.");
                conn.close();
                return;
            }

            String sql = "INSERT INTO documents(student_id, document_type, file_path, upload_date) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setString(2, documentType);
            stmt.setString(3, filePath);
            stmt.setString(4, formattedDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Document uploaded successfully!");
                loadDocumentsForStudent();
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Upload failed: " + ex.getMessage());
        }
    }

    private boolean studentExists(Connection conn, int studentId) {
        try {
            String query = "SELECT id FROM student WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking student ID: " + e.getMessage());
            return false;
        }
    }

    private void openSelectedFile() {
        String selectedDoc = documentList.getSelectedValue();
        if (selectedDoc == null) {
            JOptionPane.showMessageDialog(this, "Please select a document from the list.");
            return;
        }

        String filePath = fileMap.get(selectedDoc);
        if (filePath != null && new File(filePath).exists()) {
            try {
                Desktop.getDesktop().open(new File(filePath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open file: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "File not found at: " + filePath);
        }
    }

    private void loadDocumentsForStudent() {
        listModel.clear();
        fileMap.clear();

        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");

            String sql = "SELECT document_type, file_path FROM documents WHERE student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("document_type");
                String path = rs.getString("file_path");

                listModel.addElement(type);
                fileMap.put(type, path);
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading documents: " + e.getMessage());
        }
    }

    private void updateDocument() {
        String selectedType = documentList.getSelectedValue();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a document to update.");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            String newType = documentTypeCombo.getSelectedItem().toString();
            String newPath = filePathField.getText().trim();
            String inputDate = uploadDateField.getText().trim();

            SimpleDateFormat userFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = userFormat.parse(inputDate);
            String formattedDate = dbFormat.format(date);

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");

            String sql = "UPDATE documents SET document_type=?, file_path=?, upload_date=? WHERE student_id=? AND document_type=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newType);
            stmt.setString(2, newPath);
            stmt.setString(3, formattedDate);
            stmt.setInt(4, studentId);
            stmt.setString(5, selectedType);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Document updated successfully.");
                loadDocumentsForStudent();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }

            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating document: " + ex.getMessage());
        }
    }

    private void deleteDocument() {
        String selectedType = documentList.getSelectedValue();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a document to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete selected document?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");

                String sql = "DELETE FROM documents WHERE student_id=? AND document_type=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, studentId);
                stmt.setString(2, selectedType);

                int deleted = stmt.executeUpdate();
                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Document deleted successfully.");
                    loadDocumentsForStudent();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed.");
                }

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting document: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new DocumentsForm();
    }
}