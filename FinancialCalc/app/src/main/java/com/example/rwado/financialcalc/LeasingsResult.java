package com.example.rwado.financialcalc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class LeasingsResult extends AppCompatActivity {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_SELF_DEPOSIT = "extra_self_deposit";
    public static final String EXTRA_FINAL_BUYOUT = "extra_final_buyout";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_PERIOD_TYPE = "extra_period_type";
    public static final String EXTRA_CURRENCY = "extra_currency";

    Toolbar toolbar;

    TextView textAmount, textInterest, textPeriod, textSelfDeposit, textSelfDepositFlat,
            textFinalBuyout, textFinalBuyoutFlat, textCurrency1, textCurrency2, textCurrency3,
            textCurrency4, textCurrency5, textCurrency6, textPeriodType, textTotalSumFlat,
            textTotalSumPercentage, textInstallment, textMarginFlat, textMarginPercentage;

    Double mAmount, mInterest, mPeriod, mSelfDeposit, mSelfDepositFlat, mFinalBuyout, mFinalBuyoutFlat, mTotalSum, mInstallment;
    String mCurrency = "", mPeriodType = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leasings_result_layout);

        initializeViews();

        getExtraValues();

        setTextViewsWithExtraValues();

        calculate();

        writeResults();

    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbarPop2);
        toolbar.setTitle(getResources().getString(R.string.leasings));
        setSupportActionBar(toolbar);

        textAmount = (TextView) findViewById(R.id.textExtraLeasingsAmount);
        textInterest = (TextView) findViewById(R.id.textExtraLeasingsInterest);
        textPeriod = (TextView) findViewById(R.id.textExtraLeasingsPeriod);
        textSelfDeposit = (TextView) findViewById(R.id.textExtraLeasingsSelfDeposit);
        textSelfDepositFlat = (TextView) findViewById(R.id.textExtraLeasingsSelfDepositFlat);
        textFinalBuyout = (TextView) findViewById(R.id.textExtraLeasingsFinalBuyout);
        textFinalBuyoutFlat = (TextView) findViewById(R.id.textExtraLeasingsFinalBuyoutFlat);
        textCurrency1 = (TextView) findViewById(R.id.textExtraLeasingsCurrency);
        textCurrency2 = (TextView) findViewById(R.id.textExtraLeasingsCurrency1);
        textCurrency3 = (TextView) findViewById(R.id.textExtraLeasingsCurrency2);
        textCurrency4 = (TextView) findViewById(R.id.textExtraLeasingsCurrency3);
        textCurrency5 = (TextView) findViewById(R.id.textExtraLeasingsCurrency4);
        textCurrency6 = (TextView) findViewById(R.id.textExtraLeasingsCurrency5);
        textPeriodType = (TextView) findViewById(R.id.textExtraLeasingsPeriodType);
        textTotalSumFlat = (TextView) findViewById(R.id.textLeasingsTotalSumFlat);
        textTotalSumPercentage = (TextView) findViewById(R.id.textLeasingsTotalSumPercentage);
        textMarginFlat = (TextView) findViewById(R.id.textLeasingsMarginFlat);
        textMarginPercentage = (TextView) findViewById(R.id.textLeasingsMarginPercentage);
        textInstallment = (TextView) findViewById(R.id.textLeasingsRate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getExtraValues() {
        mAmount = getIntent().getExtras().getDouble(EXTRA_AMOUNT);
        mInterest = getIntent().getExtras().getDouble(EXTRA_INTEREST);
        mPeriod = getIntent().getExtras().getDouble(EXTRA_PERIOD);
        mSelfDeposit = getIntent().getExtras().getDouble(EXTRA_SELF_DEPOSIT);
        mFinalBuyout = getIntent().getExtras().getDouble(EXTRA_FINAL_BUYOUT);
        mCurrency = getIntent().getExtras().getString(EXTRA_CURRENCY);
        mPeriodType = getIntent().getExtras().getString(EXTRA_PERIOD_TYPE);
        mSelfDepositFlat = mAmount*mSelfDeposit/100;
        mFinalBuyoutFlat = mAmount*mFinalBuyout/100;
    }

    private void setTextViewsWithExtraValues() {
        textAmount.setText(String.valueOf(mAmount));
        textInterest.setText(String.valueOf(mInterest));
        textPeriod.setText(String.valueOf(mPeriod));
        textSelfDeposit.setText(String.valueOf(mSelfDeposit));
        textSelfDepositFlat.setText(String.valueOf(roundToTwo(mSelfDepositFlat)));
        textFinalBuyout.setText(String.valueOf(mFinalBuyout));
        textFinalBuyoutFlat.setText(String.valueOf(mFinalBuyoutFlat));
        textCurrency1.setText(mCurrency);
        textCurrency2.setText(mCurrency);
        textCurrency3.setText(mCurrency);
        textCurrency4.setText(mCurrency);
        textCurrency5.setText(mCurrency);
        textCurrency6.setText(mCurrency);
        textPeriodType.setText(mPeriodType);
    }

    private void calculate() {
        if(mPeriodType.equals(getResources().getString(R.string.years))){
            mPeriod *= 12;
        }
        Double mMonthlyInterest = mInterest/1200;
        mInstallment = ((mAmount - mSelfDepositFlat) * mMonthlyInterest *
                Math.pow((1+mMonthlyInterest), mPeriod) -
                mFinalBuyoutFlat*mMonthlyInterest) /
                ( Math.pow((1+mMonthlyInterest),mPeriod) -1 );
    }

    private void writeResults() {
        textInstallment.setText(String.valueOf(roundToTwo(mInstallment)));
        mTotalSum = roundToTwo(mInstallment*mPeriod);
        textTotalSumFlat.setText(String.valueOf(roundToTwo(mTotalSum + mSelfDepositFlat +mFinalBuyoutFlat)));
        textTotalSumPercentage.setText(String.valueOf(roundToTwo(100* (mTotalSum+mSelfDepositFlat+mFinalBuyoutFlat)/mAmount)));
        textMarginFlat.setText(String.valueOf(roundToTwo(mTotalSum+mSelfDepositFlat +mFinalBuyoutFlat - mAmount)));
        textMarginPercentage.setText(String.valueOf(roundToTwo(100* (mTotalSum+mSelfDepositFlat+mFinalBuyoutFlat - mAmount)/mAmount)));
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
