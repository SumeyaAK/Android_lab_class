package com.example.android_lab_class;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView fxt;
    EditText name_edit1, email_edit, department_edit;
    ImageView profileImageView;
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        fxt = findViewById(R.id.fname);
        name_edit1 = findViewById(R.id.fname_edit);
        email_edit = findViewById(R.id.email_edit);
        department_edit = findViewById(R.id.department_edit);
        profileImageView = findViewById(R.id.profile_image);
        bt1 = findViewById(R.id.displaybt);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_edit1.getText().toString().trim();

                if (name.isEmpty()) {
                    name_edit1.setError("Please enter a name");
                    return;
                }

                String message = "Hello, " + name;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
