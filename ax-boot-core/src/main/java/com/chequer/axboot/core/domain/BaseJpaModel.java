package com.chequer.axboot.core.domain;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.chequer.axboot.core.domain.user.User;
import com.chequer.axboot.core.utils.SessionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;

@Setter
@Getter
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BaseJpaModel<PK extends Serializable> extends CrudModel implements Persistable<PK>, Serializable {

    @Override
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @Column(name = "CREATED_AT", updatable = false)
    @ColumnPosition(Integer.MAX_VALUE - 4)
    protected Instant createdAt;

    @Column(name = "CREATED_BY", updatable = false)
    @ColumnPosition(Integer.MAX_VALUE - 3)
    protected String createdBy;

    @Column(name = "UPDATED_AT")
    @ColumnPosition(Integer.MAX_VALUE - 2)
    protected Instant updatedAt;

    @Column(name = "UPDATED_BY")
    @ColumnPosition(Integer.MAX_VALUE - 1)
    protected String updatedBy;

    @Transient
    protected User createdUser;

    @Transient
    protected User modifiedUser;

    @PrePersist
    protected void onPersist() {
        this.createdBy = this.updatedBy = SessionUtils.getCurrentLoginUserCd();
        this.createdAt = this.updatedAt = Instant.now(Clock.systemUTC());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedBy = SessionUtils.getCurrentLoginUserCd();
        this.updatedAt = Instant.now(Clock.systemUTC());
    }

    @PostLoad
    protected void onPostLoad() {
    }
}
