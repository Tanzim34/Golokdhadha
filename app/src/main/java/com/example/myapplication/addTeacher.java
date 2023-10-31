package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityAddTeacherBinding;
import com.example.myapplication.databinding.ActivityTeacherListBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class addTeacher extends AppCompatActivity {

    ActivityAddTeacherBinding binding;
    ListAdapter listAdapter;
    public ArrayList<Listdata> dataArrayList = new ArrayList<>();
    public FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       String studentID = getIntent().getStringExtra("student_id");
            db = FirebaseFirestore.getInstance();
            CollectionReference teacherCollectionRef = db.collection("Teacher");

            fetchData(teacherCollectionRef, dataList -> {
                // Update the UI with the fetched data
                updateUI(dataList);

                // Set the item click listener for the ListView
                binding.listview.setOnItemClickListener((parent, view, position, id) -> {
                    // Handle the item click here
                    Listdata selectedData = dataList.get(position);
                    System.out.println(dataArrayList.size());
                    // Create an Intent to start a new activity (e.g., studentTeacher
                    Intent intent = new Intent(addTeacher.this, sendRequest.class);
                    intent.putExtra("user_id", selectedData.getId());
                    intent.putExtra("student_id", studentID);
                    startActivity(intent);
                });
            });
        }
    

    public void fetchData(CollectionReference collectionReference, DataFetchCallback callback) {
        collectionReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //ArrayList<Listdata> dataArrayList = new ArrayList<>(); // Create a local list

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String teacherID = document.getId();

                            // Fetch teacher name
                            DocumentReference userRef = db.collection("users").document(teacherID);
                            userRef.get().addOnSuccessListener(documentSnapshot -> {
                                String name = documentSnapshot.getString("Name");
                                Listdata listData = new Listdata(name, teacherID);
                                dataArrayList.add(listData);
                                // System.out.println(dataArrayList.size());
                                // Check if all data is loaded
                                if (dataArrayList.size() == task.getResult().size()) {
                                    // All data has been fetched, invoke the callback
                                    callback.onDataFetched(dataArrayList);
                                }
                                //System.out.println(dataArrayList.size());

                            });
                        }
                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });
    }

    public void updateUI(ArrayList<Listdata> dataList) {
        // Update the UI with the fetched data
        listAdapter = new ListAdapter(addTeacher.this, dataList);
        binding.listview.setAdapter(listAdapter);
    }
}
