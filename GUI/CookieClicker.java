package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class CookieClicker {
    private int cookieCount = 0;
    private int cookiesPerClick = 1;
    private int upgradeCost = 10;
    private int passiveIncome = 0;
    private int grandmaCount = 0, grandmaCost = 50;
    private int cookieLabCount = 0, cookieLabCost = 200;
    private int cookieFactoryCount = 0, cookieFactoryCost = 500;
    private int rebirthCost = 1000;
    private int rebirthMultiplier = 1;

    private JLabel cookieCountLabel;
    private JLabel cpsLabel;
    private JButton clickButton;
    private JButton upgradeButton;
    private JButton grandmaButton;
    private JButton cookieLabButton;
    private JButton cookieFactoryButton;
    private JButton rebirthButton;

    public CookieClicker() {
        // Create the main frame
        JFrame frame = new JFrame("Cookie Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Cookie count label
        cookieCountLabel = new JLabel("Cookies: " + cookieCount, SwingConstants.CENTER);
        cookieCountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(cookieCountLabel, BorderLayout.NORTH);

        // Cookies per second label
        cpsLabel = new JLabel("Cookies per second: " + (passiveIncome * rebirthMultiplier), SwingConstants.CENTER);
        cpsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(cpsLabel, BorderLayout.SOUTH);

        // Center Panel for the click button
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        ImageIcon cookieIcon = new ImageIcon("cookie.png"); // Ensure cookie.png is in the same directory or provide
                                                            // full path
        clickButton = new JButton(cookieIcon);
        clickButton.setBorderPainted(false);
        clickButton.setContentAreaFilled(false);
        clickButton.setFocusPainted(false);
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cookieCount += cookiesPerClick * rebirthMultiplier;
                updateUI();
            }
        });
        centerPanel.add(clickButton);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Right Panel for upgrades
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 1, 5, 5));

        upgradeButton = createUpgradeButton("Upgrade (Cost: " + upgradeCost + ")", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieCount >= upgradeCost) {
                    cookieCount -= upgradeCost;
                    cookiesPerClick++;
                    upgradeCost *= 2;
                    updateUI();
                }
            }
        });
        rightPanel.add(upgradeButton);

        grandmaButton = createUpgradeButton("Grandma (Cost: " + grandmaCost + ")", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieCount >= grandmaCost) {
                    cookieCount -= grandmaCost;
                    grandmaCount++;
                    passiveIncome += 1;
                    grandmaCost *= 2;
                    updateUI();
                }
            }
        });
        rightPanel.add(grandmaButton);

        cookieLabButton = createUpgradeButton("Cookie Lab (Cost: " + cookieLabCost + ")", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieCount >= cookieLabCost) {
                    cookieCount -= cookieLabCost;
                    cookieLabCount++;
                    passiveIncome += 5;
                    cookieLabCost *= 2;
                    updateUI();
                }
            }
        });
        rightPanel.add(cookieLabButton);

        cookieFactoryButton = createUpgradeButton("Cookie Factory (Cost: " + cookieFactoryCost + ")",
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (cookieCount >= cookieFactoryCost) {
                            cookieCount -= cookieFactoryCost;
                            cookieFactoryCount++;
                            passiveIncome += 20;
                            cookieFactoryCost *= 2;
                            updateUI();
                        }
                    }
                });
        rightPanel.add(cookieFactoryButton);

        rebirthButton = createUpgradeButton("Rebirth (Cost: " + rebirthCost + ")", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cookieCount >= rebirthCost) {
                    cookieCount = 0;
                    cookiesPerClick = 1;
                    upgradeCost = 10;
                    passiveIncome = 0;
                    grandmaCount = 0;
                    grandmaCost = 50;
                    cookieLabCount = 0;
                    cookieLabCost = 200;
                    cookieFactoryCount = 0;
                    cookieFactoryCost = 500;
                    rebirthMultiplier++;
                    rebirthCost *= 2;
                    updateUI();
                }
            }
        });
        rightPanel.add(rebirthButton);

        frame.add(rightPanel, BorderLayout.EAST);

        // Timer for passive income
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cookieCount += passiveIncome * rebirthMultiplier;
                SwingUtilities.invokeLater(() -> updateUI());
            }
        }, 0, 1000); // Passive income every second

        // Show the frame
        frame.setVisible(true);
    }

    private JButton createUpgradeButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(action);
        return button;
    }

    private void updateUI() {
        cookieCountLabel.setText("Cookies: " + cookieCount);
        cpsLabel.setText("Cookies per second: " + (passiveIncome * rebirthMultiplier));
        upgradeButton.setText("Upgrade (Cost: " + upgradeCost + ")");
        grandmaButton.setText("Grandma (Cost: " + grandmaCost + ")");
        cookieLabButton.setText("Cookie Lab (Cost: " + cookieLabCost + ")");
        cookieFactoryButton.setText("Cookie Factory (Cost: " + cookieFactoryCost + ")");
        rebirthButton.setText("Rebirth (Cost: " + rebirthCost + ")");

        setButtonEnabled(upgradeButton, cookieCount >= upgradeCost);
        setButtonEnabled(grandmaButton, cookieCount >= grandmaCost);
        setButtonEnabled(cookieLabButton, cookieCount >= cookieLabCost);
        setButtonEnabled(cookieFactoryButton, cookieCount >= cookieFactoryCost);
        setButtonEnabled(rebirthButton, cookieCount >= rebirthCost);
    }

    private void setButtonEnabled(JButton button, boolean enabled) {
        button.setEnabled(enabled);
        button.setBackground(enabled ? UIManager.getColor("Button.background") : Color.DARK_GRAY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CookieClicker();
            }
        });
    }
}
