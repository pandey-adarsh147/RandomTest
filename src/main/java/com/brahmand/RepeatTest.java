package com.brahmand;

import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adarshpandey on 12/22/15.
 */
public class RepeatTest {

    public static void main(String... args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Observable.just("Hello hell")
                .flatMap(s -> {
                    atomicInteger.set(atomicInteger.get() + 1);
                    return Observable.just("Hasheena ka pasheena").repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                        @Override
                        public Observable<?> call(Observable<? extends Void> observable) {
                            return null;
                        }
                    });
                })
                .subscribe(System.out::println);
    }
}
