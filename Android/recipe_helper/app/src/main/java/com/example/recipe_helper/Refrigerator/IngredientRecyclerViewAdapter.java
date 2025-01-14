package com.example.recipe_helper.Refrigerator;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.realm.Realm;

import static com.example.recipe_helper.R.drawable.ic_apple;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.Holder> {

    private ArrayList<IngredientData> arrayList = new ArrayList<IngredientData>();
    private Context context;
    private Realm realm;

    public IngredientRecyclerViewAdapter(Context context, ArrayList<IngredientData> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_recycler_ingredient_item, parent, false);
        realm = Realm.getDefaultInstance();
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String mDrawableName;

        if (arrayList.get(position).image.equals("-")) {
            mDrawableName = "ic_" + "diet";
        } else {
            mDrawableName = "ic_" + arrayList.get(position).image;
        }

        int resId = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());

        holder.ingredient_image.setImageDrawable(context.getResources().getDrawable(resId));
        holder.ingredient_name.setText(arrayList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView ingredient_image;
        protected TextView ingredient_name;
        protected ImageView delete_button;

        public Holder(@NonNull final View itemView) {
            super(itemView);
            this.ingredient_image = (ImageView) itemView.findViewById(R.id.ingredient_image);
            this.ingredient_name = (TextView) itemView.findViewById(R.id.ingredient_name);
            this.delete_button = (ImageView) itemView.findViewById(R.id.delete_button);

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            IngredientData ingredientData = realm.where(IngredientData.class).equalTo("name", arrayList.get(position).name).findFirst();
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            ingredientData.deleteFromRealm();
                        }
                    });
                }
            });
        }

    }
}
