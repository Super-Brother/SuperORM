package com.wenchao.superorm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int checkSelfPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            insertUser();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 666);
        }
    }

    private void insertUser() {
        User user = new User();
        user.setUsername("wenchao");
        user.setPassword("111222333");

        final BaseDao baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        baseDao.insert(user);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 666) {
            insertUser();
        }
    }
}
