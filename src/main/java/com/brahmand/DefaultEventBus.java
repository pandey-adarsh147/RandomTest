package com.brahmand;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.Subject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adarshpandey on 11/30/15.
 */
public class DefaultEventBus implements EventBus {

    private Scheduler defaultScheduler;
    private final Map<Integer, Subject<?, ?>> queues = new HashMap();


    public DefaultEventBus(Scheduler scheduler) {
        this.defaultScheduler = scheduler;
    }


    @Override
    public <E> void publish(Queue<E> queue, E paramE) {
        queue(queue).onNext(paramE);
    }

    @Override
    public <E> Action0 publishAction0(Queue<E> paramQueue, E paramE) {
        return () -> publish(paramQueue, paramE);
    }

    @Override
    public <E, T> Action1<T> publishAction1(Queue<E> paramQueue, E paramE) {
        return t -> publish(paramQueue, paramE);
    }

    @Override
    public <E> Subject<E, E> queue(Queue<E> paramQueue) {
        Subject localObject = this.queues.get(paramQueue.id);
        if (localObject == null) {
            if (!paramQueue.replayLast) {
                localObject = EventSubject.create(paramQueue.onError);
            } else {
                localObject = EventSubject.replaying(paramQueue.defaultEvent, paramQueue.onError);
            }
            this.queues.put(paramQueue.id, localObject);
        }

        return localObject;
    }

    @Override
    public <E> Subscription subscribe(Queue<E> paramQueue, Subscriber<E> paramP) {
        return queue(paramQueue).observeOn(this.defaultScheduler).subscribe(paramP);
    }

    @Override
    public <E> Subscription subscribeImmediate(Queue<E> paramQueue, Subscriber<E> paramP) {
        return queue(paramQueue).subscribe(paramP);
    }
}
