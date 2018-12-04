package nyc.pursuit.ttptwitterbuild.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import nyc.pursuit.ttptwitterbuild.R;
import nyc.pursuit.ttptwitterbuild.fragments.TimeLineFragment;

public class HashTagGroupsAdapter
    extends RecyclerView.Adapter<HashTagGroupsAdapter.HashTagGroupsViewHolder> {

  List<String> tagGroupList;
  Context context;

  public HashTagGroupsAdapter(List<String> tagGroupList) {
    this.tagGroupList = tagGroupList;
  }

  public void addGroups(List<String> tagGroupList) {
    this.tagGroupList.addAll(tagGroupList);
  }

  @NonNull @Override
  public HashTagGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_hashtag, viewGroup, false);
    context = viewGroup.getContext();
    return new HashTagGroupsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull HashTagGroupsViewHolder hashTagGroupsViewHolder, int i) {
    hashTagGroupsViewHolder.textView.setText(tagGroupList.get(i));
    hashTagGroupsViewHolder.button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String text = tagGroupList.get(i);
        FragmentManager manager =
            ((FragmentActivity) v.getContext()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TimeLineFragment timeLineFragment = new TimeLineFragment();
        timeLineFragment.setFilter(text);
        transaction.replace(R.id.container_main, timeLineFragment, "group_feed");
        transaction.addToBackStack(null);
        transaction.commit();
      }
    });
  }

  @Override public int getItemCount() {
    return tagGroupList == null ? 0 : tagGroupList.size();
  }

  public class HashTagGroupsViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    private TextView textView;
    private Button button;

    public HashTagGroupsViewHolder(@NonNull View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.tv_group_name);
      button = itemView.findViewById(R.id.button_group);
    }

    @Override public void onClick(View v) {

    }
  }
}
