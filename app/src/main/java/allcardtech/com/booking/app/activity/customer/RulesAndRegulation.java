package allcardtech.com.booking.app.activity.customer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;


public class RulesAndRegulation extends AppCompatActivity {

    private LinearLayout LinearBack, LinearNext;
    private TextView txtRules,lblTitle;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_and_regulation);

        txtRules = findViewById(R.id.txtRules);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        LinearBack = findViewById(R.id.LinearBack);
        LinearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearNext = findViewById(R.id.LinearNext);
        LinearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RulesAndRegulation.this, BookingSummaryActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });

        Toast toast = Toast.makeText(RulesAndRegulation.this,"SCROLL UP TO ACCEPT RULES AND REGULATION", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        txtRules.setText("1. All children within the the premises must be accompanied by a Guardian, i.e., a parent or a companion who is eighteen (18)years old or above.\n\n" +
                "2. The Guardian must supervise the child and is responsible for the child's behaviour at all times.\n\n" +
                "3. The Guardian must ensure that the child complies with Kidzooona's " + "Rules and Regulation" + ".\n\n"+
                "4. Running inside the play area must be avoided at all times, as this may lead to injury.\n\n" +
                "5. Guardians are requested to immediately take action to end any violent behaviour of the children that may lead to their injury or injure other children in the play area.\n\n" +
                "6. To prevent injuries and         damage to clothing, sharp hair accessories should be removed before admission into the play area.\n\n" +
                "7. Personal items should be stored in the locker which are provided, free of charge. Please note that any loss or damage to the locker and the locker key shall be charged to the customer. Deposit of valuables inside the lockers are totally at the risk of the user of the locker.\n\n" +
                "8. Should you need to temporarily leave the play area, please observe the play area's facility staff.\n\n" +
                "9. Guardians are requested to follow all instructions and notices from the play area's facility staff.\n\n" +
                "10. Children above twelve (12) years old are now allowed to enter the play area.\n\n" +
                "11. For our customer's safety, children who are suffering from flu, cough, cold, skin diseases or are afflicted by other injuries and infectious disease are requested to postpone their visit to our play area, and in the exercise of prudence, Kidzooona may also refuse admission for this reason.\n\n" +
                "12. Guardians are requested to refrain the actions that might inconvenience other customer such as loud and improper language, or sleeping in the waiting area.\n\n" +
                "13. Eating and drinking is not allowed in the play area and must be done only in the designated areas.\n\n" +
                "14. Strollers are allowed inside the play area. For the customer's convenience, a stroller storage is provided.\n\n" +
                "15. Socks are required inside tha playground\n\n" +
                "16. Customer are requested to help maintain the cleanliness of the play area. Kindly refrain from leaving trash or spitting within the play area. Customer's may use the mall's restroom, if necessary.\n\n" +
                "17. Toys and other equipment in the play area are for use within the play area only and may not be taken out of the play area.\n\n" +
                "18. Kidzooona and its employees shall not be liable for any accidents, injuries or losses that take place inside the facility. To maintain the security of the facility, the store staff may ask the customer to leave the premises for non-cooperation.\n\n" +
                "19. All lost and found items will be dated and stored in out Lost and Found box and can be claimed within a period of 6 months. All unclaimed items will be sent to our head office to sell on a flea market. The sale proceeds will go to our charity fund.\n\n" +
                "20. All personal data and information are protected under the terms of the " + "Privacy and Data Protection Act." + " Aeon Fantasy Group Philippines Inc. may use these data to inform customer about additional benefits, news and promotions. The data collected will not be disclosed to third parties, unless it is legally required.\n\n"+
                "21. While Aeon Fantasy Philippines exercise due diligence to keep the premises of its facilities to secure, everyone is enjoined to take care of their personal belongings while within the facility Aeon Fantasy Philippines shall not have any liability to the customer for any loss, damage, cost or expenses which the customer suffers or incurs arsing from customer's use of its facilities unless the same is caused by gross negligence or willful misconduct of Aeon Fantasy Philippine's employees or suppliers.\n\n\n\n\n\n"+
                "I ACCEPT AND AGREE ON THE " + "RULES & REGULATION" +" STATED ABOVE."
        );
    }
}
