package com.M4A.istasocial.modelo.Logica;

import com.M4A.istasocial.modelo.FireBase.Mensaje;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LMensaje {

    private Mensaje mensaje;
    private String id;
private Lpersona lpersona;

    public LMensaje(Mensaje mensaje, String id) {
        this.mensaje = mensaje;
        this.id = id;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lpersona getLpersona() {
        return lpersona;
    }

    public void setLpersona(Lpersona lpersona) {
        this.lpersona = lpersona;
    }

    public long getCreatedTimeStampLong (){
        return (long) mensaje.getCreatedTimestamp();
    }

    public String fechaCreacion(){
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(getCreatedTimeStampLong());
        return fecha.format(date);

    }

public String fechaCreacionMensaje() {
    Date d = new Date(getCreatedTimeStampLong());
    PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
return prettyTime.format(d);

    /*Date d = new Date(getCreatedTimeStampLong());
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a",Locale.getDefault());//a pm o am
    return sdf.format(d);*/
}

}
