package com.sample.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;

import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

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

