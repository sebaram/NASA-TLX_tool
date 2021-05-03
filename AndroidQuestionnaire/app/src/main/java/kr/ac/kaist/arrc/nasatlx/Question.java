package kr.ac.kaist.arrc.nasatlx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import static kr.ac.kaist.arrc.nasatlx.Utils.getDirForDevice;

/**
 * Created by Juyoung LEE on 2018-07-25.
 */

public class Question{
    public int Q_TYPE;
    String Q_TITLE, Q_DESCRIPTION;
    String Q_VIDEOPATH = "";
    String Q_RESULT = "-1";
    public Question(String q_title, String q_description){
        Q_TITLE = q_title;
        Q_DESCRIPTION = q_description;
    }


    public Question(int q_type, String q_title, String q_description){
        Q_TYPE = q_type;
        Q_TITLE = q_title;
        Q_DESCRIPTION = q_description;
    }

    public Question(int q_type, String q_title, String q_description, String video_path){
        Q_TYPE = q_type;
        Q_TITLE = q_title;
        Q_DESCRIPTION = q_description;
        Q_VIDEOPATH = video_path;
    }
    public LinearLayout createView(Context context){
        LinearLayout linear_layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linear_layout.setLayoutParams(params);
        linear_layout.setGravity(Gravity.CENTER);
        linear_layout.setOrientation(LinearLayout.VERTICAL);
        linear_layout.setPadding(0,20,0,30);


        // Title
        TextView title = new TextView(context);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        title.setText(this.Q_TITLE);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);

        linear_layout.addView(title);


        // Description Video
        if (!Q_VIDEOPATH.equals("")) {


            LinearLayout video_layout = new LinearLayout(context);
            video_layout.setLayoutParams(new LinearLayout.LayoutParams(1600, 900));
            video_layout.setGravity(Gravity.CENTER);
            video_layout.setOrientation(LinearLayout.VERTICAL);
//            video_layout.setPadding(0,100,0,100);


            String video_file = getDirForDevice()+Q_VIDEOPATH;

            VideoView video_view = new VideoView(context);
            video_view.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            video_layout.addView(video_view);

            video_view.setVideoPath(video_file);
            video_view.seekTo(100); //to show preview

            final MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(video_view);
            video_view.setMediaController(mediaController);

            linear_layout.addView(video_layout);

            TextView space = new TextView(context);
            space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            linear_layout.addView(space);



        }



        return linear_layout;
    }
    public String getResult(){
        return Q_RESULT;
    }



}
class CheckBoxQuestion extends Question{
    CheckBox[] checkBoxes;

    public CheckBoxQuestion(String q_title, String q_description){
        super(q_title, q_description);
    }
    public CheckBoxQuestion(int q_type, String q_title, String q_description){
        super(q_type, q_title, q_description);
    }

    public LinearLayout createView(Context context){
        LinearLayout linear_layout;
        linear_layout = super.createView(context);


        LinearLayout questions_layout = new LinearLayout(context);
        questions_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        questions_layout.setOrientation(LinearLayout.VERTICAL);
        questions_layout.setPadding(20,20,20,20);

        String[] all_radios = this.Q_DESCRIPTION.split(";");

        checkBoxes = new CheckBox[all_radios.length];
        for(int j=0; j<all_radios.length; j++) {
            checkBoxes[j] = new CheckBox(context);
            checkBoxes[j].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBoxes[j].setText(all_radios[j]);
            checkBoxes[j].setTextSize(20);
            checkBoxes[j].setPadding(20,0,0,0);
            questions_layout.addView(checkBoxes[j]);
        }
        linear_layout.addView(questions_layout);

        return linear_layout;
    }
    public String getResult(){
        String result = "\"";
        String result_num = "\"";
        int count = 0;
        for(int j = 0; j<this.checkBoxes.length; j++) {
            if (this.checkBoxes[j].isChecked()) {
                if (count == 0) {
                    result += this.checkBoxes[j].getText().toString();
                }else{
                    result += ";"+this.checkBoxes[j].getText().toString();
                }

                result_num+="1";
                count++;
            }else{
                result_num+="0";
            }
        }
        result += "\"";
        result_num += "\"";

        return result+", "+result_num;
    }

}
class RadioQuestion extends Question{
    RadioGroup rg;
    RadioButton[] rb;
    public RadioQuestion(String q_title, String q_description){
        super(q_title, q_description);
    }
    public RadioQuestion(int q_type, String q_title, String q_description){
        super(q_type, q_title, q_description);
    }

