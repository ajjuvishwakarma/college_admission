package collegeadmission.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("College Admission Management - Dashboard");

        // A4 Size : 794 x 1123
        setSize(794, 900); // Increased height for new button
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(220, 255, 220)); // Light Green

        // Top Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 128, 0)); // Dark Green
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // LOGO (left)
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/apsu logo.jpeg"));
            Image logoImg = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
            topPanel.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println("Logo not found. Make sure /images/apsu logo.jpeg exists.");
        }

        // Title (center)
        JLabel welcomeLabel = new JLabel("Welcome to Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Login & Logout (right)
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonsPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 13));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 13));

        rightButtonsPanel.add(loginButton);
        rightButtonsPanel.add(logoutButton);
        topPanel.add(rightButtonsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(220, 255, 220));
        buttonPanel.setBorder(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        Dimension btnSize = new Dimension(180, 35);

        JButton studentBtn = new JButton("Student Form");
        JButton docBtn = new JButton("Documents Form");
        JButton admissionBtn = new JButton("Admission Form");
        JButton paymentBtn = new JButton("Payment Form");
        JButton adminBtn = new JButton("Admin Form");
        JButton courseBtn = new JButton("Course Form");
        JButton amountBtn = new JButton("Amount Form"); // ✅ New Button
        JButton printBtn = new JButton("Print");
        JButton pdfBtn = new JButton("Export to PDF");

        JButton[] buttons = {
            studentBtn, docBtn, admissionBtn, paymentBtn,
            adminBtn, courseBtn, amountBtn, printBtn, pdfBtn
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(btnSize);
            btn.setBackground(new Color(200, 255, 200));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        gbc.gridx = 0; gbc.gridy = 0; buttonPanel.add(studentBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 0; buttonPanel.add(docBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 1; buttonPanel.add(admissionBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 1; buttonPanel.add(paymentBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 2; buttonPanel.add(adminBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 2; buttonPanel.add(courseBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 3; buttonPanel.add(amountBtn, gbc); // ✅ Added
        gbc.gridx = 1; gbc.gridy = 3; buttonPanel.add(printBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 4; buttonPanel.add(pdfBtn, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        studentBtn.addActionListener(e -> new StudentForm());
        docBtn.addActionListener(e -> new DocumentsForm());
        admissionBtn.addActionListener(e -> new AdmissionForm());
        paymentBtn.addActionListener(e -> new PaymentForm());
        adminBtn.addActionListener(e -> new AdminForm());
        courseBtn.addActionListener(e -> new courseform());
        amountBtn.addActionListener(e -> new AmountForm()); // ✅ Linked Action

        loginButton.addActionListener(e -> new LoginForm());

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginForm();
            }
        });

        // Print Feature
        printBtn.addActionListener(e -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Dashboard Print");

            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                printAll(g2);
                return Printable.PAGE_EXISTS;
            });

            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // PDF Export Feature
        pdfBtn.addActionListener(e -> {
            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream("Dashboard.pdf"));
                document.open();

                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Paragraph title = new Paragraph("College Admission Management Dashboard\n\n", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);
                document.add(new Paragraph("Available Forms:", font));
                document.add(new Paragraph(" - Student Form", font));
                document.add(new Paragraph(" - Documents Form", font));
                document.add(new Paragraph(" - Admission Form", font));
                document.add(new Paragraph(" - Payment Form", font));
                document.add(new Paragraph(" - Admin Form", font));
                document.add(new Paragraph(" - Course Form", font));
                document.add(new Paragraph(" - Amount Form", font));

                document.add(new Paragraph("\nExported on: " + new java.util.Date(), font));
                document.close();

                JOptionPane.showMessageDialog(this, "Dashboard exported to PDF successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}