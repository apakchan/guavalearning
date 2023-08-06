package com.learn.guava.cache;

import com.google.common.base.Strings;
import com.google.common.cache.*;
import com.learn.guava.cache.entity.Employee;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheLoaderTest {
    final String KEY = "cafebabe";

    @Test
    public void testBasic() throws ExecutionException, InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                // write read update
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .build(createByNameCacheLoader());
        // load from db
        assertEquals("cafebabe", cache.get("cafebabe").getName());
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
    }

    @Test
    public void testEvictionBySize() {
        CacheLoader<String, Employee> cacheLoader = createByNameCacheLoader();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(3).build(cacheLoader);
        cache.getUnchecked("a");
        cache.getUnchecked("b");
        cache.getUnchecked("c");
        cache.getUnchecked("d");
        assertEquals(3, cache.size());
    }

    @Test
    public void testEvictionByWeight() {
        CacheLoader<String, Employee> cacheLoader = createByNameCacheLoader();
        Weigher<String, Employee> weigher = (key, emp) ->
                emp.getEmpId().length() + emp.getName().length() + emp.getDept().length();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumWeight(45)
                .concurrencyLevel(1)
                .weigher(weigher)
                .build(cacheLoader);
        cache.getUnchecked("abc");
        cache.getUnchecked("def");
        cache.getUnchecked("ghi");
        cache.getUnchecked("jkl");
        cache.getUnchecked("mno");
        cache.getUnchecked("abc");
    }

    @Test
    public void testEvictionByWriteTime() throws InterruptedException, ExecutionException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build(createByNameCacheLoader());
        // load from db
        assertEquals("cafebabe", cache.get("cafebabe").getName());
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.getIfPresent("cafebabe"));
        TimeUnit.SECONDS.sleep(2);

        // eviction
        assertNull(cache.getIfPresent("cafebabe"));
    }

    @Test
    public void testWeakKey() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS)
                .weakKeys()
                .weakValues()
                .build(createByNameCacheLoader());
        assertNotNull(cache.getUnchecked(KEY));
        garbageCollectAndSleep();
        assertNull(cache.getIfPresent(KEY));
    }

    @Test
    public void testSoftKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS)
                .softValues()
                .build(createByNameCacheLoader());
        for (int i = 0; ; i++) {
            cache.put(KEY + i, new Employee(KEY + i, KEY + i, KEY + i));
            TimeUnit.MILLISECONDS.sleep(600);
        }
    }

    @Test
    public void testLoadNullValue() {
        CacheLoader<String, Employee> cacheLoader = CacheLoader.from(k -> k.equals("null") ? null : new Employee(k, k, k));
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().build(cacheLoader);
        try {
            cache.getUnchecked("null");
            fail();
        } catch (Exception e) {
            assertEquals(CacheLoader.InvalidCacheLoadException.class, e.getClass());
        }
    }

    @Test
    public void testLoadNullValueUseOptional() {
        CacheLoader<String, Optional<Employee>> loader = new CacheLoader<String, Optional<Employee>>() {
            @Override
            public Optional<Employee> load(String key) throws Exception {
                if (Strings.isNullOrEmpty(key) || key.equals("null")) {
                    return Optional.empty();
                }
                return Optional.of(new Employee(key, key, key));
            }
        };
        LoadingCache<String, Optional<Employee>> cache = CacheBuilder.newBuilder().build(loader);
        assertTrue(cache.getUnchecked(KEY).isPresent());
        assertNull(cache.getUnchecked("null").orElse(null));
    }

    @Test
    public void testCacheRefresh() throws InterruptedException {
        CacheLoader<String, Long> cacheLoader = CacheLoader.from(k -> System.currentTimeMillis());
        LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
                // save time when taking
                // if greater than expect time then reload
                // instead of using background thread to refresh it
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(cacheLoader);
        Long now = cache.getUnchecked("now");
        TimeUnit.MILLISECONDS.sleep(500);
        assertEquals(now, cache.getUnchecked("now"));
    }

    @Test
    public void testCachePreLoad() {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);
        Map<String, String> preMap = new HashMap<String, String>() {
            {
                put("cafe", "CAFE");
                put("babe", "BABE");
            }
        };
        cache.putAll(preMap);
        assertEquals(2L, cache.size());
        assertEquals("BABE", cache.getUnchecked("babe"));
    }

    @Test
    public void testCacheRemovedNotification() {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        RemovalListener<String, String> removalListener = notification -> {
          if (notification.wasEvicted()) {
              // remove KV because of SIZE
              RemovalCause cause = notification.getCause();
              assertEquals(RemovalCause.SIZE, cause);
          }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3L)
                .removalListener(removalListener)
                .build(loader);

        cache.getUnchecked("a");
        cache.getUnchecked("b");
        cache.getUnchecked("c");
        cache.getUnchecked("d");
        assertNull(cache.getIfPresent("a"));
        assertEquals(3L, cache.size());
    }

    @Test
    public void testCacheStat() {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().recordStats().build(loader);
        cache.getUnchecked(KEY);
        // cache stat is immutable
        // thread safe
        assertEquals(0L, cache.stats().hitCount());
        assertEquals(1L, cache.stats().missCount());
        cache.getUnchecked(KEY);
        assertEquals(1, cache.stats().hitCount());
        assertEquals(1L, cache.stats().missCount());
    }

    @Test
    public void testCacheSpec() {
        String spec = "maximumSize=5,recordStats=true";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        LoadingCache<String, String> cache = CacheBuilder
                .from(cacheBuilderSpec)
                .build(loader);
    }

    private LoadingCache<String, String> toUppercaseCache() {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(loader);
        return cache;
    }

    private void garbageCollectAndSleep() {
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CacheLoader<String, Employee> createByNameCacheLoader() {
        return new CacheLoader<String, Employee>() {
            @Override
            public Employee load(String key) throws Exception {
                return findEmployeeByName(key);
            }
        };
    }

    private Employee findEmployeeByName(final String name) {
        System.out.println("[" + name + "] emp load from db");
        return new Employee(name, name, name);
    }
}
