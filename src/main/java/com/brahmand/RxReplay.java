package com.brahmand;

import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.SerialSubscription;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by adarshpandey on 12/9/15.
 */
public class RxReplay {

    static PublishSubject<String> subject = PublishSubject.create();

    public static void main(String... args) throws InterruptedException {
        /*Observable<String> a = Observable.just("Helo", "hell");
        ConnectableObservable<String> b = a.replay();

        b.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Sub1: " + s);
            }
        });


        b.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Sub2: " + s);
            }
        });

        b.connect();

        Thread.sleep(2000);

        Thread.sleep(2000);

        b.connect();
        Thread.sleep(2000);*/






        subject.nest().lift(new OperatorRetryWithPredicate<String>(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return ("main".equals(s));
            }
        })).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                System.out.println(s);
            }
        });

        subject.onNext("hello");

        Thread.sleep(5000);
    }

    public static final class OperatorRetryWithPredicate<T> implements Observable.Operator<T, Observable<T>> {
        private Func1<T, Boolean> predicate;

        public OperatorRetryWithPredicate(Func1<T, Boolean> predicate) {
            this.predicate = predicate;
        }

        @Override
        public Subscriber<? super Observable<T>> call(Subscriber<? super T> child) {
            final Scheduler.Worker inner = Schedulers.trampoline().createWorker();
            child.add(inner);

            final SerialSubscription serialSubscription = new SerialSubscription();
            // add serialSubscription so it gets unsubscribed if child is unsubscribed
            child.add(serialSubscription);
            ProducerArbiter pa = new ProducerArbiter();
            child.setProducer(pa);
            return new SourceSubscriber<T>(child, predicate, inner, serialSubscription, pa);
        }


    }

    static final class SourceSubscriber<T> extends Subscriber<Observable<T>> {
        final Subscriber<? super T> child;
        final Func1<T, Boolean> predicate;
        final Scheduler.Worker inner;
        final SerialSubscription serialSubscription;
        final ProducerArbiter pa;

        volatile int attempts;
        @SuppressWarnings("rawtypes")
        static final AtomicIntegerFieldUpdater<SourceSubscriber> ATTEMPTS_UPDATER
                = AtomicIntegerFieldUpdater.newUpdater(SourceSubscriber.class, "attempts");

        public SourceSubscriber(Subscriber<? super T> child,
                                final Func1<T, Boolean> predicate,
                                Scheduler.Worker inner,
                                SerialSubscription serialSubscription,
                                ProducerArbiter pa) {
            this.child = child;
            this.predicate = predicate;
            this.inner = inner;
            this.serialSubscription = serialSubscription;
            this.pa = pa;
        }


        @Override
        public void onCompleted() {
            // ignore as we expect a single nested Observable<T>
        }

        @Override
        public void onError(Throwable e) {
            child.onError(e);
        }

        @Override
        public void onNext(final Observable<T> o) {
            inner.schedule(new Action0() {

                @Override
                public void call() {
                    final Action0 _self = this;
                    ATTEMPTS_UPDATER.incrementAndGet(SourceSubscriber.this);

                    // new subscription each time so if it unsubscribes itself it does not prevent retries
                    // by unsubscribing the child subscription
                    Subscriber<T> subscriber = new Subscriber<T>() {
                        boolean done;
                        @Override
                        public void onCompleted() {
                            if (!done) {
                                done = true;
                                child.onCompleted();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!done) {
                                done = true;
                                /*if (predicate.call(attempts, e) && !inner.isUnsubscribed()) {
                                    // retry again
                                    inner.schedule(_self);
                                } else {
                                    // give up and pass the failure
                                    child.onError(e);
                                }*/
                                child.onError(e);
                            }
                        }

                        @Override
                        public void onNext(T v) {
                            if (predicate.call(v) && !inner.isUnsubscribed()) {
                                // retry again
                                inner.schedule(_self, 2, TimeUnit.SECONDS);
                            } else if (!done) {
                                child.onNext(v);
                                pa.produced(1);
                            }
                        }

                        @Override
                        public void setProducer(Producer p) {
                            pa.setProducer(p);
                        }
                    };
                    // register this Subscription (and unsubscribe previous if exists)
                    serialSubscription.set(subscriber);
                    o.unsafeSubscribe(subscriber);
                }
            });
        }
    }
}
