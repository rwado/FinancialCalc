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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;


public class Loans extends Fragment {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_COMMISION = "extra_commision";
    public static final String EXTRA_PERIOD_AMOUNT = "extra_period_amount";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_CURRENCY = "extra_currency";
    public static final String EXTRA_RATE_TYPE = "extra_rate_type";

    EditText editAmount;
    EditText editInterest;
    EditText editCommision;
    EditText editPeriod;

    Button buttonCalculate;
    Button amountAdd, amountSub, interestAdd, interestSub, commisionAdd, commisionSub, periodAdd, periodSub;

    SeekBar seekBarAmount, seekBarInterest, seekBarCommision, seekBarPeriod;

    Spinner spinnerCurrency;
    Spinner spinnerPeriod;

    RadioGroup radioRateType;

    String currencyTemp;
    String periodTemp;
    String[] currency = {"PLN", "EUR", "USD", "CHF", "SEK", "NOK"};
    String[] periodType = new String[2];

    Boolean rateTypeEqual = true;
    Boolean monthlyPeriod = false;

    float amount, interest, commision, period;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loans_layout, container, false);

        initializeViews(view);

        setListeners();

        return view;
    }

    private void initializeViews(View view) {

        editAmount = (EditText) view.findViewById(R.id.editAmount);
        editInterest = (EditText) view.findViewById(R.id.editInterest);
        editCommision = (EditText) view.findViewById(R.id.editCommision);
        editPeriod = (EditText) view.findViewById(R.id.editPeriod);

        spinnerCurrency = (Spinner) view.findViewById(R.id.spinnerCurrency);
        spinnerPeriod = (Spinner) view.findViewById(R.id.spinnerPeriodType);

        radioRateType = (RadioGroup) view.findViewById(R.id.radioLoansRateType);
        buttonCalculate = (Button) view.findViewById(R.id.buttonCalculate);

        amountAdd = (Button) view.findViewById(R.id.buttonAmountAdd);
        amountSub = (Button) view.findViewById(R.id.buttonAmountSub);
        interestAdd = (Button) view.findViewById(R.id.buttonInterestAdd);
        interestSub = (Button) view.findViewById(R.id.buttonInterestSub);
        commisionAdd = (Button) view.findViewById(R.id.buttonCommisionAdd);
        commisionSub = (Button) view.findViewById(R.id.buttonCommisionSub);
        periodAdd = (Button) view.findViewById(R.id.buttonPeriodAdd);
        periodSub = (Button) view.findViewById(R.id.buttonPeriodSub);

        seekBarAmount = (SeekBar) view.findViewById(R.id.seekBarAmount);
        seekBarInterest = (SeekBar) view.findViewById(R.id.seekBarInterest);
        seekBarCommision = (SeekBar) view.findViewById(R.id.seekBarCommision);
        seekBarPeriod = (SeekBar) view.findViewById(R.id.seekBarPeriod);
        periodType[0] = getResources().getString(R.string.years);
        periodType[1] = getResources().getString(R.string.months);
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, currency);
        ArrayAdapter<String> adapterPeriod = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, periodType);
        seekBarAmount.setMax(999000);
        seekBarInterest.setMax(199);
        seekBarCommision.setMax(199);
        spinnerCurrency.setAdapter(adapterCurrency);
        spinnerPeriod.setAdapter(adapterPeriod);



        amount = Float.valueOf(editAmount.getText().toString());
        interest = Float.valueOf(editInterest.getText().toString());
        commision = Float.valueOf(editCommision.getText().toString());
        period = Float.valueOf(editPeriod.getText().toString());
    }

    private void setListeners() {
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyTemp = currency[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                periodTemp = periodType[i];
                if(periodTemp.equals(getResources().getString(R.string.months))) {
                    seekBarPeriod.setMax(599);
                    monthlyPeriod = true;
                } else if(periodTemp.equals(getResources().getString(R.string.years))) {
                    seekBarPeriod.setMax(49);
                    monthlyPeriod = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty())
                    interest = Float.valueOf(s.toString());
                if(interest < 0.1) {
                    interest = 0.1f;
                }
            }
        });

        editCommision.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty())
                    commision = Float.valueOf(s.toString());
                if(commision < 0.1) {
                    commision = 0.1f;
                }
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

        seekBarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount = ( (progress + 1000) / 1000 ) * 1000;
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

        seekBarCommision.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                commision = progress + 1;
                commision = commision/10;
                editCommision.setText(String.valueOf(commision));
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

        commisionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommision();
            }
        });

        commisionSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCommision();
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

        radioRateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if(rb.getText().toString().equals(getResources().getString(R.string.equal))){
                    rateTypeEqual = true;
                } else {
                    rateTypeEqual = false;
                }
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoansResult.class);
                Boolean canStartIntent = true;
                if (editAmount.getText().toString().isEmpty() ||
                        editInterest.getText().toString().isEmpty() ||
                        editInterest.getText().toString().isEmpty() ||
                        editPeriod.getText().toString().isEmpty() ) {
                    canStartIntent = false;
                }
                if(canStartIntent) {
                    intent.putExtra(EXTRA_AMOUNT, Float
                            .valueOf(editAmount.getText().toString()));
                    intent.putExtra(EXTRA_INTEREST, Float
                            .valueOf(editInterest.getText().toString()));
                    intent.putExtra(EXTRA_COMMISION, Float
                            .valueOf(editCommision.getText().toString()));
                    intent.putExtra(EXTRA_PERIOD_AMOUNT, Float
                            .valueOf(editPeriod.getText().toString()));
                    intent.putExtra(EXTRA_PERIOD, periodTemp);
                    intent.putExtra(EXTRA_CURRENCY, currencyTemp);
                    intent.putExtra(EXTRA_RATE_TYPE, rateTypeEqual);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(
                            R.string.enter_data), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void addPeriod() {
        seekBarPeriod.setProgress((int) period);
        editPeriod.setText(String.valueOf(period));
    }

    private void subCommision() {
        if(commision > 0.5) {
            commision -= 0.6;
            editCommision.setText(String.valueOf(commision));
            seekBarCommision.setProgress((int) (commision*10));
        }
        if(commision <= 0.5) {
            commision = 0.1f;
            editCommision.setText(String.valueOf(commision));
            seekBarCommision.setProgress((int) (commision*10));
        }

    }

    private void addCommision() {
        if(commision < 20){
            commision += 0.4;
            editCommision.setText(String.valueOf(commision));
            seekBarCommision.setProgress((int) (commision*10));
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

    private void addInterest() {
        if(interest < 20){
            interest+=0.4;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest*10));
        }

    }

    private void subAmount() {
        if(amount >= 2100){
            amount -= 3000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int)amount);
        }
        if(amount < 1000) {
            amount = 1000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int)amount);
        }

    }

    private void addAmount() {
        if(amount < 1000000) {
            amount+= 1000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int)amount);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.loans));
        super.onViewCreated(view, savedInstanceState);
    }





}
