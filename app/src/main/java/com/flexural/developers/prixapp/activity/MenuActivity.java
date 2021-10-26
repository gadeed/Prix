package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flexural.developers.prixapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
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

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    public static final String SAMPLE_FILE = "sample.pdf";

    protected final String[] parties = new String[] {
            "Prix Airtime", "Network Airtime", "Data Bundles", "Electricity", "Payment", "Remit Money"
    };

    private TextView mSerialNumber;
    private LinearLayout mLayoutDevice, mLayoutTopUp, mLayoutTransfer;
    private PDFView mPdfView;
    private CardView mButtonAddFriend, mShareCode;
    private RelativeLayout mLayoutSales;
    private PieChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    String pdfFileName;
    protected Typeface tfRegular, tfLight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mSerialNumber = findViewById(R.id.serial_number);
        mLayoutDevice = findViewById(R.id.layout_device);
        mPdfView = findViewById(R.id.pdfView);
        mButtonAddFriend = findViewById(R.id.button_add_friend);
        mShareCode = findViewById(R.id.button_qr_code);
        mLayoutTopUp = findViewById(R.id.layout_topup);
        mLayoutTransfer = findViewById(R.id.layout_transfer);
        mLayoutSales = findViewById(R.id.layout_sales);

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);
        seekBarX = findViewById(R.id.seekBar1);
        seekBarY = findViewById(R.id.seekBar2);
        chart = findViewById(R.id.chart1);

        seekBarX.setOnSeekBarChangeListener(this);
        seekBarY.setOnSeekBarChangeListener(this);

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");


        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");

        if (menu.equals("device")) {
            mLayoutDevice.setVisibility(View.VISIBLE);
            String serialNumber = null;
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 9) {
                serialNumber = android.os.Build.SERIAL;
            }

            mSerialNumber.setText(serialNumber);

        } else if (menu.equals("topup")) {
            mLayoutTopUp.setVisibility(View.VISIBLE);
            displayFromAsset(SAMPLE_FILE);

        } else if (menu.equals("transfer")) {
            mLayoutTransfer.setVisibility(View.VISIBLE);
            mButtonAddFriend.setOnClickListener(v -> {
                startActivity(new Intent(this, ExtraActivity.class));
            });

            mShareCode.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a QR Code to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            });

        } else if (menu.equals("sales")) {
            mLayoutSales.setVisibility(View.VISIBLE);

            chart.setUsePercentValues(true);
            chart.getDescription().setEnabled(false);
            chart.setExtraOffsets(5, 10, 5, 5);

            chart.setDragDecelerationFrictionCoef(0.95f);

            chart.setCenterTextTypeface(tfLight);
            chart.setCenterText(generateCenterSpannableText());

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

            seekBarX.setProgress(6);
            seekBarY.setProgress(10);

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
    }

    private void setData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length],
                    getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "SHOP ABC SALES");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

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

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        mPdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
//                .pageFitPolicy(FitPolicy.BOTH)
                .load();
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

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("SALES");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 5, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress(), seekBarY.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
        Log.i("PieChart", "nothing selected");

    }
}