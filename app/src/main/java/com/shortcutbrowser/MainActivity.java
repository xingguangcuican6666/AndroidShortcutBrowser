package com.shortcutbrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private EditText urlEditText;
    private Button installButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEditText = findViewById(R.id.urlEditText);
        installButton = findViewById(R.id.installButton);

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString().trim();
                if (!url.startsWith("http")) {
                    Toast.makeText(MainActivity.this, "请输入有效网址", Toast.LENGTH_SHORT).show();
                    return;
                }
                new FaviconTask(url).execute();
            }
        });
    }

    private class FaviconTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        public FaviconTask(String url) {
            this.url = url;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                Uri uri = Uri.parse(url);
                String faviconUrl = uri.getScheme() + "://" + uri.getHost() + "/favicon.ico";
                URL iconUrl = new URL(faviconUrl);
                HttpURLConnection conn = (HttpURLConnection) iconUrl.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                InputStream is = conn.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                return bmp;
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            createShortcut(url, bitmap);
        }
    }

    private void createShortcut(String url, Bitmap icon) {
        Intent shortcutIntent = new Intent(this, ShortcutActivity.class);
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.putExtra("url", url);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, url);
        if (icon != null) {
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        } else {
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher));
        }
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        sendBroadcast(intent);
        Toast.makeText(this, "快捷方式已创建", Toast.LENGTH_SHORT).show();
    }
}
