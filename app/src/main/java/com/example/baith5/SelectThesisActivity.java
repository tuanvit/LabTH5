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
import com.example.baith5.models.Thesis;

import java.util.List;

public class SelectThesisActivity extends AppCompatActivity {
    private Spinner spinnerThesis;
    private Button btnSelect;
    private TextView tvMessage;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_thesis);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("ThesisApp", MODE_PRIVATE);

        initViews();
        loadThesis();
        setupListeners();
    }

    private void initViews() {
        spinnerThesis = findViewById(R.id.spinnerThesis);
        btnSelect = findViewById(R.id.btnSelect);
        tvMessage = findViewById(R.id.tvMessage);
    }

    private void loadThesis() {
        List<Thesis> thesisList = dbHelper.getAllThesis();
        ArrayAdapter<Thesis> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, thesisList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThesis.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSelect.setOnClickListener(v -> {
            Thesis selectedThesis = (Thesis) spinnerThesis.getSelectedItem();
            if (selectedThesis != null) {
                int groupId = prefs.getInt("selectedGroupId", -1);
                
                if (groupId != -1) {
                    dbHelper.saveRegistration(groupId, selectedThesis.getThesisId());
                }

                prefs.edit()
                        .putInt("selectedThesisId", selectedThesis.getThesisId())
                        .putString("selectedThesisName", selectedThesis.getThesisName())
                        .apply();

                tvMessage.setText("Đề tài đã được chọn.");
                tvMessage.setVisibility(View.VISIBLE);

                finish();
            }
        });
    }
}
