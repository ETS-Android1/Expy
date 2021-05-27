package com.xdev.expy.ui.main.management;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.xdev.expy.data.source.remote.entity.ProductEntity;
import com.xdev.expy.data.source.remote.entity.ReminderEntity;
import com.xdev.expy.databinding.FragmentAddUpdateBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.ui.main.MainCallback;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.xdev.expy.utils.DateUtils.DATE_FORMAT;
import static com.xdev.expy.utils.DateUtils.addDay;
import static com.xdev.expy.utils.DateUtils.getArrayDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class AddUpdateFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = AddUpdateFragment.class.getSimpleName();
    private static final String EXPIRY_DATE_PICKER = "expiry_date_picker";
    private static final String OPENED_DATE_PICKER = "opened_date_picker";

    private static final String ARG_PRODUCT = "product";

    private FragmentAddUpdateBinding binding;
    private MainCallback mainCallback;
    private MainViewModel viewModel;
    private ProductEntity product;

    private boolean isUpdate = false;
    private String expiryDate = "";
    private String openedDate = "";

    public AddUpdateFragment() {}

    public static AddUpdateFragment newInstance(ProductEntity product) {
        AddUpdateFragment fragment = new AddUpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddUpdateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setContainerBackground(true);

        binding.toolbar.setNavigationOnClickListener(v -> mainCallback.backToHome(true));

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);

        binding.tvHelpPao.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.edtOpenedDate.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);
        binding.switchOpened.setOnCheckedChangeListener(this);

        isUpdate = !product.getId().isEmpty() ;
        if (isUpdate){
            binding.toolbarTitle.setText("Edit Produk");
            binding.btnDelete.setVisibility(View.VISIBLE);
            expiryDate = product.getExpiryDate();
            openedDate = product.getOpenedDate();
            binding.edtName.setText(product.getName());
            binding.edtExpiryDate.setText(getFormattedDate(expiryDate, false));
            binding.edtOpenedDate.setText(getFormattedDate(openedDate, false));
            binding.edtPao.setText(String.valueOf(product.getPao()));
            binding.switchOpened.setChecked(product.isOpened());
            binding.switchReminder.setChecked(!product.getReminders().isEmpty());
        } else {
            binding.toolbarTitle.setText("Tambah Produk");
            binding.btnDelete.setVisibility(View.GONE);
        }
        setExpiryDateFieldVisibility(product.isOpened());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == binding.tvHelpPao.getId()) {
            HelpPAOFragment.newInstance().show(getChildFragmentManager(), HelpPAOFragment.TAG);
        } else if (id == binding.edtExpiryDate.getId()) {
            showDatePicker(EXPIRY_DATE_PICKER, getContext(), expiryDate);
        } else if (id == binding.edtOpenedDate.getId()) {
            showDatePicker(OPENED_DATE_PICKER, getContext(), openedDate);
        } else if (id == binding.btnSave.getId()) {
            saveProduct(product, isUpdate);
        } else if (id == binding.btnDelete.getId()) {
            deleteProduct(product);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();
        if (id == binding.switchOpened.getId()){
            setExpiryDateFieldVisibility(checked);
        }
    }

    public void showDatePicker(String tag, Context context, String initialDate) {
        int initialYear;
        int initialMonth;
        int initialDay;

        if (initialDate == null || initialDate.isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            initialYear = calendar.get(Calendar.YEAR);
            initialMonth = calendar.get(Calendar.MONTH);
            initialDay = calendar.get(Calendar.DATE);
        } else {
            int[] arrayDate = getArrayDate(initialDate);
            initialYear = arrayDate[0];
            initialMonth = arrayDate[1];
            initialDay = arrayDate[2];
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            if(view.isShown()){
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                if (tag.equals(EXPIRY_DATE_PICKER)) {
                    expiryDate = simpleDateFormat.format(calendar.getTime());
                    binding.edtExpiryDate.setText(getFormattedDate(expiryDate, false));
                } else if (tag.equals(OPENED_DATE_PICKER)) {
                    openedDate = simpleDateFormat.format(calendar.getTime());
                    binding.edtOpenedDate.setText(getFormattedDate(openedDate, false));
                }
            }
        }, initialYear, initialMonth , initialDay);
        datePickerDialog.show();
    }

    private void saveProduct(ProductEntity product, boolean isUpdate) {
        if (binding.edtName.getText() == null || binding.edtPao.getText() == null) return;

        String name = binding.edtName.getText().toString();
        String expiryDate = this.expiryDate;
        String openedDate = this.openedDate;
        String pao;
        boolean opened = binding.switchOpened.isChecked();
        boolean reminder = binding.switchReminder.isChecked();

        if (name.isEmpty()) return;
        if (opened) {
            pao = binding.edtPao.getText().toString();
            expiryDate = addDay(openedDate, Integer.parseInt(pao)*30);
            if (openedDate.isEmpty() || pao.isEmpty()) {
                return;
            }
        } else {
            pao = "0";
            if (expiryDate.isEmpty()) {
                return;
            }
        }

        List<ReminderEntity> reminderList = new ArrayList<>();
        if (reminder) reminderList = setReminder(product);

        product.setName(name);
        product.setExpiryDate(expiryDate);
        product.setOpenedDate(openedDate);
        product.setPao(Integer.parseInt(pao));
        product.setOpened(opened);
        product.setReminders(reminderList);

        if (isUpdate) viewModel.updateProduct(product);
        else viewModel.insertProduct(product);
        mainCallback.backToHome(false);
    }

    private void deleteProduct(ProductEntity product) {
        new AlertDialog.Builder(getContext())
                .setTitle("Hapus produk")
                .setMessage("Apakah kamu yakin ingin menghapus catatan " + product.getName() + "?")
                .setNeutralButton("Batal", null)
                .setPositiveButton("Ya", (dialogInterface, i) -> {
                        viewModel.deleteProduct(product);
                        mainCallback.backToHome(false);
                }).create().show();
    }

    private void setExpiryDateFieldVisibility(boolean isOpened){
        binding.tilExpiryDate.setEnabled(!isOpened);
        binding.tilOpenedDate.setEnabled(isOpened);
        binding.tilPao.setEnabled(isOpened);
    }

    private List<ReminderEntity> setReminder(ProductEntity product) {
        return new ArrayList<>();
        // TODO: hidupkan notifikasi
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainCallback = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) requireActivity()).setContainerBackground(false);
    }
}