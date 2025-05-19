package com.ideas2it.training.user.web.rest.controller;

import com.ideas2it.training.user.domain.PagedResponse;
import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.dto.RoleCreateDto;
import com.ideas2it.training.user.dto.RoleInfo;
import com.ideas2it.training.user.dto.RoleUpdateDto;
import com.ideas2it.training.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link Role}.
 * Provides CRUD endpoints for roles.
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new role", description = "Adds a new role entity to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role created successfully",
                    content = @Content(schema = @Schema(implementation = RoleInfo.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<RoleInfo> create(@Valid @RequestBody RoleCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Update an existing role", description = "Updates the details of an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(schema = @Schema(implementation = RoleInfo.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role input"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<RoleInfo> update(@Valid @RequestBody RoleUpdateDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }

    @Operation(summary = "Partially update an existing role", description = "Updates specific fields of an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(schema = @Schema(implementation = RoleInfo.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role input"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping
    public ResponseEntity<RoleInfo> patch(@RequestBody RoleUpdateDto dto) {
        return ResponseEntity.ok(service.partialUpdate(dto));
    }

    @Operation(summary = "List all roles", description = "Retrieves a list of all roles in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RoleInfo.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<RoleInfo>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get role by ID", description = "Fetches the details of a role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RoleInfo.class))),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleInfo> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List paged roles", description = "Returns roles with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged roles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<RoleInfo>> getAllPaged(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getAllPaged(offset, limit));
    }

    @Operation(summary = "Delete role by ID", description = "Deletes a role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
