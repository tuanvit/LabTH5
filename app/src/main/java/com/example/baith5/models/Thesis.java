package com.example.baith5.models;

public class Thesis {
    private int thesisId;
    private String thesisName;

    public Thesis(int thesisId, String thesisName) {
        this.thesisId = thesisId;
        this.thesisName = thesisName;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public String getThesisName() {
        return thesisName;
    }

    public void setThesisName(String thesisName) {
        this.thesisName = thesisName;
    }

    @Override
    public String toString() {
        return thesisName;
    }
}
