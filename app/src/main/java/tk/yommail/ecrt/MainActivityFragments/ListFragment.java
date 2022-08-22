package tk.yommail.ecrt.MainActivityFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tk.yommail.ecrt.Misc.DatabaseHelper;
import tk.yommail.ecrt.Misc.Item;
import tk.yommail.ecrt.Misc.RecyclerItemTouchHelper;
import tk.yommail.ecrt.Misc.RecyclerItemTouchHelperListener;
import tk.yommail.ecrt.Misc.RecyclerViewAdapter;
import tk.yommail.ecrt.Purchase.MakePurchaseActivity;
import tk.yommail.ecrt.R;

import java.util.ArrayList;

public class ListFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private static final String TAG = "ListFragment";

    private DatabaseHelper databaseHelper;
    private ArrayList<Item> items;
    private RecyclerViewAdapter adapter;
    private ConstraintLayout layoutList;
    private RecyclerView recyclerView;
    private View rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rootview = view;

        layoutList = view.findViewById(R.id.layout_list);

        databaseHelper = new DatabaseHelper(getContext());

        items = databaseHelper.getItems();

        // init recyclerview
        recyclerView = view.findViewById(R.id.list_recycler_view);
        if (adapter != null) {
            adapter = new RecyclerViewAdapter(getContext(), items);
            recyclerView.setAdapter(adapter);
        }
        updateList(rootview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);


        view.findViewById(R.id.listPurchaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MakePurchaseActivity.class));
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerViewAdapter.ViewHolder) {
            String name = items.get(viewHolder.getAdapterPosition()).getTitle();

            final Item deletedItem = items.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            databaseHelper.deleteTask(deletedItem.getTitle());
            adapter.removeItem(deleteIndex);

            updateList(rootview);

            Snackbar snackbar = Snackbar.make(layoutList, name + " removed from cart!", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseHelper.addItem(deletedItem.getId(), deletedItem.getTitle(), deletedItem.getPrice(), deletedItem.getAmount());
                    adapter.restoreItem(deletedItem, deleteIndex);

                    updateList(rootview);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void updateList (View view) {
        if (items.size() != 0 && recyclerView.hasPendingAdapterUpdates()) {
            adapter = new RecyclerViewAdapter(getContext(), items);
            recyclerView.setAdapter(adapter);
            view.findViewById(R.id.list_help_text).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.listPurchaseButton).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.list_help_text).setVisibility(View.VISIBLE);
            view.findViewById(R.id.listPurchaseButton).setVisibility(View.INVISIBLE);
        }
    }
}
