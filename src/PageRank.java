import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

/**
 * PageRank class
 *  - contains MINSCORE, MAXSCORE, array of 4 scores (4 factor scores)
 *  - and total score that will be used for each website. This class
 *  - has constructor, setters and getters to access the private variables,
 *  - generate scores randomly, compute total score, and print all scores
 */

public class PageRank {
    //the Score for each factor range is 0-25
    private static int MAXSCORE = 25;
    private static int MINSCORE = 0;
    private static int NUM_FACTOR = 4;
    private int [] scores = new int[NUM_FACTOR];
    private int totalScore;

    /** Constructor */
    public PageRank() { }

    /** Setters */
    public void setOneScore(int index, int value) { this.scores[index] = value; }

    public void setScore(int[] scores) { this.scores = scores; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    /** Getters */
    public static int getMAXSCORE() { return MAXSCORE; }

    public static int getMINSCORE() { return MINSCORE; }

    public int getOneScore(int index) { return scores[index]; }
    
    public int[] getAllScores() { return scores; }

    public int getTotalScore() { return totalScore; }

    /**
     * calTotalScore() method compute the totalScore by adding all 4 scores.
     * @param none
     * @return none
     * @time_complexity O(1)
     */
    public void calTotalScore() {
        this.totalScore = scores[0]+scores[1]+scores[2]+scores[3]; 
    }

    /**
     * generateScore() method is used to randomly generate 4 scores
     * from 0-2 for 4 scores in scores array and compute the
     * totalScore by adding all 4 scores.
     * @param none
     * @return none
     * @time_complexity O(n)
     */
    public void generateScore(){
        for (int i = 0; i < NUM_FACTOR; i++) {
            setOneScore(i, (int)(MINSCORE+Math.random()*(MAXSCORE)));
        }
        calTotalScore();
    }

    /**
     * print() method is used StringBuffer to display the score of 4 factors
     * and the total score.
     * @param none
     * @return none
     * @time_complexity O(n)
     */
    public void print(){
        StringBuffer buff = new StringBuffer();
        buff.append("\n - ");
        for (int i = 0; i < NUM_FACTOR; i++)
        {
            buff.append("Factor "+ (i+1)).append(": ").append(scores[i]).append(" - ");
        }
        buff.append("\n - Total Score: ").append(totalScore).append("\n\n");
        System.out.print(buff);
    }
}
