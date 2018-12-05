package nyc.pursuit.ttptwitterbuild;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.services.SearchService;
import nyc.pursuit.ttptwitterbuild.fragments.HashTagGroupsFragment;
import nyc.pursuit.ttptwitterbuild.fragments.TimeLineFragment;
import nyc.pursuit.ttptwitterbuild.utils.GPSTracker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

// Rusi notes
public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final String TAG = "GPS";
  public static SearchService searchService;
  public static Double currentLat;
  public static Double currentLong;
  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initDrawer();
    initTwitter();
    getGPSLocation();
    initFragment();
  }

  private void initFragment() {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    TimeLineFragment timeLineFragment = new TimeLineFragment();
    transaction.add(R.id.container_main, timeLineFragment, "main_feed");
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private void initDrawer() {
    drawerLayout = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    navigationView.setNavigationItemSelectedListener(this);
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

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    selectDrawerItem(menuItem);
    return true;
  }

  private void selectDrawerItem(MenuItem menuItem) {
    Fragment fragment = null;
    Class fragmentClass;
    if (menuItem.getItemId() == R.id.nav_groups) {
      fragmentClass = HashTagGroupsFragment.class;
    } else {
      fragmentClass = TimeLineFragment.class;
    }

    try {
      fragment = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.container_main, fragment).commit();



    menuItem.setChecked(true);
    drawerLayout.closeDrawers();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
