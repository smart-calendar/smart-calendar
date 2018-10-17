package com.sda.smartCalendar.domain.model;

public enum Category {

    PRACA("PRACA"), NAUKA("NAUKA"), ROZRYWKA("ROZRYWKA"), HOBBY("HOBBY");

    private String name;

    Category(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
