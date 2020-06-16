package com.huawei.codelabkit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

import com.huawei.codelabkit.R;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import static com.huawei.hms.analytics.type.HAEventType.*;
import static com.huawei.hms.analytics.type.HAParamType.*;


public class AnalyticActivity extends AppCompatActivity {

    private Button btnSetting;

    private int questions[] = {R.string.q1, R.string.q2, R.string.q3, R.string.q4, R.string.q5};
    private boolean answers[] = {true, true, false, false, true};
    private int score = 0;

    // TODO: Define a var for Analytics Instance
    HiAnalyticsInstance instance;
    private int curQuestionIdx = 0;
    private TextView txtQuestion;
    private Button btnNext;
    private Button btnTrue;
    private Button btnFalse;
    private Button postScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_analytic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        // TODO: Initiate Analytics Kit
        // Enable Analytics Kit Log
        HiAnalyticsTools.enableLog();
        // Generate the Analytics Instance
        instance = HiAnalytics.getInstance(this);
        // You can also use Context initialization
        // Context context = this.getApplicationContext();
        // instance = HiAnalytics.getInstance(context);

        // Enable collection capability
        instance.setAnalyticsEnabled(true);
        instance.regHmsSvcEvent();

        txtQuestion = (TextView) findViewById(R.id.question_text_view);
        txtQuestion.setText(questions[curQuestionIdx]);

        btnSetting = (Button) findViewById(R.id.setting_button);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnalyticActivity.this, SettingActivity.class);
                startActivityForResult(i, 0);
            }
        });

        btnNext = (Button) findViewById(R.id.next_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curQuestionIdx = (curQuestionIdx + 1) % questions.length;
                nextQuestion();
            }
        });

        btnTrue = (Button) findViewById(R.id.true_button);
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                reportAnswerEvt("true");
            }
        });


        btnFalse = (Button) findViewById(R.id.false_button);
        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                reportAnswerEvt("false");
            }
        });

        postScore = (Button) findViewById(R.id.post_score_button);
        postScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postScore();
            }
        });
    }

    private void reportAnswerEvt(String answer) {
        // TODO: Report a customized Event
        // Event Name: Answer
        // Event Parameters:
        //  -- question: String
        //  -- answer:String
        //  -- answerTime: String

        // Initialize parameters.
        Bundle bundle = new Bundle();
        bundle.putString("question", txtQuestion.getText().toString().trim());
        bundle.putString("answer", answer);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        bundle.putString("answerTime", sdf.format(new Date()));

        // Report a predefined event.
        instance.onEvent("Answer", bundle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: UnRegistering the HMS Service
        // After the HMS service is unbound, no automatic event (account event or InAppPurchase event, etc.) is reported.
        instance.unRegHmsSvcEvent();

    }

    private void nextQuestion() {
        txtQuestion.setText(questions[curQuestionIdx]);
    }

    private boolean checkAnswer(boolean answer) {
        String q = txtQuestion.getText().toString().trim();

        if (answer == answers[curQuestionIdx]) {
            score = score + 20;
            Toast.makeText(this, R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }
        return answers[curQuestionIdx];
    }


    private void postScore() {
        // Initiate Parameters
        Bundle bundle = new Bundle();
        bundle.putLong(SCORE, score);
        // Report a predefined Event
        instance.onEvent(SUBMITSCORE, bundle);
    }

}
