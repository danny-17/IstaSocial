package com.M4A.istasocial.modelo.Logica;

import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Lpersona {

    private persona p;
    private String id;

    public Lpersona(persona p, String id) {
        this.p = p;
        this.id = id;
    }

    public persona getP() {
        return p;
    }

    public void setP(persona p) {
        this.p = p;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String fechaCreacion(){
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UsuarioDAO.getInstancia().fechaCreacion());
        return fecha.format(date);

    }
    public String obtenerUltimaConexion(){
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UsuarioDAO.getInstancia().ultimaConexion());
        return fecha.format(date);

    }


}
