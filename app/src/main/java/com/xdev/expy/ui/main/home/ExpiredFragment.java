package com.xdev.expy.ui.main.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.R;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.databinding.FragmentExpiredBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.ui.main.MainCallback;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.utils.ShimmerHelper;
import com.xdev.expy.viewmodel.ViewModelFactory;

import org.jetbrains.annotations.Contract;

import static com.xdev.expy.utils.AppUtils.showToast;

public class ExpiredFragment extends Fragment implements ProductAdapter.ProductAdapterClickListener {

    private FragmentExpiredBinding binding;
    private MainCallback mainCallback;

    public ExpiredFragment() {}

    @NonNull
    @Contract(" -> new")
    public static ExpiredFragment newInstance() {
        return new ExpiredFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExpiredBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter adapter = new ProductAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        ShimmerHelper shimmer = new ShimmerHelper(binding.shimmer, binding.recyclerView,
                binding.layoutEmpty.getRoot(), binding.swipeRefresh);

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        MainViewModel viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);
        viewModel.getExpiredProducts().observe(requireActivity(), result -> {
            switch (result.status) {
                case LOADING:
                    shimmer.show();
                    break;
                case SUCCESS:
                    if (result.data != null) {
                        adapter.submitList(result.data);
                        shimmer.hide(result.data.isEmpty());
                    }
                    break;
                case ERROR:
                    showToast(getContext(), getResources().getString(R.string.toast_error_get_product));
                    shimmer.hide(true);
                    break;
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.fetchNow(true));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainCallback = (MainActivity) context;
    }

    @Override
    public void onProductClicked(ProductEntity product) {
        mainCallback.addUpdateProduct(product);
    }
}