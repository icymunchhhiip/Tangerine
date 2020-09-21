package com.sixsense.tangerine;

public class Constant {
    private static final String BASE_PATH = "http://ec2-34-203-38-62.compute-1.amazonaws.com/";
    public static final String CREATE_INGR_URL = BASE_PATH + "fridge/insert_ingr.php";
    public static final String READ_INGR = BASE_PATH + "fridge/get_ingr.php";
    public static final String UPDATE_INGR = BASE_PATH + "fridge/update_ingr.php";
    public static final String DELETE_INGR = BASE_PATH + "fridge/delete_ingr.php";

    //public static final String CREATE_INGR_URL = BASE_PATH + "insert_ingr.php";
    public static final String READ_MEMBER = BASE_PATH + "profile_edit/get_mem.php";
    public static final String UPDATE_MEMBER = BASE_PATH + "profile_edit/update_mem3.php";
    //public static final String DELETE_INGR = BASE_PATH + "delete_ingr.php";


    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

}