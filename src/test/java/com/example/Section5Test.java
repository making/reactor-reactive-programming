package com.example;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Flux/Monoの状態を通知するオペレータ
 */
public class Section5Test {
	/**
	 * Fluxが空なのかを判定する
	 */
	@Test
	public void P276_isEmptyのサンプル() throws Exception {
		// FluxにisEmptyはない
		Mono<Boolean> mono = Flux.interval(Duration.ofMillis(1000)).take(3)
				.filter(data -> data >= 3).count().map(c -> c == 0);
		mono.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * Fluxが指定したデータを含むか判定する
	 */
	@Test
	public void P278_containsのサンプル() throws Exception {
		// Fluxにcontainsはない
		Mono<Boolean> mono = Flux.interval(Duration.ofMillis(1000))
				.any(data -> data.equals(3L));
		mono.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * Fluxが指定した条件に合うのかを判定する
	 */
	@Test
	public void P280_allのサンプル() throws Exception {
		Mono<Boolean> mono = Flux.interval(Duration.ofMillis(1000)).take(3)
				.all(data -> data < 5);
		mono.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * 2つのFluxが同じ順で同じ数だけ等しいデータを通知しているかを判定する
	 */
	@Test
	public void P282_sequenceEqualのサンプル() throws Exception {
		// FluxにsequenceEqualはない
	}

	/**
	 * Fluxが持つデータ数を通知する
	 */
	@Test
	public void P284_countのサンプル() throws Exception {
		Mono<Long> mono = Flux.interval(Duration.ofMillis(1000)).take(3).count();
		mono.log().subscribe();
		Thread.sleep(4000);
	}
}
