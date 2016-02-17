package com.brahmand;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by adarshpandey on 10/17/15.
 */
public class RxTest {

    static class RxCombined {
        public Integer value;
        public List<String> sValue;

        @Override
        public String toString() {
            return "[" + sValue + " = " + value + "]";
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // observer will receive no onNext events because the subject.onCompleted() isn't called.
        /*AsyncSubject<Object> subject = AsyncSubject.create();
        subject.subscribe(observer);
        subject.onNext("one");
        subject.onNext("two");
        subject.onNext("three");*/

        // observer will receive "three" as the only onNext event.
        Observable<String> observable = Observable.from(new String[]{"1", "2", "3", "4", "5"});

        PublishSubject<String> subject = PublishSubject.create();
        subject.delay(2, TimeUnit.SECONDS).subscribe(s -> {
            System.out.println(s);
            subject.onNext(s + " hell");
            /*Observable.timer(2, TimeUnit.SECONDS).subscribe(s2-> {
                System.out.println(s);
                subject.onNext(s + " hell");
            });*/
        });

        subject.onNext("one");


        Thread.sleep(10000);




        /*Observable<Integer> observable = Observable.from(new Integer[]{1, 2, 3, 4, 5});


        Observable<String> stringObservable = observable.flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just("Hello" + integer);
            }
        });

        Observable.zip(observable.last(), stringObservable.toList(), new Func2<Integer, List<String>, Object>() {
            @Override
            public Object call(Integer integer, List<String> s) {
                RxCombined rxCombined = new RxCombined();
                rxCombined.sValue = s;
                rxCombined.value = integer;
                return rxCombined;
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println(o);
            }
        });
*/
        /*System.out.println("------------ mergingAsync");
        mergingAsync();
        System.out.println("------------ mergingSync");
        mergingSync();
        System.out.println("------------ mergingSyncMadeAsync");
        mergingSyncMadeAsync();
        System.out.println("------------ flatMapExampleSync");
        flatMapExampleSync();
        System.out.println("------------ flatMapExampleAsync");
        flatMapExampleAsync();
        System.out.println("------------Done all -------- ");*/
        /*CompositeSubscription compositeSubscription = new CompositeSubscription();

        Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        });

        Observable<Integer> stringObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        });
        Observable<Object> stringObservable1 = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                subscriber.onNext(3);
                subscriber.onCompleted();

            }
        });
        Observable<List<Integer>> stringObservable2 = stringObservable.zipWith(stringObservable1).toList();

        Observable.concat(integerObservable, stringObservable2).doOnNext(System.out::println)
                .subscribeOn(Schedulers.computation()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
*/




    }

    private static void mergingAsync() {
        Observable.merge(getDataAsync(1), getDataAsync(2)).toBlocking().forEach(System.out::println);
    }

    private static void mergingSync() {
        // here you'll see the delay as each is executed synchronously
        Observable.merge(getDataSync(1), getDataSync(2)).toBlocking().forEach(System.out::println);
    }

    private static void mergingSyncMadeAsync() {
        // if you have something synchronous and want to make it async, you can schedule it like this
        // so here we see both executed concurrently
        Observable.merge(getDataSync(1).subscribeOn(Schedulers.io()), getDataSync(2).subscribeOn(Schedulers.io())).toBlocking().forEach(System.out::println);
    }

    private static void flatMapExampleAsync() {
        Observable.range(0, 5).flatMap(i -> {
            return getDataAsync(i);
        }).toBlocking().forEach(System.out::println);
    }

    private static void flatMapExampleSync() {
        Observable.range(0, 5).flatMap(i -> {
            return getDataSync(i);
        }).toBlocking().forEach(System.out::println);
    }

    // artificial representations of IO work
    static Observable<Integer> getDataAsync(int i) {
        return getDataSync(i).subscribeOn(Schedulers.io());
    }

    static Observable<Integer> getDataSync(int i) {
        return Observable.create((Subscriber<? super Integer> s) -> {
            // simulate latency
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.onNext(i);
            s.onCompleted();
        });
    }

    /*public static void main(String... args) {

        Observable.just("Hello World")
                .observeOn(Schedulers.computation())
                .flatMap(s -> Observable.just("Adarsh", "Pandey"))
                .flatMap(s -> Observable.just(s + " - next"))
                .concatMap(Observable::just)
                .toList()
                .toBlocking()
                .forEach(s -> System.out.println("Hello " + s));

        *//*Observable.create( subscriber -> {


        });

        Observable<Integer> integerObservable = Observable.from(new Integer[]{Integer.valueOf(112)});

        Subscription subscription = integerObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Next");
            }
        });

        System.out.println(subscription.isUnsubscribed());

        String[] names = {"hello", "ben", "world"};

        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Hello " + s + "!");
            }

        });*//*
    }*/
}
