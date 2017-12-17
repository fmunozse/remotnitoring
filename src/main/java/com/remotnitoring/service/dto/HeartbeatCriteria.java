package com.remotnitoring.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Heartbeat entity. This class is used in HeartbeatResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /heartbeats?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HeartbeatCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ZonedDateTimeFilter timestamp;

    private StringFilter ip;

    private LongFilter nodeId;

    public HeartbeatCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTimeFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getIp() {
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public LongFilter getNodeId() {
        return nodeId;
    }

    public void setNodeId(LongFilter nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "HeartbeatCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (ip != null ? "ip=" + ip + ", " : "") +
                (nodeId != null ? "nodeId=" + nodeId + ", " : "") +
            "}";
    }

}
