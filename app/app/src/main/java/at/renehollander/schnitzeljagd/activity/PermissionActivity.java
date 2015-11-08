package at.renehollander.schnitzeljagd.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.renehollander.schnitzeljagd.util.ArrayUtil;
import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;

public class PermissionActivity extends Activity {

    private static final List<String> NEEDED_PERMISSIONS = Arrays.asList(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
    );
    private static final int PERMISSION_REQUEST_CODE = 0;

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> collect = StreamSupport.stream(NEEDED_PERMISSIONS).filter(s -> checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED).collect(Collectors.toList());
            String[] missingPermissions = collect.toArray(new String[collect.size()]);
            if (missingPermissions.length >= 1) {
                requestPermissions(missingPermissions, PERMISSION_REQUEST_CODE);
            } else {
                startMainActivity();
            }
        } else {
            startMainActivity();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissionStrings, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionStrings, grantResults);
        List<Pair<String, Integer>> permissions = ArrayUtil.mapArrayToPairList(permissionStrings, IntStreams.of(grantResults).boxed().toArray(Integer[]::new));
        List<String> denied = new ArrayList<>();
        for (Pair<String, Integer> permission : permissions) {
            if (permission.second == PackageManager.PERMISSION_DENIED) {
                denied.add(permission.first);
            }
        }
        if (denied.size() >= 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please grant this app all needed permissions")
                    .setCancelable(false)
                    .setNeutralButton("Ok", (dialog, which) -> {
                        requestPermissions(denied.toArray(new String[denied.size()]), PERMISSION_REQUEST_CODE);
                    });
            AlertDialog error = builder.create();
            error.show();
        } else {
            startMainActivity();
        }
    }
}