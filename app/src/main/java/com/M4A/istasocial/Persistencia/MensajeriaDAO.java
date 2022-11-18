package com.M4A.istasocial.Persistencia;

import com.M4A.istasocial.Activities.Mensajeria;
import com.M4A.istasocial.modelo.FireBase.Mensaje;
import com.M4A.istasocial.utilidades.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MensajeriaDAO {
    private static MensajeriaDAO mensajeriaDAO;
    private FirebaseDatabase base;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference reference;

    public static MensajeriaDAO getInstancia(){
        if(mensajeriaDAO==null)
            mensajeriaDAO= new MensajeriaDAO();
        return mensajeriaDAO;

    }

    private MensajeriaDAO (){
        base = FirebaseDatabase.getInstance();
        reference = base.getReference(Constantes.NODOMENSAJES);
        //storage = FirebaseStorage.getInstance();
        //storageReference=storage.getReference("Fotos/FotosPerfil/"+getKeyUsuario());

    }

    public void nuevoMensaje(String Keyemisor , String keyReceptor , Mensaje m){
        DatabaseReference refEmi = reference.child(Keyemisor).child(keyReceptor);
        DatabaseReference refRec = reference.child(keyReceptor).child(Keyemisor);
        refEmi.push().setValue(m);
        refRec.push().setValue(m);

    }
}
