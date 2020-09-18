package sms.myunibapp.unibaServices;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myunibapp.R;

import sms.myunibapp.NewTaxFragment;
import sms.myunibapp.oggetti.DrawerActivity;

public class Secretary extends DrawerActivity {

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segreteria);

        cardView = findViewById(R.id.button_tax);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.SecreteryContainer, new NewTaxFragment()).commit();
            }
        });

        
    }

}