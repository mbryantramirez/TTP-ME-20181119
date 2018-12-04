package nyc.pursuit.ttptwitterbuild.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.ArrayList;
import java.util.List;
import nyc.pursuit.ttptwitterbuild.R;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {

  private List<Tweet> tweetList = new ArrayList<>();

  public TweetAdapter() {
  }

  public void updateTweets(List<Tweet> tweetList) {
    this.tweetList.addAll(tweetList);
  }

  @NonNull @Override
  public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tweet, viewGroup, false);
    return new TweetViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull TweetViewHolder tweetViewHolder, int i) {
    Tweet tweet = tweetList.get(i);
    Picasso.get().load(tweet.user.profileImageUrl).into(tweetViewHolder.profilePic);
    tweetViewHolder.screenName.setText(tweet.inReplyToScreenName);
    tweetViewHolder.timeCreated.setText(tweet.createdAt);
    tweetViewHolder.status.setText(tweet.text);
  }

  @Override public int getItemCount() {
    return tweetList == null ? 0 : tweetList.size();
  }

  public class TweetViewHolder extends RecyclerView.ViewHolder {
    private ImageView profilePic;
    private TextView screenName;
    private TextView timeCreated;
    private TextView status;

    public TweetViewHolder(@NonNull View itemView) {
      super(itemView);
      profilePic = itemView.findViewById(R.id.iv_profile_pic);
      screenName = itemView.findViewById(R.id.tv_screename);
      timeCreated = itemView.findViewById(R.id.tv_timestamp);
      status = itemView.findViewById(R.id.tv_status);
    }
  }
}
