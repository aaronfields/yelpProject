package ly.generalassemb.yelptwitter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.entities.Business;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class CheeseActivity extends AppCompatActivity {

    YelpAPI yelpAPI;
    String businessId;
    String imageURL;
    TextView textView;
    Call<Business> call;
    BusinessYelpApp businessForDisplay;
    CollapsingToolbarLayout collapsingToolbar;
    ArrayList<String> imageUrls;

    public static final String EXTRA_NAME = "restaurant_name";
    private static final String SEARCH_QUERY = "#truefoodkitchen";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users").child(ResultsSingleton.getInstance().getUserName());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheese);

        Intent intent = getIntent();
        businessId = intent.getStringExtra("name_id");
        imageURL = intent.getStringExtra("image_url");


        yelpAPI = MainActivity.yelpAPI;
        call = yelpAPI.getBusiness(businessId);

        new BusinessInfoTask().execute();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

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

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.yelp.com/biz_photos/" + businessId + "?tab=food").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements all = doc.getAllElements();
            Log.d("RESPONSE", "154: " + all.toString());
            Pattern p = Pattern.compile("(?is)\"src_high_res\": \"(.+?)\"");
            Matcher m = p.matcher(all.toString());
            imageUrls = new ArrayList<>();
            int i = 0;
            while (m.find() && (i < 16)) {
                imageUrls.add("http:"+m.group(1));
                i++;
            }

            return business;
        }

        @Override
        protected void onPostExecute(BusinessYelpApp business) {
            super.onPostExecute(business);

            businessForDisplay = business;
            collapsingToolbar.setTitle(businessForDisplay.getmName());

            LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
            for (int i = 0; i < imageUrls.size(); i++) {
                ImageView imageView = new ImageView(CheeseActivity.this);
                imageView.setId(i);
//
                int height= layout.getMeasuredHeight();
                layout.addView(imageView, height, height);
                Picasso.with(CheeseActivity.this)
                        .load(imageUrls.get(i))
                        .resize(height, height)
                        .centerCrop()
                        .into(imageView);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                // Create a Food Object and add it to the current User
                 Food likedObject = new Food(imageURL,businessId,"");
                 userRef.push().setValue(likedObject);


                Toast.makeText(CheeseActivity.this, "Added!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu:
                //TODO: If there's a menu link, provide it
                break;
            case R.id.action_map:
                //TODO: Go to google maps with address
                double mLatitude = ResultsSingleton.getInstance().getLatitude();
                double mLongitude = ResultsSingleton.getInstance().getLatitude();
                Intent mapIntent = new Intent(CheeseActivity.this, LikesActivity.class);
                mapIntent.putExtra("latitude", mLatitude);
                mapIntent.putExtra("longitude", mLongitude);
                break;
            case R.id.action_likes:
                Intent intent = new Intent(CheeseActivity.this, LikesActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}

