package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity {

    EditText  signUp_Name, signUp_Mail, signUp_UserName, signUp_Password ;
    AppCompatButton  btn_SignUp ;
    TextView loginRedirect ;

    FirebaseDatabase database ;
    DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        signUp_Name = findViewById(R.id.signUp_Name) ;
        signUp_Mail = findViewById(R.id.signUp_Mail) ;
        signUp_UserName = findViewById(R.id.signUp_UserName) ;
        signUp_Password = findViewById(R.id.signUp_Password) ;
        btn_SignUp = findViewById(R.id.btn_SignUp) ;
        loginRedirect = findViewById(R.id.loginRedirect) ;





        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance() ;
                reference = database.getReference("users") ;

                String name  = signUp_Name.getText().toString() ;
                String email  = signUp_Mail.getText().toString() ;
                String username  = signUp_UserName.getText().toString() ;
                String password  = signUp_Password.getText().toString() ;


                 HelpingClass helpingClass = new HelpingClass(name, email, username, password) ;
                 reference.child(name).setValue(helpingClass) ;


                Toast.makeText(SignUp_Activity.this, "you have Sign up Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp_Activity.this, Login_Activity.class) ;
                startActivity(intent);
                finish();

            }
        });



        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp_Activity.this, Login_Activity.class) ;
                startActivity(intent);
                finish();

            }
        });







    }


}