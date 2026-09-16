package com.name.replace.presenter;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface GetJokePresenter extends BasePresent {
    void like(String id);
    void unlike(String id);
    void getJoke();
}
