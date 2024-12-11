import javax.swing.*; // Provides GUI components 
import java.awt.*; // Provides Layout Utilities/Colors and system tray handling
import java.awt.event.ActionEvent; // Handling User input
import java.awt.event.ActionListener; // Handling User input
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Finalproject {
    private JFrame frame; // Frame Variable
    private JTextField textField; // Displays User input
    private int timeInSeconds;  // Time in seconds
    private Timer countdownTimer; // Timer updates every seconds
    private JLabel timeLabel; // Displays Remaining time
    private JButton button; // Represent Numeric Button/Functional Button
    private ImageIcon img; // Application Icon
    private boolean tenMinutesWarningShown = false; // 10 minute notification
    private boolean oneMinuteWarningShown = false; // 1 minute notification 
    private SystemTray systemTray;  // System tray 
    private TrayIcon trayIcon; // Icon for system tray
    private JButton salesBtn;
    private static boolean allowClose = true; // Control flag for closing the frame
    
    // Title screen components
    private JPanel titlePanel;
    private JButton startButton;
    private JTextArea moneyInfo;

    public Finalproject() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 390);
        frame.setResizable(false);
        frame.setTitle("TeChronos");
       
        
        //ImageLogo
    	img = new ImageIcon(getClass().getClassLoader().getResource("hglass.png"));
    	frame.setIconImage(img.getImage());
    	
        // Set the layout to BorderLayout to place components
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //pressing close button when app is running will do nothing so long as there is time in the timer

        // Title Screen setup
        setupTitleScreen();

