package com.example.manzreport;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Sing_up extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    Dialog dialog;

    // Contraseña de 8-20 caracteres que requiere números y letras de ambos casos
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z]).{8,20}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mFullName   = findViewById(R.id.Register_Nombre_Completo);
        mEmail      = findViewById(R.id.Register_Correo);
        mPassword   = findViewById(R.id.Register_Contraseña);
        mPhone      = findViewById(R.id.Register_telefono);
        mRegisterBtn= findViewById(R.id.button_Register);
        mLoginBtn   = findViewById(R.id.txtv_inisesion_btn);
        dialog = new Dialog(this);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                return;
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialoTerms();
            }
        });

    }

    private void openDialoTerms() {
        dialog.setContentView(R.layout.terminos);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageView imageViewclose = dialog.findViewById(R.id.imageclose2);
        Button btnok = dialog.findViewById(R.id.btn_acep);
        Button btnno = dialog.findViewById(R.id.btn_no);

        imageViewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Sing_up.this, "Dialog Closed", Toast.LENGTH_SHORT).show();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();
                int roln = 1;
                String rol = String.valueOf(roln);



                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Requiere Nombre");
                    mFullName.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Requiere Contraseña");
                    mPassword.requestFocus();
                    return;
                }else if(password.length() < 8){
                    mPassword.setError("La contraseña debe ser mayor a 8 caracteres y contener una Mayuscula y una Minuscula");
                    mPassword.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Requiere Telefono");
                    mPhone.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(email)){
                    mEmail.setError("Requiere nombre");
                    mEmail.requestFocus();
                    return;
                }else if(PASSWORD_PATTERN.matcher(password).matches()){
                    progressBar.setVisibility(View.VISIBLE);

                    // register the user in firebase
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                // send verification link

                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(Sing_up.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                Toast.makeText(Sing_up.this, "Registrado.", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                String id = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("Id",id);
                                user.put("fName",fullName);
                                user.put("email",email);
                                user.put("phone",phone);
                                user.put("Rol",rol);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(Sing_up.this, "Error ! El Correo ya esta registrado" , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    mPassword.setError("La contraseña no cumple con los requerimientos.");
                    mPassword.requestFocus();
                }

            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
                Toast.makeText(Sing_up.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(Sing_up.this, MainActivity.class));
        }//sacar usuario
    }

}