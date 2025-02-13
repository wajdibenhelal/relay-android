package app.com.relay.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import app.com.relay.R;
import app.com.relay.model.Deliverer;
import app.com.relay.service.DelivererService;
import app.com.relay.utils.Permissions;
import app.com.relay.utils.Tools;

public class DelivererRegistrationFragment extends Fragment {

    private CircularImageView imageView;
    private Uri imageURI;
    private AppCompatEditText txtFirstName, txtLastName, txtPhoneNumber, txtAddress,
            txtMail, txtPassword;
    private RadioGroup vehicle;
    private static final int PICK_IMAGE = 100;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 13;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deliverer_registration_fragment, container, false);
        initToolbar();
        initComponent(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("General information ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initComponent(View view) {
        txtFirstName = (AppCompatEditText) view.findViewById(R.id.txt_first_name);
        txtLastName = (AppCompatEditText) view.findViewById(R.id.txt_last_name);
        txtPhoneNumber = (AppCompatEditText) view.findViewById(R.id.txt_phone_number);
        txtMail = (AppCompatEditText) view.findViewById(R.id.txt_mail);
        txtAddress = (AppCompatEditText) view.findViewById(R.id.txt_address);
        txtPassword = (AppCompatEditText) view.findViewById(R.id.txt_password);
        vehicle = (RadioGroup) view.findViewById(R.id.vehicle);

        imageView = (CircularImageView) view.findViewById(R.id.image);
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) view.findViewById(R.id.fab_picture);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Permissions.askForExternalStoragePermission(getActivity());
                } else {
                    Permissions.askForExternalStoragePermission(getActivity());

                }
            } else if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
            } else {
                Permissions.askForExternalStoragePermission(getActivity());
            }
        } else {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            saveDeliverer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDeliverer() {
        int checkedRadioButtonId = vehicle.getCheckedRadioButtonId();
        String selectedVehicle = "";
        if (txtFirstName.getText().toString().matches("")) {
            Tools.showToast("Please enter your first name", getActivity());
            return;
        }
        if (txtLastName.getText().toString().matches("")) {
            Tools.showToast("Please enter your last name", getActivity());
            return;
        }
        if (txtPhoneNumber.getText().toString().matches("")) {
            Tools.showToast("Please enter your phone number", getActivity());
            return;
        }
        if (txtMail.getText().toString().matches("")) {
            Tools.showToast("Please enter your mail", getActivity());
        }
        if (!txtMail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Tools.showToast("Please enter a valid mail", getActivity());
            return;
        }
        if (txtAddress.getText().toString().matches("")) {
            Tools.showToast("Please enter your address", getActivity());
            return;
        }
        if (txtPassword.getText().toString().matches("")) {
            Tools.showToast("Please enter your password", getActivity());
            return;
        }
        if (vehicle.getCheckedRadioButtonId() == -1) {
            Tools.showToast("Please select a vehicle", getActivity());
        }else {
            if (checkedRadioButtonId == R.id.radio_two_wheels) {
                selectedVehicle = "TWO WHEELS";
            } else if (checkedRadioButtonId == R.id.radio_car) {
                selectedVehicle = "CAR";
            } else if (checkedRadioButtonId == R.id.radio_truck) {
                selectedVehicle = "TRUCK";
            }
        }

        DelivererService delivererService = new DelivererService(getContext(), getActivity());
        Deliverer deliverer = new Deliverer(
                txtFirstName.getText().toString(),
                txtLastName.getText().toString(),
                txtPhoneNumber.getText().toString(),
                txtAddress.getText().toString(),
                txtMail.getText().toString(),
                txtPassword.getText().toString(),
                selectedVehicle,
                "My image"
        );

        delivererService.addNewDeliverer(deliverer);

    }
}
