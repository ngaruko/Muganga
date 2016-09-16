package org.muganga.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.muganga.R;

/**
 * This is the home fragment. Need to think of contents.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public static final int STATE_SIGNED_IN = 0;
    public static final int STATE_SIGN_IN = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private static final String TAG = "signin1";
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
    public static int mSignInProgress;
    public static boolean signedIn;
    private static PendingIntent mSignInIntent;
    private static int mSignInError;
    private SignInButton mSignInButton;
    private Button mCallButton;
    private Button mTextButton;
    private Button mAskButton;
    private Button mEmailButton;
    private Button mFindButton;
    private Button mMapsButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;
    private GoogleApiClient mGoogleApiClient;


    public HomeFragment() {
    }

    public static Fragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);


//Auth
        mSignInButton = (SignInButton) layout.findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) layout.findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) layout.findViewById(R.id.revoke_access_button);
        mStatus = (TextView) layout.findViewById(R.id.sign_in_status);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);

        mGoogleApiClient = buildApiClient();

        setHasOptionsMenu(false);

//code fod the buttons
        mCallButton= (Button) layout.findViewById(R.id.call_button);
        mTextButton= (Button) layout.findViewById(R.id.sms_button);
        mAskButton= (Button) layout.findViewById(R.id.ask_button);
         mEmailButton= (Button) layout.findViewById(R.id.email_button);
       mFindButton= (Button) layout.findViewById(R.id.find_button);
        mMapsButton= (Button) layout.findViewById(R.id.map_button);

        //
     //   setOnClickListener

        mCallButton.setOnClickListener(this);
        mAskButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mFindButton.setOnClickListener(this);
        mMapsButton.setOnClickListener(this);
        mTextButton.setOnClickListener(this);

        //code for the ad banner
        AdView mAdView = (AdView) layout.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return layout;
    }

    public GoogleApiClient buildApiClient() {
        return new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope(Scopes.PROFILE))
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.
        mSignInButton.setEnabled(false);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);
        setSignedIn(true);
        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_SIGNED_IN;


        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        //Todo: Look for the bug
        mStatus.setText(String.format("Signed In to G+ as %s", currentUser != null ? currentUser.getDisplayName() : "User"));


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

        if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {

                resolveSignInError();
            }
        }

        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
        onSignedOut();
    }

    public void resolveSignInError() {
        if (mSignInIntent != null) {


            try {

                mSignInProgress = STATE_IN_PROGRESS;
                getActivity().startIntentSenderForResult(mSignInIntent.getIntentSender(),

                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {

            getActivity().showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {

                    mSignInProgress = STATE_SIGN_IN;
                } else {

                    mSignInProgress = STATE_SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {

                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
        mRevokeButton.setEnabled(false);

        mStatus.setText("Signed out");
        setSignedIn(false);


    }

    @Override
    public void onClick(View v) {

        //user actions

        switch (v.getId()) {
            case R.id.call_button:
               Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: 0800838383"));
                startActivity(callIntent);
                break;
            case R.id.sms_button:

                String phoneNumber="0212176188";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", "We need some help");
                startActivity(intent);

                break;
            case R.id.email_button:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                emailIntent.setType("text/plain");

                startActivity(Intent.createChooser(emailIntent, "Send Email"));

                break;
            case R.id.map_button:

                Intent mapsIntent = new Intent(getContext(), MapsActivity.class);
                startActivity(mapsIntent);
                break;
            case R.id.find_button:

                Toast.makeText(getContext(), "Finding", Toast.LENGTH_LONG).show();
                break;
            case R.id.ask_button:

                Toast.makeText(getContext(), "Asking", Toast.LENGTH_LONG).show();
                break;
        }

        if (!mGoogleApiClient.isConnecting()) {

            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText("Signing In");
                    resolveSignInError();
                    break;
                case R.id.sign_out_button:

                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.revoke_access_button:

                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildApiClient();
                    mGoogleApiClient.connect();
                    break;
            }
        }
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        HomeFragment.signedIn = signedIn;
    }
}

