package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.Manifest;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private TextView textView;
    private SeekBar seekBar;

    String gender, qualification, countryCode;
    ImageView profileimage;
    EditText etfirstname, etlastname, etemail, etphonenumber, etpassword, priceET;
    TextView errorfirstname, errorlastname, erroremail, errorphone, errorstate, errorgender, errorPrice, erroreducation, errorimagetext, errorpassword, dollarTextView;
    ;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    Button submitbutton;
    Spinner spinnerstate;
    RadioButton radiomale, radiofemale;
    CountryCodePicker countryCodePicker;
    CheckBox btech, bba, mba, mtech;
    boolean isProfilePicUploaded, isValidPrice;
    int isAppPermission = 0, isStateSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        spinnerstate = findViewById(R.id.spinnerstate);

        spinnerstate.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(0, "Select State");
        categories.add("Delhi");
        categories.add("Haryana");
        categories.add("Noida");
        categories.add("Rajasthan");
        categories.add("Noida");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinnneritem, categories);

           dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spinnerstate.setAdapter(dataAdapter);

        textView = (TextView) findViewById(R.id.textView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        radiomale = findViewById(R.id.radiomale);
        radiofemale = findViewById(R.id.radiofemale);
        countryCodePicker = findViewById(R.id.countrycodepicker);
        btech = findViewById(R.id.btech);
        mtech = findViewById(R.id.mtech);
        mba = findViewById(R.id.mba);
        bba = findViewById(R.id.bba);
        errorpassword = findViewById(R.id.errorpassword);
        etpassword = findViewById(R.id.etpassword);
        etfirstname = findViewById(R.id.etfirstname);
        etlastname = findViewById(R.id.etlastname);
        profileimage = findViewById(R.id.profile_image);
        submitbutton = findViewById(R.id.btnsubmit);
        etemail = findViewById(R.id.etemail);
        etphonenumber = findViewById(R.id.etphonenumber);
        priceET = findViewById(R.id.priceEditText);
        priceET.addTextChangedListener(priceLisner);
        dollarTextView = findViewById(R.id.dollarTextView);
        errorPrice = findViewById(R.id.errorPriceTV);
        errorimagetext = findViewById(R.id.errorimagetext);
        errorfirstname = findViewById(R.id.errorfirstname);
        errorlastname = findViewById(R.id.errorlastname);
        erroremail = findViewById(R.id.erroremail);
        errorstate = findViewById(R.id.errorstate);
        errorphone = findViewById(R.id.errorphone);
        errorgender = findViewById(R.id.errorgender);

        erroreducation = findViewById(R.id.erroreducation);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("" + progress + "%");



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
                requestMultiplePermissions();

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageBoolean() && isFirstnameBoolean() && isLastnameBoolean() && isEmailBoolean() && isPhoneBoolean() && isStateSelected()
                        && isValidGender() && isValidPrice() && isCheckboxBoolean() && isPasswordBoolean()) {
                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });


    }


    private TextWatcher priceLisner = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            String tempprice = priceET.getText().toString();
            if (tempprice == null) {

            } else {
                dollarTextView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private boolean isValidPrice() {
        String tempprice = priceET.getText().toString();
        if (tempprice.isEmpty()) {
            errorPrice.setText("*Please enter amount. ");
            errorPrice.setVisibility(View.VISIBLE);
            return false;

        } else {
            errorPrice.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private boolean isImageBoolean() {
        if (!isProfilePicUploaded) {
            errorimagetext.setVisibility(View.VISIBLE);
            errorimagetext.setText("Please upload profile picture.");
            return false;

        } else {
            errorimagetext.setVisibility(View.INVISIBLE);
            return true;

        }
    }

    private boolean isValidGender() {
        if (radiomale.isChecked() || radiofemale.isChecked()) {
            if (radiomale.isChecked()) {
                gender = "Male";
                errorgender.setVisibility(View.INVISIBLE);
                return true;
            } else if (radiofemale.isChecked()) {
                gender = "Female";
                errorgender.setVisibility(View.INVISIBLE);
                return true;
            } else {
                gender = "Other";
                errorgender.setVisibility(View.INVISIBLE);
                return true;
            }

        } else {
            errorgender.setVisibility(View.VISIBLE);
            errorgender.setText("*Please select gender.");
            return false;
        }

    }

    private boolean isFirstnameBoolean() {
        String firstname = etfirstname.getText().toString();
        if (firstname.trim().isEmpty()) {
            errorfirstname.setVisibility(View.VISIBLE);
            errorfirstname.setText("*Please enter first name.");
            return false;

        } else if (Pattern.compile("[a-zA-Z]{2,40}").matcher(firstname).matches()) {
            errorfirstname.setVisibility(View.INVISIBLE);
            return true;

        } else {
            errorfirstname.setVisibility(View.VISIBLE);
            errorfirstname.setText("*Please enter valid name. ");
            return false;

        }
    }

    private boolean isLastnameBoolean() {
        String lastname = etlastname.getText().toString();
        if (lastname.trim().isEmpty()) {
            errorlastname.setVisibility(View.VISIBLE);
            errorlastname.setText("*Please enter last name. ");
            return false;

        } else if (Pattern.compile("([a-zA-Z]{2,40})").matcher(lastname).matches()) {
            errorlastname.setVisibility(View.INVISIBLE);
            return true;

        } else {
            errorlastname.setVisibility(View.VISIBLE);
            errorlastname.setText("Please enter valid name. ");
            return false;

        }
    }

    private boolean isEmailBoolean() {
        String email = etemail.getText().toString();
        if (email.trim().isEmpty()) {
            erroremail.setVisibility(View.VISIBLE);
            erroremail.setText("*Please enter your email. ");
            return false;

        } else if (Pattern.compile("^[a-zA-Z0-9]+[.!#$%&'*+/=?^_`{|}~-]*[a-zA-Z0-9]*@[a-zA-Z0-9]+[.]+[a-zA-Z0-9.]+[a-zA-Z0-9]+").matcher(email).matches()) {
            erroremail.setVisibility(View.INVISIBLE);
            return true;

        } else {
            erroremail.setVisibility(View.VISIBLE);
            erroremail.setText("*Email is not valid! ");
            return false;

        }
    }

    boolean isPhoneBoolean() {
        if (countryCodePicker.getSelectedCountryCode().equals(null)) {
            countryCode = countryCodePicker.getDefaultCountryCodeWithPlus();
        } else {
            countryCode = countryCodePicker.getSelectedCountryCode();
        }
        String phone = etphonenumber.getText().toString();
        if (phone.isEmpty()) {
            errorphone.setVisibility(View.VISIBLE);
            errorphone.setText("Please enter phone");
            return false;
        } else if (phone.length() > 7 && phone.length() < 13) {
            errorphone.setVisibility(View.INVISIBLE);
            return true;
        } else {
            errorphone.setVisibility(View.VISIBLE);
            errorphone.setText("Number must in between 8-12");
            return false;

        }
    }

    private boolean isPasswordBoolean() {
        String password = etpassword.getText().toString();
        if (password.isEmpty()) {
            errorpassword.setVisibility(View.VISIBLE);
            errorpassword.setText("*Please enter password.");
            return false;

        } else if (password.length() > 7 && password.length()<15) {
            errorpassword.setVisibility(View.INVISIBLE);

            return true;

        } else {
            errorpassword.setVisibility(View.VISIBLE);
            errorpassword.setText("*Please enter password contaning minimum length 8 and maximum 15. ");
            return false;

        }

    }

    private boolean isCheckboxBoolean() {
        int temp = 0;
        if (btech.isChecked()) {
            qualification = qualification + " B.Tech";
            temp++;
        }
        if (mtech.isChecked()) {
            qualification = qualification + " M.Tech";
            temp++;
        }
        if (mba.isChecked()) {
            qualification = qualification + " MBA";
            temp++;
        }
        if (bba.isChecked()) {
            qualification = qualification + " BBA";
            temp++;
        }

        if (temp == 0) {
            erroreducation.setVisibility(View.VISIBLE);
            erroreducation.setText("*Please select atleast a qualification. ");
            return false;
        } else {
            erroreducation.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    boolean isStateSelected() {
        if (isStateSelected == 0) {
            errorstate.setVisibility(View.VISIBLE);
            errorstate.setText("*Please select state. ");
            return false;

        } else {
            errorstate.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera", "Cancel"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                            case 2:
                                dialog.dismiss();

                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {

            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    isProfilePicUploaded = true;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(SignupActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profileimage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileimage.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            isProfilePicUploaded = true;
            Toast.makeText(SignupActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //permissions
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            isAppPermission = isAppPermission + 1;
                            if (isAppPermission != 0) {
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();

                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        String tempString = "Select State";
        if (item.equals(tempString)) {
            isStateSelected = 0;
        } else {
            isStateSelected = 2;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
