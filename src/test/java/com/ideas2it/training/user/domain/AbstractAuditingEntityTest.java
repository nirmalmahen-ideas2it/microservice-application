package com.ideas2it.training.user.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class AbstractAuditingEntityTest {
    static class TestEntity extends AbstractAuditingEntity<Long> {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Test
    void testAuditFields() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setCreatedBy("creator");
        entity.setCreatedDate(Instant.now());
        entity.setLastModifiedBy("modifier");
        entity.setLastModifiedDate(Instant.now());
        assertEquals(1L, entity.getId());
        assertEquals("creator", entity.getCreatedBy());
        assertNotNull(entity.getCreatedDate());
        assertEquals("modifier", entity.getLastModifiedBy());
        assertNotNull(entity.getLastModifiedDate());
    }
}