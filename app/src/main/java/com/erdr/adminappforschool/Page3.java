package com.erdr.adminappforschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page3 extends AppCompatActivity {
    String NameOfClass,NameOfSubject;
    RecyclerView recyclerView;
    List<ListLectureList> listPDFData;
    AdapterLecture2 adapterPDFData;
    ArrayList<String> ThisIsJustList;
    int count =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listPDFData = new ArrayList<>();
        ThisIsJustList = new ArrayList<>();
        NameOfClass = getIntent().getStringExtra("NameOfClass");
        NameOfSubject = getIntent().getStringExtra("NameOfSubject");
        GettingFireBaseData();
    }

    private void GettingFireBaseData() {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db
                .collection(getString(R.string.schoolNamelectureFileAddress)+"/VIDEODATA/"+NameOfClass+"/SubjectName/"+NameOfSubject)
                .document("ChapterName");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                count = 1;
                listPDFData.clear();
                ThisIsJustList.clear();
                while(documentSnapshot.get("Chapter"+String.valueOf(count)) != null){
                    listPDFData.add(new ListLectureList(documentSnapshot.get("Chapter"+String.valueOf(count)).toString()));
                    ThisIsJustList.add(documentSnapshot.get("Chapter"+String.valueOf(count)).toString());
                    count++;
                }
                CallTheAdapter();
            }
        });
    }

    private void CallTheAdapter() {
        adapterPDFData = new AdapterLecture2(this,listPDFData);
        recyclerView.setAdapter(adapterPDFData);
    }

    public void AddNewClass(View view) {
        final EditText editText = findViewById(R.id.edit);
        if(editText.getText().toString().length()<1){
            Toast.makeText(getApplicationContext(),"This Cant be empty",Toast.LENGTH_LONG).show();
        }
        else{


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("There are limited times you can change class. Do you want to do it");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TryToAddNewSubject(editText.getText().toString());
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }

    private void TryToAddNewSubject(String s) {

        Map<String, Object> city = new HashMap<>();
        city.put("Chapter"+String.valueOf(count), s);


        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection(getString(R.string.schoolNamelectureFileAddress)+"/VIDEODATA/"+NameOfClass+"/SubjectName/"+NameOfSubject).document("ChapterName")
                .set(city, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                        GettingFireBaseData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Not Done",Toast.LENGTH_LONG).show();

                    }
                });

    }

    public void userItemClick(int adapterPosition) {
        Intent intent = new Intent(this, Page4.class);
        intent.putExtra("NameOfClass",NameOfClass);
        intent.putExtra("NameOfSubject",NameOfSubject);
        intent.putExtra("NameOfChapter",listPDFData.get(adapterPosition).getDataToShow());
        startActivity(intent);
    }
}
