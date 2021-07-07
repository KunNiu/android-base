package com.sktlab.android.base.country;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sktlab.android.base.R;
import com.sktlab.android.base.util.Utils;

import java.util.ArrayList;

/**
 * Created by android on 17/10/17.
 */

public class CountryPicker extends DialogFragment {

    private ArrayList<Country> allCountries = new ArrayList<>();
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private OnPick onPick;

    public CountryPicker() {
    }

    public static CountryPicker newInstance(Bundle args, OnPick onPick) {
        CountryPicker picker = new CountryPicker();
        picker.setArguments(args);
        picker.onPick = onPick;
        return picker;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_country_picker, container, false);
        EditText etSearch = (EditText) root.findViewById(R.id.et_search);
        final RecyclerView rvCountry = (RecyclerView) root.findViewById(R.id.rv_country);
        allCountries.clear();
        allCountries.addAll(Country.getAll(getContext(), null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final Adapter adapter = new Adapter(getContext());
        adapter.setOnPick(country -> {
            dismiss();
            if (onPick != null) onPick.onPick(country);
        });
        adapter.setSelectedCountries(selectedCountries);
        rvCountry.setAdapter(adapter);
        rvCountry.setLayoutManager(new LinearLayoutManager(getContext()));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if (country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getContext() != null) {
            Window win = getDialog().getWindow();
//            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                WindowManager.LayoutParams params = win.getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = dm.widthPixels / 2;
                params.height = dm.heightPixels / 3;
//                params.horizontalMargin = Utils.dpToPx(getContext(), 100);
                win.setAttributes(params);
            }
        }
    }
}
