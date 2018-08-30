package app.fire.em.fire;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText phone_edittext;
    EditText code_edittext;

    FirebaseAuth auth;
    FirebaseUser user;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        phone_edittext = findViewById(R.id.phoneEditText);
        code_edittext = findViewById(R.id.codeEditText);

        findViewById(R.id.phoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });


        findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySignInCode();
            }
        });

    }


    private void verifySignInCode(){

        String code = code_edittext.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Sucessfull " + user.getPhoneNumber(), Toast.LENGTH_LONG).show();
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Incorrect Verification code", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }


    private void sendVerificationCode(){

        String phone = phone_edittext.getText().toString();

        if(phone.isEmpty()){
            phone_edittext.setError("Phone number is required");
            phone_edittext.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            phone_edittext.setError("please enter a valid phone");
            phone_edittext.requestFocus();
            return;
        }



        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
               mCallback);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;

        }
    };

}
