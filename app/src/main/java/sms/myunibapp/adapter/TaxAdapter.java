package sms.myunibapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myunibapp.R;

import java.util.List;

import sms.myunibapp.oggetti.ModelTax;

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.ViewHolder> {

    List<ModelTax> itemModelTaxList;

    public TaxAdapter(List<ModelTax> itemModelTaxList) {
            this.itemModelTaxList = itemModelTaxList;
    }

    @NonNull
    @Override
    public TaxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_itemtax, parent, false);
        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaxAdapter.ViewHolder holder, int position) {
        holder.dateTax.setText(itemModelTaxList.get(position).getDateTax());
        holder.titleTax.setText(itemModelTaxList.get(position).getTitleTax());
        holder.toPayAndPayed.setText(itemModelTaxList.get(position).getToPayAndPayed());
        holder.scadenzaTax.setText(itemModelTaxList.get(position).getScadenzaTax());
    }

    @Override
    public int getItemCount() {
        return itemModelTaxList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTax;
        TextView titleTax;
        TextView toPayAndPayed;
        TextView scadenzaTax;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTax = itemView.findViewById(R.id.dateOfTax);
            titleTax = itemView.findViewById(R.id.titleOfTax);
            toPayAndPayed = itemView.findViewById(R.id.debitOfTax);
            scadenzaTax = itemView.findViewById(R.id.scadenzaOfTax);
        }
    }
}
