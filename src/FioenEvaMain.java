import java.io.File;
import java.util.Scanner;
import java.io.PrintStream;

public class FioenEvaMain {
    public static void main(String[] args) {
        FioenEvaMain programa = new FioenEvaMain();
        programa.inicio();
    }
    Scanner input = new Scanner(System.in);
    boolean out = false;

    public void inicio(){
        System.out.println("Guess the movie");
        System.out.println("The movie title has ,, characters (including spaces and puncuation)");
        System.out.println("Remaining turns: ..");
        System.out.println("Points: ,,");
        do {
            System.out.println("Choose an option:");
            System.out.println("[1] Guess a letter");
            System.out.println("[2] Guess the movie's title");
            System.out.println("[3] Exit");
            if (input.hasNext()) {
                int opcion = input.nextInt();
                input.nextLine();
                switch (opcion) {
                    case 1:
                        guesLetter();
                        break;
                    case 2:
                        guessMovie();
                        break;
                    case 3:
                        salirJuego();
                        break;
                    default:
                        System.out.println("That option is not valid. Try again");
                        break;
                }
            } else {
                System.out.println("The option chosen is not correct. Try again");
                input.nextLine();
            }
        } while (!out);
    }

    public void guesLetter(){
        System.out.println("Guess the letter");
    }

    public void guessMovie(){
        System.out.println("Guess the movie");
    }

    public void salirJuego(){
        System.out.println("The movie was: ,,,");
        out = true;
    }
}