package kr.ac.kaist.arrc.nasatlx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TLXSecondStepActivity extends AppCompatActivity {
    private static final String TAG = "TLXSecondStepActivity";
    int current_task_id;
    private SerializableScores all_scores;

    private ProgressBar progressBar;
    private Button up_button, down_button;
    private TextView up_text, down_text;
    private int up_index,down_index;
    private int[] point_by_index = {0, 0, 0, 0, 0, 0};


    private int[] all_title_id = {R.string.mental_demand_title, R.string.physical_demand_title, R.string.temporal_demand_title, R.string.performance_title, R.string.effort_title, R.string.frustration_title};
    private int[] all_description_id = {R.string.mental_demand_description, R.string.physical_demand_description, R.string.temporal_demand_description, R.string.performance_description, R.string.effort_description, R.string.frustration_description};

    private int[] all_combination = {01, 02, 03, 04, 05,
                                    12, 13, 14, 15,
                                    23, 24, 25,
                                    34, 35,
                                    45};
    List<Integer> all_combination_list = new ArrayList<>();

    Random generator = new Random(System.nanoTime());


    private final int REQUEST_CODE = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_importance);


        Intent intent = getIntent();

        progressBar = findViewById(R.id.compare_progress);

        up_button = findViewById(R.id.top_select);
        down_button = findViewById(R.id.bottom_select);
        up_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요
                        point_by_index[up_index]++;
                        toNextQuestion();
                    }
                }
        );
        down_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요
                        point_by_index[down_index]++;
                        toNextQuestion();
                    }
                }
        );

        up_text = findViewById(R.id.top_description);
        down_text = findViewById(R.id.bottom_description);


        current_task_id = intent.getIntExtra("task_id", -1);
        all_scores = (SerializableScores) intent.getSerializableExtra("all_scores");

        // randomized order
        for (int i : all_combination) {
            all_combination_list.add(i);
        }
        Collections.shuffle(all_combination_list, new Random(System.nanoTime()));

        toNextQuestion();

    }

    private void endTLX(){
        Scores this_score;
        this_score = all_scores.getScore(current_task_id);
        for(int i = 0; i<point_by_index.length; i++){
            this_score.TALLY[i] = point_by_index[i];
        }
        all_scores.setScores(current_task_id, this_score);
        all_scores.setDone(current_task_id);


        if(CONSTANTS.enable_custom_question){
            Intent intent = new Intent(getApplicationContext(), CustomQuestionActivity.class);
            intent.putExtra("task_id", current_task_id);
            intent.putExtra("all_scores", all_scores);
//            startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE);

        }else{
            Intent returnIntent = new Intent();
            returnIntent.putExtra("task_id", current_task_id);
            returnIntent.putExtra("all_scores",all_scores);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }

    }



    private void toNextQuestion(){
        if(all_combination_list.size()==0){
            endTLX();
            return;
        }

        int ab;

        ab = all_combination_list.get(0);
        if (generator.nextBoolean()) {
            up_index = ab/10;
            down_index = ab%10;
        } else {
            down_index = ab/10;
            up_index = ab%10;
        }

        up_button.setText(all_title_id[up_index]);
        up_text.setText(all_description_id[up_index]);
        down_button.setText(all_title_id[down_index]);
        down_text.setText(all_description_id[down_index]);

        progressBar.setProgress(all_combination.length - all_combination_list.size());
        Log.d(TAG, "Progress: "+(all_combination.length - all_combination_list.size()));

        all_combination_list.remove(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult:"+requestCode+","+resultCode);

        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }//onActivityResult
}
