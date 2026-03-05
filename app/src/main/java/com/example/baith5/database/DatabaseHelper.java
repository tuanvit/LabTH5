package com.example.baith5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baith5.models.Group;
import com.example.baith5.models.Member;
import com.example.baith5.models.Thesis;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ThesisRegistration.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_GROUP = "Groups";
    private static final String TABLE_MEMBER = "Members";
    private static final String TABLE_THESIS = "Thesis";
    private static final String TABLE_REGISTRATION = "Registration";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Groups table
        db.execSQL("CREATE TABLE " + TABLE_GROUP + " (" +
                "GroupID INTEGER PRIMARY KEY, " +
                "GroupName TEXT)");

        // Create Members table
        db.execSQL("CREATE TABLE " + TABLE_MEMBER + " (" +
                "MemID TEXT PRIMARY KEY, " +
                "MemName TEXT, " +
                "GroupID INTEGER, " +
                "Role TEXT)");

        // Create Thesis table
        db.execSQL("CREATE TABLE " + TABLE_THESIS + " (" +
                "ThesisID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ThesisName TEXT)");

        // Create Registration table
        db.execSQL("CREATE TABLE " + TABLE_REGISTRATION + " (" +
                "GroupID INTEGER PRIMARY KEY, " +
                "ThesisID INTEGER)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insert Groups
        for (int i = 1; i <= 6; i++) {
            ContentValues values = new ContentValues();
            values.put("GroupID", i);
            values.put("GroupName", "Nhóm " + i);
            db.insert(TABLE_GROUP, null, values);
        }

        // Insert Members and Thesis based on provided data
        insertGroup1Data(db);
        insertGroup2Data(db);
        insertGroup3Data(db);
        insertGroup4Data(db);
        insertGroup5Data(db);
    }

    private void insertGroup1Data(SQLiteDatabase db) {
        insertMember(db, "2248001", "Đặng Đình Trung", 1, "Trưởng nhóm");
        insertMember(db, "2248002", "Vương Văn Mẫn", 1, "Thành viên");
        insertMember(db, "2248003", "Nguyễn Hữu Duy", 1, "Thành viên");
        insertThesis(db, "Xây dựng ứng dụng bán đồ ăn online TDMU FOOD trên nền tảng Android");
    }

    private void insertGroup2Data(SQLiteDatabase db) {
        insertMember(db, "2248004", "Nguyễn Hữu Đức", 2, "Trưởng nhóm");
        insertMember(db, "2248005", "Lý Phúc Hòa", 2, "Thành viên");
        insertMember(db, "2248006", "Trịnh Lê Đình Hồ", 2, "Thành viên");
        insertThesis(db, "Xây dựng ứng dụng News App trên Android");
    }

    private void insertGroup3Data(SQLiteDatabase db) {
        insertMember(db, "2248007", "Nguyễn Dương Quốc", 3, "Trưởng nhóm");
        insertMember(db, "2248008", "Trần Tuấn Đạt", 3, "Thành viên");
        insertMember(db, "2248009", "Hà Hữu Quốc", 3, "Thành viên");
        insertThesis(db, "Xây dựng ứng dụng android lưu trữ ghi chú");
    }

    private void insertGroup4Data(SQLiteDatabase db) {
        insertMember(db, "2248010", "Cổ Đăng Khoa", 4, "Trưởng nhóm");
        insertMember(db, "2248011", "Lê Thành Long", 4, "Thành viên");
        insertThesis(db, "Xây dựng ứng dụng Android cho nhân viên đặt món nước tại CFPLUS");
    }

    private void insertGroup5Data(SQLiteDatabase db) {
        insertMember(db, "2248012", "Lê Thanh Hoài", 5, "Trưởng nhóm");
        insertMember(db, "2248013", "Võ Ngọc Phương Nam", 5, "Thành viên");
        insertMember(db, "2248014", "Trần Trọng Mạnh", 5, "Thành viên");
        insertThesis(db, "Xây dựng ứng dụng Android Quản Lí Chi Tiêu Cá Nhân");
    }

    private void insertMember(SQLiteDatabase db, String memId, String memName, int groupId, String role) {
        ContentValues values = new ContentValues();
        values.put("MemID", memId);
        values.put("MemName", memName);
        values.put("GroupID", groupId);
        values.put("Role", role);
        db.insert(TABLE_MEMBER, null, values);
    }

    private void insertThesis(SQLiteDatabase db, String thesisName) {
        ContentValues values = new ContentValues();
        values.put("ThesisName", thesisName);
        db.insert(TABLE_THESIS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THESIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        onCreate(db);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GROUP, null);

        if (cursor.moveToFirst()) {
            do {
                groups.add(new Group(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groups;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEMBER, null);

        if (cursor.moveToFirst()) {
            do {
                members.add(new Member(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    public List<Member> getMembersByGroup(int groupId) {
        List<Member> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEMBER + " WHERE GroupID = ?",
                new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                members.add(new Member(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    public List<Thesis> getAllThesis() {
        List<Thesis> thesisList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_THESIS, null);

        if (cursor.moveToFirst()) {
            do {
                thesisList.add(new Thesis(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return thesisList;
    }

    public void saveRegistration(int groupId, int thesisId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GroupID", groupId);
        values.put("ThesisID", thesisId);
        db.insertWithOnConflict(TABLE_REGISTRATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateMemberGroup(String memId, int newGroupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GroupID", newGroupId);
        db.update(TABLE_MEMBER, values, "MemID = ?", new String[]{memId});
    }
}
