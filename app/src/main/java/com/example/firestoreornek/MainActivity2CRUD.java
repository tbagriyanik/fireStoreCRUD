package com.example.firestoreornek;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2CRUD extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView durum;
    ListView veriler;
    ArrayList<String> veriAnahtarlari;
    ArrayAdapter<String> anahtarAdapter;
    String kayitSeciliAnahtar = ""; //seçili kaydın anahtar bilgisi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_crud);

        durum = findViewById(R.id.textViewDurum);
        veriler = findViewById(R.id.listViewData);
        //listeden seçim yapmak
        veriler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                kayitSeciliAnahtar = adapterView.getItemAtPosition(position).toString();
                readData(kayitSeciliAnahtar);
            }
        });

        veriAnahtarlariGetir();

    }

    //liste kutusunu güncelle
    private void veriAnahtarlariGetir() {
        veriAnahtarlari = new ArrayList<>();

        db.collection("my_collection")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            veriAnahtarlari.add(document.getId());
                        }
                        anahtarAdapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, veriAnahtarlari);
                        veriler.setAdapter(anahtarAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        durum.setTextColor(Color.parseColor("#ff1744"));
                        durum.setText("Listeleme hatası\n" + e);
                    }
                });

    }

    public void kaydet(View view) {
        EditText baslik, tanim;
        baslik = findViewById(R.id.editTextBaslik);
        tanim = findViewById(R.id.editTextTanim);
        if (!baslik.getText().toString().isEmpty() && !tanim.getText().toString().isEmpty())
            addData(baslik.getText().toString(), tanim.getText().toString());
        else {
            durum.setTextColor(Color.parseColor("#ff1744"));
            durum.setText("Boş giriş var!");
        }
    }

    public void sil(View view) {
        if (!kayitSeciliAnahtar.isEmpty())
            deleteData(kayitSeciliAnahtar);
        else {
            durum.setTextColor(Color.parseColor("#ff1744"));
            durum.setText("Kayıt seçiniz");
        }
    }

    public void guncelle(View view) {
        if (!kayitSeciliAnahtar.isEmpty()) {
            EditText baslik, tanim;
            baslik = findViewById(R.id.editTextBaslik);
            tanim = findViewById(R.id.editTextTanim);
            updateData(kayitSeciliAnahtar, baslik.getText().toString(), tanim.getText().toString());
        } else {
            durum.setTextColor(Color.parseColor("#ff1744"));
            durum.setText("Kayıt seçiniz");
        }
    }

    public void addData(String title, String description) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);

        db.collection("my_collection")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        durum.setTextColor(Color.parseColor("#2e7d32"));
                        durum.setText("Veri eklendi, ID: " + documentReference.getId());
                        veriAnahtarlariGetir();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        durum.setTextColor(Color.parseColor("#ff1744"));
                        durum.setText("Ekleme hatası\n" + e);
                    }
                });
    }

    public void readData(String id) {
        DocumentReference docRef = db.collection("my_collection").document(id);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            EditText baslik, tanim;
                            baslik = findViewById(R.id.editTextBaslik);
                            tanim = findViewById(R.id.editTextTanim);
                            baslik.setText(documentSnapshot.getString("title"));
                            tanim.setText(documentSnapshot.getString("description"));

                            durum.setTextColor(Color.parseColor("#2e7d32"));
                            durum.setText("Kayıt getirildi");
                        } else {
                            durum.setTextColor(Color.parseColor("#ff1744"));
                            durum.setText("Kayıt bulunamadı");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        durum.setTextColor(Color.parseColor("#ff1744"));
                        durum.setText("Kayıt hatası\n" + e);
                    }
                });

    }

    public void updateData(String id, String title, String description) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);

        db.collection("my_collection")
                .document(id)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        durum.setTextColor(Color.parseColor("#2e7d32"));
                        durum.setText("Veri güncellendi");
                        veriAnahtarlariGetir();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        durum.setTextColor(Color.parseColor("#ff1744"));
                        durum.setText("Güncelleme hatası\n" + e);
                    }
                });
    }

    public void deleteData(String id) {
        db.collection("my_collection")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        durum.setTextColor(Color.parseColor("#2e7d32"));
                        durum.setText("Veri silindi");
                        EditText baslik, tanim;
                        baslik = findViewById(R.id.editTextBaslik);
                        tanim = findViewById(R.id.editTextTanim);
                        baslik.setText("");
                        tanim.setText("");
                        kayitSeciliAnahtar = "";
                        veriAnahtarlariGetir();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        durum.setTextColor(Color.parseColor("#ff1744"));
                        durum.setText("Silme hatası\n" + e);
                    }
                });
    }

}