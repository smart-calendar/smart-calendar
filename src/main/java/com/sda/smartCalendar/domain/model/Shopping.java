package com.sda.smartCalendar.domain.model;

public enum Shopping {

    Chleb("Chleb"), Bułki("Bułki"), Woda("Woda"), Sok("Sok"), Mleko("Mleko"), Masło("Masło"), Jogurt("Jogurt"), Serzolty("Ser żółty"),
    Twarożek("Twarożek"),Jajka("Jajka"), Kurczak("Kurczak"), Mięsomielone("Mięso mielone"), Szynka("Szynka"),
    Piwo("Piwo"), Wino("Wino"), Makaron("Makaron"), Ryż("Ryż"), Kasza("Kasza"), Płatki("Płatki"), Ziemniaki("Ziemniaki"),
    Pomidory("Pomidory"), Ogórki("Ogórki"), Marchewka("Marchewka"), Jabłka("Jabłka"), Banany("Banany"), Papier("Papier toaletowy");

    private String name;

    Shopping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
