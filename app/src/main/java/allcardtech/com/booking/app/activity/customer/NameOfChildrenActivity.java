package allcardtech.com.booking.app.activity.customer;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Attributes;

import allcardtech.com.booking.R;

public class NameOfChildrenActivity extends AppCompatActivity{

    private Button btnBack,btnNext;
    private int ChildrenCount;
    private LinearLayout linearListOfChildren;

    static ArrayList<String> ListOfChildren = new ArrayList<>();
    final ArrayList<EditText> allEditText = new ArrayList();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_of_children);


        linearListOfChildren = findViewById(R.id.LinearListOfChildren);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        RelativeLayout rl = findViewById(R.id.relativeLayout);
        for (int i = 0; i < 5; i++) {
           final Button button = new Button(this);
            button.setText("90 MINUTES\n PHP 100" + i);
            button.setTextSize(30);
            button.setId(1000 + i);
            button.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);

            int width = 700;
            int height = 400;

            RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rlp2.addRule(RelativeLayout.BELOW, button.getId() - Integer.parseInt("1"));

            RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(
                    width, height);
            rlp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rlp1.addRule(RelativeLayout.BELOW, button.getId() - Integer.parseInt("1"));
            rlp1.setMargins(0,20,0,0);
            rlp1.setMargins(0,20,0,0);
            button.setBackgroundResource(R.drawable.button_rounded_menu_red);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSelectedRate(button.getText().toString());
                }
            });
            button.setLayoutParams(rlp1);
            rl.addView(button);
        }

        ListOfChildren.clear();
        allEditText.clear();

        ChildrenCount = NumberOfGuestActivity.getChildrenCount();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] elements = new String[allEditText.size()] ;
                for(int i=0; i < allEditText.size(); i++){
                    elements[i] = allEditText.get(i).getText().toString();
                }

                Intent intent = new Intent(NameOfChildrenActivity.this, NameOfAdultActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });
    }

    private void getSelectedRate(String col) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        TextView tv =new TextView(NameOfChildrenActivity.this);
        tv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        tv.setBackgroundColor(Color.GRAY);
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setBackground(getDrawable(R.drawable.toastpinckchar));
        tv.getShadowRadius();
        tv.setText("      Please input valid email address");
        tv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        toast.setView(tv);
        toast.show();
    }



    public static ArrayList<String> getListOfChildren(){
        return ListOfChildren;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(NameOfAdultActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


//        int count = 0;
//        for (int j = 0; j < 5; j++) {
//            ++ count;
//            EditText et = new EditText(this);
//            et.setTextSize(getResources().getDimension(R.dimen.txtSize));
//            et.setBackgroundResource(R.drawable.edittextwhite);
//            allEditText.add(et);
//            android.app.ActionBar.LayoutParams lp = new android.app.ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
//            et.setGravity(Gravity.CENTER);
//            et.setHint("Child " + count);
//            lp.setMargins(0,0,0,getResources().getDimensionPixelOffset(R.dimen.marginTop));
//            linearListOfChildren.addView(et,lp);
//        }