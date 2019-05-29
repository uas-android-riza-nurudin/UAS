package com.wp.kosku.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wp.kosku.app.model.Kos;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityCRUDCreate extends AppCompatActivity {

    private DatabaseReference database;

    private Button btSubmit;
    private EditText etNama;
    private EditText etAlamat;
    private EditText etNoHp;
    private EditText etLamaSewa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_create);
//tampilan kotak kotak data dan list nama
        etNama = (EditText) findViewById(R.id.et_nama);
        etAlamat = (EditText) findViewById(R.id.et_alamat);
        etNoHp = (EditText) findViewById(R.id.et_nohp);
        etLamaSewa = (EditText) findViewById(R.id.et_lamasewa);
        btSubmit = (Button) findViewById(R.id.bt_submit);

        database = FirebaseDatabase.getInstance().getReference();

        final Kos sewa = (Kos) getIntent().getSerializableExtra("data");

        if (sewa != null) {
            etNama.setText(sewa.getNama());
            etAlamat.setText(sewa.getAlamat());
            etNoHp.setText(sewa.getNoHp());
            etLamaSewa.setText(sewa.getLamaSewa());

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sewa.setNama(etNama.getText().toString());
                    sewa.setAlamat(etAlamat.getText().toString());
                    sewa.setNoHp(etNoHp.getText().toString());
                    sewa.setLamaSewa(etLamaSewa.getText().toString());

                    updateKos(sewa);
                }
            });
        } else {
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isEmpty(etNama.getText().toString()) && !isEmpty(etAlamat.getText().toString()) && !isEmpty(etNoHp.getText().toString()) && !isEmpty(etLamaSewa.getText().toString()))
                        submitKos(new Kos(etNama.getText().toString(), etAlamat.getText().toString(), etNoHp.getText().toString(), etLamaSewa.getText().toString()));
                    else
                        Snackbar.make(findViewById(R.id.bt_submit), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            etNama.getWindowToken(), 0);
                }
            });
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void updateKos(Kos sewa) {

        database.child("sewa")
                .child(sewa.getKey())
                .setValue(sewa)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }

    private void submitKos(Kos sewa) {

        database.child("sewa").push().setValue(sewa).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNama.setText("");
                etAlamat.setText("");
                etNoHp.setText("");
                etLamaSewa.setText("");
                Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity) {

        return new Intent(activity, ActivityCRUDCreate.class);
    }
}
