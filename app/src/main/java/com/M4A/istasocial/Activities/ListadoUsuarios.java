package com.M4A.istasocial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.M4A.istasocial.Holders.HolderPersona;
import com.M4A.istasocial.R;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.M4A.istasocial.modelo.Logica.Lpersona;
import com.M4A.istasocial.utilidades.Constantes;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListadoUsuarios extends AppCompatActivity {

    private RecyclerView rvUsuario ;


    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuarios);
        rvUsuario = findViewById(R.id.rvListadoUsuarios);
        LinearLayoutManager man = new LinearLayoutManager(this);
        rvUsuario.setLayoutManager(man);
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constantes.NODOUSUARIOS);

        FirebaseRecyclerOptions<persona> options =
                new FirebaseRecyclerOptions.Builder<persona>()
                        .setQuery(query, persona.class)
                        .build();


         adapter = new FirebaseRecyclerAdapter<persona, HolderPersona>(options) {
            @Override
            public HolderPersona onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_usuario, parent, false);

                return new HolderPersona(view);
            }

            @Override
            protected void onBindViewHolder(HolderPersona holder, int position, final persona model) {
                Glide.with(ListadoUsuarios.this).load(model.getUrlFotoPerfil()).into(holder.getFotoCard());
                holder.getNombreCard().setText(model.getNombre());
                final Lpersona lper = new Lpersona(model,getSnapshots().getSnapshot(position).getKey());
                holder.getLay().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in= new Intent(ListadoUsuarios.this,Mensajeria.class);
                        in.putExtra("key_receptor" , lper.getId());
                        in.putExtra("nombre_receptor", lper.getP().getNombre());
                        in.putExtra("url_foto_receptor",lper.getP().getUrlFotoPerfil());
                        Toast.makeText(ListadoUsuarios.this,"Usuario: "+lper.getP().getUrlFotoPerfil() , Toast.LENGTH_SHORT).show();

                        startActivity(in);
                        Toast.makeText(ListadoUsuarios.this,"Usuario: "+lper.getId() , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        rvUsuario.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()){

            case R.id.menItem_Cerrar:
                FirebaseAuth.getInstance().signOut();
                returnLogin();
                break;


            case R.id.menItem_Lista:
                Intent intent = new Intent(ListadoUsuarios.this,ListadoUsuarios.class);
                startActivity(intent);
                break;


            case R.id.menItem_Registro:
                Intent intentreg = new Intent(ListadoUsuarios.this,Registro.class);
                startActivity(intentreg);
                break;

            default :
                break;

        }
        return true;
    }

    private void returnLogin() {
        startActivity(new Intent(ListadoUsuarios.this, Login .class));
        finish();
    }

}
