package ly.generalassemb.yelptwitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendan on 7/19/16.
 */
public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.likesViewHolder> {


    LayoutInflater inflater;
    List<Food> foodList;
    Context context;
    public LikesAdapter(List<Food> list, Context context) {
        this.foodList = list;
        this.context = context;
    }

    @Override
    public likesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.likes_card_item,
                parent, false);
        likesViewHolder holder = new likesViewHolder(v, context, foodList);
        return holder;
    }

    @Override
    public void onBindViewHolder(likesViewHolder holder, int position) {

        //TODO: setup layout and figure out how we want to display our images
        String imgURL = foodList.get(position).getFoodPic();
        Picasso.with(this.context)
                .load(imgURL)
                .into(holder.mImage);
        holder.mName.setText(foodList.get(position).getRestaurantName());
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // setting the ViewHolder for my recyclerview with a clickListener

    public class likesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImage;
        public TextView mName;
        List<Food> foodList = new ArrayList<>();
        Context context;

        public likesViewHolder(View itemView, Context context, List<Food> food) {
            super(itemView);
            this.foodList = food;
            this.context = context;

            itemView.setOnClickListener(this);
            mImage = (ImageView) itemView.findViewById(R.id.food_image);
            mName = (TextView) itemView.findViewById(R.id.business_name);

        }

        // telling my ClickListener what to do
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // TODO: set up detail activity to hande clicks

            Food food = ResultsSingleton.getInstance().getFoodAtPosition(position);
            Intent intent = new Intent(this.context, CheeseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("rest_name", food.getRestaurantName());
            bundle.putString("image_url", food.getFoodPic());
            intent.putExtras(bundle);
            this.context.startActivity(intent);
            }

        }
    }


