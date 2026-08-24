# Jakarta EE Security Example

[![build](https://github.com/hantsy/jakarta-security-example/actions/workflows/build.yml/badge.svg)](https://github.com/hantsy/jakarta-security-example/actions/workflows/build.yml)
[![arq-glassfish-managed](https://github.com/hantsy/jakarta-security-example/actions/workflows/arq-glassfish-managed.yml/badge.svg)](https://github.com/hantsy/jakarta-security-example/actions/workflows/arq-glassfish-managed.yml)

Demo of Jakarta EE Security for REST and WEB.

This example demonstrates how to secure a Jakarta EE application using the Jakarta Security APIs, covering:

* **Multiple authentication mechanisms** — a single `HttpAuthenticationMechanismHandler` dispatches requests by path: REST APIs (`/api/*`) are protected by MicroProfile JWT, and web pages are protected by a form-based authentication mechanism.
* **JPA-backed identity store** — a custom `IdentityStore` backed by a `UserAccount` entity (with a `RoleType` enum) and a repository, following DDD layers (`domain`, `application`, `infrastructure`, `interfaces`).
* **JWT token endpoint** — `POST /api/token` issues a JWT signed with Nimbus JOSE.
* **Register use case** — a `@UseCase` CDI stereotype (`@ApplicationScoped` + `@Transactional`) and `POST /api/register`.
* **Custom authorization** — `@Authenticated` and `@Authorized` interceptors.

## Prerequisites

Assume you have installed the following software:
* Java 21
* Maven 3.9.x
* Git
* Any code editor, such as IntelliJ IDEA Community or VSCode with the Java extension pack.

## Build

Clone the source code via:

```bash
git clone https://github.com/hantsy/jakarta-security-example
```

Enter the project root folder and run the following command to build the project:

```bash
mvn clean package
```

Run the integration tests on GlassFish:

```bash
mvn clean verify -Parq-glassfish-managed
```

## Contribute

If you have any ideas about the implementation, please create an issue to discuss them.
