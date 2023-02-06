package com.ejh.authentication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MemberProfile extends AppCompatActivity {
    TextView nomMembre, phoneMembre, emailMembre;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String membreID;
    ImageView pfp;
    StorageReference storageReference;
    Button imagebutton;
    public Uri imageuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        nomMembre=findViewById(R.id.nomMembre);
        phoneMembre=findViewById(R.id.phoneMembre);
        emailMembre=findViewById(R.id.emailMembre);
        pfp=findViewById(R.id.pfp);
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        membreID= fAuth.getCurrentUser().getUid();
        imagebutton=findViewById(R.id.imagebutton);

        StorageReference profilRef= storageReference.child("users/"+membreID+"profile.jpg");
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pfp);
            }
        });
// change pfp
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepicture();
            }
        });
        //end of change pfp
    }

    //result launcher
    ActivityResultLauncher <Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult (ActivityResult r){
                    if (r.getResultCode() == Activity.RESULT_OK){
                        Intent data = r.getData();
                        imageuri = data.getData();
                        pfp.setImageURI(imageuri);
                        uploadPicture(imageuri);
                    }
                }
            });

    private void uploadPicture(Uri imageuri) {

    }


    private void choosepicture()
    {
        Intent intent =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    public void logout (View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}