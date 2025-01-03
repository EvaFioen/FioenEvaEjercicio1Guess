package GuessGame;

import java.io.Serializable;

public class FioenEvaPlayer implements Serializable {
    private final String nickname;
    private final int score;

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