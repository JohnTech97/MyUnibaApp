package sms.myunibapp.advancedViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myunibapp.R;

public class DashboardWidgets extends CardView {

    private ImageView icon;
    private TextView nomeWidget;
    private TextView descrizione;
    private Class target;

    public DashboardWidgets(@NonNull Context context) {
        super(context);
        initializeViews(context, null);
    }

    public DashboardWidgets(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public DashboardWidgets(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context c, AttributeSet set){
        ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.template_dashboard_widgets, this, true);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void inflate() {

        icon = findViewById(R.id.logo_widget);
        nomeWidget = findViewById(R.id.nome_widget);
        descrizione = findViewById(R.id.descrizione_widget);
    }

    public Drawable getIcon() {
        return icon.getDrawable();
    }

    public void setIcon(Drawable icon) {
        this.icon.setImageDrawable(icon);
    }

    public String getNomeWidget() {
        return nomeWidget.getText().toString();
    }

    public void setNomeWidget(String nomeWidget) {
        this.nomeWidget.setText(nomeWidget);
    }

    public String getDescrizioneWidget(){
        return descrizione.toString();
    }

    public void setDescrizione(String desc){
        descrizione.setText(desc);
    }

    public Class getTarget(){return target;}

    public void setTarget(Class t){target=t;}

}