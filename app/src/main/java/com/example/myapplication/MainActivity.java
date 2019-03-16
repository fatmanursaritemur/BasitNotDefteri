package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.service.autofill.FillEventHistory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseaulth;
    private RecyclerView noteslist;
    private DatabaseReference dbref;
    private GridLayoutManager gridLayoutManager; //Alt alta belirlediğiniz sırayla.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteslist=(RecyclerView)findViewById(R.id.not_listesi);
        gridLayoutManager=new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        noteslist.setHasFixedSize(true);
        //LayoutManager Recyclerview da öğelerin yerleşimini yapmamız için gereklidir,
        noteslist.setLayoutManager(gridLayoutManager);
        noteslist.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        firebaseaulth=FirebaseAuth.getInstance();
        if (firebaseaulth.getCurrentUser() != null) {
            dbref = FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseaulth.getCurrentUser().getUid());
        }
        updateIU();
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void loadData() {
        // degerlere göre sıralanır.
        Query query =dbref.orderByValue();
        FirebaseRecyclerAdapter<NoteModel, NoteHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel,NoteHolder>(

                NoteModel.class,
                R.layout.note_layout,
                NoteHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final NoteHolder viewHolder, NoteModel model, int position) {
                final String noteId = getRef(position).getKey();

                dbref.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    //veri tabanından anlık veri almamızı sağlamaktadır
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();

                            viewHolder.setNoteTitle(title);
                            //viewHolder.setNoteTime(timestamp);

                            GetTimeAgo getTimeAgo = new GetTimeAgo();
                            viewHolder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));

                            viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, NewNotesActivity.class);
                                    intent.putExtra("noteId", noteId);
                                    startActivity(intent);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        // oluşturduğumuz adaptörü listview e atamak için kullanıyoruz.
        noteslist.setAdapter(firebaseRecyclerAdapter);
    }
    private  void updateIU()
    {
        if(firebaseaulth.getCurrentUser()!=null){
            Log.i("MainActivity","fault !=null");
        }
        else
        {
            Intent startintent=new Intent(MainActivity.this,StartActivity.class);
            startActivity(startintent);
            finish();
            Log.i("MainActivity","fault =null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_note_btn:
                Intent newIntentnote = new Intent(MainActivity.this, NewNotesActivity.class);
                startActivity(newIntentnote);
                break;

            case R.id.main_back_login:
                Intent newIntentlogin = new Intent(MainActivity.this, StartActivity.class);
                firebaseaulth.getInstance().signOut();
                startActivity(newIntentlogin);
                break;
        }

        return true;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

