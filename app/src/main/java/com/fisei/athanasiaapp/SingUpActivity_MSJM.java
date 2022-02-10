package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fisei.athanasiaapp.models.ResponseAthanasia_MSJM;
import com.fisei.athanasiaapp.objects.UserClient_MSJM;
import com.fisei.athanasiaapp.services.UserClientService_MSJM;

import org.json.JSONObject;

import java.net.URL;

public class SingUpActivity_MSJM extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCedula;
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonSignUp;
    private ResponseAthanasia_MSJM responseTask = new ResponseAthanasia_MSJM(false, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        InitializeViewComponents();



    }
    private class SignUpTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            UserClient_MSJM newUser = new UserClient_MSJM(0, editTextName.getText().toString(),
                    editTextEmail.getText().toString() + "@ath.com",
                    editTextCedula.getText().toString(), "");
            responseTask = UserClientService_MSJM.SignUpNewUser(newUser, editTextPassword.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(responseTask.Success){
                StartLoginActivity();
            } else {
                errorTextView.setText(responseTask.Message);
            }
            responseTask.Success = false;
        }
    }
    private void InitializeViewComponents(){
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        editTextName = (EditText) findViewById(R.id.editTextSignUpName);
        editTextCedula = (EditText) findViewById(R.id.editTextSignUpCedula);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        errorTextView = (TextView) findViewById(R.id.textViewSignUpFail1);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(signUpButtonClicked);
    }
    private void SignUp(){
        if(editTextEmail.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() ||
                editTextCedula.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            errorTextView.setText(R.string.fields_empty_error);
        } else {
            if(editTextPassword.getText().toString().length()<6 || editTextPassword.getText().toString().length()>10){
                errorTextView.setText("La contrasenia esta incorrecta");
            }else{
            errorTextView.setText("contrasenia correcta");
            SignUpTask signUpTask = new SignUpTask();
            signUpTask.execute();
        }
    }
    private void StartLoginActivity(){
        Intent backLogin = new Intent(this, LoginActivity_MSJM.class);
        startActivity(backLogin);
        Toast.makeText(this, "Your register was successful", Toast.LENGTH_SHORT).show();
    }
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();

}