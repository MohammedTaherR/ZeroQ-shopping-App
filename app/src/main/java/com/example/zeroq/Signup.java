package com.example.zeroq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class Signup extends AppCompatActivity {
    Button signup;
    ImageView back;
    private FirebaseAuth auth;
    FirebaseDatabase rootnode;
    DatabaseReference databaseReference,databaseReference1;
    EditText nameEdit,passEdit,emailEdit;
    TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup= findViewById(R.id.signup1);
        nameEdit=findViewById(R.id.nameEdit);
        passEdit=findViewById(R.id.passEdit);
        emailEdit=findViewById(R.id.emailEdit);
        back= findViewById(R.id.back);

        login= findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,loginActivity.class);
                startActivity(intent);

            }
        });

      auth= FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name= nameEdit.getText().toString();
                String email=emailEdit.getText().toString();
                String pass=passEdit.getText().toString();
                if(TextUtils.isEmpty(name) ||TextUtils.isEmpty(email)  || TextUtils.isEmpty(pass)){
                    Toast.makeText(Signup.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    emailEdit.setError("Enter Valid Email");
                    Toast.makeText(Signup.this, "Enter valid email", Toast.LENGTH_SHORT).show(); }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                rootnode = FirebaseDatabase.getInstance();
                                databaseReference = rootnode.getReference("Users");
                                databaseReference1 = rootnode.getReference("Users").child(auth.getUid()).child("purchase");
                                DataStorage dataStorage = new DataStorage(name, email, pass, "purchases");
                                databaseReference.child(auth.getUid()).setValue(dataStorage);


                                Intent intent = new Intent(Signup.this, scan_screen.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Signup.this, "Error in creating account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Signup.this,siginorlogin.class);
                startActivity(intent);
            }
        });

    }
}