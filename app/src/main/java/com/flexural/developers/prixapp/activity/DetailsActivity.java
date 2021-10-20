package com.flexural.developers.prixapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.User;
import com.flexural.developers.prixapp.utils.LocationAddress;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;

public class DetailsActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3600000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    private RelativeLayout mButtonNext;
    private TextView mPhoneNumber, mEmailAddress, mBirthday;
    private EditText mInputName, mInputSurname, mInputStreet, mInputCity, mInputCountry;
    private CheckBox mTermsCondition;

    private String userId, currentDateTime, mLastUpdateTime, longitude, latitude, locationAddress;
    private int day, month, year;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ProgressDialog progressDialog;
    private Boolean mRequestingLocationUpdates;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private DatabaseReference mProfileRef, mAccountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mButtonNext = findViewById(R.id.button_next);
        mPhoneNumber = findViewById(R.id.phone_number);
        mEmailAddress = findViewById(R.id.email_address);
        mInputName = findViewById(R.id.input_name);
        mInputSurname = findViewById(R.id.input_surname);
        mInputStreet = findViewById(R.id.input_street);
        mInputCity = findViewById(R.id.input_city);
        mInputCountry = findViewById(R.id.input_country);

        mBirthday = findViewById(R.id.birthday);
        mTermsCondition = findViewById(R.id.checkbox);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
        currentDateTime = simpleDateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProfileRef = FirebaseDatabase.getInstance().getReference().child("prix").child("profile");
        mAccountRef = FirebaseDatabase.getInstance().getReference().child("prix").child("accounts");

        init();
        getData();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();

    }

    private void getData() {
        mAccountRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                mPhoneNumber.setText(user.phoneNumber);
                mEmailAddress.setText(user.email);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        mBirthday.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(DetailsActivity.this,
                    (view1, year, month, dayOfMonth) -> {
                        if (dayOfMonth < 9 && (month + 1) < 9){
                            mBirthday.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);

                        } else if (dayOfMonth < 9 && (month + 1) > 9){
                            mBirthday.setText("0" + dayOfMonth + "/" + (month + 1) + "/" + year);

                        } else if (dayOfMonth > 9 && (month + 1) < 9){
                            mBirthday.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);

                        } else {
                            mBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        }

                    }, year, month, day);

            datePicker.show();
        });

        mButtonNext.setOnClickListener(v -> {
            if (mInputName.length() > 0 && mInputSurname.length() > 0 && mInputStreet.length() > 0 &&
            mInputCity.length() > 0 && mInputCountry.length() > 0 && mBirthday.length() > 0 &&
            mTermsCondition.isChecked()) {
                progressDialog.setMessage("Uploading information...");
                progressDialog.show();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", mInputName.getText().toString());
                hashMap.put("surname", mInputSurname.getText().toString());
                hashMap.put("street", mInputStreet.getText().toString());
                hashMap.put("city", mInputCity.getText().toString());
                hashMap.put("country", mInputCountry.getText().toString());
                hashMap.put("birthday", mBirthday.getText().toString());

                mProfileRef.child(userId).updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(DetailsActivity.this, PasswordActivity.class);
                                    startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                }
                            }
                        });

            } else if (mInputName.length() == 0) {
                Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();

            } else if (mInputSurname.length() == 0) {
                Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();

            } else if (mInputStreet.length() == 0) {
                Toast.makeText(this, "Please Enter Street Address", Toast.LENGTH_SHORT).show();

            } else if (mInputCity.length() == 0) {
                Toast.makeText(this, "Please Enter City", Toast.LENGTH_SHORT).show();

            } else if (mInputCountry.length() == 0) {
                Toast.makeText(this, "Please Enter Country of Residence", Toast.LENGTH_SHORT).show();

            } else if (mBirthday.length() == 0) {
                Toast.makeText(this, "Please Enter Birthday", Toast.LENGTH_SHORT).show();

            } else if (!mTermsCondition.isChecked()) {
                Toast.makeText(this, "Please Accept Terms & Conditions", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(DetailsActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(DetailsActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(DetailsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(DetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }

    private void updateUI() {
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            latitude = String.format(Locale.ENGLISH, "%f", mCurrentLocation.getLatitude());
            longitude = String.format(Locale.ENGLISH, "%f", mCurrentLocation.getLongitude());

            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(Double.valueOf(latitude), Double.valueOf(longitude),
                    DetailsActivity.this, new GeocoderHandler());



        }
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener((Executor) this, task -> mRequestingLocationUpdates = false);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    String country = bundle.getString("country");
                    String city = bundle.getString("city");

                    mInputStreet.setText(locationAddress);
                    mInputCountry.setText(country);
                    mInputCity.setText(city);

                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
//
//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            showSnackbar(R.string.permission_rationale,
//                    android.R.string.ok, view -> {
//                        // Request permission
//                        ActivityCompat.requestPermissions(this,
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                REQUEST_PERMISSIONS_REQUEST_CODE);
//                    });
//        } else {
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            startLocationUpdates();
                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();
        }

        updateUI();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        startLocationUpdates();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        startLocationUpdates();

    }

    @Override
    public void onStop() {
        super.onStop();

        stopLocationUpdates();

    }

}