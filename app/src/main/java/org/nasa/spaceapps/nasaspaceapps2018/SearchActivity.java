package org.nasa.spaceapps.nasaspaceapps2018;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

public class SearchActivity extends AppCompatActivity {
    private EditText editText;
    private ImageView noSearch;
    private TextView noSearchTv;
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bgs));
        }
        setContentView(R.layout.activity_search);
        this.editText = (EditText)findViewById(R.id.search);
        this.noSearch = (ImageView)findViewById(R.id.search_icon);
        this.noSearchTv = (TextView)findViewById(R.id.nos);
        this.recyclerView = (RecyclerView)findViewById(R.id.search_res);
        this.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        String ser = this.editText.getText().toString();
        try {
            this.feedAdapter = new FeedAdapter(null,getApplicationContext(),4,ser);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.recyclerView.setAdapter(this.feedAdapter);
        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start == 0){
                    SearchActivity.this.recyclerView.setVisibility(View.VISIBLE);
                    SearchActivity.this.noSearchTv.setVisibility(View.GONE);
                    SearchActivity.this.noSearch.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchActivity.this.feedAdapter.updateSearchList(s.toString());
                if(s.length()==0){
                    SearchActivity.this.recyclerView.setVisibility(View.GONE);
                    SearchActivity.this.noSearchTv.setVisibility(View.VISIBLE);
                    SearchActivity.this.noSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
