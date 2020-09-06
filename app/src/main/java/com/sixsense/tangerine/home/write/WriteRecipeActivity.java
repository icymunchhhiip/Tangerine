package com.sixsense.tangerine.home.write;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.InRecipe;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.util.ArrayList;
import java.util.List;

public class WriteRecipeActivity extends AppCompatActivity {
    private RsettingPrivate storeRsetting  = new RsettingPrivate();
    private RecipeDescItem[] storeRecipe;
    private RecipeIngrPrivate[] recipeIngrPrivate;
    Button write_recipe_cancle_bt, write_recipe_finished_bt;
    RecyclerView rv_ingr; //추가된 재료들 보여줄 리스트
    AddedListAdapter rv_ingr_Adapter; //팝업창에서 받은 재료 추가할 리스트
    ArrayList<ingr_list_item> rv_ingr_itemArrayList; //추가될 재료
    Button ingr_add_bt ; //재료추가버튼, 팝업창 띄움

    View layout;

    AlertDialog.Builder ad;
    private List<ingr_list_item> exampleList;//예제리스트들이 담겨있음
    ArrayList<ingr_list_item> showSearchList; //검색 결과 보여줄 리스트변수
    EditText editSearch; //검색어 입력할 input 창
    ListView searchLV;//검색을 보여줄 리스트
    SearchAdapter searchAdapter ;//검색할 어댑터
    ArrayList<ingr_list_item> addedList;//팝업창에 올라갈 추가된 재료 리스트
    AddedListAdapter addedAdapter;//팝업창에 추가된 재료칸에 등록할 어댑터

    Button desc_add_bt;

    RecyclerView recipe_desc_RV;
    ArrayList<RecipeDescItem> recipe_desc_AL;
    RecipeDescAdapter recipe_desc_AD;

    int checkclicknum = 0;

    RecipeIntroList.RecipeIntro currentInfo;
    InRecipe currentDetail;

