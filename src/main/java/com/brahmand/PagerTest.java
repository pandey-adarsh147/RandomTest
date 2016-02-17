package com.brahmand;

import rx.Observable;

/**
 * Created by adarshpandey on 11/29/15.
 */
public class PagerTest {

    public static void main(String... args) {
        Observable<Integer> source = Observable.just(1, 2, 3);
        RxPager<Integer, Integer> pager =  RxPager.create(integer -> Observable.just(5, 6, 7, 8));

        pager.page(source).subscribe(System.out::println);

        pager.next();
        pager.next();
        pager.next();
        pager.next();

    }
}
