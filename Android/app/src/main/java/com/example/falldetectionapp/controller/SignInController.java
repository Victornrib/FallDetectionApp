package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

public class SignInController {

    public String email;
    public String password;

    public SignInController(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void signIn() {
        Program program = Program.getInstance();
        program.signIn(email, password);
    }
}
