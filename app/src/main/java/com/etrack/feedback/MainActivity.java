package com.etrack.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private LinearLayout ll_good, ll_average, ll_sad;
    private AlertDialog.Builder alert;
    private QuestionsSpreadsheetWebService spreadsheetWebService;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        alert = new AlertDialog.Builder(this);
        showPopup();
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        this.getWindow().setAttributes(params);
        textToSpeech = new TextToSpeech(MainActivity.this, MainActivity.this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/e/")
                //  https://docs.google.com/forms/d/1PA1sORbNDO9vvgbUUkAoyf0xnxszvHXedbBTd9WlVD4/formResponse
                .build();
        spreadsheetWebService = retrofit.create(QuestionsSpreadsheetWebService.class);
        ll_good = (LinearLayout) findViewById(R.id.ll_good);
        ll_average = (LinearLayout) findViewById(R.id.ll_average);
        ll_sad = (LinearLayout) findViewById(R.id.ll_sad);

        ll_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDia("good");
            }
        });
        ll_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDia("average");
            }
        });
        ll_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDia("sad");
            }
        });
    }

    private void showPopup() {
        String companyName = SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this, SharedPreferenceHelper.PREF_APP_COMP_NAME, "empty");
        if (companyName.equals("empty")) {
            showCompDia();
        }
    }

    private void showCompDia() {
        final EditText edittext = new EditText(MainActivity.this);
        edittext.setMaxHeight(400);
        //edittext.setTextColor(getColor(R.color.colorPrimary));
        alert.setMessage(getString(R.string.enter_your_company_name));
        alert.setCancelable(false);
        alert.setView(edittext);
        alert.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edittext.getText().toString().trim().length() > 0) {
                    String compName = edittext.getText().toString();
                    SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this, SharedPreferenceHelper.PREF_APP_COMP_NAME, compName);
                }
            }
        });

       /* alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });*/

        alert.show();
    }

    private void showDia(final String good) {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setMaxHeight(400);
        edittext.setTextColor(Color.parseColor("#000000"));
        alert.setMessage(getString(R.string.Enter_Your_Comment));
        alert.setCancelable(false);
        alert.setView(edittext);
        alert.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (ConnectivityReceiver.isConnected(MainActivity.this)) {
                    String comment = edittext.getText().toString();
                    ProgressUtils.startProgress(MainActivity.this, getString(R.string.posting_feedback), getString(R.string.please_wait), false);
                    String companyName = SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this, SharedPreferenceHelper.PREF_APP_COMP_NAME, "empty");
                    Call<Void> completeQuestionnaireCall = spreadsheetWebService.completeQuestionnaire(companyName, good, comment);
                    completeQuestionnaireCall.enqueue(callCallback);
                }
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private final Callback<Void> callCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Log.d("XXX", "Submitted. " + response);
            TextToSpeechFunction();
            ProgressUtils.stopProgress();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            ProgressUtils.stopProgress();
            Log.e("XXX", "Failed", t);
        }

    };

    private void TextToSpeechFunction() {
        textToSpeech.speak("Thank you for your feedback", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textToSpeech.setLanguage(Locale.US);
            //    TextToSpeechFunction();
        }

    }

    @Override
    public void onDestroy() {

        textToSpeech.shutdown();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
}

interface QuestionsSpreadsheetWebService {

    @POST("1FAIpQLScYiaNVvaLn9V8P3pMHblwx4OypdleSaKTXxi10jMSlPkENTg/formResponse")

//    @POST("1PA1sORbNDO9vvgbUUkAoyf0xnxszvHXedbBTd9WlVD4/formResponse")
    @FormUrlEncoded
    Call<Void> completeQuestionnaire(
            @Field("entry.1596902811") String companyName,
            @Field("entry.2147445435") String type,
            @Field("entry.294350197") String description

    );


}