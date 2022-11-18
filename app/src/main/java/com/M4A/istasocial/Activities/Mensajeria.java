package com.M4A.istasocial.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.M4A.istasocial.Adapters.AdapterMensajes;
import com.M4A.istasocial.Holders.HolderMensajes;
import com.M4A.istasocial.Persistencia.MensajeriaDAO;
import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.R;
import com.M4A.istasocial.modelo.FireBase.Mensaje;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.M4A.istasocial.modelo.Logica.LMensaje;
import com.M4A.istasocial.modelo.Logica.Lpersona;
import com.M4A.istasocial.utilidades.Constantes;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.core.ImagePickerImpl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mensajeria extends AppCompatActivity {

    private EditText mensaje;
    private ImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private Button enviar;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;
    private Button cerrarSesion;

private FirebaseStorage storage;
private StorageReference storageReference;
    private FirebaseAuth mAuth;


private static final int ph_send=1;
    private static final int ph_perfil=2;

private String fotoperfilUrl;
private String NombreUsuario;
private String keyreceptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
keyreceptor=bundle.getString("key_receptor");
NombreUsuario = bundle.getString("nombre_receptor");
fotoperfilUrl=bundle.getString("url_foto_receptor");

        }else{
            finish();
        }
cerrarSesion= (Button) findViewById(R.id.btn_mensajes_CerrarSesion);
        mensaje = (EditText) findViewById(R.id.txtMensaje);
        fotoPerfil = (ImageView) findViewById(R.id.FotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        enviar = (Button) findViewById(R.id.enviar);
        btnEnviarFoto= (ImageButton) findViewById(R.id.btnEnviarFoto);


        Uri perf = Uri.parse(fotoperfilUrl);
        fotoPerfil.setImageURI(perf);

        nombre.setText(NombreUsuario);



        storage= FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new AdapterMensajes(this);
        LinearLayoutManager lay = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(lay);
        rvMensajes.setAdapter(adapter);

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecione una imagen"),ph_send);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

fotoPerfil.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/jpg");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(i,"Selecione una imagen"),ph_perfil);
    }
});

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ValMen = mensaje.getText().toString();
                if (! ValMen.isEmpty()){
                    Mensaje m = new Mensaje();
                    m.setMensaje(ValMen);
                    m.setContienefoto(false);
m.setIdEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                    MensajeriaDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),keyreceptor,m);
                    mensaje.setText("");
                }

            }
        });


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });


        FirebaseDatabase.getInstance().
                getReference(Constantes.NODOMENSAJES).
                child(UsuarioDAO.getInstancia().getKeyUsuario()).
                child(keyreceptor).
                addChildEventListener(new ChildEventListener() {

Map<String, Lpersona> mapPersonaTemp = new HashMap<>();


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, final String s) {
                final Mensaje men= dataSnapshot.getValue(Mensaje.class);

                final LMensaje logi = new LMensaje(men,dataSnapshot.getKey());
                final int posicion = adapter.aggMensaje(logi);

                if ( mapPersonaTemp.get(men.getIdEmisor())!=null){
          logi.setLpersona(mapPersonaTemp.get(men.getIdEmisor()));
          adapter.actualizarMensajes(posicion,logi);
                }else{
                    UsuarioDAO.getInstancia().ObtenerUsuarioporLlave(men.getIdEmisor(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(Lpersona lp) {
                            mapPersonaTemp.put(men.getIdEmisor(),lp);
                            logi.setLpersona(lp);
                            adapter.actualizarMensajes(posicion,logi);
                        }

                        @Override
                        public void error(String e) {
                            Toast.makeText(Mensajeria.this,"Error: "+e , Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        verifyStoragePermissions(this);

    }

    private void returnLogin() {
        startActivity(new Intent(Mensajeria.this, Login .class));
        finish();
    }


    private void setScrollBar(){

        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==ph_send && resultCode== RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        Mensaje mensaje = new Mensaje();
                        mensaje.setMensaje("El uuario ha enviado una foto");
                        //adapter.onBindViewHolder(,resultCode);
                        mensaje.setUrlPh(uri.toString());
                        mensaje.setContienefoto(true);
                        mensaje.setIdEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                        MensajeriaDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),keyreceptor,mensaje);

                    }
                }
            });
        } /*else if(requestCode==ph_perfil && resultCode== RESULT_OK){
            Uri u = data.getData();
            storageReference= storage.getReference("imagenesPerfil_redSocial"); // imagenes_RedSocial
            final StorageReference photo = storageReference.child(u.getLastPathSegment());
            photo.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    fotoperfilUrl= u.toString();
                    MensajeEnviar m = new MensajeEnviar(NombreUsuario+" cambio su foto",fotoperfilUrl,NombreUsuario,"2",u.toString(),ServerValue.TIMESTAMP);
                    bdreference.push().setValue(m);
                    Glide.with(Mensajeria.this).load(u.toString()).into(fotoPerfil);
                }
            });
        }*/
    }

   /*@Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            enviar.setEnabled(false);
            //DatabaseReference ref = bd.getReference("Persona/"+currentUser.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    persona per = dataSnapshot.getValue(persona.class);
                    NombreUsuario = per.getNombre();
                    nombre.setText(NombreUsuario);
                    enviar.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            returnLogin();
        }
    }*/

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
                Intent intent = new Intent(Mensajeria.this,ListadoUsuarios.class);
                startActivity(intent);
                break;


            case R.id.menItem_Registro:
                Intent intentreg = new Intent(Mensajeria.this,Registro.class);
                startActivity(intentreg);
                break;

            default :
                break;

        }
        return true;
    }



}
