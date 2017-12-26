package dexterdatul.com.firebasephonenumberauthentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();

        logOut = findViewById(R.id.button2);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToAuth();
            }
        });
    }


    private void sendToAuth() {
        Intent authIntent = new Intent(Main2Activity.this, Main2Activity.class);
        startActivity(authIntent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent authIntent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(authIntent);
            finish();
        }

    }
}
