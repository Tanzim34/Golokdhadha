package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityTeacherListBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {

    ActivityTeacherListBinding binding;
    ListAdapter listAdapter;
    public ArrayList<Listdata> dataArrayList = new ArrayList<>();
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String Suserid = getIntent().getStringExtra("user_uid");

        if (Suserid != null) {
            db = FirebaseFirestore.getInstance();
            DocumentReference studentRef = db.collection("Teacher").document(Suserid);
            CollectionReference teacherCollectionRef = studentRef.collection("StudentID");

            fetchData(teacherCollectionRef);
        }
    }

    private void fetchData(CollectionReference collectionReference) {
        collectionReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> teacherIDs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String teacherID = document.getId();
                            teacherIDs.add(teacherID);

                            // Fetch teacher name
                            DocumentReference userRef = db.collection("users").document(teacherID);
                            userRef.get().addOnSuccessListener(documentSnapshot -> {
                                String name = documentSnapshot.getString("Name");
                                Listdata listData = new Listdata(name, teacherID);
                                dataArrayList.add(listData);

                                // Check if all data is loaded
                                if (dataArrayList.size() == teacherIDs.size()) {
                                    // All data has been fetched, update the UI
                                    updateUI(dataArrayList);
                                }
                            });
                        }
                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });
    }

    private void updateUI(ArrayList<Listdata> dataList) {
        // Update the UI with the fetched data
        listAdapter = new ListAdapter(StudentList.this, dataList);
        // Assuming you have a ListView or RecyclerView to display the data, set the adapter here.
        // Example for a ListView:
        binding.listview.setAdapter(listAdapter);
    }
}
