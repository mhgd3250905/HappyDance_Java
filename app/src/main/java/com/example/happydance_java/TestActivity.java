package com.example.happydance_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    private TextView tvTest;
    private String testStr="AAAAAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvTest=findViewById(R.id.tv_test);

        final TestViewModel testViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TestViewModel.class);

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testViewModel.getData().setValue(testStr);
            }
        });
        tvTest.setText(testViewModel.getData().getValue());
    }

    public class TestViewModel extends ViewModel {
        private MutableLiveData<String> data;

        public MutableLiveData<String> getData() {
            if (data==null){
                data=new MutableLiveData<>();
                data.setValue("------");
            }
            return data;
        }
    }
}
