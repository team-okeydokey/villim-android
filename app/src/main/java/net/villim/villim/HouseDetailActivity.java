package net.villim.villim;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.CalendarActivity.END_DATE;
import static net.villim.villim.CalendarActivity.START_DATE;
import static net.villim.villim.HouseDescriptionActivity.KEY_BASIC_DESCRIPTION;
import static net.villim.villim.MainActivity.DATE_SELECTED;
import static net.villim.villim.VillimKeys.HOUSE_INFO_URL;
import static net.villim.villim.VillimKeys.KEY_ABOUT;
import static net.villim.villim.VillimKeys.KEY_ADDITIONAL_GUEST_FEE;
import static net.villim.villim.VillimKeys.KEY_ADDR_DIRECTION;
import static net.villim.villim.VillimKeys.KEY_ADDR_SUMMARY;
import static net.villim.villim.VillimKeys.KEY_AMENITY_IDS;
import static net.villim.villim.VillimKeys.KEY_CANCELLATION_POLICY;
import static net.villim.villim.VillimKeys.KEY_CLEANING_FEE;
import static net.villim.villim.VillimKeys.KEY_DEPOSIT;
import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOST_NAME;
import static net.villim.villim.VillimKeys.KEY_HOST_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_INFO;
import static net.villim.villim.VillimKeys.KEY_HOUSE_PIC_URLS;
import static net.villim.villim.VillimKeys.KEY_HOUSE_POLICY;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_NIGHT;
import static net.villim.villim.VillimKeys.KEY_REVIEW_LAST_CONTENT;
import static net.villim.villim.VillimKeys.KEY_REVIEW_LAST_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_REVIEW_LAST_RATING;
import static net.villim.villim.VillimKeys.KEY_REVIEW_LAST_REVIEWER;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;


public class HouseDetailActivity extends VillimActivity implements OnMapReadyCallback {

    private final static int MAX_AMENITY_ICONS = 6;

    private VillimHouse house;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private RelativeLayout hostInfo;
    private ImageView hostProfilePic;
    private TextView hostName;
    private RatingBar hostRating;
    private TextView hostReviewCount;
    private Button contactHostButton;

    private TextView houseName;
    private TextView houseAddress;

    private TextView numGuest;
    private TextView numBedroom;
    private TextView numBed;
    private TextView numBathroom;

    private TextView description;
    private Button descriptionSeeMore;

    private TextView pricePolicyRead;

    private LinearLayoutCompat amenityIcons;

    private RelativeLayout reviewBody;
    private ImageView reviewerProfilePic;
    private TextView reviewerName;
    private RatingBar reviewerRating;
    private TextView reviewContent;
    private TextView seeMoreReviews;
    private RatingBar houseRating;

    private MapFragment mapFragment;

    private TextView housePolicyRead;
    private TextView refundPolicyRead;

    private LinearLayoutCompat reserveButton;
    private TextView reserveButtonPrice;
    private ImageView coinImageView;

    private AVLoadingIndicatorView loadingIndicator;

    private boolean dateSelected;
    private Date startDate;
    private Date endDate;

    private String lastReviewContent;
    private String lastReviewReviewer;
    private String lastReviewProfilePictureUrl;
    private float lastReviewRating;

    private GoogleMap map;
    /* We set these variables because we don't know which will occur first. */
    private boolean markerPlaced;
    private boolean dataDownloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        markerPlaced = false;
        dataDownloaded = false;

