package com.example.vehicle_breakdown_help_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vehicle_breakdown_help_app.Helper_class.User_class;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_reg_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_reg_frag extends Fragment implements Validator.ValidationListener {
    private CircleImageView profile_image;
    @NotEmpty
    @Length(min = 3, max = 10)
    private TextInputEditText name_etext;
    @NotEmpty
    @Length(min = 10, max = 10)
    private TextInputEditText phone_etext;
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Please Use ALPHA NUMERIC MIXED CASE SYMBOLS!")
    @Length(min = 6,max = 15)
    private TextInputEditText password_etext;
    @NotEmpty
    @Email
    private TextInputEditText email_etext;
    Uri uri;
    private AppCompatButton reg_btn;
    private Validator validator;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public user_reg_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user_reg_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static user_reg_frag newInstance(String param1, String param2) {
        user_reg_frag fragment = new user_reg_frag();
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
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    uri = result.getData().getData();
                    // For example
                    profile_image.setImageURI(uri);

                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root =  inflater.inflate(R.layout.user_reg_frag, container, false);

        profile_image=(CircleImageView)root.findViewById(R.id.profile_image);
        name_etext = (TextInputEditText) root.findViewById(R.id.name_etext);
        phone_etext =(TextInputEditText) root.findViewById(R.id.phone_etext);
        password_etext =(TextInputEditText) root.findViewById(R.id.password_etext);
        email_etext =(TextInputEditText) root.findViewById(R.id.email_etext);
        reg_btn=(AppCompatButton)root.findViewById(R.id.reg_btn);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(requireActivity())
                        .crop()
                        .cropOval()
                        .maxResultSize(1080, 1080, true)
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                galleryLauncher.launch(it);
                            }


                        }));
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });


        return root;
    }

    @Override
    public void onValidationSucceeded() {
        String name = name_etext.getText().toString();
        String email = email_etext.getText().toString();
        String phone = phone_etext.getText().toString();
        String password = password_etext.getText().toString();

        verify_userno_frag fragment = new verify_userno_frag();

        User_class user = new User_class(uri,name,email,phone,password);
        Bundle bundle=new Bundle();
        bundle.putParcelable("key",user);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.frag,fragment).commit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);

            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}