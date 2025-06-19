package com.ideas2it.training.user.web.rest.controller;

import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.dto.PagedResponse;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.dto.PasswordResetRequestDto;
import com.ideas2it.training.user.dto.PasswordResetTokenDto;
import com.ideas2it.training.user.dto.PasswordResetResponseDto;
import com.ideas2it.training.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * REST controller for managing {@link User}.
 * Provides CRUD + PATCH + pagination endpoints.
 * <p>
 * OpenAPI documentation is provided using Swagger annotations.
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

        private final UserService service;

        public UserController(UserService service) {
                this.service = service;
        }

        @Operation(summary = "Create a new user", description = "Adds a new user entity to the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserInfo.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid user input"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping
        public ResponseEntity<UserInfo> create(@Valid @RequestBody UserCreateDto dto) {

                return ResponseEntity.ok(service.create(dto));
        }

        @Operation(summary = "Update an existing user", description = "Updates the details of an existing user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserInfo.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid user input"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PutMapping
        public ResponseEntity<UserInfo> update(@Valid @RequestBody UserUpdateDto dto) {
                return ResponseEntity.ok(service.update(dto));
        }

        @Operation(summary = "Partially update an existing user", description = "Updates specific fields of an existing user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserInfo.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid user input"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PatchMapping
        public ResponseEntity<UserInfo> patch(@RequestBody UserUpdateDto dto) {
                return ResponseEntity.ok(service.partialUpdate(dto));
        }

        @Operation(summary = "Get user by ID", description = "Fetches the details of a user by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(schema = @Schema(implementation = UserInfo.class))),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/{id}")
        public ResponseEntity<UserInfo> getById(@PathVariable Long id) {
                return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "List all users", description = "Retrieves a list of all users in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UserInfo.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/all")
        public ResponseEntity<List<UserInfo>> getAll() {
                return ResponseEntity.ok(service.getAll());
        }

        @Operation(summary = "List paged users", description = "Returns users with pagination support")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Paged users retrieved successfully", content = @Content(schema = @Schema(implementation = PagedResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Void.class)))
        })
        @GetMapping("/paged")
        public ResponseEntity<PagedResponse<UserInfo>> getAllPaged(
                        @RequestParam(defaultValue = "0") int offset,
                        @RequestParam(defaultValue = "10") int limit) {
                return ResponseEntity.ok(service.getAllPaged(offset, limit));
        }

        @Operation(summary = "Delete user by ID", description = "Deletes a user by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
                service.delete(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Request password reset", description = "Initiates a password reset by generating a reset token for the user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Password reset token generated", content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/password-reset/request")
        public ResponseEntity<PasswordResetResponseDto> requestPasswordReset(
                        @Validated @RequestBody PasswordResetRequestDto requestDto) {
                return ResponseEntity.ok(service.requestPasswordReset(requestDto));
        }

        @Operation(summary = "Confirm password reset", description = "Resets the user's password using the provided token and new password.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Password reset successful", content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input or token"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/password-reset/confirm")
        public ResponseEntity<PasswordResetResponseDto> resetPassword(
                        @Validated @RequestBody PasswordResetTokenDto tokenDto) {
                return ResponseEntity.ok(service.resetPassword(tokenDto));
        }
}
