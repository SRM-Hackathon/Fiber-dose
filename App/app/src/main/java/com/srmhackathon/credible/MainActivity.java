package com.srmhackathon.credible;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

    private String content;

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


    private void hostDetection() {

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


    private void scrapeData(String host) {

        if (host.equalsIgnoreCase("scoopwhoop")) {

            try {

                Document doc = Jsoup.connect(urlcheck).get();

                Elements articles = doc.select("div.articleContentData").select("p");

                for (Element element : articles) {

                    content+= element.text();

                }

                alert(content);

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
