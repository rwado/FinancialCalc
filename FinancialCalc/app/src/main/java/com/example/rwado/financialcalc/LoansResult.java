package com.example.rwado.financialcalc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class LoansResult extends AppCompatActivity {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_COMMISION = "extra_commision";
    public static final String EXTRA_PERIOD_AMOUNT = "extra_period_amount";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_CURRENCY = "extra_currency";
    public static final String EXTRA_RATE_TYPE = "extra_rate_type";

    private TextView textCreditInstallment;
    private TextView textInterestSum;
    private TextView textCommisionSum;
    private TextView textSumAmount;
    private TextView textExtraAmount;
    private TextView textExtraInterest;
    private TextView textExtraCommision;
    private TextView textExtraPeriod;
    private TextView textExtraCurrency;
    private TextView textYearMonth;
    private TextView textCurrency1,textCurrency2,textCurrency3,textCurrency4, textCurrency5;
    private TextView textRateFirst;
    private TextView textRateLast;
    private TextView textCreditInstallmentLast;
    private TextView textRates;

    private Float amount, interest, commision, period, totalSum = 0f, creditRate, interestSum,
        commisionSum, capitalPart, interestPart = 0f;

    private String currency, yearMonth;

    private Boolean rateTypeEqual = true;
    private PieChartView pieChartView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loans_result_layout);

        initializeViews();

        getExtraValues();

        setTextViewsWithExtraValues();

        getPeriodValue();

        calculateLoanAndWriteResults();

        completeChart();

    }

    private void initializeViews() {

        pieChartView = findViewById(R.id.chart);
        toolbar = findViewById(R.id.toolbarPop);
        toolbar.setTitle(getResources().getString(R.string.loans));
        textCreditInstallment = (TextView) findViewById(R.id.textCreditRate);
        textInterestSum = (TextView) findViewById(R.id.textInterestSum);
        textCommisionSum = (TextView) findViewById(R.id.textCommisionSum);
        textSumAmount = (TextView) findViewById(R.id.textSumAmount);
        textExtraAmount = (TextView) findViewById(R.id.textExtraAmount);
        textExtraInterest = (TextView) findViewById(R.id.textExtraInterest);
        textExtraCommision = (TextView) findViewById(R.id.textExtraCommision);
        textExtraPeriod = (TextView) findViewById(R.id.textExtraPeriod);
        textExtraCurrency = (TextView) findViewById(R.id.textExtraCurrency);
        textCurrency1 = (TextView) findViewById(R.id.textCurrency1);
        textCurrency2 = (TextView) findViewById(R.id.textCurrency2);
        textCurrency3 = (TextView) findViewById(R.id.textCurrency3);
        textCurrency4 = (TextView) findViewById(R.id.textCurrency4);
        textCurrency5 = (TextView) findViewById(R.id.textCurrency5);
        textYearMonth = (TextView) findViewById(R.id.textYearMonth);
        textRateFirst = (TextView) findViewById(R.id.textRateFirst);;
        textRateLast = (TextView) findViewById(R.id.textRateLast);;
        textCreditInstallmentLast = (TextView) findViewById(R.id.textCreditRateLast);
        textRates = (TextView) findViewById(R.id.textRates);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getExtraValues() {
        amount = getIntent().getExtras().getFloat(EXTRA_AMOUNT);
        interest = getIntent().getExtras().getFloat(EXTRA_INTEREST);
        commision = getIntent().getExtras().getFloat(EXTRA_COMMISION);
        period = getIntent().getExtras().getFloat(EXTRA_PERIOD_AMOUNT);
        currency = getIntent().getExtras().getString(EXTRA_CURRENCY);
        yearMonth = getIntent().getExtras().getString(EXTRA_PERIOD);
        rateTypeEqual = getIntent().getExtras().getBoolean(EXTRA_RATE_TYPE);
    }

    private void setTextViewsWithExtraValues() {

        textExtraAmount.setText(String.valueOf(amount));
        textExtraInterest.setText(String.valueOf(interest));
        textExtraCommision.setText(String.valueOf(commision));
        textExtraPeriod.setText(String.valueOf(period));
        textExtraCurrency.setText(currency);
        textYearMonth.setText(yearMonth);
        textCurrency1.setText(currency);
        textCurrency2.setText(currency);
        textCurrency3.setText(currency);
        textCurrency4.setText(currency);
        textCurrency5.setText(currency);
    }

    private void getPeriodValue() {
        if(yearMonth.equals(getResources().getString(R.string.years))){
            period *= 12;
        }
    }

    private void calculateLoanAndWriteResults() {
        if(rateTypeEqual) {
            textRates.setText(getResources().getString(R.string.equal_installment));
            textRateFirst.setText(getResources().getString(R.string.installment_rate));
            textRateLast.setVisibility(View.INVISIBLE);
            textCreditInstallmentLast.setVisibility(View.INVISIBLE);
            textCurrency5.setVisibility(View.INVISIBLE);

            calculateEqualInstallment();

            totalSum = roundToTwo(creditRate * period);
            interestSum = roundToTwo(totalSum - amount);
            totalSum = roundToTwo(totalSum);
            creditRate = roundToTwo(creditRate);
            textCreditInstallment.setText(String.valueOf(creditRate));
        } else {
            textRates.setText(getResources().getString(R.string.decreasing_installment));
            textRateLast.setVisibility(View.VISIBLE);
            textRateFirst.setText(getResources().getString(R.string.first_installment));
            textRateLast.setText(getResources().getString(R.string.last_installment));
            textCreditInstallmentLast.setVisibility(View.VISIBLE);
            textCurrency5.setVisibility(View.VISIBLE);

            calculateDecreasingInstallment();

            totalSum = roundToTwo(totalSum);
            interestSum = roundToTwo(totalSum-amount);
            totalSum = roundToTwo(totalSum);
            creditRate = roundToTwo(interestPart+capitalPart);
        }
        commisionSum = amount*commision/100;

        textInterestSum.setText(String.valueOf(interestSum));
        textCommisionSum.setText(String.valueOf(commisionSum));
        textSumAmount.setText(String.valueOf(totalSum+commisionSum));

    }

    private void completeChart() {
        Float a,b,c;
        a = amount/(totalSum+commisionSum);
        b = commisionSum/(totalSum+commisionSum);
        c = interestSum/(totalSum+commisionSum);

        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(a, Color.rgb(255, 153, 51)));
        pieData.add(new SliceValue(b, Color.rgb(51, 153, 255 )));
        pieData.add(new SliceValue(c, Color.rgb(76, 191, 38 )));

        PieChartData pieChartData = new PieChartData(pieData);

        pieChartView.setPieChartData(pieChartData);
    }

    private void calculateEqualInstallment() {
        Float gamma = 1 + interest / 1200.0f;
        if (gamma.equals(1.0)) {
            creditRate = amount / period;
        } else {
            Double creditRateTemp = amount * Math.
                    pow(gamma, period) * (gamma - 1.0) / (Math.pow(gamma, period) - 1.0);
            creditRate = creditRateTemp.floatValue();
        }
        totalSum = creditRate * period;
    }

    private void calculateDecreasingInstallment() {
        capitalPart = amount / period;
        for(int i = 0; i < period; i++) {
            interestPart = ((amount - (i * capitalPart))* interest/100) / 12;
            if(i == 0) {
                textCreditInstallment.
                        setText(String.valueOf(roundToTwo(interestPart + capitalPart)));
            }
            if(i == (period - 1)) {
                textCreditInstallmentLast.
                        setText(String.valueOf(roundToTwo(interestPart + capitalPart)));
            }
            totalSum = totalSum + capitalPart + interestPart;
        }
    }

    public static float roundToTwo(float value) {
        return Math.round(value*100.0) / 100.0f;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
