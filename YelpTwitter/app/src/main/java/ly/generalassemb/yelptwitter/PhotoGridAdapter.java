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
 * Created by brendan on 7/18/16.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.myViewHolder> {
    LayoutInflater inflater;
    List<Food> foodList;
    Context context;

    public PhotoGridAdapter(List<Food> list, Context context) {
        this.foodList = list;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.grid_view_item,
                parent, false);
        myViewHolder holder = new myViewHolder(v, context, foodList);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        //TODO: setup layout and figure out how we want to display our images

        //Place holder image for testing

//        holder.mImage.setImageResource(R.mipmap.ic_launcher);
//        holder.mName.setText(foodList.get(position).getFoodPic());
        holder.putThePhoto(foodList.get(position).getFoodPic());



    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // setting the ViewHolder for my recyclerview with a clickListener

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImage;
        public TextView mName;
        List<Food> foodList = new ArrayList<>();
        Context context;

        public myViewHolder(View itemView, Context context, List<Food> food) {
            super(itemView);
            this.foodList = food;
            this.context = context;

            itemView.setOnClickListener(this);
            mImage = (ImageView) itemView.findViewById(R.id.imageView);
           // mName = (TextView) itemView.findViewById(R.id.textView);

        }

        public void putThePhoto(String url){
            Picasso.with(itemView.getContext())
                    .load(url)
                    .into(mImage);
        }

        // telling my ClickListener what to do
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Food food = ResultsSingleton.getInstance().getFoodAtPosition(position);
            Intent intent = new Intent(this.context, CheeseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name_id", food.getFoodId());
            bundle.putString("image_url", food.getFoodPic());
            intent.putExtras(bundle);
            this.context.startActivity(intent);
        }
    }


}