// Add a WindowListener to handle the close event
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (allowClose) {
                    frame.dispose(); // Allow closing the frame
                } else {
                    
                }
            }
        });

        // Display the title screen first
        frame.setVisible(true);
    	
    }
    // Method to setup the Title Screen
    private void setupTitleScreen() {
    	
        titlePanel = new JPanel();
        //titlePanel.setLayout(new BorderLayout());
        titlePanel.setLayout(null);
        // Create the welcome label
        Color colBg = new Color(247, 239, 210);
        titlePanel.setBackground(colBg);
        ImageIcon mushCloud = new ImageIcon("./res/hglass.png");
        JLabel welcomeLabel = new JLabel("TeChronos", JLabel.CENTER);
        Image image = mushCloud.getImage();
        Image newImg = image.getScaledInstance(50, 60, java.awt.Image.SCALE_SMOOTH);
        mushCloud = new ImageIcon(newImg);
        
        JLabel icons = new JLabel("", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setPreferredSize(new Dimension(300, 50));
        welcomeLabel.setBounds(90, 90, 130, 80);
        icons.setIcon(mushCloud);
        icons.setBounds(10, 50, 130, 150);
        
        titlePanel.add(welcomeLabel, BorderLayout.NORTH);
        titlePanel.add(icons, BorderLayout.NORTH);

        //Money equivalent information
        moneyInfo = new JTextArea();
        moneyInfo.setText(
            
            "          Money Equivalent:\n" +
            "₱10 = 1 hour ₱20 = 2 hours\n" +
            "₱30 = 3 hours ₱40 = 4 hours\n" +
            "            ₱50 = 5 hours"
        );
        moneyInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        moneyInfo.setEditable(false);
        moneyInfo.setBackground(titlePanel.getBackground());
        moneyInfo.setBounds(65, 270, 200, 120);
        titlePanel.add(moneyInfo, BorderLayout.CENTER); 

        // Start button
        Color butCol = new Color(205, 139, 98);
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(67, 170, 150, 40);
        startButton.setBackground(butCol);
        startButton.setFocusPainted(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Once the "Start" button is pressed, set up the main screen
                titlePanel.setVisible(false); // Hide title screen
                setupMainScreen(); // Setup the main screen
            }
        });

        titlePanel.add(startButton, BorderLayout.SOUTH);
        frame.add(titlePanel, BorderLayout.CENTER);
    }
    private void setupMainScreen() {
        // Remove the title screen and show the main GUI
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // End button
        Color butCol = new Color(205, 139, 98);
        JButton endButton = new JButton("END");
        salesBtn = new JButton("Sales");
        salesBtn.setBackground(butCol);
        salesBtn.setBounds(191, 310, 81, 20);
        salesBtn.setFont(new Font("Arial", Font.PLAIN, 15)); 
        
      
        // Add action listener for "Add Sales" button
        salesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSales();
            }
        });
        
        endButton.setBackground(butCol);
        endButton.setFocusable(false);
        endButton.setBounds(101, 280, 81, 20);
        endButton.setForeground(Color.BLACK);
        endButton.setFont(new Font("Arial", Font.PLAIN, 20));       
       endButton.addActionListener((ActionEvent e) -> {
           if (timeInSeconds <= 0) { //checks if time is 0
               JOptionPane.showMessageDialog(frame, "No time has been allotted. Please allocate time first."); //Displays if there's no alloted time
               return;
           }
           
           countdownTimer.stop(); // Stop the timer
           timeInSeconds = 0;     // Reset time
           textField.setText(""); // Clear the input field
           timeLabel.setText("Time remaining: 00:00:00");
           allowClose = true;
           lockPC(); // Lock the PC
        });
        

        // Create text field to show the input
    	Color txtFieldCol = new Color(247, 239, 230);
    	
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setBackground(txtFieldCol);
        textField.setEditable(false); // Make text field non-editable
        

        // Create label to show the remaining time
        Color tLabelCol = new Color(247, 239, 210);
        
        timeLabel = new JLabel("Time remaining: ", JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setBackground(tLabelCol);
        
        

        // Create panel for numeric buttons
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(5, 3, 10, 10)); // 4 rows and 3 columns
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add number buttons (0-9)
        String[] buttonLabels = {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "C", "0", "Enter"
        };

        for (String label : buttonLabels) {
        	
        	Color labelCol = new Color(12, 0 ,0);
        	Color buttonCol = new Color(205, 139, 98);
        	Color bgCol = new Color(247, 239, 210);
        	
            button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(new ButtonClickListener(label));
            button.setFocusPainted(false);
            button.setForeground(labelCol);
            button.setBackground(buttonCol);
            
            keypadPanel.setBackground(bgCol);
            keypadPanel.add(button);
        }

        // Layout setup       
        frame.setLayout(new BorderLayout());
        frame.add(endButton);
        frame.add(salesBtn);
        frame.add(textField, BorderLayout.NORTH);
        frame.add(timeLabel, BorderLayout.SOUTH); // Add time labeL
        frame.add(keypadPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray(); // Get system tray instance
            trayIcon = new TrayIcon(new ImageIcon("./res/hglass.png").getImage(), "TeChronos"); // Icon for tray
            trayIcon.setImageAutoSize(true); // set to true so img will resize to tray icon

            // Add a listener to handle click events on the tray icon 
            trayIcon.addActionListener((ActionEvent e) -> {
                // Restore the window when the user clicks on the tray icon
                frame.setVisible(true);
                frame.toFront();
            });

            // Set up the system tray 
            try {
                systemTray.add(trayIcon); // Add the icon to the system tray
            } catch (AWTException e) {

            }
        }
    }

    // ActionListener for button clicks
    private class ButtonClickListener implements ActionListener {
        private final String label;

        public ButtonClickListener(String label) {
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (label) {
                case "Enter" -> {
                    try {
                        // Get the entered time and convert to seconds
                        int input = Integer.parseInt(textField.getText());
                        
                        // Validate and map the input to the corresponding time in seconds
                        int timeToAddInSeconds = 0;
                        switch (input) {
                            case 50:
                                timeToAddInSeconds = 60 * 60 * 5; // 5 hours in seconds
                                break;
                            case 40:
                                timeToAddInSeconds = 60 * 60 * 4; // 4 hours in seconds
                                break;
                            case 30:
                                timeToAddInSeconds = 60 * 60 * 3; // 3 hours in seconds
                                break;
                            case 20:
                                timeToAddInSeconds = 60 * 60 * 2; // 2 hours in seconds
                                break;
                            case 10:
                                timeToAddInSeconds = 60 * 60; // 1 hour in seconds
                                break;
                            case 1:
                                timeToAddInSeconds = 60 * 2; // 2 minutes in seconds
                                break;
                            case 2:
                                timeToAddInSeconds = 60 * 11; // 10 minutes in seconds
                                break;
                            default:
                                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                                return;
                        }
                        
                        // Accumulate the new time to the current countdown time
                        timeInSeconds += timeToAddInSeconds;
                        
                        // Start or continue the countdown timer
                        if (countdownTimer == null || !countdownTimer.isRunning()) {
                            startCountdown();
                        }
                        saveSales();
                        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
                    }
                }
                case "C" -> textField.setText(""); // Clear text field
                default -> textField.setText(textField.getText() + label); // Append number
            }
        }
    }
    
    private void saveSales() {
        // File name
        String fileName = "sales.txt";

        // Retrieve sales data from the text field
        int sales;
        try {
            sales = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a numeric value.");
            return; // Exit the method if the input is invalid
        }

        // Prepare data to write to the file
        String data = "Total Sales: " + sales + System.lineSeparator(); // Add a newline for separation

        try {
            // Write sales data to the file
            try ( // Create a FileWriter object in append mode
                    FileWriter writer = new FileWriter(fileName, true)) {
                // Write sales data to the file
                writer.write(data);
                // Close the FileWriter to release resources
            }

            System.out.println("Sales data appended successfully!");

        } catch (IOException e) {
            // Handle exceptions
            System.out.println("An error occurred while writing to the file.");

        }
    }

    private void viewSales() {
        String fileName = "sales.txt";
        int totalSales = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder salesData = new StringBuilder("");
            while ((line = reader.readLine()) != null) {
                try {
                    String saleString = line.replaceFirst("Total Sales: ", "");
                    int sale = Integer.parseInt(saleString);
                    totalSales += sale;
                } catch (NumberFormatException e) {
                    salesData.append("Invalid data, skipping: ").append(line).append("\n");
                }
            }
            salesData.append("Total Sales: P ").append(totalSales);
            // Display sales data with a reset option
            int option = JOptionPane.showOptionDialog(
                    null,
                    salesData.toString(),
                    "Sales Data",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Close", "Reset"}, // Custom buttons
                    "Close"
            );

            if (option == 1) { // Reset button clicked
                // Reset the sales data
                clearSales();

                // Show message after reset
                JOptionPane.showMessageDialog(null, "Sales Data Cleared.", "Sales Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearSales() {
        File file = new File("sales.txt");

        try (FileWriter writer = new FileWriter(file)) {
            // Overwriting the file with an empty string
            writer.write("");
            System.out.println("File content deleted successfully.");
        } catch (IOException e) {
        }
    }

    // Start the countdown timer
    private void startCountdown() {
        countdownTimer = new Timer(1000, (ActionEvent e) -> {
            if (timeInSeconds > 0) {
                allowClose = false;
                timeInSeconds--;  // Decrement the time by 1 second
                int hours = timeInSeconds / 3600;
                int minutes = (timeInSeconds % 3600) / 60;
                int seconds = timeInSeconds % 60;
                timeLabel.setText(String.format("Time remaining: %02d:%02d:%02d", hours, minutes, seconds));
                
                // If 10 minutes are left, show a message
                if (timeInSeconds == 60 * 10 && !tenMinutesWarningShown) {
                    // Use a non-blocking JOptionPane so that the timer will still work even though there is a warning
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(frame, "Only 10 minutes left!");
                        }
                    });
                    tenMinutesWarningShown = false; // Set flag to false so it shows warning everytime
                }
                
                if (timeInSeconds == 60 && !oneMinuteWarningShown) {
                    // Use a non-blocking JOptionPane so that the timer will still work even though there is a warning
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(frame, "Only 1 minute left!");
                        }
                    });
                    
                    oneMinuteWarningShown = false; // Set flag to false so it shows warning everytime
                }
            }
            if (timeInSeconds == 0) {
                countdownTimer.stop();
                lockPC(); // Lock the PC
            }
        });
        countdownTimer.start();
    }

    // Lock the computer
    private void lockPC() {
        try {
            Runtime.getRuntime().exec("rundll32 user32.dll,LockWorkStation");
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Finalproject finalproject = new Finalproject();
        });
    }
}
