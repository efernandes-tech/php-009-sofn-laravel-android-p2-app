package br.com.edersonfernandes.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.ListView;

public class Bills extends Fragment implements View.OnClickListener {

    private Button btnNewBills;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        btnNewBills = (Button) view.findViewById(R.id.btn_new_bills);
        btnNewBills.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        getActivity().setTitle("List Bills");
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new NewBills());
        ft.commit();
    }
}
