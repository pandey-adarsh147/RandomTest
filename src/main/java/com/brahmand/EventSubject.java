package com.brahmand;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by adarshpandey on 11/30/15.
 */
public class EventSubject<T> extends Subject<T, T> {

    @NotNull
    private final Action1<Throwable> onError;
    private final Subject<T, T> wrappedSubject;


    protected EventSubject(OnSubscribe<T> onSubscribe, Subject<T, T> subject, Action1<Throwable> onError) {
        super(onSubscribe);
        this.wrappedSubject = subject;
        if (onError == null) {
            onError = new DefaultErrorHandler();
        }

        this.onError = onError;
    }

    public static <T> EventSubject<T> create() {
        PublishSubjectOnSubscribe<T> localPublishSubjectOnSubscribe = new PublishSubjectOnSubscribe<>();
        return new EventSubject<>(localPublishSubjectOnSubscribe, localPublishSubjectOnSubscribe.subject, null);
    }

    public static <T> EventSubject<T> create(@Nullable Action1<Throwable> paramb) {
        PublishSubjectOnSubscribe<T> localPublishSubjectOnSubscribe = new PublishSubjectOnSubscribe<>();
        return new EventSubject<>(localPublishSubjectOnSubscribe, localPublishSubjectOnSubscribe.subject, paramb);
    }

    public static <T> EventSubject<T> replaying() {
        BehaviorSubjectOnSubscribe<T> localBehaviorSubjectOnSubscribe = new BehaviorSubjectOnSubscribe<>(null);
        return new EventSubject<>(localBehaviorSubjectOnSubscribe, localBehaviorSubjectOnSubscribe.subject, null);
    }

    public static <T> EventSubject<T> replaying(@Nullable T paramT, @Nullable Action1<Throwable> paramb) {
        BehaviorSubjectOnSubscribe<T> localBehaviorSubjectOnSubscribe = new BehaviorSubjectOnSubscribe<>(paramT);
        return new EventSubject<>(localBehaviorSubjectOnSubscribe, localBehaviorSubjectOnSubscribe.subject, paramb);
    }

    public final boolean hasObservers() {
        return this.wrappedSubject.hasObservers();
    }

    public final void onCompleted() {
    }

    public final void onError(Throwable paramThrowable) {
        this.onError.call(paramThrowable);
    }

    public final void onNext(T paramT) {
        this.wrappedSubject.onNext(paramT);
    }

    private static final class BehaviorSubjectOnSubscribe<T>
            implements Observable.OnSubscribe<T> {
        private final BehaviorSubject<T> subject;

        BehaviorSubjectOnSubscribe(@Nullable T paramT) {
            if (paramT == null) {
                this.subject = BehaviorSubject.create();
                return;
            }
            this.subject = BehaviorSubject.create(paramT);
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            this.subject.subscribe(subscriber);
        }
    }

    private static final class DefaultErrorHandler
            implements Action1<Throwable> {
        public final void call(Throwable paramThrowable) {
            paramThrowable.printStackTrace();
        }
    }

    private static final class PublishSubjectOnSubscribe<T>
            implements OnSubscribe<T> {
        private final PublishSubject<T> subject = PublishSubject.create();

        @Override
        public void call(Subscriber<? super T> subscriber) {
            this.subject.subscribe(subscriber);
        }
    }
}