    public LinearLayout createView(Context context){
        LinearLayout linear_layout;
        linear_layout = super.createView(context);

        String[] all_radios = this.Q_DESCRIPTION.split(";");

        rb = new RadioButton[all_radios.length];
        rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int i=0; i<rb.length; i++){
            rb[i]  = new RadioButton(context);
            rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[i].setText(all_radios[i]);
            rb[i].setTextSize(20);
        }
        linear_layout.addView(rg);


        return linear_layout;
    }
    public String getResult(){
        String result="-1,-1";

        for(int i=0; i<rb.length; i++) {
            if(rb[i].isChecked()){
                result = rb[i].getText().toString() + ", " + i;
                return result;
            }
        }
        return result;
    }


}
class LikertQuestion extends Question{
    String left_end, right_end;
    int scale;

    RadioGroup rg;
    RadioButton[] rb;

    public LikertQuestion(String q_title, String q_description){
        super(q_title, q_description);
    }
    public LikertQuestion(int q_type, String q_title, String q_description){
        super(q_type, q_title, q_description);
    }

    public LinearLayout createView(Context context){
        LinearLayout linear_layout;
        linear_layout = super.createView(context);

        String[] all_radios = this.Q_DESCRIPTION.split(";");



//        SeekBar seekBar = new SeekBar(context,null, R.style.Widget_AppCompat_SeekBar_Discrete);
//        SeekBar seekBar = new SeekBar(context);
//        seekBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
//        seekBar.setProgress(3);
//        seekBar.setMax(6);
//        seekBar.setTickMark( context.getDrawable(R.drawable.tickmark));
//
//        linear_layout.addView(seekBar);

        RadioGroup.LayoutParams button_params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button_params.weight = 1;

        rb = new RadioButton[7];
        rg = new RadioGroup(context);
        rg.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rg.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        rg.setGravity(Gravity.CENTER);
        for(int i=0; i<7; i++){
            rb[i]  = new RadioButton(context);
            rb[i].setLayoutParams(button_params);
            rb[i].setGravity(Gravity.CENTER);
            rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout

        }
        linear_layout.addView(rg);

        LinearLayout bottom_text = new LinearLayout(context);
        bottom_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bottom_text.setPadding(0,20,150,20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 0;

        TextView left_end = new TextView(context);
        left_end.setLayoutParams(params);
        left_end.setText("전혀");
        left_end.setGravity(Gravity.LEFT);


        TextView right_end = new TextView(context);
        right_end.setLayoutParams(params);
        right_end.setText("매우");
        right_end.setGravity(Gravity.RIGHT);


        params.weight = 1;
        TextView middle = new TextView(context);
        middle.setLayoutParams(params);
        middle.setText("중간");
        middle.setGravity(Gravity.CENTER);

        bottom_text.addView(left_end);
        bottom_text.addView(middle);
        bottom_text.addView(right_end);

        linear_layout.addView(bottom_text);

        return linear_layout;
    }
    public String getResult(){
        String result="-1,-1";

        for(int i=0; i<rb.length; i++) {
            if(rb[i].isChecked()){
                result = i + ", " + i;
                return result;
            }
        }
        return result;
    }

}
class TextQuestion extends Question{
    public TextQuestion(String q_title, String q_description){
        super(q_title, q_description);
    }
    public TextQuestion(int q_type, String q_title, String q_description){
        super(q_type, q_title, q_description);
    }

}
