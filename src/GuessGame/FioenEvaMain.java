package GuessGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;


public class FioenEvaMain {
    public static void main(String[] args) {
        FioenEvaMain programa = new FioenEvaMain();
        programa.inicio();
    }

    Scanner input = new Scanner(System.in);
    boolean out = false;
    final int maxTurns = 10;
    int remainingTurns = maxTurns;
    int points = 0;

    private final List<String> movies = new ArrayList<>();
    private final List<Character> wrongLetters = new ArrayList<>();
    private final List<Character> guessedLetters = new ArrayList<>();

    String randomMovie;
    String maskedMovie;

    public void inicio() {
        leerFichero();

        randomMovie = getRandomMovie(movies).toLowerCase();
        maskedMovie = randomMovie.replaceAll("[a-zA-Z]", "*");

        System.out.println("Guess the movie");
        System.out.println("You are guessing: " + maskedMovie);
        System.out.println("The movie title has " + randomMovie.length() + " characters (including spaces and punctuation)");
        System.out.println("Remaining turns: " + maxTurns);
        System.out.println("Points: " + points);
        System.out.println(" ");





        while (!out && remainingTurns > 0) {
            System.out.println("Choose an option:");
            System.out.println("[1] Guess a letter");
            System.out.println("[2] Guess the movie's title");
            System.out.println("[3] Exit");

            try {
                if (input.hasNext()) {
                    int opcion = input.nextInt();
                    input.nextLine();
                    switch (opcion) {
                        case 1:
                            guessLetter();
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
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                input.nextLine();
            }
        }
        if (remainingTurns == 0 && !out) {
            System.out.println("You've run out of attempts! Game over.");
            salirJuego();
        }
    }

    public String getRandomMovie(List<String> movies) {
        Random random = new Random();
        int randomIndex = random.nextInt(movies.size());
        return movies.get(randomIndex);
    }

    public void leerFichero() {
        File file = new File("movies.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                movies.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Something went wrong while reading the file: " + e.getMessage());
        }
    }

    public void guessLetter() {
        if (remainingTurns > 0) {
            System.out.println("Guess the letter of the movie " + maskedMovie);

            String guessedLetter = input.nextLine().toLowerCase();
            if (guessedLetter.length() != 1 || !Character.isLetter(guessedLetter.charAt(0))) {
                System.out.println("Invalid");
                return;
            }
            char guessedChar = guessedLetter.charAt(0);

            if (guessedLetters.contains(guessedChar) || wrongLetters.contains(guessedChar)) {
                System.out.println("You've already guessed that letter. Try again.");
                return;
            }
            if (randomMovie.contains(String.valueOf(guessedChar))) {
                System.out.println("Good guess!");
                guessedLetters.add(guessedChar);

                StringBuilder updatedMaskedMovie = new StringBuilder(maskedMovie);
                for (int i = 0; i < randomMovie.length(); i++) {
                    if (randomMovie.charAt(i) == guessedChar) {
                        updatedMaskedMovie.setCharAt(i, guessedChar);
                    }

                }
                maskedMovie = updatedMaskedMovie.toString();
                System.out.println(maskedMovie);
                points += 10;
            } else {
                System.out.println("Incorrect guess.");
                wrongLetters.add(guessedChar);
                points -= 10;
            }
        }
        remainingTurns--;
        System.out.println("Remaining turns: " + remainingTurns);
        System.out.println("Incorrect letters: " + wrongLetters);
        System.out.println("Points: " + points);

        if (!maskedMovie.contains("*")) {
            System.out.println("Congratulations! You've guessed the movie: " + randomMovie);
            System.out.println("Final points: " + points);
            writePlayers();
            readPlayers();
            out = true;
        }

    }

    public void guessMovie() {
        System.out.println("Guess the movie");
        String guessedMovie = input.nextLine().toLowerCase();

        if (guessedMovie.equals(randomMovie)) {
            System.out.println("Congratulations! You've guessed the movie: " + randomMovie);
            points += 20;
        } else {
            System.out.println("Incorrect guess. Game over!");
            System.out.println("The movie was: " + randomMovie);
            points -= 20;
        }
        System.out.println("Points: " + points);
        writePlayers();
        readPlayers();

        out = true;
    }

    public void salirJuego() {
        System.out.println("The movie was: " + randomMovie);
        out = true;
    }

    public void writePlayers() {
        ArrayList<FioenEvaPlayer> players = new ArrayList<>();

        players.add(new FioenEvaPlayer("Pro", 250));
        players.add(new FioenEvaPlayer("hi123", 60));
        players.add(new FioenEvaPlayer("happy", 30));
        players.add(new FioenEvaPlayer("eva123", 100));
        players.add(new FioenEvaPlayer("ef", 100));

        System.out.println("Enter your nickname:");
        String nickname = input.nextLine();

        players.add(new FioenEvaPlayer(nickname,points));

        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        if (players.size() > 5) {
            players = new ArrayList<>(players.subList(0, 5));
        }

        try (FileOutputStream fileOut = new FileOutputStream("ranking.data");
             ObjectOutputStream output = new ObjectOutputStream(fileOut)) {

            for (FioenEvaPlayer player : players) {
                output.writeObject(player);
            }
            System.out.println("Player added successfully to the ranking.");
        } catch (Exception e) {
            System.out.println("Error adding the player to the ranking");
        }
    }

    public void readPlayers() {
        try (FileInputStream fileIn = new FileInputStream("ranking.data");
             ObjectInputStream input = new ObjectInputStream(fileIn)) {

            System.out.println("Ranking:");

            while (true) {
                try {
                    FioenEvaPlayer player = (FioenEvaPlayer) input.readObject();
                    System.out.println(player);
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading the file");
        }
    }
}

