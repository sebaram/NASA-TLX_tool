package kr.ac.kaist.arrc.nasatlx;

import java.util.ArrayList;

/**
 * Created by JY on 2018-05-03.
 */

public class CONSTANTS {
    public static boolean enable_tlx = true;
    public static boolean enable_second_step = false;
    public static boolean enable_custom_question = false;
    public static String FOLDER = "TaskLoadIndex";
//    public static String NASATLX_COLUMNS = "Name, Task, Mental Demand, Physical Demand, Temporal Demand, Performance, Effort, Frustration";
    public static String NASATLX_COLUMNS = "Name, Task, Mental Demand, Physical Demand, Temporal Demand, Performance, Effort, Frustration," +
                                            "Mental DemandTALLY, Physical DemandTALLY, Temporal DemandTALLY, PerformanceTALLY, EffortTALLY, FrustrationTALLY";


    public static String SAVING_COLUMN = NASATLX_COLUMNS;
    public static int NUMER_OF_FACTORS = 6;

    public static ArrayList<Question> QUESTION_LISTS;
    public static ArrayList<ArrayList<Question>> QUESTION_LISTS_BYPAGE;
}
