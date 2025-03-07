import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SQL_Tester {
  static Scanner sc = new Scanner(System.in);
  static int score = 0;
  static int highscore = 0;
  static MySQLConnector datenbank = new MySQLConnector("root", "", "127.0.0.1", "geoquiz");
  private static ArrayList<String> questions = new ArrayList<>();
  private static ArrayList<String> answers = new ArrayList<>();
  private static ArrayList<Integer> questionSequence;
  private static String mode = "capital";
  private static String region = "EU";
  private static int index = 0;
  private static JFrame frame;
  private static JLabel usernameLabel;
  private static JTextField usernameField;
  private static JLabel passwordLabel;
  private static JTextField passwordField;
  private static JButton loginButton;
  private static JButton registerButton;
  private static JLabel loginFeedbackLabel;
  private static JLabel questionLabel;
  private static JTextField answerField;
  private static JButton submitButton;
  private static JLabel feedbackLabel;
  private static JLabel scoreLabel;
  private static JLabel highscoreLabel;
  private static JPanel loginPanel;
  private static JPanel gamePanel;
  private static int frameWidth = 800;

  public static void main(String[] args) {

    frame = new JFrame("GeoQuiz");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(frameWidth, frameWidth * 9 / 16);
    frame.setLayout(new FlowLayout());

    view();
    login();
    frame.setVisible(true);
    try {
      datenbank.connect();
    } catch (SQLException e) {
      System.out.println("Connection failed");
      return;
    }
    generateQuestions();
    showQuestion();
  }

  public static void login() {

    loginPanel = new JPanel();
    loginPanel.setLayout(new GridLayout(4, 2));

    usernameLabel = new JLabel("Username:");
    loginPanel.add(usernameLabel);

    usernameField = new JTextField();
    loginPanel.add(usernameField);

    passwordLabel = new JLabel("Password:");
    loginPanel.add(passwordLabel);

    passwordField = new JPasswordField();
    loginPanel.add(passwordField);

    loginButton = new JButton("Login");
    loginPanel.add(loginButton);

    registerButton = new JButton("Register");
    loginPanel.add(registerButton);

    loginFeedbackLabel = new JLabel("");
    loginPanel.add(loginFeedbackLabel);

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loginUser();
      }
    });

    registerButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        registerUser();
      }

    });

    frame.add(loginPanel);
    loginPanel.setVisible(true);
  }

  private static void loginUser() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    try {
      String befehl = "Select highscore From User Where user = '" + username + "' and password = '" + password + "';";
      System.out.printf(">> %s%n", befehl);
      ResultSet ergebnis = datenbank.executeQuery(befehl);
      ergebnis.next();
      highscore = Integer.parseInt(ergebnis.getString(1));

      showHighscore();
      gamePanel.setVisible(true);
      loginPanel.setVisible(false);
    } catch (SQLException e) {
      System.out.printf("FEHLER: %s%n", e.getMessage());
      loginFeedbackLabel.setText("The login data is not correct");
    }
  }

  public static void registerUser() {
    try {
      String befehl = "Select * From User Where user = '" + usernameField.getText() + "';";
      System.out.printf(">> %s%n", befehl);
      ResultSet ergebnis = datenbank.executeQuery(befehl);
      if (!ergebnis.next()) {
        befehl = "INSERT INTO `user`(`user`, `password`) VALUES ('" + usernameField.getText() + "','"
            + passwordField.getText() + "');";
        System.out.printf(">> %s%n", befehl);
        datenbank.executeUpdate(befehl);
        gamePanel.setVisible(true);
        loginPanel.setVisible(false);
      } else {
        loginFeedbackLabel.setText("Username already taken!");
      }
    } catch (SQLException e) {
      System.err.println(e);

    }
  }

  public static void generateQuestions() {
    String befehl;
    String questionSQL;
    String questionString;
    String questionJoin;
    try {
      switch (mode) {
        case "capital":
          questionString = "Was ist die Hauptstadt von ";
          questionSQL = "land.name, ort.name";
          questionJoin = "land.hauptonr = ort.onr";
          break;

        case "country":
          questionSQL = "ort.name, land.name";
          questionString = "In welchem Land liegt ";
          questionJoin = "land.lnr = ort.lnr";
          break;
        default:
          questionSQL = "land.name, ort.name";
          questionString = "Was ist die Hauptstadt von ";
          questionJoin = "land.hauptonr = ort.onr";
          break;
      }
      questions.clear();
      answers.clear();
      befehl = "Select " + questionSQL + " From ort,land where " + questionJoin + " and " + questionSQL.split(", ")[0]
          + " IS NOT NULL and " + questionSQL.split(", ")[1] + " IS NOT NULL and land.knr = '" + region
          + "';";
      System.out.printf(">> %s%n", befehl);
      ResultSet ergebnis = datenbank.executeQuery(befehl);
      while (ergebnis.next()) {
        questions.add(questionString + ergebnis.getString(1));
        answers.add(ergebnis.getString(2));
      }
    } catch (SQLException e) {
      System.out.printf("FEHLER: %s%n", e.getMessage());
    }
    questionSequence = uniqueRandomNums(questions.size());
  }

  public static ArrayList<Integer> uniqueRandomNums(int length) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i = 1; i < length; i++)
      list.add(i);
    Collections.shuffle(list);
    return list;
  }

  public static String getCurrentAnswer() {
    try {
      return answers.get(questionSequence.get(index));
    } catch (IndexOutOfBoundsException e) {
      return "No more questions";
    }
  }

  public static String getCurrentQuestion() {
    try {
      return questions.get(questionSequence.get(index));
    } catch (IndexOutOfBoundsException e) {
      return "No more questions";
    }
  }

  public static void view() {
    gamePanel = new JPanel();
    gamePanel.setLayout(new GridLayout(6, 2));

    questionLabel = new JLabel("Question will appear here");
    gamePanel.add(questionLabel);

    answerField = new JTextField();
    gamePanel.add(answerField);

    submitButton = new JButton("Submit");
    gamePanel.add(submitButton);

    feedbackLabel = new JLabel("");
    gamePanel.add(feedbackLabel);

    scoreLabel = new JLabel("Score: 0");
    gamePanel.add(scoreLabel);

    highscoreLabel = new JLabel("Highscore: " + highscore);
    gamePanel.add(highscoreLabel);

    JLabel modeLabel = new JLabel("Select Mode:");
    gamePanel.add(modeLabel);

    String[] modes = { "capital", "country" };
    JComboBox<String> modeComboBox = new JComboBox<>(modes);
    modeComboBox.setSelectedItem(mode);
    gamePanel.add(modeComboBox);

    modeComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selectedMode = (String) modeComboBox.getSelectedItem();
        if (!selectedMode.equals(mode)) {
          System.out.println("Mode changed");
          mode = selectedMode;
          generateQuestions();
          showQuestion();
        }
      }
    });

    JLabel regionLabel = new JLabel("Select Region:");
    gamePanel.add(regionLabel);

    String[] regions = { "EU", "NA" };
    JComboBox<String> regionComboBox = new JComboBox<>(regions);
    modeComboBox.setSelectedItem(region);
    gamePanel.add(regionComboBox);

    regionComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == regionComboBox) {
          System.out.println("Region changed");
          String selectedRegion = (String) regionComboBox.getSelectedItem();
          region = selectedRegion;
          generateQuestions();
          showQuestion();
        }
      }
    });

    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        handleSubmit();
      }
    });
    frame.add(gamePanel);
    gamePanel.setVisible(false);
  }

  public void updateView() {
    questionLabel.setText(getCurrentQuestion() + "?");
    scoreLabel.setText("Score: " + score);
    highscoreLabel.setText("Highscore: " + highscore);
    feedbackLabel.setText("");
  }

  public static void showQuestion() {
    questionLabel.setText(getCurrentQuestion() + "?");
    System.out.println(getCurrentAnswer());
  }

  public String getInput() {
    return answerField.getText();
  }

  public static void showFeedback(boolean correct) {
    if (correct) {
      feedbackLabel.setText("That's the right answer!");
    } else {
      feedbackLabel.setText("That's the wrong answer!");
    }
    scoreLabel.setText("Score: " + score);
  }

  public static void showHighscore() {
    highscoreLabel.setText("Highscore: " + highscore);
  }

  private static void handleSubmit() {
    String input = answerField.getText();
    boolean correct = input.equalsIgnoreCase(getCurrentAnswer());
    if (correct) {
      score++;
      index++;
    } else {
      if (highscore < score) {
        highscore = score;
      }
      score = 0;

    }
    showFeedback(correct);
    if (index < questions.size()) {
      showQuestion();
    } else {
      showHighscore();
    }
    answerField.setText("");
    answerField.requestFocus();
  }
}