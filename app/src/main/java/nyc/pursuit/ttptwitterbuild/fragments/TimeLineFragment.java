package nyc.pursuit.ttptwitterbuild.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.params.Geocode;
import java.util.List;
import nyc.pursuit.ttptwitterbuild.MainActivity;
import nyc.pursuit.ttptwitterbuild.R;
import nyc.pursuit.ttptwitterbuild.adapters.TweetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nyc.pursuit.ttptwitterbuild.MainActivity.currentLat;
import static nyc.pursuit.ttptwitterbuild.MainActivity.currentLong;

public class TimeLineFragment extends Fragment {

  SearchService searchService;
  private TweetAdapter tweetAdapter;
  private View timelineView;
  private static final String TAG = "TWITTER";
  String hashTagFilter;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    timelineView = inflater.inflate(R.layout.fragment_timeline, container, false);
    init();
    searchService = MainActivity.searchService;
    getTweets(currentLat, currentLong);
    return timelineView;
  }

  private void init() {
    RecyclerView tweetRecyclerView = timelineView.findViewById(R.id.rv_tweets);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    tweetRecyclerView.setLayoutManager(linearLayoutManager);
    tweetAdapter = new TweetAdapter();
    tweetRecyclerView.setAdapter(tweetAdapter);
  }

  public void getTweets(double latitude, double longitude) {

    Geocode geocode = new Geocode(latitude, longitude, 5, Geocode.Distance.MILES);

    String query = "";

    if (hashTagFilter != null) {
      query = hashTagFilter;
    }

    Call<Search> call =
        searchService.tweets(query, geocode, null, null, null, 20, null, null, null, null);
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

  public void setFilter(String text) {
    hashTagFilter = text;
  }
}
