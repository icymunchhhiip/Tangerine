package com.sixsense.tangerine.ref;

import android.app.AlertDialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.R;

import java.util.ArrayList;

//import java.util.Myingredient;

public class MyingredientAdapter extends RecyclerView.Adapter<MyingredientAdapter.CustomViewHolder> {

    private ArrayList<Myingredient> mList;
    private Context mContext;
    private String mJsonString;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    private static String IP_ADDRESS = "18.214.88.248";
    private static String TAG = "sixsense (6)";

    String userid;
    private String strVal;
    private int counter;

    public MyingredientAdapter(Context context, ArrayList<Myingredient> list) {
        mList = list;
        mContext = context;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView ingredientTextView;

        public CustomViewHolder(View view) {
            super(view);
            this.ingredientTextView = (TextView) view.findViewById(R.id.ingredientTextView);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "상세보기 | 수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final String storage = (mList.get(getAdapterPosition()).getStorage());
                final String fID =  (mList.get(getAdapterPosition()).getF_id());
                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.ref_ingredient_details, null, false);
                        builder.setView(view);


                        final Button okbutton = (Button) view.findViewById(R.id.okbutton);
                        final Button xbutton = (Button) view.findViewById(R.id.xbutton);
                        final ImageButton upbutton = (ImageButton) view.findViewById(R.id.upButton);
                        final ImageButton downbutton = (ImageButton) view.findViewById(R.id.downButton);

                        //여기서 EditText는 빈칸이다 빈칸을 불러온거다.

                        final EditText nameEditText = (EditText) view.findViewById(R.id.nameEditText);
                        final EditText dateEditText = (EditText) view.findViewById(R.id.dateEditText);
                        final EditText memoEditText = (EditText) view.findViewById(R.id.memoEditText);
                        final EditText remainingEditText = (EditText) view.findViewById(R.id.remainingEditText);
                        final TextView storageTextView = (TextView) view.findViewById(R.id.storageTextView);
                        // final TextView idTextView = (TextView) view.findViewById(R.id.idTextView);


                        //빈칸에다가 Myingredient 배열인 mList에 저장된 값들을 get해
                        nameEditText.setText(mList.get(getAdapterPosition()).getIngredient());
                        dateEditText.setText(mList.get(getAdapterPosition()).getDate());
                        memoEditText.setText(mList.get(getAdapterPosition()).getMemo());
                        remainingEditText.setText((mList.get(getAdapterPosition()).getRemaining()));
                        counter=Integer.parseInt(remainingEditText.getText().toString());


                        if (storage.equals("01")) {
                            storageTextView.setText("냉동보관");
                        } else if (storage.equals("10")) {
                            storageTextView.setText("냉장보관");
                        } else {
                            storageTextView.setText("실온보관");
                        }

                        // idTextView.setText(mList.get(getAdapterPosition()).getM_id());

                        final AlertDialog dialog = builder.create();


                        //고유아이디 불러오기
                        UserManagement.getInstance().me(new MeV2ResponseCallback() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                            }

                            @Override
                            public void onSuccess(MeV2Response result) {
                                MeV2Response myAccount = result;
                                userid = Long.toString(result.getId());
                            }
                        });

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


                        xbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        okbutton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String mid = userid;
                                String strIngredient = nameEditText.getText().toString();
                                String strDate = dateEditText.getText().toString();
                                String strMemo = memoEditText.getText().toString();
                                String strNum = remainingEditText.getText().toString();
//                                int strNum = Integer.parseInt(remainingEditText.getText().toString());
                                Myingredient dict = new Myingredient(mid, strIngredient, strDate, strMemo, strNum, storage);
                                dict.setF_id(fID);

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                                PostRequestHandler postRequestHandler = new PostRequestHandler(AppConstants.UPDATE_INGR, dict);
                                postRequestHandler.execute();
                            }
                        });

                        dialog.show();

                        break;

                    case 1002:
                        String mid = userid;
                        String strIngredient = mList.get(getAdapterPosition()).getIngredient();
                        String strDate = mList.get(getAdapterPosition()).getDate();
                        String strMemo = mList.get(getAdapterPosition()).getMemo();
                        String strNum = mList.get(getAdapterPosition()).getRemaining();

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());
//                                int strNum = Integer.parseInt(remainingEditText.getText().toString());
                        Myingredient dict = new Myingredient(mid, strIngredient, strDate, strMemo, strNum, storage);
                        dict.setF_id(fID);

                        PostRequestHandler postRequestHandler = new PostRequestHandler(AppConstants.DELETE_INGR, dict);
                        postRequestHandler.execute();
                        break;

                }
                return false;
            }
        };
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ref_ingredient, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.ingredientTextView.setText(mList.get(position).getIngredient());
    }

    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


    public void addItem(Myingredient item) {
        mList.add(item);
    }

    public void setItems(ArrayList<Myingredient> items) {
        this.mList = items;
    }

    public Myingredient getItem(int position) {
        return mList.get(position);
    }

}