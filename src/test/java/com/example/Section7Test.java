package com.example;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * ユーティリティ系オペレータ
 */
public class Section7Test {
	/**
	 * データの通知を最初から繰り返す
	 */
	@Test
	public void P293_repeatのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "B", "C").repeat(2);
		flux.log().subscribe();
	}

	/**
	 * 指定した条件がtrueになるまでデータの通知を繰り返す
	 */
	@Test
	public void P295_repeatUntilのサンプル() throws Exception {
		// FluxにrepeatUntilはない
		final long startTime = System.currentTimeMillis();
		Flux<Long> flux = Flux.interval(Duration.ofMillis(100)).take(3).repeat(() -> {
			System.out.println("called");
			return System.currentTimeMillis() - startTime > 500;
		});
		flux.log().subscribe();
		Thread.sleep(1000L);
	}

	/**
	 * 繰り返し処理を定義して通知を繰り返す
	 */
	@Test
	public void P298_repeatWhenのサンプル() throws Exception {
		Flux<String> flux = Flux.just(1, 2, 3)
				.repeatWhen(completeHandler -> completeHandler
						.delayElements(Duration.ofMillis(1000)).take(2)
						.doOnNext(data -> System.out.println("emit: " + data))
						.doOnComplete(() -> System.out.println("complete")))
				.map(data -> {
					long time = System.currentTimeMillis();
					return time + "ms: " + data;
				});
		flux.log().subscribe();
		Thread.sleep(5000);
	}

	/**
	 * データ通知のタイミングを遅らせる
	 */
	@Test
	public void P302_delayのサンプル() throws Exception {
		// Fluxにdelay(初回の通知を遅延)がなく、delaySubscription(購読を遅延)とdelayElements(各データの通知を遅延)のみ
	}

	/**
	 * 処理の開始を遅らせる
	 */
	@Test
	public void P305_delaySubscriptionのサンプル() throws Exception {
		System.out.println("処理開始: " + System.currentTimeMillis());
		Flux<String> flux = Flux.<String>create(sink -> {
			System.out.println("購読開始: " + System.currentTimeMillis());
			sink.next("A");
			sink.next("B");
			sink.next("C");
			sink.complete();
		}, FluxSink.OverflowStrategy.BUFFER).delaySubscription(Duration.ofMillis(2000));
		flux.log().subscribe();
		Thread.sleep(5000);
	}

	/**
	 * データを通知するまでのタイムアウトする期限を設定する
	 */
	@Test
	public void P309_timoutのサンプル() throws Exception {
		Flux<Integer> flux = Flux.<Integer>create(sink -> {
			sink.next(1);
			sink.next(2);
			// しばらく待つ ※思い処理をしていると想定する
			try {
				Thread.sleep(1200);
			}
			catch (InterruptedException e) {
				sink.error(e);
				return;
			}
			sink.next(3);
			sink.complete();
		}, FluxSink.OverflowStrategy.BUFFER).timeout(Duration.ofMillis(1000));

		flux.log().subscribe();
		Thread.sleep(2000);
	}
}
