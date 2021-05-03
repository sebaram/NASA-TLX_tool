package kr.ac.kaist.arrc.nasatlx;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Juyoung LEE on 4/26/2018.
 */

public class CustomTaskView extends LinearLayout{
    private TextView title, text;
    private RadioButton done_radiobtn;

    public CustomTaskView(Context context) {
        super(context);
        initView();

    }

    public CustomTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

    }

    public CustomTaskView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);

    }


    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.task_view, this, false);
        addView(v);

        title = (TextView) findViewById(R.id.task_title);
        text = (TextView) findViewById(R.id.task_description);
        done_radiobtn = findViewById(R.id.done_check);


    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TaskView);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TaskView, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {


        String title_string  = typedArray.getString(R.styleable.TaskView_task_title);
        this.setTitle(title_string);

        String text_string = typedArray.getString(R.styleable.TaskView_task_description);
        this.setDescription(text_string);


        typedArray.recycle();

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

    public TextView getTitle() {
        return title;
    }

    public TextView getText() {
        return text;
    }

    public void setDone(){
        this.done_radiobtn.setChecked(true);
    }
    public void setDone(boolean result){
        this.done_radiobtn.setChecked(result);
    }

}
