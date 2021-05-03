package kr.ac.kaist.arrc.nasatlx;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskSelectionActivity extends AppCompatActivity {
    static final String TAG = "TaskSelectionActivity";
    private SerializableScores all_scores;
    ArrayList<TaskDescription> task_lists;
//    ArrayList<Question> question_lists;
    ArrayList<ArrayList<Question>> question_lists_bypage;
    ArrayList<CustomTaskView> taskViews = new ArrayList<>();

    EditText name_edit, date_edit;
    LinearLayout task_place_layout;
    CheckBox checkBox_first, checkBox_sec, checkBox_cus;

    private Menu option_menu;

    private final int FILE_PERMISSION = 0;
    private final int REQUEST_CODE = 0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 메뉴버튼이 처음 눌러졌을 때 실행되는 콜백메서드
        // 메뉴버튼을 눌렀을 때 보여줄 menu 에 대해서 정의
        getMenuInflater().inflate(R.menu.menu_home, menu);

        option_menu = menu;

        menu.findItem(R.id.first_tlx).setChecked(CONSTANTS.enable_tlx);
        menu.findItem(R.id.second_tlx).setChecked(CONSTANTS.enable_second_step);
        menu.findItem(R.id.custom_q).setChecked(CONSTANTS.enable_custom_question);




        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Option items for check TLX:First, TLX:Second, Custom Q.
        // Also include option to save result manually

        Log.d(TAG, "onOptionsItemSelected");

        int id = item.getItemId();


        switch(id) {
            case R.id.menu_save_csv:
                // SAVE MANUALLY
                all_scores.setNAME(name_edit.getText().toString());
                all_scores.setDATE(date_edit.getText().toString());
                all_scores.toCSV();
                Toast.makeText(getApplicationContext(), "CSV saved",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.first_tlx:
                // ENABLE/Disable TLX
                CONSTANTS.enable_tlx = !CONSTANTS.enable_tlx;
                item.setChecked(CONSTANTS.enable_tlx);
                if(!CONSTANTS.enable_tlx){
                    CONSTANTS.enable_second_step = false;
                    option_menu.findItem(R.id.second_tlx).setChecked(CONSTANTS.enable_second_step);
                }
                Log.d(TAG, "onOptionsItemSelected - first_tlx: "+CONSTANTS.enable_tlx);
                return true;
            case R.id.second_tlx:
                // ENABLE/Disable TLX second step(Only when TLX first step is activated)
                if(CONSTANTS.enable_tlx){
                    CONSTANTS.enable_second_step = !CONSTANTS.enable_second_step;
                    item.setChecked(CONSTANTS.enable_second_step);
                    Log.d(TAG, "onOptionsItemSelected - second_tlx: "+CONSTANTS.enable_second_step);
                    return true;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please activate TLX First step first!", Toast.LENGTH_SHORT).show();
                    return true;
                }

            case R.id.custom_q:
                // ENABLE/Disable Custom Q.
                CONSTANTS.enable_custom_question = !CONSTANTS.enable_custom_question;
                item.setChecked(CONSTANTS.enable_custom_question);
                Log.d(TAG, "onOptionsItemSelected - custom_q: "+CONSTANTS.enable_custom_question);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);

        checkPermission();


        name_edit = (EditText) findViewById(R.id.pre_name_edit);

        date_edit = (EditText) findViewById(R.id.pre_date_edit);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String currentDateTime = sdf.format(new Date());
        date_edit.setText(currentDateTime);

        task_place_layout = (LinearLayout) findViewById(R.id.tasks_layout);

        task_lists = Utils.readTaskLists();

        /*
        question_lists = Utils.readQuestionLists(getApplicationContext());
        CONSTANTS.QUESTION_LISTS = question_lists;
        String new_columns = "";
        for(Question one_question: question_lists){
            new_columns += ",\""+one_question.Q_TITLE+"\""+"CODE_"+one_question.Q_TITLE;
        }
        CONSTANTS.SAVING_COLUMN = CONSTANTS.NASATLX_COLUMNS + new_columns;
        */

        question_lists_bypage = Utils.readQuestionListsByPage(getApplicationContext());
        CONSTANTS.QUESTION_LISTS_BYPAGE = question_lists_bypage;

        CONSTANTS.SAVING_COLUMN = CONSTANTS.NASATLX_COLUMNS + Utils.getAllTitleForSaving(question_lists_bypage);


        all_scores = new SerializableScores(task_lists.size());

        int i = 0;
        for(TaskDescription one_task: task_lists){
            CustomTaskView new_task = new CustomTaskView(this);
            new_task.setTitle(one_task.title);
            new_task.setDescription(one_task.description);
            new_task.setClickable(true);

            task_place_layout.addView(new_task);
            taskViews.add(new_task);
            new_task.setOnClickListener(taskviewClickListener);

            all_scores.setScores(i, new Scores(one_task.title,
                                                name_edit.getText().toString()));
            all_scores.setCustom_scores(i, new Scores(one_task.title,
                                                        name_edit.getText().toString(),
                                                        Utils.getSize(question_lists_bypage)));
            i++;
        }


    }

    Button.OnClickListener taskviewClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            //for (CustomTaskView one_task : taskViews) {
            for (int i=0; i<taskViews.size(); i++) {
                CustomTaskView one_task = taskViews.get(i);
                if (arg0 == one_task) {
                    if (CONSTANTS.enable_tlx || CONSTANTS.enable_custom_question) {
                        Log.d(TAG, "Task selected: "+one_task.getTitle().getText().toString() );
                        Intent intent;
                        if(CONSTANTS.enable_tlx)
                            intent = new Intent(getApplicationContext(), TLXFirstStepActivity.class);
                        else
                            intent = new Intent(getApplicationContext(), CustomQuestionActivity.class);

                        intent.putExtra("name", name_edit.getText().toString());    //if you need to pass parameters
                        intent.putExtra("date", date_edit.getText().toString());    //if you need to pass parameters
                        intent.putExtra("task", one_task.getTitle().getText().toString());    //if you need to pass parameters
                        intent.putExtra("task_id", i);    //if you need to pass parameters

                        all_scores.setNAME(name_edit.getText().toString());
                        all_scores.setDATE(date_edit.getText().toString());

                        intent.putExtra("all_scores", all_scores);    //if you need to pass parameters

                        startActivityForResult(intent, REQUEST_CODE);
                    }else{
                        Log.d(TAG, "Task selected: No questions to show" );
                        Toast.makeText(getApplicationContext(), "Select at least one of TLX/Custom question", Toast.LENGTH_LONG).show();
                    }
                }
            }

        }
    };


    private void checkPermission(){
        // Here, thisActivity is the current activity
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        FILE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Succeed: FileWrite Permission", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(getApplicationContext(), "Failed: FileWrite Permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult:"+requestCode+","+resultCode+"|"+REQUEST_CODE+","+Activity.RESULT_OK);
        Log.d(TAG, "onActivityResult:"+(requestCode == REQUEST_CODE)+"|"+(resultCode == Activity.RESULT_OK));

        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Log.d(TAG, "RESULT_OK");

                all_scores = (SerializableScores) intent.getSerializableExtra("all_scores");
                Log.d(TAG, "Update all_scores");

                for(int i=0; i<all_scores.getSize(); i++) {
                    try {
                        taskViews.get(i).setDone(all_scores.getDone(i));
                        Log.d(TAG, "Result " + i + ": " + all_scores.getScore(i).toOnelineString());
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Result " + i + ": " + all_scores.getDone(i));
                        taskViews.get(i).setDone(false);
                    }
                }
                all_scores.toCSV();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "RESULT_CANCELED");
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }//onActivityResult

}
