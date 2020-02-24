package dev.philsca_capstone.avs_gsa.Fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.widget.DatePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import dev.philsca_capstone.avs_gsa.R;

public class DatePickerFragment extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    MaterialEditText edtShippingDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new android.app.DatePickerDialog(getActivity(),this, year , month ,day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        edtShippingDate = getActivity().findViewById(R.id.edtDate);
        edtShippingDate.setText(new StringBuilder().append(dayOfMonth).append(" / ").append(month+1).append(" / ").append(year));
    }

}
