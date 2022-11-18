package com.M4A.istasocial.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.M4A.istasocial.Persistencia.UsuarioDAO;
import com.M4A.istasocial.R;
import com.M4A.istasocial.modelo.FireBase.persona;
import com.M4A.istasocial.utilidades.Constantes;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Registro extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText correo;
    private EditText contrasenia;
    private EditText confirmarContrasenia;
    private EditText fechaNacimiento;
    private Button registrar ;
    private RadioGroup grupoGenero;
    private RadioButton hombre;
    private RadioButton mujer;
    private ImageView FotoPerfil;
    private FirebaseAuth mAuth;

    private FirebaseDatabase bd;
    private DatabaseReference bdreference;

    private long fechalong;
    private String fechaString;
    private String pickerPath;


    private ImagePicker imagePicker;
    private CameraImagePicker cameraImagePicker;
    private Uri fotoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = (EditText) findViewById(R.id.nombreRegistro);
        apellido= (EditText) findViewById(R.id.apellidoRegistro);
        correo= (EditText) findViewById(R.id.correoRegistro);
        contrasenia = (EditText) findViewById(R.id.contraseñaRegistro);
        confirmarContrasenia= (EditText) findViewById(R.id.confimarcontraseñaRegistro);
        registrar= (Button) findViewById(R.id.btn_registro_registrar);
        fechaNacimiento = (EditText) findViewById(R.id.txt_Registro_fecha);
        grupoGenero = (RadioGroup) findViewById(R.id.rg_registro_genero);
        FotoPerfil=(ImageView) findViewById(R.id.Registro_fotoPerfil);
        hombre= (RadioButton) findViewById(R.id.rb_Registro_hombre);
        mujer= (RadioButton) findViewById(R.id.rb_Registro_mujer);


        mAuth= FirebaseAuth.getInstance();
        bd = FirebaseDatabase.getInstance();

        imagePicker = new ImagePicker(this);
        cameraImagePicker = new CameraImagePicker(this);

        cameraImagePicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if (! list.isEmpty()){
String  path= list.get(0).getOriginalPath();
fotoUri= Uri.parse(path);
FotoPerfil.setImageURI(fotoUri);
                }

            }

            @Override
            public void onError(String s) {
                Toast.makeText(Registro.this,"Error: "+s , Toast.LENGTH_SHORT).show();
            }
        });

        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {

                if (! list.isEmpty()){
                    String path= list.get(0).getOriginalPath();
                    fotoUri= Uri.fromFile(new File(path));
                    FotoPerfil.setImageURI(fotoUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(Registro.this,"Error: "+s , Toast.LENGTH_SHORT).show();
            }
        });

        FotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//imagePicker.pickImage();
                AlertDialog.Builder alert= new AlertDialog.Builder(Registro.this);
                alert.setTitle("Foto De Perfil" );
                String [] items = {"Elegir Foto de la Galeria","Tomar foto"};

                alert.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                  switch (i){
                      case 0:
                          imagePicker.pickImage();
                          ;
                          break;
                      case 1:
                          pickerPath = cameraImagePicker.pickImage();
                          ;
                          break;
                  }
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });



        fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog calendario = new DatePickerDialog(Registro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
 Calendar resultado = Calendar.getInstance();
resultado.set(Calendar.YEAR,anio);
resultado.set(Calendar.MONTH,mes);
resultado.set(Calendar.DAY_OF_MONTH,dia);
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
Date fecha = resultado.getTime();
fechalong = fecha.getTime();
fechaString = formato.format(fecha);
fechaNacimiento.setText(fechaString);
                    }
                },calendar.get(Calendar.YEAR)-18,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                calendario.show();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Correo = correo.getText().toString();
                final String Nombre = nombre.getText().toString();
                final String Apellido = apellido.getText().toString();
                if(isValidEmail(Correo) && validarContra() && validarCampos(Nombre,Apellido)) {
                    String Contrasenia= contrasenia.getText().toString();
                    mAuth.createUserWithEmailAndPassword(Correo, Contrasenia)
                            .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        final String Genero;
                                        if (hombre.isChecked()){
                                            Genero="Hombre";
                                        }else{
                                            Genero="Mujer";
                                        }
                                        if (fotoUri != null) {

                                            UsuarioDAO.getInstancia().subirFotoUri(fotoUri, new UsuarioDAO.IDevolverURlfoto() {
                                                @Override
                                                public void devolverURLstring(String url) {
                                                    persona p = new persona();
                                                    p.setNombre(Nombre);
                                                    p.setApellido(Apellido);
                                                    p.setCorreo(Correo);
                                                    p.setFechaDeNacimiento(fechalong);
                                                    p.setGenero(Genero);
                                                    p.setUrlFotoPerfil(url);
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                    bdreference = bd.getReference("Persona/" + currentUser.getUid());
                                                    bdreference.setValue(p);
                                                    Toast.makeText(Registro.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }else{
                                            persona p = new persona();
                                            p.setNombre(Nombre);
                                            p.setApellido(Apellido);
                                            p.setCorreo(Correo);
                                            p.setFechaDeNacimiento(fechalong);
                                            p.setGenero(Genero);
                                            p.setUrlFotoPerfil(Constantes.URLDEFECTOUSUARIOS);
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            bdreference = bd.getReference("Persona/" + currentUser.getUid());
                                            bdreference.setValue(p);
                                            Toast.makeText(Registro.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    } else {
                                        Toast.makeText(Registro.this,"Error al Registrarse" , Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }

            }
        });


        Glide.with(this).load(Constantes.URLDEFECTOUSUARIOS).into(FotoPerfil);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== Picker.PICK_IMAGE_DEVICE && resultCode== RESULT_OK){
imagePicker.submit(data);
        }else if (requestCode== Picker.PICK_IMAGE_CAMERA && resultCode==RESULT_OK){
            cameraImagePicker.reinitialize(pickerPath);
            cameraImagePicker.submit(data);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarContra() {
        String contra,confContra;
        contra=contrasenia.getText().toString();
        confContra=confirmarContrasenia.getText().toString();

        if (contra.equals(confContra)){
            if (contra.length()<=16 && contra.length()>=6){
               return true;
            } else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean validarCampos (String nombre , String apellido){
        if (!nombre.isEmpty() && !apellido.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
