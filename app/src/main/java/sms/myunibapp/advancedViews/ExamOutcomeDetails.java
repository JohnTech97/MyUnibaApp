package sms.myunibapp.advancedViews;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myunibapp.R;

public class ExamOutcomeDetails extends LinearLayout {

    private Context ctx;

    private TextView nomeEsame, data, docente, refuseBack;
    private ExamOutcomeCustomDrawing esito;
    private Button accetta;

    public ExamOutcomeDetails(Context context) {
        super(context);
        initializeViews(context);
    }

    public ExamOutcomeDetails(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public ExamOutcomeDetails(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public ExamOutcomeDetails(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        ctx = context;
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.template_exam_outcome, this, true);
    }

    public void inflate() {

        nomeEsame = findViewById(R.id.nome_esame_esito);
        data = findViewById(R.id.data_esito_esame);
        docente = findViewById(R.id.docente_bacheca_esiti);
        refuseBack = findViewById(R.id.refuse_back);
        refuseBack.setClickable(true);
        esito = findViewById(R.id.esito);
        accetta = findViewById(R.id.accettazione);

    }

    public void setEsito(String esito) {
        //se supera l'esame il bottone di accettazione rimane visibile
        Resources res = ctx.getResources();
        try {
            if (Integer.parseInt(esito) < 18) {
                accetta.setVisibility(View.GONE);
                refuseBack.setText(res.getString(R.string.back));
            }
        } catch (NumberFormatException e) {
            accetta.setVisibility(View.GONE);
            refuseBack.setText(res.getString(R.string.back));
        }
        this.esito.setEsito(esito);
    }

    public String getEsito() {
        return esito.getEsito();
    }

    public void setNomeEsame(String nomeEsame) {
        this.nomeEsame.setText(nomeEsame);
    }

    public void setData(String data) {
        this.data.setText(data);
    }

    public void setDocente(String docente) {
        this.docente.setText(docente);
    }

    public void setRefuseBack(String refuseBack) {
        this.refuseBack.setText(refuseBack);
    }

    public TextView getRefuseBack(){
        return refuseBack;
    }

    public Button getPulsanteAccetta(){
        return accetta;
    }
}