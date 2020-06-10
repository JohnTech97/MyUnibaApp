package sms.myunibapp;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myunibapp.R;

public class InfoEsame extends ConstraintLayout {

    private TextView titoloEsame;


    public InfoEsame(Context c) {
        super(c);
        initializeViews(c);
    }

    public InfoEsame(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        initializeViews(c);
    }

    public InfoEsame(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        initializeViews(c);
    }


    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.base, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        titoloEsame = (TextView)findViewById(R.id.titolo_esame);
    }

    public String getText(){
        return titoloEsame.getText().toString();
    }

    public void setText(String s){
        titoloEsame.setText(s);
    }
}
