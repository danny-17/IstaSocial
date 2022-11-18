package com.M4A.istasocial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.M4A.istasocial.Holders.HolderMensajes;
import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.R;
import com.M4A.istasocial.modelo.Logica.LMensaje;
import com.M4A.istasocial.modelo.Logica.Lpersona;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensajes> {

    private List<LMensaje> listaMensajes= new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public int aggMensaje(LMensaje m){
       listaMensajes.add(m);
       notifyItemInserted(listaMensajes.size());
       int posicion = listaMensajes.size()-1;
       return posicion;
    }
 public void actualizarMensajes(int posicion, LMensaje m){
        listaMensajes.set(posicion,m);
        notifyItemChanged(posicion);


}
    @NonNull
    @Override
    public HolderMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v ;
        if ( viewType==1){
             v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_emisor ,parent,false);
        }else  {
            v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor, parent, false);
        }
//        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor,parent,false);
        return new HolderMensajes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensajes holder, int position) {

        LMensaje e = listaMensajes.get(position);
        Lpersona lp = e.getLpersona();

        if (lp!= null){
            holder.getNombre().setText(lp.getP().getNombre());
            Glide.with(c).load(lp.getP().getUrlFotoPerfil()).into(holder.getFotoPerfil());
        }

        holder.getMensaje().setText(e.getMensaje().getMensaje());

        if(e.getMensaje().isContienefoto()){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(e.getMensaje().getUrlPh()).into(holder.getFotoMensaje());
        }else{
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }


//holder.getHora().setText(listaMensajes.get(position).getHora());



        holder.getHora().setText(e.fechaCreacionMensaje());
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (listaMensajes.get(position).getLpersona()!=null){
            if (listaMensajes.get(position).getLpersona().getId().equals(UsuarioDAO.getKeyUsuario())){
                return 1;
            }else{
                return -1;
            }
        }else {
            return -1;
        }



        //return super.getItemViewType(position);
    }
}
