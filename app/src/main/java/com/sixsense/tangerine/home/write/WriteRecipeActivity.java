package com.sixsense.tangerine.home.write;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.InRecipe;
import com.sixsense.tangerine.network.RecipeIntroList;
import com.sixsense.tangerine.network.SelectedRecipe;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.sixsense.tangerine.MainActivity.sMyAccount;

public class WriteRecipeActivity extends AppCompatActivity {
    private RsettingPrivate storeRsetting = new RsettingPrivate();
    private RecipeDescItem[] storeRecipe;
    private RecipeIngrPrivate[] recipeIngrPrivate;
    Button write_recipe_cancle_bt, write_recipe_finished_bt;
    RecyclerView rv_ingr; //추가된 재료들 보여줄 리스트
    AddedListAdapter rv_ingr_Adapter; //팝업창에서 받은 재료 추가할 리스트
    ArrayList<ingr_list_item> rv_ingr_itemArrayList; //추가될 재료
    Button ingr_add_bt; //재료추가버튼, 팝업창 띄움

    View layout;

    AlertDialog.Builder ad;
    private List<ingr_list_item> exampleList;//예제리스트들이 담겨있음
    ArrayList<ingr_list_item> showSearchList; //검색 결과 보여줄 리스트변수
    EditText editSearch; //검색어 입력할 input 창
    ListView searchLV;//검색을 보여줄 리스트
    SearchAdapter searchAdapter;//검색할 어댑터
    ArrayList<ingr_list_item> addedList;//팝업창에 올라갈 추가된 재료 리스트
    AddedListAdapter addedAdapter;//팝업창에 추가된 재료칸에 등록할 어댑터

    Button desc_add_bt;

    RecyclerView recipe_desc_RV;
    ArrayList<RecipeDescItem> recipe_desc_AL;
    RecipeDescAdapter recipe_desc_AD;


    RecipeIntroList.RecipeIntro currentInfo;
    InRecipe currentDetail;

    EditText recipe_write_title;
    ToggleButton foodtype_all, foodtype_meal, foodtype_snack;
    ToggleButton foodtool_all, foodtool_fire, foodtool_microwave, foodtool_oven, foodtool_airfryer;
    ToggleButton foodlevel_all, foodlevel_veryeasy, foodlevel_easy, foodlevel_normal, foodlevel_hard;
    ToggleButton foodtime_all, foodtime_veryshort, foodtime_short, foodtime_medium, foodtime_long;
    int type=0, tool=0, level=0,time=0;

    SelectedRecipe selectedRecipe = new SelectedRecipe();

