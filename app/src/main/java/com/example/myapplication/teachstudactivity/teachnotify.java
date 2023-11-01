package com.example.myapplication.teachstudactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import com.example.myapplication.Listdata;
import com.example.myapplication.databinding.ActivityAddTeacherBinding;
import com.example.myapplication.databinding.ActivityStdnotifyBinding;
import com.example.myapplication.databinding.ActivityTeacherListBinding;
import com.example.myapplication.itemAdapter;
import com.example.myapplication.teachstudactivity.acceptRequest;
import com.example.myapplication.teachstudactivity.teachcalender;
import com.example.myapplication.teachstudactivity.teachpayment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.myapplication.Listdata;
import com.example.myapplication.DataFetchCallback;

import java.util.ArrayList;

public class teachnotify extends AppCompatActivity {

    ActivityStdnotifyBinding binding;
    itemAdapter listAdapter;
    public ArrayList<Listdata> dataArrayList = new ArrayList<>();
    public FirebaseFirestore db;

    String studentID, teacherID, where, message;
    private Listdata listData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStdnotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        where = getIntent().getStringExtra("where");
        studentID = getIntent().getStringExtra("student_id");
        teacherID = getIntent().getStringExtra("teacher_id");
        db = FirebaseFirestore.getInstance();
        CollectionReference teacherCollectionRef = db.collection("Teacher").document(teacherID).collection("notifications");

        fetchData(teacherCollectionRef, dataList -> {
            // Update the UI with the fetched data
            updateUI(dataList);

            // Set the item click listener for the ListView
            binding.listview.setOnItemClickListener((parent, view, position, id) -> {
                // Handle the item click here
                Listdata selectedData = dataList.get(position);
                String std_id = selectedData.getId();
                //System.out.println(dataArrayList.size());
                int type = selectedData.getType();
                String message = selectedData.getName();
                // Create an Intent to start a new activity (e.g., studentTeacher
                //for requests
                if(type==1) {
                    Intent intent = new Intent(teachnotify.this, acceptRequest.class);
                    intent.putExtra("teacher_id", teacherID);
                    intent.putExtra("student_id", std_id);
                    startActivity(intent);
                }
                // for payment
                if(type==2)
                {
                    Intent intent = new Intent(teachnotify.this, teachpayment.class);
                    intent.putExtra("student_id", studentID);
                    intent.putExtra("teacher_id", teacherID);
                }
                // for calender
                if(type==3){
                    Intent intent = new Intent(teachnotify.this, teachcalender.class);
                    intent.putExtra("student_id",studentID);
                    intent.putExtra("teacher_id", teacherID);
                }
                if(type==-1) Toast.makeText(teachnotify.this,"There is some error", Toast.LENGTH_SHORT).show();
            });
        });
    }


    public void fetchData(CollectionReference collectionReference, DataFetchCallback callback) {
        collectionReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //ArrayList<Listdata> dataArrayList = new ArrayList<>(); // Create a local list

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String ID = document.getId();
                            String id = document.getString("studentID");
                            message = document.getString("message");
                            int type = document.getLong("type").intValue();

                            if (type == 1) {
                                DocumentReference userRef = db.collection("users").document(id);
                                userRef.get().addOnSuccessListener(documentSnapshot -> {
                                    String name = documentSnapshot.getString("Name");
                                    String msg = name + " " + message;
                                    //message = name + " " + message;
                                    // Fetch teacher name

                                    Listdata listData = new Listdata(msg, id, type);
                                    dataArrayList.add(listData);
                                    // System.out.println(dataArrayList.size());
                                    // Check if all data is loaded

                                        // All data has been fetched, invoke the callback
                                        callback.onDataFetched(dataArrayList);


                                });
//                                //System.out.println(dataArrayList.size());
//
//                            });
                            } else {
                                Listdata listData = new Listdata(message, id, type);
                                dataArrayList.add(listData);
                                // System.out.println(dataArrayList.size());
                                // Check if all data is loaded
                                if (dataArrayList.size() == task.getResult().size()) {
                                    // All data has been fetched, invoke the callback
                                    callback.onDataFetched(dataArrayList);
                                }
                            }
                        }
                    }
                    else{
                            System.err.println("Error getting documents: " + task.getException());
                        }

                });
    }

    public void updateUI(ArrayList<Listdata> dataList) {
        // Update the UI with the fetched data
        listAdapter = new itemAdapter(teachnotify.this, dataList);
        binding.listview.setAdapter(listAdapter);
    }
}
