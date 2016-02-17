package com.brahmand;

import com.jakewharton.rxrelay.PublishRelay;
import rx.Subscriber;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

/**
 * Created by adarshpandey on 1/4/16.
 */
public class RxRelay {
    static int i = 0;
    public static void main(String... args) throws InterruptedException {

        PublishRelay<String> l = PublishRelay.create();
        Subscription subscription = l.delay(2, TimeUnit.SECONDS).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed ====");
            }

            @Override
            public void onError(Throwable e) {
                l.call("Error");
            }

            @Override
            public void onNext(String s) {
                System.out.println("--- : " + s);
                i++;
                if (i == 2) {
                    throw new RuntimeException("som");
                }
                l.call(s);
            }
        });

        l.call("Hello ");

        Thread.sleep(10000);
        l.call("2Hello");
        Thread.sleep(5000);
        System.out.println("isUnSubscribed: " + subscription.isUnsubscribed());
        subscription.unsubscribe();

        Thread.sleep(5000);
    }
}
