package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fisei.athanasiaapp.OrderDetailsActivity_MSJM;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.objects.Order_MSJM;
import com.fisei.athanasiaapp.utilities.Utils;

import java.util.List;

public class OrderArrayAdapter_MSJM extends ArrayAdapter<Order_MSJM> {
    private static class ViewHolder{
        TextView orderDateView;
        TextView orderIDView;
        TextView orderTotalView;
        Button orderInfoBtn;
    }

    public OrderArrayAdapter_MSJM(Context context, List<Order_MSJM> orderMSJMList) {
        super(context, -1, orderMSJMList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Order_MSJM orderMSJM = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_myorders, parent, false);
            viewHolder.orderDateView = (TextView) convertView.findViewById(R.id.textViewOrderDate);
            viewHolder.orderIDView = (TextView) convertView.findViewById(R.id.textViewOrderId);
            viewHolder.orderTotalView = (TextView) convertView.findViewById(R.id.textViewOrderTotal);
            viewHolder.orderInfoBtn = (Button) convertView.findViewById(R.id.btnOrderInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderDateView.setText(Utils.ConvertDate(orderMSJM.Date));
        viewHolder.orderIDView.setText(String.format("%s", orderMSJM.ID));
        viewHolder.orderTotalView.setText(String.format("%s", orderMSJM.Total + " $"));
        viewHolder.orderInfoBtn.setOnClickListener(view -> {
            ShowOrderDetails(orderMSJM);
        });
        return convertView;
    }

    private void ShowOrderDetails(Order_MSJM orderMSJM){
        Intent orderDetails = new Intent(getContext(), OrderDetailsActivity_MSJM.class);
        orderDetails.putExtra("orderID", orderMSJM.ID);
        orderDetails.putExtra("orderUserClient", orderMSJM.UserClientID);
        orderDetails.putExtra("orderDate", orderMSJM.Date);
        orderDetails.putExtra("orderTotal", orderMSJM.Total);
        getContext().startActivity(orderDetails);
    }
}