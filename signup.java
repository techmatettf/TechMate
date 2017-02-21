package com.example.vatsal.techmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.vatsal.techmate.R.id.status;

public class signup extends AppCompatActivity implements View.OnClickListener {

    ImageView dpv;
    EditText u_namev, statusv, emailv, passv, repassv, m_nov, websitev;
    private static final int Result_load_image = 1;
    ProgressDialog mDialog;
    Button signupv;
    String register_url = "http://techmatettf.000webhostapp.com/register.php";
    String gender, qualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dpv = (ImageView)findViewById(R.id.dp);
        u_namev = (EditText)findViewById(R.id.u_name);
        statusv = (EditText)findViewById(status);
        emailv = (EditText)findViewById(R.id.email);
        passv = (EditText)findViewById(R.id.pass);
        repassv = (EditText)findViewById(R.id.repass);
        m_nov = (EditText)findViewById(R.id.m_no);
        websitev = (EditText)findViewById(R.id.website);
        signupv = (Button)findViewById(R.id.signup);
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioSex);
        gender = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        qualification = "b.tech";
        dpv.setOnClickListener(this);
        signupv.setOnClickListener(this);

    }

    public void onClick(View v) {

        // for image upload
        switch (v.getId()) {
            case R.id.dp:
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, Result_load_image);
                break;
            case R.id.signup:
                Bitmap image = ((BitmapDrawable) dpv.getDrawable()).getBitmap();
                mDialog = ProgressDialog.show(signup.this, "Please wait...", "Uploading image...", true);

                new transfer(image, u_namev.getText().toString(), statusv.getText().toString(), emailv.getText().toString(), passv.getText().toString(), m_nov.getText().toString(), websitev.getText().toString(), gender, qualification).execute();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Result_load_image && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            dpv.setImageURI(selectedImage);
        }
    }

    private class transfer extends AsyncTask<Object, Object, Void> {
        Bitmap image;
        String u_name;
        String status;
        String email;
        String pass;
        String m_no;
        String website;
        String gender;
        String qualifiaction;

        public transfer(Bitmap imageb, String u_nameb, String statusb, String emailb, String passb, String m_nob, String websiteb, String genderb, String qualifiacationb) {

            this.image = imageb;
            this.u_name = u_nameb;
            this.status = statusb;
            this.email = emailb;
            this.pass = passb;
            this.m_no = m_nob;
            this.website = websiteb;
            this.gender = genderb;
this.qualifiaction=qualifiacationb;
        }

        @Override
        protected Void doInBackground(Object... params) {
            try {
                System.out.println(image + u_name + status + email + pass + m_no + website + gender );
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("u_name", "UTF-8") + "=" + URLEncoder.encode(u_name, "UTF-8") + "&" + URLEncoder.encode("email_id", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(image), "UTF-8") + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8") + "&" + URLEncoder.encode("m_no", "UTF-8") + "=" + URLEncoder.encode(m_no, "UTF-8") + "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&" + URLEncoder.encode("website", "UTF-8") + "=" + URLEncoder.encode(website, "UTF-8") + "&" + URLEncoder.encode("qualification", "UTF-8") + "=" + URLEncoder.encode(qualification, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                    System.out.println(result);
                    System.out.println("register");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                if (result.equals("User created successfully.")) {
                    System.out.println("Enter next page");
                    Intent intent = new Intent(signup.this, login.class);
                    startActivity(intent);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
