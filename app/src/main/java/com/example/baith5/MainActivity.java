package com.example.baith5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baith5.database.DatabaseHelper;
import com.example.baith5.models.Member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private TextView tvSelectedGroup, tvGroupMembers, tvResult;
    private Button btnSelectMember, btnSelectGroup, btnSelectThesis, btnExit;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("ThesisApp", MODE_PRIVATE);

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDisplay();
    }

    private void initViews() {
        tvSelectedGroup = findViewById(R.id.tvSelectedGroup);
        tvGroupMembers = findViewById(R.id.tvGroupMembers);
        tvResult = findViewById(R.id.tvResult);
        btnSelectMember = findViewById(R.id.btnSelectMember);
        btnSelectGroup = findViewById(R.id.btnSelectGroup);
        btnSelectThesis = findViewById(R.id.btnSelectThesis);
        btnExit = findViewById(R.id.btnExit);
    }

    private void setupListeners() {
        btnSelectMember.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectMemberActivity.class);
            startActivity(intent);
        });

        btnSelectGroup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectGroupActivity.class);
            startActivity(intent);
        });

        btnSelectThesis.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectThesisActivity.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> finish());
    }

    private void updateDisplay() {
        int selectedGroupId = prefs.getInt("selectedGroupId", -1);
        String selectedGroupName = prefs.getString("selectedGroupName", "");
        String selectedThesisName = prefs.getString("selectedThesisName", "");

        if (selectedGroupId != -1) {
            tvSelectedGroup.setText("Nhóm được chọn: " + selectedGroupName);
            
            // Load members from saved data in SharedPreferences
            Set<String> savedMembers = prefs.getStringSet("group_" + selectedGroupId + "_members", new HashSet<>());
            
            if (!savedMembers.isEmpty()) {
                List<Member> allMembers = dbHelper.getAllMembers();
                StringBuilder memberList = new StringBuilder("Danh sách thành viên nhóm:\n");
                for (Member member : allMembers) {
                    if (savedMembers.contains(member.getMemId())) {
                        memberList.append("- ").append(member.getMemName()).append("\n");
                    }
                }
                tvGroupMembers.setText(memberList.toString());
            } else {
                tvGroupMembers.setText("Danh sách thành viên nhóm:\nChưa có thành viên");
            }
        } else {
            tvSelectedGroup.setText("Nhóm được chọn: Chưa chọn");
            tvGroupMembers.setText("Danh sách thành viên nhóm:\nChưa có thành viên");
        }

        if (!selectedGroupName.isEmpty() && !selectedThesisName.isEmpty()) {
            tvResult.setText(selectedGroupName + " - " + selectedThesisName);
        } else {
            tvResult.setText("Chưa có kết quả đăng ký");
        }
    }
}
