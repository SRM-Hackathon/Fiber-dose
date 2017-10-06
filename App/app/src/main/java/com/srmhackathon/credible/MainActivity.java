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
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText url;

    private int com;

    private StrictMode.ThreadPolicy policy;

    private String urlcheck;

    private String urlchecked;

    private String host;

    private String scoopwhoop_title;

    private String scoopwhoop_content;

    private String SCOOPWHOOP_TITLE = "artTitle";

    private String SCOOPWHOOP_CONTENT = "sw_para";

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


    private void watsonNLU(String text) {

        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, "6a872cfb-9761-41ce-a308-271a80101b0a", "VF1nPTAeKpNt");


        SentimentOptions sentimentOptions = new SentimentOptions.Builder()
                .document(true)
                .build();

        Features features = new Features.Builder()
                .sentiment(sentimentOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = service
                .analyze(parameters)
                .execute();

        Log.d("JSON",response.toString());

    }


    private void hostDetection() {

        if (urlcheck != "") {

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

        }

        else {

            alert("The URL Cannot Be Empty!");

        }

    }


    private void scrapeData(String host) {

        if (host.equalsIgnoreCase("scoopwhoop")) {

            try {

                Document doc = Jsoup.connect(urlcheck).get();

                Elements title = doc.select("h1." + SCOOPWHOOP_TITLE);

                Elements body = doc.select("h1." + SCOOPWHOOP_CONTENT).select("p");

                for (Element element : title) {

                    scoopwhoop_title = element.text();

                }

                for (Element element : body) {

                    scoopwhoop_content += element.text();

                }

                watsonNLU(scoopwhoop_title);

            } catch (IOException io) {

                alert(io.toString());

            }

        }

    }


    @Override
    public void onClick(View v) {

        if (v == submit) {

            hostDetection();

        }

    }


}
