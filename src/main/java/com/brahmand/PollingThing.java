package com.brahmand;

import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by adarshpandey on 12/17/15.
 */
public class PollingThing {

    final static PublishSubject<Boolean> subject = PublishSubject.create();

    public static void main(String... args) {
        subject.subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });


    }
}
