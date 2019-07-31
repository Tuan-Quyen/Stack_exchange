package com.android.quyentraining.fragments.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.SelectedLocationItemAdapter;
import com.android.quyentraining.adapters.TrackedWayItemAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.interfaces.ItemAllSiteListener;
import com.android.quyentraining.interfaces.ItemFeedCallBack;
import com.android.quyentraining.interfaces.ItemQuestionListener;
import com.android.quyentraining.interfaces.ItemYourSiteListener;
import com.android.quyentraining.interfaces.SaveChooseCallBack;
import com.android.quyentraining.models.ItemEndLocation;
import com.android.quyentraining.models.ItemLatlng;
import com.android.quyentraining.models.places.ItemResultPlace;
import com.android.quyentraining.models.places.ObjectPlace;
import com.android.quyentraining.models.User;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener,
        ItemAllSiteListener, ItemFeedCallBack, ItemQuestionListener, SaveChooseCallBack, ItemYourSiteListener, MapView {
    @BindViews({R.id.frMap_imvSearch, R.id.frMap_imvClearText, R.id.frMap_btndirection, R.id.frMap_userImage})
    List<ImageView> imvList;
    @BindViews({R.id.frMap_btnTurnTrackingLocation, R.id.frMap_btnOffTrackingLocation, R.id.frMap_btnGpsLocation})
    List<FloatingActionButton> btnFloatList;
    @BindViews({R.id.frMap_rvSelectedLocation, R.id.frMap_rvWayLocation})
    List<RecyclerView> rvList;
    @BindView(R.id.frMap_etSearch)
    AutoCompleteTextView mSearchText;
    @BindView(R.id.frMap_userName)
    TextView userName;
    @BindView(R.id.frMap_drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.frMap_Refresh)
    SwipeRefreshLayout refreshLayout;
    private GoogleMap mgoogleMap;
    private LocationManager locationManager;
    private LatLng latLngCurrent, latLngMarker;
    private GoogleApiClient mgoogleApiClient;
    private List<ObjectPlace> objectPlaceNearByList = new ArrayList<>();
    private List<String> locationChooseList = new ArrayList<>(), wayTrackingList = new ArrayList<>();
    private List<LatLng> latLngList = new ArrayList<>();
    private String sequense, userID;
    private boolean isGPS, isNetwork, isDrawed, isTurnOn;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private long radius = 1000, zoomCamera = 15;
    private SelectedLocationItemAdapter selectedLocationItemAdapter;
    private TrackedWayItemAdapter trackedWayItemAdapter;
    AppDialog appDialog = new AppDialog();
    MapPresenter mapPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locationmap, container, false);
        ButterKnife.bind(this, view);
        bindView();
        getListFromFireBase(userID);
        getUserInfo(user);
        return view;
    }

    public void bindView() {
        mapPresenter = new MapPresenter(this.getContext(), this.getActivity(), this);
        locationManager = (LocationManager) getActivity().getSystemService(this.getContext().LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        mgoogleApiClient = new GoogleApiClient.Builder(this.getContext()).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this.getActivity(), this).build();
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.framelocation_map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        //navigation list selected location
        selectedLocationItemAdapter = new SelectedLocationItemAdapter(locationChooseList);
        rvList.get(0).setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvList.get(0).setNestedScrollingEnabled(true);
        selectedLocationItemAdapter.setItemAllSiteListener(this, this);
        rvList.get(0).setAdapter(selectedLocationItemAdapter);
        //navigation list way tracked
        trackedWayItemAdapter = new TrackedWayItemAdapter(wayTrackingList);
        rvList.get(1).setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvList.get(1).setNestedScrollingEnabled(true);
        trackedWayItemAdapter.setItemClickListener(this, this);
        rvList.get(1).setAdapter(trackedWayItemAdapter);
        refreshLayout.setEnabled(false);
    }

    @OnClick({R.id.frMap_btnOpenLocation, R.id.frMap_btnOpenWay, R.id.frMap_imvClearText, R.id.frMap_btnGpsLocation, R.id.frMap_btndirection, R.id.frMap_btnTurnTrackingLocation, R.id.frMap_btnOffTrackingLocation, R.id.frMap_btnlayer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frMap_btnOpenLocation:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.frMap_btnOpenWay:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.frMap_imvClearText:
                mgoogleMap.clear();
                mSearchText.setText("");
                closeKeyBoard(this.getActivity().getCurrentFocus());
                break;
            case R.id.frMap_btnGpsLocation:
                mapPresenter.getLastknowLocation(isGPS, isNetwork);
                btnFloatList.get(2).setColorFilter(this.getContext().getResources().getColor(R.color.colorLocationClick));
                moveCameraZoom(latLngCurrent, zoomCamera);
                break;
            case R.id.frMap_btndirection:
                if (!isDrawed) {
                    refreshLayout.setRefreshing(true);
                    directMarker(latLngMarker);
                }
                isDrawed = true;
                break;
            case R.id.frMap_btnTurnTrackingLocation:
                onTurnTrack();
                break;
            case R.id.frMap_btnOffTrackingLocation:
                onOffTrack();
                break;
            case R.id.frMap_btnlayer:
                tileOverLay();
                break;
        }
    }

    //get user image and name
    public void getUserInfo(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(imvList.get(3));
        } else {
            imvList.get(3).setImageResource(R.drawable.contacts);
        }
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(user.getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userObject = dataSnapshot.getValue(User.class);
                userName.setText(userObject.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
    }

    //onClickTracking function
    @SuppressLint("RestrictedApi")
    public void onTurnTrack() {
        mgoogleMap.clear();
        latLngList.clear();
        isTurnOn = true;
        imvList.get(2).setVisibility(View.GONE);
        btnFloatList.get(0).setVisibility(View.GONE);
        btnFloatList.get(1).setVisibility(View.VISIBLE);
        latLngList.add(latLngCurrent);
        Toast.makeText(getContext(), AppConstain.TOAST_START_TRACKING, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("RestrictedApi")
    public void onOffTrack() {
        isTurnOn = false;
        btnFloatList.get(0).setVisibility(View.VISIBLE);
        btnFloatList.get(1).setVisibility(View.GONE);
        appDialog.saveWayDialog(this.getContext(), latLngList);
        Toast.makeText(getContext(), AppConstain.TOAST_STOP_TRACKING, Toast.LENGTH_SHORT).show();
    }

    //get list location & get way location
    public void getListFromFireBase(String userID) {
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_LOCATION);
        ValueEventListener valueListLocation = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationChooseList.clear();
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String namePlace = ds.getKey();
                        locationChooseList.add(namePlace);
                    }
                }
                selectedLocationItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addValueEventListener(valueListLocation);
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TRACKING);
        ValueEventListener valueWayList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wayTrackingList.clear();
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nameWay = ds.getKey();
                        wayTrackingList.add(nameWay);
                    }
                }
                trackedWayItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addValueEventListener(valueWayList);
    }

    //check current location and location change
    @Override
    public void noGPSAndNetWork(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getGPSLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mapPresenter.locationChange(location, AppConstain.TOAST_GPS_ERROR);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getNetWorkLocation() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 100, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mapPresenter.locationChange(location, AppConstain.TOAST_NETWORK_ERROR);
    }

    @Override
    public void locationChange(Location location) {
        onLocationChanged(location);
    }

    @Override
    public void noLocation(String errorString) {
        Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void markerHasUrl(View customMarkerView, String uri) {
        ImageView myImage = customMarkerView.findViewById(R.id.marker_imv);
        Picasso.get().load(uri).into(myImage);
    }

    @Override
    public void markerNoUrl(View customMarkerView) {
        ImageView myImage = customMarkerView.findViewById(R.id.marker_imv);
        myImage.setImageResource((R.drawable.ic_location_on_pink_24dp));
    }

    @Override
    public void nearPlaceData(ItemResultPlace itemResultPlace) {
        objectPlaceNearByList.addAll(itemResultPlace.getObjectPlace());
        String error = itemResultPlace.getError();
        mapPresenter.getDataPlace(objectPlaceNearByList, error);
        hideLoading();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void nearPlaceDataFail(String errorString) {
        hideLoading();
    }

    @Override
    public void getDataPlace(double lat, double lng, String name, String address, String url) {
        addMarker(new LatLng(lat, lng), name, address, url);
    }

    @Override
    public void getDataPlaceFail(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
        if (isTurnOn) {
            mgoogleMap.clear();
            latLngList.add(latLngCurrent);
            polylineDraw(latLngList);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    //edit text search place nearby
    @OnEditorAction(R.id.frMap_etSearch)
    public boolean onSearchAddress(int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
            mgoogleMap.clear();
            if (latLngCurrent != null && sequense != "") {
                showLoading();
                refreshLayout.setRefreshing(true);
                mapPresenter.CallNearbyPlace(sequense, latLngCurrent, radius);
                moveCameraZoom(latLngCurrent, zoomCamera);
            } else {
                Toast.makeText(getActivity(), AppConstain.TOAST_NO_PLACE_FOUND, Toast.LENGTH_SHORT).show();
            }
            closeKeyBoard(this.getActivity().getCurrentFocus());
        }
        return false;
    }

    @OnTextChanged(R.id.frMap_etSearch)
    public void onTextChangeSearch(CharSequence sequence) {
        if (sequence.length() > 0) {
            imvList.get(0).setVisibility(View.GONE);
            imvList.get(1).setVisibility(View.VISIBLE);
            sequense = String.valueOf(sequence);
        } else {
            imvList.get(0).setVisibility(View.VISIBLE);
            imvList.get(1).setVisibility(View.GONE);
            imvList.get(2).setVisibility(View.GONE);
            mgoogleMap.clear();
        }
    }

    //marker on map
    public void addMarker(LatLng latLng, String title, String address, String urlPhotoPlace) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mapPresenter.getMarkerBitmapFromView(urlPhotoPlace)));
        String snipet = AppConstain.ADDRESS + address;
        if (address != null) {
            markerOptions.snippet(snipet);
        }
        mgoogleMap.addMarker(markerOptions);
    }

    public void addMarkerCar(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mapPresenter.getMarkerCarBitmapFromView()));
        mgoogleMap.addMarker(markerOptions);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        isDrawed = false;
        latLngMarker = marker.getPosition();
        imvList.get(2).setVisibility(View.VISIBLE);
        return false;
    }

    //move camera
    public void moveCamera(LatLng latLng) {
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void moveCameraZoom(LatLng latLng, long zoom) {
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void moveCameraListener() {
        mgoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                btnFloatList.get(2).setColorFilter(getContext().getResources().getColor(R.color.colorDark));
            }
        });
    }

    //onMap start
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapPresenter.getLastknowLocation(isGPS, isNetwork);
        moveCameraZoom(latLngCurrent, zoomCamera);
        mgoogleMap.setMyLocationEnabled(true);
        mgoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mgoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mgoogleMap.setOnMarkerClickListener(this);
        onMapLongClick();
        onMapClick();
        moveCameraListener();
    }

    //onMap long click
    public void onMapLongClick() {
        mgoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mgoogleMap.clear();
                SaveMapLongClick(latLng);
                addMarker(latLng, String.valueOf(latLng.latitude + "," + latLng.longitude), null, null);
            }
        });
    }

    //onMap click
    public void onMapClick() {
        mgoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mgoogleMap.clear();
                closeKeyBoard(getActivity().getCurrentFocus());
                imvList.get(2).setVisibility(View.GONE);
            }
        });
    }

    //onMap click layer
    public void tileOverLay() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                String s = "http://b.tile.openstreetmap.org/" + zoom + "/" + x + "/" + y + ".png";
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }
                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }
        };
        mgoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private boolean checkTileExists(int x, int y, int zoom) {
        int minZoom = 12;
        int maxZoom = 16;

        if ((zoom < minZoom || zoom > maxZoom)) {
            return false;
        }

        return true;
    }

    //save choose location dialog
    public void SaveMapLongClick(LatLng latLngLongMap) {
        appDialog.saveChooseDialog(this.getContext(), latLngLongMap, this);
    }

    //onClick item selected list location
    @Override
    public void onItemAllSiteClickListener(int position) {
        drawerLayout.closeDrawer(GravityCompat.START);
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_LOCATION).child(locationChooseList.get(position));
        ValueEventListener valueItemLocation = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mgoogleMap.clear();
                ItemLatlng itemLatlng = dataSnapshot.getValue(ItemLatlng.class);
                LatLng latLng = new LatLng(itemLatlng.getLat(), itemLatlng.getLng());
                addMarker(latLng, dataSnapshot.getKey(), null, null);
                moveCamera(latLng);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addListenerForSingleValueEvent(valueItemLocation);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //add item selected location
    @Override
    public void setOnClickBackSaveChoose(String text, boolean isSave) {
        if (isSave) {
            locationChooseList.add(text);
            selectedLocationItemAdapter.notifyDataSetChanged();
        } else {
            closeKeyBoard(this.getActivity().getCurrentFocus());
        }
    }

    //onLongClick item selected list location to remove
    @Override
    public void onItemFeedCallBack(int position) {
        appDialog.deleteLocationDialog(this.getContext(), locationChooseList, position);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //connection failed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    public void directMarker(final LatLng latLngMarker) {
        final List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(latLngCurrent);
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_ROUTES + "/0").child(AppConstain.KEY_CHILD_LEGS + "/0").child(AppConstain.KEY_CHILD_STEPS);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    DataSnapshot mDataSnapShot = dataSnapshot.child(String.valueOf(i) + AppConstain.KEY_CHILD_END_LOCATION);
                    ItemEndLocation itemEndLocation = mDataSnapShot.getValue(ItemEndLocation.class);
                    LatLng latLngPolylite = new LatLng(itemEndLocation.getLng(), itemEndLocation.getLat());
                    latLngList.add(latLngPolylite);
                }
                latLngList.add(latLngMarker);
                polylineDraw(latLngList);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
    }

    public void polylineDraw(final List<LatLng> latLngList) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latLngList);
        polylineOptions.width(25);
        polylineOptions.color(getResources().getColor(R.color.colorBlueLightPress));
        mgoogleMap.addPolyline(polylineOptions);
    }

    //onClick item list way tracked
    @Override
    public void onItemClickListener(int position) {
        refreshLayout.setRefreshing(true);
        mgoogleMap.clear();
        latLngList.clear();
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TRACKING).child(wayTrackingList.get(position));
        ValueEventListener valueWay = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String json = dataSnapshot.getValue(String.class);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LatLng latLng = new LatLng(jsonObject.getDouble(AppConstain.LATITUDE), jsonObject.getDouble(AppConstain.LONGITUDE));
                        latLngList.add(latLng);
                    }
                    if (latLngList.size() > 1) {
                        polylineDraw(latLngList);
                        addMarkerCar(latLngList.get(0), String.valueOf(latLngList.get(0).latitude + "," + latLngList.get(0).longitude));
                        LatLng latLng = latLngList.get(latLngList.size() - 1);
                        addMarker(latLng, String.valueOf(latLng.latitude + "," + latLng.longitude), null, null);
                        moveCamera(latLng);
                    } else {
                        LatLng latLng = latLngList.get(0);
                        moveCamera(latLng);
                        addMarker(latLng, String.valueOf(latLng.latitude + "," + latLng.longitude), null, null);
                    }
                    refreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addListenerForSingleValueEvent(valueWay);
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    //onLongClick item list way tracked to delete
    @Override
    public void onClickItemQuestionListener(int position) {
        appDialog.deleteWayDialog(this.getContext(), wayTrackingList, position);
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgoogleApiClient.stopAutoManage(getActivity());
        mgoogleApiClient.disconnect();
    }
}
