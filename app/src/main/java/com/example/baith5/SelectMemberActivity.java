package com.example.baith5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baith5.database.DatabaseHelper;
import com.example.baith5.models.Group;
import com.example.baith5.models.Member;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectMemberActivity extends AppCompatActivity {
    private Spinner spinnerGroup;
    private ListView listViewAllMembers, listViewGroupMembers;
    private Button btnSave, btnBack;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;
    private ArrayAdapter<Member> allMembersAdapter, groupMembersAdapter;
    private List<Member> allMembers, groupMembers;
    private int selectedGroupId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("ThesisApp", MODE_PRIVATE);
        initViews();
        loadGroups();
        setupListeners();
    }

    private void initViews() {
        spinnerGroup = findViewById(R.id.spinnerGroup);
        listViewAllMembers = findViewById(R.id.listViewAllMembers);
        listViewGroupMembers = findViewById(R.id.listViewGroupMembers);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        allMembers = new ArrayList<>();
        groupMembers = new ArrayList<>();
    }

    private void loadGroups() {
        List<Group> groups = dbHelper.getAllGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);
    }

    private void setupListeners() {
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Group selectedGroup = (Group) parent.getItemAtPosition(position);
                selectedGroupId = selectedGroup.getGroupId();
                loadMembers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Click sinh viên bên trái -> Chuyển sang bên phải
        listViewAllMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member selectedMember = allMembers.get(position);
            
            // Kiểm tra xem sinh viên đã thuộc nhóm khác chưa
            if (isMemberInOtherGroup(selectedMember.getMemId())) {
                Toast.makeText(this, selectedMember.getMemName() + " đã được chọn vào nhóm khác!", 
                    Toast.LENGTH_SHORT).show();
                return;
            }
            
            groupMembers.add(selectedMember);
            allMembers.remove(position);
            updateAdapters();
        });

        // Click sinh viên bên phải -> Chuyển về bên trái
        listViewGroupMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member selectedMember = groupMembers.get(position);
            allMembers.add(selectedMember);
            groupMembers.remove(position);
            updateAdapters();
        });

        btnSave.setOnClickListener(v -> {
            saveGroupMembers();
            Toast.makeText(this, "Đã lưu thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean isMemberInOtherGroup(String memId) {
        List<Group> allGroups = dbHelper.getAllGroups();
        for (Group group : allGroups) {
            if (group.getGroupId() != selectedGroupId) {
                Set<String> otherGroupMembers = prefs.getStringSet("group_" + group.getGroupId() + "_members", new HashSet<>());
                if (otherGroupMembers.contains(memId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void loadMembers() {
        allMembers.clear();
        groupMembers.clear();

        // Load saved member assignments for current group
        Set<String> savedMembers = prefs.getStringSet("group_" + selectedGroupId + "_members", new HashSet<>());

        List<Member> allMembersList = dbHelper.getAllMembers();
        for (Member member : allMembersList) {
            if (savedMembers.contains(member.getMemId())) {
                groupMembers.add(member);
            } else {
                allMembers.add(member);
            }
        }

        updateAdapters();
    }

    private void saveGroupMembers() {
        Set<String> memberIds = new HashSet<>();
        for (Member member : groupMembers) {
            memberIds.add(member.getMemId());
        }
        prefs.edit().putStringSet("group_" + selectedGroupId + "_members", memberIds).apply();
    }

    private void updateAdapters() {
        allMembersAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, allMembers);
        listViewAllMembers.setAdapter(allMembersAdapter);

        groupMembersAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, groupMembers);
        listViewGroupMembers.setAdapter(groupMembersAdapter);
    }
}
