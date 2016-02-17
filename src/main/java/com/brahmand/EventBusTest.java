package com.brahmand;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by adarshpandey on 11/30/15.
 */
public class EventBusTest {

    public static void main(String... args) throws InterruptedException {
        EventBus eventBus = new DefaultEventBus(Schedulers.computation());

        Queue<ProgressEvent> progressEventQueue = Queue.of(ProgressEvent.class).replay().get();

        eventBus.publish(progressEventQueue, new ProgressEvent(2, 43));
        eventBus.publish(progressEventQueue, new ProgressEvent(2, 43));

        eventBus.subscribe(progressEventQueue, new Subscriber<ProgressEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ProgressEvent progressEvent) {
                System.out.println(progressEvent.progress + " -- " + progressEvent.secondaryProgress);
            }
        });

        eventBus.publish(progressEventQueue, new ProgressEvent(32, 43));

        Thread.sleep(2000);

        eventBus.subscribe(progressEventQueue, new Subscriber<ProgressEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ProgressEvent progressEvent) {
                System.out.println("2nd : " + progressEvent.progress + " -- " + progressEvent.secondaryProgress);
            }
        });

        Thread.sleep(2000);
    }
}
