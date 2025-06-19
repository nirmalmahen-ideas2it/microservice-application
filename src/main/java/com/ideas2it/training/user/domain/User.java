package com.ideas2it.training.user.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a User in the system.
 * Includes attributes for user details such as username, password, and
 * associated roles.
 * Extends auditing functionality for tracking creation and modification
 * details.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@EqualsAndHashCode(callSuper = true, exclude = { "roles" })
@Entity
@Table(name = "users")
@Data
public class User extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String address;
    private String postalCode;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Instant resetTokenExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @Version
    private Long rowVersion;

}
