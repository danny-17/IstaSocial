package com.M4A.istasocial.Persistencia;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.M4A.istasocial.modelo.FireBase.Mensaje;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.M4A.istasocial.modelo.Logica.Lpersona;
import com.M4A.istasocial.utilidades.Constantes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsuarioDAO {

    public interface IDevolverUsuario{
        public void devolverUsuario(Lpersona lp);
        public void error(String e);
    }

    public interface IDevolverURlfoto{
        public void devolverURLstring (String url);
    }

    private static UsuarioDAO usuarioPer;
    private FirebaseDatabase base;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference reference;

    public static UsuarioDAO getInstancia(){
        if(usuarioPer==null)
            usuarioPer= new UsuarioDAO();
            return usuarioPer;

    }

    private UsuarioDAO (){
        base = FirebaseDatabase.getInstance();
        reference = base.getReference(Constantes.NODOUSUARIOS);
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference("Fotos/FotosPerfil/"+getKeyUsuario());

    }

    public static String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }


    public void ObtenerUsuarioporLlave(final String key, final IDevolverUsuario iDevolverUsuario){
        reference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
persona persona= dataSnapshot.getValue(persona.class);
Lpersona lpersona= new Lpersona(persona,key);
iDevolverUsuario.devolverUsuario(lpersona);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                iDevolverUsuario.error(databaseError.getMessage());

            }
        });

    }



    public long fechaCreacion(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();

    }

    public long ultimaConexion(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();

    }

    public void añadirfotoPerfil(){
reference.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Lpersona> lista = new ArrayList<>();
        for (DataSnapshot childData : dataSnapshot.getChildren()){
persona p =childData.getValue(persona.class);
Lpersona lpersona = new Lpersona(p,childData.getKey());
lista.add(lpersona);
        }

        for(Lpersona persona : lista){

            if (persona.getP().getUrlFotoPerfil()==null){
                reference.child(persona.getId()).child("urlFotoPerfil").setValue(Constantes.URLDEFECTOUSUARIOS);
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
        }

        public void subirFotoUri (Uri u, final IDevolverURlfoto url){
String nombrePH="";
            Date date = new Date();
            SimpleDateFormat formt= new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
            nombrePH= formt.format(date);

            final StorageReference fotoReferencia = storageReference.child(nombrePH);
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
                        url.devolverURLstring(uri.toString());
                    }
                }
            });
        }

        public boolean isLogeado(){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        return firebaseUser!= null;
        }
}
