package com.sixsense.tangerine.home.write;

public class ingr_list_item {
    String ingr_list_name;
    String ingr_list_num;
    String ingr_list_kcal;
    String ingr_list_unit;
   public ingr_list_item(String ingr, String num,  String unit, String kcal) {
        this.ingr_list_name = ingr;
        this.ingr_list_num = num;
        this.ingr_list_kcal = kcal;
        this.ingr_list_unit = unit;
    }


    public String getIngr_list_name() {
        return ingr_list_name;
    }

    public String getIngt_list_kcal() {
        return ingr_list_kcal;
    }

    public String getIngr_list_unit() {
        return ingr_list_unit;
    }

}
