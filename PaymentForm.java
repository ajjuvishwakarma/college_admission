package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PaymentForm extends JFrame {
    private JTextField paymentIdField, studentIdField, amountIdField, amountField, paymentDateField;
    private JComboBox<String> methodBox;
    private JButton submitButton, clearButton, backButton;

    public PaymentForm() {
        setTitle("Student Payment Form");
        setSize(600, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Sky Blue theme colors
        Color bgColor = new Color(135, 206, 235); // Sky Blue
        Color labelColor = Color.BLACK;
        Color fieldBg = Color.WHITE;
        Color fieldFg = Color.BLACK;
        Color buttonColor = new Color(30, 144, 255); // Dodger Blue
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels
        JLabel paymentIdLabel = new JLabel("Payment ID (Auto):");
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel amountIdLabel = new JLabel("Amount ID:");
        JLabel amountLabel = new JLabel("Amount (₹):");
        JLabel paymentDateLabel = new JLabel("Payment Date (dd-MM-yyyy):");
        JLabel methodLabel = new JLabel("Payment Method:");

        for (JLabel lbl : new JLabel[]{paymentIdLabel, studentIdLabel, amountIdLabel, amountLabel, paymentDateLabel, methodLabel}) {
            lbl.setForeground(labelColor);
            lbl.setFont(font);
        }

        // Fields
        paymentIdField = new JTextField(15);
        paymentIdField.setEditable(false);
        studentIdField = new JTextField(15);
        amountIdField = new JTextField(15);
        amountField = new JTextField(15);
        amountField.setEditable(false); // fetched from DB
        paymentDateField = new JTextField(15);

        JTextField[] allFields = {paymentIdField, studentIdField, amountIdField, amountField, paymentDateField};
        for (JTextField tf : allFields) {
            tf.setBackground(fieldBg);
            tf.setForeground(fieldFg);
            tf.setFont(font);
            tf.setCaretColor(Color.BLACK);
        }

        String[] methods = {"Cash", "UPI", "Card", "Net Banking"};
        methodBox = new JComboBox<>(methods);
        methodBox.setBackground(fieldBg);
        methodBox.setForeground(fieldFg);
        methodBox.setFont(font);

        // Buttons
        submitButton = new JButton("Submit Payment");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        for (JButton btn : new JButton[]{submitButton, clearButton, backButton}) {
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        // Layout positions
        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; panel.add(paymentIdLabel, gbc);
        gbc.gridx = 1; panel.add(paymentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; panel.add(studentIdLabel, gbc);
        gbc.gridx = 1; panel.add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; panel.add(amountIdLabel, gbc);
        gbc.gridx = 1; panel.add(amountIdField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; panel.add(amountLabel, gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; panel.add(paymentDateLabel, gbc);
        gbc.gridx = 1; panel.add(paymentDateField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; panel.add(methodLabel, gbc);
        gbc.gridx = 1; panel.add(methodBox, gbc);

        gbc.gridx = 1; gbc.gridy = ++y;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        gbc.gridy = ++y;
        panel.add(clearButton, gbc);

        gbc.gridy = ++y;
        panel.add(backButton, gbc);

        // Add panel to frame
        add(panel);

        // Events
        submitButton.addActionListener(e -> insertPayment());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> dispose());
        amountIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fetchAmount();
            }
        });

        generateNextPaymentId();
        setVisible(true);
    }

    private void generateNextPaymentId() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(payment_id) FROM payment");
            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
            paymentIdField.setText(String.valueOf(nextId));
            con.close();
        } catch (Exception ex) {
            paymentIdField.setText("Auto");
        }
    }

    private void fetchAmount() {
        String amountId = amountIdField.getText().trim();
        if (amountId.isEmpty()) {
            amountField.setText("");
            return;
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");
            PreparedStatement ps = con.prepareStatement("SELECT amount FROM amount WHERE amount_id=?");
            ps.setString(1, amountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                amountField.setText(rs.getString("amount"));
            } else {
                amountField.setText("Not Found");
            }
            con.close();
        } catch (Exception ex) {
            amountField.setText("Error");
        }
    }

    private void insertPayment() {
        String studentId = studentIdField.getText().trim();
        String amountId = amountIdField.getText().trim();
        String amount = amountField.getText().trim();
        String paymentDateStr = paymentDateField.getText().trim();
        String method = (String) methodBox.getSelectedItem();

        if (studentId.isEmpty() || amountId.isEmpty() || paymentDateStr.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:4306/college_admission_db", "root", "Ajay2001*");

            PreparedStatement checkStmt = con.prepareStatement("SELECT id FROM student WHERE id=?");
            checkStmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Student ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(paymentDateStr);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            PreparedStatement stmt = con.prepareStatement("INSERT INTO payment(student_id, amount_id, payment_date, method) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setString(2, amountId);
            stmt.setDate(3, sqlDate);
            stmt.setString(4, method);

            int i = stmt.executeUpdate();
            if (i > 0) {
                JOptionPane.showMessageDialog(this, "Payment inserted successfully.");
                clearFields();
                generateNextPaymentId();
            } else {
                JOptionPane.showMessageDialog(this, "Payment insertion failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        amountIdField.setText("");
        amountField.setText("");
        paymentDateField.setText("");
        methodBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new PaymentForm();
    }
}