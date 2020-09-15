package com.sixsense.tangerine.community;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.CookingTip;

import java.util.ArrayList;

public class CookingTipActivity extends AppCompatActivity {
    private static final String TAG = "CookingTipActivity";
    private ArrayList<CookingTip> mList;
    private CookingTip item;
    TextView tv_upload_date;
    TextView tv_title;
    TextView tv_content;
    TextView textView4;
    Button button2;
    Button button3;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        Bundle bundle = getIntent().getExtras();
        item = (CookingTip) bundle.getSerializable("cooking");


        CookingTipListAdapter adapter= new CookingTipListAdapter();

        tv_upload_date = findViewById(R.id.tv_upload_date);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        textView4 = findViewById(R.id.textView4);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        //유용한가요는 너무 허전해서 걍 넣은거.. 별 기능 없음 보고서쓸때 뺄수도 있음
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CookingTipActivity.this, "감사합니다:)", Toast.LENGTH_SHORT).show();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CookingTipActivity.this, "분발하겠습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        tv_upload_date.setText(item.getWdate());
        tv_title.setText(item.getTitle());
        tv_content.setText(item.getContent());

    }



//    private class GetData extends AsyncTask<String, Void, String>{
//
//        ProgressDialog progressDialog;
//        String errorString = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "Please Wait", null, true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//            mTextViewResult.setText(result);
//            Log.d(TAG, "response - " + result);
//
//            if (result == null){
//
//                mTextViewResult.setText(errorString);
//            }
//            else {
//
//                mJsonString = result;
//                showResult();
//            }
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String searchKeyword1 = params[0];
//            String searchKeyword2 = params[1];
//
//            String serverURL = "http://서버IP/query.php";
//            String postParameters = "country=" + searchKeyword1 + "&name=" + searchKeyword2;
//
//
//            try {
//
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.connect();
//
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d(TAG, "response code - " + responseStatusCode);
//
//                InputStream inputStream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                while((line = bufferedReader.readLine()) != null){
//                    sb.append(line);
//                }
//
//
//                bufferedReader.close();
//
//
//                return sb.toString().trim();
//
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "InsertData: Error ", e);
//                errorString = e.toString();
//
//                return null;
//            }
//
//        }
//    }
//
//
//    private void showResult(){
//        try {
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            for(int i=0;i<jsonArray.length();i++){
//
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String id = item.getString(TAG_ID);
//                String name = item.getString(TAG_NAME);
//                String address = item.getString(TAG_ADDRESS);
//
//                HashMap<String,String> hashMap = new HashMap<>();
//
//                hashMap.put(TAG_ID, id);
//                hashMap.put(TAG_NAME, name);
//                hashMap.put(TAG_ADDRESS, address);
//
//                mArrayList.add(hashMap);
//            }
//
//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, mArrayList, R.layout.item_list,
//                    new String[]{TAG_ID,TAG_NAME, TAG_ADDRESS},
//                    new int[]{R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_address}
//            );
//
//            mListViewList.setAdapter(adapter);
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }
//
//    }

}