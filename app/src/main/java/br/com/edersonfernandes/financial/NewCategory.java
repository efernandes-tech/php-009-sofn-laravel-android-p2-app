package br.com.edersonfernandes.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NewCategory extends Fragment implements View.OnClickListener {

    private Button btnCreateCategory;
    private AutoCompleteTextView mCategoryName;
    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save) {
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);

        btnCreateCategory = (Button) view.findViewById(R.id.btn_create_category);
        btnCreateCategory.setOnClickListener(this);

        mCategoryName = (AutoCompleteTextView)view.findViewById(R.id.category_name);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        getActivity().setTitle("New Category");
    }

    @Override
    public void onClick(View view) {
        HttpPost clientPost = new HttpPost("http://php-008-sofn-lar-and-p1-api.herokuapp.com/api/categories");

        JSONObject requestBody = new JSONObject();

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());
        StringEntity data = null;

        try {
            requestBody.put("name", mCategoryName.getText());
            data = new StringEntity(requestBody.toString());
            clientPost.setEntity(data);

            HttpResponse response = httpClient.execute(clientPost);
            String json = EntityUtils.toString(response.getEntity());

            JSONObject result = new JSONObject(json);

            getFragmentManager().popBackStackImmediate();
        } catch (JSONException | UnsupportedEncodingException  e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
