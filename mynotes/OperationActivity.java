package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationActivity extends AppCompatActivity {
    private static final String TAG = "OperationActivity";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
    }

    public void createDocument(View view) {
        Toast.makeText(this, "Create Document", Toast.LENGTH_SHORT).show();

//        Map<String, Object> map = new HashMap<>();
//        map.put("text", "i wanna learn Android Application Development");
//        map.put("isCompleted", false);
//        map.put("created", new Timestamp(new Date()));

        Product product = new Product("iPhone 11", 699, true);

        firestore.collection("products")
//                .add(map)
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Task Was Successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Task Was NOT Successful");
                    }
                });
    }

    public void readDocument(View view) {
        Toast.makeText(this, "Read Document", Toast.LENGTH_SHORT).show();


//          gat all the data from this code
//        FirebaseFirestore.getInstance()
//                .collection("notes")
//                //.where --> here we list out our query start with where
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Log.d(TAG, "onSuccess: we're getting the data");
//                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
//                        for(DocumentSnapshot snapshot: snapshotList){
//                            Log.d(TAG, "onSuccess: "+snapshot.getData());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ", e);
//                    }
//                });

        FirebaseFirestore.getInstance()
                .collection("products")
                .document("pLxHZHJHHKDoK6GXl5Lz")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Log.d(TAG, "onSuccess: "+documentSnapshot.getData());
//                        Log.d(TAG, "onSuccess: "+documentSnapshot.getId());
//                        Log.d(TAG, "onSuccess: "+documentSnapshot.getString("text"));
//                        Log.d(TAG, "onSuccess: "+documentSnapshot.getTimestamp("cretaed"));
//                        Log.d(TAG, "onSuccess: "+documentSnapshot.getBoolean("isCompleted"));

                        Product pro = documentSnapshot.toObject(Product.class);
                        Log.d(TAG, "onSuccess: " + pro.toString());
                        Log.d(TAG, "onSuccess: "+ pro.getText());
                        Log.d(TAG, "onSuccess: " + pro.getIsAvailable());
                        Log.d(TAG, "onSuccess: "+ pro.getPrice());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });


    }

    public void updateDocument(View view) {
        Toast.makeText(this, "update Document", Toast.LENGTH_SHORT).show();


        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("products")
                .document("IQZwlL5wqjvPLWBHQnPb");
        Map<String, Object> map = new HashMap<>();
        map.put("text", "Redmi note 9 pro");
        map.put("price", 13999);
        map.put("brand", "Redmi");


//        docRef.update(map)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: yay, updated successfully");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ", e);
//                    }
//                });

//        docRef.set(object set) --> only update given data and remaining wll n=be deleted
//        docRef.set(object set, setOption merge) --> merge all the given data and does not dlete any data from database
        docRef.set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: yay, updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void deleteDocument(View view) {
        Toast.makeText(this, "Delete Document", Toast.LENGTH_SHORT).show();

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .document("IQZwlL5wqjvPLWBHQnPb")
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: we have deleted that data");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ", e);
//                    }
//                });

        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("brand", "kbc")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot : snapshotList){
                            batch.delete(snapshot.getReference());
                        }

                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Deleted all doc with brand name  = kbc");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });


    }

    public void getAllDocument(View view) {
        Toast.makeText(this, "Get All Document", Toast.LENGTH_SHORT).show();


//      Sorting in asc order or desc order
//        FirebaseFirestore.getInstance()
//                .collection("products")
////                .orderBy("price")
//                .orderBy("price")
//                .limit(3)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
//                        for(DocumentSnapshot snapshot:snapshotList){
//                            Log.d(TAG, "onSuccess: "+snapshot.getData());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ",e );
//                    }
//                });
        FirebaseFirestore.getInstance()
                .collection("products")
                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            Product product = (Product) task.getResult().toObjects(Product.class);
//                            Log.d(TAG, "onComplete: " + product);
//                        } else {
//                            Log.e(TAG, "onComplete: ", task.getException());
//                        }
//                    }
//                });
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: We,re getting data");

                        List<Product> productList = queryDocumentSnapshots.toObjects(Product.class);
                        Log.d(TAG, "onSuccess: " + productList.toString());



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void getAllDocumentWithRealTimeupdates(View view) {
        Toast.makeText(this, "Get All Document With Real Time Updates", Toast.LENGTH_SHORT).show();

//        FirebaseFirestore.getInstance()
//                .collection("products")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null){
//                            Log.e(TAG, "onEvent: ", e);
//                            return;
//                        }
//                        if (queryDocumentSnapshots != null) {
//                            Log.d(TAG, "onEvent: ------------------------");
////                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
////                            for (DocumentSnapshot snapshot:snapshotList){
////                                Log.d(TAG, "onEvent: " + snapshot.getData());
////                            }
//                            List<DocumentChange> snapshotChange = queryDocumentSnapshots.getDocumentChanges();
//                            for (DocumentChange snapshot: snapshotChange) {
//                                Log.d(TAG, "onEvent: " + snapshot.getDocument().getData());
//                            }
//
//                        } else {
//                            Log.e(TAG, "onEvent: query snapshot was null");
//                        }
//
//
//                    }
//                });

        FirebaseFirestore.getInstance()
                .collection("products")
                .document("IQZwlL5wqjvPLWBHQnPb")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.e(TAG, "onEvent: ", e);
                            return;
                        }
                        if (documentSnapshot != null){
                            Log.d(TAG, "onEvent: -----------");
                            Log.d(TAG, "onEvent: " + documentSnapshot.getData());
                        } else {
                            Log.e(TAG, "onEvent: null");
                        }
                    }
                });
    }
}
