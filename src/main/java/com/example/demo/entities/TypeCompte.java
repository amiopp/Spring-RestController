package com.example.demo.entities;

public enum TypeCompte {
    COURANT("Courant"),
    EPARGNE("Epargne");

    private final String displayName;

    TypeCompte(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isEpargne() {
        return this == EPARGNE;
    }

    public boolean isCourant() {
        return this == COURANT;
    }

    @Override
    public String toString() {
        return displayName;
    }
}