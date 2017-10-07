package com.srmhackathon.credible;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SemanticRolesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SemanticRolesSubject;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText url;

    private int com;

    private int 

    private float R1_emotion_score = 0;

    private float R2_emotion_score = 0;

    private StrictMode.ThreadPolicy policy;

    private ArrayList<String> top5 = new ArrayList<String>();

    private ArrayList<String> R1_keywords = new ArrayList<String>();

    private ArrayList<String> R2_keywords = new ArrayList<String>();

    private SeekBar titlebodyseek;

    private SeekBar keywordmatchseek;

    private SeekBar googleseek;

    private String USERNAME = "6a872cfb-9761-41ce-a308-271a80101b0a";

    private String PASSWORD = "VF1nPTAeKpNt";

    private String SCOOPWHOOP_TITLE = "artTitle";

    private String SCOOPWHOOP_CONTENT = "articleContentData";

    private String GOOGLE_QUERY = "";

    private String R1_highest_emotion;

    private String R2_highest_emotion;

    private String R1_K1;

    private String R1_K2;

    private String R1_K3;

    private String R2_K1;

    private String R2_K2;

    private String R2_K3;

    private String urlcheck;

    private String urlchecked;

    private String host;

    private String R1_textscore;

    private String R2_textscore;

    private String scoopwhoop_title;

    private String scoopwhoop_content;

    private TextView highest_emotion;

    private TextView tk1;

    private TextView tk2;

    private TextView tk3;

    private TextView ck1;

    private TextView ck2;

    private TextView ck3;

    private TextView gs1;

    private TextView gs2;

    private TextView gs3;

    private TextView gs4;

    private TextView gs5;

    private LinearLayout start;

    private LinearLayout report;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        start = (LinearLayout) findViewById(R.id.start);

        report = (LinearLayout) findViewById(R.id.report);

        highest_emotion = (TextView) findViewById(R.id.highest_emotion);

        titlebodyseek = (SeekBar) findViewById(R.id.titlebodyseek);

        keywordmatchseek = (SeekBar) findViewById(R.id.keywordseek);

        googleseek = (SeekBar) findViewById(R.id.googleseek);

        titlebodyseek.setEnabled(false);

        keywordmatchseek.setEnabled(false);

        googleseek.setEnabled(false);

        tk1 = (TextView) findViewById(R.id.tk1);

        tk2 = (TextView) findViewById(R.id.tk2);

        tk3 = (TextView) findViewById(R.id.tk3);

        ck1 = (TextView) findViewById(R.id.ck1);

        ck2 = (TextView) findViewById(R.id.ck2);

        ck3 = (TextView) findViewById(R.id.ck3);

        gs1 = (TextView) findViewById(R.id.gs1);

        gs2 = (TextView) findViewById(R.id.gs2);

        gs3 = (TextView) findViewById(R.id.gs3);

        gs4 = (TextView) findViewById(R.id.gs4);

        gs5 = (TextView) findViewById(R.id.gs5);

        url = (EditText) findViewById(R.id.url);

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(this);

    }


    private void alert(String message) {

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

    }


    private void hostDetection() {

        if (!url.getText().toString().isEmpty()) {

            urlcheck = url.getText().toString().trim();

            com = urlcheck.indexOf(".com");

            urlchecked = urlcheck.substring(0, com);

            if (urlchecked.contains("scoopwhoop")) {

                host = "Scoopwhoop";

                scrapeData(host);

            } else if ((urlchecked.contains("buzzfeed"))) {

                host = "Buzzfeed";

                alert("HOST: BUZZFEED");

            } else if ((urlchecked.contains("menxp"))) {

                host = "MenXP";

                alert("HOST: MENXP");

            } else {

                alert("Not Yet Supported");

            }

        } else {

            alert("The URL Cannot Be Empty!");

        }

    }


    private void scrapeData(String host) {

        if (host.equalsIgnoreCase("scoopwhoop")) {

            try {

                Document doc = Jsoup.connect(urlcheck).get();

                Elements title = doc.select("h1." + SCOOPWHOOP_TITLE);

                Elements body = doc.select("div." + SCOOPWHOOP_CONTENT).select("p");

                for (Element element : title) {

                    scoopwhoop_title = element.text();

                }

                for (Element element : body) {

                    scoopwhoop_content += element.text();

                }

                watsonNLU(scoopwhoop_title, "R1");

            } catch (IOException io) {

                alert(io.toString());

            }

        }

    }

    private void googleTOP5(String query) {

        try {

            int i = 0;

            int j = 0;

            Document doc = Jsoup.connect("https://www.google.com/search?q=" + query).get();

            Elements title = doc.select("div.srg").select("h3.r").remove();

            while (i < 5) {

                for (Element element : title) {

                    top5.add(element.text());

                    i++;

                }

            }

            gs1.setText(top5.get(0));

            gs2.setText(top5.get(1));

            gs3.setText(top5.get(2));

            gs4.setText(top5.get(3));

            gs5.setText(top5.get(4));

        } catch (IOException io) {

            alert(io.toString());

        }

    }


    private void watsonNLU(String text, String context) {

        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, USERNAME, PASSWORD);

        SentimentOptions sentimentOptions = new SentimentOptions.Builder()
                .document(true)
                .build();

        EmotionOptions emotionOptions = new EmotionOptions.Builder()
                .document(true)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(5)
                .build();

        SemanticRolesOptions semanticRolesOptions = new SemanticRolesOptions.Builder()
                .build();

        Features features = new Features.Builder()
                .sentiment(sentimentOptions)
                .emotion(emotionOptions)
                .keywords(keywordsOptions)
                .semanticRoles(semanticRolesOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = service
                .analyze(parameters)
                .execute();

        Log.d("NLU", response.toString());

        if (context == "R1") {

            try {

                // RESULT

                JSONObject result = new JSONObject(response.toString());


                // SCORE

                JSONObject sentiment = result.getJSONObject("sentiment");

                JSONObject scoredocument = sentiment.getJSONObject("document");


                // EMOTIONS

                JSONObject emo = result.getJSONObject("emotion");

                JSONObject emotionsdocument = emo.getJSONObject("document");

                JSONObject emoemotions = emotionsdocument.getJSONObject("emotion");


                // KEYWORDS

                JSONArray key_texts = result.getJSONArray("keywords");

                int emos = key_texts.length();

                if (emos >= 3) {

                    JSONObject check1 = key_texts.getJSONObject(0);

                    JSONObject check2 = key_texts.getJSONObject(1);

                    JSONObject check3 = key_texts.getJSONObject(2);

                    R1_K1 = check1.getString("text");

                    R1_K2 = check2.getString("text");

                    R1_K3 = check3.getString("text");

                    tk1.setText(R1_K1);

                    tk2.setText(R1_K2);

                    tk3.setText(R1_K3);

                } else {

                    if (emos == 1) {

                        JSONObject check1 = key_texts.getJSONObject(0);

                        R1_K1 = check1.getString("text");

                        tk1.setText(R1_K1);

                    } else if (emos == 2) {

                        JSONObject check1 = key_texts.getJSONObject(0);

                        JSONObject check2 = key_texts.getJSONObject(1);

                        R1_K1 = check1.getString("text");

                        R1_K2 = check2.getString("text");

                        tk1.setText(R1_K1);

                        tk2.setText(R1_K2);

                    }

                }

                // FINAL DECLARATIONS

                R1_textscore = scoredocument.getString("score");

                Iterator<String> iter = emoemotions.keys();

                while (iter.hasNext()) {

                    String key = iter.next();

                    try {

                        Object value = emoemotions.get(key);

                        if (Float.parseFloat(value.toString()) > R1_emotion_score) {

                            R1_highest_emotion = key;

                            R1_emotion_score = Float.parseFloat(value.toString());

                            highest_emotion.setText(R1_highest_emotion);

                            watsonNLU(scoopwhoop_content, "R2");

                        }

                    } catch (JSONException e) {

                        alert(e.toString());

                    }

                }

            } catch (JSONException je) {

                alert(je.toString());

            }

        } else if (context == "R2") {

            try {

                // RESULT

                JSONObject result = new JSONObject(response.toString());


                // SCORE

                JSONObject sentiment = result.getJSONObject("sentiment");

                JSONObject scoredocument = sentiment.getJSONObject("document");


                // EMOTIONS

                JSONObject emo = result.getJSONObject("emotion");

                JSONObject emotionsdocument = emo.getJSONObject("document");

                JSONObject emoemotions = emotionsdocument.getJSONObject("emotion");


                // KEYWORDS

                JSONArray key_texts = result.getJSONArray("keywords");

                int emos = key_texts.length();

                if (emos >= 3) {

                    JSONObject check1 = key_texts.getJSONObject(0);

                    JSONObject check2 = key_texts.getJSONObject(1);

                    JSONObject check3 = key_texts.getJSONObject(2);

                    R2_K1 = check1.getString("text");

                    R2_K2 = check2.getString("text");

                    R2_K3 = check3.getString("text");

                    ck1.setText(R2_K1);

                    ck2.setText(R2_K2);

                    ck3.setText(R2_K3);

                } else {

                    if (emos == 1) {

                        JSONObject check1 = key_texts.getJSONObject(0);

                        R2_K1 = check1.getString("text");

                        ck1.setText(R2_K1);

                    } else if (emos == 2) {

                        JSONObject check1 = key_texts.getJSONObject(0);

                        JSONObject check2 = key_texts.getJSONObject(1);

                        R2_K1 = check1.getString("text");

                        R2_K2 = check2.getString("text");

                        ck1.setText(R2_K1);

                        ck2.setText(R2_K2);

                    }

                }

                // FINAL DECLARATIONS

                R2_textscore = scoredocument.getString("score");

                Iterator<String> iter = emoemotions.keys();

                while (iter.hasNext()) {

                    String key = iter.next();

                    try {

                        Object value = emoemotions.get(key);

                        if (Float.parseFloat(value.toString()) > R2_emotion_score) {

                            R2_highest_emotion = key;

                            R2_emotion_score = Float.parseFloat(value.toString());

                            GOOGLE_QUERY = scoopwhoop_title.replace(" ", "+");

                            googleTOP5(GOOGLE_QUERY);

                            algorithm();

                        }

                    } catch (JSONException e) {

                        alert(e.toString());

                    }

                }

            } catch (JSONException je) {

                alert(je.toString());

            }

        } else {

            alert("Invalid Context");

        }

    }


    private void algorithm() {

        if (gs1.getText().toString().contains(ck1.getText()) ||)

        start.setVisibility(View.GONE);

        report.setVisibility(View.VISIBLE);

    }


    @Override
    public void onClick(View v) {

        if (v == submit) {

            hostDetection();

        }

    }


}
