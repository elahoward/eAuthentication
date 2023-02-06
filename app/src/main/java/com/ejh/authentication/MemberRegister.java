package com.ejh.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MemberRegister extends AppCompatActivity {
    EditText regEmail;
    EditText regName;
    EditText regPass;
    EditText regNum;
    TextView txtLogin;
    Button buttonreg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        regName=findViewById(R.id.regName);
        regNum=findViewById(R.id.regNum);
        regPass=findViewById(R.id.regPass);
        regEmail=findViewById(R.id.regEmail);
        buttonreg=findViewById(R.id.buttonreg);
        txtLogin=findViewById(R.id.txtLogin);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = regName.getText().toString();
               String email = regEmail.getText().toString();
                String num = regNum.getText().toString();
                String pass= regPass.getText().toString();
                if (TextUtils.isEmpty(email)){
                    regEmail.setError("L'adresse email est réquise");
                    return;
                } if (TextUtils.isEmpty(name)){
                    regName.setError("Votre nom est réquise");
                    return;
                }
                if (pass.length()<6){
                regPass.setError("Le mot de passe doit avoir au minimum 6 caractères");
                return;
             }
                if (TextUtils.isEmpty(num)){
                    regNum.setError("Un numéro de téléphone est réquis");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MemberRegister.this, "Compte crée",Toast.LENGTH_SHORT).show();
                            memberID=fAuth.getCurrentUser().getUid();
                            DocumentReference dr = fStore.collection("members").document(memberID);
                            Map<String, Object> membre = new HashMap<>();
                            membre.put("nom", name);
                            membre.put("email", email);
                            membre.put("phone", num);
                            dr.set(membre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAGS", "Profil crée pour " + memberID);
                                }
                            });
                            startActivity (new Intent(getApplicationContext(),MemberProfile.class));

                        }
                        else{
                            Toast.makeText(MemberRegister.this,"Une erreur s'est produite " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
                //fin sauvegarde
            }
        });
        txtLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                startActivity(new Intent(getApplicationContext(), MemberLogin.class));
            }
        });
    }
}