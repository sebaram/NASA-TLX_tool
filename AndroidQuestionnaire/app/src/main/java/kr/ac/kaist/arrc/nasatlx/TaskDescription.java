package kr.ac.kaist.arrc.nasatlx;

/**
 * Created by Juyoung LEE on 4/26/2018.
 */

public class TaskDescription {
    String title;
    String description;
    TaskDescription(){
        this.title="Create \"task_lists.txt\" to change task list";
        this.description="in folder \"TaskLoadIndex\" 'n Write title and description in same line and place :: between them";
    }
    TaskDescription(String title, String description){
        this.title=title;
        this.description=description;
    }
}
