package lk.zeamac.app.powermart.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Adapter.FavoriteAdapter;
import lk.zeamac.app.powermart.Adapter.ProductViewAdapter;
import lk.zeamac.app.powermart.Entity.FavoriteEntity;
import lk.zeamac.app.powermart.Entity.ProductViewEntity;
import lk.zeamac.app.powermart.R;


public class FavoriteFragment extends Fragment {

    private RecyclerView.Adapter favouriteViewAdapter;
    private RecyclerView recyclerViewFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        ArrayList<FavoriteEntity> favorite_view_Items = new ArrayList<>();
        favorite_view_Items.add(new FavoriteEntity("chainsaw","Chainsaw","1000"));
        favorite_view_Items.add(new FavoriteEntity("grainder","grainde","1000"));
        favorite_view_Items.add(new FavoriteEntity("generators","Generator","2500"));
        favorite_view_Items.add(new FavoriteEntity("grainder2","Grainde","1000"));
        favorite_view_Items.add(new FavoriteEntity("chainsaw","Chainsaw","1000"));
        favorite_view_Items.add(new FavoriteEntity("grasscutter","Grass Cutter","2000"));



        recyclerViewFavorite= fragment.findViewById(R.id.favoriteView);
        recyclerViewFavorite.setLayoutManager(new GridLayoutManager(getActivity(),2));
        favouriteViewAdapter = new FavoriteAdapter(favorite_view_Items);
        recyclerViewFavorite.setAdapter(favouriteViewAdapter);
    }
}