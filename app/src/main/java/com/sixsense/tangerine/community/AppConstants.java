package com.sixsense.tangerine.community;

import java.text.SimpleDateFormat;

public class AppConstants {
    public static int MARKET_SIG = 0; //장터 게시판
    public static int BOARD_SIG = 2; //게시판

    //카테고리 바이너리값
    public static String CATEGORY_1 = "01"; //잡담 01
    public static String CATEGORY_2 = "10"; //꿀팁 10
    public static String CATEGORY_3 = "11"; //질문 11

    //게시판 바이너리값
    public static String MARKET_BINARY = "01";
    public static String BOARD_BINARY = "10";


    public static int MODE_READ = 10; //읽기
    public static int MODE_WRITE = 11; //새로작성
    public static int MODE_EDIT = 12; //수정
    public static int MODE_DELETE = 13; //삭제
    public static int MODE_SEARCH = 14; //검색

    public static int ABSOLUTE_PATH = 1; //절대경로 : 프로필
    public static int RELATEVE_PATH = 2; //상대경로 : 게시글 이미지

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
}
