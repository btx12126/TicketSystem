# Ticket System

A REST API for selling tickets, built with **Spring Boot**, **Spring Data JPA** and **MySQL**.
Clients can be registered, tickets can be added to the offer, and a client can buy an available ticket. Every purchase is stored with its date.

The project is split into two parts:

| Folder | Description |
|---|---|
| [`backend/`](backend) | The main part of the project: Spring Boot REST API with a MySQL database. |
| [`frontend/`](frontend) | A small demo UI (plain HTML, CSS and JavaScript) that calls the API. **It exists only to make the API easy to try out and was generated with AI.** It is not part of what this project is about. |

## Tech stack

- **Java 25**
- **Spring Boot 4.1** (Spring Web MVC, Spring Data JPA, Hibernate)
- **MySQL 8**
- **Maven** (wrapper included, no local Maven installation needed)
- **Docker / Docker Compose** (database, backend and demo frontend start with one command)
- Frontend: HTML, CSS, vanilla JavaScript (`fetch`)

## Features

- Add clients and tickets (a ticket has a category and a price)
- List tickets that are still available
- Buy a ticket as a given client
- Purchase history: every purchase is saved as a separate record with the client, the ticket and the purchase date
- Protection against buying the same ticket twice at the same time (see [Design decisions](#design-decisions))
- Input validation for prices, names and ticket categories
- CORS configuration that allows the demo frontend to talk to the API

## Architecture

The backend uses a classic layered structure:

```
Controller  ->  Service  ->  Repository  ->  MySQL
(HTTP)          (business     (Spring Data
                 rules,        JPA)
                 transactions)
```

```
backend/src/main/java/.../
├── config/          CORS configuration
├── controllers/     REST endpoints (Ticket, Client, Order)
├── services/        business logic and transactions
├── repositories/    Spring Data JPA repositories
└── model/           JPA entities (Client, Ticket, Buy)
```

### Data model

```
Client 1 ──── * Buy * ──── 1 Ticket
                              │
                              └── owner: Client (set when the ticket is sold)
```

| Entity | Fields |
|---|---|
| `Client` | `id`, `name`, `surname` |
| `Ticket` | `id`, `category`, `price` (`BigDecimal`), `available`, `owner`, `version` |
| `Buy` | `id`, `ticket`, `client`, `purchaseDate` |

## API

Base URL: `http://localhost:8080`

| Method | Path | Parameters | Description |
|---|---|---|---|
| `GET` | `/tickets/check-available` | none | Returns all tickets that have not been sold yet. |
| `POST` | `/tickets/add-ticket` | `price`, `category` | Adds a ticket and returns it. |
| `POST` | `/client/add-client` | `name`, `surname` | Adds a client and returns it. |
| `POST` | `/order/buy` | `clientId`, `ticketId` | Buys a ticket for a client and returns the saved purchase. |

Example session:

```bash
# add a client and two tickets
curl -X POST "http://localhost:8080/client/add-client?name=Jan&surname=Kowalski"
curl -X POST "http://localhost:8080/tickets/add-ticket?price=150.00&category=VIP"
curl -X POST "http://localhost:8080/tickets/add-ticket?price=80.00&category=Normal"

# list available tickets
curl "http://localhost:8080/tickets/check-available"

# client 1 buys ticket 1
curl -X POST "http://localhost:8080/order/buy?clientId=1&ticketId=1"
```

On Windows PowerShell use `curl.exe` instead of `curl`.

## Design decisions

- **Buying a ticket is one transaction.** Marking the ticket as sold, assigning its owner and saving the `Buy` record happen together (`@Transactional`), so the data cannot end up half-updated.
- **No double sales (optimistic locking).** `Ticket` has a `@Version` field. If two requests try to buy the same ticket at the same time, Hibernate adds the version to the `UPDATE` condition and only one of them succeeds. Checking the `available` flag alone would not be enough, because two requests can both read `available = true` before either one saves.
- **Money is stored as `BigDecimal`**, not `double`, to avoid floating-point rounding errors.
- **A separate purchase record (`Buy`)** keeps the history of who bought what and when, instead of only a flag on the ticket.
- **Constructor injection** is used everywhere, and the layers are kept separate so each one can be tested on its own.

## Quick start (Docker)

The easiest way to run everything: MySQL, the backend and the demo frontend.

**Requirement:** [Docker Desktop](https://www.docker.com/products/docker-desktop/) (running).

```bash
docker compose up --build
```

On Windows you can also double-click `start.bat`: it starts the containers, waits until the backend answers and opens the demo UI in the browser. `stop.bat` stops everything.

| Service | Address |
|---|---|
| Demo frontend | http://localhost:5500 |
| Backend API | http://localhost:8080 |
| MySQL (optional, e.g. for MySQL Workbench) | `127.0.0.1:3307`, database `tickets` |

The first build downloads the dependencies and can take a few minutes; later starts are fast. Data is kept in a Docker volume between runs. To remove it as well: `docker compose down -v`.

Make sure ports `5500` and `8080` are free (stop a locally running backend first). Database credentials default to `app` / `app` and can be overridden with the `DB_USER`, `DB_PASSWORD` and `DB_ROOT_PASSWORD` environment variables.

## Running without Docker

### Requirements

- JDK 25
- MySQL 8 running locally on port `3306`
- (optional, for the demo UI) a simple static file server, e.g. Python 3 or the VS Code *Live Server* extension

### 1. Create the database

In MySQL Workbench or the `mysql` client:

```sql
CREATE DATABASE tickets;
CREATE USER 'app'@'localhost' IDENTIFIED BY 'app';
GRANT ALL PRIVILEGES ON tickets.* TO 'app'@'localhost';
FLUSH PRIVILEGES;
```

These are local development credentials. For anything beyond local use, pick your own and do not commit them.

The tables are created automatically on the first start (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Check the configuration

`backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tickets
spring.datasource.username=app
spring.datasource.password=app
spring.jpa.hibernate.ddl-auto=update
```

### 3. Start the backend

```bash
cd backend
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

The API is available at `http://localhost:8080`.

### 4. Start the demo frontend (optional)

Serve the `frontend` folder with any static file server. It must run on `localhost` (not opened as a `file://` page), otherwise the browser blocks the requests to the API:

```bash
cd frontend
python -m http.server 5500
```

Then open `http://localhost:5500`. The backend address is set in `frontend/js/api.js`.

The demo UI has four pages: the list of available tickets, adding a ticket, adding a client, and buying a ticket.

## Roadmap

Planned improvements, in order of priority:

1. **Custom exceptions and global error handling.** At the moment business errors (for example buying a ticket that is already sold, or a missing client) are returned as generic server errors. They will be replaced by dedicated exceptions handled in one place with `@RestControllerAdvice`, returning proper HTTP statuses (`404 Not Found`, `409 Conflict`, `400 Bad Request`) and a consistent error body.
2. **Automated tests.**
   - Unit tests of the service layer (JUnit, Mockito).
   - Integration tests of the endpoints and the persistence layer.
   - **A concurrency test:** many threads try to buy the same ticket at once, and the test asserts that exactly one purchase succeeds and the rest are rejected. This will prove that the optimistic locking works.
3. **DTOs and request validation** (Bean Validation) so the API does not expose entities directly and validates input declaratively.
4. **API documentation** with Swagger / OpenAPI.
5. **Keep credentials out of the repository.** The Docker setup already reads them from environment variables; the remaining defaults in `application.properties` and `docker-compose.yml` should move to an untracked `.env` file.

## Notes

- The demo frontend was generated with AI to provide a quick way of trying the API. The backend (API design, data model, transactions and concurrency handling) is the actual work of this project.
- This is a learning project and is not intended for production use.
