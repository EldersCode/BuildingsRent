package com.project.buildingsrent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Hesham on 7/19/2017.
 */

public class HandlingLoginAuth extends Activity {

                 //Declaration of what we need in login layout

    private View loginLayout , registerLayout;
    private ProgressDialog progressD ;
    private String email = null;
    private String pass = null;
    private TextView forgetPass;
    private CheckBox showPass;
    private Button login_btn , register_btn , registerLoginBtn;
    private EditText emailLogin , passwordLogin ,userName , password , ConfPass;
    private CallbackManager callbackManager ;
    private LoginButton loginButton;

                         //////////////////////

              // Check if user is signed in (non-null) and update UI accordingly.

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Profile profile = Profile.getCurrentProfile();


    ////////////////////

    public void HandlingLoginAuth (final Context context){

        ViewsInitialization(context);
        HandlingFunctionalities(context);

                            //Handling Email and Password side
         if(currentUser != null){

            mAuth= FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            if(currentUser.isEmailVerified()){
                startActivity(new Intent(context , MapsActivity.class));
                finish();
            }else if (!currentUser.isEmailVerified()){

            }
        }
                                    //////////////

                           //Handling Facebook side here
        else if(profile != null){
            startActivity(new Intent(context , MapsActivity.class));
            finish();
        }
                                  /////////////



    }



    public void ViewsInitialization(Context context){

                    //Facebook login requirements here ..

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();

                        ////////////////////////

        loginLayout = (LinearLayout)findViewById(R.id.login_id);
        registerLayout = (LinearLayout) findViewById(R.id.register_id);
        login_btn =(Button)findViewById(R.id.button);
        register_btn = (Button) findViewById(R.id.Register);
        registerLoginBtn = (Button)findViewById(R.id.RegisterR);
        forgetPass = (TextView)findViewById(R.id.forgetPass_textView);
        showPass = (CheckBox)findViewById(R.id.showPassCheck);

                    // The Login Screen Edit texts initializing here ..
        emailLogin = (EditText) findViewById(R.id.Username);
        passwordLogin = (EditText) findViewById(R.id.Password);
                            ////////////////////////

                    // The Register Screen Edit texts initializing here ..
        userName=(EditText) findViewById(R.id.UsernameR);
        password=(EditText)findViewById(R.id.PasswordR);
        ConfPass = (EditText)findViewById(R.id.confirmPassR);
                            ///////////////////////

    }

    public void HandlingFunctionalities(final Context context){


                            // Login with Facebook handling ..
        // Callback registration
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                progressD = new ProgressDialog(context);
                progressD.setMessage("Logging in ...");
                progressD.show();
                startActivity(new Intent(context , MapsActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

            }
        });
                                ///////////////////



        // Handling show password check box here

        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(showPass.isChecked()){
                    passwordLogin.setTransformationMethod(null);
                }
                else if(!showPass.isChecked()){
                    passwordLogin.setTransformationMethod(new PasswordTransformationMethod());
                }

            }
        });

                                     ////////////////////


                             //Handling Login Button first ..


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth= FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();

                email = null;
                pass = null;
                if (emailLogin.getText().length() > 0 && passwordLogin.getText().length() > 0) {
                    email = emailLogin.getText().toString();
                    pass = passwordLogin.getText().toString();
                    Login(context, email, pass);
                } else {
                    Toast.makeText(context, "Please enter your full data !", Toast.LENGTH_SHORT).show();
                }
            }
        });

                             /////////////////////////


                            //Handling Register Button of login layout here ..

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);


            }
        });

                            //////////////////////////


                        // Handling Register Button After you Sign up here ..

        registerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = null;
                pass = null;
                if(userName.getText().length()>0 && password.getText().length()>0 && ConfPass.getText().length()>0 ) {
                    email = userName.getText().toString();
                    pass = password.getText().toString();
                    String confirm = ConfPass.getText().toString();
                    if (pass.equals(confirm)) {
                        signUpEmail(context, email, pass);
                        registerLayout.setVisibility(View.GONE);
                        loginLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "password and confirmation not matched !", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Please enter your full data !", Toast.LENGTH_SHORT).show();
                }
            }
        });


                            ////////////////////////////////


                        // Handling Forget Password Text Here ..

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailLogin.getText().length() > 0) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context)
                            .setTitle("Notification")
                            .setMessage("Reset password to this email ?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mail = emailLogin.getText().toString();
                            ForgetPass(context , mail);
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.setCancelable(true);
                        }
                    });
                    alert.create().show();
                }
                else{
                    Toast.makeText(context, "Please enter your email first !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


                        // here I put all the firebase authentication needed codes ..

    private void ForgetPass(final Context context , String Email) {
        mAuth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(context)
                                    .setTitle("Notification")
                                    .setMessage( "you have received an email to reset your password .. check it out !")
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.alarm);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.setCancelable(true);
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                    context.startActivity(intent);
                                    alert.setCancelable(true);
                                }
                            });
                            alert.create().show();
                        }
                    }
                });
    }



    public void Login(final Context context , String email , String password){

        try {

            progressD = new ProgressDialog(context);
            progressD.setMessage("Logging in ...");
            progressD.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth= FirebaseAuth.getInstance();
                                currentUser = mAuth.getCurrentUser();
                                if(currentUser.isEmailVerified()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    progressD.dismiss();
                                    Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context , MapsActivity.class));
                                    //here we go to the next action
                                }else{
                                    progressD.dismiss();
                                    Toast.makeText(context, "Please Verify your email first !", Toast.LENGTH_SHORT).show();
                                }



                            } else {
                                     // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Login Authentication failed .. please check internet connection or user's email and password ..",
                                        Toast.LENGTH_SHORT).show();
                                progressD.dismiss();
                            }

                            // ...
                        }
                    });

        } catch (Exception e) {

        }

    }

    public void signUpEmail(final Context context, String email , String password){



        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(context, "Authentication Success",
                                        Toast.LENGTH_SHORT).show();
                                currentUser = mAuth.getCurrentUser();
                                VerifyEmail(currentUser , context);
                                mAuth.signOut();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Authentication failed .. please check internet connection ..",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } catch (Exception e) {

        }
    }



    public void VerifyEmail(FirebaseUser user , final Context context){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(context)
                                    .setTitle("Notification")
                                    .setMessage( "you have received an email .. Verification sent check it out !")
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.alarm);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                    context.startActivity(intent);
                                    alert.setCancelable(true);
                                }
                            });
                            alert.create().show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(registerLayout.getVisibility() == View.VISIBLE){
            registerLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }else{
            finishAffinity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
// Prepare the View for the animation
        loginLayout.setVisibility(View.VISIBLE);
        loginLayout.setAlpha(0.0f);
        registerLayout.setVisibility(View.GONE);

// Start the animation
        loginLayout.animate()
//                .translationY(loginLayout.getHeight())
                .alpha(1.0f).setDuration(5000);    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
