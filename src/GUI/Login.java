package GUI;

import javax.swing.*;

import Bodies.Admin;
import DBHelpers.ManagementDatabase;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Login {
    private JFrame window = new JFrame();
    private JTextField usernameTextField = new JTextField(20);
    private JPasswordField passwordTextField = new JPasswordField(20);
    private String[] userTypes = {"Student", "Teacher", "Administrator"};
    private JComboBox<String> selectUserType = new JComboBox<>(userTypes);
    private Boolean isValidLogin = false;
    private Admin admin = new Admin();
    private ManagementDatabase db = new ManagementDatabase();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.showLoginFrame();
        });
    }
    /**
     * @wbp.parser.entryPoint
     */


    public void showLoginFrame() {
        JFrame loginFrame = new JFrame("Log in");
        loginFrame.setSize(600, 300); // Increased size of the login box
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#ad7224")); // Set background color
        JLabel title = new JLabel("Course Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE); // Set text color
        titlePanel.add(title);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.decode("#f59002"));
        loginButton.setForeground(Color.WHITE);
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.decode("#06a13a"));
        registerButton.setForeground(Color.WHITE);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new GridLayout(5, 2, 8, 10));
        bodyPanel.setBackground(Color.decode("#dbcdba")); // Set background color
        bodyPanel.add(new JLabel("  Username :"));
        bodyPanel.add(usernameTextField);
        bodyPanel.add(new JLabel("  Password :"));
        bodyPanel.add(passwordTextField);
        bodyPanel.add(new JLabel("  User Type :"));
        bodyPanel.add(selectUserType);
        bodyPanel.add(registerButton);
        bodyPanel.add(loginButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.decode("#ad7224")); // Set background color
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        loginFrame.getContentPane().add(mainPanel);
        loginFrame.setVisible(true);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = String.valueOf(passwordTextField.getPassword());
                String userType = (String) selectUserType.getSelectedItem();
                if (!userType.equals("Administrator")) {
                    try {
                        Scanner sc = new Scanner(new FileReader("src/Files/LogIn.txt"));
                        while (sc.hasNextLine()) {
                            Scanner sc2 = new Scanner(sc.nextLine());
                            while (sc2.hasNext()) {
                                String id = sc2.next();
                                String pw = sc2.next();
                                String type = sc2.next();
                                if (username.equals(id) && password.equals(pw) && userType.equals(type)) {
                                    isValidLogin = true;
                                    break;
                                }
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        Scanner sc = new Scanner(new FileReader("src/Files/Admin.txt"));
                        while (sc.hasNext()) {
                            String adminId = sc.next();
                            String pw = sc.next();
                            admin.setAdminId(adminId);
                            admin.setPassword(pw);
                            if (username.equals(admin.getAdminId()) && password.equals(admin.getPassword())) {
                                isValidLogin = true;
                                userType = "Administrator";
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }

                if (isValidLogin) {
                    usernameTextField.setText("");
                    passwordTextField.setText("");
                    selectUserType.setSelectedIndex(0);
                    loginFrame.dispose();
                    showFrame(username, password, userType);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Username or Password!");
                }
            }
        });


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
                Registration registration = new Registration();
                registration.showRegistrationFrame();
            }
        });

    }

    public void showFrame(String username, String password, String userType) {
        window = new JFrame();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/GUI/TitleLogo.png");
        window.setIconImage(icon);
        window.setTitle("Course Management System");
        window.setSize(1600, 1000);
        window.setExtendedState(Frame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton logoutButton = new JButton("Log Out");

        if(userType.equals ("Student")){
            StudentPanel panel1 = new StudentPanel(username, password, logoutButton);
            window.getContentPane().add(panel1);
        } else if(userType.equals ("Teacher")){
            TeacherPanel panel2 = new TeacherPanel(username, password, logoutButton);
            window.getContentPane().add(panel2);
        } else if(userType.equals ("Administrator")){
            AdminPanel panel3 = new AdminPanel(logoutButton);
            window.getContentPane().add(panel3);
        }

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int logout = JOptionPane.showConfirmDialog(window, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (logout == JOptionPane.YES_OPTION) {
                    window.dispose();
                    showLoginFrame();
                }
            }
        });

        window.setVisible(true);

    }
}
