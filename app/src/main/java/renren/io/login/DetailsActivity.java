package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class DetailsActivity extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textView = (TextView) findViewById(R.id.tv1);

        System.out.println(getIntent().getStringExtra("1"));
        textView.setText(getIntent().getStringExtra("1"));
    }
}
