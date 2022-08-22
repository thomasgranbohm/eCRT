package tk.yommail.ecrt.Purchase.PurchaseFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import tk.yommail.ecrt.Purchase.MakePurchaseActivity;
import tk.yommail.ecrt.R;

public class Purchase_CredentialsFragment extends Fragment {

    private static final String TAG = "Purchase_CredentialsFra";

    public TextInputLayout cardNumber;
    public TextInputLayout cardHolder;
    public TextInputLayout cardCVC;
    public TextInputLayout cardExpiry;
    private FlexboxLayout proceedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_purchase_credentials, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                "LOGIN",
                Context.MODE_PRIVATE
        );

        Toolbar toolbar = view.findViewById(R.id.credentials_toolbar);
        ((MakePurchaseActivity)getActivity()).setSupportActionBar(toolbar);

        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        cardHolder = (TextInputLayout) view.findViewById(R.id.card_holder);
        cardNumber = (TextInputLayout) view.findViewById(R.id.card_number);
        cardExpiry = (TextInputLayout) view.findViewById(R.id.card_expiry);
        cardCVC = (TextInputLayout) view.findViewById(R.id.card_cvc);
        proceedButton = view.findViewById(R.id.purchase_credentials_proceed_button);
        proceedButton.setEnabled(sharedPreferences.getBoolean("CARD_SET", false));

        if (sharedPreferences.getBoolean("CARD_SET", false)) {
            cardHolder.getEditText().setText(sharedPreferences.getString("HOLDER", null));
            cardNumber.getEditText().setText(sharedPreferences.getString("CREDIT_CARD", null));
            cardExpiry.getEditText().setText(sharedPreferences.getString("EXPIRY_DATE", null));
            cardCVC.getEditText().setText(sharedPreferences.getString("CVC", null));
        }

        
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                        "LOGIN",
                        Context.MODE_PRIVATE
                );


                if (!sharedPreferences.getBoolean("CARD_SET", false)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("HOLDER", cardHolder.getEditText().getText().toString());
                    editor.putString("CREDIT_CARD", cardNumber.getEditText().getText().toString());
                    editor.putString("EXPIRY_DATE", cardExpiry.getEditText().getText().toString());
                    editor.putString("CVC", cardCVC.getEditText().getText().toString());
                    editor.putBoolean("CARD_SET", true);
                    editor.apply();
                }

                ((MakePurchaseActivity) getActivity()).chooseTab(2);
            }
        });

        cardNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + start + " " + count + " " + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && (start + 2) % 5 == 0) {
                    cardNumber.getEditText().setText(s + " ");
                    cardNumber.getEditText().setSelection(cardNumber.getEditText().getText().length());
                } else if (before == 1 && start % 5 == 0 && cardNumber.getEditText().getText().length() != 0) {
                    cardNumber.getEditText().setText(cardNumber.getEditText().getText().subSequence(0,
                            cardNumber.getEditText().getText().length() - 1));
                    cardNumber.getEditText().setSelection(cardNumber.getEditText().getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cardHolder.getEditText().getText().length() != 0 &&
                        cardNumber.getEditText().getText().length() == 19 &&
                        cardExpiry.getEditText().getText().length() == 5 &&
                        cardCVC.getEditText().getText().length() == 3) {
                    proceedButton.setEnabled(true);
                } else {
                    if (proceedButton.isEnabled()) {
                        proceedButton.setEnabled(false);
                        // TODO lägg till unchecked color
                    }
                }
            }
        });
        cardExpiry.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && start == 1) {
                    cardExpiry.getEditText().setText(s + "/");
                    cardExpiry.getEditText().setSelection(cardExpiry.getEditText().getText().length());
                } else if (before == 1 && start == 5 && cardExpiry.getEditText().getText().length() != 0) {
                    cardExpiry.getEditText().setText(cardExpiry.getEditText().getText().subSequence(0,
                            cardExpiry.getEditText().getText().length() - 3));
                    cardExpiry.getEditText().setSelection(cardExpiry.getEditText().getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cardHolder.getEditText().getText().length() != 0 &&
                    cardNumber.getEditText().getText().length() == 19 &&
                    cardExpiry.getEditText().getText().length() == 5 &&
                    cardCVC.getEditText().getText().length() == 3) {
                    proceedButton.setEnabled(true);
                } else {
                    proceedButton.setEnabled(false);
                    // TODO lägg till unchecked color
                }
            }
        });
        cardCVC.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cardHolder.getEditText().getText().length() != 0 &&
                        cardNumber.getEditText().getText().length() == 19 &&
                        cardExpiry.getEditText().getText().length() == 5 &&
                        cardCVC.getEditText().getText().length() == 3) {
                    proceedButton.setEnabled(true);
                } else {
                    proceedButton.setEnabled(false);
                    // TODO lägg till unchecked color
                }
            }
        });
        cardHolder.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cardHolder.getEditText().getText().length() != 0 &&
                        cardNumber.getEditText().getText().length() == 19 &&
                        cardExpiry.getEditText().getText().length() == 5 &&
                        cardCVC.getEditText().getText().length() == 3) {
                    proceedButton.setEnabled(true);
                } else {
                    proceedButton.setEnabled(false);
                    // TODO lägg till unchecked color
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public String getCardNumber() {
        return cardNumber.getEditText().getText().toString();
    }

    public String getCardHolder() {
        return cardHolder.getEditText().getText().toString();
    }

    public String getCardExpiry() {
        return cardExpiry.getEditText().getText().toString();
    }
}