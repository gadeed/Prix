package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.AirtimeAdapter;
import com.flexural.developers.prixapp.adapters.SalesAdapter;
import com.flexural.developers.prixapp.model.Airtime;
import com.flexural.developers.prixapp.model.Sales;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class SalesActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, OnChartValueSelectedListener {
    
    private PieChart chart;
    private ImageView mButtonBack;
    private TextView mAmount;

    private List<Sales> salesList;
    protected Typeface tfRegular, tfLight;
    private int numNetworkAirtime, numGaming, numRemitMoney, numPrixAirtime, numElectricity, numDataBundles;

    private String URL =  BASE_URL + "merchantSales.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        
        chart = findViewById(R.id.chart1);
        mButtonBack = findViewById(R.id.button_back);
        mAmount = findViewById(R.id.amount);

        salesList = new ArrayList<>();

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        numNetworkAirtime = numGaming = numDataBundles = numElectricity = numPrixAirtime = numRemitMoney = 0;

        init();
        receiveIntent();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }

    private void receiveIntent() {
        String shopName = getIntent().getStringExtra("shopName");

        loadData(shopName);
        loadChart(shopName);

    }

    private void loadChart(String shopName) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfRegular);
        chart.setCenterText(shopName + " SALES");

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);
    }

    private void loadData(String shopName) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        String price = object.getString("price");
                        String productType = object.getString("ProductType");
                        String productName = object.getString("ProductName");
                        String quantity = object.getString("qty");
                        String merchant = object.getString("merchant");
                        String totalPrice = object.getString("total");
                        String percent = object.getString("percent");

                        if (merchant.equals(shopName)) {

                            if (productType.equals("Network Airtime")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numNetworkAirtime = salesList.size();


                            }

                            if (productType.equals("Gaming")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numGaming = salesList.size();


                            }

                            if (productType.equals("Prix Airtime")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numPrixAirtime = salesList.size();

                                salesList.clear();

                            }

                            if (productType.equals("Electricity")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numElectricity = salesList.size();

                                salesList.clear();

                            }

                            if (productType.equals("Data Bundles")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numDataBundles = salesList.size();

                                salesList.clear();

                            }

                            if (productType.equals("Remit Money")) {
                                Sales sales = new Sales(productType, productName, quantity, price, totalPrice, percent, merchant);
                                salesList.add(sales);

                                numRemitMoney = salesList.size();

                                salesList.clear();

                            }

                            ArrayList<PieEntry> entries = new ArrayList<>();

                            if (numNetworkAirtime != 0) {
                                entries.add(new PieEntry(Float.parseFloat(String.valueOf(numNetworkAirtime)), "Network Airtime"));

                            }

                            if (numGaming != 0) {
                                entries.add(new PieEntry(Integer.parseInt(String.valueOf(numGaming)), "Gaming"));

                            }

                            if (numRemitMoney != 0) {
                                entries.add(new PieEntry(Integer.parseInt(String.valueOf(numRemitMoney)), "Remit Money"));

                            }

                            if (numPrixAirtime != 0) {
                                entries.add(new PieEntry(Integer.parseInt(String.valueOf(numPrixAirtime)), "Prix Airtime"));

                            }

                            if (numElectricity != 0) {
                                entries.add(new PieEntry(Integer.parseInt(String.valueOf(numElectricity)), "Electricity"));

                            }

                            if (numDataBundles != 0) {
                                entries.add(new PieEntry(Integer.parseInt(String.valueOf(numDataBundles)), "Data Bundles"));

                            }

                            mAmount.setText("Network: " + numNetworkAirtime + " Gaming: " + numGaming);

                            PieDataSet dataSet = new PieDataSet(entries, shopName + " SALES");

                            dataSet.setDrawIcons(false);

                            dataSet.setSliceSpace(3f);
                            dataSet.setIconsOffset(new MPPointF(0, 40));
                            dataSet.setSelectionShift(5f);

                            // add a lot of colors

                            ArrayList<Integer> colors = new ArrayList<>();

                            for (int c : ColorTemplate.COLORFUL_COLORS)
                                colors.add(c);
//
//                            for (int c : ColorTemplate.JOYFUL_COLORS)
//                                colors.add(c);
//
//                            for (int c : ColorTemplate.COLORFUL_COLORS)
//                                colors.add(c);
//
//                            for (int c : ColorTemplate.LIBERTY_COLORS)
//                                colors.add(c);
//
//                            for (int c : ColorTemplate.PASTEL_COLORS)
//                                colors.add(c);

                            colors.add(ColorTemplate.getHoloBlue());

                            dataSet.setColors(colors);
                            //dataSet.setSelectionShift(0f);

                            PieData data = new PieData(dataSet);
                            data.setValueFormatter(new PercentFormatter());
                            data.setValueTextSize(11f);
                            data.setValueTextColor(Color.WHITE);
                            data.setValueTypeface(tfLight);
                            chart.setData(data);

                            // undo all highlights
                            chart.highlightValues(null);

                            chart.invalidate();

                        }

//                        salesList.clear();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SalesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(SalesActivity.this).add(stringRequest);
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }
}

