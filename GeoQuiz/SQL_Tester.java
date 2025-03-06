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

  public static void main(String[] args) {
    try {
      datenbank.connect();
    } catch (SQLException e) {
      System.out.println("Connection failed");
      return;
    }

    Model model = new Model();
    view view = new view(model);
    controller controller = new controller(model, view);
    model.generateQuestions();
    view.showQuestion();
    if (model.getHighscore() < model.getScore()) {
      model.setHighscore(model.getScore());
    }

    model.setScore(0);
  }

  /*
   * static void userLogIn(MySQLConnector datenbank) {
   * String[] loginData = new String[2];
   * 
   * while (true) {
   * try {
   * datenbank.connect();
   * System.out.println("Username:");
   * String username = sc.nextLine();
   * System.out.println("Password:");
   * String password = sc.nextLine();
   * 
   * String befehl = "Select highscore From User Where user = ;";
   * System.out.printf(">> %s%n", befehl);
   * ResultSet ergebnis = datenbank.executeQuery(befehl);
   * for (int i = 1; i <= ergebnis.getMetaData().getColumnCount(); i++) {
   * loginData = (ergebnis.getString(i)).split(" ");
   * }
   * if (username == loginData[0] && password == loginData[1]) {
   * 
   * }
   * } catch (Exception e) {
   * System.out.println("user not valid");
   * }
   * break;
   * }
   * }
   */
}

class Model {
  private String username;
  private String password;
  private int highscore;
  private int score = 0;
  private ArrayList<String> questions = new ArrayList<>();
  private ArrayList<String> answers = new ArrayList<>();
  private ArrayList<Integer> questionSequence;
  private String mode = "capital";
  private String region = "EU";
  private int index = 0;
  static MySQLConnector datenbank = new MySQLConnector("root", "", "127.0.0.1", "geoquiz");
  static ResultSet ergebnis = null;

  public Model() {
    try {
      datenbank.connect();
    } catch (SQLException e) {
      System.out.println("Connection failed");
    }
  }

  public void generateQuestions() {
    String befehl;
    String questionSQL;
    String answerSQL;
    String questionString;
    String questionJoin;
    try {
      switch (mode) {
        case "capital":
          questionString = "Was ist die Hauptstadt von ";
          questionSQL = "land.name";
          answerSQL = "ort.name";
          questionJoin = "land.hauptonr = ort.onr";
          break;

        case "country":
          questionSQL = "ort.name";
          answerSQL = "land.name";
          questionString = "In welchem Land liegt ";
          questionJoin = "land.lnr = ort.lnr";
          break;
        default:
          questionSQL = "land.name";
          answerSQL = "ort.name";
          questionString = "Was ist die Hauptstadt von ";
          questionJoin = "land.hauptonr = ort.onr";
          break;
      }
      for (ArrayList<String> variable : new ArrayList[] { questions, answers }) {
        variable.clear();
        if (variable == questions) {
          befehl = "Select " + questionSQL + " From ort,land where " + questionJoin + " and " + questionSQL
              + " IS NOT NULL and " + answerSQL + " IS NOT NULL and land.knr = '" + region
              + "';";
        } else {
          befehl = "Select " + answerSQL + " From ort,land where land.hauptonr = ort.onr and land.knr = '" + region
              + "';";
          questionString = "";
        }
        System.out.printf(">> %s%n", befehl);
        ResultSet ergebnis = datenbank.executeQuery(befehl);
        while (ergebnis.next()) {
          for (int i = 1; i <= ergebnis.getMetaData().getColumnCount(); i++) {
            variable.add(questionString + ergebnis.getString(i));
          }
        }
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

  public String getCurrentAnswer() {
    if (questionSequence.get(index) >= questionSequence.size()) {
      return "No more questions";
    }
    return answers.get(questionSequence.get(index));
  }

  public String getCurrentQuestion() {
    if (questionSequence.get(index) >= questionSequence.size()) {
      return "No more questions";
    }
    return questions.get(questionSequence.get(index));
  }

  public ArrayList<String> getAnswers() {
    return answers;
  }

  public ArrayList<Integer> getQuestionSequence() {
    return questionSequence;
  }

  public ArrayList<String> getQuestions() {
    return questions;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setAnswers(ArrayList<String> answers) {
    this.answers = answers;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getHighscore() {
    return highscore;
  }

  public void setHighscore(int highscore) {
    this.highscore = highscore;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}

class view {
  private Model model;
  private JFrame frame;
  private JLabel questionLabel;
  private JTextField answerField;
  private JButton submitButton;
  private JLabel feedbackLabel;
  private JLabel scoreLabel;
  private JLabel highscoreLabel;

  public view(Model model) {
    this.model = model;
    frame = new JFrame("GeoQuiz");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);
    frame.setLayout(new GridLayout(6, 1));

    questionLabel = new JLabel("Question will appear here");
    frame.add(questionLabel);

    answerField = new JTextField();
    frame.add(answerField);

    submitButton = new JButton("Submit");
    frame.add(submitButton);

    feedbackLabel = new JLabel("");
    frame.add(feedbackLabel);

    scoreLabel = new JLabel("Score: 0");
    frame.add(scoreLabel);

    highscoreLabel = new JLabel("Highscore: " + model.getHighscore());
    frame.add(highscoreLabel);
    JPanel modePanel = new JPanel();
    modePanel.setLayout(new FlowLayout());

    JLabel modeLabel = new JLabel("Select Mode:");
    modePanel.add(modeLabel);

    String[] modes = { "capital", "country" };
    JComboBox<String> modeComboBox = new JComboBox<>(modes);
    modeComboBox.setSelectedItem(model.getMode());
    modePanel.add(modeComboBox);

    modeComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == modeComboBox) {
          System.out.println("Mode changed");
          String selectedMode = (String) modeComboBox.getSelectedItem();
          System.out.println(selectedMode);
          model.setMode(selectedMode);
          model.generateQuestions();
          model.setIndex(0);
          model.uniqueRandomNums(model.getQuestions().size());
          showQuestion();

        }

      }
    });

    frame.add(modePanel);
    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
          handleSubmit();
        }
      }
    });

    frame.setVisible(true);
  }

  public void showQuestion() {
    questionLabel.setText(model.getCurrentQuestion() + "?");
    System.out.println(model.getCurrentAnswer());
  }

  public String getInput() {
    return answerField.getText();
  }

  public void showFeedback(boolean correct) {
    if (correct) {
      feedbackLabel.setText("That's the right answer!");
    } else {
      feedbackLabel.setText("That's the wrong answer!");
    }
    scoreLabel.setText("Score: " + model.getScore());
  }

  public void showHighscore() {
    highscoreLabel.setText("Your highscore is " + model.getHighscore());
  }

  private void handleSubmit() {
    String input = answerField.getText();
    boolean correct = input.equalsIgnoreCase(model.getCurrentAnswer());
    if (correct) {
      model.setScore(model.getScore() + 1);
      model.setIndex(model.getIndex() + 1);
    } else {
      if (model.getHighscore() < model.getScore()) {
        model.setHighscore(model.getScore());
      }
      model.setScore(0);
    }
    showFeedback(correct);
    if (model.getIndex() < model.getQuestions().size()) {
      showQuestion();
    } else {
      showHighscore();
    }
  }
}

class controller {
  private Model model;
  private view view;

  public controller(Model model, view view) {
    this.model = model;
    this.view = view;
  }

  public void setHighscore(int highscore) {
    model.setHighscore(highscore);
  }

  public void showHighscore() {
    view.showHighscore();
  }
}