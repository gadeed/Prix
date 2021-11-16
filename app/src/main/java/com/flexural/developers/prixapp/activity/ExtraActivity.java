package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ExtraActivity extends AppCompatActivity {

    private ImageView mButtonBack, mQrCode;
    private LinearLayout mLayoutTicket, mLayoutQrCode;
    private RelativeLayout mButtonTicket;
    private TextView mShopName, mShopCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        mButtonBack = findViewById(R.id.button_back);
        mLayoutTicket = findViewById(R.id.layout_ticket);
        mButtonTicket = findViewById(R.id.button_ticket);
        mLayoutQrCode = findViewById(R.id.layout_qr_code);
        mQrCode = findViewById(R.id.qr_code);
        mShopName = findViewById(R.id.shop_name);
        mShopCode = findViewById(R.id.shop_code);

        init();
        try {
            receiveIntent();
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void receiveIntent() throws WriterException {
        Intent intent = getIntent();
        String extra = intent.getStringExtra("extra");
        String shopName = intent.getStringExtra("shopName");
        String acc_no = intent.getStringExtra("mid");

        if (extra.equals("ticket")) {
            mLayoutTicket.setVisibility(View.VISIBLE);

            mButtonTicket.setOnClickListener(v -> {
                Snackbar.make(findViewById(android.R.id.content), "Not connected to mail server", Snackbar.LENGTH_SHORT).show();
            });

        } else if (extra.equals("qr_code")) {
            mLayoutQrCode.setVisibility(View.VISIBLE);

            mShopName.setText(shopName);
            mShopCode.setText(acc_no);

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;
            
            // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
            QRGEncoder qrgEncoder = new QRGEncoder(shopName, null, QRGContents.Type.TEXT, smallerDimension);
            qrgEncoder.setColorBlack(Color.WHITE);
            qrgEncoder.setColorWhite(Color.BLACK);
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            mQrCode.setImageBitmap(bitmap);

        }
    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }
}