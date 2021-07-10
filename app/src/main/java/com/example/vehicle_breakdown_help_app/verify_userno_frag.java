package com.example.vehicle_breakdown_help_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.example.vehicle_breakdown_help_app.Helper_class.User_class;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link verify_userno_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class verify_userno_frag extends Fragment {
    private OtpEditText otp_code;
    private MaterialTextView display_no;
    private AppCompatButton verify_btn;
    private Button resend_btn;
    private CircularProgressIndicator progress;

    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private String verificationId;
    private  String userID;
    private String username;
    private String email;
    private  String password;
    private Uri profile_img;

    private String number;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
/*    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;*/

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public verify_userno_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment verify_userno_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static verify_userno_frag newInstance(String param1, String param2) {
        verify_userno_frag fragment = new verify_userno_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.verify_userno_frag, container, false);
        display_no=(MaterialTextView) root.findViewById(R.id.display_no);
        otp_code=(OtpEditText) root.findViewById(R.id.otp_code);
        verify_btn = (AppCompatButton) root.findViewById(R.id.verify_btn);
        resend_btn = (Button) root.findViewById(R.id.resend_btn);
        progress = (CircularProgressIndicator) root.findViewById(R.id.progress);



        Bundle bundle = getArguments();

        if (bundle != null)
        {
            User_class user_bundle =bundle.getParcelable("key");

            number = "+92" + user_bundle.getUser_phone();
            username = user_bundle.getUser_name();
            email = user_bundle.getUser_email();
            password = user_bundle.getUser_password();
            profile_img = user_bundle.getUser_profile_image();

            display_no.setText("Number:"+number);
            /*display_no.append(user.getUser_email()+"\n");
            display_no.append(user.getUser_phone()+"\n");
            display_no.append(user.getUser_profile_image()+"\n");
            display_no.append(user.getUser_password());*/
        }
            sendVerificationCode(number);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp_code.getText().toString();
                if (code.isEmpty() || code.length() < 6) {

                    otp_code.triggerErrorAnimation();
                }
                else
                {
                    verifyCode(code);
                }
            }
        });

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp_code.getText().toString();
                if (code.isEmpty() || code.length() < 6) {

                    otp_code.triggerErrorAnimation();
                }
                else
                {
                    resendVerificationCode(code,mResendToken);
                }
            }
        });

        otp_code.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                Toast.makeText(getContext(), "Your Code:" + value, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    private void verifyCode(String code) {
        if (verificationId==null)
        {
            Toast.makeText(getActivity(), "Verification Code Not Sent!", Toast.LENGTH_LONG).show();
        }
        else if(code.length()!=6)
        {
            Toast.makeText(getActivity(),  "Please Check Your Verification Code!", Toast.LENGTH_LONG).show();
        }
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                            Toast.makeText(getActivity(), "signin succfully!", Toast.LENGTH_LONG).show();

                            progress.setVisibility(View.VISIBLE);
                            verify_btn.setVisibility(View.GONE);

                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Username",username);
                            user.put("Email",email);
                            user.put("Phone",number);
                            user.put("Password",password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "user Profile is created for", Toast.LENGTH_LONG).show();

                                    Log.d("TAG", "onSuccess: user Profile is created for "+ userID);

                                    if(profile_img!=null)
                                    {
                                        uploadImageToFirebase(profile_img);
                                    }
                                    else {
                                        progress.setVisibility(View.GONE);
                                        verify_btn.setVisibility(View.VISIBLE);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.toString());
                                }
                            });



                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Toast.makeText(getActivity(), "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_LONG).show();
            resend_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                //editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("Users_Profile_Img/"+mAuth.getCurrentUser().getUid()+"/profile_img.jpg");
        progress.setVisibility(View.VISIBLE);
        verify_btn.setVisibility(View.GONE);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded.", Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
                verify_btn.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed!!!"+e.getMessage(), Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
                verify_btn.setVisibility(View.VISIBLE);
            }
        });

    }

}