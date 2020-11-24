package com.sixsense.tangerine;

import java.text.SimpleDateFormat;

public class AppConstants {
    public static int MARKET_SIG = 0; //장터 게시물
    public static int TIPS_SIG = 1; //꿀팁 게시물

    public static int MODE_READ = 10; //읽기
    public static int MODE_WRITE = 11; //새로작성
    public static int MODE_EDIT = 12; //수정
    public static int MODE_DELETE = 13; //삭제
    public static int MODE_SEARCH = 14; //검색

    public static int ABSOLUTE_PATH = 1; //절대경로 : 프로필
    public static int RELATIVE_PATH = 2; //상대경로 : 게시글 이미지

    public static int WRITE_OK = 202;
    public static int EDIT_OK = 203;
    public static int DELETE_OK = 204;

    public static int GET_GALLERY_IMAGE = 200;
    public static int PERMISSION_OK = 201;

    public static int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public static int REQUEST_CODE_SETLOCATION = 101;
    public static int RESULT_OK = -1;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    private static final String BASE_PATH = "http://115.85.183.191/";
    public static final String CREATE_INGR_URL = BASE_PATH + "fridge/insert_ingr.php";
    public static final String READ_INGR = BASE_PATH + "fridge/get_ingr.php";
    public static final String UPDATE_INGR = BASE_PATH + "fridge/update_ingr.php";
    public static final String DELETE_INGR = BASE_PATH + "fridge/delete_ingr.php";


    public static final String READ_MEMBER = BASE_PATH + "profile_edit/get_mem.php";
    public static final String UPDATE_MEMBER = "profile_edit/update_mem3.php";



    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
}
