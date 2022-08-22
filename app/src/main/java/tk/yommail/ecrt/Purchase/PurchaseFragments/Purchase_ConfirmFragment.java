package tk.yommail.ecrt.Purchase.PurchaseFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.yommail.ecrt.Misc.DatabaseHelper;
import tk.yommail.ecrt.Purchase.MakePurchaseActivity;
import tk.yommail.ecrt.Purchase.ReceiptActivity;
import tk.yommail.ecrt.R;
import com.google.android.flexbox.FlexboxLayout;

public class Purchase_ConfirmFragment extends Fragment {

    private static final String TAG = "Purchase_StartFragment";

    private String holder;
    private String number;
    private String expiry;

    private TextView userName;
    private TextView userEmail;
    private static TextView tvCreditCard;
    private static TextView userCreditCard;
    private static TextView tvCVC;
    private static TextView userCVC;
    private static TextView tvExpiryDate;
    private static TextView userExpiryDate;
    private static SharedPreferences sharedPreferences;
    private TextView subtotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_purchase_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences(
                "LOGIN",
                Context.MODE_PRIVATE
        );

        final DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        Toolbar toolbar = view.findViewById(R.id.confirm_purchase_toolbar);
        ((MakePurchaseActivity)getActivity()).setSupportActionBar(toolbar);

        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);

        subtotal = (TextView) view.findViewById(R.id.product_subtotal);

        tvCreditCard = (TextView) view.findViewById(R.id.tvProfileCreditCard);
        userCreditCard = (TextView) view.findViewById(R.id.userCreditCard);
        tvCVC = (TextView) view.findViewById(R.id.tvProfileCVC);
        userCVC = (TextView) view.findViewById(R.id.userCVC);
        tvExpiryDate = (TextView) view.findViewById(R.id.tvProfileExpiryDate);
        userExpiryDate = (TextView) view.findViewById(R.id.userExpiryDate);

        if (sharedPreferences.getBoolean("CARD_SET", false)) {
            userName.setText(sharedPreferences.getString("NAME", null));
            userEmail.setText(sharedPreferences.getString("EMAIL", null));

            String creditcard = "**** **** **** " + sharedPreferences.getString("CREDIT_CARD", null)
                    .substring(sharedPreferences.getString("CREDIT_CARD", null).length() - 4);
            userCreditCard.setText(creditcard);
            userExpiryDate.setText(sharedPreferences.getString("EXPIRY_DATE", null));
            userCVC.setText(sharedPreferences.getString("CVC", null));
        }

        subtotal.setText(String.valueOf(databaseHelper.getItemsSubtotal()));

        // Make Purchase Button
        FlexboxLayout purchaseButton = view.findViewById(R.id.purchase_finish_purchase);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReceiptActivity.class));
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}