package com.example.rwado.financialcalc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Transfers extends Fragment {

    private Boolean isWeekend = false;
    private Button buttonTime1;
    private Button buttonDate1;
    private Button buttonCalculate;
    private Button buttonTime2;
    private Button buttonDate2;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Transfers context;
    private Bank bankSender, bankRecipient;

    private String resultText = "";

    private Date date = new Date();

    private Spinner spinner1, spinner2;

    private  ArrayList<Bank> banks = new ArrayList<Bank>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transfers_layout, container, false);

        initializeViews(view);

        createBankList();

        initializeSpinners();

        getCurrDate();

        setListeners();

        showResult();

        return view;
    }

    private void initializeViews(View view) {
        spinner1 = (Spinner) view.findViewById(R.id.spinnerTransferBankSender);
        spinner2 = (Spinner) view.findViewById(R.id.spinnerTransferBankRecipient);
        buttonTime1 = (Button) view.findViewById(R.id.buttonTransferTime);
        buttonDate1 = (Button) view.findViewById(R.id.buttonTransferDate);
        buttonCalculate = (Button) view.findViewById(R.id.buttonTransferCalculate);
        buttonTime2 = (Button) view.findViewById(R.id.buttonTransferTime2);
        buttonDate2 = (Button) view.findViewById(R.id.buttonTransferDate2);
    }

    private void createBankList(){
        String[] bankNames = {"Alior Bank",	"Bank BPH",	"Bank BPS",	"Bank Millennium",	"Bank Pekao SA",	"Bank Pocztowy",	"BGK",	"BIZ Bank",	"BNP Paribas",	"BOŚ Bank",	"Citi Handlowy",	"Credit Agricole",	"Deutsche Bank Polska",	"DnB NORD",	"DZ BANK",	"Getin Bank",	"HSBC Bank Polska",	"Idea Bank",	"ING Bank Śląski",	"Inteligo",	"mBank",	"Meritum Bank",	"Nest Bank",	"Noble Bank",	"Nordea Bank",	"Pekao Bank Hipoteczny",	"PKO Bank Polski",	"PLUS BANK",	"Raiffeisen Polbank",	"Santander Bank Polska",	"T-Mobile Usługi Bankowe",	"Toyota Bank",	"VW Bank direct",};
        String[] firstOut = {"9:30",	"10:30",	"15:00",	"11:00",	"08:30",	"09:00",	"08:45",	"09:00",	"08:00",	"09:30",	"08:00",	"14:30",	"07:45",	"08:00",	"08:00",	"08:15",	"09:30",	"09:30",	"08:10",	"08:00",	"05:55",	"08:30",	"08:00",	"08:15",	"08:30",	"08:30",	"08:00",	"08:00",	"08:00",	"08:15",	"09:30",	"08:10",	"07:55"};
        String[] secondOut = {"13:30",	"14:30",	"15:00",	"15:00",	"12:30",	"13:00",	"12:00",	"12:00",	"11:45",	"13:30",	"12:15",	"14:30",	"12:00",	"12:00",	"12:00",	"12:15",	"13:30",	"13:30",	"11:30",	"11:45",	"09:55",	"12:30",	"11:30",	"12:15",	"11:50",	"12:30",	"11:45",	"11:30",	"12:15",	"12:15",	"13:30",	"12:10",	"11:45"};
        String[] thirdOut = {"16:00",	"17:00",	"15:00",	"17:30",	"15:00",	"15:00",	"15:10",	"14:30",	"14:15",	"16:00",	"15:30",	"14:30",	"15:00",	"14:30",	"14:30",	"14:30",	"16:00",	"16:00",	"14:30",	"14:30",	"13:25",	"15:00",	"14:00",	"14:30",	"14:30",	"15:30",	"14:30",	"14:00",	"15:00",	"14:45",	"16:00",	"14:40",	"14:15"};
        String[] firstIn = {"12:00",	"11:45",	"12:00",	"12:00",	"11:00",	"11:00",	"10:30",	"11:30",	"12:00",	"11:00",	"10:30",	"12:00",	"12:00",	"11:00",	"10:30",	"10:00",	"10:00",	"10:30",	"11:00",	"11:30",	"11:15",	"10:30",	"11:30",	"10:00",	"10:45",	"15:00",	"11:30",	"12:00",	"11:30",	"11:00",	"11:30",	"10:30",	"12:00"};
        String[] secondIn = {"15:30",	"15:45",	"16:00",	"15:30",	"15:00",	"15:00",	"14:30",	"15:30",	"15:00",	"15:00",	"14:30",	"16:00",	"16:00",	"15:00",	"14:35",	"14:00",	"14:00",	"14:30",	"15:00",	"15:10",	"15:00",	"14:30",	"15:30",	"14:00",	"14:45",	"17:00",	"15:10",	"15:30",	"15:15",	"15:00",	"15:30",	"14:30",	"16:00"};
        String[] thirdIn = {"17:30",	"17:00",	"18:00",	"17:15",	"17:30",	"17:30",	"17:00",	"17:30",	"18:00",	"17:30",	"17:30",	"18:00",	"17:30",	"17:30",	"16:30",	"17:00",	"16:00",	"16:30",	"17:30",	"17:30",	"18:15",	"14:30",	"17:30",	"17:00",	"17:15",	"20:00",	"17:30",	"18:00",	"18:00",	"18:00",	"17:30",	"16:30",	"18:00"};
        Integer i = 0;
        for(String bName:bankNames){
            banks.add(new Bank(bName));
            SimpleDateFormat mSdf = new SimpleDateFormat("HH:mm");

            Date[] dateOut = new Date[3];
            try{
                dateOut[0] = mSdf.parse(firstOut[i]);
                dateOut[1] = mSdf.parse(secondOut[i]);
                dateOut[2] = mSdf.parse(thirdOut[i]);
            } catch (Exception e){
                e.printStackTrace();
            }
            Date[] dateIn = new Date[3];
            try{
                dateIn[0] = mSdf.parse(firstIn[i]);
                dateIn[1] = mSdf.parse(secondIn[i]);
                dateIn[2] = mSdf.parse(thirdIn[i]);
            } catch (Exception e){
                e.printStackTrace();
            }
            banks.get(i).setOutgoingTransfersTime(dateOut);
            banks.get(i).setIncomingTransfersTime(dateIn);
            i++;
        }
    }

    private void initializeSpinners() {
        final List<String> banksSender = new ArrayList<String>();
        for(Bank bankTemp:banks) {
            banksSender.add(bankTemp.getName());
        }
        List<String> banksRecipient = new ArrayList<String>();
        for(Bank bankTemp:banks) {
            banksRecipient.add(bankTemp.getName());
        }

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, banksSender);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, banksRecipient);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter1);
        spinner2.setAdapter(dataAdapter2);
    }

    private void getCurrDate() {
        mMinute = date.getMinutes();
        mHour = date.getHours();
        mDay = date.getDate();
        mMonth = date.getMonth();
        mYear = date.getYear();
    }

    private void setListeners() {


        buttonTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });

        buttonDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });

        buttonTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });

        buttonDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });

        bankSender = banks.get(0);
        bankRecipient = banks.get(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)   {
                if(i > 0) {
                    bankSender = banks.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)   {
                if(i > 0) {
                    bankRecipient = banks.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void showResult() {
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bankSender == null || bankRecipient == null){
                    Toast.makeText(getContext(), getResources().getString(R.string.first_choose_banks), Toast.LENGTH_SHORT).show();
                } else if(bankSender.getName().equals(bankRecipient.getName())) {
                    resultText = getResources().getString(R.string.transfer_immediately);
                    showDialog();
                } else {
                    findDate();
                    showDialog();
                }
            }
        });

    }


    private void findDate() {
        date.setMinutes(mMinute);
        date.setHours(mHour);
        date.setDate(mDay);
        date.setMonth(mMonth);
        date.setYear(mYear);
        Date mDate = date;
        
        Date outgoingDate = compareDates(mDate, bankSender.getOutgoingTransfersTime());
        Date receivingDate = compareDates(outgoingDate, bankRecipient.getIncomingTransfersTime());
        
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        resultText = getResources().getString(R.string.your_transfer_will_come_on) + " " + formatDate.format(receivingDate) + " " + getResources().getString(R.string.at)+ " " +formatTime.format(receivingDate) + ".";

    }

    private Date compareDates(Date dateOne, Date[] dates) {

        for(int i = 0; i < 3; i++) {
            Date dateTemp = dates[i];

            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEEE");
            int a = 0;
            while(simpleDateFormat.format(dateOne).equals(getResources().getString(R.string.saturday)) || simpleDateFormat.format(dateOne).equals(getResources().getString(R.string.sunday))){
                a++;
                dateOne.setDate(mDay+a);
                isWeekend = true;
            }
            if(dateOne.getHours() < dateTemp.getHours()) {
                dateOne.setHours(dateTemp.getHours());
                dateOne.setMinutes(dateTemp.getMinutes());
                break;

            }
            if(dateOne.getHours() == dateTemp.getHours()) {
                if(dateOne.getMinutes() < dateTemp.getMinutes()) {
                    dateOne.setHours(dateTemp.getHours());
                    dateOne.setMinutes(dateTemp.getMinutes());
                    break;
                }
            }
            if(i == 2) {
                dateOne.setHours(dates[0].getHours());
                dateOne.setMinutes(dates[0].getMinutes());
                dateOne.setDate(mDay + 1);
            }


        }

        return dateOne;
    }



    private void pickTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        mHour = hourOfDay;
                        mMinute = minute;
                        String currentTime = sdfTime.format(date);
                        buttonTime2.setText(currentTime);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy");
                        date.setDate(dayOfMonth);
                        date.setMonth(monthOfYear);
                        date.setYear(year - 1900);
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;
                        mYear = year - 1900;
                        String currentTime = sdfTime.format(date);
                        buttonDate2.setText(currentTime);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showDialog(){
        final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater mLayoutInflater = getLayoutInflater();
        final View mDialogView = mLayoutInflater.inflate(R.layout.transfers_result_layout, null);
        mDialogBuilder.setView(mDialogView);
        mDialogBuilder.setTitle("");

        final TextView textTransferResult, editMaxRotation, editSignalDelayMS;
        Button buttonCancel;
        buttonCancel = mDialogView.findViewById(R.id.buttonCancel);
        textTransferResult = mDialogView.findViewById(R.id.textTransferResult);
        textTransferResult.setText(resultText);

        final AlertDialog mAlertDialog = mDialogBuilder.create();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.cancel();
            }
        });


        mAlertDialog.show();
    }


    @Override
    public void onResume() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = sdfTime.format(new Date());
        SimpleDateFormat stfData = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
        String currentDate = stfData.format(new Date());
        buttonTime2.setText(currentTime);
        buttonDate2.setText(currentDate);
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.transfers));
        super.onViewCreated(view, savedInstanceState);
    }

}
