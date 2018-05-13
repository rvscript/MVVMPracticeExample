package com.rvscript.viewmodelproject.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.rvscript.viewmodelproject.model.Repo;
import com.rvscript.viewmodelproject.networking.RepoApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectedRepoViewModel extends ViewModel {
    private final MutableLiveData<Repo> selectedRepo = new MutableLiveData<>();
    private Call<Repo> repoCall;
    private final String TAG = getClass().getSimpleName();

    public MutableLiveData<Repo> getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(Repo repo){
        selectedRepo.setValue(repo);
    }

    public void restoreFromBundle(Bundle savedInstanceState) {
        /*
        We re only doing this if we don't already have a selected RepoSet
         */
        if(selectedRepo.getValue() == null){
            if(savedInstanceState != null && savedInstanceState.containsKey("repo_details")){
                loadRepo(savedInstanceState.getStringArray("repo_details"));
            }
        }
    }

    private void loadRepo(String[] repo_details) {
        repoCall = RepoApi.getInstance().getRepo(repo_details[0], repo_details[1]);
        repoCall.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                selectedRepo.setValue(response.body());
                repoCall = null;
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e(TAG, "Error loading repo: ",  t);
                repoCall = null;
            }
        });
    }

    public void saveToBundle(Bundle outState) {
        if(selectedRepo.getValue() != null){
            outState.putStringArray("repo_details",
                    new String[]{
                    selectedRepo.getValue().owner.login, selectedRepo.getValue().name
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(repoCall !=null){
            repoCall.cancel();
            repoCall = null;
        }
    }
}
