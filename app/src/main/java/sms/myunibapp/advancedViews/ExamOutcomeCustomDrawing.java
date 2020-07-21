package sms.myunibapp.advancedViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ExamOutcomeCustomDrawing extends View {

    private Paint positivoBorder, negativoBorder, positivo, negativo, fill;

    private String esito = "0", bocciato = "Bocciato";

    public ExamOutcomeCustomDrawing(Context context) {
        super(context);
        initialize();
    }

    public ExamOutcomeCustomDrawing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ExamOutcomeCustomDrawing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public ExamOutcomeCustomDrawing(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        positivoBorder = new Paint();
        positivoBorder.setColor(Color.GREEN);
        positivoBorder.setStyle(Paint.Style.STROKE);
        positivoBorder.setStrokeWidth(10);
        negativoBorder = new Paint();
        negativoBorder.setColor(Color.RED);
        negativoBorder.setStyle(Paint.Style.STROKE);
        negativoBorder.setStrokeWidth(10);
        positivo = new Paint();
        positivo.setTextSize(50);
        positivo.setColor(Color.GREEN);
        negativo = new Paint();
        negativo.setTextSize(50);
        negativo.setColor(Color.RED);
        fill = new Paint();
        fill.setColor(Color.WHITE);
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getEsito() {
        return esito;

    }

    private int centroX, centroY;
    private Rect bound = new Rect();

    @Override
    protected void onDraw(Canvas c) {
        centroX = c.getWidth() / 2;
        centroY = c.getHeight() / 2;
        //se l'esito dell'esame è un numero allora lo studente è promosso
        try {
            if (Integer.parseInt(esito) >= 18) {
                //riempio prima uno spazio bianco
                c.drawCircle(centroX, centroY, centroX, fill);
                c.drawCircle(centroX, centroY, centroX - 10, positivoBorder);
                negativo.getTextBounds(esito, 0, esito.length(), bound);
                c.drawText(esito, centroX - bound.width() / 2, centroY + bound.height() / 2, positivo);
            } else {
                c.drawRect(10, 10, centroX * 2-10, centroY * 2-10, fill);
                c.drawRect(10, 10, centroX * 2-10, centroY * 2-10, negativoBorder);
                negativo.getTextBounds(bocciato, 0, bocciato.length(), bound);
                c.drawText(bocciato, centroX - bound.width() / 2, centroY + bound.height() / 2, negativo);
            }
        } catch (NumberFormatException e) {
            //altrimenti può avere un altro risvolto: assente, ritirato, ecc;
            c.drawRect(10, 10, centroX * 2-10, centroY * 2-10, fill);
            c.drawRect(10, 10, centroX * 2-10, centroY * 2-10, negativoBorder);
            negativo.getTextBounds(esito, 0, esito.length(), bound);
            c.drawText(esito, centroX - bound.width() / 2, centroY + bound.height() / 2, negativo);
        }
    }
}
