package com.example;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 通知するデータを変換するオペレータ
 */
public class Section2Test {
	/**
	 * データを変換して通知する
	 */
	@Test
	public void P184_mapのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "B", "C", "D", "E")
				.map(data -> data.toLowerCase());
		flux.log().subscribe();
	}

	/**
	 * 受け取ったデータをFluxに変換し、そのFluxが持つデータを通知する*
	 */
	@Test
	public void P189_flatMapのサンプル() throws Exception {
		Flux<String> flux = Flux.just("A", "", "B", "", "C").flatMap(data -> {
			if ("".equals(data)) {
				return Flux.empty();
			}
			else {
				return Flux.just(data.toLowerCase());
			}
		});
		flux.log().subscribe();
	}

	@Test
	public void P191_flatMapのサンプル() throws Exception {
		// Flowable.flatMap(mapper, combiner)に相当するFluxのメソッドがない
	}

	@Test
	public void P192_flatMapのサンプル() throws Exception {
		Flux<Integer> original = Flux.just(1, 2, 0, 4, 5).map(data -> 10 / data);
		Flux<Integer> flux = original.flatMap(data -> Flux.just(data), // 通常時のデータ
				error -> Flux.just(-1), // エラー時のデータ
				() -> Flux.just(100) // 完了時のデータ
		);
		flux.log().subscribe();
	}

	/**
	 * 受け取ったデータをFluxに変換し、そのFluxを一つずつ順に実行し、そのデータを通知する
	 */
	@Test
	public void P195_concatMapのサンプル() throws Exception {
		Flux<String> flux = Flux.range(10, 3).concatMap(
				sourceData -> Flux.interval(Duration.ofMillis(500)).take(2).map(data -> {
					// 通知時のシステム時間もデータに加える
					long time = System.currentTimeMillis();
					return time + "ms: [" + sourceData + "] " + data;
				}));
		flux.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * 受け取ったデータをFluxに変換しすぐに実行するが、通知するデータは生成したFluxの順に通知する
	 */
	@Test
	public void P198_concatMapEagerのサンプル() throws Exception {
		// FluxにconcatMapEagerはない
	}

	@Test
	public void P199_concatMapEagerDelayErrorのサンプル() throws Exception {
		// FluxにconcatMapEagerDelayErrorはない
	}

	/**
	 * 通知するデータを指定した範囲でまとめたListやCollectionにして通知する
	 */
	@Test
	public void P204_bufferのサンプル() throws Exception {
		Flux<List<Long>> flux = Flux.interval(Duration.ofMillis(100)).take(10).buffer(3);
		flux.log().subscribe();
		Thread.sleep(3000);
	}

	@Test
	public void P205_bufferのサンプル() throws Exception {
		Flux<List<Long>> flux = Flux.interval(Duration.ofMillis(300)).take(7)
				.buffer(Flux.interval(Duration.ofMillis(1000)));
		flux.log().subscribe();
		Thread.sleep(4000);
	}

	/**
	 * 通知するデータを全てListにして通知する
	 */
	@Test
	public void P207_toListのサンプル() throws Exception {
		Mono<List<String>> mono = Flux.just("A", "B", "C", "D", "E").collectList();
		mono.log().subscribe();
	}

	/**
	 * 通知するデータをキーと一つの値を持つMapに格納して通知する
	 */
	@Test
	public void P210_toMapのサンプル() throws Exception {
		Mono<Map<Long, String>> mono = Flux.just("1A", "2B", "3C", "1D", "2E")
				.collectMap(data -> Long.valueOf(data.substring(0, 1)));
		mono.log().subscribe();
	}

	@Test
	public void P211_toMapのサンプル() throws Exception {
		Mono<Map<Long, String>> mono = Flux.just("1A", "2B", "3C", "1D", "2E").collectMap(
				data -> Long.valueOf(data.substring(0, 1)), data -> data.substring(1));
		mono.log().subscribe();
	}

	/**
	 * 通知するデータをキーとCollectionを持つMapに格納して通知する
	 */
	@Test
	public void P214_toMultiMapのサンプル() throws Exception {
		Mono<Map<String, Collection<Long>>> mono = Flux.interval(Duration.ofMillis(500))
				.take(5).collectMultimap(data -> {
					if (data % 2 == 0) {
						return "偶数";
					}
					else {
						return "奇数";
					}
				});
		mono.log().subscribe();
		Thread.sleep(3000);
	}
}
