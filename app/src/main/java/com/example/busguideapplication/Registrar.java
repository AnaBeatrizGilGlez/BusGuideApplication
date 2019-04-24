package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;*/

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.regex.Pattern;

public class Registrar<TAG> extends AppCompatActivity /*implements GoogleApiClient.OnConnectionFailedListener*/ {

    private static final String TAG = "Signin" ;
    private EditText mail, Contraseña, Repetir;
    //private GoogleApiClient googleApiClient;
    //private SignInButton google;
    Integer aux=0;
    //FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        mail = findViewById(R.id.mail);
        Contraseña = findViewById(R.id.Contraseña);
        Repetir = findViewById(R.id.Repetir);
        //google=findViewById(R.id.google);

        //GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        //googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        /*google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });*/

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==777){
            /*GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);*/
        }
    }

   /* protected void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this, "No es posible entrar con ese correo", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void goMainScreen(){
        /*Intent intent = new Intent(this,Inicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }



    public void registrar(View view){
        String email =mail.getText().toString();
        String pass=Contraseña.getText().toString();
        String repeaters=Repetir.getText().toString();

        if ((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass))){
            Toast.makeText(Registrar.this, "No debe existir ningún campo vacío", Toast.LENGTH_LONG).show();
            return;
        }else{
            if(pass.length()<6){
                Toast.makeText(Registrar.this, "La contraseña debe ser de tamaño superior o igual a 6", Toast.LENGTH_LONG).show();
                return;
            }else {
                if(Objects.equals(pass,repeaters)){
                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                    if(pattern.matcher(email).matches()==false){
                        Toast.makeText(Registrar.this, "El e-mail no esta en formato letra@gmail.com", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(Registrar.this, "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        registrarUsuario(email,pass);

    }

    private void registrarUsuario(String email,String pass){
        Log.i(TAG,"Todo va");
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Registrar.this, "Este e-mail ya esta registrado", Toast.LENGTH_LONG).show(); //Tiene que ser mismo correo y pass para uqe pase esot revisar
                }else{
                    Log.i(TAG,"Todo va algo mejor");
                    aux=1;
                }
            }
        });

        if(aux==1) {
            Log.i(TAG,"Todo va mejor");
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(Registrar.this,"Se ha producido un error de registro", Toast.LENGTH_LONG).show();
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(null).build();
                        user.updateProfile(profile);
                        //user.sendEmailVerification();
                        Toast.makeText(Registrar.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(Registrar.this,Inicio.class));
                    }
                }
            });
        }else{
            Log.i(TAG, "FALLANDO" + aux);
        }
    }


}
