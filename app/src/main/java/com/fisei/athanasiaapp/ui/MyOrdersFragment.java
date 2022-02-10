package com.fisei.athanasiaapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.adapters.OrderArrayAdapter_MSJM;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal_MSJM;
import com.fisei.athanasiaapp.objects.Order_MSJM;
import com.fisei.athanasiaapp.services.SaleService;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private List<Order_MSJM> myOrderMSJMList = new ArrayList<>();
    private OrderArrayAdapter_MSJM orderArrayAdapterMSJM;
    private ListView listView;
    private Bundle bundle = new Bundle();

    public MyOrdersFragment() {
    }
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        listView = (ListView) view.findViewById(R.id.listViewMyOrdersFragment);
        orderArrayAdapterMSJM = new OrderArrayAdapter_MSJM(getContext(), myOrderMSJMList);
        GetOrderTask getOrderTask = new GetOrderTask();
        getOrderTask.execute();

        return view;
    }
    class GetOrderTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            myOrderMSJMList.clear();
            if(AthanasiaGlobal_MSJM.ADMIN_PRIVILEGES){
                myOrderMSJMList = SaleService.GetAllSales();
            } else {
                myOrderMSJMList = SaleService.GetSalesByUserID();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            orderArrayAdapterMSJM.clear();
            orderArrayAdapterMSJM.addAll(myOrderMSJMList);
            orderArrayAdapterMSJM.notifyDataSetChanged();
            listView.setAdapter(orderArrayAdapterMSJM);
        }
    }
}