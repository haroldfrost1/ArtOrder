package id11965252.com.artorder.View;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import id11965252.com.artorder.Adapter.OrderAdapter;
import id11965252.com.artorder.Model.Order;
import id11965252.com.artorder.Model.Orders;
import id11965252.com.artorder.R;
import id11965252.com.artorder.Util.Constants;

public class OrderListAcitivty extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // UI
    private List<Order> mOrderList;
    private OrderAdapter mOrderAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addOrderButton = (FloatingActionButton) findViewById(R.id.orderlistactivity_add_order_button);
        addOrderButton.setOnClickListener(this);

        // Init Swipe Refresh Layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.orderlistactivity_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Initialize Recylcler View
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.orderlistactivity_recycler_view);

        mOrderList = new ArrayList<>();
        mOrderAdapter = new OrderAdapter(this, mOrderList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mOrderAdapter);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderlistactivity_add_order_button:
                startActivity(new Intent(this, AddOrderFormActivity.class));
                break;
        }
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        new RetrieveListRequestTask(this).execute();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Calls when the screen is dragged down
     */
    @Override
    public void onRefresh() {
        new RetrieveListRequestTask(this).execute();
    }

    private class RetrieveListRequestTask extends AsyncTask<Void, Void, Orders> {

        private ProgressBar mProgressBar;
        private Context mContext;

        public RetrieveListRequestTask(Context context) {
            mProgressBar = (ProgressBar) findViewById(R.id.orderlistactivity_retrieve_list_progress);
            this.mContext = context;
        }

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Orders doInBackground(Void... params) {
            try {
                final String url = Constants.HTTP + Constants.SERVER_IP + Constants.PORT + Constants.API_ADDRESS + Constants.ORDER_LIST_URI;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
                return restTemplate.getForObject(url, Orders.class);
            } catch (HttpMessageNotReadableException e) {
                Log.e(getString(R.string.orderlistactivity_tag), e.getMessage(), e);
                Log.e(getString(R.string.orderlistactivity_tag), "The list is empty!");
            }

            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param orders The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Orders orders) {
            super.onPostExecute(orders);
            // Add orders to the recycler view
            mOrderList.clear();
            if (orders != null) {
                for (Order order : orders.getOrdersList()) {
                    mOrderList.add(order);
                }
            } else {
                // Notifies users if the list is empty
                Toast.makeText(mContext, "The list is empty!", Toast.LENGTH_LONG).show();
            }
            mOrderAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
