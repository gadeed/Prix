package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.StatementAdapter;
import com.flexural.developers.prixapp.model.Statements;
import com.github.barteksc.pdfviewer.PDFView;
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

public class MenuActivity extends AppCompatActivity  {

    public static final String SAMPLE_FILE = "sample.pdf";

    private String URL =  BASE_URL + "statementList.php";

    private TextView mSerialNumber, mTitle, mReferenceNumber;
    private LinearLayout mLayoutDevice, mLayoutTopUp, mLayoutTransfer, mLayoutHelpCenter, mLayoutPrivacy;
    private LinearLayout mLayoutStatement;
    private CardView mButtonAddFriend, mShareCode;
    private RelativeLayout mLayoutSales, mButtonTicket;
    private ImageView mButtonBack;
    private Button mButtonRequest;
    private PDFView mPdfView;
    private RecyclerView mRecyclerStatements;

    String pdfFileName;
    private StatementAdapter statementAdapter;
    private List<Statements> statementsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mSerialNumber = findViewById(R.id.serial_number);
        mLayoutDevice = findViewById(R.id.layout_device);
        mButtonAddFriend = findViewById(R.id.button_add_friend);
        mShareCode = findViewById(R.id.button_qr_code);
        mLayoutTopUp = findViewById(R.id.layout_topup);
        mLayoutTransfer = findViewById(R.id.layout_transfer);
        mLayoutSales = findViewById(R.id.layout_sales);

        mButtonBack = findViewById(R.id.button_back);
        mTitle = findViewById(R.id.title);
        mLayoutHelpCenter = findViewById(R.id.layout_help_center);
        mButtonTicket = findViewById(R.id.button_ticket);

        mButtonRequest = findViewById(R.id.button_request);
        mLayoutPrivacy = findViewById(R.id.layout_privacy);
        mReferenceNumber = findViewById(R.id.reference_number);
        mRecyclerStatements = findViewById(R.id.recycler_statements);
        mLayoutStatement = findViewById(R.id.layout_statement);
        mPdfView = findViewById(R.id.pdf_viewer);



        mRecyclerStatements.setLayoutManager(new LinearLayoutManager(this));

        statementsList = new ArrayList<>();
        statementAdapter = new StatementAdapter(this, statementsList);

        init();
        receiveIntent();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");
        String shopName = intent.getStringExtra("shopName");
        String acc_no = intent.getStringExtra("mid");

        if (menu.equals("device")) {
            mLayoutDevice.setVisibility(View.VISIBLE);
            String serialNumber = null;
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 9) {
                serialNumber = android.os.Build.SERIAL;
            }

            mSerialNumber.setText(serialNumber);
            mTitle.setText("Devices");

        } else if (menu.equals("topup")) {
            mLayoutTopUp.setVisibility(View.VISIBLE);

            mTitle.setText("Top Up");

            mReferenceNumber.setText(acc_no);

            mButtonRequest.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, RequestActivity.class);
                intent1.putExtra("mid", acc_no);
                startActivity(intent1);
            });


        } else if (menu.equals("transfer")) {
            mLayoutTransfer.setVisibility(View.VISIBLE);
            mTitle.setText("Transfers");

            mButtonAddFriend.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, AddFriendActivity.class);
                startActivity(intent1);
            });

            mShareCode.setOnClickListener(v -> {
                mTitle.setText("Share My Code");

                Intent sendIntent = new Intent(MenuActivity.this, ExtraActivity.class);
                sendIntent.putExtra("extra", "qr_code");
                sendIntent.putExtra("shopName", shopName);
                sendIntent.putExtra("mid", acc_no);
                startActivity(sendIntent);
            });

        } else if (menu.equals("help_center")) {
            mTitle.setText("Help Center");
            mLayoutHelpCenter.setVisibility(View.VISIBLE);

            mButtonTicket.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, ExtraActivity.class);
                intent1.putExtra("extra", "ticket");
                startActivity(intent1);
            });

        } else if (menu.equals("privacy")) {
            mLayoutPrivacy.setVisibility(View.VISIBLE);
            mTitle.setText("Terms and Conditions");

            mPdfView.fromAsset("files/terms_and_conditions.pdf")
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(1)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();


        } else if (menu.equals("statement")) {
            mLayoutStatement.setVisibility(View.VISIBLE);


        }
    }

    private void loadData(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        String mid = object.getString("mid");
                        String date_added = object.getString("date_added");
//                        String serial_no = object.getString("serial_no");
//                        String status = object.getString("status");
//                        String expired_date = object.getString("expired_date");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statementAdapter = new StatementAdapter(MenuActivity.this, statementsList);
                mRecyclerStatements.setAdapter(statementAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(MenuActivity.this).add(stringRequest);
    }

}