package sms.myunibapp.oggetti;

import android.view.View;
import android.widget.TextView;

public class Tax {

    private String dateTax;
    private String titleTax;
    private String toPayAndPayed;
    private String scadenzaTax;

    public Tax(String dateTax, String titleTax, String toPayAndPayed, String scadenzaTax) {
        this.dateTax = dateTax;
        this.titleTax = titleTax;
        this.toPayAndPayed = toPayAndPayed;
        this.scadenzaTax = scadenzaTax;
    }

    public String getDateTax() {
        return dateTax;
    }

    public void setDateTax(String dateTax) {
        this.dateTax = dateTax;
    }

    public String getTitleTax() {
        return titleTax;
    }

    public void setTitleTax(String titleTax) {
        this.titleTax = titleTax;
    }

    public String getToPayAndPayed() {
        return toPayAndPayed;
    }

    public void setToPayAndPayed(String toPayAndPayed) {
        this.toPayAndPayed = toPayAndPayed;
    }

    public String getScadenzaTax() {
        return scadenzaTax;
    }

    public void setScadenzaTax(String scadenzaTax) {
        this.scadenzaTax = scadenzaTax;
    }

}
