package tk.yommail.ecrt.Misc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import tk.yommail.ecrt.R;

public class AddCreditcardDialog extends AppCompatDialogFragment {

    private TextInputLayout holderTextInput;
    private TextInputLayout numberTextInput;
    private TextInputLayout expiryTextInput;
    private TextInputLayout cvcTextInput;

    private AddCredidcardDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcreditcard, null);
        
        holderTextInput = view.findViewById(R.id.card_holder);
        numberTextInput = view.findViewById(R.id.card_number);
        expiryTextInput = view.findViewById(R.id.card_expiry);
        cvcTextInput = view.findViewById(R.id.card_cvc);

        builder.setView(view)
                .setTitle("Add Creditcard")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String holder = holderTextInput.getEditText().getText().toString();
                        String number = numberTextInput.getEditText().getText().toString();
                        String expiry = expiryTextInput.getEditText().getText().toString();
                        String cvc = cvcTextInput.getEditText().getText().toString();
                        listener.applyTexts(holder, number, expiry, cvc);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                holderTextInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if  (holderTextInput.getEditText().getText().length() != 0 &&
                                numberTextInput.getEditText().getText().length() == 19 &&
                                expiryTextInput.getEditText().getText().length() == 5 &&
                                cvcTextInput.getEditText().getText().length() == 3) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        } else if (((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled()) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }
                });
                numberTextInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count != 0 && (start == 3 || start == 8 || start == 13)) {
                            numberTextInput.getEditText().setText(String.valueOf(s) + " ");
                            numberTextInput.getEditText().setSelection(
                                    numberTextInput.getEditText().getText().toString().length()
                            );
                        } else if (count != 0 && (start == 4 || start == 9 || start == 14)) {
                            numberTextInput.getEditText().setText(s.toString().substring(0, s.length() - 1) + " " + s.toString().substring(s.length() - 1));
                            numberTextInput.getEditText().setSelection(
                                    numberTextInput.getEditText().getText().toString().length()
                            );
                        } else if (before != 0 && (start == 5 || start == 10 || start == 15)) {
                            numberTextInput.getEditText().setText(String.valueOf(s).substring(
                                    0, numberTextInput.getEditText().getText().toString().length() - 1
                            ));
                            numberTextInput.getEditText().setSelection(
                                    numberTextInput.getEditText().getText().toString().length()
                            );
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if  (holderTextInput.getEditText().getText().length() != 0 &&
                                numberTextInput.getEditText().getText().length() == 19 &&
                                expiryTextInput.getEditText().getText().length() == 5 &&
                                cvcTextInput.getEditText().getText().length() == 3) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        } else if (((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled()) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }
                });
                expiryTextInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count != 0 && start == 2) {
                            expiryTextInput.getEditText().setText(
                                    s.subSequence(0, s.length() - 1) + "/" +
                                            s.subSequence(s.length() - 1, s.length())
                            );
                            expiryTextInput.getEditText().setSelection(
                                    expiryTextInput.getEditText().getText().toString().length()
                            );
                        } else if (before != 0 && start == 3) {
                            expiryTextInput.getEditText().setText(String.valueOf(s).substring(
                                    0, expiryTextInput.getEditText().getText().toString().length() - 1
                            ));
                            expiryTextInput.getEditText().setSelection(
                                    expiryTextInput.getEditText().getText().toString().length()
                            );
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if  (holderTextInput.getEditText().getText().length() != 0 &&
                                numberTextInput.getEditText().getText().length() == 19 &&
                                expiryTextInput.getEditText().getText().length() == 5 &&
                                cvcTextInput.getEditText().getText().length() == 3) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        } else if (((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled()) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }
                });
                cvcTextInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if  (holderTextInput.getEditText().getText().length() != 0 &&
                                numberTextInput.getEditText().getText().length() == 19 &&
                                expiryTextInput.getEditText().getText().length() == 5 &&
                                cvcTextInput.getEditText().getText().length() == 3) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        } else if (((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled()) {
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }
                });
            }
        });
        
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddCredidcardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddCredidcardDialogListener");
        }
    }

    public interface AddCredidcardDialogListener {
        void applyTexts(String holder, String number, String expiry, String cvc);
    }
}
