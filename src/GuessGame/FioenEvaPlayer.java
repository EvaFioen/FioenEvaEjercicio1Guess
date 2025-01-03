package GuessGame;

import java.io.Serializable;

/**
 * Clase que representa a un jugador del juego de adivinanza.
 */
public class FioenEvaPlayer implements Serializable {
    private final String nickname;
    private final int score;

    /**
     * Constructor para crear un nuevo jugador con un apodo y una puntuación.
     * @param nickname El apodo del jugador.
     * @param score La puntuación del jugador.
     */
    public FioenEvaPlayer(String nickname, int score){
        this.nickname = nickname;
        this.score = score;
    }
    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }


    @Override
    public String toString() {
        return nickname + " - " + score;
    }
}