package com.huawei.codelabkit.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.huawei.codelabkit.R;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SiteActivity extends AppCompatActivity {

    private static final String TAG = "SiteActivity";
    private SearchService searchService;
    TextView resultTextView;
    EditText queryInput;
    String apiKey = "your_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_site);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());


        String encodedurl = null;
        try {
            encodedurl = URLEncoder.encode(apiKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        searchService = SearchServiceFactory.create(this, encodedurl);

        queryInput = findViewById(R.id.edit_text_text_search_query);
        resultTextView = findViewById(R.id.response_text_search);

    }

    public void search(View view) {

        TextSearchRequest textSearchRequest = new TextSearchRequest();
        textSearchRequest.setQuery(queryInput.getText().toString());
        searchService.textSearch(textSearchRequest, new SearchResultListener<TextSearchResponse>() {
            @Override
            public void onSearchResult(TextSearchResponse textSearchResponse) {

                StringBuilder response = new StringBuilder("\n");
                response.append("success\n");
                int count = 1;
                AddressDetail addressDetail;
                for (Site site : textSearchResponse.getSites()) {
                    addressDetail = site.getAddress();
                    response.append(String.format(
                            "[%s]  name: %s, formatAddress: %s, country: %s, countryCode: %s \r\n",
                            "" + (count++), site.getName(), site.getFormatAddress(),
                            (addressDetail == null ? "" : addressDetail.getCountry()),
                            (addressDetail == null ? "" : addressDetail.getCountryCode())));
                }
                Log.d(TAG, "search result is : " + response);
                resultTextView.setText(response.toString());
            }

            @Override
            public void onSearchError(SearchStatus searchStatus) {
                Log.e(TAG, "onSearchError is: " + searchStatus.getErrorCode());
            }
        });
    }


}
