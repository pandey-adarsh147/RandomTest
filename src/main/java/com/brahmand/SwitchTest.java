package com.brahmand;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by adarshpandey on 11/30/15.
 */
public class SwitchTest {
    public static void main(String... args) throws InterruptedException {
        /*PublishSubject<Observable<String>> subject = PublishSubject.create();
        Observable<String> integerObservable = Observable.just("12", "3", "4", "5");

        Observable.switchOnNext(subject).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        subject.onNext(integerObservable);*/

        /*subject.nest().lift(new BreakChain<String>(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String o) {
                return null;
            }
        }))*/

        PublishSubject<Integer> retrySubject = PublishSubject.create();
        PublishSubject<Integer> timerSubject = PublishSubject.create();
        /*retrySubject.delay(2, TimeUnit.SECONDS)
                .zipWith(Observable.just("Things"), (integer, s) -> integer)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer >= 2) {
                            System.out.println(integer);
                        } else {
                            retrySubject.onNext(integer + 1);
                        }
                    }
                });*/
        /*timerSubject.delay(2, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                retrySubject.onNext(integer);
            }
        });

        retrySubject.delay(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                        if (integer >= 2) {
                            System.out.println(integer);
                        } else {
                            Observable.just("Things").subscribe(o -> {
                                retrySubject.onNext(integer + 1);
                            });
                        }

                    }
                });

        retrySubject.onNext(1);*/

        Observable<CredentialsWithTimestamp> credentialsProvider = Observable.just(new CredentialsWithTimestamp("credentials", 1434873025320L)); // replace with your implementation

        Observable<String> o = credentialsProvider.flatMap(credentialsWithTimestamp -> {
            // side effect variable
            AtomicLong timestamp = new AtomicLong(credentialsWithTimestamp.timestamp); // computational steering (inc. initial value)
            return Observable.just(credentialsWithTimestamp.credentials).delay(10, TimeUnit.SECONDS) // same credentials are reused for each request - if invalid / onError, the later retry() will be called for new credentials
                    .flatMap(credentials -> Observable.just("Jhakas"))  // this will use the value from previous doOnNext
                    .repeat();
        })
                .retry()
                .share();

        o.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("Kya baat hai: " + s);
            }
        });

        Thread.sleep(20000);

    }

    private static class CredentialsWithTimestamp {

        public final String credentials;
        public final long timestamp; // I assume this is necessary for you from the first request

        public CredentialsWithTimestamp(String credentials, long timestamp) {
            this.credentials = credentials;
            this.timestamp = timestamp;
        }
    }

    public static final class BreakChain<T> implements Observable.Operator<T, Observable<T>> {
        private Func1<T, Boolean> predicate;

        public BreakChain(Func1<T, Boolean> predicate) {
            this.predicate = predicate;
        }

        @Override
        public Subscriber<? super Observable<T>> call(Subscriber<? super T> child) {
            child.onCompleted();
//            return new SourceSubscriber<T>(child, predicate, inner, serialSubscription, pa);
            return null;
        }


    }
}
