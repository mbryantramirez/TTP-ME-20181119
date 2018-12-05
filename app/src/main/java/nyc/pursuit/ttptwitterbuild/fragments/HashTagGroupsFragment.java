package nyc.pursuit.ttptwitterbuild.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import nyc.pursuit.ttptwitterbuild.R;
import nyc.pursuit.ttptwitterbuild.adapters.HashTagGroupsAdapter;

public class HashTagGroupsFragment extends Fragment {
  // Always private and most likely final.
  private final List<String> hashTagArray = new ArrayList<>();
  private final RecyclerView groupRecyclerView;
  private final HashTagGroupsAdapter hashTagGroupsAdapter;

    /**
     * You should never create new Fragments--always get a newInstance:
     * https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
     */
  public static HashTagGroupsFragment newInstance() {
    return new HashTagGroupsFragment();
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View hashTagGroupsView = inflater.inflate(R.layout.fragment_hashtag_groups, container, false);

      /**
       * All this should be in onViewCreated.
       */
    initGroupList();

    groupRecyclerView = hashTagGroupsView.findViewById(R.id.rv_groups);
    groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity());
    hashTagGroupsAdapter = new HashTagGroupsAdapter(hashTagArray);
    groupRecyclerView.setAdapter(hashTagGroupsAdapter);

    return hashTagGroupsView;
  }

  private void initGroupList() {
    hashTagArray.add("ico");
    hashTagArray.add("ethereum");
    hashTagArray.add("crypto");
    hashTagArray.add("crowdfunding");
    hashTagArray.add("medicaid");
  }
}
