package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class chatwindo extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionReference chatsCollection;
    DocumentReference chatDocument;
    EditText msg;
    Button send;
    ListView messageListView;
    ArrayAdapter<String> adapter;
    String senderUID;
    String receiverUID;
    public String sendername;
    public String receivername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);

        firebaseAuth = FirebaseAuth.getInstance();

        msg = findViewById(R.id.messageEditText);
        send = findViewById(R.id.sendButton);
        messageListView = findViewById(R.id.messageListView);

        senderUID = getIntent().getStringExtra("senderID");
        receiverUID = getIntent().getStringExtra("receiverID");
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(senderUID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sendername = documentSnapshot.getString("Name");



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(chatwindo.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
        documentReference = firestore.collection("users").document(senderUID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                receivername = documentSnapshot.getString("Name");



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(chatwindo.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });


        DocumentReference studentRef = firestore.collection("Student").document(senderUID);
        CollectionReference teacherCollectionRef = studentRef.collection("TeacherID");
        DocumentReference finalRef = teacherCollectionRef.document(receiverUID);

        chatDocument = finalRef.collection("chats")
                .document("chat_" + senderUID + "_" + receiverUID);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        messageListView.setAdapter(adapter);

        // Listen for incoming messages
        chatDocument.collection("messages")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        showToast("Error listening for messages: " + e.getMessage());
                        return;
                    }
                    adapter.clear(); // Clear previous messages

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String text = document.getString("text");
                        String sender = document.getString("senderUID");


                        adapter.add(sendername + ": " + text);
                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = msg.getText().toString();
                if (!messageText.isEmpty()) {
                    Date date = new Date();
                    Map<String, Object> messageData = new HashMap<>();
                    messageData.put("text", messageText);
                    messageData.put("senderUID", senderUID);
                    messageData.put("receiverUID", receiverUID);
                    messageData.put("timestamp", new Date());

                    chatDocument.collection("messages").add(messageData)
                            .addOnSuccessListener(documentReference -> {
                                msg.setText("");
                            })
                            .addOnFailureListener(e -> {
                                showToast("Failed to send message: " + e.getMessage());
                            });
                } else {
                    showToast("Enter a message first");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
