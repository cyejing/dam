package cn.cyejing.dam.registry.etcd;

import cn.cyejing.dam.registry.RegistryConfig;
import cn.cyejing.dam.registry.spi.KeyValue;
import cn.cyejing.dam.registry.spi.RegistrySPI;
import cn.cyejing.dam.registry.spi.Watch;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class EtcdRegistry implements RegistrySPI {

    protected Client client;
    protected RegistryConfig config;

    public String wrapNamespace(String key) {
        return config.getNamespace() + key;
    }

    @Override
    public void init(RegistryConfig config) {
        this.config = config;
        this.client = Client.builder().endpoints(config.getAddresses()).build();
    }

    @Override
    public void put(String key, String value) {
        try {
            client.getKVClient()
                    .put(ByteSequence.from(wrapNamespace(key), Charset.defaultCharset()), ByteSequence.from(value, Charset.defaultCharset()))
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putWithHeartbeat(String key, String value) {
        try {
            //TODO
            client.getKVClient()
                    .put(ByteSequence.from(wrapNamespace(key), Charset.defaultCharset()), ByteSequence.from(value, Charset.defaultCharset()))
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void putWithTimeout(String key, String value, int timeout, TimeUnit unit) {
        try {
            LeaseGrantResponse leaseGrantResponse = client.getLeaseClient().grant(unit.toSeconds(timeout)).get();

            client.getKVClient()
                    .put(ByteSequence.from(wrapNamespace(key), Charset.defaultCharset()),
                            ByteSequence.from(value, Charset.defaultCharset()),
                            PutOption.newBuilder().withLeaseId(leaseGrantResponse.getID()).build())
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String key,boolean withPrefix) {
        try {
            ByteSequence etcdKey = ByteSequence.from(wrapNamespace(key), Charset.defaultCharset());

            DeleteOption.Builder deleteOption = DeleteOption.newBuilder();
            if (withPrefix) {
                deleteOption.withPrefix(etcdKey);
            }
            client.getKVClient().delete(etcdKey, deleteOption.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<KeyValue> get(String key,boolean withPrefix) {
        try {
            ByteSequence etcdKey = ByteSequence.from(wrapNamespace(key), Charset.defaultCharset());
            GetOption.Builder getOption = GetOption.newBuilder();
            if (withPrefix) {
                getOption.withPrefix(etcdKey);
            }
            GetResponse getResponse = client.getKVClient().get(etcdKey, getOption.build()).get();
            if (getResponse.getKvs().size() > 0) {
                return getResponse.getKvs().stream()
                        .map(keyValue -> new KeyValue(keyValue.getKey().toString(Charset.defaultCharset()),
                                keyValue.getValue().toString(Charset.defaultCharset())))
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addWatch(String watchKey, Watch watch,boolean prefix) {
        ByteSequence etcdKey = ByteSequence.from(watchKey, Charset.defaultCharset());
        WatchOption.Builder watchOption = WatchOption.newBuilder();
        if (prefix) {
            watchOption.withPrefix(etcdKey);
        }
        client.getWatchClient().watch(etcdKey, watchOption.build()
                , response -> response.getEvents().forEach(watchEvent -> {
                    String key = watchEvent.getKeyValue().getKey().toString(Charset.defaultCharset());
                    String value = watchEvent.getKeyValue().getValue().toString(Charset.defaultCharset());
                    KeyValue keyValue = new KeyValue(key, value);
                    if (WatchEvent.EventType.PUT.equals(watchEvent.getEventType())) {
                        watch.put(keyValue);
                    } else if (WatchEvent.EventType.DELETE.equals(watchEvent.getEventType())) {
                        watch.delete(keyValue);
                    } else {
                        log.warn("UNRECOGNIZED watchEvent:{}", keyValue);
                    }
                }));
    }

}
