package com.sixsense.tangerine.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.MyRecipeListFragment;
import com.sixsense.tangerine.home.RecentRecipeListFragment;

import java.util.ArrayList;

public class MywrittenRecipeFragment extends Fragment {
//    public static MeV2Response sMyAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_activity_mywritten_recipe,container,false);

        MyRecipeListFragment myRecipeListFragment = new MyRecipeListFragment("written");

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.my_written_frame, myRecipeListFragment).addToBackStack(null).commit();

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.setting_activity_mywritten_recipe);
//
////        Bundle bundle = getIntent().getExtras();
////        sMyAccount = (MeV2Response) bundle.getSerializable("member");
//
//        MyRecipeListFragment myRecipeListFragment = new MyRecipeListFragment("written");
//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.my_written_frame, myRecipeListFragment).addToBackStack(null).commit();
//    }
}