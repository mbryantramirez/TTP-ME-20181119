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

  private List<String> hashTagArray = new ArrayList<>();
  RecyclerView groupRecyclerView;
  HashTagGroupsAdapter hashTagGroupsAdapter;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View hashTagGroupsView = inflater.inflate(R.layout.fragment_hashtag_groups, container, false);
    initGroupList();
    groupRecyclerView = hashTagGroupsView.findViewById(R.id.rv_groups);
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(getActivity());
    groupRecyclerView.setLayoutManager(linearLayoutManager);
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
