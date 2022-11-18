package com.M4A.istasocial.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText loginCorreo;
    private EditText loginContra;
    private Button btnLogin,btnRegistro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        loginCorreo = (EditText) findViewById(R.id.correoLogin);
        loginContra = (EditText) findViewById(R.id.contraseñaLogin);
        btnLogin = (Button) findViewById(R.id.btn_Login_entrar);
        btnRegistro= (Button) findViewById(R.id.btn_Login_registrar);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = loginCorreo.getText().toString();
                if (isValidEmail(correo) && validarContra()){

                    String pass =loginContra.getText().toString();
                    mAuth.signInWithEmailAndPassword(correo, pass)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this,"Bienvenido" , Toast.LENGTH_SHORT).show();
                                        nextActivity();
                                    } else {
                                        Toast.makeText(Login.this,"Credenciales incorrectas" , Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }else{
                    Toast.makeText(Login.this,"Error" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registro.class));
            }
        });

        //UsuarioDAO.getInstancia().añadirfotoPerfil();
    }


    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public boolean validarContra() {
        String contra;
        contra=loginContra.getText().toString();

            if (contra.length()<=16 && contra.length()>=6){
                return true;
            } else{
                return false;
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(Login.this,"Usuario Logeado" , Toast.LENGTH_SHORT).show();
            nextActivity();
        }
    }

    public void nextActivity(){

       startActivity(new Intent(Login.this, Menu.class));
        finish();
    }
}
