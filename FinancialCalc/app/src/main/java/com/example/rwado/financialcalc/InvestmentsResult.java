package com.example.rwado.financialcalc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class InvestmentsResult extends AppCompatActivity {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_PERIOD_VALUES = "extra_period_values";
    public static final String EXTRA_CURRENCY = "extra_currency";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_CAPITALIZATION = "extra_capitalization";
    public static final String EXTRA_YEAR_TIME = "extra_year_time";

    private int yearTime;

    private Toolbar toolbar;

    private  PieChartView pieChartView;

    private TextView textExtraAmount, textExtraInterest, textExtraCapitalizationValue, textExtraPeriodValue,
            textExtraCurrency, textExtraCapitalization, textExtraPeriod, textResultAmount,
    textProfitBrutto, textProfitNetto, textTax, textCurrency1, textCurrency2, textCurrency3, textCurrency4;

    private Double amount, interest, periodValue, resultAmount, profitBrutto, profitNetto;
    private String currency, period, capitalizationValue, capitalization, capitalizationValueTemp;

    private Double capitalizationsPerYear = 1.0;
    private Double totalNumberOfCapitalization = 1.0;
    private Double restOfThePeriod = 0.1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investments_result_layout);

        initializeViews();

        getExtraValues();

        getCapitalization();

        setTextViewsWithExtraValues();

        calculateInvestment();

        writeResults();

        completeTheChart();

    }

    private void initializeViews(){
        pieChartView = findViewById(R.id.chart);
        toolbar = findViewById(R.id.toolbarPop);
        textExtraAmount = (TextView) findViewById(R.id.textExtraInvestAmount);
        textExtraInterest = (TextView) findViewById(R.id.textExtraInvestInterest);
        textExtraPeriodValue = (TextView) findViewById(R.id.textExtraPeriodValue);
        textExtraCapitalizationValue = (TextView) findViewById(R.id.textExtraInvestCapitalizationValue);
        textExtraCurrency = (TextView) findViewById(R.id.textExtraInvestCurrency);
        textExtraCapitalization = (TextView) findViewById(R.id.textExtraInvestCapitalization);
        textExtraPeriod = (TextView) findViewById(R.id.textExtraPeriod);
        textResultAmount = (TextView) findViewById(R.id.textInvestTotalSum);
        textProfitBrutto = (TextView) findViewById(R.id.textInvestProfitBrutto);
        textProfitNetto = (TextView) findViewById(R.id.textInvestProfitNetto);
        textTax = (TextView) findViewById(R.id.textInvestTax);
        textCurrency1 = (TextView) findViewById(R.id.textInvestCurrency1);
        textCurrency2 = (TextView) findViewById(R.id.textInvestCurrency2);
        textCurrency3 = (TextView) findViewById(R.id.textInvestCurrency3);
        textCurrency4 = (TextView) findViewById(R.id.textInvestCurrency4);
        toolbar.setTitle(getResources().getString(R.string.investments));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getExtraValues() {
        amount = getIntent().getExtras().getDouble(EXTRA_AMOUNT);
        interest = getIntent().getExtras().getDouble(EXTRA_INTEREST);
        periodValue = getIntent().getExtras().getDouble(EXTRA_PERIOD_VALUES);
        currency = getIntent().getExtras().getString(EXTRA_CURRENCY);
        period = getIntent().getExtras().getString(EXTRA_PERIOD);
        capitalizationValue = getIntent().getExtras().getString(EXTRA_CAPITALIZATION);
        yearTime = getIntent().getExtras().getInt(EXTRA_YEAR_TIME);
    }

    private void getCapitalization(){
        if (getResources().getString(R.string.one_day).equals(capitalizationValue)) {
            capitalizationValueTemp = "1";
            capitalization = getResources().getString(R.string.day);

        } else if (getResources().getString(R.string.one_week).equals(capitalizationValue)) {
            capitalizationValueTemp = "1";
            capitalization = getResources().getString(R.string.week);

        } else if (getResources().getString(R.string.one_month).equals(capitalizationValue)) {
            capitalizationValueTemp = "1";
            capitalization = getResources().getString(R.string.months);

        } else if (getResources().getString(R.string.quarter).equals(capitalizationValue)) {
            capitalizationValueTemp = "1";
            capitalization = getResources().getString(R.string.quarter);

        } else if (getResources().getString(R.string.half_year).equals(capitalizationValue)) {
            capitalizationValueTemp = "6";
            capitalization = getResources().getString(R.string.months);

        } else if (getResources().getString(R.string.year).equals(capitalizationValue)) {
            capitalizationValueTemp = "1";
            capitalization = getResources().getString(R.string.year);

        } else if (getResources().getString(R.string.end_of_period).equals(capitalizationValue)) {
            capitalizationValueTemp = getResources().getString(R.string.end_of_period);
            capitalization = "-";

        }
    }

    private void setTextViewsWithExtraValues() {
        textExtraAmount.setText(String.valueOf(amount));
        textExtraInterest.setText(String.valueOf(interest));
        textExtraPeriodValue.setText(String.valueOf(periodValue));
        textExtraCapitalizationValue.setText(String.valueOf(capitalizationValueTemp));
        textExtraCurrency.setText(currency);
        textExtraCapitalization.setText(capitalization);
        textExtraPeriod.setText(period);
        textCurrency1.setText(currency);
        textCurrency2.setText(currency);
        textCurrency3.setText(currency);
        textCurrency4.setText(currency);
    }

    private void calculateInvestment(){
        
        calculateTotalNumberOfCapitalizations();
        calculateNumberOfCapitalizationPerYear();

        profitBrutto = amount;
        profitNetto = amount;

        for(int i = 0; i < totalNumberOfCapitalization - 0.99; i++) {
            profitNetto += roundToTwo(
                    ((profitNetto * interest/capitalizationsPerYear/100.0) * 0.81));
            profitBrutto += roundToTwo(
                    (profitBrutto * interest/capitalizationsPerYear/100.0));
        }
        if(countAtEnd()) {
            countAtEndPeriod(restOfThePeriod);
        }
        profitBrutto = roundToTwo(profitBrutto - amount);
        profitNetto = roundToTwo(profitNetto - amount);
        resultAmount = roundToTwo(amount + profitBrutto);
    }

    private void writeResults() {
        textResultAmount.setText(String.valueOf(resultAmount));
        textProfitBrutto.setText(String.valueOf(profitBrutto));
        textProfitNetto.setText(String.valueOf(profitNetto));
        textTax.setText(String.valueOf(roundToTwo(profitBrutto - profitNetto)));
    }

    private void completeTheChart(){
        Double a,b,c;
        a = amount/(amount+profitBrutto);
        b = profitBrutto/(amount+profitBrutto);
        c = (profitBrutto-profitNetto)/(amount+profitBrutto);

        if(b < 0.01) {
            a = 0.98;
            b = 0.015;
            c = 0.005;
        }

        Float x = a.floatValue();
        Float y = b.floatValue();
        Float z = c.floatValue();

        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(x, Color.rgb(255, 153, 51)));
        pieData.add(new SliceValue(y, Color.rgb(51, 153, 255 )));
        pieData.add(new SliceValue(z, Color.rgb(76, 191, 38 )));

        PieChartData pieChartData = new PieChartData(pieData);

        pieChartView.setPieChartData(pieChartData);
    }

    private boolean countAtEnd(){
        return restOfThePeriod >= 0.5;
    }


    private void calculateTotalNumberOfCapitalizations() {
        if (getResources().getString(R.string.days).equals(period)) {
            if (getResources().getString(R.string.one_day).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue;

            } else if (getResources().getString(R.string.one_week).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue / 7.0;
                restOfThePeriod = periodValue % 7.0;

            } else if (getResources().getString(R.string.one_month).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue / 30;
                restOfThePeriod = periodValue % 30;

            } else if (getResources().getString(R.string.quarter).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 4 * periodValue / yearTime;
                restOfThePeriod = periodValue % (yearTime / 4.0);

            } else if (getResources().getString(R.string.half_year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 2 * periodValue / yearTime;
                restOfThePeriod = periodValue % (yearTime / 2.0);

            } else if (getResources().getString(R.string.year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue / yearTime;
                restOfThePeriod = periodValue % yearTime;

            } else if (getResources().getString(R.string.end_of_period).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 0.0;
                restOfThePeriod = periodValue;

            }

        } else if (getResources().getString(R.string.months).equals(period)) {
            if (getResources().getString(R.string.one_day).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 30 * periodValue;

            } else if (getResources().getString(R.string.one_week).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 30 * periodValue / 7.0;
                restOfThePeriod = 30 * periodValue % 7.0;

            } else if (getResources().getString(R.string.one_month).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue;
                restOfThePeriod = 30 * periodValue % 30;

            } else if (getResources().getString(R.string.quarter).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 30 * 4 * periodValue / yearTime;
                restOfThePeriod = 30 * periodValue % (yearTime / 4.0);

            } else if (getResources().getString(R.string.half_year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 30 * 2 * periodValue / yearTime;
                restOfThePeriod = 30 * periodValue % (yearTime / 2.0);

            } else if (getResources().getString(R.string.year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 30 * periodValue / yearTime;
                restOfThePeriod = 30 * periodValue % yearTime;

            } else if (getResources().getString(R.string.end_of_period).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 0.0;
                restOfThePeriod = 30 * periodValue;

            }

        } else if (getResources().getString(R.string.years).equals(period)) {
            if (getResources().getString(R.string.one_day).equals(capitalizationValue)) {
                totalNumberOfCapitalization = yearTime * periodValue;

            } else if (getResources().getString(R.string.one_week).equals(capitalizationValue)) {
                totalNumberOfCapitalization = yearTime * periodValue / 7.0;
                restOfThePeriod = yearTime * periodValue % 7.0;

            } else if (getResources().getString(R.string.one_month).equals(capitalizationValue)) {
                totalNumberOfCapitalization = yearTime * periodValue / 30;
                restOfThePeriod = yearTime * periodValue % 30;

            } else if (getResources().getString(R.string.quarter).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 4 * periodValue;
                restOfThePeriod = yearTime * periodValue % (yearTime / 4.0);

            } else if (getResources().getString(R.string.half_year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 2 * periodValue;
                restOfThePeriod = yearTime * periodValue % (yearTime / 2.0);

            } else if (getResources().getString(R.string.year).equals(capitalizationValue)) {
                totalNumberOfCapitalization = periodValue;
                restOfThePeriod = yearTime * periodValue % yearTime;

            } else if (getResources().getString(R.string.end_of_period).equals(capitalizationValue)) {
                totalNumberOfCapitalization = 0.0;
                restOfThePeriod = yearTime * periodValue;

            }

        }
    }

    private void calculateNumberOfCapitalizationPerYear() {
        if (getResources().getString(R.string.one_day).equals(capitalizationValue)) {
            capitalizationsPerYear = Double.valueOf(yearTime);

        } else if (getResources().getString(R.string.one_week).equals(capitalizationValue)) {
            capitalizationsPerYear = yearTime / 7.0;

        } else if (getResources().getString(R.string.one_month).equals(capitalizationValue)) {
            capitalizationsPerYear = yearTime / 30.0;

        } else if (getResources().getString(R.string.quarter).equals(capitalizationValue)) {
            capitalizationsPerYear = 4.0;

        } else if (getResources().getString(R.string.half_year).equals(capitalizationValue)) {
            capitalizationsPerYear = 2.0;

        } else if (getResources().getString(R.string.year).equals(capitalizationValue)) {
            capitalizationsPerYear = 1.0;

        } else if (getResources().getString(R.string.end_of_period).equals(capitalizationValue)) {
            capitalizationsPerYear = 1.0;

        }
    }

    private void countAtEndPeriod(double time) {
        profitNetto  += roundToTwo(((profitNetto * time*interest/yearTime/100.0) * 0.81));
        profitBrutto += roundToTwo((profitBrutto * time*interest/yearTime/100.0));

    }





    private static double roundToTwo(double value) {
            value = value * 100;
            long tmp = Math.round(value);
            return (double) tmp / 100;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
