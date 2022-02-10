package com.fisei.athanasiaapp.services;


import com.fisei.athanasiaapp.models.SaleDetails_MSJM;
import com.fisei.athanasiaapp.models.SaleRequest_MSJM;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal_MSJM;
import com.fisei.athanasiaapp.objects.Order_MSJM;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.utilities.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SaleService {
    public static List<Order_MSJM> GetAllSales(){
        List<Order_MSJM> orderMSJMList = new ArrayList<>();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.SALES);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal_MSJM.ACTUAL_USER.JWT);
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject orders = list.getJSONObject(i);
                    orderMSJMList.add(new Order_MSJM(
                            orders.getInt("id"),
                            orders.getString("date"),
                            orders.getInt("iduserClient"),
                            orders.getDouble("total")));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return orderMSJMList;
    }
    public static List<Order_MSJM> GetSalesByUserID(){
        List<Order_MSJM> orderMSJMList = new ArrayList<>();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.SALES + "/" + AthanasiaGlobal_MSJM.ACTUAL_USER.ID);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal_MSJM.ACTUAL_USER.JWT);
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject orders = list.getJSONObject(i);
                    orderMSJMList.add(new Order_MSJM(
                            orders.getInt("id"),
                            orders.getString("date"),
                            orders.getInt("iduserClient"),
                            orders.getDouble("total")));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return orderMSJMList;
    }
    public static List<Product> GetSalesDetailsByID(int id){
        List<Product> orderList = new ArrayList<>();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.SALE_DETAILS + id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal_MSJM.ACTUAL_USER.JWT);
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject orders = list.getJSONObject(i);
                    orderList.add(new Product(
                            orders.getInt("idproduct"),
                           "", "",
                            orders.getInt("quantity"),
                            0, 0, ""));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return orderList;
    }
    public static boolean AddNewSale(SaleRequest_MSJM sale){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URLs.SALES);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal_MSJM.ACTUAL_USER.JWT);
            connection.setDoInput(true);
            String jsonInputPart1 = "{\"IDUserClient\": " + sale.UserClientID + ",\"saleDetails\":[";
            StringBuilder jsonInputPart2 = new StringBuilder();
            for(SaleDetails_MSJM detail: sale.SaleDetails_MSJM){
                jsonInputPart2.append("{\"IDProduct\": " + detail.ProductID +
                        ",\"Quantity\": " + detail.Quantity +
                        "},");
            }
            String jsonInput = jsonInputPart1 + jsonInputPart2.substring(0, jsonInputPart2.length() - 1) + "]}";
            try(OutputStream os = connection.getOutputStream()){
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                return data.getBoolean("success");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return false;
    }
}