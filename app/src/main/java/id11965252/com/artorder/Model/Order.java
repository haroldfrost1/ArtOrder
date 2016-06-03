package id11965252.com.artorder.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;

/**
 * The art order object class
 */
@Root(name = "order", strict = false)
public class Order {

    @Element(name = "id")
    private int mId;

    @Element(name = "client")
    private String mClient;

    @Element(name = "phone")
    private String mPhone;

    @Element(name = "date")
    private long mDate;

    @Element(name = "description")
    private String mDescription;

    @Element(name = "quote")
    private double mQuote;

    @Element(name = "receipt")
    private double mReceipt;

    @Element(name = "archiveDate")
    private long mArchiveDate;

    @Element(name = "status")
    private int mStatus;

    @Element(name = "dueDate")
    private long mDueDate;

    // who creates the order
    @Element(name = "creator")
    private String mCreator;

    public Order() {
//        mId = 0;
//        mClient = "";
//        mPhone = "";
//        mDescription = "";
//        mQuote = 0.00;
//        mReceipt = 0.00;
//        mCreator = "";
//        mStatus = 0;
//        mDate = 0;
//        mArchiveDate = 0;
//        mDueDate = 0;
    }

    public Order(String client, String phone, long date, String description, double quote, double receipt, String creator, int status, long dueDate) {
        this.mId = 0;
        this.mClient = client;
        this.mPhone = phone;
        this.mDate = date;
        this.mDescription = description;
        this.mQuote = quote;
        this.mReceipt = receipt;
        this.mCreator = creator;
        this.mStatus = status;
        this.mDueDate = dueDate;
        this.mArchiveDate = 0;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }


    public String getClient() {
        return mClient;
    }

    public void setClient(String client) {
        this.mClient = client;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public double getQuote() {
        return mQuote;
    }

    public void setQuote(double quote) {
        this.mQuote = quote;
    }

    public double getReceipt() {
        return mReceipt;
    }

    public void setReceipt(double receipt) {
        this.mReceipt = receipt;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        this.mCreator = creator;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public long getArchiveDate() {
        return mArchiveDate;
    }

    public void setArchiveDate(long archiveDate) {
        this.mArchiveDate = archiveDate;
    }

    public long getDueDate() {
        return mDueDate;
    }

    public void setDueDate(long dueDate) {
        this.mDueDate = dueDate;
    }

    public String getDueDateToString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm");
        return simpleDateFormat.format(mDueDate);
    }
}

