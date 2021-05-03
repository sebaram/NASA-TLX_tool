package kr.ac.kaist.arrc.nasatlx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomQuestionActivity extends AppCompatActivity {
    final String TAG = "CustomQuestionActivity";

    String all_result = "";
    ArrayList<Question> current_question_lists;
    ArrayList<ArrayList<Question>> question_lists_bypage;
    int current_page_num = 0;
    LinearLayout question_layout;

    ArrayList<CheckBox[]> checkboxes_list = new ArrayList<CheckBox[]>();
    int current_task_id;

    Button continue_btn;
    EditText name_edit, task_edit, date_edit;
    LinearLayout custom_question_main;
    CustomRankBar[] custom_bars;

    private Scores this_score;
    private SerializableScores all_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_question);


        // Load Stored results
        Intent intent = getIntent();
        current_task_id = intent.getIntExtra("task_id", -1);
        all_scores = (SerializableScores) intent.getSerializableExtra("all_scores");
        this_score = all_scores.custom_scores[current_task_id];


        /**Do not show name,task,date in custom activity layout**/
//        name_edit = findViewById(R.id.custom_name_edit);
//        task_edit = findViewById(R.id.custom_task_edit);
//        date_edit = findViewById(R.id.custom_date_edit);
//        name_edit.setText(all_scores.getNAME());
//        date_edit.setText(all_scores.getDATE());
//        task_edit.setText(all_scores.getScore(current_task_id).TASK);


        custom_question_main = findViewById(R.id.custom_question_layout);
        setTitle(getResources().getString(R.string.app_name)+" - "+all_scores.getScore(current_task_id).TASK);




        question_lists_bypage = CONSTANTS.QUESTION_LISTS_BYPAGE;
        updateCurrentPage();



        continue_btn = new Button(this);
        continue_btn.setText("Continue");
        continue_btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));

        custom_question_main.addView(continue_btn);


        continue_btn.setOnClickListener(continueClickListener);




    }

    void updateCurrentPage(){
        LinearLayout new_layout = getCurrentPage();
        if(current_page_num==0){
            question_layout = new_layout;
            custom_question_main.addView(question_layout);
        }else{
            int index = custom_question_main.indexOfChild(question_layout);
            custom_question_main.removeView(question_layout);
            question_layout = new_layout;
            custom_question_main.addView(question_layout, index);

        }
    }

    LinearLayout getCurrentPage(){
        LinearLayout linear_layout = new LinearLayout(this);
        linear_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linear_layout.setOrientation(LinearLayout.VERTICAL);

        current_question_lists = question_lists_bypage.get(current_page_num);
        Log.d(TAG, "current_question_lists size:"+current_question_lists.size());

        for(Question one_question: current_question_lists){
            linear_layout.addView(one_question.createView(this));
        }


        return linear_layout;
    }



    //continue button's action
    Button.OnClickListener continueClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            // save current result
            for(int i=0; i<current_question_lists.size(); i++){
                all_result +=  current_question_lists.get(i).getResult()+", ";
            }


            if(current_page_num==(question_lists_bypage.size()-1)){
                all_scores.check_lists[current_task_id] = all_result;
                all_scores.setDone(current_task_id);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("task_id", current_task_id);
                returnIntent.putExtra("all_scores",all_scores);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }else{
                current_page_num++;
                updateCurrentPage();

            }

        }
    };
}

