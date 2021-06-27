package com.example.ispass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.NoteDao;
import com.example.ispass.models.entities.Note;
import com.example.ispass.ui.adapters.NotesAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private ImageView buttonExit, buttonHome;
    private Button addNotes;
    private List<Note> notesList;
    private NoteDao noteDao;
    private NotesAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        userId = (Integer) getIntent().getSerializableExtra("user_id");
        addNotes = findViewById(R.id.btn_add_notes);
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, AddNoteActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
            }
        });

        buttonHome = findViewById(R.id.toolbar_notes_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, HomeActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        buttonExit = findViewById(R.id.toolbar_note_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        noteDao = Room.databaseBuilder(this, IspDb.class, "notes_table").allowMainThreadQueries().build().noteDao();
        coordinatorLayout = findViewById(R.id.layout_note_main);
        recyclerView = findViewById(R.id.recyclerNotes_view);
        notesList = new ArrayList<>();

        fetchAllNotesFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(this,NotesActivity.this, notesList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }

    void fetchAllNotesFromDatabase () {
        List<Note> list = noteDao.getUserNotes(userId);
        if (list.size() == 0) {
            Toast.makeText(this, "No notes saved", Toast.LENGTH_SHORT).show();
        }
        else {
            notesList = list;
        }
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Note item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(item, position);
                    recyclerView.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (!(event == DISMISS_EVENT_ACTION)) {
                        noteDao.delete(item);
                    }
                }

            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotesActivity.this, HomeActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}