package com.srmhackathon.credible;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
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

    private float R1_emotion_score = 0;

    private StrictMode.ThreadPolicy policy;

    private ArrayList<String> top5 = new ArrayList<String>();

    private ArrayList<String> R1_keywords = new ArrayList<String>();

    private String R1_highest_emotion;

    private String R1_K1;

    private String R1_K2;

    private String R1_K3;

    private String urlcheck;

    private String urlchecked;

    private String host;

    private String R1_textscore;

    private String scoopwhoop_title;

    private String scoopwhoop_content;

    private String USERNAME = "6a872cfb-9761-41ce-a308-271a80101b0a";

    private String PASSWORD = "VF1nPTAeKpNt";

    private String SCOOPWHOOP_TITLE = "artTitle";

    private String SCOOPWHOOP_CONTENT = "articleContentData";

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

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

                watsonNLU("23 amusing ways you could die", "R1");

            } catch (IOException io) {

                alert(io.toString());

            }

        }

    }

    private void googleTOP5() {

        try {

            int i = 0;

            int j = 0;

            Document doc = Jsoup.connect("https://www.google.com/search?q=rahul+gandhi+ai").get();

            Elements title = doc.select("div.srg").select("h3.r").remove();

            while (i < 5) {

                for (Element element : title) {

                    top5.add(element.text());

                    i++;

                }

            }

            for (j=0; j<=5; j++) {

                alert(top5.get(j));

            }

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

        Features features = new Features.Builder()
                .sentiment(sentimentOptions)
                .emotion(emotionOptions)
                .keywords(keywordsOptions)
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

                        }

                    } catch (JSONException e) {

                        alert(e.toString());

                    }

                }

            } catch (JSONException je) {

                alert("YO: "+je.toString());

            }

        } else {

            alert("Invalid Context");

        }

    }


    @Override
    public void onClick(View v) {

        if (v == submit) {

            hostDetection();

        }

    }


}
