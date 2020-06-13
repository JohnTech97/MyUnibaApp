package sms.myunibapp.CompoundViews;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myunibapp.R;

public class InfoEsamePrenotabili extends ConstraintLayout {

    private TextView titoloEsame, cfu, nEsami;
    private ImageView iconaEsame;

    private Context ctx;
    private String esame;

    private final int dimensioniMassime = 200;


    public InfoEsamePrenotabili(Context c) {
        super(c);
        initializeViews(c);
    }

    public InfoEsamePrenotabili(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        initializeViews(c);
    }

    public InfoEsamePrenotabili(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        initializeViews(c);
    }


    /**
     * Inflates the views in the layout.
     *
     * @param context the current context for the view.
     */
    private void initializeViews(Context context) {
        ctx = context;
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.esame_info_template, this, true);
    }

    public void inflate() {

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        titoloEsame = findViewById(R.id.titolo_esame);
        cfu = findViewById(R.id.cfu);
        iconaEsame = findViewById(R.id.icona_esame);
        iconaEsame.setMaxWidth(dimensioniMassime);
        iconaEsame.setMaxHeight(dimensioniMassime);

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

    public Drawable getIconaEsame() {
        return iconaEsame.getDrawable();
    }

    public void setIconaEsame(Drawable iconaEsame) {
        this.iconaEsame.setImageDrawable(iconaEsame);
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
