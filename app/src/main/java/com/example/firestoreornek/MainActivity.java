package com.example.firestoreornek;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText kullaniciMail, parola;
    TextView sonuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kullaniciMail = findViewById(R.id.kullaniciMail);
        parola = findViewById(R.id.editTextTextPassword);
        sonuc = findViewById(R.id.textViewDurum);

    }

    public void giris(View view) {
        String eposta = kullaniciMail.getText().toString().trim();
        String sifre = parola.getText().toString().trim();
        if (eposta.equals("") || sifre.equals("")) {
            sonuc.setTextColor(Color.parseColor("#ff1744"));
            sonuc.setText("E-posta veya Şifre boş olamaz");
        } else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth
                    .signInWithEmailAndPassword(kullaniciMail.getText().toString().trim(),
                            parola.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            sonuc.setText("Kullanıcı girişi tamam");
                            sonuc.setTextColor(Color.parseColor("#2e7d32"));
                            Toast.makeText(MainActivity.this, "Kullanıcı girişi " +
                                            "tamam",
                                    Toast.LENGTH_SHORT).show();
                            Intent niyet = new Intent(getBaseContext(), MainActivity2CRUD.class);
                            startActivity(niyet);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sonuc.setTextColor(Color.parseColor("#ff1744"));
                            sonuc.setText("Kullanıcı girişi hatalı");
                            Toast.makeText(MainActivity.this, "Kullanıcı girişi " +
                                            "hatalı",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void kaydol(View view) {
        String eposta = kullaniciMail.getText().toString().trim();
        String sifre = parola.getText().toString().trim();
        if (eposta.equals("") || sifre.equals("")) {
            sonuc.setTextColor(Color.parseColor("#ff1744"));
            sonuc.setText("E-posta veya Şifre boş olamaz");
        } else {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(eposta, sifre)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            sonuc.setTextColor(Color.parseColor("#2e7d32"));
                            sonuc.setText("Kullanıcı oluşturuldu");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sonuc.setTextColor(Color.parseColor(
                                    "#ff1744"));
                            sonuc.setText("Kullanıcı " +
                                    "oluşturulamadı\n" + e.getLocalizedMessage());
                        }
                    });
        }
    }

    public void parolaHatirlat(View view) {
        String eposta = kullaniciMail.getText().toString().trim();
        if (eposta.equals("")) {
            sonuc.setTextColor(Color.parseColor("#ff1744"));
            sonuc.setText("E-posta boş olamaz");
        } else {
            FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(eposta)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            sonuc.setTextColor(Color.parseColor(
                                    "#2e7d32"));
                            sonuc.setText("Hatırlatma e-postası " +
                                    "gönderildi");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sonuc.setTextColor(Color.parseColor(
                                    "#ff1744"));
                            sonuc.setText("Hatırlatma e-postası " +
                                    "gönderilemedi\n" +
                                    e.getLocalizedMessage());
                        }
                    });
        }
    }

}