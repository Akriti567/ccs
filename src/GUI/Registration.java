package GUI;

import javax.swing.*;

import DBHelpers.ManagementDatabase;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Registration {
    private ManagementDatabase db = new ManagementDatabase();
    /**
     * @wbp.parser.entryPoint
     */

    public void showRegistrationFrame() {
        JFrame registrationWindow = new JFrame("Registration");
        registrationWindow.setLocation(300, 50);
        registrationWindow.setSize(500, 600);
        registrationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registrationWindow.setBackground(Color.decode("#d1e4e8"));

        JPanel editDetailsPanel = new JPanel();
        editDetailsPanel.setLayout(new BoxLayout(editDetailsPanel, BoxLayout.Y_AXIS));
        editDetailsPanel.setBackground(Color.decode("#d1e4e8")); // Set background color
        editDetailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#d1e4e8")), "Registration: "));

        JPanel editDetailsTitlePanel = new JPanel();
        editDetailsTitlePanel.setBackground(Color.decode("#049ab8")); // Set background color
         // Set text color
        JLabel editDetailTitle = new JLabel("Register as a Student");
        editDetailTitle.setForeground(Color.WHITE);
        editDetailTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        editDetailsTitlePanel.add(editDetailTitle);

        ResultSet modulesSet = db.getCourses();
        int i = 0;
        try {
            while (modulesSet.next()) {
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[] courses = new String[i];
        String[] courseTags = new String[i];
        modulesSet = db.getCourses();
        i = 0;
        try {
            while (modulesSet.next()) {
                courses[i] = modulesSet.getString("courseName");
                courseTags[i] = modulesSet.getString("courseTag");
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String[] levels = {"1", "2", "3"};
        JComboBox<String> coursesAvailable = new JComboBox<>(courses);
        JComboBox<String> levelComboBox = new JComboBox<>(levels);

        JPanel editDetailsBodyPanel = new JPanel();
        editDetailsBodyPanel.setLayout(new GridLayout(8, 2, 0, 32));
        editDetailsBodyPanel.setBackground(Color.decode("#d1e4e8")); // Set background color
        JLabel nameJLabel = new JLabel("Name: ");
        JTextField nameTextField = new JTextField();
        JLabel phoneJLabel = new JLabel("Phone No: ");
        JTextField phoneTextField = new JTextField();
        JLabel addressJLabel = new JLabel("Address: ");
        JTextField addressTextField = new JTextField();
        JLabel passwordJLabel = new JLabel("Password: ");
        JPasswordField passwordTextField = new JPasswordField();
        JLabel confirmPasswordJLabel = new JLabel("Confirm Password: ");
        JPasswordField confirmPasswordTextField = new JPasswordField();
        JLabel coursesAvailableJLabel = new JLabel("Choose Course: ");
        JLabel chooseLevelLabel = new JLabel("Level: ");

        editDetailsBodyPanel.add(new JLabel(""));
        editDetailsBodyPanel.add(new JLabel(""));
        editDetailsBodyPanel.add(nameJLabel);
        editDetailsBodyPanel.add(nameTextField);
        editDetailsBodyPanel.add(phoneJLabel);
        editDetailsBodyPanel.add(phoneTextField);
        editDetailsBodyPanel.add(addressJLabel);
        editDetailsBodyPanel.add(addressTextField);
        editDetailsBodyPanel.add(coursesAvailableJLabel);
        editDetailsBodyPanel.add(coursesAvailable);
        editDetailsBodyPanel.add(chooseLevelLabel);
        editDetailsBodyPanel.add(levelComboBox);
        editDetailsBodyPanel.add(passwordJLabel);
        editDetailsBodyPanel.add(passwordTextField);
        editDetailsBodyPanel.add(confirmPasswordJLabel);
        editDetailsBodyPanel.add(confirmPasswordTextField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.decode("#d1e4e8")); // Set background color
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setBackground(Color.decode("#09e653")); // Set button color
        submitButton.setForeground(Color.WHITE); // Set text color
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(Color.decode("#f5f12c")); // Set button color
        backButton.setForeground(Color.WHITE); // Set text color

        buttonsPanel.add(backButton);
        buttonsPanel.add(submitButton);

        editDetailsPanel.add(editDetailsTitlePanel);
        editDetailsPanel.add(editDetailsBodyPanel);
        editDetailsPanel.add(buttonsPanel);

        registrationWindow.getContentPane().add(editDetailsPanel);

        registrationWindow.setVisible(true);



        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = nameTextField.getText();
                String phoneNo = phoneTextField.getText();
                String address = addressTextField.getText();
                String courseTag = courseTags[coursesAvailable.getSelectedIndex()];
                String level = (String) levelComboBox.getSelectedItem();
                String password1 = String.valueOf(passwordTextField.getPassword());
                String password2 = String.valueOf(confirmPasswordTextField.getPassword());
                if (password1.equals(password2)) {
                    if (!password1.isEmpty() && !studentName.isEmpty() && !address.isEmpty() && !phoneNo.isEmpty()) {
                        String userId = generateUserId();
                        db.saveUserDetails(level, studentName, courseTag, "Fail", userId, "Student", password1, address, phoneNo);
                        JOptionPane.showMessageDialog(null, "Your username is : " + userId + "\nYour Password is " + password1 + "\n\nNote down these credentials or else you won't be able to login!!\n\n", "IMPORTANT MESSAGE!!!!!", JOptionPane.INFORMATION_MESSAGE);
                        registrationWindow.dispose();
                        Login login = new Login();
                        login.showLoginFrame();
                    } else {
                        JOptionPane.showMessageDialog(registrationWindow, "Please fill in all the boxes!", "Empty Inputs", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(registrationWindow, "Passwords do not match! Please try again.", "Passwords Invalid", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationWindow.dispose();
                Login login = new Login();
                login.showLoginFrame();
            }
        });


    }


    private String generateUserId() {

        int idNumber = 1;

        try {
            Scanner sc = new Scanner(new FileReader("src/Files/LogIn.txt"));

            while (sc.hasNextLine()) {
                Scanner sc2 = new Scanner(sc.nextLine());
                while (sc2.hasNext()) {
                    sc2.next();
                    sc2.next();
                    String type = sc2.next();
                    if (type.equals("Student")) {
                        idNumber++;
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (idNumber < 10) {
            return "ST0000" + idNumber;
        } else {
            return "ST000" + idNumber;
        }

    }
}
