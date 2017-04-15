package com.example;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Fluxのデータを集計するオペレータ
 */
public class Section6Test {
	/**
	 * Fluxの持つデータを集計し、その集計結果のみを通知する
	 */
	@Test
	public void P288_reduceのサンプル() throws Exception {
		Mono<Integer> mono = Flux.just(1, 10, 100, 1000, 10000).reduce(0,
				(sum, data) -> sum + data);
		mono.log().subscribe();
	}

	/**
	 * Fluxの持つデータを集計し各計算結果を通知する
	 */
	@Test
	public void P291_scanのサンプル() throws Exception {
		Flux<Integer> flux = Flux.just(1, 10, 100, 1000, 10000).scan(0,
				(sum, data) -> sum + data);
		flux.log().subscribe();
	}
}
