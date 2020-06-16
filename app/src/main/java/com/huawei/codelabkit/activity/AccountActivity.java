package com.huawei.codelabkit.activity;

import android.content.Intent;
import android.os.Bundle;

import com.huawei.codelabkit.Constant;
import com.huawei.codelabkit.R;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_result;
    private HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;
    public static final String TAG = "Account Kit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        findViewById(R.id.btn_id_token).setOnClickListener(this);
        findViewById(R.id.btn_auth_code_mode).setOnClickListener(this);
        findViewById(R.id.btn_sign_out).setOnClickListener(this);

        txt_result = findViewById(R.id.txt_result);

        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(AccountActivity.this, mAuthParam);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_id_token:
                signIn();
                break;
            case R.id.btn_auth_code_mode:
                signInCode();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
            default:
                break;
        }

    }

    private void signIn() {
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN);
    }


    private void signInCode() {
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setAuthorizationCode()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(AccountActivity.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN) {
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                printResult(huaweiAccount, "Id Token");
                Log.i(TAG, "signIn token success " + huaweiAccount.getDisplayName());
            } else {
                Log.i(TAG, "signIn token failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                printResult(huaweiAccount, "Authorization Code");
                Log.i(TAG, "signIn code success " + huaweiAccount.getDisplayName());
            } else {
                Log.i(TAG, "signIn code failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
    }

    private void printResult(AuthHuaweiId huaweiAccount, String type) {
        String resultType = type.equals("Id Token") ? "AccessToken: " + huaweiAccount.getAccessToken() : "AuthorizationCode: " + huaweiAccount.getAuthorizationCode();
        txt_result.setText("welcome: " + huaweiAccount.getDisplayName() + " SignIn Success \n" +
                "SignIn Mode: " + type + "\n" + resultType);

    }


    private void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                txt_result.setText("You have been Signed out successfully");
                Log.i(TAG, "signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut  test fail");
            }
        });
    }

}
