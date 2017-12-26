package dexterdatul.com.firebasephonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextCode;

    private LinearLayout ll;
    private LinearLayout iii;

    private ProgressBar phoneBar;
    private ProgressBar codeBar;

    private Button btnSendConfirmation;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    private FirebaseAuth mAuth;

    private TextView errorText;

    private String verificationId;

    private int btnType = 0;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhone = findViewById(R.id.editTextPhoneNumber);
        editTextCode = findViewById(R.id.editTextCode);

        ll = findViewById(R.id.ll);
        iii = findViewById(R.id.ii2);

        phoneBar = findViewById(R.id.phoneBar);
        codeBar = findViewById(R.id.codeBar);

        btnSendConfirmation = findViewById(R.id.buttonSnedVerification);

        errorText = findViewById(R.id.errorText);
        errorText.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        btnSendConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnType == 0){
                    phoneBar.setVisibility(View.VISIBLE);
                    editTextPhone.setEnabled(false);
                    btnSendConfirmation.setEnabled(false);

                    String phoneNumber = editTextPhone.getText().toString();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber, 60, TimeUnit.SECONDS, MainActivity.this, mCallback
                    );
                }else{
                    btnSendConfirmation.setEnabled(false);
                    codeBar.setVisibility(View.VISIBLE);

                    String code = editTextCode.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                errorText.setText("There was some error in verification");
                errorText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken token){
            verificationId = verificationID;
            resendingToken = token;

            phoneBar.setVisibility(View.INVISIBLE);
            iii.setVisibility(View.VISIBLE);

            btnType = 1;

            btnSendConfirmation.setText("Verifiy Code");
            btnSendConfirmation.setEnabled(true);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Intent intet = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intet);
                    finish();
                }else{
                    errorText.setText("There was some error in logging in");
                    errorText.setVisibility(View.VISIBLE);
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                    }
                }
            }
        });

    }

}
