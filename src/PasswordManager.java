import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea resultArea;
    private JButton saveButton;
    private JButton retrieveButton;
    private JButton passwordGeneratorButton;
    private JButton passwordStrengthButton;
    private JButton developerButton;
    private JButton donateButton; // New button for donation

    private JCheckBox showPasswordCheckBox;
    private Map<String, String> passwordMap;

    public PasswordManager() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        showPasswordCheckBox = new JCheckBox("Show Password");

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel());
        inputPanel.add(showPasswordCheckBox);
        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePassword();
            }
        });
        retrieveButton = new JButton("Retrieve");
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrievePassword();
            }
        });
        passwordGeneratorButton = new JButton("Password Generator");
        passwordGeneratorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });
        passwordStrengthButton = new JButton("Check Strength");
        passwordStrengthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPasswordStrength();
            }
        });
        developerButton = new JButton("Developer");
        developerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeveloperInfo();
            }
        });
        donateButton = new JButton("Donate"); // New button for donation
        donateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDonationPage();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(passwordGeneratorButton);
        buttonPanel.add(passwordStrengthButton);
        buttonPanel.add(developerButton);
        buttonPanel.add(donateButton); // Add the donate button
        add(buttonPanel, BorderLayout.CENTER);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.SOUTH);

        passwordMap = new HashMap<>();

        showPasswordCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                toggleShowPassword();
            }
        });

        setVisible(true);
    }

    private void toggleShowPassword() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('\u2022');
        }
    }

    private void savePassword() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!username.isEmpty() && !password.isEmpty()) {
            passwordMap.put(username, password);
            resultArea.setText("Password saved for username: " + username);
        } else {
            resultArea.setText("Please enter a username and password.");
        }

        clearFields();
    }

    private void retrievePassword() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            String password = passwordMap.get(username);
            if (password != null) {
                resultArea.setText("Password for username " + username + ": " + password);
            } else {
                resultArea.setText("No password found for username: " + username);
            }
        } else {
            resultArea.setText("Please enter a username.");
        }

        clearFields();
    }

    private void generatePassword() {
        String password = generateRandomPassword(10);
        passwordField.setText(password);
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private void checkPasswordStrength() {
        String password = new String(passwordField.getPassword());
        int score = calculatePasswordStrength(password);
        String strength;
        if (score < 50) {
            strength = "Weak";
        } else if (score < 80) {
            strength = "Medium";
        } else {
            strength = "Strong";
        }
        resultArea.setText("Password Strength: " + strength);
    }

    private int calculatePasswordStrength(String password) {
        int score = 0;
        if (password.length() >= 8) {
            score += 10;
            if (containsLowerCase(password)) {
                score += 10;
            }
            if (containsUpperCase(password)) {
                score += 10;
            }
            if (containsDigit(password)) {
                score += 10;
            }
            if (containsSpecialCharacter(password)) {
                score += 10;
            }
            if (containsUniqueCharacters(password)) {
                score += 10;
            }
        }
        return score;
    }

    private boolean containsLowerCase(String password) {
        return password.matches(".*[a-z].*");
    }

    private boolean containsUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[^a-zA-Z0-9].*");
    }

    private boolean containsUniqueCharacters(String password) {
        return password.chars().distinct().count() >= password.length() * 0.9;
    }

    private void showDeveloperInfo() {
        resultArea.setText("This Tool Is Developed By Sumalya Chatterjee");
    }

    private void openDonationPage() {
        String donationURL = "https://www.buymeacoffee.com/r3dhulk"; // Replace with your donation page URL
        try {
            Desktop.getDesktop().browse(new URI(donationURL));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PasswordManager();
            }
        });
    }
}