        /* Extract room info and fill view elements with data */
        extractRoomInfo();

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);

        /* Toolbar. */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_light));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        /* Change back button color on collapse. */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -appBarLayout.getTotalScrollRange()) { // Completely collapsed.
//                    collapsingToolbarLayout.setTitle(getString(R.string.room_detail_title));
                    toolbarTitle.setVisibility(View.VISIBLE);
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_dark), PorterDuff.Mode.SRC_ATOP);
                } else {
//                    collapsingToolbarLayout.setTitle(" ");
                    toolbarTitle.setVisibility(View.INVISIBLE);
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_light), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        /* View elements containing room info. */

        /* Host Info */
        hostInfo = (RelativeLayout) findViewById(R.id.host_info);
        hostProfilePic = (ImageView) findViewById(R.id.host_profile_pic);
        hostName = (TextView) findViewById(R.id.host_name);
        hostRating = (RatingBar) findViewById(R.id.host_review_rating);
        hostReviewCount = (TextView) findViewById(R.id.host_review_count);
        contactHostButton = (Button) findViewById(R.id.contact_host_button);
        contactHostButton.setVisibility(View.GONE); // For now.

        /* House Name and address */
        houseName = (TextView) findViewById(R.id.house_name);
        houseAddress = (TextView) findViewById(R.id.house_address);

        /* Icons with house info */
        numGuest = (TextView) findViewById(R.id.num_guest);
        numBedroom = (TextView) findViewById(R.id.num_bedroom);
        numBed = (TextView) findViewById(R.id.num_bed);
        numBathroom = (TextView) findViewById(R.id.num_bathroom);

        /* House description */
        description = (TextView) findViewById(R.id.description);
        descriptionSeeMore = (Button) findViewById(R.id.description_see_more);

        /* Price Policy */
        pricePolicyRead = (TextView) findViewById(R.id.price_policy_read);

        /* Amenities */
        amenityIcons = (LinearLayoutCompat) findViewById(R.id.amenity_icons);

        /* Review */
        reviewBody = (RelativeLayout) findViewById(R.id.review_body);
        reviewerProfilePic = (ImageView) findViewById(R.id.reviewer_profile_pic);
        reviewerName = (TextView) findViewById(R.id.reviewer_name);
        reviewerRating = (RatingBar) findViewById(R.id.reviewer_rating);
        reviewContent = (TextView) findViewById(R.id.review_content);
        seeMoreReviews = (TextView) findViewById(R.id.see_more_reviews);
        houseRating = (RatingBar) findViewById(R.id.house_rating);

        /* Map */
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* House & Refund Policy */
        housePolicyRead = (TextView) findViewById(R.id.house_policy_read);
        refundPolicyRead = (TextView) findViewById(R.id.refund_policy_read);

        /* Bottom Button */
        reserveButton = (LinearLayoutCompat) findViewById(R.id.reserve_button);
        reserveButtonPrice = (TextView) findViewById(R.id.reserve_button_price);
        coinImageView = (ImageView) findViewById(R.id.coin_image);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        sendHouseInfoRequest();
    }

    // Extract room info.
    private void extractRoomInfo() {
        Bundle args = getIntent().getExtras();
        house = args.getParcelable(getString(R.string.key_house));
        dateSelected = args.getBoolean(DATE_SELECTED, false);
        if (dateSelected) {
            startDate = (Date) args.getSerializable(CalendarActivity.START_DATE);
            endDate = (Date) args.getSerializable(CalendarActivity.END_DATE);
        }
    }

    public void sendHouseInfoRequest() {
        startLoadingAnimation();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


//        URL url = new HttpUrl.Builder()
//                .scheme(SERVER_SCHEME)
//                .host(SERVER_HOST)
//                .addPathSegments("a/host-info")
//                .addQueryParameter(KEY_HOST_ID, getIntent().getStringExtra(KEY_HOST_ID))
//                .build().url();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(HOUSE_INFO_URL)
                .addQueryParameter(KEY_HOUSE_ID, Integer.toString(house.houseId))
                .build().url();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong.
                showErrorMessage(getString(R.string.cant_connect_to_server));
                stopLoadingAnimation();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    throw new IOException("Response not successful   " + response);
                }
                /* Request success. */
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
                        /* Set host picture */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    house = VillimHouse.createHouseFromJSONObject(jsonObject.getJSONObject(KEY_HOUSE_INFO));
                                    dataDownloaded = true;
                                    lastReviewContent = jsonObject.getJSONObject(KEY_HOUSE_INFO).getString(KEY_REVIEW_LAST_CONTENT);
                                    lastReviewReviewer = jsonObject.getJSONObject(KEY_HOUSE_INFO).getString(KEY_REVIEW_LAST_REVIEWER);
                                    lastReviewProfilePictureUrl = jsonObject.getJSONObject(KEY_HOUSE_INFO).getString(KEY_REVIEW_LAST_PROFILE_PIC_URL);
                                    lastReviewRating = (float) jsonObject.getJSONObject(KEY_HOUSE_INFO).getDouble(KEY_REVIEW_LAST_RATING);
                                    populateView();
                                } catch (JSONException e) {
                                    showErrorMessage(getString(R.string.server_error));
                                    stopLoadingAnimation();
                                }
                            }
                        });

                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                }
            }
        });
    }


    // Make this async.
    private void populateView() {
        /* House picture */
        if (house.housePicUrls.length > 0) {
            toolbarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HouseDetailActivity.this, GalleryActivity.class);
                    intent.putExtra(KEY_HOUSE_PIC_URLS, house.housePicUrls);
                    startActivity(intent);
                }
            });
        }
        Glide.with(this)
                .load(house.houseThumbnailUrl)
                .into(toolbarImage);

        /* Add callback to post profile activity */
        hostInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, HostProfileActivity.class);
                intent.putExtra(KEY_HOST_ID, house.hostId);
                intent.putExtra(KEY_HOST_NAME, house.hostName);
                intent.putExtra(KEY_ADDR_SUMMARY, house.addrSummary);
                intent.putExtra(KEY_HOST_PROFILE_PIC_URL, house.hostProfilePicUrl);
                startActivity(intent);
            }
        });

        /* Host profile pic */
        Glide.with(this)
                .load(house.hostProfilePicUrl)
                .into(hostProfilePic);

        /* Host name and rating */
        hostName.setText(house.hostName);
        hostRating.setRating(house.hostRating);
        String countText = String.format(getString(R.string.review_count_text), house.hostReviewCount);
        hostReviewCount.setText(countText);

        /* House Name and address */
        houseName.setText(house.houseName);
        houseAddress.setText(house.addrSummary);

        /* Icons with house info */
        String numGuestText = String.format(getString(R.string.num_guest_format, house.numGuest));
        numGuest.setText(numGuestText);
        String numBedroomText = String.format(getString(R.string.num_bedroom_format, house.numBedroom));
        numBedroom.setText(numBedroomText);
        String numBedText = String.format(getString(R.string.num_bed_format, house.numBed));
        numBed.setText(numBedText);
        String numBathroomText = String.format(getString(R.string.num_bathroom_format, house.numBathroom));
        numBathroom.setText(numBathroomText);

        /* Description */
        description.setText(house.description.trim());
        description.post(new Runnable() {
            @Override
            public void run() {
                if (description.getLineCount() > description.getMaxLines()) {
                    descriptionSeeMore.setVisibility(View.VISIBLE);
                } else {
                    descriptionSeeMore.setVisibility(View.GONE);
                    description.setPadding(
                            description.getPaddingLeft(),
                            description.getPaddingTop(),
                            description.getPaddingRight(),
                            getResources().getDimensionPixelSize(R.dimen.description_see_more_bottom_padding));
                }

            }
        });
        descriptionSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, HouseDescriptionActivity.class);
                intent.putExtra(KEY_BASIC_DESCRIPTION, house.description);
                startActivity(intent);
            }
        });

        /* Price Policy */
        pricePolicyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, PricePolicyActivity.class);
                intent.putExtra(KEY_RATE_PER_NIGHT, house.ratePerNight);
                intent.putExtra(KEY_DEPOSIT, house.deposit);
                intent.putExtra(KEY_ADDITIONAL_GUEST_FEE, house.additionalGuestFee);
                intent.putExtra(KEY_CLEANING_FEE, house.cleaningFee);
                startActivity(intent);
            }
        });

        /* Amenities */
        populateAmenityIcons();

        /* Reviews */
        populateReviews();

        /* Map */
        if (map != null && !markerPlaced) {
            map.addMarker(new MarkerOptions().position(new LatLng(house.latitude, house.longitude)));
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(house.latitude, house.longitude)));
            map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(HouseDetailActivity.this, FullScreenMapActivity.class);
                    intent.putExtra(KEY_LATITUDE, house.latitude);
                    intent.putExtra(KEY_LONGITUDE, house.longitude);
                    intent.putExtra(KEY_ADDR_DIRECTION, house.addrDirection);
                    startActivity(intent);
                }
            });
        }

        /* House & Refund Policy */
        housePolicyRead = (TextView) findViewById(R.id.house_policy_read);
        housePolicyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, HousePolicyActivity.class);
                intent.putExtra(KEY_HOUSE_POLICY, house.housePolicy);
                startActivity(intent);
            }
        });

        refundPolicyRead = (TextView) findViewById(R.id.refund_policy_read);
        refundPolicyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, CancellationPolicyActivity.class);
                intent.putExtra(KEY_CANCELLATION_POLICY, house.cancellationPolicy);
                startActivity(intent);
            }
        });

        /* Bottom Button */
        int numberOfNights;
        if (dateSelected) {
            numberOfNights = VillimUtil.daysBetween(startDate, endDate);
        } else {
            /* 선택된 날짜가 없을 떈 30일로 계산 */
            numberOfNights = 30;
        }
        int price = numberOfNights * house.ratePerNight;
        String priceString = NumberFormat.getIntegerInstance().format(price);
        String priceText = String.format(getString(R.string.price_text_format), priceString, numberOfNights);
        reserveButtonPrice.setText(priceText);

        /* Set drawable to bottom button. */
        Glide.with(this).load(R.drawable.icon_money).into(coinImageView);

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, ReservationActivity.class);
                intent.putExtra(getString(R.string.key_house), house);
                intent.putExtra(DATE_SELECTED, dateSelected);
                if (dateSelected) {
                    intent.putExtra(START_DATE, startDate);
                    intent.putExtra(END_DATE, endDate);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (dataDownloaded) {
            markerPlaced = true;
            map.addMarker(new MarkerOptions().position(new LatLng(house.latitude, house.longitude)));
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(house.latitude, house.longitude)));
            map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(HouseDetailActivity.this, FullScreenMapActivity.class);
                    intent.putExtra(KEY_LATITUDE, house.latitude);
                    intent.putExtra(KEY_LONGITUDE, house.longitude);
                    intent.putExtra(KEY_ADDR_DIRECTION, house.addrDirection);
                    startActivity(intent);
                }
            });
        }
    }

    private class PopulateHouseDetailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            populateView();
            return null;
        }

        protected void onProgressUpdate(Void... voids) {

        }

        protected void onPostExecute() {

        }
    }

    private void populateAmenityIcons() {
        int[] amenityIds = house.amenityIds;

        /* Displayed icons should be one less than MAX_AMENITY_ICONS if there are more amenities
           than MAX_AMENITY_ICONS, to make room for the "see more" button. */
        int numIcons = amenityIds.length > MAX_AMENITY_ICONS ? MAX_AMENITY_ICONS - 1 : amenityIds.length;

        /* Add icons. */
        for (int i = 0; i < numIcons; ++i) {
            ImageView iconView = new ImageView(this);
            iconView.setImageResource(VillimAmenity.getAmenityDrawableResourceId(amenityIds[i]));
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    (int) getResources().getDimension(R.dimen.amenity_icon_width),
                    (int) getResources().getDimension(R.dimen.amenity_icon_height),
                    1.0f
            );
            iconView.setLayoutParams(params);
            amenityIcons.addView(iconView);
        }

        /* Add "see more" button, if applicable. */
        if (amenityIds.length > MAX_AMENITY_ICONS) {
            TextView seeMoreTextView = new TextView(this);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    (int) getResources().getDimension(R.dimen.amenity_icon_width),
                    (int) getResources().getDimension(R.dimen.amenity_icon_height),
                    1.0f
            );
            seeMoreTextView.setLayoutParams(params);
            String seeMoreText = String.format(getString(R.string.amenities_see_more_format), amenityIds.length - numIcons);
            seeMoreTextView.setText(seeMoreText);
            seeMoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            seeMoreTextView.setGravity(Gravity.CENTER_VERTICAL);
            seeMoreTextView.setTextColor(getResources().getColor(R.color.see_more));
            seeMoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            seeMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HouseDetailActivity.this, AmenityActivity.class);
                    intent.putExtra(KEY_AMENITY_IDS, house.amenityIds);
                    startActivity(intent);
                }
            });
            amenityIcons.addView(seeMoreTextView);
        }

    }

    private void populateReviews() {


        if (house.houseReviewCount == 0) {
            /* Remove the whole review section andreplace it with "No review" textview. */
            LinearLayoutCompat parent = (LinearLayoutCompat) reviewBody.getParent();
            int index = parent.indexOfChild(reviewBody);
            parent.removeView(reviewBody);

            TextView noReviewTextView = new TextView(this);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            noReviewTextView.setLayoutParams(params);
            noReviewTextView.setText(getString(R.string.reviews_no_review));
            noReviewTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noReviewTextView.setGravity(Gravity.CENTER_VERTICAL);
            noReviewTextView.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.room_detail_activity_padding));
            parent.addView(noReviewTextView, index);

        } else {

            /* There is at least 1 review. Load reviewer info. */
            Glide.with(this)
                    .load(lastReviewProfilePictureUrl)
                    .into(reviewerProfilePic);
            reviewerName.setText(lastReviewReviewer);
            reviewerRating.setRating(lastReviewRating);
            reviewContent.setText(lastReviewContent);
            houseRating.setRating(house.houseRating);

            if (house.houseReviewCount == 1) {
                /* Be away with "see more" if there is only one review. Also, shift house rating bar to the left. */
                seeMoreReviews.setVisibility(View.GONE);
                RelativeLayout.LayoutParams houseRatingParams = (RelativeLayout.LayoutParams) houseRating.getLayoutParams();
                houseRatingParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                houseRating.setLayoutParams(houseRatingParams);
            } else {
                /* Activate "see more" button with appropriate text. */
                String seeMoreReviewsText = String.format(getString(R.string.reviews_see_more_format), house.houseReviewCount - 1);
                seeMoreReviews.setText(seeMoreReviewsText);
                seeMoreReviews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HouseDetailActivity.this, ViewReviewActivity.class);
                        intent.putExtra(KEY_HOUSE_ID, house.houseId);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void startLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToShow();
            }
        });
    }

    public void stopLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToHide();
            }
        });
    }

    public void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
