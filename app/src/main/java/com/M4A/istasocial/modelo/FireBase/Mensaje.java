package com.M4A.istasocial.modelo.FireBase;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    private String mensaje;

    private String urlPh;
    private boolean contienefoto;
    private String idEmisor;
    private Object createdTimestamp;

    public Mensaje() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }


    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getUrlPh() {
        return urlPh;
    }

    public void setUrlPh(String urlPh) {
        this.urlPh = urlPh;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isContienefoto() {
        return contienefoto;
    }

    public void setContienefoto(boolean contienefoto) {
        this.contienefoto = contienefoto;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }
}
