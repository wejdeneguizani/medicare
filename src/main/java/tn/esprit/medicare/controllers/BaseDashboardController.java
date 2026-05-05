package tn.esprit.medicare.controllers;

public abstract class BaseDashboardController {
    protected int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        initializeData();
    }

    protected abstract void initializeData();
}
