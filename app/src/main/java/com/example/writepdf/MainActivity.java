package com.example.writepdf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNim;
    EditText txtNama;
    Button btnSimpan;

    private static final int STORAGE_CODE = 1000;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNim = findViewById(R.id.textNim);
        txtNama = findViewById(R.id.textNama);
        btnSimpan = findViewById(R.id.simpan);
        btnSimpan.setOnClickListener(this);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savePdf() {

        PdfDocument dokumen = new PdfDocument();
        PdfDocument.PageInfo page = new PdfDocument.
                PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page screen = dokumen.startPage(page);

        Paint desain = new Paint();
        String inputNim = txtNim.getText().toString();
        String inputNama = txtNama.getText().toString();
        int x = 10, y = 25;

        for (String line : inputNim.split ("\n")) {
            screen.getCanvas().drawText(line, x, y, desain);
            y += desain.descent() - desain.ascent();
        }

        for (String line : inputNama.split ("\n")) {
            screen.getCanvas().drawText(line, x, y, desain);
            y += desain.descent() - desain.ascent();
        }

        dokumen.finishPage(screen);

        String FilePath = Environment.getExternalStorageDirectory().getPath() + "/"+inputNim+inputNama+".pdf";
        File filed = new File(FilePath);
        try {
            dokumen.writeTo(new FileOutputStream(filed));
            Toast.makeText(this, "Sukses ", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Log.d("response", ""+e.getMessage());
            Toast.makeText(this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        dokumen.close();
    }

    @Override
    public void onClick(View view) {
        if (view == btnSimpan){
            savePdf();
        }
    }
}
