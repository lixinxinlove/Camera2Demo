package com.lixinxinlove.camerademo;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.lixinxinlove.camerademo.fragment.Camera2VideoFragment;
import com.permissionx.guolindev.PermissionX;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        PermissionX.init(this)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                ).request((allGranted, grantedList, deniedList) -> {

            if (allGranted) {


                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, Camera2VideoFragment.newInstance(), "lee")
                        .commit();
            } else {
                Toast.makeText(HomeActivity.this, "这些权限被拒绝: $deniedList", Toast.LENGTH_LONG).show();
            }
        });





    }
}