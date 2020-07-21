package sms.myunibapp.advancedViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myunibapp.R;

public class DashboardWidgets extends CardView {

    private ImageView icona;
    private TextView nomeWidget;


    public DashboardWidgets(@NonNull Context context) {
        super(context);
        initializeViews(context);
    }

    public DashboardWidgets(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public DashboardWidgets(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context c){
        ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.template_dashboard_widgets, this, true);
    }

    public void inflate() {

        icona = findViewById(R.id.logo_widget);
        nomeWidget = findViewById(R.id.nome_widget);
    }

    public Drawable getIcona() {
        return icona.getDrawable();
    }

    public void setIcona(Drawable icona) {
        this.icona.setImageDrawable(icona);
    }

    public String getNomeWidget() {
        return nomeWidget.getText().toString();
    }

    public void setNomeWidget(String nomeWidget) {
        this.nomeWidget.setText(nomeWidget);
    }
}