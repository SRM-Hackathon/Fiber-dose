package com.srmhackathon.credible;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText url;

    private int com;

    private String urlcheck;

    private String host;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        urlcheck = urlcheck.substring(0, com);

        if (urlcheck.contains("scoopwhoop")) {

            host = "Scoopwhoop";

            alert("HOST: SCOOPWHOOP");

        }

        else if ((urlcheck.contains("buzzfeed"))) {

            host = "Buzzfeed";

            alert("HOST: BUZZFEED");

        }

        else if ((urlcheck.contains("menxp"))) {

            host = "MenXP";

            alert("HOST: MENXP");

        }

        else {

            alert("Not Yet Supported");

        }

    }


    @Override
    public void onClick(View v) {

        if (v == submit) {

            hostDetection();

        }

    }


}
