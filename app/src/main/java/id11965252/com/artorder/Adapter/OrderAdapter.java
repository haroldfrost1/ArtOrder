package id11965252.com.artorder.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id11965252.com.artorder.Model.Order;
import id11965252.com.artorder.R;
import id11965252.com.artorder.Util.StatusFormat;

/**
 * OrderAdapter Class to inflate OrderListActivity RecyclerView
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private List<Order> mOrderList;
    private LayoutInflater mInflater;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.mOrderList = orderList;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Called when RecyclerView needs a new {@link OrderViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(OrderViewHolder, int)
     */
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = mInflater.inflate(R.layout.order_adapter_item, parent, false);

        return new OrderViewHolder(itemview);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link OrderViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = this.mOrderList.get(position);
        holder.mClientView.setText(order.getClient());

        // Format Status code to user-readable string
        int status = order.getStatus();
        holder.mStatusView.setText(StatusFormat.formatStatus(status));
        switch(status){
            case 1:
            case 2:
            case 4:
            case 5:
                holder.mStatusView.setTextColor(Color.GREEN);
                break;
            default:
                holder.mStatusView.setTextColor(Color.RED);
        }
        holder.mStatusView.setTextColor(Color.RED);

        long dueDate = order.getDueDate();

        // Format Days Left to user-readable string
        String daysLeftStr = "";
        long daysLeft = dueDate - System.currentTimeMillis();
        daysLeftStr += TimeUnit.DAYS.convert(daysLeft, TimeUnit.MILLISECONDS);
        daysLeftStr += " Days Left";
        holder.mDaysLeftView.setText(daysLeftStr);

        // Format Due date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm");
        holder.mDueDateView.setText(simpleDateFormat.format(new Date(dueDate)));
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    /**
     * RecyclerViewHolder used to hold each row of Order data
     */
    protected class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView mClientView;
        public TextView mStatusView;
        public TextView mDaysLeftView;
        public TextView mDueDateView;
        public ImageView mThumbnailView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            mClientView = (TextView) itemView.findViewById(R.id.order_adapter_item_client);
            mStatusView = (TextView) itemView.findViewById(R.id.order_adapter_item_status);
            mDaysLeftView = (TextView) itemView.findViewById(R.id.order_adapter_item_days_left);
            mDueDateView = (TextView) itemView.findViewById(R.id.order_adapter_item_due_date);
            mThumbnailView = (ImageView) itemView.findViewById(R.id.order_adapter_item_thumbnail);
        }
    }
}
