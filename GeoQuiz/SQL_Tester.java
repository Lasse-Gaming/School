import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

class SQL_Tester {
  static Scanner sc = new Scanner(System.in);
  static int score = 0;
  static int highscore = 0;

  public static void main(String[] args) {
    MySQLConnector datenbank = new MySQLConnector("admin", "admin", "127.0.0.1", "geoquiz");
    ResultSet ergebnis = null;

    try {
      datenbank.connect();
      try {
        String befehl = "Select land.name From ort,land where land.hauptonr = ort.onr and land.knr = 'EU';";
        System.out.printf(">> %s%n", befehl);
        ergebnis = datenbank.executeQuery(befehl);
        ArrayList<String> questions = new ArrayList();
        while (ergebnis.next()) {
          for (int i = 1; i <= ergebnis.getMetaData().getColumnCount(); i++) {
            questions.add(ergebnis.getString(i));
          }
        }
        befehl = "Select ort.name From ort,land where land.hauptonr = ort.onr and land.knr = 'EU';";
        System.out.printf(">> %s%n", befehl);
        ergebnis = datenbank.executeQuery(befehl);
        ArrayList<String> answers = new ArrayList();
        while (ergebnis.next()) {
          for (int i = 1; i <= ergebnis.getMetaData().getColumnCount(); i++) {
            answers.add(ergebnis.getString(i));
          }
        }
        game(questions, answers);
      } catch (SQLException e) {
        System.out.printf("FEHLER: %s%n", e.getMessage());
      }

      // Verbindung zur Datenbank trennen
      datenbank.disconnect();

    } catch (SQLException e) {
      // Gibt eine Fehlermeldung aus und beendet die Anwendung, falls beim Verbinden
      // mit der Datenbank ein Problem aufgetreten ist
      System.out.printf("FEHLER: %s%n", e.getMessage());
      System.exit(1);
    }
  }

  public static void game(ArrayList<String> questions, ArrayList<String> answers) {
    ArrayList<Integer> questionSequence = uniqueRandomNums(questions.size());
    for (int index : questionSequence) {
      while (true) {
        System.out.println("Was ist die Hauptstadt von " + questions.get(index) + "?");
        System.out.println(answers.get(index));
        String answer = sc.nextLine();
        if (answer.equalsIgnoreCase(answers.get(index))) {
          score++;
          System.out.println("That's the right answer! Score: " + score);
          break;
        } else {
          System.out.println("That's the wrong answer!");
          if (highscore < score) {
            highscore = score;
            System.out.println("Your highscore is " + highscore);
          }
          score = 0;
        }
      }
    }
  }

  static void userLogIn(MySQLConnector datenbank) {
    String[] loginData = new String[2];

    while (true) {
      try {
        datenbank.connect();
        System.out.println("Username:");
        String username = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();

        String befehl = "Select highscore From User Where user = ;";
        System.out.printf(">> %s%n", befehl);
        ResultSet ergebnis = datenbank.executeQuery(befehl);
        for (int i = 1; i <= ergebnis.getMetaData().getColumnCount(); i++) {
          loginData = (ergebnis.getString(i)).split(" ");
        }
        if (username == loginData[0] && password == loginData[1]) {

        }
      } catch (Exception e) {
        System.out.println("user not valid");
      }
      break;
    }
  }

  public static ArrayList<Integer> uniqueRandomNums(int length) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i = 1; i < length + 1; i++)
      list.add(i);
    Collections.shuffle(list);
    return list;
  }
}
