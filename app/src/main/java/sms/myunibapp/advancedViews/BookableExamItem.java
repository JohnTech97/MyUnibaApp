package sms.myunibapp.advancedViews;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myunibapp.R;

public class BookableExamItem extends ConstraintLayout {

    private TextView titoloEsame, cfu, nEsami;
    private String esame;

    public BookableExamItem(Context c) {
        super(c);
        initializeViews(c);
    }

    public BookableExamItem(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        initializeViews(c);
    }

    public BookableExamItem(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        initializeViews(c);
    }

    private void initializeViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.template_exam_basic_info, this, true);
    }

    public void inflate() {

        titoloEsame = findViewById(R.id.titolo_esame);
        cfu = findViewById(R.id.cfu);
        nEsami = findViewById(R.id.n_esami_prenotabili);
    }

    public String getTitoloEsame() {
        return titoloEsame.getText().toString();
    }

    public void setTitoloEsame(String s) {
        titoloEsame.setText(s);
    }

    public int getCfu() {
        return Integer.parseInt(cfu.getText().toString());
    }

    public void setCfu(int cfu) {
        this.cfu.setText("" + cfu);
    }

    public int getNEsamiPrenotabili() {
        return Integer.parseInt(nEsami.getText().toString());
    }

    public void setEsamiPrenotabili(int n) {
        nEsami.setText("" + n);
    }

    public void setLink(String esame) {
        this.esame = esame;
    }

    public String getLink() {
        return esame;
    }

}
