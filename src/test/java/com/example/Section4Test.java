package com.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

/**
 * Flux/Monoを結合するオペレータ
 */
public class Section4Test {
	/**
	 * 複数のFluxを一つにまとめ同時に実行する
	 */
	@Test
	public void P255_mergeのサンプル() throws Exception {
		Flux<Long> flux1 = Flux.interval(Duration.ofMillis(300)).take(5);
		Flux<Long> flux2 = Flux.interval(Duration.ofMillis(500)).take(2)
				.map(data -> data + 100);

		Flux<Long> flux = Flux.merge(flux1, flux2);
		flux.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 複数のFluxを一つずつ実行する
	 */
	@Test
	public void P259_concatのサンプル() throws Exception {
		Flux<Long> flux1 = Flux.interval(Duration.ofMillis(300)).take(5);
		Flux<Long> flux2 = Flux.interval(Duration.ofMillis(500)).take(2)
				.map(data -> data + 100);
		Flux<Long> flux = Flux.concat(flux1, flux2);
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	@Test
	public void P262_concatEagerのサンプル() throws Exception {
		// FluxにconcatEagerに相当するメソッドはない
	}

	/**
	 * 引数のデータを通知した後に自分自身のデータを通知する
	 */
	@Test
	public void P265_startWithのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(300)).take(5);
		Flux<Long> other = Flux.interval(Duration.ofMillis(500)).take(2)
				.map(data -> data + 100);
		Flux<Long> result = flux.startWith(other);
		result.log().subscribe();
		Thread.sleep(3000);
	}

	/**
	 * 複数のFluxの通知データが揃ったタイミングで新しいデータを生成する
	 */
	@Test
	public void P269_zipのサンプル() throws Exception {
		Flux<Long> flux1 = Flux.interval(Duration.ofMillis(300)).take(5);
		Flux<Long> flux2 = Flux.interval(Duration.ofMillis(500)).take(3)
				.map(data -> data + 100);
		Flux<Tuple2<Long, Long>> result = Flux.zip(flux1, flux2);
		result.log().subscribe();
		Thread.sleep(2000);
	}

	/**
	 * 複数のFluxがデータを通知するたびに新しいデータを生成する
	 */

	@Test
	public void P273_combineLatestのサンプル() throws Exception {
		Flux<Long> flux1 = Flux.interval(Duration.ofMillis(300)).take(5);
		Flux<Long> flux2 = Flux.interval(Duration.ofMillis(500)).take(3)
				.map(data -> data + 100);

		Flux<List<Long>> result = Flux.combineLatest(flux1, flux2,
				(data1, data2) -> Arrays.asList(data1, data2));
		result.log().subscribe();
		Thread.sleep(2000);
	}
}
