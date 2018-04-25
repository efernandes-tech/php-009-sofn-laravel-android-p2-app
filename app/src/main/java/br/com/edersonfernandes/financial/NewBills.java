package br.com.edersonfernandes.financial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NewBills extends Fragment implements View.OnClickListener {

    private Button btnCreateBills;
    private AutoCompleteTextView mBillsName;
    private AutoCompleteTextView mBillsValue;
    private Spinner sCategory;
    private ArrayList<Category> categoriesList = new ArrayList<Category>();
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private View view;
    private Category selectedCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save) {
        view = inflater.inflate(R.layout.fragment_new_bills, container, false);

        btnCreateBills = (Button) view.findViewById(R.id.btn_create_bills);
        btnCreateBills.setOnClickListener(this);

        mBillsName = (AutoCompleteTextView)view.findViewById(R.id.bills_name);
        mBillsValue = (AutoCompleteTextView)view.findViewById(R.id.bills_value);

        buildCategorySpinner();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        getActivity().setTitle("New Category");
    }

    public void buildCategorySpinner() {
        GatCategoryAsync taskAsync = new GatCategoryAsync(UserSession.getInstance(getContext()).getUserToken());

        try {
            categoriesList = taskAsync.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sCategory = (Spinner) view.findViewById(R.id.select_category);
        CategoryAdapter cAdapter = new CategoryAdapter(getActivity(), R.id.select_category, categoriesList);
        sCategory.setAdapter(cAdapter);

        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoriesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }

    @Override
    public void onClick(View view) {
        HttpPost clientPost = new HttpPost("http://php-008-sofn-lar-and-p1-api.herokuapp.com/api/bill_pays");

        JSONObject requestBody = new JSONObject();

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());
        StringEntity data = null;

        try {
            requestBody.put("name", mBillsName.getText());
            requestBody.put("value", Integer.parseInt(mBillsValue.getText().toString()));
            requestBody.put("category_id", Integer.parseInt(selectedCategory.getId().toString()));
            requestBody.put("date_due", new Date());
            data = new StringEntity(requestBody.toString());
            clientPost.setEntity(data);

            HttpResponse response = httpClient.execute(clientPost);
            String json = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();

            JSONObject result = new JSONObject(json);

            if (statusCode == 201) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | UnsupportedEncodingException  e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GatCategoryAsync extends AsyncTask<Void, Void, ArrayList> {
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private String token;

    public GatCategoryAsync(String token) {
        this.token = token;
    }

    @Override
    public ArrayList<Category> doInBackground(Void... params) {
        HttpGet clientGet = new HttpGet("http://php-008-sofn-lar-and-p1-api.herokuapp.com/api/categories");

        clientGet.addHeader("Authorization", "Bearer " + token);

        HttpResponse response = null;
        String json = null;
        try {
            response = httpClient.execute(clientGet);
            json = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject resData = null;
        try {
            resData = new JSONObject(json);

            for (int i = 0; i < resData.getJSONArray("data").length(); i++) {
                JSONObject data = resData.getJSONArray("data").getJSONObject(i);

                categories.add(new Category(data.getString("id"), data.getString("name")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }
}