package com.rvscript.viewmodelproject.networking;

import com.rvscript.viewmodelproject.model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ga-mlsdiscovery on 4/5/18.
 */

public interface RepoService {

    @GET("orgs/Google/repos")
    Call<List<Repo>> getRepositories();

    @GET("repos/{owner}/{name}")
    Call<Repo>getRepo(@Path("owner")String repoOwner, @Path("name")String repoName);
}
