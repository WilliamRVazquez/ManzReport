package com.example.manzreport;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class crear_reportes extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton atras;
    EditText ubicacion, DescReport;
    ImageView imageView;
    StorageReference storageReference;
    String storage_path = "report/*";
    String download_uri;

    LinearLayout linearbtn;
    Button btndelete;
    //Button btnedit;
    String show = "";
    Spinner report;

    String exitLoad = "";

    String [] opciones = {"-","Capdam","Proteccion civil","Jardineria","Mantenimiento publico"};

    String item;
    ProgressDialog progressDialog;

    Button btn_image, btn_enviar;


    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reportes);
        atras = (ImageButton) findViewById(R.id.atras_de_crear_reporte);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);
        btn_image = (Button) findViewById(R.id.btn_image);
        linearbtn = (LinearLayout) findViewById(R.id.layoutbtn);
        //btnedit = (Button) findViewById(R.id.btnEdit);
        btndelete = (Button) findViewById(R.id.btnRemove);
        progressDialog = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.imageView);


        ubicacion = (EditText) findViewById(R.id.ubicacion_manual);
        DescReport = (EditText) findViewById(R.id.DescReport);

        report = (Spinner)findViewById(R.id.spinreport);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(crear_reportes.this,
                R.layout.listviewresours, opciones);
        report.setAdapter(aa);
        report.setOnItemSelectedListener(this);



        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mfirestore = FirebaseFirestore.getInstance();

        atras.setOnClickListener(view ->{
            onBackPressed();
        });


        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                uploadPhoto();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirestore.collection("Reportes").document(idd).delete();
                Toast.makeText(crear_reportes.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
                linearbtn.setVisibility(GONE);
                btn_image.setVisibility(View.VISIBLE);
                imageView.setVisibility(GONE);


            }
        });
        //btnedit.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //uploadPhoto();
              //  editSi = "SI";

            //}
        //});


        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ubireport = ubicacion.getText().toString().trim();
                String desreporte = DescReport.getText().toString().trim();

                if(item.equals("-")){
                    Toast.makeText(getApplicationContext(), "Escoga el tipo de incidencia", Toast.LENGTH_SHORT).show();
                }else if (ubireport.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingresar ubicacion del reporte", Toast.LENGTH_SHORT).show();
                }
                else if (desreporte.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingresar descripccion del reporte", Toast.LENGTH_SHORT).show();
                }
                else{
                    postReport(ubireport, desreporte, item);
                }
            }
        });

    }

    private void uploadPhoto() {

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");


        startActivityForResult(i, COD_SEL_IMAGE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                String ubireport = ubicacion.getText().toString().trim();
                String desreporte = DescReport.getText().toString().trim();
                subirPhoto(image_url ,ubireport, desreporte);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url, String ubireport, String desreporte) {
        progressDialog.setMessage("Añadiendo la imagen");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("Reportes").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("photo", "");
        map.put("id", id.getId());
        map.put("ubicacion", "");
        map.put("Descripcion", "");
        map.put("Tipo reporte", "");
        exitLoad = "SI";
        mfirestore.collection("Reportes").document(id.getId()).set(map);
        idd = id.getId();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);



        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            download_uri = uri.toString();
                            Map<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("Reportes").document(idd).update(map);
                            imageView.setVisibility(View.VISIBLE);
                            btn_image.setVisibility(GONE);

                            btn_enviar.setVisibility(View.VISIBLE);
                            getimage(idd);
                            show = "ok";

                            progressDialog.dismiss();
                            Toast.makeText(crear_reportes.this, "Se agrego la imagen", Toast.LENGTH_SHORT).show();


                            //if (editSi.equals("SI")){
                               // Map<String, Object> map = new HashMap<>();
                               // map.put("photo", download_uri);
                                //mfirestore.collection("Reportes").document(idd).update(map);
                                //Toast.makeText(crear_reportes.this, "Se edito la imagen", Toast.LENGTH_SHORT).show();
                                //linearbtn.setVisibility(View.VISIBLE);
                            //btn_image.setVisibility(View.GONE);
                            //}else{
                            // Map<String, Object> map = new HashMap<>();
                              //  map.put("photo", download_uri);
                                //mfirestore.collection("Reportes").document(idd).update(map);
                               // Toast.makeText(crear_reportes.this, "Se agrego la imagen", Toast.LENGTH_SHORT).show();
                               // btn_image.setVisibility(View.GONE);
                                //linearbtn.setVisibility(View.VISIBLE);
                            //}
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(crear_reportes.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getimage(String idd) {
        mfirestore.collection("Reportes").document(idd).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String image = documentSnapshot.getString("photo");
                try {
                    if(!image.equals("")){

                        Picasso.with(crear_reportes.this).load(image).resize(350, 350).into(imageView);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void postReport(String ubireport, String desreporte, String item) {

        Map<String, Object> map = new HashMap<>();
        map.put("ubicacion", ubireport);
        map.put("Descripcion", desreporte);
        map.put("Tipo reporte", item);

        mfirestore.collection("Reportes").document(idd).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Reporte creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Agrege una imagen  o espere a que cargue", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        linearbtn.setVisibility(GONE);
        btn_enviar.setVisibility(GONE);
        if (show.equals("ok")){
            linearbtn.setVisibility(VISIBLE);
            btn_enviar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        item = report.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        if (exitLoad.equals("SI")){
            AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
            myBulid.setMessage("Seguro que deseas salir? perderas todos los datos");
            myBulid.setTitle("Alerta");
            myBulid.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mfirestore.collection("Reportes").document(idd).delete();
                    finish();
                }
            });
            myBulid.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = myBulid.create();
            dialog.show();

        }else{
            super.onBackPressed();
        }

    }

}