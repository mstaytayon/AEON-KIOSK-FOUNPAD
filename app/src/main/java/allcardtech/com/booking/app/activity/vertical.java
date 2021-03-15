package allcardtech.com.booking.app.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.text.DecimalFormat;
import java.util.List;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.HoursOfPlayActivity;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.models.BranchRateModel;

public class vertical extends AppCompatActivity {

    //private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
       // linearLayout=findViewById(R.id.ll);
        int width=getScreenWidth(this);
        populateRates();
//        int childCount=linearLayout.getChildCount();
//        for (int i=0;i<childCount;i++){
//            Button button= (Button) linearLayout.getChildAt(i);
//            button.setWidth(width);
//        }
    }
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void populateRates() {

        List<BranchRateModel> rates = new BranchRateDao(vertical.this).getBranchRatesList();

        LinearLayout table =  findViewById(R.id.tableForButtons);


        for (int i = 0; i < rates.size(); i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);

            final Button button = new Button(this);

            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);

            tableRowParams.setMargins(0, 0, 0, 10);
            tableRow.setLayoutParams(tableRowParams);

            button.setText(rates.get(i).getDescription() +"\n PHP "+  new DecimalFormat("#,###.00").format(rates.get(i).getRate()));

            button.setTextSize(30);
            button.setPadding(20, 20, 20, 20);
            button.setGravity(Gravity.CENTER);
            button.setTypeface(null, Typeface.BOLD);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rounded_menu_red));
            tableRow.addView(button);




        }
    }



}

