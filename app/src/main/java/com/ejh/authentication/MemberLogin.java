package com.ejh.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Member;

public class MemberLogin extends AppCompatActivity {
    TextView txtNouveau, txtNp;
    EditText LoginEmail, LoginPass;
    Button button;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);
        txtNouveau= findViewById(R.id.txtNouveau);
        LoginEmail= findViewById(R.id.LoginEmail);
        LoginPass= findViewById(R.id.LoginPass);
        button= findViewById(R.id.button);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        txtNp= findViewById(R.id.txtNp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = LoginEmail.getText().toString();
                String pass = LoginPass.getText().toString();
                if (TextUtils.isEmpty(email)){
                    LoginEmail.setError("L'adresse email est réquise");
                    return;
                } if (TextUtils.isEmpty(pass)){
                    LoginPass.setError("Votre mot de passe est réquise");
                    return;
                }
                if (pass.length()<6){
                    LoginPass.setError("Le mot de passe doit avoir au minimum 6 caractères");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){
                            Toast.makeText(MemberLogin.this,"Membre connecté", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MemberLogin.this,"Une erreur s'est produite "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
            }
        });

        txtNouveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MemberRegister.class));
            }
        });


        txtNp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail=new EditText(v.getContext());
                AlertDialog.Builder pResetDialog = new AlertDialog.Builder(v.getContext());
                pResetDialog.setTitle("Mot de passe oublié?");
                pResetDialog.setMessage("Écris ton courriel pour recevoir un lien");
                pResetDialog.setView(resetMail);

                pResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        //recuperer couriel et envoyer lien
                        String c = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MemberLogin.this, "Succes", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MemberLogin.this, "Le lien n'a pas été envoyé "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) ;
                    }
                });

                pResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialog, int i ){
                        //ne rien faire
                    }
                });
                pResetDialog.show();
            }
        });
// fin du rappel nP

    }
}