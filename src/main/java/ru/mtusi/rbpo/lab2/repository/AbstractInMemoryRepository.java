package ru.mtusi.rbpo.lab2.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
public abstract class AbstractInMemoryRepository<T> {

    private final Map<Long, T> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    private final Function<T, Long> idGetter;
    private final BiFunction<T, Long, T> idSetter;

    protected AbstractInMemoryRepository(Function<T, Long> idGetter,
                                          BiFunction<T, Long, T> idSetter) {
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    public T save(T value) {
        Long id = idGetter.apply(value);
        if (id == null) {
            id = sequence.incrementAndGet();
        } else {
            sequence.accumulateAndGet(id, Math::max);
        }
        T saved = idSetter.apply(value, id);
        storage.put(id, saved);
        return saved;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> findAll() {
        return storage.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .map(Map.Entry::getValue)
                .toList();
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}
