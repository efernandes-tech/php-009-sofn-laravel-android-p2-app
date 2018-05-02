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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Bills extends Fragment implements View.OnClickListener {

    private Button btnNewBills;
    private ListView listBills;
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Bill> bills = new ArrayList<Bill>();
    private CustomAdapterBills cBills;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        btnNewBills = (Button) view.findViewById(R.id.btn_new_bills);
        btnNewBills.setOnClickListener(this);

        listBills = (ListView) view.findViewById(R.id.listBills);
        registerForContextMenu(listBills);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        getActivity().setTitle("List Bills");

        try {
            findBills();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new NewBills());
        ft.commit();
    }

    public void findBills() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://php-008-sofn-lar-and-p1-api.herokuapp.com/api/bill_pays");

        clientGet.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        HttpResponse response = httpClient.execute(clientGet);

        String json = EntityUtils.toString(response.getEntity());

        JSONObject resData = new JSONObject(json);

        for (int i = 0; i < resData.getJSONArray("data").length(); i++) {
            JSONObject data = resData.getJSONArray("data").getJSONObject(i);

            bills.add(new Bill(data.getString("id"), data.getString("name"), data.getInt("value")));
        }

        cBills = new CustomAdapterBills(getContext(), 0, bills);
        listBills.setAdapter(cBills);
    }
}
