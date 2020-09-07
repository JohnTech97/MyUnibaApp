package sms.myunibapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myunibapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import sms.myunibapp.adapter.TaxAdapter;
import sms.myunibapp.oggetti.ModelTax;

/**
 * Questa classe consente la creazione di una lista di tasse.
 */
public class NewTaxFragment extends Fragment {

    RecyclerView recyclerViewTax;
    List<ModelTax> itemList;



    public NewTaxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_tax, container, false);

        recyclerViewTax = view.findViewById(R.id.recyclerViewTax);
        recyclerViewTax.setHasFixedSize(true);
        recyclerViewTax.setLayoutManager(new LinearLayoutManager(getContext()));
        
        //initData();

        recyclerViewTax.setAdapter(new TaxAdapter(initData()));
        return view;
    }

    /* ACCESSO AL DATABASE */
    DatabaseReference studente = FirebaseDatabase.getInstance().getReference().child("Studente");

    private List<ModelTax> initData() {

        itemList = new ArrayList<>();
        itemList.add(new ModelTax("2019","Tassa 3° Anno (2° Anno Fuori corso)","Da pagare €500 | Pagato €2.500","22/10/2020"));
        itemList.add(new ModelTax("2018","Tassa 3° Anno (1° Anno Fuori corso)","Da pagare €0 | Pagato €2.517","22/10/2019"));
        itemList.add(new ModelTax("2017","Tassa 3° Anno ","Da pagare €0 | Pagato €2.000","22/10/2018"));
        itemList.add(new ModelTax("2016","Tassa 2° Anno ","Da pagare €0 | Pagato €1000","22/10/2017"));
        itemList.add(new ModelTax("2015","Tassa 1° Anno ","Da pagare €0 | Pagato €500","22/10/2016"));

        return  itemList;
    }
}