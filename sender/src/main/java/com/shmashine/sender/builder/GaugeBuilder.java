// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.builder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/4 11:54
 * @since v1.0
 */

public class GaugeBuilder {
    private final MeterRegistry meterRegistry;

    private Gauge.Builder builder;

    @Getter
    private AtomicInteger counter = new AtomicInteger(0);

    private Consumer<Gauge.Builder> consumer;

    public GaugeBuilder(MeterRegistry meterRegistry, String name, Consumer<Gauge.Builder> consumer) {
        this.builder = Gauge.builder(name, counter, c -> c.get());
        this.meterRegistry = meterRegistry;
        this.consumer = consumer;
    }

    public GaugeBuilder setCounter(Integer count) {
        counter.set(count);
        return this;
    }

    public Gauge build() {
        consumer.accept(builder);
        return builder.register(meterRegistry);
    }
}
