package com.aapkatrade.buyer.search;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.dialogs.ChatDialogFragment;
import com.aapkatrade.buyer.dialogs.SearchLocationDialogFragment;
import com.aapkatrade.buyer.general.Utils.adapter.SearchAutoCompleteData;
import com.aapkatrade.buyer.general.Utils.adapter.Webservice_Search_AutoCompleteAdapterNew;
import com.aapkatrade.buyer.general.Utils.adapter.Webservice_search_autocompleteadapter;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.home.CommonAdapter;
import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.ShopListByCategoryActivity;
import com.aapkatrade.buyer.filter.entity.FilterObject;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.interfaces.Adapter_callback_interface;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.adapter.CustomAutocompleteAdapter;

import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class Search extends AppCompatActivity implements Adapter_callback_interface
{

    AutoCompleteTextView autocomplete_textview_state, autocomplete_textview_product;
    private ArrayList<SearchAutoCompleteData> searchAutoCompleteDatas = new ArrayList<>();
    CustomAutocompleteAdapter categoryadapter;
    Context c;
    GridLayoutManager gridLayoutManager;
    public static RecyclerView recyclerView_search, state_names_recycler, category_names_recycler;
    CommonAdapter commonAdapter;
    Spinner state_list_spinner;
    RecyclerView.LayoutManager mLayoutManager_state, mLayoutManager_category;
    common_state_search common_state_search;
    Adapter_callback_interface callback_interface;
    ArrayList<String> state_names = new ArrayList<>();
    ArrayList<String> SearchSuggestionList = new ArrayList<>();
    ArrayList<String> LocationList = new ArrayList<>();
    ArrayList<String> productidList = new ArrayList<>();
    ArrayList<String> categoriesList = new ArrayList<>();
    ArrayList<String> DistanceList = new ArrayList<>();
    ArrayList<CommonData> search_productlist = new ArrayList<>();
    ArrayList<common_category_search> common_category_searchlist = new ArrayList<>();
    ArrayList<common_state_search> common_state_searchlist = new ArrayList<>();
    ArrayList<common_city_search> common_city_searchlist = new ArrayList<>();
    Toolbar toolbar;
    Webservice_search_autocompleteadapter product_autocompleteadapter;
    Webservice_Search_AutoCompleteAdapterNew webservice_search_autoCompleteAdapterNew;
    ProgressBarHandler progressBarHandler;
    CoordinatorLayout coordinate_search;
    private Adapter_callback_interface callback_listener;
    private final int SPEECH_RECOGNITION_CODE = 1;
    private final int SearchSuggestioncode = 2;
    private ArrayList<String> stateList = new ArrayList<>();
    SearchResultsAdapter searchResultsAdapter;
    SearchcategoryAdapter searchResults_category_Adapter;
    SearchStateAdapter searchResults_state_Adapter, searchResults_city_Adapter;
    HashMap<String, String> webservice_header_type = new HashMap<>();
    String currentlocation_statename, latitude, longitude;
    int current_state_index;
    String class_name;
    private ArrayMap<String, ArrayList<FilterObject>> filterHashMap = null;
    String selected_categoryid;
    ViewPager viewpager_state;
    AppCompatImageView voice_search;
    private LinearLayoutManager linearLayoutManager;
    LinearLayout ll_search_container;

    public static CommonInterface commonInterface;

    int firsttime=0;

    TextView selectedlocation;

RelativeLayout rl_selected_state;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        c = Search.this;
        call_state_dialog(true);
        /*if(firsttime==0)
        {

            firsttime=1;

        }
        else{

        }*/

        Intent i = getIntent();
        currentlocation_statename = i.getStringExtra("state_name");

        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");

        Log.e("current_statename_", latitude);
        Log.e("current_latitude", currentlocation_statename);
        Log.e("current_longitude", longitude);

        class_name = i.getStringExtra("classname");
        Log.e("class_name", class_name);


        setuptoolbar();

        initview();



       // call_state_webservice(currentlocation_statename);

        commonInterface=new CommonInterface() {
            @Override
            public Object getData(Object object) {

      String selected_state=(String)object;
                selectedlocation.setText(selected_state);
                return null;
            }
        };

    }

    private void call_state_dialog(boolean b) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchLocationDialogFragment searchLocationDialogFragment = new SearchLocationDialogFragment(c,b);
        searchLocationDialogFragment.setCancelable(false);
        searchLocationDialogFragment.show(fragmentManager, "Select State");
    }


    private void setuptoolbar()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imgvew_home_icon = (ImageView) findViewById(R.id.imgvew_home_icon);
        imgvew_home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setElevation(0);
        //((ImageView) toolbar.findViewById(R.id.img_vew_location)).setColorFilter(ContextCompat.getColor(Search.this, R.color.white));

        // getSupportActionBar().setIcon(R.drawable.home_logo);

    }

    private void initview()
    {

        voice_search = (AppCompatImageView) findViewById(R.id.voice_input);
        ll_search_container= (LinearLayout) findViewById(R.id.ll_search_container);


        AndroidUtils.setGradientColor(ll_search_container, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(this, R.color.wallet_card2_gradient_bottom), ContextCompat.getColor(this, R.color.wallet_card2_gradient_Top), GradientDrawable.Orientation.LEFT_RIGHT, 0);

        voice_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        progressBarHandler = new ProgressBarHandler(Search.this);

        autocomplete_textview_product = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext_products);

        webservice_search_autoCompleteAdapterNew = new Webservice_Search_AutoCompleteAdapterNew(c,R.layout.activity_search,searchAutoCompleteDatas);

        autocomplete_textview_product.setAdapter(webservice_search_autoCompleteAdapterNew);

        autocomplete_textview_product.setThreshold(1);

        setup_state_spinner();

        setup_search_Recyclewview();

        coordinate_search = (CoordinatorLayout) findViewById(R.id.coordinate_search);

        autocomplete_textview_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                String text = s.toString();

                AndroidUtils.showErrorLog(getApplicationContext(),text);

                if (text.length() > 0)
                {
                   /* if (state_list_spinner.getSelectedItemPosition() != 0)
                    {*/
                        String product_search_url = (getResources().getString(R.string.webservice_base_url)) + "/search_suggesion";

                        call_search_suggest_webservice_product(product_search_url, text, selectedlocation.getText().toString());

                        autocomplete_textview_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                                Log.e("search_click_data", p.getItemAtPosition(pos).toString());
                            }
                        });

                    //}
                   /* else
                        {

                        AndroidUtils.showSnackBar(coordinate_search, "Please Select State First");
                    }*/

                }
                else
                {


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        autocomplete_textview_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    if (autocomplete_textview_product.getText().length() != 0)
                    {
                        call_search_webservice(selectedlocation.getText().toString(), autocomplete_textview_product.getText().toString());
                        autocomplete_textview_product.setHint("");
                        AppConfig.hideKeyboard(Search.this);

                    }
                    return true;
                }
                return false;


            }
        });


    }

    private void setup_search_Recyclewview()
    {
        recyclerView_search = (RecyclerView) findViewById(R.id.recycleview_search);
        linearLayoutManager = new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false);
        recyclerView_search.setLayoutManager(linearLayoutManager);
        commonAdapter = new CommonAdapter(Search.this, search_productlist, "search_list", "search_list_update");
        recyclerView_search.setAdapter(commonAdapter);

    }


    private void setup_state_spinner()
    {
        rl_selected_state=(RelativeLayout)findViewById(R.id.rl_selected_state);
        selectedlocation=(TextView)findViewById(R.id.selected_state);

        rl_selected_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_state_dialog(false);
            }


        });
      /*  selectedlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_state_dialog(false);
            }
        });*/
       /* state_list_spinner = (Spinner) findViewById(R.id.spin_select_state);
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(c, R.layout.white_textcolor_spinner, stateList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.white_textcolor_spinner);

        state_list_spinner.setAdapter(spinnerArrayAdapter);*/

    }


    private void call_state_webservice(String a)
    {
        progressBarHandler.show();
        Log.e("statelist_state", stateList.toString() + "" + c);

        for (int i = 0; i < stateList.size(); i++) {

            if (a.equals(stateList.get(i))) {

                current_state_index = i;
                Log.e("current_state_index", current_state_index + "");
            }
        }
        Log.e("current_state_index2", current_state_index + "");
        state_list_spinner.setSelection(current_state_index);
        progressBarHandler.hide();
    }

    private void call_search_webservice(String location_text, final String product_name1)
    {

        String search_url = (getResources().getString(R.string.webservice_base_url)) + "/search";
        progressBarHandler.show();

        Log.e("lat_search", latitude + "" + longitude);
        Ion.with(Search.this)
                .load(search_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("location", location_text)
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("name", product_name1)
                .setBodyParameter("lat", latitude)
                .setBodyParameter("long", longitude)
                .setBodyParameter("apply", "1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            progressBarHandler.hide();
                            set_webservice_data(result, "");

                            Log.e("call 3", result.toString());

                        } else {
                            progressBarHandler.hide();
                        }


//
                    }


                });


    }

    public void set_webservice_data(JsonObject result, String type)
    {

        Log.e("Arvind_data", result.toString());

        search_productlist.clear();

        JsonObject jsonObject = result.getAsJsonObject();

        String error = jsonObject.get("error").getAsString();
        String message = jsonObject.get("message").getAsString();
        if (error.contains("true")) {

            AndroidUtils.showSnackBar(coordinate_search, "No Suggesstion found");
            progressBarHandler.hide();

        } else {
            //  search_productlist=new ArrayList<>();

            Log.e("data2_search", result.toString());
            if (jsonObject.get("result") == null) {
                Log.e("data_jsonArray null", "NULLLLL");
            }


            JsonArray jsonarray_result = jsonObject.getAsJsonArray("result");

            JsonArray filterArray = result.getAsJsonArray("filter");
            if (filterArray != null) {
                loadFilterDataInHashMap(filterArray);
            }

            Log.e("data_jsonarray", jsonarray_result.toString());

            for (int l = 0; l < jsonarray_result.size(); l++)
            {

                JsonObject jsonObject_result = (JsonObject) jsonarray_result.get(l);
                String productname = jsonObject_result.get("name").getAsString();
                String productid = jsonObject_result.get("id").getAsString();
                String product_prize = jsonObject_result.get("price").getAsString();
                String imageurl = jsonObject_result.get("image_url").getAsString();
                String productlocation = jsonObject_result.get("city_name").getAsString() + "," + jsonObject_result.get("state_name").getAsString() + "," +
                        jsonObject_result.get("country_name").getAsString();

                String category_name = jsonObject_result.get("category_name").getAsString();

                String distance = jsonObject_result.get("distance").getAsString();

                String shop_location = jsonObject_result.get("state_name").getAsString() + "," + jsonObject_result.get("country_name").getAsString();

                search_productlist.add(new CommonData(productid, productname, product_prize, imageurl, productlocation,category_name,distance,shop_location));

              }

            commonAdapter = new CommonAdapter(Search.this, search_productlist, "search_list", "search_list");
            recyclerView_search.setAdapter(commonAdapter);
            //commonAdapter.notifyDataSetChanged();

            JsonArray jsonarray_category = jsonObject.getAsJsonArray("category");

            if (jsonarray_category != null) {
                for (int l = 0; l < jsonarray_category.size(); l++) {

                    JsonObject jsonObject_result = (JsonObject) jsonarray_category.get(l);
                    String cat_id = jsonObject_result.get("category_id").getAsString();
                    String catname = jsonObject_result.get("catname").getAsString();
                    String countprod = jsonObject_result.get("countprod").getAsString();

                    common_category_searchlist.add(new common_category_search(cat_id, catname, countprod));


                }
            }

            searchResults_category_Adapter = new SearchcategoryAdapter(Search.this, common_category_searchlist, selectedlocation.getText().toString(),
                    autocomplete_textview_product.getText().toString());
            Log.e("category_data", common_category_searchlist.toString());
            searchResults_category_Adapter.notifyDataSetChanged();


            JsonArray jsonarray_states = jsonObject.getAsJsonArray("states");
            if (jsonarray_states != null) {

                for (int l = 0; l < jsonarray_states.size(); l++) {

                    JsonObject jsonObject_result = (JsonObject) jsonarray_states.get(l);
                    String state_id = jsonObject_result.get("state_id").getAsString();
                    String statename = jsonObject_result.get("statename").getAsString();
                    String countprod = jsonObject_result.get("countprod").getAsString();
                    common_state_search = new common_state_search(state_id, statename, countprod);


                    common_state_searchlist.add(common_state_search);

                }

            }

            searchResults_state_Adapter = new SearchStateAdapter(Search.this, common_state_searchlist);

            Log.e("state_data", common_state_searchlist.toString());

            searchResults_state_Adapter.notifyDataSetChanged();

            JsonArray jsonarray_cities = jsonObject.getAsJsonArray("cities");

            if (jsonarray_cities != null)
            {
                for (int l = 0; l < jsonarray_cities.size(); l++)
                {

                    JsonObject jsonObject_result = (JsonObject) jsonarray_cities.get(l);
                    String city_id = jsonObject_result.get("city_id").getAsString();
                    String ctyname = jsonObject_result.get("ctyname").getAsString();
                    String countprod = jsonObject_result.get("countprod").getAsString();

                    common_city_searchlist.add(new common_city_search(city_id, ctyname, countprod));

                }

            }
            progressBarHandler.hide();
        }


    }

    private void call_search_suggest_webservice_product(String product_search_url, String product_search_text, String location_text)
    {
        final Context context = Search.this;

        Ion.with(Search.this)
                .load(product_search_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("name", product_search_text.trim())
                .setBodyParameter("lat", latitude)
                .setBodyParameter("long", longitude)
                .setBodyParameter("location", selectedlocation.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result != null)
                        {
                            System.out.println("result Search______" + result.toString());

                            AndroidUtils.showErrorLog(context, result.toString());

                            searchAutoCompleteDatas.clear();

                           /* SearchSuggestionList.clear();
                            productidList.clear();
                            categoriesList.clear();
                            LocationList.clear();*/

                            DistanceList = new ArrayList<String>();
                            JsonObject jsonObject = result.getAsJsonObject();

                            String error = jsonObject.get("error").getAsString();
                            if (error.contains("false")) {
                                String message = jsonObject.get("message").getAsString();
                                if (message.contains("Failed"))
                                {
                                    AndroidUtils.showSnackBar(coordinate_search, "No Suggesstion found");

                                }
                                else
                                    {

                                    Log.e("data2", result.toString());

                                    JsonArray jsonarray_result = jsonObject.getAsJsonArray("result");

                                    for (int l = 0; l < jsonarray_result.size(); l++)
                                    {

                                        JsonObject jsonObject_result = (JsonObject) jsonarray_result.get(l);

                                        String product_id = jsonObject_result.get("id").getAsString();
                                        String productname = jsonObject_result.get("name").getAsString();
                                        String distance = jsonObject_result.get("distance").getAsString();
                                        String category_name = jsonObject_result.get("category_name").getAsString();
                                        String shopLocation = jsonObject_result.get("state_name").getAsString() + "," + jsonObject_result.get("country_name").getAsString();

                                        searchAutoCompleteDatas.add(new SearchAutoCompleteData(product_id,productname,distance,category_name,shopLocation));
                                       /* SearchSuggestionList.add(productname);
                                        productidList.add(product_id);
                                        DistanceList.add(String.valueOf(distance));
                                        categoriesList.add(category_name);
                                        LocationList.add(shopLocation);*/

                                    }

                                }
                                if (error.contains("false"))
                                {
                                        // product_autocompleteadapter = new Webservice_search_autocompleteadapter(c, SearchSuggestionList, DistanceList,categoriesList,productidList,LocationList);
                                         autocomplete_textview_product.setAdapter(webservice_search_autoCompleteAdapterNew);

                                     //   AndroidUtils.showToast(context,"hi bhaiya");
                                    webservice_search_autoCompleteAdapterNew.notifyDataSetChanged();
                                }

                            }

                        }

                    }

                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (class_name.contains("homeactivity"))

                {
                    Intent goto_home = new Intent(Search.this, HomeActivity.class);
                    startActivity(goto_home);
                    finish();

                } else {
                    Intent goto_categorylist = new Intent(Search.this, ShopListByCategoryActivity.class);
                    startActivity(goto_categorylist);
                    finish();
                }


                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (class_name.contains("homeactivity"))
        {
            Intent goto_home = new Intent(Search.this, HomeActivity.class);
            startActivity(goto_home);
            finish();

        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void callback(String id, String type) {


    }

    private void loadFilterDataInHashMap(JsonArray filterArray) {
        filterHashMap = new ArrayMap<>();
        if (filterArray.size() > 0) {
            AndroidUtils.showErrorLog(Search.this, "size of filter Array is  :  " + filterArray.size());
            for (int i = 0; i < filterArray.size(); i++) {
                JsonObject filterObject = (JsonObject) filterArray.get(i);
                String filterName = filterObject.get("name").getAsString();
                JsonArray valueJsonArray = filterObject.get("values").getAsJsonArray();
                ArrayList<FilterObject> valueArrayList = new ArrayList<>();

                if (valueJsonArray != null) {

                    for (int j = 0; j < valueJsonArray.size(); j++) {
                        FilterObject filterObjectData = new FilterObject();
                        JsonObject filterValueObject = (JsonObject) valueJsonArray.get(j);
                        String[] filterValueObjectArray = filterValueObject.toString().replaceAll("\\{", "").replaceAll("\\}", "").trim().split(",");
                        AndroidUtils.showErrorLog(Search.this, "Length of filter value array is : ******" + filterValueObjectArray.length);

                        for (int k = 0; k < filterValueObjectArray.length; k++) {
                            AndroidUtils.showErrorLog(Search.this, "filterValueObjectArray[k]" + filterValueObjectArray[k]);
                            String key = filterValueObjectArray[k].split(":")[0].replaceAll("\"", "");
                            String value = filterValueObjectArray[k].split(":")[1].replaceAll("\"", "");
                            if (key.contains("id")) {
                                filterObjectData.id.key = key;
                                filterObjectData.id.value = value;
                            } else if (key.contains("name")) {
                                filterObjectData.name.key = key;
                                filterObjectData.name.value = value;
                            } else if (key.contains("count")) {
                                filterObjectData.count.key = key;
                                filterObjectData.count.value = value;
                            }
                        }

                        valueArrayList.add(filterObjectData);
                    }
                }
                filterHashMap.put(filterName, valueArrayList);
            }
        }
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            AndroidUtils.showToast(Search.this, "Sorry! Speech recognition is not supported in this device.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SPEECH_RECOGNITION_CODE:
             {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    autocomplete_textview_product.setText(text);
                }
                break;
            }
        }
    }


}









