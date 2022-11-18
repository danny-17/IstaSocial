package com.M4A.istasocial.Holders;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.M4A.istasocial.R;

public class HolderPersona extends RecyclerView.ViewHolder{

    private ImageView fotoCard;
    private TextView nombreCard;
    private LinearLayout lay;

    public HolderPersona(@NonNull View itemView) {
        super(itemView);

        fotoCard = itemView.findViewById(R.id.carUsuarios_fotoPerfil);
        nombreCard= itemView.findViewById(R.id.txtNombre_cardUsuario);
        lay = itemView.findViewById(R.id.layUsuario);

    }

    public ImageView getFotoCard() {
        return fotoCard;
    }

    public void setFotoCard(ImageView fotoCard) {
        this.fotoCard = fotoCard;
    }

    public TextView getNombreCard() {
        return nombreCard;
    }

    public void setNombreCard(TextView nombreCard) {
        this.nombreCard = nombreCard;
    }

    public LinearLayout getLay() {
        return lay;
    }

    public void setLay(LinearLayout lay) {
        this.lay = lay;
    }
}
