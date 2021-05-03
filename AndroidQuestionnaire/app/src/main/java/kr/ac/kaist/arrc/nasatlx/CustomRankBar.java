package kr.ac.kaist.arrc.nasatlx;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Juyoung LEE on 4/26/2018.
 */

public class CustomRankBar  extends LinearLayout{
    String TAG = "CustomRankBar";
    TextView title, text;
    TextView left_endpoint, right_endpoint;
    View black_line;
    RadioGroup scores;
    RadioButton[] rb;

    int scale;
    private int selected_point = -1;


    public CustomRankBar(Context context) {
        super(context);
        initView();
        this.addRadioButton(context);

    }
    public CustomRankBar(Context context, int scale) {
        super(context);
        initView();
        this.scale = scale;
        this.addRadioButton(context);

    }

    public CustomRankBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
        this.addRadioButton(context, scale);

    }

    public CustomRankBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
        this.addRadioButton(context, scale);

    }


    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.rank_bar, this, false);
        addView(v);

        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.description);
        scores = (RadioGroup) findViewById(R.id.scores);

        left_endpoint = (TextView) findViewById(R.id.left_end);
        right_endpoint = (TextView) findViewById(R.id.right_end);

        black_line = findViewById(R.id.black_bottom_line);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RankBar);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RankBar, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {


        String title_string  = typedArray.getString(R.styleable.RankBar_rb_title);
        this.setTitle(title_string);

        String text_string = typedArray.getString(R.styleable.RankBar_rb_description);
        this.setDescription(text_string);

        scale = typedArray.getInt(R.styleable.RankBar_rb_scale, 21);


        String left_end = typedArray.getString(R.styleable.RankBar_rb_left_end);
        if(left_end==null){
            left_endpoint.setText(R.string.very_low);
        }else{
            left_endpoint.setText(left_end);
        }

        String right_end = typedArray.getString(R.styleable.RankBar_rb_right_end);
        if(right_end==null){
            right_endpoint.setText(R.string.very_high);
        }else{
            right_endpoint.setText(right_end);
        }


        typedArray.recycle();

    }

    private void addRadioButton(Context context) {

        this.addRadioButton(context, 7);

    }

    private void addRadioButton(Context context, int number) {


        rb = new RadioButton[number];
        int mid_point = (number-1)/2;
        int width = scores.getWidth()/number;


        for(int i=0; i < number; i++){
            rb[i]  = new RadioButton(context);
            rb[i].setId(i + 100);
//            rb[i].setLayoutParams(new LayoutParams(, LayoutParams.WRAP_CONTENT, 1f));
            // rb[i].setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
            rb[i].setLayoutParams(new RadioGroup.LayoutParams(width, LayoutParams.WRAP_CONTENT, 1));
            rb[i].setGravity(Gravity.CENTER | Gravity.BOTTOM);
            rb[i].setScaleX(1f);


            if (i == mid_point) {
                rb[i].setButtonDrawable(R.drawable.custom_long_radio);
            }
            else {
                rb[i].setButtonDrawable(R.drawable.custom_small_radio);
            }

            scores.addView(rb[i]);
        }


        scores.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("CustomRankBar", "selected: "+title.getText().toString()+"|"+checkedId);
                selected_point = checkedId-100;
            }
        });

        /*
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins( -(width/2), 0, -(width/2), 0);
        scores.setLayoutParams(params);
        */





    }

    void setTitle(String title_str) {
        title.setText(title_str);

    }void setTitle(int text_resID) {
        title.setText(text_resID);
    }

    void setDescription(String desc_str) {
        text.setText(desc_str);
    }void setDescription(int text_resID) {
        text.setText(text_resID);
    }

    public int getSelected_point() {
        return selected_point;
    }

    public void setSelected_point(int i) {
        this.rb[i].setChecked(true);
    }
}


