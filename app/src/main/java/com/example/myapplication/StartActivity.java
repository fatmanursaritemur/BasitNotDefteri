package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.user.LoginActivity;
import com.example.myapplication.user.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;



public class StartActivity extends AppCompatActivity {
    private Button buttonreg,buttonlog;
    private FirebaseAuth faulth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        buttonlog=(Button)findViewById(R.id.logbutton);
        buttonreg=(Button)findViewById(R.id.registerbutton);
        faulth=FirebaseAuth.getInstance();
        updateIU();
        buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register()
    {
        Intent registerintent=new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(registerintent);
    }

    private void login()
    {
        Intent loginintent=new Intent(StartActivity.this, LoginActivity.class);
        startActivity(loginintent);
    }
    private void updateIU()
    {
        if(faulth.getCurrentUser()!=null){
            Log.i("StartActivity","fault !=null");
            Intent startintent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(startintent);
            finish();
        }
        else
        {

            Log.i("StartActivity","fault =null");
        }

    }
}
