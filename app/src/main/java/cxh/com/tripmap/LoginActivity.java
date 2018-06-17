package cxh.com.tripmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Format;
import java.util.List;

import cxh.com.tripmap.dao.SiteDao;
import cxh.com.tripmap.entity.SiteBean;
import cxh.com.tripmap.util.HttpCallbackListener;
import cxh.com.tripmap.util.HttpUtil;
import okhttp3.FormBody;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String pass = password.getText().toString();
                FormBody formBody = new FormBody.Builder()
                .add("phone",userName).add("password",pass).build();
                HttpUtil.getInstance().post("http://140.143.225.154:8081/user/login",formBody, new HttpCallbackListener() {

                @Override
                public void onFinish(String response) {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(final Exception e) {
                    super.onError(e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            }
        });
    }

}
