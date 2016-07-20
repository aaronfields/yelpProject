package ly.generalassemb.yelptwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

;


public class LikesActivity extends AppCompatActivity {
    LikesAdapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<Food> foodList;
    Button mSubmit;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users").child("username2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodList = new ArrayList<>();
//        mSubmit = (Button) findViewById(R.id.submit_button);
        for (int i = 0; i < 21; i++) {
            Food item = new Food(i + "", "", "");
            foodList.add(item);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LikesAdapter(foodList, LikesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        final List<Food> fList = new ArrayList<>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ADDED", "something was added:" + dataSnapshot.getKey());


                // This iterates through the different children of the given user and retrives
                // the vaules for my Food Class

                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    String url = dataSnapshot.child("foodPic").getValue().toString();
                    String name = dataSnapshot.child("restaurantName").getValue().toString();
                    String id = dataSnapshot.child("foodId").getValue().toString();
                    Food m = new Food(url, name, id);
                    fList.add(m);
                    i += 2;
                    Log.d("Food", "" + fList.size());
                    mAdapter = new LikesAdapter(fList, LikesActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };
        userRef.addChildEventListener(listener);


//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Food item = new Food("new url here", "new business name here", "new business id here");
//                //userRef.child("new user").setValue("liked");
//                String key = userRef.push().getKey().toString();
//
//                Map<String, Object> childUpdates = new HashMap<String, Object>();
//
//                childUpdates.put(key, item);
//                userRef.push().setValue(item);
//
//            }
//        });
    }


}
