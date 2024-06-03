package fpt.example.db_protect.services;

import fpt.example.db_protect.models_h2.H2Sessions;
import fpt.example.db_protect.models_h2.H2SessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SessionsService {
    @Autowired
    H2SessionsRepository h2SessionsRepository;

    @Autowired
    private CacheManager cacheManager;

    public H2Sessions getSessionById(Long id) {
        Cache cache = cacheManager.getCache("itemCache");
        H2Sessions cachedObject = cache != null ? cache.get(id, H2Sessions.class) : null;

        System.out.println(cachedObject);
        if (cachedObject != null) {
            return cachedObject;
        } else {
            if (!queryExist(id)) {
                H2Sessions dbObject = h2SessionsRepository.findById(id).orElse(null);

                if (dbObject != null && cache != null) {
                    cache.put(id, dbObject);
                }
                return dbObject;
            } else {
                return new H2Sessions();
            }
        }
    }

    private boolean queryExist(Long id) {
        Cache cache = cacheManager.getCache("queryCache");
        if (cache != null) {
            return cache.get(id) != null;
        }
        return false;
    }
}
