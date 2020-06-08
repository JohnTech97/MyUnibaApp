package com.example.myunibaapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CreatoreElencoEsami extends View {

    private Context ctx;

    private String titolo, desc;
    private Drawable iconaEsame;

    private Paint background = new Paint();

    private Paint nero = new Paint();

    private int round;

    public CreatoreElencoEsami(Context context) {
        super(context);
    }

    public CreatoreElencoEsami(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ctx = context;
        TypedArray arr = ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.CreatoreElencoEsami, 0, 0);

        titolo = arr.getString(R.styleable.CreatoreElencoEsami_titolo);
        desc = arr.getString(R.styleable.CreatoreElencoEsami_descrizione);
        iconaEsame = arr.getDrawable(R.styleable.CreatoreElencoEsami_icona);
        round= arr.getInt(R.styleable.CreatoreElencoEsami_angoli,20);
        //get background come attributo xml

        //background.setColor(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android","background",-1));
        background.setColor(context.obtainStyledAttributes(attrs, new int[]{android.R.attr.colorBackground}).getColor(0,0));
        nero.setColor(Color.BLACK);
        nero.setTextSize(40);
        arr.recycle();

    }

    @Override
    protected void onDraw(Canvas c) {
        RectF base = new RectF();
        base.top = 0;
        base.left = 0;
        base.bottom = base.top + c.getHeight();
        base.right = base.left + c.getWidth();
        c.drawRoundRect(base, round, round, background);
        c.drawText(titolo, 9, 8 + nero.getTextSize(), nero);
        if (iconaEsame != null) {
            iconaEsame.setBounds(c.getWidth() - iconaEsame.getMinimumWidth() - 30, 4, c.getWidth() - 30, 4 + iconaEsame.getMinimumHeight());
            iconaEsame.draw(c);
        }
    }
}
