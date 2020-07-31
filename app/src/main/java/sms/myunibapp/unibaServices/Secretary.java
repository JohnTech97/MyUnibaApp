package sms.myunibapp.unibaServices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myunibapp.R;

public class Secretary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segreteria);

        
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}