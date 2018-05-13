package com.rvscript.viewmodelproject.model;

import com.squareup.moshi.Json;

/**
 * Created by ga-mlsdiscovery on 4/5/18.
 */

public class Repo {
    public final long id;
    public final String name;
    public final String description;
    public final User owner;
    @Json(name = "stargazers_count")
    public final long stars;
    @Json(name = "forks_count")
    public final long forks;

    public Repo(long id, String name, String description, User owner, long stars, long forks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.stars = stars;
        this.forks = forks;
    }
}
