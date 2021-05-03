package kr.ac.kaist.arrc.nasatlx;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Juyoung LEE on 2018-05-02.
 */

public class SerializableScores implements Serializable {
    private int size;

    String NAME, DATE;
    Scores[] scores, custom_scores; // array for store all TASKs' score
    Boolean[] done;

    String[] check_lists;

    public SerializableScores(int N){
        this.scores = new Scores[N];
        this.custom_scores = new Scores[N];
        this.done = new Boolean[N];
        this.check_lists = new String[N];
        Arrays.fill(this.done, false);
        Arrays.fill(this.check_lists, ",");

        this.size = N;
    }
    public SerializableScores(int N, String NAME, String DATE){
        this.scores = new Scores[N];
        this.custom_scores = new Scores[N];
        this.done = new Boolean[N];
        this.check_lists = new String[N];
        Arrays.fill(this.done, false);
        Arrays.fill(this.check_lists, ",");

        this.NAME = NAME;
        this.DATE = DATE;
        this.size = N;
    }


    public void setScores(int id, Scores result) {
        this.scores[id] = result;
    }
    public void setCustom_scores(int id, Scores result) {
        this.custom_scores[id] = result;
    }
    public void setDone(int id){
        this.done[id] = true;
    }
    public Scores getScore(int id) {
        return this.scores[id];
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getSize(){
        return size;
    }

    public boolean getDone(int id) {
        return this.done[id];
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
    public void toCSV(){
        Log.d("testing", "toCSV");


        String file_name = Utils.getDirForDevice() + this.DATE + "_"+ this.NAME + ".csv";

        File outputFile = new File(file_name);
        try {
//			FileUtils.writeLines(outputFile, data);
            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(CONSTANTS.SAVING_COLUMN);
            bw.newLine();
            for (int i=0; i<scores.length; i++) {
//                bw.write(this.NAME +", " +scores[i].TASK+scores[i].toOnelineString()+custom_scores[i].toOnelineString());
                bw.write(this.NAME +", " +scores[i].TASK+scores[i].toOnelineString()+","+this.check_lists[i]);
                bw.newLine();
            }
            bw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
