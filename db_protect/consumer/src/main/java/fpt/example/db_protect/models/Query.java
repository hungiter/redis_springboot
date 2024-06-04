package fpt.example.db_protect.models;

import java.io.Serializable;

public class Query implements Serializable {
    Long id;
    QueryType queryType;

    public Query(Long id, QueryType queryType) {
        this.id = id;
        this.queryType = queryType;
    }

    public Long getId() {
        return id;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    @Override
    public String toString() {
        return "Query{" +
                "id=" + id +
                ", queryType=" + queryType +
                '}';
    }
}
