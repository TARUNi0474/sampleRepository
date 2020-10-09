package com.example.agriproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.LOCATION_SERVICE;

public class RegisterFragment extends Fragment {
    LocationManager manager;
    TextView tv;
    EditText  uname,email,password,number,pincode;
    Button register;
    double lat, log;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        reference=FirebaseDatabase.getInstance().getReference("Users");
        tv = v.findViewById(R.id.loc);
        uname=v.findViewById(R.id.uname);
        email=v.findViewById(R.id.email);
        password=v.findViewById(R.id.password);
        number=v.findViewById(R.id.number);
        pincode=v.findViewById(R.id.pincode);
        register=v.findViewById(R.id.register);
        manager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                log = location.getLongitude();
            }
        };
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, listener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);

        tv.setText(lat+","+log);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uloc=lat+","+log;
                String uuname=uname.getText().toString();
                String uemail=email.getText().toString();
                String upassword=password.getText().toString();
                String unumber=number.getText().toString();
                String upincode=pincode.getText().toString();
                MyModel model=new MyModel(uuname,uemail,upassword,unumber,upincode,uloc);
                String id=reference.push().getKey();
                reference.child(id).setValue(model);
            }
        });
        return v;
    }
}