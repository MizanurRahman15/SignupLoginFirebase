package com.example.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class Login_Activity extends AppCompatActivity {

    EditText login_username, login_password ;
    AppCompatButton  btn_login;
    TextView loginRedirect ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        login_username = findViewById(R.id.login_username) ;
        login_password = findViewById(R.id.login_password) ;
        btn_login = findViewById(R.id.btn_login) ;
        loginRedirect = findViewById(R.id.loginRedirect) ;





       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {



               if ( validateUsername() | validatePassword() ){

                   Checkuser() ;

               }else {

                   Toast.makeText(Login_Activity.this,"Enter valid info",Toast.LENGTH_SHORT).show();


               }


           }
       });



        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login_Activity.this, SignUp_Activity.class) ;

                startActivity(intent);
            }
        });




    }


    public  Boolean validateUsername(){

         String val = login_username.getText().toString() ;

         if (val.isEmpty()){
             login_username.setError("Username cannot be Empty");
             return false ;

         }else {

             login_username.setError(null);
             return true ;
         }

    }



    public  Boolean validatePassword(){

        String val = login_password.getText().toString() ;

        if (val.isEmpty()){
            login_password.setError("Password cannot be Empty");
            return false ;

        }else {

            login_password.setError(null);
            return true ;
        }

    }


//calling method
    public void Checkuser(){

        String userUsername =  login_username.getText().toString().trim();
        String userPassword =  login_password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuserDatabase = reference.orderByChild("username").equalTo(userUsername);


        checkuserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ১. ব্যবহারকারী (user) ডেটাবেসে আছে কিনা তা যাচাই করা হচ্ছে।
                if (snapshot.exists()){
                    login_username.setError(null);

                    // ২. সঠিক ডেটা পেতে snapshot-এর মধ্যে লুপ করা হচ্ছে।
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        // ৩. ডেটাবেসের পাসওয়ার্ড এবং ব্যবহারকারীর দেওয়া পাসওয়ার্ড মিলছে কিনা তা যাচাই করা হচ্ছে।
                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)){


                            SessionManager sessionManager = new SessionManager(Login_Activity.this);
                            sessionManager.setLogin(true);

                            // পাসওয়ার্ড মিলে গেলে, MainActivity-তে যাওয়া হচ্ছে।
                            login_password.setError(null);
                            Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                            // লগইন সফল হলে একটি মেসেজ দেখানো যেতে পারে।
                            Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            return; // সফল হলে লুপ থেকে বেরিয়ে আসা হচ্ছে।
                        }
                    }

                    // যদি লুপ শেষ হওয়ার পরেও পাসওয়ার্ড না মেলে, তাহলে এইখানে কোড আসবে।
                    // অর্থাৎ, পাসওয়ার্ড ভুল।
                    login_password.setError("Wrong Password");
                    login_password.requestFocus();

                }else {
                    // যদি ব্যবহারকারীর নাম ডেটাবেসে না থাকে।
                    login_username.setError("User Does not Exist");
                    login_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // কোনো ত্রুটি হলে এখানে তা সামলানো হবে।
                Toast.makeText(Login_Activity.this, "ডেটাবেস ত্রুটি: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }











    //OnCreate methods ends here //

}