    EditText recipe_write_title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_write_write_recipe_activity);

        ingr_add_bt = findViewById(R.id.ingr_add_bt);

        rv_ingr=(RecyclerView)findViewById(R.id.RV_ingr_id);
        rv_ingr_itemArrayList = new ArrayList<ingr_list_item>();

        recipe_write_title = findViewById(R.id.recipe_write_title);
        write_recipe_cancle_bt=findViewById(R.id.write_recipe_cancle_bt);
        write_recipe_finished_bt = findViewById(R.id.write_recipe_finished_bt);

        currentInfo = (RecipeIntroList.RecipeIntro) getIntent().getSerializableExtra("currentIntro");
        getIntent().getSerializableExtra("currentDetail");
        if(currentInfo!=null){
            recipe_write_title.setText(currentInfo.recipe_name);
        }
        if(currentDetail!=null){
            getbuttons(currentDetail.foodType,currentDetail.level,currentDetail.rTool,currentDetail.rTime);
        }
        else {
            getbuttons(null,null,null,null);
        }
        storeDB();
        Recipe_desc();
        ingr_add_bt.setOnClickListener(new View.OnClickListener() {
            int num = 0;
            @Override
            public void onClick(View v) {
                rv_ingr_Adapter = new AddedListAdapter(WriteRecipeActivity.this, rv_ingr_itemArrayList);
                rv_ingr.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this));

                PopUP();
                //listView.setAdapter(myListAdapter);
            }
        });

    }

    public void storeDB(){

        storeRsetting.setR_likes(0);
        if(storeRsetting.getR_title()==null){
            Toast.makeText(getApplicationContext(), "제목이 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        if(storeRsetting.getR_foodT()==0){
            Toast.makeText(getApplicationContext(), "종류가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        if(storeRsetting.getR_level()==0){
            Toast.makeText(getApplicationContext(), "난이도가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        if(storeRsetting.getR_tool()==0){
            Toast.makeText(getApplicationContext(), "조리도구가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        if(storeRsetting.getR_time()==0){
            Toast.makeText(getApplicationContext(), "조리시간이 선책되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }


        int recipeIngrSize = rv_ingr_itemArrayList.size();
        recipeIngrPrivate = new RecipeIngrPrivate[recipeIngrSize];
        if(recipeIngrSize==0){
            Toast.makeText(getApplicationContext(), "레시피를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
            return;

        }

        int rid = 0;
        for(int i=0; i<recipeIngrSize; i++){
            ingr_list_item ingr = rv_ingr_itemArrayList.get(i);
            recipeIngrPrivate[i].setRid(rid);
            recipeIngrPrivate[i].setRingrname(ingr.ingr_list_name);
            recipeIngrPrivate[i].setRingrnum(Float.parseFloat(ingr.ingr_list_num));
            recipeIngrPrivate[i].setRkcal(Float.parseFloat(ingr.ingr_list_kcal));
            recipeIngrPrivate[i].setRunit(ingr.ingr_list_unit);
        }

        write_recipe_finished_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void Recipe_desc(){
        desc_add_bt = findViewById(R.id.desc_add_bt);

        recipe_desc_RV = findViewById(R.id.RV_recipe_desc_id);
        recipe_desc_AL= new ArrayList<RecipeDescItem>();
        recipe_desc_AD = new RecipeDescAdapter(WriteRecipeActivity.this, recipe_desc_AL);
        recipe_desc_AL.add(new RecipeDescItem("","","", 0,"00:00:00"));

        recipe_desc_RV.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recipe_desc_RV.setAdapter(recipe_desc_AD);


        recipe_desc_AD.setOnItemClickListener(new RecipeDescAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                recipe_desc_AL.remove(position);
                recipe_desc_AD.notifyItemRemoved(position);
                recipe_desc_RV.setAdapter(recipe_desc_AD);
            }

        });

        desc_add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recipe_desc_AL = recipe_desc_AD.getArrayList();
                recipe_desc_AL.add(new RecipeDescItem("","","", 0, "00:00:00"));
                recipe_desc_RV.setAdapter(recipe_desc_AD);

            }
        });



    }

    public void PopUP(){

        final EditText write_ingr_kcal_id, write_ingr_num_id, write_ingr_unit_id;
        final Button register;

        final Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.home_write_ingrpopup, (ViewGroup)findViewById(R.id.ingr_popup_id));
        ad= new AlertDialog.Builder(WriteRecipeActivity.this);
        ad.setTitle("재료추가");
        ad.setView(layout); //팝업창에 띄워질 view(xml)


        searchLV = layout.findViewById(R.id.ingr_searchLV);
        editSearch =layout.findViewById(R.id.editSearch);


        write_ingr_unit_id = layout.findViewById(R.id.write_ingr_unit_id);
        write_ingr_kcal_id = layout.findViewById(R.id.write_ingr_kcal_id);
        write_ingr_num_id = layout.findViewById(R.id.write_ingr_num_id);
        register = layout.findViewById(R.id.register_ingr_id);

        exampleList = new ArrayList<ingr_list_item>();
        addedList = new ArrayList<ingr_list_item>();
        addedAdapter=new AddedListAdapter(WriteRecipeActivity.this, addedList);

        rv_ingr.setLayoutManager(new LinearLayoutManager(WriteRecipeActivity.this));

        exampleList.add(new ingr_list_item("감자","0", "개","45"));
        exampleList.add(new ingr_list_item("고구마","0", "개","80"));
        exampleList.add(new ingr_list_item("오이","0", "개","9"));
        exampleList.add(new ingr_list_item("돼지감자","0", "개","70"));
        exampleList.add(new ingr_list_item("고추","0", "개","30"));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingrname, ingrunit="";
                float ingrnum=0, ingrkcal = 0;
                TextView alarm;
                alarm = layout.findViewById(R.id.popup_alarm);
                String nn="", nk="";
                ingrname=String.valueOf(editSearch.getText());
                try {
                    ingrnum= Float.parseFloat(String.valueOf(write_ingr_num_id.getText()));

                    nn = ingrnum +"";
                }catch (Exception e){
                    write_ingr_num_id.setText("");
                }
                try {
                    ingrkcal= Integer.parseInt(String.valueOf(write_ingr_kcal_id.getText()));
                    nk = ingrkcal+"";
                }catch (Exception e){
                    write_ingr_kcal_id.setText("");
                }
                try{
                    ingrunit=String.valueOf(write_ingr_unit_id.getText());
                }catch (Exception e){
                    write_ingr_unit_id.setText("");
                }
                if(nn.compareTo("")!=0 && nk.compareTo("")!=0&&ingrunit.compareTo("")!=0){
                    float totalkcal = ingrkcal*ingrnum;
                    nk = String.valueOf(totalkcal);
                    rv_ingr_itemArrayList.add(new ingr_list_item(ingrname, nn, ingrunit, nk));
                    rv_ingr.setAdapter(rv_ingr_Adapter);

                    rv_ingr_Adapter.setOnItemClickListener(new AddedListAdapter.OnItemClickListener(){
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
                }
                else if(nn.compareTo("")==0){
                    alarm.setText("재료수에는 정수만 입력해 주십시오.");
                }
                else if(nk.compareTo("")==0){
                    alarm.setText("칼로리에는 숫자만 입력해주세요.");
                }
                else if(ingrunit.compareTo("")==0){alarm.setText("단위를 입력해주세요.");}
                else{alarm.setText("칼로리에는 정수만, 재료수에는 숫자만 입력해주세요.");}
            }
        });



        showSearchList =  new ArrayList<ingr_list_item>();
        showSearchList.addAll(exampleList);

        // 리스트에 연동될 아답터를 생성한다.
        searchAdapter = new SearchAdapter(mContext, exampleList);
        searchLV.setAdapter(searchAdapter);

        searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editSearch.setText(showSearchList.get(position).getIngr_list_name());
                write_ingr_kcal_id.setText(showSearchList.get(position).getIngt_list_kcal());
                write_ingr_unit_id.setText(showSearchList.get(position).getIngr_list_unit());

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

        AlertDialog nad =ad.create();
        nad.show();

    }
    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        exampleList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            exampleList.addAll(showSearchList);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < showSearchList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (showSearchList.get(i).getIngr_list_name().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    exampleList.add(showSearchList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        searchAdapter.notifyDataSetChanged();
    }

    public void getbuttons(String foodType, String level, String rTool, String rTime){
        ToggleButton foodtype_all, foodtype_meal, foodtype_snack;
        ToggleButton foodtool_all, foodtool_fire, foodtool_microwave, foodtool_oven, foodtool_airfryer;
        ToggleButton foodlevel_all, foodlevel_veryeasy, foodlevel_easy, foodlevel_normal, foodlevel_hard;
        ToggleButton foodtime_all, foodtime_veryshort, foodtime_short, foodtime_medium, foodtime_long;

        foodtype_all=findViewById(R.id.foodtype_all);
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

        switch (foodType){
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

        switch (level){
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

        switch (rTool){
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

        switch (rTime){
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

        toggleClickCheck(2, foodtype_all, foodtype_meal, foodtype_snack);
        toggleClickCheck(3, foodlevel_all, foodlevel_veryeasy, foodlevel_easy, foodlevel_normal, foodlevel_hard);
        toggleClickCheck(4, foodtime_all, foodtime_veryshort, foodtime_short, foodtime_medium, foodtime_long);
        toggleClickCheck(5, foodtool_all, foodtool_fire, foodtool_microwave, foodtool_oven, foodtool_airfryer);

    }
    public void toggleClickCheck( int listelementnum, final ToggleButton all, final ToggleButton one, final ToggleButton two){
        checkclicknum=0;
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(one, two);
                checkclicknum =15;
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all,  two);
                checkclicknum = 1;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all, one);
                checkclicknum = 2;
            }
        });
        storeRsetting.setR_foodT(checkclicknum);
    }
    public void toggleButtonOnly(ToggleButton elsev1, ToggleButton elsev2){
        elsev1.setChecked(false);
        elsev2.setChecked(false);
    }

    public void toggleClickCheck( int listelementnum, final ToggleButton all, final ToggleButton one, final ToggleButton two, final ToggleButton four, final ToggleButton eight){
        checkclicknum = 0;
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(one, two, four, eight);
                checkclicknum =15;
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all,  two, four, eight);
                checkclicknum = 1;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all, one, four, eight);
                checkclicknum = 2;
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all, one, two,  eight);
                checkclicknum = 3;
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonOnly(all, one, two, four);
                checkclicknum = 4;
            }
        });
        if(listelementnum == 3 )
            storeRsetting.setR_level(checkclicknum);
        if(listelementnum == 4)
            storeRsetting.setR_time(checkclicknum);
        if(listelementnum ==5)
            storeRsetting.setR_tool(checkclicknum);
    }
    public void toggleButtonOnly(ToggleButton elsev1, ToggleButton elsev2, ToggleButton elsev3, ToggleButton elsev4){
        elsev1.setChecked(false);
        elsev2.setChecked(false);
        elsev3.setChecked(false);
        elsev4.setChecked(false);
    }


}
