package FioenEvaGuessGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * Clase principal del juego para adivinar el nombre de la pelicula.
 * Permite al jugador adivinar letras o el título de una película, gestionar intentos,
 * y actualizar un ranking basado en las puntuaciones.
 */
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

    /**
     * Metodo principal del programa.
     */
    public void inicio() {
        defaultRanking();
        leerFichero();

        randomMovie = getRandomMovie(movies).toLowerCase();
        maskedMovie = randomMovie.replaceAll("[a-zA-Z]", "*");

        System.out.println("Welcome to the game: Guess the movie! 🎬🎬🎬");
        System.out.println("You are guessing: " + maskedMovie);
        System.out.println("The movie title has " + randomMovie.length() + " characters (including spaces and punctuation)");
        System.out.println("Remaining turns: " + maxTurns);
        System.out.println("Points: " + points);
        System.out.println(" ");

        while (!out && remainingTurns > 0) {
            try {
                System.out.println("Choose an option:");
                System.out.println("[1] Guess a letter");
                System.out.println("[2] Guess the movie's title");
                System.out.println("[3] Exit");

                if (input.hasNextInt()) {
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
                            System.out.println("You have exited the game.");
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
                System.out.println("An unexpected error occurred. Try again!");
                input.nextLine();
            }
        }
        if (remainingTurns == 0 && !out) {
            System.out.println("You've run out of attempts! Game over.");
            salirJuego();
        }

        System.out.println("Thank you for playing!");
        input.close();
    }

    /**
     * Selecciona aleatoriamente una película del archivo movies.txt.
     *
     * @param movies Lista de títulos de películas.
     * @return Un título de película seleccionado aleatoriamente.
     */
    public String getRandomMovie(List<String> movies) {
        Random random = new Random();
        int randomIndex = random.nextInt(movies.size());
        return movies.get(randomIndex);
    }

    /**
     * Lee el archivo movies.txt
     */
    public void leerFichero() {
        try {
            File file = new File("movies.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                movies.add(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }

    /**
     * Adivina una letra del título de la película. Actualiza la puntuación,
     * los intentos restantes y la lista de letras incorrectas.
     */
    public void guessLetter() {
        if (remainingTurns > 0) {
            System.out.println("Guess the letter of the movie: " + maskedMovie);

            String guessedLetter = input.nextLine().toLowerCase();
            if (!validateInput(guessedLetter)) {
                System.out.println("Invalid input. Try again.");
                return;
            }

            char guessedChar = guessedLetter.charAt(0);
            if (guessedLetters.contains(guessedChar) || wrongLetters.contains(guessedChar)) {
                System.out.println("You've already guessed that letter. Try again.");
                return;
            }

            processGuess(guessedChar);
            displayUpdatedState();

            if (!maskedMovie.contains("*")) {
                System.out.println("Congratulations! You've guessed the movie: " + randomMovie);
                System.out.println("Final points: " + points);
                writePlayers();
                out = true;
            }
        }
    }

    /**
     * Verifica si la entrada proporcionada es una única letra.
     * @param input Entrada proporcionada por el usuario.
     * @return true si es válida, false en caso contrario.
     */
    private boolean validateInput(String input) {
        return input.length() == 1 && Character.isLetter(input.charAt(0));
    }

    /**
     * Procesa el intento de adivinar una letra, actualizando los resultados y
     * la lista de letras correctas o incorrectas.
     * @param guessedChar Letra proporcionada por el usuario.
     */
    private void processGuess(char guessedChar) {
        if (randomMovie.contains(String.valueOf(guessedChar))) {
            System.out.println("Good guess!");
            guessedLetters.add(guessedChar);
            updateMaskedMovie(guessedChar);
            points += 10;
        } else {
            System.out.println("Incorrect guess.");
            wrongLetters.add(guessedChar);
            points -= 10;
        }
        remainingTurns--;
    }

    /**
     * Actualiza el título de la película oculta con las letras adivinadas
     * correctamente.
     * @param guessedChar Letra correctamente adivinada.
     */
    private void updateMaskedMovie(char guessedChar) {
        StringBuilder updatedMaskedMovie = new StringBuilder(maskedMovie);
        for (int i = 0; i < randomMovie.length(); i++) {
            if (randomMovie.charAt(i) == guessedChar) {
                updatedMaskedMovie.setCharAt(i, guessedChar);
            }
        }
        maskedMovie = updatedMaskedMovie.toString();
        System.out.println(maskedMovie);
    }

    /**
     * Muestra el estado actualizado del juego: intentos restantes, puntuación,
     * y letras incorrectas.
     */
    private void displayUpdatedState() {
        System.out.println("Remaining turns: " + remainingTurns);
        System.out.println("Incorrect letters: " + wrongLetters);
        System.out.println("Points: " + points);
        System.out.println(" ");
    }

    /**
     * Finaliza el juego mostrando el título correcto y los resultados finales.
     */
    public void guessMovie() {
        System.out.println("Guess the movie: " + maskedMovie);
        String guessedMovie = input.nextLine().toLowerCase();

        if (guessedMovie.equals(randomMovie)) {
            System.out.println("Congratulations! You've guessed the movie: " + randomMovie);
            points += 20;
            System.out.println("Final points: " + points);
            writePlayers();
            readPlayers();
        } else {
            System.out.println("Incorrect guess. Game over!");
            System.out.println("The movie was: " + randomMovie);
            points -= 20;
        }
        out = true;
    }

    /**
     * Finaliza el juego mostrando el título correcto de la película.
     */
    public void salirJuego() {
        System.out.println("The movie was: " + randomMovie);
        out = true;
    }

    /**
     * Crea un ranking predeterminado y lo guarda en "ranking.data".
     * @return Lista con jugadores por defecto.
     */
    public ArrayList<FioenEvaPlayer> defaultRanking() {
        ArrayList<FioenEvaPlayer> defaultPlayers = new ArrayList<>();
        defaultPlayers.add(new FioenEvaPlayer("Pro", 250));
        defaultPlayers.add(new FioenEvaPlayer("hi123", 60));
        defaultPlayers.add(new FioenEvaPlayer("happy", 30));
        defaultPlayers.add(new FioenEvaPlayer("eva123", 10));
        defaultPlayers.add(new FioenEvaPlayer("ef", 100));

        defaultPlayers.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        try (FileOutputStream fileOut = new FileOutputStream("ranking.data");
                 ObjectOutputStream output = new ObjectOutputStream(fileOut)) {

                for (FioenEvaPlayer player : defaultPlayers) {
                    output.writeObject(player);
                }
            } catch (Exception e) {
                System.out.println("Error initializing default ranking.");
            }
        return defaultPlayers;
    }
    /**
     * Registra a los jugadores y actualiza la tabla de clasificación (ranking).
     */
    public void writePlayers() {
        ArrayList<FioenEvaPlayer> players = defaultRanking();
        readPlayers();

        String nickname;
        boolean isValidNickname;
        do {
            System.out.println("Enter your nickname:");
            nickname = input.nextLine();
            isValidNickname = true;

            for (FioenEvaPlayer player : players) {
                if (player.getNickname().equalsIgnoreCase(nickname)) {
                    System.out.println("Nickname already exists. Please choose a different nickname.");
                    isValidNickname = false;
                    break;
                }
            }
        } while (!isValidNickname);

        players.add(new FioenEvaPlayer(nickname, points));

        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        if (players.size() > 5 && players.get(5).getNickname().equalsIgnoreCase(nickname)) {
            System.out.println("Your score does not qualify for the top 5 ranking.");
            String finalNickname = nickname;
            players.removeIf(player -> player.getNickname().equalsIgnoreCase(finalNickname));
        } else {
            if (players.size() > 5) {
                players.subList(5, players.size()).clear();
            }

            try (FileOutputStream fileOut = new FileOutputStream("ranking.data");
                 ObjectOutputStream output = new ObjectOutputStream(fileOut)) {

                for (FioenEvaPlayer player : players) {
                    output.writeObject(player);
                }
                System.out.println("Ranking updated");

            } catch (Exception e) {
                System.out.println("Error adding the player to the ranking");
            }
        }
    }

    /**
     * Lee y muestra la tabla de clasificación actual (ranking).
     */
    public void readPlayers() {
        File file = new File("ranking.data");

        // Check if the file exists or is empty
        if (!file.exists() || file.length() == 0) {
            System.out.println("No ranking data available.");
            return;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream input = new ObjectInputStream(fileIn)) {

            System.out.println("Ranking of the top 5 players:");
            while (true) {
                try {
                    FioenEvaPlayer player = (FioenEvaPlayer) input.readObject();
                    System.out.println(player);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Error while reading player data: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error opening the ranking file: " + e.getMessage());
        }
    }
}
