package org.cloud.mclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends BaseActivity {

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        View decodeView = getWindow().getDecorView();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }).start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
