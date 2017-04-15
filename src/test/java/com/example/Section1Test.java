package com.example;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Flux/Monoを生成するオペレータ
 */
public class Section1Test {
	/**
	 * 引数のデータを通知するFluxを生成する
	 */
	@Test
	public void P166_justメソッドのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "B", "C", "D", "E");
		flux.log().subscribe();
	}

	/**
	 * 配列やIterableからFluxを生成する
	 */
	@Test
	public void P167_fromArrayのサンプル() throws Exception {
		Flux<String> flux = Flux.fromArray(new String[] { "A", "B", "C", "D", "E" });
		flux.log().subscribe();
	}

	/**
	 * Callableの処理を実行し、その結果を通知するMonoを生成する
	 */
	@Test
	public void P169_fromCallableのサンプル() throws Exception {
		Mono<Long> mono = Mono.fromCallable(() -> System.currentTimeMillis());
		mono.log().subscribe();
	}

	/**
	 * 指定した開始から順にデータ数分の数値を通知するFluxを生成する
	 */
	@Test
	public void P170_rangeのサンプル() throws Exception {
		Flux<Integer> flux = Flux.range(10, 3);
		flux.log().subscribe();
	}

	/**
	 * 指定した通知間隔で数値を通知するFluxを生成する
	 */
	@Test
	public void P172_intervalのサンプル() throws Exception {
		Flux<Long> flux = Flux.interval(Duration.ofMillis(1000));
		System.out.println("開始");
		flux.log().subscribe(); // parallel scheduler上で実行される。
								// RxJavaのcomputation schedulerに相当
		Thread.sleep(5000L);
	}

	/**
	 * 指定した時間の後に「0」を通知するMonoを生成する
	 */
	@Test
	public void P175_timerのサンプル() throws Exception {
		Mono<Long> mono = Mono.delay(Duration.ofMillis(1000));
		System.out.println("開始");
		mono.log().subscribe();
		Thread.sleep(1500L);
	}

	/**
	 * 購読されるたびに新しいFluxを生成する
	 */
	@Test
	public void P177_deferのサンプル() throws Exception {
		Flux<LocalTime> flux = Flux.defer(() -> Flux.just(LocalTime.now()));
		System.out.println("開始");
		flux.log().subscribe();
		Thread.sleep(2000L);
		flux.log().subscribe();
	}

	/**
	 * 空のFluxを生成する
	 */
	@Test
	public void P178_emptyのサンプル() throws Exception {
		Flux<Void> flux = Flux.empty();
		flux.log().subscribe();
	}

	/**
	 * エラーのみを通知するFluxを生成する
	 */
	@Test
	public void P180_errorのサンプル() throws Exception {
		Flux<Void> flux = Flux.error(new Exception("例外発生"));
		flux.log().subscribe();
	}

	/**
	 * 何も通知しないFluxを生成する
	 */
	@Test
	public void P182_neverのサンプル() throws Exception {
		Flux<Void> flux = Flux.never();
		flux.log().subscribe();
	}
}
