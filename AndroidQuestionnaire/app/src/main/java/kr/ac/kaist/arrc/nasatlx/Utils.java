package kr.ac.kaist.arrc.nasatlx;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Juyoung LEE on 4/26/2018.
 */

public class Utils {


    public static String getDirForDevice(){
        String baseDir_str;
        if(Build.MODEL.contains( "Glass")){
            baseDir_str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + CONSTANTS.FOLDER +"/";
        }else{
            baseDir_str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CONSTANTS.FOLDER +"/";
        }
        File base_folder = new File(baseDir_str);
        if(!base_folder.exists()){
            base_folder.mkdir();
        }

        return baseDir_str;
    }

    public static ArrayList<TaskDescription> readTaskLists(){
        ArrayList<TaskDescription> task_lists = new ArrayList<>();

        String baseDir_str = getDirForDevice();
        File base_folder = new File(baseDir_str);
        if(!base_folder.exists()){
            base_folder.mkdir();

        }

        // check if File exists or not
        FileReader fr=null;
        try {
            fr = new FileReader(baseDir_str + "task_lists.txt");
        }
        catch (FileNotFoundException fe) {
            Log.d("Utils|readCurrentStatus", "task_list.txt file not found, "+baseDir_str);
            for(int i=0;i<10;i++)
                task_lists.add(new TaskDescription());
            return task_lists;
        }


        try{

            BufferedReader buffer_reader = new BufferedReader(fr);
            // read from FileReader till the end of file
            String line;
            while ((line = buffer_reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] split_result = line.split("::");

                task_lists.add( new TaskDescription(split_result[0], split_result[1] ));

            }

            // close the file
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return task_lists;

    }
    public static ArrayList<ArrayList<Question>> readQuestionListsByPage(Context cont){
        ArrayList<Question> question_lists = new ArrayList<>();
        ArrayList<ArrayList<Question>> question_lists_bypage = new ArrayList<>();

        String baseDir_str = getDirForDevice();
        File base_folder = new File(baseDir_str);
        if(!base_folder.exists()){
            base_folder.mkdir();

        }

        // check if File exists or not
        FileReader fr=null;
        try {
            fr = new FileReader(baseDir_str + "question_lists.txt");
        }
        catch (FileNotFoundException fe) {
            Log.d("Utils|readQuestionLists", "question_lists.txt file not found, "+baseDir_str);
            for(int i=0;i<5;i++)
                question_lists.add(new Question("Is this good questions?", "really;seriously;no"));
            question_lists_bypage.add(question_lists);

            return question_lists_bypage;
        }


        try{

            BufferedReader buffer_reader = new BufferedReader(fr);
            // read from FileReader till the end of file
            String line;
            while ((line = buffer_reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                try {
                    if(line.charAt(0) == '!'){
                        if (line.equalsIgnoreCase("!tlx") || line.equalsIgnoreCase("!tlx1")) {
                            Toast.makeText(cont, "!Command!: simple TLX", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = true;
                            CONSTANTS.enable_second_step = false;
                        }else if(line.equalsIgnoreCase("!tlx2")) {
                            Toast.makeText(cont, "!Command!: Full TLX", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = true;
                            CONSTANTS.enable_second_step = true;
                        }else if(line.equalsIgnoreCase("!no")|| line.equalsIgnoreCase("!notlx")) {
                            Toast.makeText(cont, "!Command!: Only Custom", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = false;
                            CONSTANTS.enable_second_step = false;
                        }else if(line.equalsIgnoreCase("!---")){
                            question_lists_bypage.add(question_lists);
                            question_lists = new ArrayList<>();
                            Toast.makeText(cont, "!Command!: seperate page", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(cont, "!Command!: "+line, Toast.LENGTH_SHORT).show();
                        }

//                        Toast.makeText(cont, "!Command!: "+line, Toast.LENGTH_SHORT).show();

                    }else{
                        String[] split_result = line.split("::");
                        Log.d("Utils|readQuestionLists", "question size: "+split_result.length);

                        if(split_result.length==2){
                            question_lists.add(new CheckBoxQuestion(split_result[0], split_result[1]));
                        }else if(split_result.length==3 || split_result.length==4){
                            Question this_question;
                            switch(Integer.parseInt(split_result[0])){
                                case 0:
                                    this_question = new CheckBoxQuestion(split_result[1], split_result[2]);
                                    break;
                                case 1:
                                    this_question = new RadioQuestion(split_result[1], split_result[2]);
                                    break;
                                case 2:
                                    this_question = new LikertQuestion(split_result[1], split_result[2]);
                                    break;
                                case 3:
                                    this_question = new TextQuestion(split_result[1], split_result[2]);
                                    break;
                                default:
                                    this_question = new Question(split_result[0], split_result[1]);
                                    break;
                            }
                            if( split_result.length==4)
                                this_question.Q_VIDEOPATH = split_result[3];
                            question_lists.add(this_question);
                        }

                        CONSTANTS.enable_custom_question = true;
                    }




                } catch (Exception e) {
                    Toast.makeText(cont, "Wrong Syntax: "+line, Toast.LENGTH_LONG).show();
                }



            }
            question_lists_bypage.add(question_lists);

            // close the file
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return question_lists_bypage;

    }
    public static int getSize(ArrayList<ArrayList<Question>> question_lists_bypage){

        int size = 0;
        for(ArrayList<Question> one_page:question_lists_bypage){
            size += one_page.size();
        }
        Log.d("Utils|getSize", "size: "+size);
        return size;

    }

    public static String getAllTitleForSaving(ArrayList<ArrayList<Question>> question_lists_bypage) {
        String result="";


        int i =0;
        for(ArrayList<Question> one_page: question_lists_bypage){
            for(Question one_question: one_page){
                result += ",\"p"+i+"_"+one_question.Q_TITLE+"\", \"p"+i+"_"+"CODE_"+one_question.Q_TITLE+"\"";
            }
            i++;
        }
        Log.d("Utils|getAllTitle", result);


        return result;


    }

    public static ArrayList<Question> readQuestionLists(Context cont){
        ArrayList<Question> question_lists = new ArrayList<>();

        String baseDir_str = getDirForDevice();
        File base_folder = new File(baseDir_str);
        if(!base_folder.exists()){
            base_folder.mkdir();

        }

        // check if File exists or not
        FileReader fr=null;
        try {
            fr = new FileReader(baseDir_str + "question_lists.txt");
        }
        catch (FileNotFoundException fe) {
            Log.d("Utils|readQuestionLists", "question_lists.txt file not found, "+baseDir_str);
            for(int i=0;i<5;i++)
                question_lists.add(new Question("Is this good questions?", "really;seriously;no"));


            return question_lists;
        }


        try{

            BufferedReader buffer_reader = new BufferedReader(fr);
            // read from FileReader till the end of file
            String line;
            while ((line = buffer_reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                try {
                    if(line.charAt(0) == '!'){
                        if (line.equalsIgnoreCase("!tlx") || line.equalsIgnoreCase("!tlx1")) {
                            Toast.makeText(cont, "!Command!: simple TLX", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = true;
                            CONSTANTS.enable_second_step = false;
                        }else if(line.equalsIgnoreCase("!tlx2")) {
                            Toast.makeText(cont, "!Command!: Full TLX", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = true;
                            CONSTANTS.enable_second_step = true;
                        }else if(line.equalsIgnoreCase("!no")|| line.equalsIgnoreCase("!notlx")) {
                            Toast.makeText(cont, "!Command!: Only Custom", Toast.LENGTH_SHORT).show();
                            CONSTANTS.enable_tlx = false;
                            CONSTANTS.enable_second_step = false;
                        }else{
                            Toast.makeText(cont, "!Command!: "+line, Toast.LENGTH_SHORT).show();
                        }

//                        Toast.makeText(cont, "!Command!: "+line, Toast.LENGTH_SHORT).show();

                    }else{
                        String[] split_result = line.split("::");

                        if(split_result.length==2){
                            question_lists.add(new CheckBoxQuestion(split_result[0], split_result[1]));
                        }else if(split_result.length==3){
                            switch(Integer.parseInt(split_result[0])){
                                case 0:
                                    question_lists.add(new CheckBoxQuestion(split_result[1], split_result[2]));
                                    break;
                                case 1:
                                    question_lists.add(new RadioQuestion(split_result[1], split_result[2]));
                                    break;
                                case 2:
                                    question_lists.add(new LikertQuestion(split_result[1], split_result[2]));
                                    break;
                                case 3:
                                    question_lists.add(new TextQuestion(split_result[1], split_result[2]));
                                    break;
                            }
                        }

                        CONSTANTS.enable_custom_question = true;
                    }




                } catch (Exception e) {
                    Toast.makeText(cont, "Wrong Syntax: "+line, Toast.LENGTH_LONG).show();
                }



            }

            // close the file
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return question_lists;

    }

}
