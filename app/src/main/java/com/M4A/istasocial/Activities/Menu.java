package com.M4A.istasocial.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.R;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        Intent intent = new Intent(Menu.this,ListadoUsuarios.class);
        startActivity(intent);


        /*cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        verUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,ListadoUsuarios.class);
                startActivity(intent);
            }
        });*/
    }

    private void returnLogin() {
        startActivity(new Intent(Menu.this, Login .class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UsuarioDAO.getInstancia().isLogeado() ){


        }else {
            returnLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
