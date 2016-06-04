package id11965252.com.artorder.View;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

import id11965252.com.artorder.Model.Order;
import id11965252.com.artorder.R;
import id11965252.com.artorder.Util.Constants;
import id11965252.com.artorder.Util.StatusFormat;


public class OrderDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent orderDetailIntent = getIntent();
        new RequestOrderDetailTask(this, orderDetailIntent.getIntExtra(Constants.ORDER_DETAIL_ID, 0)).execute();
    }

    private class RequestOrderDetailTask extends AsyncTask<Void, Void, Order> {

        private Context mContext;
        private int mOrderId;
        private ProgressBar mProgressBar;

        public RequestOrderDetailTask(Context context, int orderId) {
            mContext = context;
            mOrderId = orderId;
            mProgressBar = (ProgressBar) findViewById(R.id.order_detail_progress_bar);
        }

        /**
         * Show Progress Bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Send Request to Server in background
         * return an order object
         */
        @Override
        protected Order doInBackground(Void... params) {
            try {
                final String url = "http://" + Constants.SERVER_IP + Constants.PORT + Constants.API_ADDRESS + Constants.ORDER_DETAIL_URI + mOrderId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

                return restTemplate.getForObject(url, Order.class);
            } catch (Exception e) {
                Log.e(getString(R.string.order_detail_activity), e.getMessage(), e);
            }

            return null;
        }

        /**
         * Give views values of the order
         */
        @Override
        protected void onPostExecute(Order order) {
            super.onPostExecute(order);

            // Assign values to views
            if (order != null) {
                TextView clientView = (TextView) findViewById(R.id.order_detail_client_value);
                TextView phoneView = (TextView) findViewById(R.id.order_detail_phone_value);
                TextView descriptionView = (TextView) findViewById(R.id.order_detail_description_value);
                TextView createDateView = (TextView) findViewById(R.id.order_detail_create_date_value);
                TextView quoteView = (TextView) findViewById(R.id.order_detail_quote_value);
                TextView receiptView = (TextView) findViewById(R.id.order_detail_receipt_value);
                TextView dueDateView = (TextView) findViewById(R.id.order_detail_due_date_value);
                TextView creatorView = (TextView) findViewById(R.id.order_detail_creator_value);
                TextView statusView = (TextView) findViewById(R.id.order_detail_status_value);
                TextView idView = (TextView) findViewById(R.id.order_detail_id_value);

                idView.setText(order.getId() + "");
                clientView.setText(order.getClient());
                phoneView.setText(order.getPhone());
                descriptionView.setText(order.getDescription());
                quoteView.setText(order.getQuote() + "");
                receiptView.setText(order.getReceipt() + "");
                creatorView.setText(order.getCreator());
                statusView.setText(StatusFormat.format(order.getStatus()));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                createDateView.setText(simpleDateFormat.format(order.getDate()));
                dueDateView.setText(simpleDateFormat.format(order.getDueDate()));
            }
            else{
                // Handles no response from server
                Toast.makeText(mContext, getString(R.string.no_response_from_server), Toast.LENGTH_LONG).show();
            }

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
