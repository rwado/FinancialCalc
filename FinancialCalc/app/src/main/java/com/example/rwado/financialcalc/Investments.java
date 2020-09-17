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

public class Investments extends Fragment {

    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_INTEREST = "extra_interest";
    public static final String EXTRA_PERIOD_VALUES = "extra_period_values";
    public static final String EXTRA_CURRENCY = "extra_currency";
    public static final String EXTRA_PERIOD = "extra_period";
    public static final String EXTRA_CAPITALIZATION = "extra_capitalization";
    public static final String EXTRA_YEAR_TIME = "extra_year_time";


    private Button amountAdd, amountSub, interestAdd, interestSub, periodAdd, periodSub;
    private SeekBar seekBarAmount, seekBarInterest, seekBarPeriod;
    private float amount, interest, period;

    final private String[] spinnerCurrencyValues = {"PLN", "EUR", "USD", "CHF", "SEK", "NOK"};
    final private String[] spinnerPeriodValues = new String[3];
    final private String[] spinnerCapitalizationValues = new String[7];
    private EditText editAmount, editInterest, editPeriod;
    private Spinner spinnerCapitalization, spinnerCurrency, spinnerPeriod;
    private RadioGroup radioYearTime;
    private Button buttonCalculate;

    private String currencyTemp;
    private String periodTemp;
    private String capitalizationTemp;
    private Integer yearTime = 360;

