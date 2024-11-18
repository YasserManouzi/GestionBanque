package com.atoudeft.banque;

import java.util.Date;

public abstract class Operation {
    private TypeOperation type;
    private Date date;

    public Operation(TypeOperation type) {
        this.type = type;
        this.date = new Date(System.currentTimeMillis());
    }

    public TypeOperation getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }
}
