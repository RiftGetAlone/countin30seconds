package ptusoftwraestudio.countin30seconds;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView answerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        answerTitle = (TextView)findViewById(R.id.textView1);
       // Typeface face=Typeface.createFromAsset(getAssets(),"assets/fonts/JosefinSans-Regular.ttf");
       // answerTitle.setTypeface(face);
    }
}
