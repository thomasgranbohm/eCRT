package tk.yommail.ecrt.Purchase.PurchaseFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import tk.yommail.ecrt.Misc.DatabaseHelper;
import tk.yommail.ecrt.Misc.Item;
import tk.yommail.ecrt.Misc.RecyclerViewAdapter;
import tk.yommail.ecrt.Purchase.MakePurchaseActivity;
import tk.yommail.ecrt.R;

import java.util.ArrayList;

public class Purchase_StartFragment extends Fragment {

    private static final String TAG = "Purchase_StartFragment";
    private View startButton;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Item> items;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_purchase_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = view.findViewById(R.id.review_items_toolbar);
        ((MakePurchaseActivity)getActivity()).setSupportActionBar(toolbar);

        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MakePurchaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        FlexboxLayout start = view.findViewById(R.id.purchase_start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MakePurchaseActivity) getActivity()).chooseTab(1);
            }
        });

        databaseHelper = new DatabaseHelper(getContext());

        items = databaseHelper.getItems();

        // init recyclerview
        recyclerView = view.findViewById(R.id.fakeRecyclerView);
        adapter = new RecyclerViewAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        super.onViewCreated(view, savedInstanceState);
    }

}