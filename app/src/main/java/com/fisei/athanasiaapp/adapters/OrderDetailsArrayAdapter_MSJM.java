package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fisei.athanasiaapp.objects.OrderDetail_MSJM;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.services.ImageService_MSJM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsArrayAdapter_MSJM extends ArrayAdapter<OrderDetail_MSJM> {
    private static class ViewHolder{
        TextView orderDetailNameTextView;
        TextView orderDetailQuantityTextView;
        TextView orderDetailUnitPriceTextView;
        ImageView orderDetailImageView;
    }
    private final Map<String, Bitmap> bitmaps = new HashMap<>();
    public OrderDetailsArrayAdapter_MSJM(Context context, List<OrderDetail_MSJM> orderDetailMSJMList){
        super(context, -1, orderDetailMSJMList);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        OrderDetail_MSJM orderDetailMSJM = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_order_details, parent, false);
            viewHolder.orderDetailNameTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailName);
            viewHolder.orderDetailQuantityTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailQuantity);
            viewHolder.orderDetailUnitPriceTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailUnitPrice);
            viewHolder.orderDetailImageView = (ImageView) convertView.findViewById(R.id.imageViewOrderDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(bitmaps.containsKey(orderDetailMSJM.ImageURL)){
            viewHolder.orderDetailImageView.setImageBitmap(bitmaps.get(orderDetailMSJM.ImageURL));
        } else {
            new LoadImageTask(viewHolder.orderDetailImageView).execute(orderDetailMSJM.ImageURL);
        }
        viewHolder.orderDetailNameTextView.setText(orderDetailMSJM.Name);
        viewHolder.orderDetailQuantityTextView.setText(String.format("%s", orderDetailMSJM.Quantity));
        viewHolder.orderDetailUnitPriceTextView.setText(String.format("%s", orderDetailMSJM.UnitPrice) + " $");
        return convertView;
    }
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;
        public LoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params){
            Bitmap bitmap = ImageService_MSJM.GetImageByURL(params[0]);
            bitmaps.put(params[0], bitmap);
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }
    }
}