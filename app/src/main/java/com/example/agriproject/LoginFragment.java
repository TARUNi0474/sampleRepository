package com.example.agriproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    ImageView psign,gsign;
    EditText number,code;
    Button otp,verify,resend,login;
    EditText uname,password;
    FirebaseAuth auth;
    TextView forgot;
    GoogleSignInClient client;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        psign = v.findViewById(R.id.psign);
        gsign=v.findViewById(R.id.gsign);
        login=v.findViewById(R.id.login);
        uname=v.findViewById(R.id.uname);
        password=v.findViewById(R.id.password);
        forgot=v.findViewById(R.id.forgot);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        client= GoogleSignIn.getClient(getActivity(),gso);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 auth=FirebaseAuth.getInstance();
                 String uuname=uname.getText().toString();
                 String upassword=password.getText().toString();
                 auth.signInWithEmailAndPassword(uuname,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(getActivity(), "Successfully signed in", Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(getActivity(), "Failed to login", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
            }
        });//end of login onClick listener


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth=FirebaseAuth.getInstance();
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                View v=LayoutInflater.from(getActivity()).inflate(R.layout.reset,null,false);
                final EditText email=v.findViewById(R.id.email);
                builder.setView(v);
                builder.setCancelable(false);
                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uemail=email.getText().toString();
                        if (uemail.isEmpty()){
                            Toast.makeText(getActivity(), "cant be empty", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            auth.sendPasswordResetEmail(uemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "reset mail sent", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "failed to reset", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });//end of forgot onClick listener




        psign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.phone,null,false);
                BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(v);
                dialog.setCancelable(true);
                dialog.show();
                 auth=FirebaseAuth.getInstance();
                number=v.findViewById(R.id.number);
                code=v.findViewById(R.id.code);
                otp=v.findViewById(R.id.otp);
                verify=v.findViewById(R.id.verify);
                resend=v.findViewById(R.id.resend);
                callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        id=s;
                        token=forceResendingToken;
                        Toast.makeText(getActivity(), "onCodeSent method", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signPhoneAuth(phoneAuthCredential);
                        Toast.makeText(getActivity(), "onVerificationCompleted method", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                       Toast.makeText(getActivity(), "Failed "+e, Toast.LENGTH_SHORT).show();
                    }
                };


                //by clicking send button
                otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String num=number.getText().toString();
                        if (num.isEmpty()){
                            Toast.makeText(getActivity(), "number can not be empty", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+num,
                                    60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,callbacks);
                            Toast.makeText(getActivity(), "Otp sent in send", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



                //by clicking verify button
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String otp=code.getText().toString();
                        if(otp.isEmpty()){
                            Toast.makeText(getActivity(), "otp can not be empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(id,otp);
                            Toast.makeText(getActivity(), "in verify method", Toast.LENGTH_SHORT).show();
                            signPhoneAuth(credential);
                        }
                    }
                });



                //by clicking resend button
                resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String num=number.getText().toString();
                        if (num.isEmpty()){
                            Toast.makeText(getActivity(), "number can not be empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+num,60,TimeUnit.SECONDS,TaskExecutors.MAIN_THREAD,callbacks,token);
                            Toast.makeText(getActivity(), "Otp sent in resend", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }//end of onClick method
        });//end of  phone setOnClickListener


        gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=client.getSignInIntent();
                startActivityForResult(i,0);
            }
        });
        return v;
    }//end of onCreateView method



    private void signPhoneAuth(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Phone Authentication Successful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Phone Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseGsign(account.getIdToken(),account.getEmail());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void firebaseGsign(String idToken, String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "successfully signed in", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}