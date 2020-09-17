package com.example.rwado.financialcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class Leasings extends Fragment {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_SELF_DEPOSIT = "extra_self_deposit";
    public static final String EXTRA_FINAL_BUYOUT = "extra_final_buyout";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_PERIOD_TYPE = "extra_period_type";
    public static final String EXTRA_CURRENCY = "extra_currency";

    private EditText editAmount, editInterest, editPeriod, editSelfDeposit, editFinalBuyout;
    private Spinner spinnerCurrency, spinnerPeriod;

    private Button buttonCalculate;
    Button amountAdd, amountSub, interestAdd, interestSub, selfDepositAdd, selfDepositSub, periodAdd,
            periodSub, finalBuyoutAdd, finalBuyoutSub;

    private SeekBar seekBarAmount, seekBarInterest, seekBarPeriod, seekBarFinalBuyout, seekBarSelfDeposit;
    private float amount, interest, period, finalBuyout, selfDeposit;

    private final String[] spinnerCurrencyValues = {"PLN", "EUR", "USD", "CHF", "SEK", "NOK"};
    private String[] spinnerPeriodValues = new String[2];
    private String currencyTemp;
    private String periodType;
    private Boolean monthlyPeriod = true;
    private ArrayAdapter<String> adapterCurrency;
    private ArrayAdapter<String> adapterPeriod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leasings_layout, container, false);

        initializeViews(view);

        getValuesToPass();

        setListeners();

        return view;
    }

    private void initializeViews(View view) {
        editAmount = (EditText) view.findViewById(R.id.editLeasingsAmount);
        editInterest = (EditText) view.findViewById(R.id.editLeasingsInterest);
        editPeriod = (EditText) view.findViewById(R.id.editLeasingsPeriod);
        editSelfDeposit = (EditText) view.findViewById(R.id.editLeasingsSelfDeposit);
        editFinalBuyout = (EditText) view.findViewById(R.id.editLeasingFinalBuyout);

        spinnerCurrency = (Spinner) view.findViewById(R.id.spinnerLeasingsCurrency);
        spinnerPeriod = (Spinner) view.findViewById(R.id.spinnerLeasingsPeriod);

        buttonCalculate = (Button) view.findViewById(R.id.buttonLeasingsCalculate);
        amountAdd = (Button) view.findViewById(R.id.buttonLeasingsAmountAdd);
        amountSub = (Button) view.findViewById(R.id.buttonLeasingsAmountSub);
        interestAdd = (Button) view.findViewById(R.id.buttonLeasingsInterestAdd);
        interestSub = (Button) view.findViewById(R.id.buttonLeasingsInterestSub);
        selfDepositAdd = (Button) view.findViewById(R.id.buttonLeasingsSelfDepositAdd);
        selfDepositSub = (Button) view.findViewById(R.id.buttonLeasingsSelfDepositSub);
        periodAdd = (Button) view.findViewById(R.id.buttonLeasingsPeriodAdd);
        periodSub = (Button) view.findViewById(R.id.buttonLeasingsPeriodSub);
        finalBuyoutAdd = (Button) view.findViewById(R.id.buttonLeasingsFinalBuyoutAdd);
        finalBuyoutSub = (Button) view.findViewById(R.id.buttonLeasingsFinalBuyoutSub);

        seekBarAmount = (SeekBar) view.findViewById(R.id.seekBarLeasingsAmount);
        seekBarInterest = (SeekBar) view.findViewById(R.id.seekBarLeasingsnterest);
        seekBarPeriod = (SeekBar) view.findViewById(R.id.seekBarLeasingsPeriod);
        seekBarFinalBuyout = (SeekBar) view.findViewById(R.id.seekBarLeasingsFinalBuyout);
        seekBarSelfDeposit = (SeekBar) view.findViewById(R.id.seekBarLeasingsSelfDeposit);


        spinnerPeriodValues[0] = getResources().getString(R.string.years);
        spinnerPeriodValues[1] = getResources().getString(R.string.months);

        seekBarAmount.setMax(300000);
        seekBarInterest.setMax(199);
        seekBarPeriod.setMax(96);
        seekBarFinalBuyout.setMax(449);
        seekBarSelfDeposit.setMax(449);

        adapterCurrency = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCurrencyValues);
        adapterPeriod = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPeriodValues);
    }

    private void getValuesToPass(){
        amount = Float.valueOf(editAmount.getText().toString());
        interest = Float.valueOf(editInterest.getText().toString());
        finalBuyout = Float.valueOf(editFinalBuyout.getText().toString());
        period = Float.valueOf(editPeriod.getText().toString());
        selfDeposit = Float.valueOf(editSelfDeposit.getText().toString());
    }

    private void setListeners() {

        spinnerCurrency.setAdapter(adapterCurrency);
        spinnerPeriod.setAdapter(adapterPeriod);

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty()) {
                    amount = Float.valueOf(s.toString());
                }
                if(amount < 1000) {
                    amount = 1000;
                }
            }
        });

        editInterest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s != null && !s.toString().isEmpty())
                    interest = Float.valueOf(s.toString());
                if(interest < 0.1) {
                    interest = 0.1f;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty())
                    period = Float.valueOf(s.toString());
                if(period < 1) {
                    period = 1;
                }
            }
        });

        editSelfDeposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editFinalBuyout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyTemp = spinnerCurrencyValues[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                periodType = spinnerPeriodValues[i];
                if(periodType.equals(getResources().getString(R.string.months))) {
                    seekBarPeriod.setMax(119);
                    monthlyPeriod = true;
                } else if(periodType.equals(getResources().getString(R.string.years))) {
                    seekBarPeriod.setMax(9);
                    monthlyPeriod = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seekBarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ( (progress) / 1000 ) * 1000;
                amount = progress;
                editAmount.setText(String.valueOf(amount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarInterest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                interest = progress + 1;
                interest = interest/10;
                editInterest.setText(String.valueOf(interest));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                period = progress + 1;
                editPeriod.setText(String.valueOf(period));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarFinalBuyout.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                finalBuyout = progress + 1;
                finalBuyout = finalBuyout/10;
                editFinalBuyout.setText(String.valueOf(finalBuyout));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarSelfDeposit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selfDeposit = progress + 1;
                selfDeposit = selfDeposit/10;
                editSelfDeposit.setText(String.valueOf(selfDeposit));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        amountAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAmount();
            }
        });
        amountSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAmount();
            }
        });
        interestAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInterest();
            }
        });
        interestSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subInterest();
            }
        });
        selfDepositAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelfDeposit();
            }
        });
        selfDepositSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subSelfDeposit();
            }
        });
        periodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPeriod();
            }
        });
        periodSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subPeriod();
            }
        });
        finalBuyoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFinalBuyout();
            }
        });
        finalBuyoutSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subFinalBuyout();
            }
        });


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeasingsResult.class);
                Boolean canStartIntent = true;
                if (editAmount.getText().toString().isEmpty() ||
                        editInterest.getText().toString().isEmpty() ||
                        editPeriod.getText().toString().isEmpty() ||
                        editSelfDeposit.getText().toString().isEmpty() ||
                        editFinalBuyout.getText().toString().isEmpty()) {
                    canStartIntent = false;
                }
                if(canStartIntent) {
                    intent.putExtra(EXTRA_AMOUNT, Double.valueOf(editAmount.getText().toString()));
                    intent.putExtra(EXTRA_INTEREST, Double.valueOf(editInterest.getText().toString()));
                    intent.putExtra(EXTRA_PERIOD, Double.valueOf(editPeriod.getText().toString()));
                    intent.putExtra(EXTRA_SELF_DEPOSIT, Double.valueOf(editSelfDeposit.getText().toString()));
                    intent.putExtra(EXTRA_FINAL_BUYOUT, Double.valueOf(editFinalBuyout.getText().toString()));
                    intent.putExtra(EXTRA_CURRENCY, currencyTemp);
                    intent.putExtra(EXTRA_PERIOD_TYPE, periodType);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addAmount() {
        if(amount < 1000000) {
            amount+=1000;
            seekBarAmount.setProgress((int)amount);
            editAmount.setText(String.valueOf(amount));

        }
    }

    private void subAmount() {
        if(amount >= 1100){
            amount = amount - 1000;
            seekBarAmount.setProgress((int)amount);
        }
        if(amount < 1000) {
            amount = 1000;

            seekBarAmount.setProgress((int)amount);
        }
    }

    private void addInterest() {
        if(interest < 20){
            interest+=0.4;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest*10));
        }
    }

    private void subInterest() {
        if(interest > 0.5) {
            interest -= 0.6;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest*10));
        }
        if(interest <= 0.5) {
            interest = 0.1f;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest*10));
        }
    }

    private void addPeriod() {
        seekBarPeriod.setProgress((int) period);
        editPeriod.setText(String.valueOf(period));
    }

    private void subPeriod() {
        if(period > 1) {
            period-=2;
            seekBarPeriod.setProgress((int) period);
            editPeriod.setText(String.valueOf(period));
        }
        if(period <= 1) {
            period = 1;
            seekBarPeriod.setProgress((int) period - 1);
            editPeriod.setText(String.valueOf(period));
        }
    }

    private void addSelfDeposit() {
        if(selfDeposit < 45){
            selfDeposit+=0.4;
            editSelfDeposit.setText(String.valueOf(selfDeposit));
            seekBarSelfDeposit.setProgress((int) (selfDeposit*10));
        }

    }

    private void subSelfDeposit() {
        if(selfDeposit > 0.5) {
            selfDeposit -= 0.6;
            editSelfDeposit.setText(String.valueOf(selfDeposit));
            seekBarSelfDeposit.setProgress((int) (selfDeposit*10));
        }
        if(selfDeposit <= 0.5) {
            selfDeposit = 0.1f;
            editSelfDeposit.setText(String.valueOf(selfDeposit));
            seekBarSelfDeposit.setProgress((int) (selfDeposit*10));
        }
    }

    private void addFinalBuyout() {
        if(finalBuyout < 45){
            finalBuyout+=0.4;
            editFinalBuyout.setText(String.valueOf(finalBuyout));
            seekBarFinalBuyout.setProgress((int) (finalBuyout*10));
        }
    }

    private void subFinalBuyout() {
        if(finalBuyout > 0.5) {
            finalBuyout -= 0.6;
            editFinalBuyout.setText(String.valueOf(finalBuyout));
            seekBarFinalBuyout.setProgress((int) (finalBuyout*10));
        }
        if(finalBuyout <= 0.5) {
            finalBuyout = 0.1f;
            editFinalBuyout.setText(String.valueOf(finalBuyout));
            seekBarFinalBuyout.setProgress((int) (finalBuyout*10));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.leasings));
        super.onViewCreated(view, savedInstanceState);
    }
}
