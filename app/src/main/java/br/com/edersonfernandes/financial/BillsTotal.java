package br.com.edersonfernandes.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class BillsTotal extends Fragment {

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private Double billTotals = 0.0;
    private TextView txtBillsTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save) {
        View view = inflater.inflate(R.layout.fragment_bills_total, container, false);

        txtBillsTotal = (TextView) view.findViewById(R.id.txt_bills_total);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        getActivity().setTitle("Total Bills");

        try {
            findBills();
        } catch(IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void findBills() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://php-008-sofn-lar-and-p1-api.herokuapp.com/api/bill_pays");

        clientGet.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        HttpResponse response = httpClient.execute(clientGet);

        String json = EntityUtils.toString(response.getEntity());

        JSONObject resData = new JSONObject(json);

        for (int i = 0; i < resData.getJSONArray("data").length(); i++) {
            JSONObject data = resData.getJSONArray("data").getJSONObject(i);

            billTotals = billTotals + data.getDouble("value");

            if (txtBillsTotal != null) {
                txtBillsTotal.setText("R$ " + billTotals.toString());
            }
        }
    }
}
