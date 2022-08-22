package tk.yommail.ecrt.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tk.yommail.ecrt.Misc.AddCreditcardDialog;
import tk.yommail.ecrt.R;
import tk.yommail.ecrt.Settings.SettingsActivity;
import com.google.android.flexbox.FlexboxLayout;

public class ProfileFragment extends Fragment {

    private TextView userName;
    private TextView userEmail;
    private static TextView tvCreditCard;
    private static TextView userCreditCard;
    private static TextView tvCVC;
    private static TextView userCVC;
    private static TextView tvExpiryDate;
    private static TextView userExpiryDate;
    private static SharedPreferences sharedPreferences;
    private static FlexboxLayout addCreditCard;

    private static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences(
                "LOGIN",
                Context.MODE_PRIVATE
        );

        final Button settingsButton = (Button) view.findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });


        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);

        tvCreditCard = (TextView) view.findViewById(R.id.tvProfileCreditCard);
        userCreditCard = (TextView) view.findViewById(R.id.userCreditCard);
        tvCVC = (TextView) view.findViewById(R.id.tvProfileCVC);
        userCVC = (TextView) view.findViewById(R.id.userCVC);
        tvExpiryDate = (TextView) view.findViewById(R.id.tvProfileExpiryDate);
        userExpiryDate = (TextView) view.findViewById(R.id.userExpiryDate);

        userName.setText(sharedPreferences.getString("NAME", null));
        userEmail.setText(sharedPreferences.getString("EMAIL", null));

        addCreditCard = (FlexboxLayout) view.findViewById(R.id.addCreditCard);
        addCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCreditcardDialog addCreditcardDialog = new AddCreditcardDialog();
                addCreditcardDialog.show(getActivity().getSupportFragmentManager(), "wowsa");
            }
        });

        refreshSharedPreferences();

        super.onViewCreated(view, savedInstanceState);
    }

    public static void refreshSharedPreferences() {

        boolean cardSet = sharedPreferences.getBoolean("CARD_SET", false);
        int visibility = cardSet ? View.VISIBLE : View.GONE;

        tvCreditCard.setVisibility(visibility);
        userCreditCard.setVisibility(visibility);
        tvCVC.setVisibility(visibility);
        userCVC.setVisibility(visibility);
        tvExpiryDate.setVisibility(visibility);
        userExpiryDate.setVisibility(visibility);

        if (cardSet) {
            addCreditCard.setVisibility(View.GONE);
            String creditcard = "**** **** **** " + sharedPreferences.getString("CREDIT_CARD", null)
                    .substring(sharedPreferences.getString("CREDIT_CARD", null).length() - 4);
            userCreditCard.setText(creditcard);
            userExpiryDate.setText(sharedPreferences.getString("EXPIRY_DATE", null));
            userCVC.setText(sharedPreferences.getString("CVC", null));
        }
    }
}
