package com.example.baith5.models;

public class Member {
    private String memId;
    private String memName;
    private int groupId;
    private String role;

    public Member(String memId, String memName, int groupId, String role) {
        this.memId = memId;
        this.memName = memName;
        this.groupId = groupId;
        this.role = role;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return memName;
    }
}