    final int CODE_GALLERY_REQUEST = 999;
    String url =  "http://ec2-34-203-38-62.compute-1.amazonaws.com/recipe/upload.php";
    Bitmap bitmap;
    private int CLICKPOSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_write_write_recipe_activity);
        Toolbar toolbar = findViewById(R.id.write_recipe_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ingr_add_bt = findViewById(R.id.ingr_add_bt);
        recipe_write_title = findViewById(R.id.recipe_write_title);
        write_recipe_cancle_bt = findViewById(R.id.write_recipe_cancle_bt);
        write_recipe_finished_bt = findViewById(R.id.write_recipe_finished_bt);

        foodtype_all = findViewById(R.id.foodtype_all);
        foodtype_meal = findViewById(R.id.foodtype_meal);
        foodtype_snack = findViewById(R.id.foodtype_snack);

        foodlevel_all = findViewById(R.id.foodlevel_all);
        foodlevel_veryeasy = findViewById(R.id.foodlevel_veryeasy);
        foodlevel_easy = findViewById(R.id.foodlevel_easy);
        foodlevel_hard = findViewById(R.id.foodlevel_hard);
        foodlevel_normal = findViewById(R.id.foodlevel_normal);

        foodtime_all = findViewById(R.id.foodtime_all);
        foodtime_veryshort = findViewById(R.id.foodtime_veryshort);
        foodtime_short = findViewById(R.id.foodtime_short);
        foodtime_medium = findViewById(R.id.foodtime_medium);
        foodtime_long = findViewById(R.id.foodtime_long);

        foodtool_all = findViewById(R.id.foodtool_all);
        foodtool_fire = findViewById(R.id.foodtool_fire);
        foodtool_microwave = findViewById(R.id.foodtool_microwave);
        foodtool_oven = findViewById(R.id.foodtool_oven);
        foodtool_airfryer = findViewById(R.id.foodtool_airfryer);
        rv_ingr = (RecyclerView) findViewById(R.id.RV_ingr_id);

        rv_ingr_itemArrayList = new ArrayList<ingr_list_item>();
        //rv_ingr_itemlist에 불러온 값 넣기


        write_recipe_cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        currentInfo = (RecipeIntroList.RecipeIntro) getIntent().getSerializableExtra("EXTRA_CURRENT_INFO");
        currentDetail = (InRecipe) getIntent().getSerializableExtra("EXTRA_CURRENT_DETAIL");
        write_recipe_finished_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String state = "fail";
                    if (storeDB()) {
                        state = new InsertCall().execute(selectedRecipe).get();
                        if (state.equals("success")) {
                            onBackPressed();
                            Toast.makeText(getApplicationContext(),"메인화면에서 아래로 당겨서 새로고침 해주세요.",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"등록을 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        if (currentInfo != null) {
            recipe_write_title.setText(currentInfo.recipeName);
        }


        ToggleButton.OnCheckedChangeListener onCheckedChangeListener = new ToggleButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.foodtype_all:
                            foodtype_meal.setChecked(false);
                            foodtype_snack.setChecked(false);
                            type=3;
                            break;
                        case R.id.foodtype_meal:
                            foodtype_all.setChecked(false);
                            foodtype_snack.setChecked(false);
                            type=1;
                            break;
                        case R.id.foodtype_snack:
                            foodtype_all.setChecked(false);
                            foodtype_meal.setChecked(false);
                            type=2;
                            break;
                        case R.id.foodlevel_all:
                            foodlevel_veryeasy.setChecked(false);
                            foodlevel_easy.setChecked(false);
                            foodlevel_hard.setChecked(false);
                            foodlevel_normal.setChecked(false);
                            level=15;
                            break;
                        case R.id.foodlevel_veryeasy:
                            foodlevel_all.setChecked(false);
                            foodlevel_easy.setChecked(false);
                            foodlevel_hard.setChecked(false);
                            foodlevel_normal.setChecked(false);
                            level=1;
                            break;
                        case R.id.foodlevel_easy:
                            foodlevel_veryeasy.setChecked(false);
                            foodlevel_all.setChecked(false);
                            foodlevel_hard.setChecked(false);
                            foodlevel_normal.setChecked(false);
                            level=2;
                            break;
                        case R.id.foodlevel_normal:
                            foodlevel_veryeasy.setChecked(false);
                            foodlevel_easy.setChecked(false);
                            foodlevel_hard.setChecked(false);
                            foodlevel_all.setChecked(false);
                            level=4;
                            break;
                        case R.id.foodlevel_hard:
                            foodlevel_veryeasy.setChecked(false);
                            foodlevel_easy.setChecked(false);
                            foodlevel_all.setChecked(false);
                            foodlevel_normal.setChecked(false);
                            level=8;
                            break;
                        case R.id.foodtool_all:
                            foodtool_airfryer.setChecked(false);
                            foodtool_fire.setChecked(false);
                            foodtool_microwave.setChecked(false);
                            foodtool_oven.setChecked(false);
                            tool=15;
                            break;
                        case R.id.foodtool_fire:
                            foodtool_all.setChecked(false);
                            foodtool_airfryer.setChecked(false);
                            foodtool_microwave.setChecked(false);
                            foodtool_oven.setChecked(false);
                            tool=1;
                            break;
                        case R.id.foodtool_microwave:
                            foodtool_all.setChecked(false);
                            foodtool_airfryer.setChecked(false);
                            foodtool_fire.setChecked(false);
                            foodtool_oven.setChecked(false);
                            tool=2;
                            break;
                        case R.id.foodtool_oven:
                            foodtool_all.setChecked(false);
                            foodtool_airfryer.setChecked(false);
                            foodtool_fire.setChecked(false);
                            foodtool_microwave.setChecked(false);
                            tool=4;
                            break;
                        case R.id.foodtool_airfryer:
                            foodtool_all.setChecked(false);
                            foodtool_oven.setChecked(false);
                            foodtool_fire.setChecked(false);
                            foodtool_microwave.setChecked(false);
                            tool=8;
                            break;
                        case R.id.foodtime_all:
                            foodtime_long.setChecked(false);
                            foodtime_veryshort.setChecked(false);
                            foodtime_short.setChecked(false);
                            foodtime_medium.setChecked(false);
                            time=15;
                            break;
                        case R.id.foodtime_veryshort:
                            foodtime_long.setChecked(false);
                            foodtime_all.setChecked(false);
                            foodtime_short.setChecked(false);
                            foodtime_medium.setChecked(false);
                            time=1;
                            break;
                        case R.id.foodtime_short:
                            foodtime_long.setChecked(false);
                            foodtime_all.setChecked(false);
                            foodtime_veryshort.setChecked(false);
                            foodtime_medium.setChecked(false);
                            time=2;
                            break;
                        case R.id.foodtime_medium:
                            foodtime_long.setChecked(false);
                            foodtime_all.setChecked(false);
                            foodtime_short.setChecked(false);
                            foodtime_veryshort.setChecked(false);
                            time=4;
                            break;
                        case R.id.foodtime_long:
                            foodtime_veryshort.setChecked(false);
                            foodtime_all.setChecked(false);
                            foodtime_short.setChecked(false);
                            foodtime_medium.setChecked(false);
                            time=8;
                            break;
                    }
                }
            }
        };

        foodtype_all.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtype_meal.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtype_snack.setOnCheckedChangeListener(onCheckedChangeListener);
        foodlevel_all.setOnCheckedChangeListener(onCheckedChangeListener);
        foodlevel_veryeasy.setOnCheckedChangeListener(onCheckedChangeListener);
        foodlevel_easy.setOnCheckedChangeListener(onCheckedChangeListener);
        foodlevel_hard.setOnCheckedChangeListener(onCheckedChangeListener);
        foodlevel_normal.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtime_all.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtime_veryshort.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtime_short.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtime_medium.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtime_long.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtool_all.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtool_fire.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtool_microwave.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtool_oven.setOnCheckedChangeListener(onCheckedChangeListener);
        foodtool_airfryer.setOnCheckedChangeListener(onCheckedChangeListener);

        if (currentDetail != null) {
            if (currentDetail.foodType != null) {
                switch (currentDetail.foodType) {
                    case "난이도 전체":
                        foodtype_all.setChecked(true);
                        break;
                    case "한끼":
                        foodtype_meal.setChecked(true);
                        break;
                    case "간식":
                        foodtype_snack.setChecked(true);
                        break;
                    default:
                }
            }

            if (currentDetail.level != null) {
                switch (currentDetail.level) {
                    case "난이도 전체":
                        foodlevel_all.setChecked(true);
                        break;
                    case "초간단":
                        foodlevel_veryeasy.setChecked(true);
                        break;
                    case "초급":
                        foodlevel_easy.setChecked(true);
                        break;
                    case "중급":
                        foodtime_medium.setChecked(true);
                        break;
                    case "고급":
                        foodlevel_hard.setChecked(true);
                        break;
                    default:
                }
            }

            if (currentDetail.rTool != null) {
                switch (currentDetail.rTool) {
                    case "도구 전체":
                        foodtool_all.setChecked(true);
                        break;
                    case "불":
                        foodtool_fire.setChecked(true);
                        break;
                    case "전자레인지":
                        foodtool_microwave.setChecked(true);
                        break;
                    case "오븐":
                        foodtool_oven.setChecked(true);
                        break;
                    case "에어프라이어":
                        foodtool_airfryer.setChecked(true);
                        break;
                    default:
                }
            }

            if (currentDetail.rTime != null) {
                switch (currentDetail.rTime) {
                    case "시간 전체":
                        foodtime_all.setChecked(true);
                        break;
                    case "0-30분":
                        foodtime_veryshort.setChecked(true);
                        break;
                    case "30분-1시간":
                        foodtime_short.setChecked(true);
                        break;
                    case "1-2시간":
                        foodtime_medium.setChecked(true);
                        break;
                    case "2시간 이상":
                        foodtime_long.setChecked(true);
                        break;
                    default:
                }
            }
            Recipe_desc(currentDetail.recipeList);
            for (InRecipe.IngrInfo ingr : currentDetail.ingrList) {
                rv_ingr_itemArrayList.add(new ingr_list_item(ingr.ingrName, ingr.ingrCount, ingr.ingrUnit, ingr.ingrKcal));
            }

        } else {
            Recipe_desc(null);
        }
        rv_ingr_Adapter = new AddedListAdapter(WriteRecipeActivity.this, rv_ingr_itemArrayList);
        rv_ingr.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this));
        rv_ingr_Adapter.notifyItemInserted(rv_ingr_itemArrayList.size() + 1);
        rv_ingr.setAdapter(rv_ingr_Adapter);

        rv_ingr_Adapter.setOnItemClickListener(new AddedListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                rv_ingr_itemArrayList.remove(position);
                rv_ingr_Adapter.notifyItemRemoved(position);
            }
        });
        ingr_add_bt.setOnClickListener(new View.OnClickListener() {
            int num = 0;

            @Override
            public void onClick(View v) {
                PopUP();
                rv_ingr_Adapter.notifyDataSetChanged();
                rv_ingr_Adapter.notifyItemInserted(rv_ingr_itemArrayList.size() + 1);
                //listView.setAdapter(myListAdapter);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CODE_GALLERY_REQUEST){
            if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"),CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission.", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==CODE_GALLERY_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Uri filepath = data.getData();
            try{
                InputStream inputStream= getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);

                //upload
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        recipe_desc_AD.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String imageData = imageToString(bitmap);
                        params.put("image", imageData);
                        SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyyMMddHHmmss");
                        Date time = new Date();
                        String fname = MainActivity.sMyId +"_" +dateFormat.format(time);
                        params.put("filename", fname);
                        recipe_desc_AL.get(CLICKPOSITION).food_image_addr = fname+".jpeg";
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 30000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 30000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(WriteRecipeActivity.this);
                requestQueue.add(stringRequest);
            }catch(FileNotFoundException e){e.printStackTrace();}
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean storeDB() {
        if (currentInfo != null) {
            selectedRecipe.likeNum = currentInfo.recipeFav;
            selectedRecipe.recipeId = currentInfo.recipeId;
        } else {
            selectedRecipe.likeNum = 0;
        }
        selectedRecipe.memId = MainActivity.sMyId;
        if (recipe_write_title.getText() == null || recipe_write_title.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "제목이 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            String rname = recipe_write_title.getText().toString();
            selectedRecipe.recipeName = rname;
        }
        if (type==0) {
            Toast.makeText(getApplicationContext(), "종류가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.foodType = type;
        }
        if (level==0) {
            Toast.makeText(getApplicationContext(), "난이도가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.level = level;
        }
        if (tool==0) {
            Toast.makeText(getApplicationContext(), "조리도구가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.rTool = tool;
        }
        if (time==0) {
            Toast.makeText(getApplicationContext(), "조리시간이 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.rTime = time;
        }

        int recipeIngrSize = rv_ingr_itemArrayList.size();
        if (recipeIngrSize == 0) {
            Toast.makeText(getApplicationContext(), "재료를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.ingrList = new ArrayList<>();
            for (ingr_list_item item : rv_ingr_itemArrayList) {
                SelectedRecipe.IngrInfo tmp = selectedRecipe.new IngrInfo();
                tmp.ingrName = item.ingr_list_name;
                tmp.ingrCount = Float.parseFloat(item.ingr_list_num);
                tmp.ingrKcal = Float.parseFloat(item.ingr_list_kcal);
                tmp.ingrUnit = item.ingr_list_unit;
                selectedRecipe.ingrList.add(tmp);
            }
        }
        int recipeDescSize = rv_ingr_itemArrayList.size();

        if (recipeDescSize == 0) {
            Toast.makeText(getApplicationContext(), "레시피를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            selectedRecipe.recipeList = new ArrayList<>();
            for (RecipeDescItem item : recipe_desc_AL) {
                SelectedRecipe.RecipeContent tmp = selectedRecipe.new RecipeContent();
                tmp.rNum = item.descbtnum;
                if(item.recipe_desc.compareTo("")==0) {
                    Toast.makeText(getApplicationContext(), tmp.rNum+1 +"번째 설명칸에 설명을 넣어주세요!!", Toast.LENGTH_LONG).show();
                    return false;
                }
                tmp.recipeDesc = item.recipe_desc;
                tmp.descDetail = item.recipe_desc_detail;
                tmp.recipeImg = item.food_image_addr;
                tmp.recipeTime = item.cookingtime;
                selectedRecipe.recipeList.add(tmp);
            }
        }
        return true;
    }

    public void Recipe_desc(List<InRecipe.RecipeContent> recipeList) {
        desc_add_bt = findViewById(R.id.desc_add_bt);

        recipe_desc_RV = findViewById(R.id.RV_recipe_desc_id);
        recipe_desc_AL = new ArrayList<RecipeDescItem>();
        recipe_desc_AD = new RecipeDescAdapter(WriteRecipeActivity.this, recipe_desc_AL);
        if (recipeList != null) {
            for (InRecipe.RecipeContent content : recipeList) {
                if (content.recipeTime == null) {
                    recipe_desc_AL.add(new RecipeDescItem(content.recipeImg, content.recipeDesc, content.descDetail, 0, "00:00:00"));
                } else {
                    recipe_desc_AL.add(new RecipeDescItem(content.recipeImg, content.recipeDesc, content.descDetail, 0, content.recipeTime));
                }
            }
        } else {
            recipe_desc_AL.add(new RecipeDescItem("", "", "", 0, "00:00:00"));
        }

        recipe_desc_RV.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recipe_desc_RV.setAdapter(recipe_desc_AD);


        recipe_desc_AD.setOnItemClickListener(new RecipeDescAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                recipe_desc_AL.remove(position);
                recipe_desc_AD.notifyItemRemoved(position);
                recipe_desc_RV.setAdapter(recipe_desc_AD);
            }

            @Override
            public void onImageClick(int position) {
                CLICKPOSITION=0;
                CLICKPOSITION=position;
                ActivityCompat.requestPermissions(WriteRecipeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }

        });

        desc_add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recipe_desc_AL = recipe_desc_AD.getArrayList();
                recipe_desc_AL.add(new RecipeDescItem("", "", "", 0, "00:00:00"));
                recipe_desc_RV.setAdapter(recipe_desc_AD);

            }
        });


    }

    public void PopUP() {

        final EditText write_ingr_kcal_id, write_ingr_num_id, write_ingr_unit_id;
        final Button register;

        final Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.home_write_ingrpopup, (ViewGroup) findViewById(R.id.ingr_popup_id));
        ad = new AlertDialog.Builder(WriteRecipeActivity.this);
        ad.setTitle("재료추가");
        ad.setView(layout); //팝업창에 띄워질 view(xml)


        searchLV = layout.findViewById(R.id.ingr_searchLV);
        editSearch = layout.findViewById(R.id.editSearch);


        write_ingr_unit_id = layout.findViewById(R.id.write_ingr_unit_id);
        write_ingr_kcal_id = layout.findViewById(R.id.write_ingr_kcal_id);
        write_ingr_num_id = layout.findViewById(R.id.write_ingr_num_id);
        register = layout.findViewById(R.id.register_ingr_id);

        exampleList = new ArrayList<ingr_list_item>();
        addedList = new ArrayList<ingr_list_item>();
        addedAdapter = new AddedListAdapter(WriteRecipeActivity.this, addedList);

        rv_ingr.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this));

        try {
            List<InRecipe.IngrInfo> ingrInfoList = new IngrListCall().execute().get();
            putExL(ingrInfoList);
        } catch (Exception e) {
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingrname, ingrunit = "";
                float ingrnum = 0, ingrkcal = 0;
                TextView alarm;
                alarm = layout.findViewById(R.id.popup_alarm);
                String nn = "", nk = "";
                ingrname = String.valueOf(editSearch.getText());
                try {
                    ingrnum = Float.parseFloat(String.valueOf(write_ingr_num_id.getText()));

                    nn = ingrnum + "";
                } catch (Exception e) {
                    write_ingr_num_id.setText("");
                }
                try {
                    ingrkcal = Float.parseFloat(String.valueOf(write_ingr_kcal_id.getText()));
                    nk = ingrkcal + "";
                } catch (Exception e) {
                    write_ingr_kcal_id.setText("");
                }
                try {
                    ingrunit = String.valueOf(write_ingr_unit_id.getText());
                } catch (Exception e) {
                    write_ingr_unit_id.setText("");
                }
                if (nn.compareTo("") != 0 && nk.compareTo("") != 0 && ingrunit.compareTo("") != 0&&ingrname.compareTo("")!=0) {
                    float totalkcal = ingrkcal * ingrnum;
                    nk = String.valueOf(totalkcal);
                    rv_ingr_itemArrayList.add(new ingr_list_item(ingrname, nn, ingrunit, nk));
                    rv_ingr_Adapter.notifyItemInserted(rv_ingr_itemArrayList.size());

                    rv_ingr_Adapter.setOnItemClickListener(new AddedListAdapter.OnItemClickListener() {
                        @Override
                        public void onDeleteClick(int position) {
                            rv_ingr_itemArrayList.remove(position);
                            rv_ingr_Adapter.notifyItemRemoved(position);
                        }
                    });
                    write_ingr_kcal_id.setText("");
                    write_ingr_num_id.setText("");
                    write_ingr_unit_id.setText("");
                    editSearch.setText("");

                    Toast.makeText(getApplicationContext(), "재료가 등록되었습니다.", Toast.LENGTH_LONG).show();
                } else {
                    alarm.setText("재료명과 단위를 입력해주시고, 칼로리와 수량에는 숫자만 입력해주세요.");
                }
            }
        });

        showSearchList = new ArrayList<ingr_list_item>();
        showSearchList.addAll(exampleList);

        // 리스트에 연동될 아답터를 생성한다.
        searchAdapter = new SearchAdapter(mContext, showSearchList);
        searchLV.setAdapter(searchAdapter);

        searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingr_list_item ili=showSearchList.get(position);
                editSearch.setText(ili.getIngr_list_name());
                write_ingr_kcal_id.setText(ili.getIngt_list_kcal());
                write_ingr_unit_id.setText(ili.getIngr_list_unit());

            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });
        ad.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog nad = ad.create();
        nad.show();

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        showSearchList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            showSearchList.addAll(exampleList);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < exampleList.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (exampleList.get(i).getIngr_list_name().toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    showSearchList.add(exampleList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        searchAdapter.notifyDataSetChanged();
    }

    private void putExL(List<InRecipe.IngrInfo> ingrInfoList) {
        for (InRecipe.IngrInfo ingrInfo : ingrInfoList) {
            exampleList.add(new ingr_list_item(ingrInfo.ingrName, ingrInfo.ingrCount, ingrInfo.ingrUnit, ingrInfo.ingrKcal));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class IngrListCall extends AsyncTask<Void, Void, List<InRecipe.IngrInfo>> {

        @Override
        protected List<InRecipe.IngrInfo> doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<List<InRecipe.IngrInfo>> call = httpInterface.getIngrs();
            List<InRecipe.IngrInfo> resource = null;
            try {
                resource = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resource;
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class InsertCall extends AsyncTask<SelectedRecipe, Void, String> {
        @Override
        protected String doInBackground(SelectedRecipe... selectedRecipes) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.setRecipe(selectedRecipes[0]);
            String response = null;
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}