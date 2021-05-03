package kr.ac.kaist.arrc.nasatlx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TLXFirstStepActivity extends AppCompatActivity {
    private static final String TAG = "TLXFirstStepActivity";


    EditText name_edit, task_edit, date_edit;
    int current_task_id;

    CustomRankBar[] custom_bars = new CustomRankBar[6];
    Button continue_btn;

    private Scores this_score;
    private SerializableScores all_scores;

    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_edit = findViewById(R.id.name_edit);
        task_edit = findViewById(R.id.task_edit);
        date_edit = findViewById(R.id.date_edit);

        custom_bars[0] = findViewById(R.id.mental_demand);
        custom_bars[1] = findViewById(R.id.physical_demand);
        custom_bars[2] = findViewById(R.id.temporal_demand);
        custom_bars[3] = findViewById(R.id.performance);
        custom_bars[4] = findViewById(R.id.frustration);
        custom_bars[5] = findViewById(R.id.effort);

        continue_btn =findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<custom_bars.length; i++){
                    if(custom_bars[i].getSelected_point()!=-1){
                        this_score.GRADES[i] = custom_bars[i].getSelected_point();

                        // quit loop and go to next activity at the end of loop
                        if(i==custom_bars.length-1){
                            all_scores.setScores(current_task_id, this_score);
//                            all_scores.setDone(current_task_id);

                            if (CONSTANTS.enable_second_step) {
                                Intent intent = new Intent(getApplicationContext(), TLXSecondStepActivity.class);
                                intent.putExtra("task_id", current_task_id);
                                intent.putExtra("all_scores", all_scores);
                                startActivityForResult(intent, REQUEST_CODE);

                            }else if(CONSTANTS.enable_custom_question){
                                Intent intent = new Intent(getApplicationContext(), CustomQuestionActivity.class);
                                intent.putExtra("task_id", current_task_id);
                                intent.putExtra("all_scores", all_scores);
                                startActivityForResult(intent, REQUEST_CODE);

                            }else{
                                all_scores.setDone(current_task_id);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("task_id", current_task_id);
                                returnIntent.putExtra("all_scores",all_scores);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();




                            }

                        }


                    }else{

                        // Snackbar.make(this, "Welcome to AndroidHive", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Choose All Scores before continue", Toast.LENGTH_LONG).show();
                        break;

                    }

                }
            }
        });


        Intent intent = getIntent();
        // name_edit.setText(intent.getStringExtra("name"));
        // task_edit.setText(intent.getStringExtra("task"));
        // date_edit.setText(intent.getStringExtra("date"));


        current_task_id = intent.getIntExtra("task_id", -1);
        all_scores = (SerializableScores) intent.getSerializableExtra("all_scores");

        name_edit.setText(all_scores.getNAME());
        date_edit.setText(all_scores.getDATE());
        task_edit.setText(all_scores.getScore(current_task_id).TASK);
        setTitle(getResources().getString(R.string.app_name)+" - "+all_scores.getScore(current_task_id).TASK);

        this_score = all_scores.getScore(current_task_id);
        for(int i=0; i<this_score.GRADES.length; i++){
            if(this_score.GRADES[i]!=-1){
                custom_bars[i].setSelected_point(this_score.GRADES[i]);
            }
        }


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
