package com.fisei.athanasiaapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.adapters.ProductArrayAdapter_MSJM;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.services.ProductService_MSJM;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment_MSJM extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private ProductArrayAdapter_MSJM productArrayAdapterMSJM;

    private ListView listView;

    public ProductsFragment_MSJM() {
    }
    public static ProductsFragment_MSJM newInstance(String param1, String param2) {
        ProductsFragment_MSJM fragment = new ProductsFragment_MSJM();
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
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        listView = (ListView) view.findViewById(R.id.listViewProductFragment);
        productArrayAdapterMSJM = new ProductArrayAdapter_MSJM(getContext(), QuitProductsWith0Qty(productList));
        GetProductsTask getProductsTask = new GetProductsTask();
        getProductsTask.execute();

        return view;
    }

    class GetProductsTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            productList.clear();
            productList = ProductService_MSJM.GetAllProducts();
            productList = QuitProductsWith0Qty(productList);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            productArrayAdapterMSJM.clear();
            productArrayAdapterMSJM.addAll(QuitProductsWith0Qty(productList));
            productArrayAdapterMSJM.notifyDataSetChanged();
            listView.setAdapter(productArrayAdapterMSJM);
        }
    }
    private List<Product> QuitProductsWith0Qty(List<Product> list){
        for (int x = 0; x < list.size(); x++) {
                if (list.get(x).quantity == 0) {
                    list.remove(x);
                    x = 0;
                }
        }
        return list;
    }
}