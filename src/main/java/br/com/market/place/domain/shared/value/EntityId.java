package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.exception.InvalidIdException;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class EntityId {
    private UUID id;

    public EntityId(String id) {
        if (id == null) {
            throw new InvalidIdException("Id is required, cannot be null!");
        }
        try {
            this.id = UUID.fromString(id);
        } catch (Exception ex) {
            throw new InvalidIdException("Id " + id + " is invalid!");
        }
    }

    public EntityId() {
        this.id = UUID.randomUUID();
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof EntityId entity) {
            return Objects.equals(getId(), entity.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public final UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
