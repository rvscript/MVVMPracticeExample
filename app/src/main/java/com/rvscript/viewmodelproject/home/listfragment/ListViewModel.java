package com.rvscript.viewmodelproject.home.listfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.rvscript.viewmodelproject.model.Repo;
import com.rvscript.viewmodelproject.networking.RepoApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListViewModel extends ViewModel {
    private final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
    //For retrofit when calling api
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    //For retrofit when calling api
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    //our local list of repos
    private Call<List<Repo>> repoCall;


    //In our constructor we will automatically make our API call
    public ListViewModel(){

        fetchRepos();
    }

    /*
    Expose all of our live data fields with getters
    They will be exposed as liveData
    That means the view will not be able to set the value on them
    and that is the difference between mutable live data and live data
     */

    public LiveData<List<Repo>> getRepos(){
        return repos;
    }

    LiveData<Boolean> getError(){
        return repoLoadError;
    }

    LiveData<Boolean> getLoading(){
        return loading;
    }

    private void fetchRepos() {
        loading.setValue(true);
        /*
        RepoApi.getInstance().getRepositories() comes from
        RepoApi instance that creates a retrofit call and applies
        reposervice getRepositories.
        The result is a <List<Repo>>
         */
        repoCall = RepoApi.getInstance().getRepositories();
        repoCall.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                repoLoadError.setValue(false);
                repos.setValue(response.body());
                loading.setValue(false);
                repoCall = null;
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e(getClass().getSimpleName(), "Error loading repos", t);
                repoLoadError.setValue(true);
                loading.setValue(false);
                repoCall = null;
            }
        });
    }

    //our cleanup method
    @Override
    protected void onCleared() {
        if(repoCall != null){
            repoCall.cancel();
            repoCall = null;
        }
    }
}
