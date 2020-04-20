package com.erdr.adminappforschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Page4 extends AppCompatActivity {
    String NameOfClass,NameOfSubject,NameOfChapter;
    int Asscount =0;
    int Notecount =0;
    int Videocount =0;
    private Uri mNotesURI,mAssignURI;
    private ProgressBar mProgressBar;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);
        CallTheVisibility(false);
        NameOfClass = getIntent().getStringExtra("NameOfClass");
        NameOfSubject = getIntent().getStringExtra("NameOfSubject");
        NameOfChapter = getIntent().getStringExtra("NameOfChapter");
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        GettingFireBaseData();
    }

    private void GettingFireBaseData() {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db
                .collection("RLBvideodata/VIDEODATA/"+NameOfClass+"/SubjectName/"+NameOfSubject+"/ChapterName/"+NameOfChapter)
                .document("Lecture");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Asscount = 0;
                Videocount = 0;
                Notecount = 0;
                while(documentSnapshot.get("AssignmentINFO"+String.valueOf(Asscount+1)) != null){
                    Asscount++;
                }
                while(documentSnapshot.get("VideoURL"+String.valueOf(Videocount+1)) != null){
                    Videocount++;
                }
                while(documentSnapshot.get("NotesINFO"+String.valueOf(Notecount+1)) != null){
                    Notecount++;
                }
                CallTheVisibility(true);
            }
        });
    }

    private void CallTheVisibility(boolean b) {
        if(b){
            LinearLayout rootLL = findViewById(R.id.rootLL);
            rootLL.setVisibility(View.VISIBLE);
        }
        else {
            LinearLayout rootLL = findViewById(R.id.rootLL);
            rootLL.setVisibility(View.VISIBLE);
        }
    }



    public void AddNewClass(View view) {
        final EditText editText = findViewById(R.id.edit);
        if(editText.getText().toString().length()<1){
            Toast.makeText(getApplicationContext(),"This Cant be empty",Toast.LENGTH_LONG).show();
        }
        else{



        }
    }


    public void AddYouVideo(View view) {
        final EditText editText = findViewById(R.id.ed1);
        final EditText editText1 = findViewById(R.id.ed11);
        if(editText.getText().toString().length()<1 || editText1.getText().toString().length()<1 ){
            Toast.makeText(getApplicationContext(),"This cannot go through",Toast.LENGTH_LONG).show();
        }
        else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Sure To add Video");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ADDthisYoutubeVIdeo(editText.getText().toString(),editText1.getText().toString());
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

    private void ADDthisYoutubeVIdeo(String s, String toString) {
        Map<String, Object> city = new HashMap<>();
        city.put("ChapterName", NameOfChapter);
        city.put("VideoURL"+ String.valueOf(Videocount+1),s);
        city.put("VideoTEXT"+ String.valueOf(Videocount+1), toString);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("RLBvideodata/VIDEODATA/"+NameOfClass+"/SubjectName/"+NameOfSubject+"/ChapterName/"+NameOfChapter)
                .document("Lecture")
                .set(city, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                        Videocount++;
                        CallTheVisibility(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Not Done",Toast.LENGTH_LONG).show();

                    }
                });

    }

    public void AddAssign(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2002);
    }

    public void AddNotes(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2001 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mNotesURI = data.getData();
            Button UPLOADNOTES = findViewById(R.id.UPLOADNOTES);
            UPLOADNOTES.setEnabled(true);

        }
        if (requestCode == 2002 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mAssignURI = data.getData();
            Button UPLOADASS = findViewById(R.id.UPLOADASS);
            UPLOADASS.setEnabled(true);
        }
    }

    public void UploadNotes(View view) {
        uploadFile(mNotesURI,"NotesINFO"+String.valueOf(Notecount+1),"NOTE");
    }

    public void UploadAssign(View view) {
        uploadFile(mAssignURI,"AssignmentINFO"+String.valueOf(Asscount+1),"ASS");

    }


    private void uploadFile(Uri mImageUri, final String s, final String fileType) {
        CallTheVisibility(false);
        if (mImageUri != null) {
            final String path=mImageUri.getLastPathSegment();

            StorageReference fileReference = mStorageRef.child(path);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                    StoreThisInfoInDataBase(path,s,fileType);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void StoreThisInfoInDataBase(String path, String s, final String fileType) {
        if(!fileType.equals("NOTE") && !fileType.equals("ASS")){
            Toast.makeText(getApplicationContext(),"Some error is there",Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> city = new HashMap<>();
        city.put("ChapterName", NameOfChapter);
        city.put(s,path);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("RLBvideodata/VIDEODATA/"+NameOfClass+"/SubjectName/"+NameOfSubject+"/ChapterName/"+NameOfChapter)
                .document("Lecture")
                .set(city, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                        if(fileType.equals("NOTE")){
                            Notecount++;
                        }
                        else {
                            Asscount++;
                        }
                        CallTheVisibility(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Not Done",Toast.LENGTH_LONG).show();

                    }
                });

    }

}
