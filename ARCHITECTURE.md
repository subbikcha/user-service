# Architecture — user-service

## 1. Service Boundaries & Dependencies

Package hierarchy: `controller` → `service` → `repository` → `entity`

- `controller` may import `service`, `dto`, and `entity` only.
- `service` may import `repository`, `entity`, `dto`, and `exception` only.
- `repository` may import `entity` only.
- `dto` must have no internal package imports — no imports from `entity`, `service`, or `repository`.
- `entity` must have no internal package imports.
- `config` (DataLoader) may import `repository` and `entity` only, and is the sole location for seed/startup data.
- `controller` must never import `repository` directly.
- `service` must never import `controller`.
- Business logic belongs exclusively in `service` — controllers must not contain business logic.

## 2. API Contract Rules

- All request and response field names use **camelCase** (e.g. `userId`, `userName`, `rewardPoints`, `walletBalance`, `phoneNumber`, `isActive`, `createdAt`).
- `userId` is a String — an 8-character prefix of a random UUID, generated in the service layer via `UUID.randomUUID().toString().substring(0, 8)`.
- `walletBalance` is a `Double` representing rupees (not paise/cents).
- `rewardPoints` is an `Integer`, not a float.
- `tier` is a String constrained to the values: `bronze`, `silver`, `gold`, `platinum`, `premium`, `standard`, `new`.
- `isActive` is always present in the response; it is never omitted.
- `createdAt` is serialized as an ISO 8601 string — never as a numeric timestamp (`spring.jackson.serialization.write-dates-as-timestamps=false`).
- Default values applied on creation: `tier` → `"new"`, `rewardPoints` → `0`, `walletBalance` → `0.0`, `isActive` → `true`.
- `POST /users` returns HTTP 201; `DELETE /users/{id}` returns HTTP 204; all other successful responses return HTTP 200.
- Error responses always include: `timestamp`, `status` (integer), `error` (string), `message` (string), `path` (string).
- No API versioning scheme exists — do not add version prefixes (e.g. `/v1/`) without an architectural decision.

## 3. Security Rules

- No authentication or authorization framework is currently configured — do not silently add Spring Security in a way that locks out existing consumers.
- CORS is enabled globally with `origins = "*"` at the controller level — any narrowing must be done at the controller annotation, not via a separate security filter.
- Passwords, secrets, and credentials must never appear in entity fields, DTOs, or API responses.
- `application.properties` credentials (`spring.datasource.password`) must never be committed as production values.
- SQL is generated exclusively by Hibernate — raw SQL strings must not be concatenated with user input anywhere.

## 4. Data Access Patterns

- All database access goes through `UserRepository`, which extends `JpaRepository<User, String>`.
- No custom `@Query` methods exist — use inherited JPA methods or add derived query methods on the repository; do not bypass the repository by injecting `EntityManager` into the service.
- No explicit `@Transactional` annotations are used — rely on Spring Data JPA's implicit transaction per `save()` call.
- Do not introduce multi-step transactions without adding explicit `@Transactional` on the service method.
- `spring.jpa.hibernate.ddl-auto=update` is in effect — schema changes must be backward-compatible; do not rename or drop columns without a migration plan.
- Soft deletes: `deleteUser` sets `isActive = false` and calls `save()` — it does not call `repository.delete()`. All delete operations must follow this pattern.
- `getAllUsers()` filters by `isActive == true` in the service layer — queries must respect soft-delete state.

## 5. Naming Conventions

- Java classes: PascalCase.
- Methods and fields: camelCase.
- Packages: lowercase, underscore-separated where needed (`user_service`).
- DTO classes are suffixed `Request` for inbound payloads (e.g. `CreateUserRequest`, `UpdateUserRequest`).
- Service classes are suffixed `Service`; controllers suffixed `Controller`; repositories suffixed `Repository`; exceptions suffixed `Exception`; exception handlers suffixed `Handler`.
- Entity class is named after the domain noun without a suffix: `User`, not `UserEntity`.
- File names match their class name exactly (Java standard).
- Tier values are lowercase strings: `bronze`, `silver`, `gold`, `platinum`, `premium`, `standard`, `new`.

## 6. Error Handling

- `UserNotFoundException` (extends `RuntimeException`) is the sole custom domain exception — throw it whenever a user lookup fails, never return `null` from `getUser()`.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) is the single location for translating exceptions to HTTP responses — do not add `try/catch` blocks in controllers that return error responses directly.
- `UserNotFoundException` maps to HTTP 404.
- All unhandled exceptions map to HTTP 500 via the generic handler in `GlobalExceptionHandler`.
- Exceptions must not be swallowed silently (empty `catch` blocks are forbidden).
- Do not expose raw Java stack traces or exception class names in API responses.
- Service methods throw exceptions upward — they do not return sentinel values like `null` or `-1` to indicate errors.

## 7. Things That Must Never Happen

- Controllers must not call `UserRepository` directly.
- DTOs must not be used as JPA entities (no `@Entity` on a DTO class).
- The `User` entity must not be returned directly from controller methods — it is passed through the service, which currently returns the entity; if a separate response DTO is introduced, all endpoints must be updated consistently.
- `repository.delete()` (hard delete) must not be called — all deletions are soft deletes via `isActive = false`.
- `tier` must not be stored as a Java enum in the database column without a migration, as it is currently stored as a plain String.
- Negative values for `rewardPoints` additions or `walletBalance` top-ups must not be accepted once input validation is introduced — the existing add/top-up operations are additive only.
- `UUID.randomUUID().toString().substring(0, 8)` is the mandated `userId` generation strategy — do not switch to sequential IDs or full UUIDs without updating all existing references.
- Lombok annotations (`@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`) are used on the `User` entity — do not remove them or replace with manual boilerplate.
- `spring.jpa.show-sql=true` is intentional for development — do not disable it in the dev profile; do not enable it in production profiles.
- Seed data lives exclusively in `DataLoader` — do not add hardcoded users elsewhere.
