package io.nobt.persistence.cashflow;

import io.nobt.persistence.nobt.NobtEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@MappedSuperclass
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class CashFlowEntity {

    @EmbeddedId
    private Key key = new Key();

    @ManyToOne
    @JoinColumn(name = "nobtId", nullable = false, insertable = false, updatable = false)
    private NobtEntity nobt;

    @Column(name = "createdOn", nullable = false, updatable = false)
    private ZonedDateTime createdOn;

    public NobtEntity getNobt() {
        return nobt;
    }

    public void setNobt(NobtEntity nobt) {
        this.nobt = nobt;
        this.key.setNobt(nobt);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key id) {
        this.key = id;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Embeddable
    public static class Key implements Serializable {

        @Column(name = "id")
        private long id;

        @ManyToOne
        @JoinColumn(name = "nobtId", updatable = false, insertable = false, nullable = false)
        private NobtEntity nobt;

        public Key() {
        }

        public Key(long id, NobtEntity nobt) {
            this.id = id;
            this.nobt = nobt;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public NobtEntity getNobt() {
            return nobt;
        }

        public void setNobt(NobtEntity nobtId) {
            this.nobt = nobtId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(id, key.id) &&
                    Objects.equals(nobt, key.nobt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, nobt);
        }
    }
}
