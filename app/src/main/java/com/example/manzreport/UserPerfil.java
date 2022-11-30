package com.example.manzreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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
    String correo_e;
    String correo_c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);
        //
        //
        atras = (ImageButton) findViewById(R.id.atras_perfil_de_usuario);
        SharedPreferences prefs = getSharedPreferences("Preferences", 0);
        correo_e = prefs.getString("users", "");
        SharedPreferences prefs2 = getSharedPreferences("Preferences2", 0);
        correo_c = prefs2.getString("users2", "");


        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        buttonsalir = findViewById(R.id.button);

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

        buttonsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogcerrarsesion();
            }
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
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();


                        AuthCredential credential = EmailAuthProvider
                                .getCredential(correo_e,correo_c);


                        user2.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                        String newPassword = resetPassword.getText().toString();
                                        user1.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(UserPerfil.this, "Se reseteo", Toast.LENGTH_SHORT).show();
                                                            //SharedPreferences prefs2 = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                                            //SharedPreferences.Editor editor = prefs2.edit();
                                                            //editor.putString("users", newPassword);
                                                            //editor.commit();
                                                            Map<String, Object> map = new HashMap<>();
                                                            try {
                                                                map.put("password",Security.encrypt(newPassword));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();

                                                            }
                                                            fStore.collection("users").document(userId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(UserPerfil.this, "se modificoaasasasasasa", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(UserPerfil.this, "lloremos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                            SharedPreferences prefs2 = getSharedPreferences("Preferences2", Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor editor2 = prefs2.edit();
                                                            editor2.putString("users2", newPassword);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserPerfil.this, "no se pudo", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

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
        //}
        //});
        //
    }



    private void openDialogcerrarsesion() {
        dialog.setContentView(R.layout.cerrarseion);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        Button sicerrar = dialog.findViewById(R.id.btn_yescerrar);
        Button nocerrar = dialog.findViewById(R.id.btn_nocerrar);

        sicerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(UserPerfil.this, login.class));
            }
        });

        nocerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


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
                Toast.makeText(UserPerfil.this, "Cerrado", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserPerfil.this, "Cancelado", Toast.LENGTH_SHORT).show();
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