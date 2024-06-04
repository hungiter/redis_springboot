package fpt.example.db_protect.services;

import fpt.example.db_protect.models.Query;
import fpt.example.db_protect.models.QueryType;
import fpt.example.db_protect.models_h2.H2Sessions;
import fpt.example.db_protect.models_h2.H2SessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.GeoArgs.Sort;


@Service
public class SessionsService {
    @Autowired
    H2SessionsRepository h2SessionsRepository;

    @Autowired
    private CacheManager cacheManager;

    public String getSessionById(Long id) {
        String result = "Response{\n";
        int statusCode = 0;
        String session = "";
        String queryData = "";
        H2Sessions cacheSessions = cacheSessions(id);
        if (cacheSessions != null) {
            session = sessionToString(cacheSessions);
        }


        if (session.isEmpty()) {
            Query query = selectQuery(id);
            if (queryExist(id) == null) {
                H2Sessions dbSessions = databaseSessions(id);
                if (dbSessions != null) {
                    session = sessionToString(dbSessions);
                    addSessions(id, dbSessions);
                }
                statusCode = 1;
                addQuery(id, query);
                queryData = " Query{\n  Id: " + query.getId() + "\n  Type: " + query.getQueryType().name() + "\n  Status: Added" + "\n }";
            } else {
                Long ttl = getItemTTL("queryCache", String.valueOf(id));
                queryData = " Query{\n  Id: " + query.getId() + "\n  Type: " + query.getQueryType().name() + "\n  Status: Existed\n  TTL: " + ttl + "s\n }";
                statusCode = 2;
            }
        }

        result += session.isEmpty() ? " Session{\n  message: data not found\n }," : session + ",";
        result += queryData.isEmpty() ? "\n" : "\n" + queryData + ",\n";
        result += statusGet(statusCode);
        result += "\n}";

        return result;
    }

    public List<String> get2MinutesSession() {
        List<String> result = new ArrayList<>();

        Query query = new Query(0L, QueryType.SELECT2MINVALUE);
        Cache cache = cacheManager.getCache("queryCache");
        if (cache != null) {
            cache.put(0L, query);
        }

        List<H2Sessions> list = h2SessionsRepository.findEntitiesWithTimeLessThan2Minutes();
        list.forEach(item -> result.add(sessionToString(item) + "\n"));
        return result;
    }


    private H2Sessions cacheSessions(Long id) {
        Cache cache = cacheManager.getCache("itemCache");
        return cache != null ? cache.get(id, H2Sessions.class) : null;
    }

    private H2Sessions databaseSessions(Long id) {
        return h2SessionsRepository.findById(id).orElse(null);
    }

    private String sessionToString(H2Sessions session) {
        String result = "";
        result += " Session{\n";
        result += "  Id: " + session.getId() + "\n";
        result += "  SessionId: " + session.getSessionId() + "\n";
        result += "  Contract: " + session.getContract() + "\n";
        result += "  Status: " + session.getStatus() + "\n";
        result += "  Create at: " + session.getCreateAt() + "\n";
        result += "  Update at: " + session.getUpdatedAt() + "\n }";
        return result;
    }

    private String statusGet(int statusCode) {
        return switch (statusCode) {
            case 0 -> " Status{\n  message: Cache's data\n }";
            case 1 -> " Status{\n  message: Database's result\n }";
            case 2 -> " Status{\n  message: Please wait.... (query already use) \n }";
            default -> "";
        };
    }

    private Query selectQuery(Long id) {
        return new Query(id, QueryType.SELECT);
    }

    // Cache Manager
    private H2Sessions sessionExist(Long id) {
        Cache cache = cacheManager.getCache("itemCache");
        return cache != null ? cache.get(id, H2Sessions.class) : null;
    }


    private void addSessions(Long id, H2Sessions h2Sessions) {
        Cache cache = cacheManager.getCache("itemCache");
        if (h2Sessions != null && cache != null) {
            cache.put(id, h2Sessions);
        }
    }

    private Query queryExist(Long id) {
        Cache cache = cacheManager.getCache("queryCache");
        return cache != null ? cache.get(id, Query.class) : null;
    }

    private void addQuery(Long id, Query query) {
        Cache cache = cacheManager.getCache("queryCache");
        if (query != null && cache != null) {
            cache.put(id, query);
        }
    }

    // Optional
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public Long getItemTTL(String cacheName, String key) {
        // Construct the Redis key
        String redisKey = cacheName + "::" + key;
        // Get the TTL in seconds
        return redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
    }
}
