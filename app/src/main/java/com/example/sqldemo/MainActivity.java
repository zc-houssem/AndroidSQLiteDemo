package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_add,btn_view_all;
    EditText et_name,et_age,search;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        btn_view_all = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        search = findViewById(R.id.search);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customer_list);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomersOnListView("");

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1,et_name.getText().toString(),Integer.parseInt(et_age.getText().toString()),sw_activeCustomer.isChecked());
                    Toast.makeText(MainActivity.this,customerModel.toString(),Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    customerModel = new CustomerModel(-1,"error",0,false);
                    Toast.makeText(MainActivity.this,"Error creating customer",Toast.LENGTH_SHORT).show();
                }
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addOne(customerModel);
                ShowCustomersOnListView("");
                Toast.makeText(MainActivity.this,"Success = "+success,Toast.LENGTH_SHORT).show();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                ShowCustomersOnListView(s.toString());
            }
        });
        btn_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomersOnListView("");
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clicked = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.DeleteOne(clicked);
                ShowCustomersOnListView("");
                Toast.makeText(MainActivity.this,"Deleted " + clicked.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void ShowCustomersOnListView(String key) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this,android.R.layout.simple_list_item_1,dataBaseHelper.getAll(key));
        lv_customerList.setAdapter(customerArrayAdapter);
    }

}