package com.sixsense.tangerine.ref;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.JsonParser;
import com.sixsense.tangerine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MyrefridgeratorFragment extends Fragment {

    private static String TAG = "MyrefridgeratorFragment";

    private TextView mTextViewResult;
    private String strVal;
    private int counter;

    private ArrayList<Myingredient> freezerList;
    private ArrayList<Myingredient> fridgeList;
    private ArrayList<Myingredient> roomList;

    private MyingredientAdapter myingredientAdapter;
    private MyingredientAdapter myingredientAdapter1;
    private MyingredientAdapter myingredientAdapter2;

    private RecyclerView freezerRecyclerView;
    private RecyclerView fridgeRecyclerView;
    private RecyclerView roomRecyclerView;

    private String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ref_fragment_myrefridgerator, container, false);

        mTextViewResult = (TextView) rootView.findViewById(R.id.textView_main_result);
        //냉동실,냉장실,실온실 배열 선언
        freezerList = new ArrayList<>();
        fridgeList = new ArrayList<>();
        roomList = new ArrayList<>();

        freezerRecyclerView = rootView.findViewById(R.id.freezerRecyclerView);
        fridgeRecyclerView = rootView.findViewById(R.id.fridgeRecyclerView);
        roomRecyclerView = rootView.findViewById(R.id.roomRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        freezerRecyclerView.setLayoutManager(gridLayoutManager);
        fridgeRecyclerView.setLayoutManager(gridLayoutManager1);
        roomRecyclerView.setLayoutManager(linearLayoutManager);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        //3개 어뎁터 설정
        myingredientAdapter = new MyingredientAdapter(getContext(), freezerList);
        myingredientAdapter1 = new MyingredientAdapter(getContext(), fridgeList);
        myingredientAdapter2 = new MyingredientAdapter(getContext(), roomList);

        //3개 어뎁터에 리싸이클러뷰 셋팅
        freezerRecyclerView.setAdapter(myingredientAdapter);
        fridgeRecyclerView.setAdapter(myingredientAdapter1);
        roomRecyclerView.setAdapter(myingredientAdapter2);

        //고유아이디 불러오기
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(MeV2Response result) {
                MeV2Response myAccount = result;
                userid = Long.toString(result.getId());
                Handler handler = new Handler();
                handler.execute(userid);
            }
        });

        //재료추가 버튼
        Button addbutton = rootView.findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //해당 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.ref_ingredient_add, null, false);

                builder.setView(view);

                final Button okbutton = (Button) view.findViewById(R.id.okbutton);
                final Button xbutton = (Button) view.findViewById(R.id.xbutton);
                final ImageButton upbutton = (ImageButton) view.findViewById(R.id.upButton);
                final ImageButton downbutton = (ImageButton) view.findViewById(R.id.downButton);

                final RadioGroup rg = (RadioGroup) view.findViewById(R.id.radioStorage);
                final EditText nameEditText = (EditText) view.findViewById(R.id.nameEditText);
                final EditText dateEditText = (EditText) view.findViewById(R.id.dateEditText);
                final EditText memoEditText = (EditText) view.findViewById(R.id.memoEditText);
                final EditText remainingEditText = (EditText) view.findViewById(R.id.remainingEditText);
                final AlertDialog dialog = builder.create();

                counter = 1;
                upbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter++;
                        strVal = Integer.toString(counter);
                        remainingEditText.setText(strVal);
                    }
                });

                downbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counter > 1) {
                            counter--;
                            strVal = Integer.toString(counter);
                            remainingEditText.setText(strVal);
                        }
                    }
                });

                //취소버튼: 창닫기
                xbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //확인버튼: 저장하고 창닫기
                okbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String mid = userid;
                        String name = nameEditText.getText().toString();
                        String date = dateEditText.getText().toString();
                        String memo = memoEditText.getText().toString();
                        String remaining = remainingEditText.getText().toString();
                        String storage = null;

                        int buttonId = rg.getCheckedRadioButtonId();
                        switch (buttonId) {
                            case R.id.radioBTfreezer:
                                storage = "01";
                                break;

                            case R.id.radioBTfridge:
                                storage = "10";
                                break;
                        }

                        if (nameEditText.getText().toString().equals("") || nameEditText.getText().toString() == null) {
                            setMessage("재료이름");
                        } else if (storage == null) {
                            setMessage("보관장소");
                        } else if (dateEditText.getText().toString().length() != 0 && dateEditText.getText().toString().length() != 8) { //유통기한은 선택사항이지만 입력된 경우에는
                            //8자리가 아니면 에러메세지
                            Toast toast = Toast.makeText(getContext(), "유통기한은 8자리의 YYYYMMDD 형태여야 합니다.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else {
                            Myingredient myingredient = new Myingredient(mid, name, date, memo, remaining, storage);
                            PostRequestHandler postRequestHandler = new PostRequestHandler(AppConstants.CREATE_INGR_URL, myingredient);   //계속 freezeList를 파라미터로 넣으려고 해서 오류.....
                            postRequestHandler.execute();

                            dialog.dismiss();
                            new Handler().execute(userid); //arraylist를 clear()하니까 성공

                        }
                    }
                });

                dialog.show();
            }
        });

        return rootView;
    }

    public void setMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text + "을(를) 확인해주세요.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    //--------------------Async task class to get json by making HTTP call
    private class Handler extends AsyncTask<String, Void, String> {
        String TAG_JSON = "webnautes";
        String TAG_FID = "f_id";
        String TAG_MID = "m_id";
        String TAG_INGR = "f_ingr";
        String TAG_DATE = "f_date";
        String TAG_MEMO = "f_memo";
        String TAG_REMAINING = "f_remaining";
        String TAG_STORAGE = "f_storage";

        ProgressDialog pDialog;
        String errorString = null;

        URL url;

        @Override
        protected void onPreExecute() {

            super.onPreExecute(); // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        //서버에 있는 php파일을 실행시키고 응답을 저장하고 스트링으로 변환하여 리턴.
        protected String doInBackground(String... arg0) {

            freezerList.clear();
            fridgeList.clear();
            roomList.clear();

            String USERID = arg0[0];
            Log.e(TAG, "USERID: " + USERID);

            JsonParser sh = new JsonParser();
            // Making a request to url and getting response
            String jsonStr = sh.convertJson(AppConstants.READ_INGR, USERID);

            Log.e(TAG, "Response from url: " + jsonStr);


            //showResult부분
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray(TAG_JSON);


                    //JsonArray에는 JSONObject가 데이터 갯수만큼 포함되어있음
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        //JSONObject에서 의 값을 가져옴
                        String fid = item.getString(TAG_FID);
                        String mid = item.getString(TAG_MID);
                        String ingr = item.getString(TAG_INGR);
                        String date = item.getString(TAG_DATE);
                        String memo = item.getString(TAG_MEMO);
                        String remaining = item.getString(TAG_REMAINING);
                        String storage = item.getString(TAG_STORAGE);
                        int int_date;


                        //데이터를 새로 생성한 Myingredient 클래스의 멤버변수에 입력하고 ArrayList에 추가합니다.
                        Myingredient myingredient = new Myingredient(mid, ingr, date, memo, remaining, storage);
                        myingredient.setF_id(fid);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                        Date cdate = new Date();
                        String currentDate = formatter.format(cdate);
                        int int_currentDate = Integer.parseInt(currentDate);

                        if (date != null && date.length() != 0) {
                            int_date = Integer.parseInt(date);

                            if (int_date < (int_currentDate)) {
                                roomList.add(myingredient);
                            } else {
                                if (storage.equals("01")) {
                                    freezerList.add(myingredient);
                                } else if (storage.equals("10") || storage.equals("11")) {
                                    fridgeList.add(myingredient);
                                }
                            }
                        } else {
                            if (storage.equals("01")) {
                                freezerList.add(myingredient);
                            } else if (storage.equals("10") || storage.equals("11")) {
                                fridgeList.add(myingredient);
                            }
                        }
                    }


                } catch (final JSONException e) {

                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }

            return null;

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();


            ////-------Updating parsed JSON data into ListView
            myingredientAdapter = new MyingredientAdapter(getContext(), freezerList);
            myingredientAdapter1 = new MyingredientAdapter(getContext(), fridgeList);
            myingredientAdapter2 = new MyingredientAdapter(getContext(), roomList);

            freezerRecyclerView.setAdapter(myingredientAdapter);
            fridgeRecyclerView.setAdapter(myingredientAdapter1);
            roomRecyclerView.setAdapter(myingredientAdapter2);
        }
    }

}
