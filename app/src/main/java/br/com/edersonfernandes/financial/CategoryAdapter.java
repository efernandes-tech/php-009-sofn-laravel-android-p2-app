package br.com.edersonfernandes.financial;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ederson.fernandes on 25/04/2018.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Activity context;
    ArrayList<Category> data = null;

    public CategoryAdapter(Activity context, int resource, ArrayList<Category> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Category category = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner, parent, false);
        }

        Category item = data.get(position);

        if (item != null) {
            TextView categoryName = (TextView) view.findViewById(R.id.item_name);
            if (categoryName != null) {
                categoryName.setText((item.getName()));
            }
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        View row = view;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.category_spinner, parent, false);
        }

        Category item = data.get(position);

        if (item != null) {
            TextView categoryName = (TextView) row.findViewById(R.id.item_name);
            TextView categoryId = (TextView) row.findViewById(R.id.item_id);
            if (categoryName != null) {
                categoryName.setText((item.getName()));
            }
            if (categoryId != null) {
                categoryId.setText((item.getId()));
            }
        }

        return row;
    }

}
