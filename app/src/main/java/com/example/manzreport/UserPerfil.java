package com.example.manzreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class UserPerfil extends AppCompatActivity {
    EditText Nombre,Email,Phone;
    TextView fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button resendCode;
    Button resetPassLocal,changeProfileImage,buttonsalir;
    FirebaseUser user;
    ImageView profileImage;
    ImageButton atras;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);
        //
        //
        atras = (ImageButton) findViewById(R.id.atras_perfil_de_usuario);

        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        buttonsalir = findViewById(R.id.button);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        dialog =new Dialog(this);

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                 Nombre = dialog.findViewById(R.id.Name);
                 Email = dialog.findViewById(R.id.Email);
                 Phone = dialog.findViewById(R.id.Phone);
                fStore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if(value.exists()){
                            Nombre.setText(value.getString("fName"));
                            Email.setText(value.getString("email"));
                            Phone.setText(value.getString("phone"));
                        }
                    }
                });




            }
        });

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));


                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


        buttonsalir.setOnClickListener(view ->{
            AlertDialog.Builder alert = new AlertDialog.Builder(UserPerfil.this);
            alert.setMessage("¿Deseas cerrar sesion?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fAuth.signOut();
                            startActivity(new Intent(UserPerfil.this, login.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog title = alert.create();
            title.setTitle("Cerrar Sesion");
            title.show();
        });
        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),login.class));
            finish();
        }//sacar usuario

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                return;
            }
        });





        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Resetiar Contraseña ?");
                passwordResetDialog.setMessage("Ingresa Nueva Contraseña > 6 Caracteres Minimo.");
                passwordResetDialog.setView(resetPassword);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserPerfil.this, "Reseteo de Contraseña Completo.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserPerfil.this, "Error al Cambiar la Contraseña.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });




        //

        //changeProfileImage.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                // open gallery
                //Intent i = new Intent(v.getContext(),EditProfile.class);
                //i.putExtra("fullName",fullName.getText().toString());
                //i.putExtra("email",email.getText().toString());
                //i.putExtra("phone",phone.getText().toString());
                //startActivity(i);
//

            //}
        //});
        //


    }

    public void openDialog() {

        dialog.setContentView(R.layout.cus);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageView imageViewclose = dialog.findViewById(R.id.imageclose2);
        Button btnsave = dialog.findViewById(R.id.btn_yes);
        Button btnno = dialog.findViewById(R.id.btn_no);
        //EditText Nombre = dialog.findViewById(R.id.Name);


        imageViewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(UserPerfil.this, "Dialog Closed", Toast.LENGTH_SHORT).show();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Nombre.getText().toString().isEmpty() || Email.getText().toString().isEmpty() || Phone.getText().toString().isEmpty()){
                    Toast.makeText(UserPerfil.this, "Uno o varios campos estan vacios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = Email.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("fName",Nombre.getText().toString());
                        edited.put("phone",Phone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserPerfil.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                                //onBackPressed();
                                dialog.dismiss();
                                return;
                            }
                        });
                        //Toast.makeText(EditProfile.this, "Perfil cambiado.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserPerfil.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Toast.makeText(UserPerfil.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(UserPerfil.this, Principal.class));
        }//sacar usuario
    }




    //public void logout(View view) {
    //FirebaseAuth.getInstance().signOut();//logout
    // startActivity(new Intent(getApplicationContext(),login.class));
    //  finish();
//}


}