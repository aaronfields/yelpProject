package ly.generalassemb.yelptwitter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.entities.Business;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class CheeseActivity extends AppCompatActivity {

    YelpAPI yelpAPI;
    String businessId;
    TextView textView;
    Call<Business> call;
    BusinessYelpApp businessForDisplay;
    CollapsingToolbarLayout collapsingToolbar;

    public static final String EXTRA_NAME = "restaurant_name";
    private static final String SEARCH_QUERY = "#truefoodkitchen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheese);

        //textView = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        businessId = intent.getStringExtra("name_id");

        yelpAPI = MainActivity.yelpAPI;
        call = yelpAPI.getBusiness(businessId);

        //textView.setText(businessId);

        new BusinessInfoTask().execute();

//        Intent intent = getIntent();
//        final String restaurantName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


//        loadBackdrop();

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(SEARCH_QUERY)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();

        ListView listView = (ListView) findViewById(R.id.twitter_list);

        listView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(new CustomPagerAdapter(this));

//        File myImageFile = new File("/path/to/image");
//        Uri myImageUri = Uri.fromFile(myImageFile);
//
//        TweetComposer.Builder builder = new TweetComposer.Builder(this)
//                .text("just setting up my Fabric.")
//                .image(myImageUri);
//        builder.show();

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < 16; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
//            imageView.setPadding(4, 4, 4, 4);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.hemingway));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }

    }

    private class BusinessInfoTask extends AsyncTask<Void, Void, BusinessYelpApp> {
        @Override
        protected BusinessYelpApp doInBackground(Void... voids) {

            Response<Business> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("SEARCH", "yelp: "+response.body());

            String address = "";
            for (int i = 0; i< response.body().location().displayAddress().size(); i++) {
                address += response.body().location().displayAddress().get(i) +"\n";
            }

            address = address.substring(0, (address.length() - 1));
            Log.d("SEARCH", "yelp: "+address);
            BusinessYelpApp business = new BusinessYelpApp(response.body().name(), response.body().ratingImgUrl(), address);

            return business;
        }

        @Override
        protected void onPostExecute(BusinessYelpApp business) {
            super.onPostExecute(business);

            businessForDisplay = business;
            collapsingToolbar.setTitle(businessForDisplay.getmName());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);
        return true;
    }

}


//    private void loadBackdrop() {
//        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);

        //Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sample_actions, menu);
//        return true;
//    }



//    public enum CustomPagerEnum {;
//
////        RED(R.string.red, R.layout.view_red),
////        BLUE(R.string.blue, R.layout.view_blue),
////        ORANGE(R.string.orange, R.layout.view_orange);
//
//        private int mTitleResId;
//        private int mLayoutResId;
//
//        CustomPagerEnum(int titleResId, int layoutResId) {
//            mTitleResId = titleResId;
//            mLayoutResId = layoutResId;
//        }
//
//        public int getTitleResId() {
//            return mTitleResId;
//        }
//
//        public int getLayoutResId() {
//            return mLayoutResId;
//        }
//
//    }
//
//
//    public class CustomPagerAdapter extends PagerAdapter {
//
//        private Context mContext;
//
//        public CustomPagerAdapter(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup collection, int position) {
//            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
//            collection.addView(layout);
//            return layout;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//
//        @Override
//        public int getCount() {
//            return CustomPagerEnum.values().length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//            return mContext.getString(customPagerEnum.getTitleResId());
//        }
//
//    }
//
//
//
//}
//
