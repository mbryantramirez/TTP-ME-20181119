package nyc.pursuit.ttptwitterbuild;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.params.Geocode;
import java.util.List;
import nyc.pursuit.ttptwitterbuild.adapters.TweetAdapter;
import nyc.pursuit.ttptwitterbuild.utils.GPSTracker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "TWITTER";
  public static SearchService searchService;
  private RecyclerView tweetRecyclerView;
  private TweetAdapter tweetAdapter;
  public Double currentLat;
  public Double currentLong;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    initTwitter();
    getGPSLocation();
    getTweets(currentLat, currentLong);
  }

  private void init() {
    tweetRecyclerView = findViewById(R.id.rv_tweets);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    tweetRecyclerView.setLayoutManager(linearLayoutManager);
    tweetAdapter = new TweetAdapter();
    tweetRecyclerView.setAdapter(tweetAdapter);
  }

  private void initTwitter() {
    TwitterConfig config = new TwitterConfig.Builder(this)
        .logger(new DefaultLogger(Log.DEBUG))
        .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.ConsumerKey, BuildConfig.SecretKey))
        .debug(true)
        .build();
    Twitter.initialize(config);
    searchService = getTwitterSearchService();
  }

  private void getGPSLocation() {
    GPSTracker gpsTracker = new GPSTracker(this);
    if (gpsTracker.canGetLocation()) {
      currentLat = gpsTracker.getLatitude();
      currentLong = gpsTracker.getLongitude();
      Log.e(TAG, String.valueOf(currentLat));
      Log.e(TAG, String.valueOf(currentLong));
    } else {
      gpsTracker.showSettingsAlert();
    }
  }

  public static SearchService getTwitterSearchService() {
    final TwitterSession activeSession = TwitterCore.getInstance()
        .getSessionManager().getActiveSession();

    final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    final OkHttpClient customClient = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor).build();

    final TwitterApiClient customApiClient;

    customApiClient = new TwitterApiClient(customClient);
    TwitterCore.getInstance().addGuestApiClient(customApiClient);

    return TwitterCore.getInstance().getGuestApiClient().getSearchService();
  }

  public void getTweets(double latitude, double longitude) {

    Geocode geocode = new Geocode(latitude, longitude, 5, Geocode.Distance.MILES);

    Call<Search> call =
        searchService.tweets("q", geocode, null, null, null, 20, null, null, null, null);
    call.enqueue(new Callback<Search>() {
      @Override public void onResponse(Call<Search> call, Response<Search> response) {
        List<Tweet> tweets = response.body().tweets;
        Log.e(TAG, String.valueOf(tweets.size()));
        tweetAdapter.updateTweets(tweets);
        tweetAdapter.notifyDataSetChanged();
      }

      @Override public void onFailure(Call<Search> call, Throwable t) {

      }
    });
  }
}
