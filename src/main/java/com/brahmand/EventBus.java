package com.brahmand;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.Subject;

/**
 * Created by adarshpandey on 11/30/15.
 */
public interface EventBus {

    <E> void publish(Queue<E> paramQueue, E paramE);

    <E> Action0 publishAction0(Queue<E> paramQueue, E paramE);

    <E, T> Action1<T> publishAction1(Queue<E> paramQueue, E paramE);

    <E> Subject<E, E> queue(Queue<E> paramQueue);

    <E> Subscription subscribe(Queue<E> paramQueue, Subscriber<E> paramP);

    <E> Subscription subscribeImmediate(Queue<E> paramQueue, Subscriber<E> paramP);

}
