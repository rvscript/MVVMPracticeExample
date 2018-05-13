package com.rvscript.viewmodelproject.home.listfragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rvscript.viewmodelproject.R;
import com.rvscript.viewmodelproject.details.DetailsFragment;
import com.rvscript.viewmodelproject.home.RepoListAdapter;
import com.rvscript.viewmodelproject.home.RepoSelectedListener;
import com.rvscript.viewmodelproject.home.SelectedRepoViewModel;
import com.rvscript.viewmodelproject.model.Repo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListFragment extends Fragment implements RepoSelectedListener {
    private Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView listview;
    @BindView(R.id.tv_error)
    TextView textView_error;
    @BindView(R.id.progressbar)
    View progressBar;
    private ListViewModel viewModel;

    public ListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.screen_list, container, false);

        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*
        This is how we reference the viewModel
        If the viewModel has already been created it will reference that instance
        rather than create a new one. State is maintained in the first instance of the view model
         */
        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        observeViewModel();

        //set up our recycler view
        //This is done after building pojos, viewmodel, item_xml, adapter class, view holder,
        //Finish implemented methods of adapter class and finally to fragment and set up of view
        listview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //repoSelectedListener added as a parameter as last this
        listview.setAdapter(new RepoListAdapter(viewModel, this, this));
        listview.setLayoutManager(new LinearLayoutManager(getContext()));
        //REM: Add Network permissions to our manifest
    }

    /*
    ViewModelProviders.of(getActivity()).get(SelectedRepoViewModel.class) is how we can access a
    viewModel with multiple fragments.
    The scope is getActivity()
     */
    @Override
    public void onRepoSelected(Repo repo) {
        SelectedRepoViewModel selectedRepoViewModel =
                ViewModelProviders.of(getActivity()).get(SelectedRepoViewModel.class);
        selectedRepoViewModel.setSelectedRepo(repo);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new DetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void observeViewModel() {
        //before Lambdas
        /*
        created anonymous single class that has a single method
        that has a single value
         */
        viewModel.getRepos().observe(this, repos -> {
            if (repos != null) {
                listview.setVisibility(View.VISIBLE);
                listview.setHasFixedSize(true);
            }
        });

        viewModel.getError().observe(this, isError -> {
            if (isError) {
                textView_error.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                textView_error.setText(R.string.api_error_repos);
            } else {
                textView_error.setVisibility(View.GONE);
                textView_error.setText(null);
            }

        });

        viewModel.getLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

}
