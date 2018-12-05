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
  // Static variables always at the top
  private static final String TAG = "TWITTER";

  private final SearchService searchService;
  private final TweetAdapter tweetAdapter;
  private String hashTagFilter;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    timelineView = inflater.inflate(R.layout.fragment_timeline, container, false);
    init(timelineView);
    searchService = MainActivity.searchService;
    getTweets(currentLat, currentLong);
    return timelineView;
  }

  public void getTweets(double latitude, double longitude) {
    Geocode geocode = new Geocode(latitude, longitude, 5, Geocode.Distance.MILES);

    String query = "";
    if (hashTagFilter != null) {
      query = hashTagFilter;
    }

    Call<Search> call = searchService.tweets(query, geocode, null, null, null, 20, null, null, null, null);
    call.enqueue(new Callback<Search>() {
      @Override public void onResponse(Call<Search> call, Response<Search> response) {
        List<Tweet> tweets = response.body().tweets;
        tweetAdapter.updateTweets(tweets);
        tweetAdapter.notifyDataSetChanged();    // Should use diffUtil:
        // https://medium.com/@iammert/using-diffutil-in-android-recyclerview-bdca8e4fbb00
      }

      @Override public void onFailure(Call<Search> call, Throwable t) {
        // Atleast log your failed call.
        // Ideally should show a Toast or Snackbar if something goes wrong.
      }
    });
  }

  public void setFilter(String text) {
    hashTagFilter = text;
  }

  // Pass in view if you're not using it anywhere else.
  // Private methods should always be below public ones.
  private void init(View view) {
    RecyclerView tweetRecyclerView = view.findViewById(R.id.rv_tweets);
    tweetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext());
    tweetRecyclerView.setAdapter(new TweetAdapter());
  }
}
