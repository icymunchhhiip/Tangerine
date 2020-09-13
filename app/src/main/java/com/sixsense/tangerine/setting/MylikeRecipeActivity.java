package com.sixsense.tangerine.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class MylikeRecipeActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUSET=1;
    private ArrayList<Recipe> items;
    private RecyclerView recyclerView;
    private MylikeRecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mylike_recipe);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        createItems();
        adapter = new MylikeRecipeAdapter(items);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MylikeRecipeAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    //임의로 넣어놓음 DB연결할땐 지우면 됨.
    public void createItems() {

        items = new ArrayList<>();
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("존맛탱파스타","13분", "#초보","12346"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
    }
    public void removeItem(int position) {
        try {
            items.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemChanged(position, items.size());
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

//    public static void setpictureImageView(){
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUSET);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUSET && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri selectedImageUri = data.getData();
//            try{
//
//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//
//                MylikeRecipeAdapter.ViewHolder.pictureImageView.setImageBitmap(bitmap);
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//
//        }
//    }
}