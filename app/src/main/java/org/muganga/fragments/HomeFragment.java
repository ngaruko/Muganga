package org.muganga.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;

import org.muganga.Logs.Logger;
import org.muganga.R;
import org.muganga.activities.BluetoothActivity;
import org.muganga.activities.FirebaseActivity;
import org.muganga.activities.LocationsActivity;
import org.muganga.activities.MapsActivity;
import org.muganga.activities.NotificationActivity;

/**
 * This is the home fragment. Need to think of contents.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
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

//search invisible
        //new MainActivity().mSearchItem.setVisible(false);
//Auth
        mSignInButton = (SignInButton) layout.findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) layout.findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) layout.findViewById(R.id.revoke_access_button);
        mStatus = (TextView) layout.findViewById(R.id.sign_in_status);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);


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
     //   AdView mAdView = (AdView) layout.findViewById(R.id.adView);
    //    AdRequest adRequest = new AdRequest.Builder().build();
    //    mAdView.loadAd(adRequest);

        return layout;
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onStop() {
        super.onStop();


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
                //startActivity(intent);

                //choose how to share ....will display all apps with action_send
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

                //
                startActivity(new Intent(getContext(),NotificationActivity.class));

                break;
            case R.id.email_button:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "info@muganga.org");
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

                Intent findIntent = new Intent(getContext(), LocationsActivity.class);
                startActivity(findIntent);
                break;

            case R.id.ask_button:
                //Open firebase activity...
                Logger.longToast("Calling Firebase----");
                Intent firebaseIntent = new Intent(getContext(), FirebaseActivity.class);
                startActivity(firebaseIntent);
               // Toast.makeText(getContext(), "Asking", Toast.LENGTH_LONG).show();
                break;
        }



            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText("Signing In");
                    Intent firebaseIntent = new Intent(getContext(), FirebaseActivity.class);
                    startActivity(firebaseIntent);
                    break;
                case R.id.sign_out_button:

                    Intent i = new Intent(getContext(), BluetoothActivity.class);
                    startActivity(i);
                    break;
                case R.id.revoke_access_button:

                    //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

                   // Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                   //mGoogleApiClient = buildApiClient();
                    //mGoogleApiClient.connect();

                    //Open firebase activity...
                    Logger.longToast("Calling Firebase----");
                    Intent intent = new Intent(getContext(), FirebaseActivity.class);
                    startActivity(intent);
                    break;
            }

    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        HomeFragment.signedIn = signedIn;
    }



}

