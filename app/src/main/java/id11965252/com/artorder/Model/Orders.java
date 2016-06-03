package id11965252.com.artorder.Model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Orders class holds a list of Order
 */
@Root(name = "orders")
public class Orders {

    @ElementList(entry = "order", inline = true)
    private ArrayList<Order> orders;

    public Orders(){
    }

    public void setOrders(ArrayList<Order> orders){
        this.orders = orders;
    }

    public ArrayList<Order> getOrdersList(){
        return orders;
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }
}