    private Boolean monthlyPeriod = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.investments_layout, container, false);

        initializeViews(view);

        setListeners();

        return view;
    }

    private void initializeViews(View view) {
        editAmount = (EditText) view.findViewById(R.id.editInvestAmount);
        editInterest = (EditText) view.findViewById(R.id.editInvestInterest);
        editPeriod = (EditText) view.findViewById(R.id.editInvestPeriod);

        spinnerCapitalization = (Spinner) view.findViewById(R.id.spinnerInvestCapitalization);
        spinnerCurrency = (Spinner) view.findViewById(R.id.spinnerInvestCurrency);
        spinnerPeriod = (Spinner) view.findViewById(R.id.spinnerInvestPeriod);
        radioYearTime = (RadioGroup) view.findViewById(R.id.radioGroupInvest);
        buttonCalculate = (Button) view.findViewById(R.id.buttonInvestCalculate);

        amountAdd = (Button) view.findViewById(R.id.buttonInvestAmountAdd);
        amountSub = (Button) view.findViewById(R.id.buttonInvestAmountSub);
        interestAdd = (Button) view.findViewById(R.id.buttonInvestInterestAdd);
        interestSub = (Button) view.findViewById(R.id.buttonInvestInterestSub);
        periodAdd = (Button) view.findViewById(R.id.buttonInvestPeriodAdd);
        periodSub = (Button) view.findViewById(R.id.buttonInvestPeriodSub);

        seekBarAmount = (SeekBar) view.findViewById(R.id.seekBarInvestAmount);
        seekBarInterest = (SeekBar) view.findViewById(R.id.seekBarInvestInterest);
        seekBarPeriod = (SeekBar) view.findViewById(R.id.seekBarInvestPeriod);


        spinnerPeriodValues[0] = getResources().getString(R.string.days);
        spinnerPeriodValues[1] = getResources().getString(R.string.months);
        spinnerPeriodValues[2] = getResources().getString(R.string.years);

        spinnerCapitalizationValues[0] = getResources().getString(R.string.one_day);
        spinnerCapitalizationValues[1] = getResources().getString(R.string.one_week);
        spinnerCapitalizationValues[2] = getResources().getString(R.string.one_month);
        spinnerCapitalizationValues[3] = getResources().getString(R.string.quarter);
        spinnerCapitalizationValues[4] = getResources().getString(R.string.half_year);
        spinnerCapitalizationValues[5] = getResources().getString(R.string.year);
        spinnerCapitalizationValues[6] = getResources().getString(R.string.end_of_period);


        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCurrencyValues);
        ArrayAdapter<String> adapterPeriod = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPeriodValues);
        ArrayAdapter<String> adapterCapitalization = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCapitalizationValues);

        spinnerCurrency.setAdapter(adapterCurrency);
        spinnerPeriod.setAdapter(adapterPeriod);
        spinnerCapitalization.setAdapter(adapterCapitalization);

        amount = Float.valueOf(editAmount.getText().toString());
        interest = Float.valueOf(editInterest.getText().toString());
        period = Float.valueOf(editPeriod.getText().toString());

        seekBarAmount.setMax(999000);
        seekBarInterest.setMax(199);
    }

    private void setListeners() {
        radioYearTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (rb.getText().toString().equals(getResources().getString(R.string.days_360))) {
                    yearTime = 360;
                } else {
                    yearTime = 365;
                }
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
                if (s != null && !s.toString().isEmpty()) {
                    amount = Float.valueOf(s.toString());
                }
                if (amount < 1000) {
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
                if (s != null && !s.toString().isEmpty())
                    interest = Float.valueOf(s.toString());
                if (interest < 0.1) {
                    interest = 0.1f;
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
                if (s != null && !s.toString().isEmpty())
                    period = Float.valueOf(s.toString());
                if (period < 1) {
                    period = 1;
                }
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
                periodTemp = spinnerPeriodValues[i];
                if (periodTemp.equals(getResources().getString(R.string.months))) {
                    seekBarPeriod.setMax(599);
                    monthlyPeriod = true;
                } else if (periodTemp.equals(getResources().getString(R.string.year))) {
                    seekBarPeriod.setMax(49);
                    monthlyPeriod = false;
                } else {
                    seekBarPeriod.setMax(599);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCapitalization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                capitalizationTemp = spinnerCapitalizationValues[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seekBarAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount = ((progress + 1000) / 1000) * 1000;
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
                interest = interest / 10;
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

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), InvestmentsResult.class);
                Boolean canStartIntent = true;
                if (editAmount.getText().toString().isEmpty() ||
                        editInterest.getText().toString().isEmpty() ||
                        editPeriod.getText().toString().isEmpty() ) {
                    canStartIntent = false;
                }
                if(canStartIntent) {
                    intent.putExtra(EXTRA_AMOUNT, Double.valueOf(editAmount.getText().toString()));
                    intent.putExtra(EXTRA_INTEREST, Double.valueOf(editInterest.getText().toString()));
                    intent.putExtra(EXTRA_PERIOD_VALUES, Double.valueOf(editPeriod.getText().toString()));
                    intent.putExtra(EXTRA_CURRENCY, currencyTemp);
                    intent.putExtra(EXTRA_PERIOD, periodTemp);
                    intent.putExtra(EXTRA_CAPITALIZATION, capitalizationTemp);
                    intent.putExtra(EXTRA_YEAR_TIME, yearTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subPeriod() {
        if (period > 1) {
            period -= 2;
            seekBarPeriod.setProgress((int) period);
            editPeriod.setText(String.valueOf(period));
        }
        if (period <= 1) {
            period = 1;
            seekBarPeriod.setProgress((int) period - 1);
            editPeriod.setText(String.valueOf(period));
        }

    }

    private void addPeriod() {
        if (period < 600) {
            seekBarPeriod.setProgress((int) period);
            editPeriod.setText(String.valueOf(period));
        }

    }

    private void subInterest() {
        if (interest > 0.5) {
            interest -= 0.6;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest * 10));
        }
        if (interest <= 0.5) {
            interest = 0.1f;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest * 10));
        }

    }

    private void addInterest() {
        if (interest < 20) {
            interest += 0.4;
            editInterest.setText(String.valueOf(interest));
            seekBarInterest.setProgress((int) (interest * 10));
        }

    }

    private void subAmount() {
        if (amount >= 2100) {
            amount -= 3000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int) amount);
        }
        if (amount < 1000) {
            amount = 1000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int) amount);
        }
    }

    private void addAmount() {
        if (amount < 1000000) {
            amount += 1000;
            editAmount.setText(String.valueOf(amount));
            seekBarAmount.setProgress((int) amount);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.investments));
        super.onViewCreated(view, savedInstanceState);
    }
}
