package com.fisei.athanasiaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fisei.athanasiaapp.objects.AthanasiaGlobal_MSJM;
import com.fisei.athanasiaapp.objects.UserClient_MSJM;
import com.fisei.athanasiaapp.services.UserClientService_MSJM;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.fisei.athanasiaapp.databinding.ActAthanasiaBinding;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class AthanasiaActivity_MSJM extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActAthanasiaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActAthanasiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarAthanasia.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_shop, R.id.nav_orders, R.id.nav_shop_cart).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_athanasia);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        if(AthanasiaGlobal_MSJM.ADMIN_PRIVILEGES){
            Menu menuNav=navigationView.getMenu();
            menuNav.removeItem(R.id.nav_shop_cart);
        }
        GetUserClientInfoTask getUserClientInfo = new GetUserClientInfoTask();
        getUserClientInfo.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.athanasia, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            LogOut();
        }
        return false;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_athanasia);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
    private class GetUserClientInfoTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            String tempToken = AthanasiaGlobal_MSJM.ACTUAL_USER.JWT;
            AthanasiaGlobal_MSJM.ACTUAL_USER = UserClientService_MSJM.GetUserInfoByID(AthanasiaGlobal_MSJM.ACTUAL_USER.ID);
            AthanasiaGlobal_MSJM.ACTUAL_USER.JWT = tempToken;
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //Las vistas no pueden ser editadas en un hilo diferente al main
            if(AthanasiaGlobal_MSJM.ADMIN_PRIVILEGES){
                ShowAdminInfo();
            } else {
                ShowUserInfo();
            }
        }
    }


    private void ShowUserInfo(){
        TextView userName = (TextView) findViewById(R.id.textViewUserName);
        userName.setText(AthanasiaGlobal_MSJM.ACTUAL_USER.Name);
        TextView userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText(AthanasiaGlobal_MSJM.ACTUAL_USER.Email);
    }
    private void ShowAdminInfo(){
        TextView userName = (TextView) findViewById(R.id.textViewUserName);
        userName.setText("ADMIN");
        TextView userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText("ADMIN");
    }
    private void LogOut(){
        AthanasiaGlobal_MSJM.ACTUAL_USER = new UserClient_MSJM();
        AthanasiaGlobal_MSJM.ADMIN_PRIVILEGES = false;
        AthanasiaGlobal_MSJM.SHOPPING_CART = new ArrayList<>();
        Intent login = new Intent(this, LoginActivity_MSJM.class);
        startActivity(login);
        finish();
    }
}