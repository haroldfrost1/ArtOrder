package id11965252.com.artorder.View;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import id11965252.com.artorder.Model.Order;
import id11965252.com.artorder.Model.Response;
import id11965252.com.artorder.R;
import id11965252.com.artorder.Util.Constants;



public class AddOrderFormActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    // UI
    private EditText mClientEditText;
    private EditText mPhoneEditText;
    private EditText mDescriptionEditText;
    private EditText mQuoteEditText;
    private TextView mCreatorTextView;
    private TextView mCreateDateTextView;
    private Button mPlaceOrderButton;
    private Button mResetButton;
    private EditText mDueDayNumberView;
    private Spinner mDurationSpinner;

    private AddOrderTask mAddOrderTask;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_form);

        mAddOrderTask = null;

        // Binding UI
        mClientEditText = (EditText) findViewById(R.id.order_form_client_value);
        mPhoneEditText = (EditText) findViewById(R.id.order_form_phone_value);
        mDescriptionEditText = (EditText) findViewById(R.id.order_form_description_value);
        mQuoteEditText = (EditText) findViewById(R.id.order_form_quote_value);
        mCreateDateTextView = (TextView) findViewById(R.id.order_form_create_date_value);
        mDueDayNumberView = (EditText) findViewById(R.id.addorderformactivity_due_number);
        mDueDayNumberView.setOnEditorActionListener(this);

        mResetButton = (Button) findViewById(R.id.order_form_reset_button);
        mResetButton.setOnClickListener(this);

        // TODO: Adapter
        mDurationSpinner = (Spinner)findViewById(R.id.addorderformactivity_due_spinner);
        String[] spinnerItem = new String[]{getString(R.string.day),getString(R.string.week),getString(R.string.month)};
        mDurationSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItem));

        // Display today's date
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mCreateDateTextView.setText(simpleDateFormat.format(date));

        // TODO: Change test creator to real user name
        mCreatorTextView = (TextView) findViewById(R.id.order_form_creator_value);
        mCreatorTextView.setText("Test Creator");

        mPlaceOrderButton = (Button) findViewById(R.id.order_form_place_order_button);
        mPlaceOrderButton.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_form_place_order_button:
                attemptAddOrder();
                break;
            case R.id.order_form_reset_button:
                reset();
                break;
        }

    }

    /**
     * Reset all fields and error
     */
    public void reset() {
        // Reset errors.
        mClientEditText.setError(null);
        mClientEditText.setText("");

        mPhoneEditText.setError(null);
        mPhoneEditText.setText("");

        mDescriptionEditText.setError(null);
        mDescriptionEditText.setText("");

        mQuoteEditText.setError(null);
        mQuoteEditText.setText(R.string.quote_0);

        mDueDayNumberView.setError(null);
        mDueDayNumberView.setText(R.string.zero);
    }

    /**
     * Attempts to place an order specified by the add order form.
     * If there are form errors (invalid client, missing fields, etc.), the
     * errors are presented and no actual order attempt is made.
     */
    public void attemptAddOrder() {
        // Avoid duplicate attempts
        if (mAddOrderTask != null) {
            return;
        }

        // Reset errors.
        mClientEditText.setError(null);
        mPhoneEditText.setError(null);
        mDescriptionEditText.setError(null);
        mQuoteEditText.setError(null);
        mDueDayNumberView.setError(null);

        // Store values at the time of the add order attempt.
        String client = mClientEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        String quoteStr = mQuoteEditText.getText().toString();
        String daysStr = mDueDayNumberView.getText().toString();
        String creator = mCreatorTextView.getText().toString();
        long dueDate = System.currentTimeMillis();
        Double quote = 0.0;

        boolean cancel = false;
        View focusView = null;



        // Check for a valid day number and cast into a long value @dueDate
        if (TextUtils.isEmpty(daysStr)) {
            mDueDayNumberView.setError(getString(R.string.error_field_required));
            focusView = mDueDayNumberView;
            cancel = true;
        } else if (!isDayNumberValid(daysStr)) {
            mDueDayNumberView.setError(getString(R.string.error_invalid_daynumber));
            focusView = mDueDayNumberView;
            cancel = true;
        } else {
            // TODO: parse day number to actual date
            int numberOfDays = Integer.parseInt(mDueDayNumberView.getText().toString());
            switch (mDurationSpinner.getSelectedItemPosition()){
                case 0:
                    break;
                case 1:
                    numberOfDays *= 7;
                    break;
                case 2:
                    numberOfDays *= 30;
                    break;
                default:
                    break;
            }
            dueDate += TimeUnit.MILLISECONDS.convert(numberOfDays,TimeUnit.DAYS);
        }

        // Check for a valid description
        if (TextUtils.isEmpty(description)) {
            mDescriptionEditText.setError(getString(R.string.error_field_required));
            focusView = mDescriptionEditText;
            cancel = true;
        } else if (!isDescriptionValid(description)) {
            mDescriptionEditText.setError(getString(R.string.error_invalid_description));
            focusView = mDescriptionEditText;
            cancel = true;
        }

        // Check for a valid quote
        if (TextUtils.isEmpty(quoteStr)) {
            mQuoteEditText.setError(getString(R.string.error_field_required));
            focusView = mQuoteEditText;
            cancel = true;
        } else if (!isQuoteValid(quoteStr)) {
            mQuoteEditText.setError(getString(R.string.error_invalid_quote));
            focusView = mQuoteEditText;
            cancel = true;
        } else {
            quote = Double.parseDouble(quoteStr);
        }

        // Check for a valid phone
        if (TextUtils.isEmpty(phone)) {
            mPhoneEditText.setError(getString(R.string.error_field_required));
            focusView = mPhoneEditText;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneEditText.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(client)) {
            mClientEditText.setError(getString(R.string.error_field_required));
            focusView = mClientEditText;
            cancel = true;
        } else if (!isClientValid(client)) {
            mClientEditText.setError(getString(R.string.error_invalid_client_name));
            focusView = mClientEditText;
            cancel = true;
            Log.e("WOW", "invalid");
        }

        if (cancel) {
            // There was an error; don't attempt placing order and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.e("WOW", "attempt");
            // perform the place order attempt.
            mOrder = new Order(client, phone, System.currentTimeMillis(), description, quote, 0.0, creator, 0, dueDate);
            mAddOrderTask = new AddOrderTask(this);
            mAddOrderTask.execute();
        }
    }

    /**
     * Validate day number
     */
    private boolean isDayNumberValid(String daysStr) {
        return Pattern.matches("[0-9]+", daysStr);
    }

    /**
     * Validate quote field
     */
    private boolean isQuoteValid(String quoteStr) {
        return Pattern.matches("[0-9]+(.[0-9])?", quoteStr);
    }

    /**
     * Validate description field
     */
    private boolean isDescriptionValid(String description) {
        return Pattern.matches("[\\p{Punct}a-zA-Z0-9]+", description);
    }

    /**
     * Validate Phone field
     */
    private boolean isPhoneValid(String phone) {
        return Pattern.matches("[0-9]+", phone);
    }

    /**
     * Validate Client field
     */
    private boolean isClientValid(String client) {
        Log.e("WOW", "yay");
        return Pattern.matches("[a-zA-Z]+(.)?((\\s)+[a-zA-Z]+(.)?)*", client);
    }

    /**
     * Called when an action is being performed.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.addorderformactivity_due_number:
                String dayNumberStr = mDueDayNumberView.getText().toString();
                TextView dueDateValueView = (TextView)findViewById(R.id.addorderformactivity_due_date_value);

                // Check for a valid day number
                if (TextUtils.isEmpty(dayNumberStr)) {
                    dueDateValueView.setError(getString(R.string.error_field_required));
                } else if (!isDayNumberValid(dayNumberStr)) {
                    dueDateValueView.setTextColor(Color.RED);
                    dueDateValueView.setText(R.string.error_invalid_day_number);
                } else {
                    // TODO: parse day number to actual date
                    int numberOfDays = Integer.parseInt(mDueDayNumberView.getText().toString());
                    switch (mDurationSpinner.getSelectedItemPosition()){
                        case 0:
                            break;
                        case 1:
                            numberOfDays *= 7;
                            break;
                        case 2:
                            numberOfDays *= 30;
                            break;
                        default:
                            break;
                    }
                    long dueDate = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(numberOfDays, TimeUnit.DAYS);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dueDateValueView.setText(simpleDateFormat.format(new Date(dueDate)));
                }
                return true;
        }
        return false;
    }

    private class AddOrderTask extends AsyncTask<Void, Void, Response> {

        private ProgressBar mProgressBar;
        private Context mContext;

        public AddOrderTask(Context context) {
            this.mContext = context;
            this.mProgressBar = (ProgressBar)findViewById(R.id.addorderformactivity_progress_bar);
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
        protected Response doInBackground(Void... params) {
            try {
                final String url = Constants.HTTP + Constants.SERVER_IP + Constants.PORT + Constants.API_ADDRESS + Constants.ADD_ORDER_URI;

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

                return restTemplate.postForObject(url, mOrder, Response.class);
            } catch (Exception e) {
                Log.e(getString(R.string.orderformactivity_tag), e.getMessage(), e);
            }
            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param response The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            mProgressBar.setVisibility(View.GONE);

            // Check if there is a response
            if (response != null) {
                if (response.getResult() == Constants.ADDORDER_SUCCESS) {
                    Toast.makeText(mContext, getString(R.string.addorder_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(mContext, getString(R.string.addorder_fail), Toast.LENGTH_SHORT).show();
                    mAddOrderTask = null;
                }
            }else{
                // No response from server
                Toast.makeText(mContext, getString(R.string.no_response_from_server), Toast.LENGTH_SHORT).show();
                mAddOrderTask = null;
            }

        }
    }
}
