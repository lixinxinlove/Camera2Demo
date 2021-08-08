package com.lixinxinlove.camerademo

import android.Manifest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE

            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                } else {
                    Toast.makeText(this, "这些权限被拒绝: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }
}
