package sms.myunibapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class EsitoEsameCustomView extends View {

    private Paint positivo;
    private Paint negativo;

    private int voto=0;

    public EsitoEsameCustomView(Context context) {
        super(context);
        initialize();
    }

    public EsitoEsameCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public EsitoEsameCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public EsitoEsameCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        positivo=new Paint();
        positivo.setColor(Color.GREEN);
        negativo=new Paint();
        negativo.setColor(Color.RED);
    }

    public void setVoto(int voto){
        this.voto=voto;
    }

    public int getVoto(){
        return voto;
    }

    @Override
    protected void onDraw(Canvas c) {
        if(voto>=18) {
            c.drawText(""+voto, 20, 20, positivo);
        }else{
            c.drawText("Bocciato", 20, 20, negativo);
        }
    }
}
