package com.example.baith5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baith5.database.DatabaseHelper;
import com.example.baith5.models.Group;

import java.util.List;

public class SelectGroupActivity extends AppCompatActivity {
    private Spinner spinnerGroup;
    private Button btnSelect;
    private TextView tvMessage;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("ThesisApp", MODE_PRIVATE);

        initViews();
        loadGroups();
        setupListeners();
    }

    private void initViews() {
        spinnerGroup = findViewById(R.id.spinnerGroup);
        btnSelect = findViewById(R.id.btnSelect);
        tvMessage = findViewById(R.id.tvMessage);
    }

    private void loadGroups() {
        List<Group> groups = dbHelper.getAllGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSelect.setOnClickListener(v -> {
            Group selectedGroup = (Group) spinnerGroup.getSelectedItem();
            if (selectedGroup != null) {
                prefs.edit()
                        .putInt("selectedGroupId", selectedGroup.getGroupId())
                        .putString("selectedGroupName", selectedGroup.getGroupName())
                        .apply();

                tvMessage.setText(selectedGroup.getGroupName() + " đã được chọn.");
                tvMessage.setVisibility(View.VISIBLE);

                finish();
            }
        });
    }
}
