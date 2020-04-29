package com.example.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.NoteListener {

    //<a target="_blank" href="https://icons8.com/icons/set/note">Note icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>

    RecyclerView recyclerView;
    NotesRecyclerAdapter notesRecyclerAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {
        final EditText noteEditText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(noteEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + noteEditText.getText());
                        addNote(noteEditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addNote(String text) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Note note = new Note(text, false, new Timestamp(new Date()), userId);

        FirebaseFirestore.getInstance()
                .collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Successfully added the note...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void StartOperationActivity(){
        Intent intent  = new Intent(this, OperationActivity.class);
        startActivity(intent);
    }

    private void StartLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                Profile();
                return true;
                
            case R.id.action_logout:
                Toast.makeText(this, "See you Soon", Toast.LENGTH_SHORT).show();
                Logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Profile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void Logout() {

        //FirebaseAuth.getInstance().signOut();
        AuthUI.getInstance().signOut(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if (notesRecyclerAdapter != null){
            notesRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
            StartLoginActivity();
            finish();
        }

        initRecyclerView(firebaseAuth.getCurrentUser());
        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(TAG, "onSuccess" + getTokenResult.getToken());
                    }
                });

        Log.d(TAG, "onAuthStateChange: "+ firebaseAuth.getCurrentUser().getPhoneNumber());
    }

    private void initRecyclerView(FirebaseUser user) {

        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userID", user.getUid())
                .orderBy("completed", Query.Direction.ASCENDING)
                .orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options, this);
        recyclerView.setAdapter(notesRecyclerAdapter);


        // real time database
        notesRecyclerAdapter.startListening();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void handelCheckChanged(boolean isChecked, DocumentSnapshot snapshot) {
        Log.d(TAG, "handelCheckChanged: " + isChecked);
        snapshot.getReference().update("completed", isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    @Override
    public void handelEditNote(final DocumentSnapshot snapshot) {

        final Note note = snapshot.toObject(Note.class);

        final EditText editText = new EditText(this);
        editText.setText(note.getText().toString());
        editText.setSelection(note.getText().length());


        new AlertDialog.Builder(this)
                .setTitle("Edit Text")
                .setView(editText)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = editText.getText().toString();
                        note.setText(newText);
                        snapshot.getReference().set(note)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: updated Successfully");
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void handelDeleteItem(DocumentSnapshot snapshot) {

        final DocumentReference docRef = snapshot.getReference();
        final Note note = snapshot.toObject(Note.class);

        snapshot.getReference().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Item Deleted");
                    }
                });

        Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        docRef.set(note);
                    }
                })
                .show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                Toast.makeText(MainActivity.this, "Deleting...", Toast.LENGTH_SHORT).show();
                NotesRecyclerAdapter.NoteViewHolder noteViewHolder = (NotesRecyclerAdapter.NoteViewHolder) viewHolder;
                noteViewHolder.deleteItem();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_delete_white_24dp)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
