package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityTeacherListBinding;
import com.example.myapplication.ListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class TeacherList extends AppCompatActivity {


    //MyCallback callback = new MyCallback() {
    public interface DataCallback {
        void onDataLoaded(ArrayList<String> teacherIDs, ArrayList<String> names);
    }

    public void onCallback(String data) {
            teacherIDlist.add(data);
        }
        public void onReturnback(String data){
            name.add(data);
        }


    ActivityTeacherListBinding binding;
    ListAdapter listAdapter;
    public ArrayList<Listdata> dataArrayList = new ArrayList<>();
    public ArrayList<String> teacherIDlist = new ArrayList<>();
    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<Integer> imagelist = new ArrayList<Integer>();
    public Listdata listData;
    public FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String Suserid = getIntent().getStringExtra("user_uid");
        // ...

        if (Suserid != null) {
            db = FirebaseFirestore.getInstance();
            DocumentReference studentRef = db.collection("Student").document(Suserid);
            CollectionReference teacherCollectionRef = studentRef.collection("TeacherID");
            fetchData(teacherCollectionRef, new DataCallback() {
                @Override
                public void onDataLoaded(ArrayList<String> teacherIDs, ArrayList<String> names) {
                    // You can access teacherIDs and names here after they have been populated
                    for (int i = 0; i < names.size(); i++) {
                        listData = new Listdata(names.get(i), teacherIDs.get(i));
                        dataArrayList.add(listData);
                    }
                    System.out.println(dataArrayList.size());
                    // Update the UI with the data
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    private void fetchData(CollectionReference collectionReference, DataCallback callback) {
        ArrayList<String> teacherIDs = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        collectionReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String s = document.getId();
                            teacherIDs.add(s);
                            DocumentReference userRef = db.collection("users").document(s);
                            userRef.get().addOnSuccessListener(documentSnapshot -> {
                                String n = documentSnapshot.getString("Name");
                                names.add(n);
                                    // All data has been fetched, notify the callback
                                    callback.onDataLoaded(teacherIDs, names);

                            });
                        }
                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });
    }

}