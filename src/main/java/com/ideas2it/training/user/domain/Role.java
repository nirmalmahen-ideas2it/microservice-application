package com.ideas2it.training.user.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a Role in the system.
 * Includes attributes for role name and associated users.
 * Extends auditing functionality for tracking creation and modification details.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@Entity
@Table(name = "roles")
@Data
public class Role extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude // Prevent circular reference
    private Set<User> users = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

}

