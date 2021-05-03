package kr.ac.kaist.arrc.nasatlx;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Juyoung LEE on 4/27/2018.
 */

public class Scores implements Serializable {

    public String TASK, NAME, DETAILS;

    public int[] GRADES;
    public int[] TALLY;

    Scores(String task) {
        this.TASK = task;

        Arrays.fill(GRADES, -1);
        Arrays.fill(TALLY, -1);
    }
    Scores(String task, String name) {
        this.TASK = task;
        this.NAME = name;

        this.GRADES = new int[CONSTANTS.NUMER_OF_FACTORS];
        this.TALLY = new int[CONSTANTS.NUMER_OF_FACTORS];
        Arrays.fill(GRADES, -1);
        Arrays.fill(TALLY, -1);
    }
    Scores(String task, String name, int numberOfquestion) {
        this.TASK = task;
        this.NAME = name;

        this.GRADES = new int[numberOfquestion];
        this.TALLY = new int[numberOfquestion];
        Arrays.fill(GRADES, -1);
        Arrays.fill(TALLY, -1);
    }
    Scores(String task, String name, String details, int scale) {
        this.TASK = task;
        this.NAME = name;
        this.DETAILS = details;

        this.GRADES = new int[scale];
        this.TALLY = new int[scale];
        Arrays.fill(GRADES, -1);
        Arrays.fill(TALLY, -1);
    }


    public String toOnelineString() {
        //String str = NAME +", " +TASK;
        String str ="";
        for(int i = 0; i<GRADES.length; i++){
            str += ", "+GRADES[i];
        }
        for(int i = 0; i<GRADES.length; i++){
            str += ", "+TALLY[i];
        }
        return str;
    }
}
