package com.example;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

/**
 * 通知するデータを制限するオペレータ
 */
public class Section3Test {
	/**
	 * 指定した条件のデータのみ通知する
	 */
	@Test
	public void P216_filterのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.filter(data -> data % 2 == 0);
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	/**
	 * 一度通知したデータと同じデータは通知しない
	 */
	@Test
	public void P219_distinctのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "a", "B", "b", "A", "a", "B", "b").distinct();
		flux.log().subscribe();
	}

	@Test
	public void P220_distinctのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "a", "B", "b", "A", "a", "B", "b")
				.distinct(data -> data.toLowerCase());
		flux.log().subscribe();
	}

	/**
	 * 連続して重複したデータは通知しない
	 */
	@Test
	public void P222_distinctUntilChangedのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "a", "a", "A", "a").distinctUntilChanged();
		flux.log().subscribe();
	}

	@Test
	public void P223_distinctUntilChangedのサンプル() throws Exception {
		Flux<String> flux = Flux.just("1", "1.0", "0.1", "0.10", "1")
				.distinctUntilChanged(data -> new BigDecimal(data) {
					@Override
					public boolean equals(Object x) {
						return this.compareTo((BigDecimal) x) == 0;
					}
				});
		flux.log().subscribe();
	}

	/**
	 * 指定したデータ数や期間までデータを通知する
	 */
	@Test
	public void P225_takeのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(1000)).take(3);
		flux.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * 指定した条件になるまでデータを通知する
	 */
	@Test
	public void P227_takeUntilのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.takeUntil(data -> data == 3);
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	@Test
	public void P228_takeUntilのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.takeUntilOther(Mono.delay(Duration.ofMillis(1000)));
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 指定した条件の間だけ、データを通知する
	 */
	@Test
	public void P230_takeWhileのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.takeWhile(data -> data != 3);
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 最後から数えて指定した範囲のデータを通知する
	 */
	@Test
	public void P232_takeLastのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(800)).take(5).takeLast(2);
		flux.log().subscribe();
		Thread.sleep(5000);
	}

	@Test
	public void P253_takeLastのサンプル() throws Exception {
		// FluxにtakeLast(count, time, unit)相当のメソッドはない
	}

	/**
	 * 最初に通知されるデータを指定した範囲だけを除く
	 */
	@Test
	public void P235_skipのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(1000)).skip(2);
		flux.log().subscribe();
		Thread.sleep(5000);
	}

	/**
	 * 引数のPublisherがデータを通知するまでデータを通知しない
	 */
	@Test
	public void P236_skipUntilのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.skipUntilOther(Mono.delay(Duration.ofMillis(1000)));
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 指定した条件の間だけ、データを通知しない
	 */
	@Test
	public void P239_skipWhileのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300))
				.skipWhile(data -> data != 3);
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 最後から数えて指定した範囲のデータを通知しない
	 */
	@Test
	public void P241_skipLastのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(1000)).take(5).skipLast(2);
		flux.log().subscribe();
		Thread.sleep(6000);
	}

	/**
	 * データの通知後の指定した期間は他のデータを通知させない
	 */
	@Test
	public void P242_throttleFirstのサンプル() throws Exception {
		// FluxにthrottleFirstはない
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300)).take(10)
				.sampleFirst(Duration.ofMillis(1000));
		flux.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * 指定した間隔の中で最後に通知されるデータのみを通知する
	 */
	@Test
	public void P245_throttleLastのサンプル() throws Exception {
		// FluxにthrottleLastはない
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300)).take(9)
				.sample(Duration.ofMillis(1000));
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	@Test
	public void P246_sampleのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300)).take(9)
				.sample(Flux.interval(Duration.ofMillis(1000)));
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	/**
	 * データを受け取ってから指定した期間に次のデータを受け取らなければそのデータを通知する
	 */
	@Test
	public void P248_throttleWithTimeoutのサンプル() throws Exception {
		Flux<String> flux = Flux.<String>create(sink -> {
			try {
				sink.next("A");
				Thread.sleep(1000);
				sink.next("B");
				Thread.sleep(300);
				sink.next("C");
				Thread.sleep(300);
				sink.next("D");
				Thread.sleep(1000);
				sink.next("E");
				Thread.sleep(100);
				sink.complete();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, FluxSink.OverflowStrategy.BUFFER)
				.sampleTimeout(s -> Mono.delay(Duration.ofMillis(500)));
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	@Test
	public void P250_debounceのサンプル() throws Exception {
		P248_throttleWithTimeoutのサンプル(); // 同じ
	}

	/**
	 * 指定したインデックスのデータのみを通知する
	 */
	@Test
	public void P252_elementAtのサンプル() throws Exception {
		Mono<Long> mono = Flux.interval(Duration.ofMillis(100)).elementAt(3);
		mono.log().subscribe();
		Thread.sleep(1000);
	}
}
