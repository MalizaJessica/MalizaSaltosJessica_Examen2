package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.fisei.athanasiaapp.adapters.OrderDetailsArrayAdapter_MSJM;
import com.fisei.athanasiaapp.objects.OrderDetail_MSJM;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.services.ProductService_MSJM;
import com.fisei.athanasiaapp.services.SaleService_MSJM;
import com.fisei.athanasiaapp.utilities.Utils_MSJM;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity_MSJM extends AppCompatActivity {
    private int orderID = 0;

    private TextView textViewOrderID;
    private TextView textViewOrderUserClient;
    private TextView textViewOrderDate;
    private TextView textViewOrderTotal;
    private TextView textViewOrderTotalIVA;
    private ListView listViewOrderDetails;

    private OrderDetailsArrayAdapter_MSJM orderArrayAdapter;

    Bundle bundle;
    private List<OrderDetail_MSJM> orderDetailMSJMS = new ArrayList<>();
    private List<Product> saleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        bundle = getIntent().getExtras();
        orderID = bundle.getInt("orderID");
        InitializeViewComponents();
        orderArrayAdapter = new OrderDetailsArrayAdapter_MSJM(this, orderDetailMSJMS);

        GetOrderDetailsTask getOrderDetailsTask = new GetOrderDetailsTask();
        getOrderDetailsTask.execute();
    }

    class GetOrderDetailsTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            saleDetails = SaleService_MSJM.GetSalesDetailsByID(orderID);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            GetProductTask getProductTask = new GetProductTask();
            getProductTask.execute();
        }
    }
    class GetProductTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            for (Product item: saleDetails) {
                orderDetailMSJMS.add(ConvertProductToOrderDetail(ProductService_MSJM.GetSpecifiedProductByID(item.id), item.quantity));
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            orderArrayAdapter.notifyDataSetChanged();
            listViewOrderDetails.setAdapter(orderArrayAdapter);
            //orderDetails.clear();
        }
    }
    private OrderDetail_MSJM ConvertProductToOrderDetail(Product product, int qty){
        return new OrderDetail_MSJM(product.name, qty, product.unitPrice, product.imageURL);
    }
    private void InitializeViewComponents(){
        textViewOrderID = (TextView) findViewById(R.id.textViewOrderInfoID);
        textViewOrderUserClient = (TextView) findViewById(R.id.textViewOrderInfoUserClient);
        textViewOrderDate = (TextView) findViewById(R.id.textViewOrderInfoDate);
        textViewOrderTotal = (TextView) findViewById(R.id.textViewOrderInfoTotal);
        listViewOrderDetails = (ListView) findViewById(R.id.listViewOrderDetails);
        textViewOrderTotalIVA = (TextView) findViewById(R.id.textViewOrderInfoTotalIva);
        FillOrderHeader();
    }
    private void FillOrderHeader(){
        textViewOrderID.setText(String.format("%s",bundle.getInt("orderID")));
        textViewOrderUserClient.setText(String.format("%s",bundle.getInt("orderUserClient")));
        textViewOrderDate.setText(String.format("%s", Utils_MSJM.ConvertDate(bundle.getString("orderDate"))));
        textViewOrderTotal.setText(String.format("%s",bundle.getDouble("orderTotal") + " $"));
        textViewOrderTotalIVA.setText(String.format("%.2f",bundle.getDouble("orderTotal") / 1.1 )+ " $");
    }
}