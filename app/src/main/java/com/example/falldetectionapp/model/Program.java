package com.example.falldetectionapp.model;

public class Program {
    static Program program;
    private User currentUser;

    private Program() {

    }

    public static Program getInstance(){
        if (program == null) {
            program = new Program();
        }
        return program;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
