package com.example.myapplication.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button btnReg;
    private TextInputLayout inputName, inputEmail, inputPass;
    //ProgressDialog ile kullanıcıya bilgi mesajı verilir
    private ProgressDialog progressDialog;
    private FirebaseAuth firebasea;
    private DatabaseReference fUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnReg = (Button) findViewById(R.id.btn_reg);
        inputName = (TextInputLayout) findViewById(R.id.reg_name);
        inputEmail = (TextInputLayout) findViewById(R.id.reg_mail);
        inputPass = (TextInputLayout) findViewById(R.id.reg_password);

        firebasea = FirebaseAuth.getInstance();
        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oname = inputName.getEditText().getText().toString().trim();
                String oemail = inputEmail.getEditText().getText().toString().trim();
                String opass = inputPass.getEditText().getText().toString().trim();

                registerUser(oname, oemail, opass);
            }
        });
    }

    private void registerUser(final String name, String email, String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("İsteğiniz işleniyor, lütfen bekleyiniz...");

        progressDialog.show();
//kullanıcı mail ve pasaport ile kaydedilir
        firebasea.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            fUsersDatabase.child(firebasea.getCurrentUser().getUid())
                                    //database'de name adlı child oluşur.
                                    .child("basic").child("name").setValue(name)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                progressDialog.dismiss();

                                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(mainIntent);
                                                finish();
                                                Toast.makeText(RegisterActivity.this, "Kayıt Yapıldı!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "Hata : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        } else {

                            progressDialog.dismiss();

                            Toast.makeText(RegisterActivity.this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}