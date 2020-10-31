package com.sixsense.tangerine.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*장터게시판 : 위치설정 했을 때*/
public class MarketListFragment extends Fragment implements OnTaskCompletedListener {
    private static final String TAG = "MarketListFragment";

    private MyCommunityListener myCommunityListener;
    private Member member;
    private MarketPostAdapter adapter;

    private EditText editTextSearch;
    private TextView textViewKeyword, textViewNoResult;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof MainActivity) {
            myCommunityListener = (MyCommunityListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myCommunityListener != null){
            this.member = myCommunityListener.getMember();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_market_list, container, false);

        textViewKeyword = rootView.findViewById(R.id.textView_keyword);
        textViewNoResult = rootView.findViewById(R.id.textView_noResult);
        textViewKeyword.setVisibility(View.GONE);
        textViewNoResult.setVisibility(View.GONE);

        ImageButton buttonWriting = rootView.findViewById(R.id.button_writing); //장터 글쓰기 화면 이동
        buttonWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MarketWritingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("member",member);
                bundle.putInt("mode", AppConstants.MODE_WRITE);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.WRITE_OK);
            }
        });

        TextView textViewSetRegion = rootView.findViewById(R.id.textView_setting_region);
        textViewSetRegion.setText(member.getLocalString());

        textViewSetRegion.setOnClickListener(new View.OnClickListener() { //위치설정 다시 누르면
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetLocationActivity.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_SETLOCATION);//위치 설정 화면 이동
            }
        });

        Context context = rootView.getContext();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_board);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MarketPostAdapter();
        recyclerView.setAdapter(adapter);
//        recyclerView.setNestedScrollingEnabled(false);

        String[] paramNames ={"mk_localcode"};
        String[] values = {Integer.toString(member.getLocalCode())};
        GetDataTask task = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        task.execute("community/read_market.php");
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new OnMarketPostItemClickListener() { //장터 게시글 누르면
            @Override
            public void onItemClick(MarketPostAdapter.ViewHolder holder, View view, int position) { //게시글 클릭 이벤트
                MarketPost item = adapter.getItem(position);

                Intent intent = new Intent(getContext(),MarketReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("marketItem",item);
                bundle.putSerializable("member", member);
                bundle.putInt("p_type", AppConstants.MARKET_SIG);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });

        editTextSearch = rootView.findViewById(R.id.editText_search);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textViewKeyword.setVisibility(View.GONE);
                    textViewNoResult.setVisibility(View.GONE);
                    if(!editTextSearch.getText().toString().equals("")) {
                        String[] paramNames ={"mk_localcode","search"};
                        String values[] = {Integer.toString(member.getLocalCode()), editTextSearch.getText().toString()};
                        GetDataTask task = new GetDataTask(MarketListFragment.this,paramNames,values, AppConstants.MODE_SEARCH);
                        task.execute("community/search_market.php");
                        adapter.notifyDataSetChanged();
                        editTextSearch.setText("");
                        // 키보드 내려기
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    else
                        Toast.makeText(getContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppConstants.WRITE_OK || requestCode == AppConstants.DELETE_OK || requestCode == AppConstants.EDIT_OK){
            if(myCommunityListener != null){
                myCommunityListener.showMarketListFragment(); //프래그먼트 갱신
            }
        }

        if (requestCode == AppConstants.REQUEST_CODE_SETLOCATION) {
            if (resultCode == AppConstants.RESULT_OK) {
                String localString = data.getStringExtra("localString");
                int localCode = Integer.parseInt(data.getStringExtra("localCode")); //MarketSetLocation 액티비티에서 받아옴

                if(myCommunityListener != null){
                    myCommunityListener.setMemberLocation(localString,localCode); //Community 액티비티의 member객체에 위치 set

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(MarketListFragment.this).commit(); //현재 프래그먼트 스택 제거
                    myCommunityListener.showMarketListFragment();// 프래그먼트 갱신
                }
            }
        }
    }


    @Override
    public boolean jsonToItem(String jsonString) {
        String TAG_JSON = "localmarketlist";
        ArrayList<MarketPost> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                JSONObject writer = item.getJSONObject("writer");
                JSONObject post = item.getJSONObject("post");

                int m_id = writer.getInt("m_id");
                String m_name = writer.getString("m_name");
                String m_profile = null;
                if(!writer.isNull("m_profile"))
                    m_profile = writer.getString("m_profile");

                int mk_no = post.getInt("mk_no");
                int mk_localcode = post.getInt("mk_localcode");
                int mk_price = post.getInt("mk_price");
                String mk_title = post.getString("mk_title");
                String mk_description = post.getString("mk_description");
                String mk_date = post.getString("mk_date");
                String mk_imgpath = null;
                if(!post.isNull("mk_imgpath"))
                    mk_imgpath = post.getString("mk_imgpath");

                MarketPost readPost = new MarketPost(new Member(m_id,m_name,m_profile), mk_no, mk_localcode, mk_price, mk_title, mk_description, mk_date, mk_imgpath);
                items.add(readPost);
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
            return true;
        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
            return false;
        }
    }

    @Override
    public void noResultNotice(String searchString) {
        ArrayList<MarketPost> tmpItems = new ArrayList<>();
        adapter.setItems(tmpItems);
        adapter.notifyDataSetChanged();

        textViewKeyword.setText("'" + searchString + "'");
        textViewKeyword.setVisibility(View.VISIBLE);
        textViewNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }
}
