package com.example.mos3if;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class StepsActivity extends AppCompatActivity {

    ImageView img ;
    TextView title , step21, step22 , step23 , step24 , step25;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        img=findViewById(R.id.img);
        title=findViewById(R.id.title);
        step21=findViewById(R.id.step21);
        step22=findViewById(R.id.step22);
        step23=findViewById(R.id.step23);
        step24=findViewById(R.id.step24);
        step25=findViewById(R.id.step25);



        Intent intent = getIntent();
        String heading = intent.getExtras().getString("title");
        int image = intent.getExtras().getInt("image");

        String step1 = intent.getExtras().getString("step1");
        String step2 = intent.getExtras().getString("step2");
        String step3 = intent.getExtras().getString("step3");
        String step4 = intent.getExtras().getString("step4");
        String step5 = intent.getExtras().getString("step5");



        img.setImageResource(image);

        title.setText(heading);
        step21.setText(step1);
        step22.setText(step2);
        step23.setText(step3);
        step24.setText(step4);
        step25.setText(step5);
    }
}