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

import com.xdev.expy.data.source.remote.entity.ProductEntity;
import com.xdev.expy.databinding.FragmentMonitoredBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.ui.main.MainCallback;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.utils.ShimmerHelper;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.showToast;

public class MonitoredFragment extends Fragment implements ProductAdapter.ProductAdapterClickListener {

    private FragmentMonitoredBinding binding;
    private MainCallback mainCallback;

    public MonitoredFragment() {}

    public static MonitoredFragment newInstance() {
        return new MonitoredFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMonitoredBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter adapter = new ProductAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        ShimmerHelper shimmer = new ShimmerHelper(binding.shimmer, binding.recyclerView, binding.layoutEmpty.getRoot());

        if (getActivity() != null) {
            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
            MainViewModel viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);
            viewModel.getMonitoredProducts().observe(requireActivity(), result -> {
                switch (result.status) {
                    case LOADING:
                        shimmer.show();
                        break;
                    case SUCCESS:
                        adapter.submitList(result.body);
                        shimmer.hide(false);
                        break;
                    case EMPTY:
                        shimmer.hide(true);
                        break;
                    case ERROR:
                        // TODO: ganti isi pesannya (beri tahu kalau gagal)
                        showToast(getContext(), result.message);
                        shimmer.hide(true);
                        break;
                }
            });
            viewModel.addProductsSnapshotListener();
        }

        binding.fabAdd.setOnClickListener(v -> mainCallback.addUpdateProduct(new ProductEntity()));
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