package com.axreng.backend.server;

import java.util.concurrent.Flow;

public interface Observable<T> {
    void onSubscribe(Flow.Subscription subscription);
    void onNext(T item);
    void onError(Throwable throwable);
    void onComplete();
}
