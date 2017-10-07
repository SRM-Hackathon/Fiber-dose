package com.srmhackathon.credible;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FinalReport extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_report);

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

    }
}
