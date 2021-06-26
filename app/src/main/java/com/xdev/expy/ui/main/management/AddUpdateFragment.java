package com.xdev.expy.ui.main.management;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.Timestamp;
import com.xdev.expy.R;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.local.entity.ReminderEntity;
import com.xdev.expy.databinding.FragmentAddUpdateBinding;
import com.xdev.expy.reminder.ReminderReceiver;
import com.xdev.expy.textwatcher.ExpiryDateTextWatcher;
import com.xdev.expy.textwatcher.OpenedDateTextWatcher;
import com.xdev.expy.textwatcher.PaoTextWatcher;
import com.xdev.expy.textwatcher.ProductNameTextWatcher;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.ui.main.MainCallback;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.viewmodel.ViewModelFactory;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.xdev.expy.utils.AppUtils.showToast;
import static com.xdev.expy.utils.DateUtils.DATE_FORMAT;
import static com.xdev.expy.utils.DateUtils.addDay;
import static com.xdev.expy.utils.DateUtils.differenceOfDates;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class AddUpdateFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = AddUpdateFragment.class.getSimpleName();
    private static final String NOTIFICATION_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final String EXPIRY_DATE_PICKER = "expiry_date_picker";
    private static final String OPENED_DATE_PICKER = "opened_date_picker";

    private static final String ARG_PRODUCT = "product";

    private FragmentAddUpdateBinding binding;
    private MainCallback mainCallback;
    private MainViewModel viewModel;
    private ProductEntity product;
    private ReminderReceiver reminderReceiver;

    private boolean isUpdate = false;
    private String expiryDate = "";
    private String openedDate = "";

    public AddUpdateFragment() {}

    @NonNull
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

        reminderReceiver = new ReminderReceiver();

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);

        binding.tvHelpPao.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.edtOpenedDate.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);
        binding.switchOpened.setOnCheckedChangeListener(this);
        binding.switchFinished.setOnCheckedChangeListener(this);

        isUpdate = !product.getId().isEmpty() ;
        if (isUpdate){
            binding.toolbarTitle.setText(R.string.title_update_product);
            binding.btnDelete.setVisibility(View.VISIBLE);
            expiryDate = product.getExpiryDate();
            openedDate = product.getOpenedDate();
            binding.edtName.setText(product.getName());
            binding.edtExpiryDate.setText(getFormattedDate(expiryDate, false));
            binding.edtOpenedDate.setText(getFormattedDate(openedDate, false));
            binding.edtPao.setText(String.valueOf(product.getPao()));
            binding.switchOpened.setChecked(product.getIsOpened());
            binding.switchReminder.setChecked(!product.getReminders().isEmpty());
            binding.switchFinished.setChecked(product.getIsFinished());
        } else {
            binding.toolbarTitle.setText(R.string.title_add_product);
            binding.btnDelete.setVisibility(View.GONE);
        }

        binding.edtName.addTextChangedListener(new ProductNameTextWatcher(getContext(), binding.tilName));
        binding.edtExpiryDate.addTextChangedListener(new ExpiryDateTextWatcher(getContext(), binding.tilExpiryDate, binding.switchOpened));
        binding.edtOpenedDate.addTextChangedListener(new OpenedDateTextWatcher(getContext(), binding.tilOpenedDate, binding.switchOpened));
        binding.edtPao.addTextChangedListener(new PaoTextWatcher(getContext(), binding.tilPao, binding.switchOpened));

        setExpiryDateFieldVisibility(product.getIsOpened());
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == binding.tvHelpPao.getId()) {
            HelpPAOFragment.newInstance().show(getChildFragmentManager(), HelpPAOFragment.TAG);
        } else if (id == binding.edtExpiryDate.getId()) {
            showDatePicker(EXPIRY_DATE_PICKER, getContext(), expiryDate);
        } else if (id == binding.edtOpenedDate.getId()) {
            showDatePicker(OPENED_DATE_PICKER, getContext(), openedDate);
        } else if (id == binding.btnSave.getId()) {
            saveProduct(getContext(), product, isUpdate);
        } else if (id == binding.btnDelete.getId()) {
            deleteProduct(getContext(), product);
        }
    }

    public void showDatePicker(String tag, Context context, String initialDate) {
        Calendar calendar = Calendar.getInstance();
        if (initialDate != null && !initialDate.isEmpty()) {
            calendar.setTime(stringToDate(DATE_FORMAT, initialDate));
        }

        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            if(view.isShown()){
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                if (tag.equals(EXPIRY_DATE_PICKER)) {
                    expiryDate = simpleDateFormat.format(selectedDate.getTime());
                    binding.edtExpiryDate.setText(getFormattedDate(expiryDate, false));
                } else if (tag.equals(OPENED_DATE_PICKER)) {
                    openedDate = simpleDateFormat.format(selectedDate.getTime());
                    binding.edtOpenedDate.setText(getFormattedDate(openedDate, false));
                }
            }
        }, initialYear, initialMonth , initialDay);
        datePickerDialog.getDatePicker().setMinDate(stringToDate(DATE_FORMAT, "2000/01/01").getTime());
        datePickerDialog.getDatePicker().setMaxDate(stringToDate(DATE_FORMAT, "9999/12/31").getTime());
        datePickerDialog.show();
    }

    private void saveProduct(Context context, ProductEntity product, boolean isUpdate) {
        if (binding.edtName.getText() == null || binding.edtPao.getText() == null) return;

        String name = binding.edtName.getText().toString();
        String expiryDate = this.expiryDate;
        String openedDate = this.openedDate;
        String pao = binding.edtPao.getText().toString();
        boolean isOpened = binding.switchOpened.isChecked();
        boolean isReminderTurnedOn = binding.switchReminder.isChecked();
        boolean isFinished = binding.switchFinished.isChecked();

        if (!isValidForm(name, expiryDate, openedDate, pao, isOpened)) {
            showToast(getContext(), getResources().getString(R.string.toast_empty_fields));
            return;
        }

        if (isOpened) expiryDate = addDay(openedDate, Integer.parseInt(pao)*30);
        else {
            openedDate = "";
            pao = "0";
        }

        if (isUpdate) {
            if (!isReminderTurnedOn) cancelAllReminders(context, product);
        } else {
            String id = viewModel.getProductsReference().document().getId();
            product.setId(id);
        }

        product.setName(name);
        product.setExpiryDate(expiryDate);
        product.setOpenedDate(openedDate);
        product.setPao(Integer.parseInt(pao));
        product.setIsOpened(isOpened);
        product.setIsFinished(isFinished);

        List<ReminderEntity> reminderList = new ArrayList<>();
        if (isReminderTurnedOn && !isFinished) reminderList = setReminders(context, product);
        product.setReminders(reminderList);

        if (isUpdate) viewModel.updateProduct(product);
        else viewModel.insertProduct(product);
        mainCallback.backToHome(false);
    }

    private boolean isValidForm(@NonNull String name, String expiryDate, String openedDate, String pao, boolean isOpened){
        return !((name.isEmpty()) || ((isOpened && (openedDate.isEmpty() || pao.isEmpty())) || (!isOpened && (expiryDate.isEmpty())))) &&
                binding.tilName.getError() == null &&
                binding.tilExpiryDate.getError() == null &&
                binding.tilOpenedDate.getError() == null &&
                binding.tilPao.getError() == null;
    }

    private void deleteProduct(Context context, @NonNull ProductEntity product) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_title_delete_product)
                .setMessage(getResources().getString(R.string.dialog_message_delete_product, product.getName()))
                .setNeutralButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    cancelAllReminders(context, product);
                    viewModel.deleteProduct(product);
                    mainCallback.backToHome(false);
                }).create().show();
    }

    private void cancelAllReminders(Context context, @NonNull ProductEntity product) {
        for (ReminderEntity reminder : product.getReminders())
            reminderReceiver.cancelReminder(context, reminder.getId());
    }

    @Override
    public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();
        if (id == binding.switchOpened.getId()){
            setExpiryDateFieldVisibility(checked);
        } else if (id == binding.switchFinished.getId()){
            setReminderSwitchEnability(checked);
        }
    }

    private void setExpiryDateFieldVisibility(boolean isOpened){
        binding.tilExpiryDate.setEnabled(!isOpened);
        binding.tilOpenedDate.setEnabled(isOpened);
        binding.tilPao.setEnabled(isOpened);
        binding.edtExpiryDate.setEnabled(!isOpened);
        binding.edtOpenedDate.setEnabled(isOpened);
        binding.edtPao.setEnabled(isOpened);
    }

    private void setReminderSwitchEnability(boolean isFinished) {
        if (isFinished) binding.switchReminder.setChecked(false);
        binding.switchReminder.setEnabled(!isFinished);
        binding.tvReminder.setEnabled(!isFinished);
    }

    @NonNull
    private List<ReminderEntity> setReminders(Context context, @NonNull ProductEntity product) {
        if (product.getId().isEmpty()) throw new RuntimeException("Product id is empty");

        List<ReminderEntity> reminderList = new ArrayList<>();
        String expiryDate = product.getExpiryDate();

        int dte = (int) differenceOfDates(expiryDate, getCurrentDate());
        Log.d(TAG, "Days to expiration = " + dte);

        List<Integer> notificationDayList = new ArrayList<>();
        if (dte >= 0) notificationDayList.add(0);
        if (dte >= 1) notificationDayList.add(1);
        if (dte >= 2) notificationDayList.add(2);
        if (dte >= 3) notificationDayList.add(3);
        if (dte >= 7) notificationDayList.add(7);
        if (dte >= 14) notificationDayList.add(14);
        if (dte >= 30) notificationDayList.add(30);
        if (dte >= 60) notificationDayList.add(60);
        if (dte >= 90) notificationDayList.add(90);
        if (dte >= 180) notificationDayList.add(180);
        Log.d(TAG, "Notification days = " + notificationDayList.toString());

        for (int notificationDay : notificationDayList){
            ReminderEntity reminder = new ReminderEntity();
            Date date = stringToDate(NOTIFICATION_DATE_FORMAT, addDay(expiryDate, -notificationDay) + " 12:00:00");
            reminder.setId((Math.abs(product.getId().hashCode()) + notificationDay) % Integer.MAX_VALUE);
            reminder.setTimestamp(new Timestamp(date));
            reminderList.add(reminder);
            Log.d(TAG, reminder.toString());

            String day;
            switch (notificationDay) {
                case 0:
                    day = getResources().getString(R.string.today);
                    break;
                case 1:
                    day = getResources().getString(R.string.tomorrow);
                    break;
                case 2:
                    day = getResources().getString(R.string.day_after_tomorrow);
                    break;
                case 3:
                    day = getResources().getString(R.string.number_of_days_remaining, notificationDay);
                    break;
                case 7:
                case 14:
                    day = getResources().getQuantityString(R.plurals.number_of_weeks_remaining, notificationDay/7, notificationDay/7);
                    break;
                default:
                    day = getResources().getQuantityString(R.plurals.number_of_months_remaining, notificationDay/30, notificationDay/30);
                    break;
            }

            String title = MessageFormat.format(getResources().getString(
                    R.string.notification_title_expiration_reminder, day, product.getName()), notificationDay);
            String message = getResources().getString(R.string.notification_message_expiration_reminder,
                    getFormattedDate(product.getExpiryDate(), false));
            reminderReceiver.setReminder(context, reminder.getId(), title, message, date);
        }

        return reminderList;
    }

    private Date stringToDate(String dateFormat, String date) {
        try {
            DateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
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