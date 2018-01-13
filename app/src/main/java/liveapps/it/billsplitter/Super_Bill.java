package liveapps.it.billsplitter;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Super_Bill extends AppCompatActivity {

    String TAG = "Super_Bill";

    private FirebaseAnalytics mFirebaseAnalytics;

    ScrollView content;

    TextView total, total_tip, total_person, total_tip_person;
    EditText subtotal, tip, persons;

    boolean calculated = false;

    FloatingActionButton fab;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_super_bill);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setupGraphic();

    }

    void setupGraphic(){

        content = findViewById(R.id.scroll);
        fab = findViewById(R.id.fab);
        total = findViewById(R.id.label_total_result);
        total_tip = findViewById(R.id.label_tip_total_result);
        total_person = findViewById(R.id.label_total_person_result);
        total_tip_person = findViewById(R.id.label_tip_person_result);

        subtotal = findViewById(R.id.editText_price);
        tip = findViewById(R.id.editText_tip);
        persons = findViewById(R.id.editText_persons);

        subtotal.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                fab.setImageResource(R.mipmap.ic_equal_white_48dp);
                calculated = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        tip.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                fab.setImageResource(R.mipmap.ic_equal_white_48dp);
                calculated = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        persons.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                fab.setImageResource(R.mipmap.ic_equal_white_48dp);
                calculated = false;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        setGraphic();

    }

    void setGraphic(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!calculated) {
                    if (subtotal.getText() == null || subtotal.getText().toString().length() == 0 || subtotal.getText().toString().equals("") || Integer.parseInt(subtotal.getText().toString()) == 0 ||
                            persons.getText() == null || persons.getText().toString().length() == 0 || persons.getText().toString().equals("") || Integer.parseInt(subtotal.getText().toString()) == 0) {
                        showAToast(getResources().getString(R.string.super_bill_no_fill));

                        fab.setImageResource(R.mipmap.ic_equal_white_48dp);
                        calculated = false;
                    } else {
                        fab.setImageResource(R.mipmap.ic_settings_backup_restore_white_48dp);

                        calcResults();

                    }
                }
                else{
                    subtotal.setText("");
                    tip.setText("");
                    persons.setText("");

                    total.setText("");
                    total_person.setText("");
                    total_tip.setText("");
                    total_tip_person.setText("");

                    fab.setImageResource(R.mipmap.ic_equal_white_48dp);
                    calculated = false;
                }
            }
        });

    }

    void calcResults(){

        try {
            double d_subtotal = Double.parseDouble(subtotal.getText().toString());
            int i_persons = Integer.parseInt(persons.getText().toString());

            double d_tip = 0.0;
            if (tip.getText() != null && !tip.getText().toString().equals("") && Double.parseDouble(tip.getText().toString()) > 0){
                d_tip = Double.parseDouble(tip.getText().toString());

                total_tip.setText(String.valueOf(calcTip(d_subtotal, d_tip)));
                total_tip_person.setText(String.valueOf(calcPersonTip(d_subtotal, d_tip, i_persons)));
            }

            total.setText(String.valueOf(calcTotal(d_subtotal, d_tip)));
            total_person.setText(String.valueOf(calcTotalPerson(d_subtotal, d_tip, i_persons)));

            calculated = true;
        }
        catch(Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);

            showAToast(getResources().getString(R.string.catch2));
            fab.setImageResource(R.mipmap.ic_equal_white_48dp);

            if(total.getText() == null || total.getText().equals(""))
                total.setText(getResources().getString(R.string.error_generic));

            if(total_person.getText() == null || total_person.getText().equals(""))
                total_person.setText(getResources().getString(R.string.error_generic));

            if(total_tip.getText() == null || total_tip.getText().equals(""))
                total_tip.setText(getResources().getString(R.string.error_generic));

            if(total_tip_person.getText() == null || total_tip_person.getText().equals(""))
                total_tip_person.setText(getResources().getString(R.string.error_generic));

            calculated = false;
        }

    }

    double calcTotal(double total, double tip){
        return round(total + ((total / 100) * tip), 2);
    }

    double calcTotalPerson(double total, double tip, int persons){
        return round((total + ((total / 100) * tip)) / persons, 2);
    }

    double calcTip(double total, double tip){
        return round((total / 100) * tip, 2);
    }

    double calcPersonTip(double total, double tip, int persons){
        return round(((total / 100) * tip) / persons, 2);
    }

    public void showAToast (final String message) { //"Toast toast" is declared in the class
        Super_Bill.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        toast.getView().isShown();     // true if visible
                        toast.setText(message);
                    } catch (Exception e) {         // invisible if exception
                        e.printStackTrace();
                        Crashlytics.logException(e);

                        toast = Toast.makeText(Super_Bill.this, message, Toast.LENGTH_LONG);
                    }
                    toast.show();  //finally display it
                }
                else{
                    try {
                        final Snackbar snackBar = Snackbar.make(content, message, Snackbar.LENGTH_LONG);

                        snackBar.setAction(getResources().getString(R.string.messaggio_chiudi), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackBar.dismiss();
                            }
                        });
                        snackBar.show();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Crashlytics.logException(e);

                        try {
                            toast.getView().isShown();     // true if visible
                            toast.setText(message);
                        } catch (Exception f) {         // invisible if exception
                            f.printStackTrace();
                            Crashlytics.logException(f);

                            toast = Toast.makeText(Super_Bill.this, message, Toast.LENGTH_LONG);
                        }
                        toast.show();  //finally display it
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
