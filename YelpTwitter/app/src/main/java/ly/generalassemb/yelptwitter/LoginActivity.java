package ly.generalassemb.yelptwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Uzr84VNnQjD7RJQnXneJTPifp";
    private static final String TWITTER_SECRET = "XEo4SwCTB85HSe2bw65HSJ45nbHMIObVgjQddJDILzDaYtOXWT";
    private TwitterLoginButton loginButton;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERID = "userid";


    private String twitter_username;
    private long twitter_userId;
    private String twitter_image_url;
    private String twitter_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        setContentView(R.layout.activity_login);



//        Intent intent = new Intent(LoginActivity.this, CheeseActivity.class);
//        startActivity(intent);

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d("TAG", "onAuthStateChanged:signed_out");
//                }
//                // [START_EXCLUDE]
//                //updateUI(user);
//                // [END_EXCLUDE]
//            }
//        };

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                twitter_username = session.getUserName();
                twitter_userId = session.getUserId();
                twitter_image_url = " https://twitter.com/" + twitter_username + "/profile_image?size=original";

//                Intent intent = new Intent(LoginActivity.this, CheeseActivity.class);
//                intent.putExtra(KEY_USERNAME, twitter_username);
//                intent.putExtra(KEY_USERID, twitter_userId);
//                startActivity(intent);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("username",twitter_username);
                returnIntent.putExtra("twitterId", twitter_userId);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();



            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                //updateUI(null);
            }
        });

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

//    private void handleTwitterSession(TwitterSession session) {
//        Log.d("TAG", "handleTwitterSession:" + session);
//        // [START_EXCLUDE silent]
//        //showProgressDialog();
//        // [END_EXCLUDE]
//
//        AuthCredential credential = TwitterAuthProvider.getCredential(
//                session.getAuthToken().token,
//                session.getAuthToken().secret);
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w("TAG", "signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
////                        // [START_EXCLUDE]
////                        hideProgressDialog();
////                        // [END_EXCLUDE]
//                    }
//                });
//    }
    // [END auth_with_twitter]

//    private void signOut() {
//        mAuth.signOut();
//        Twitter.logOut();
//
//        updateUI(null);
//    }

//    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.twitter_status_fmt, user.getDisplayName()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.button_twitter_login).setVisibility(View.GONE);
//            findViewById(R.id.button_twitter_signout).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.button_twitter_login).setVisibility(View.VISIBLE);
//            findViewById(R.id.button_twitter_signout).setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button_twitter_signout:
//                signOut();
//                break;
//        }
//    }



}
