package br.com.edersonfernandes.financial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ederson.fernandes on 02/05/2018.
 */

public class CustomAdapterBills extends ArrayAdapter<Bill> {

    private Context context;

    public CustomAdapterBills(Context context, int resource, ArrayList<Bill> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Bill bill = getItem(position);

        if (view == null ){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_bills, parent, false);
        }

        view.setLongClickable(true);

        TextView txtName = (TextView) view.findViewById(R.id.txt_bills_name);
        TextView txtId = (TextView) view.findViewById(R.id.txt_bills_id);
        TextView txtValue = (TextView) view.findViewById(R.id.txt_bills_value);

        TextView txtSlug = (TextView) view.findViewById(R.id.txt_bills_slug);

        txtName.setText(bill.getName());
        txtId.setText(bill.getId());
        txtValue.setText(bill.getValue().toString());

        txtSlug.setText(bill.getName() + " - R$" + bill.getValue().toString());

        return view;
    }

}