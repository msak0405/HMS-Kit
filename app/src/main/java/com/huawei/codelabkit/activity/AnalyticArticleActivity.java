package com.huawei.codelabkit.activity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.codelabkit.R;
import com.huawei.codelabkit.utilities.Global;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import java.util.Date;
import java.util.Locale;

import static com.huawei.hms.analytics.type.HAEventType.*;
import static com.huawei.hms.analytics.type.HAParamType.*;

public class AnalyticArticleActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtQuestion, txtScoreValue;
    Button btnNext, btnQuit;
    RadioGroup radio_g;

    RadioButton rdbOption1, rdbOption2, rdbOption3, rdbOption4;

    String questions[] = {
            "Which is the Capital of Finland?",
            "For which of the following disciplines is Nobel Prize awarded?",
            "First human heart transplant operation conducted by Dr. Christiaan Barnard on Louis Washkansky, was conducted in?",
            "Galileo was an Italian astronomer who?",
            "Which of these access specifiers can be used for an interface?",
            "Each year World Red Cross and Red Crescent Day is celebrated on?",
            "G-15 is an economic grouping of?",
            "In which city can you find the Liberty Bell?",
            "Which of these method of class String is used to compare two String objects for their equality?",
            "According to Forrest Gump, life was like"
    };
    String rightAnswers[] = {"Heslinki", "All of the above", "1967", "All of the above", "public", "May 8", "Third World Nations", "Philadelphia", "equals()", "A box of chocolates"};
    String opt[] = {
            "Espoo", "Uusimaa", "Heslinki", "Turku",
            "Physics and Chemistry", "Physiology or Medicine", "Literature, Peace and Economics", "All of the above",
            "1967", "1968", "1958", "1922",
            "developed the telescope", "discovered four satellites of Jupiter", "discovered that the movement of pendulum produces a regular time measurement", "All of the above",
            "public", "protected", "private", "All of the mentioned",
            "May 8", "May 18", "June 8", "June 18",
            "First World Nations", "Second World Nations", "Third World Nations", "Fourth World Nations",
            "Washington, D.C.", "Boston", "Philadelphia", "Manhattan",
            "equals()", "Equals()", "isequal()", "Isequal()",
            "A bag of lemons", "A handful of roses", "A lollipop", "A box of chocolates"
    };

    int flag = 0;
    public static int marks = 0, correct = 0;
    HiAnalyticsInstance instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic_article);
        Toolbar toolbar = findViewById(R.id.toolbar_article_analytic);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        HiAnalyticsTools.enableLog();
        instance = HiAnalytics.getInstance(this);

        // Enable collection capability
        instance.setAnalyticsEnabled(true);
        instance.regHmsSvcEvent();
        Global.getToken();

        txtScoreValue = (TextView) findViewById(R.id.txt_score_value);
        txtQuestion = (TextView) findViewById(R.id.txt_question);

        radio_g = (RadioGroup) findViewById(R.id.rdg_options);
        rdbOption1 = (RadioButton) findViewById(R.id.rdb_opt1);
        rdbOption2 = (RadioButton) findViewById(R.id.rdb_opt2);
        rdbOption3 = (RadioButton) findViewById(R.id.rdb_opt3);
        rdbOption4 = (RadioButton) findViewById(R.id.rdb_opt4);

        txtQuestion.setText(questions[flag]);
        rdbOption1.setText(opt[0]);
        rdbOption2.setText(opt[1]);
        rdbOption3.setText(opt[2]);
        rdbOption4.setText(opt[3]);

        btnNext = (Button) findViewById(R.id.btn_nxt_question);
        btnNext.setOnClickListener(this);
        findViewById(R.id.btn_quit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nxt_question:
                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }


                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String selectedAnswer = uans.getText().toString();
                radio_g.clearCheck();
                if (selectedAnswer.equals(rightAnswers[flag])) {
                    correct+=10;
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                }
                reportEvent(selectedAnswer, rightAnswers[flag]);

                flag++;

                if (txtScoreValue != null)
                    txtScoreValue.setText("" + correct);


                if (flag < questions.length) {
                    txtQuestion.setText(questions[flag]);
                    rdbOption1.setText(opt[flag * 4]);
                    rdbOption2.setText(opt[flag * 4 + 1]);
                    rdbOption3.setText(opt[flag * 4 + 2]);
                    rdbOption4.setText(opt[flag * 4 + 3]);
                    if(questions.length == flag+1)
                        btnNext.setText("Post Score and Return");
                }else {
                    postScore(correct);
                    correct=0;
                    this.finish();
                }
                break;
            case R.id.btn_quit:
                this.finish();
                break;

            default:
                break;
        }
    }

    private void reportEvent(String selectedAnswer, String rightAnswer) {
        Bundle bundle = new Bundle();
        bundle.putString("question", txtQuestion.getText().toString().trim());
        bundle.putString("selectedAnswer", selectedAnswer);
        bundle.putString("correctAnswer", rightAnswer);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        bundle.putString("reportedTime", sdf.format(new Date()));
        instance.onEvent("QuestionSession", bundle);
    }

    private void postScore(int correct) {
        Bundle bundle = new Bundle();
        bundle.putLong(SCORE, correct);
        instance.onEvent(SUBMITSCORE, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.unRegHmsSvcEvent();

    }
}
