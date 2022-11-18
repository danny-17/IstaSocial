package com.M4A.istasocial.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.M4A.istasocial.R;

public class HolderMensajes extends RecyclerView.ViewHolder {

    private TextView Nombre;
    private TextView Mensaje;
    private TextView hora;
    private ImageView fotoPerfil;
    private ImageView fotoMensaje;

    public HolderMensajes(@NonNull View itemView) {
        super(itemView);
        Nombre = (TextView) itemView.findViewById(R.id.nombreMensaje);
        Mensaje = (TextView) itemView.findViewById(R.id.mensajeMensaje);
        hora = (TextView) itemView.findViewById(R.id.horaMensaje);
        fotoPerfil = (ImageView) itemView.findViewById(R.id.FotoPerfilMensaje);
        fotoMensaje=(ImageView) itemView.findViewById(R.id.imagenMensaje);
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }

    public TextView getNombre() {
        return Nombre;
    }

    public void setNombre(TextView nombre) {
        Nombre = nombre;
    }

    public TextView getMensaje() {
        return Mensaje;
    }

    public void setMensaje(TextView mensaje) {
        Mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public ImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(ImageView fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
