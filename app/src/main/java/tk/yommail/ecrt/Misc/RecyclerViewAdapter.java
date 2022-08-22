package tk.yommail.ecrt.Misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tk.yommail.ecrt.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Item> items;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_item, viewGroup,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.amount.setText(String.format("%d ×", items.get(i).getAmount()));
        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.price.setText(
                String.format("%s%s", Math.floor(items.get(i).getPrice() * (double) items.get(i).getAmount() * 100) / 100, "kr")
        );

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position){
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position){
        items.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView amount;
        TextView title;
        TextView price;
        ConstraintLayout parentLayout;
        RelativeLayout background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.item_amount);
            title = itemView.findViewById(R.id.item_title);
            price = itemView.findViewById(R.id.item_price);
            parentLayout = itemView.findViewById(R.id.item_parent_layout);
            background = itemView.findViewById(R.id.list_background);

        }
    }

}
