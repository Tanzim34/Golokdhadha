package com.example.myapplication.studteachactivity;

import static com.example.myapplication.studteachactivity.AddNewTask.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.addTeacher;
import com.example.myapplication.sendRequest;
import com.example.myapplication.studentTeacher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.studteachactivity.ToDoAdapter;
import com.example.myapplication.studteachactivity.ToDoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class stdtask extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private FloatingActionButton mFab;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;

    public static String student_id, teacher_id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdtask);

        student_id = getIntent().getStringExtra("student_id");
        teacher_id = getIntent().getStringExtra("teacher_id");
        recyclerView = findViewById(R.id.recyclerView);
        mFab = findViewById(R.id.addButton);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(stdtask.this));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager() , TAG);
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("Student").document(student_id).collection("notifications").document();

                Map<String, Object> request = new HashMap<>();
                request.put("teacherID", teacher_id);
                request.put("message"," added a new Task");
                request.put("type", 4);

                db.collection("student").document(student_id).collection("notifications")
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                // Add your success message here
                                Toast.makeText(stdtask.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(stdtask.this, addTeacher.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                // Add your failure message here
                               // Toast.makeText(stdtask.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(stdtask.this , mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);
    }
    private void showData(){
        DocumentReference studentRef = firestore.collection("Student").document(student_id); // Use doc() to get a document
        CollectionReference teacherCollectionRef = studentRef.collection("TeacherID"); // Access the subcollection
        DocumentReference finalRef = teacherCollectionRef.document(teacher_id); // Use doc() to get a document within the subcollection
        query = finalRef.collection("task").orderBy("time" , Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Handle the error
                    Log.e(TAG, "Error getting documents: ", error);
                    return;
                }
                else {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String id = documentChange.getDocument().getId();
                            ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                            mList.add(toDoModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}