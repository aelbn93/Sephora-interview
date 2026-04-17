# Sephora — Android Interview Project

> ⚠️ **Over-engineering disclaimer**: This is a single-feature app built with the same architecture
> you'd use for a large-scale, multi-team production application. A real app of this scope would
> not need this level of modularization. This is intentional — the goal is to demonstrate
> familiarity with modern Android architecture patterns as seen in
> [Now in Android](https://github.com/android/nowinandroid).

## Features

The app has a single feature: **Products**. It allows users to:

- Browse a list of beauty products with reviews
- Search products by name
- Sort products by review rating (best to worst / worst to best)

---

## Architecture

The app follows the
[official Android architecture guidance](https://developer.android.com/topic/architecture) and is
 inspired by the [Now in Android](https://github.com/android/nowinandroid) project.

The architecture is built around three main principles:

- **Unidirectional Data Flow (UDF)** — state flows down, events flow up
- **Single source of truth** — the local Room database is the source of truth, synced from the network
- **Offline-first** — the app reads from the database, never directly from the network

### Data Flow

```
Network (Retrofit) ──sync()──▶ Room Database ──Flow──▶ Repository ──Flow──▶ ViewModel ──StateFlow──▶ UI
```

---

## Modularization

The app is modularized. Each module has a single,
well-defined responsibility.

```
sephora-app/
├── app/                          # Application module, DI wiring, navigation root
│
├── core/
│   ├── common/                   # Shared utilities, coroutine dispatchers
│   ├── data/                     # Repository implementations, data models/mappers
│   ├── database/                 # Room database, DAOs, entities
│   ├── designsystem/             # Design tokens, shared Composables, icons
│   ├── model/                    # Pure Kotlin domain models (Product, Review)
│   ├── network/                  # Retrofit, network DTOs, SephoraNetworkDataSource
│   ├── testing/                  # Test doubles, shared test data, MainDispatcherRule
│   └── ui/                       # Shared UI components (TopAppBar, LoadingWheel)
│
└── feature/
    └── product/
        ├── api/                  # Public navigation contract (no implementation details)
        └── impl/                 # ProductViewModel, ProductsScreen, ProductUiState
```

### Module graph

```
         :app
           │
    ┌──────┴──────┐
    │             │
:feature:      :core:
product:impl   designsystem
    │
:feature:
product:api
    │
 :core:data ──▶ :core:database
    │                │
 :core:network    :core:model
    │
 :core:model
```

---

## Modules in detail

### `:app`
The top-level application module. It wires together all feature and core modules, hosts the
`NavHost`, and provides the application-level Hilt component.

### `:core:model`
Pure Kotlin module. Contains the domain models (`Product`, `Review`) shared across all layers.
Has no Android dependencies.

### `:core:network`
Handles all network communication. Contains:
- `SephoraNetworkDataSource` interface
- Retrofit + Kotlinx Serialization implementation
- Network DTOs (`NetworkProduct`, `NetworkProductReview`)

### `:core:database`
Handles local persistence. Contains:
- Room database (`SephoraDatabase`)
- Entities (`ProductEntity`, `ReviewEntity`)
- DAOs (`ProductDao`)
- Relation models (`ProductWithReviews`)

### `:core:data`
The bridge between network and database. Contains:
- `ProductRepository` interface
- `DefaultProductRepository` — offline-first implementation
- Mappers between network DTOs, database entities, and domain models

The repository pattern here is **offline-first**: `sync()` fetches from the network and writes to
Room, while all reads come from Room via `Flow`. This means the UI always reflects the local
database state.

### `:core:common`
Shared Kotlin utilities. Contains coroutine dispatcher bindings (`SephoraDispatchers`, `@Dispatcher`)
used to inject `IO` and `Default` dispatchers for testability.

### `:core:designsystem`
Contains the Sephora design system:
- Color tokens (`SephoraColors`)
- Typography
- Shared Composable components (`SephoraLoadingWheel`)
- Icon definitions (`SephoraIcons`)

### `:core:ui`
Composables that depend on both `:core:designsystem` and `:core:model` — components that are too
domain-aware to live in the design system but too reusable to live in a feature module.

### `:core:testing`
Test-only module. Contains everything needed to test other modules in isolation:
- `TestProductRepository` — in-memory fake for unit/UI tests
- `MainDispatcherRule` — JUnit rule for coroutine testing
- Shared test data (`sampleProducts`, `sampleNetworkProducts`, etc.)

Only depended on via `testImplementation` / `androidTestImplementation`.

### `:feature:product:api`
Defines the public contract of the product feature — the navigation destination — without exposing
any implementation details. Other features or `:app` depend on this to navigate to the product
feature.

### `:feature:product:impl`
The full product feature implementation:
- `ProductViewModel` — manages `ProductUiState`, search query via `SavedStateHandle`, sort field
- `ProductsScreen` — stateless Composable, driven entirely by state from the ViewModel
- `ProductUiState` — sealed interface with `Loading`, `Success`, `Error`

---

## Testing strategy

The testing approach mirrors Now in Android — each layer is tested in isolation using the
appropriate test double.

| Layer | Test type | Source set | Approach |
|---|---|---|---|
| DAO | Instrumented | `androidTest` | Real Room in-memory database |
| Mappers | Unit | `test` | Pure function assertions, no fakes needed |
| Repository | Unit | `test` | `TestProductDao` + `TestSephoraNetworkDataSource` |
| ViewModel | Unit | `test` | `TestProductRepository` + `MainDispatcherRule` |
| Screen (UI) | Instrumented | `androidTest` | `createAndroidComposeRule`, no Hilt |

### Key testing conventions

**Test doubles over mocks** — all fakes are hand-written classes implementing the real interface,
living in `:core:testing` or in a `testdoubles` package local to the module under test. No
Mockito or MockK.

**Stateless screen testing** — UI tests call the `internal` Composable overload that accepts
explicit state, bypassing `hiltViewModel()` entirely. This keeps UI tests fast and hermetic.

**`TestProductRepository`** uses `MutableSharedFlow(replay = 1)` and exposes a `sendProducts()`
API to push data, following the same pattern as NiA's `TestTopicsRepository`.

**`TestProductDao`** and **`TestSephoraNetworkDataSource`** live in `:core:data`'s
`testdoubles` package (not in `:core:testing`) to avoid circular dependencies.

---

## Potential enhancements

These are out of scope for an interview project but would be natural next steps in a production app:

- **Baseline Profiles** — pre-compiled traces to improve startup and scroll performance
  (`androidx.benchmark:benchmark-macro-junit4`)
- **Macrobenchmark** — automated performance regression testing
- **Analytics** — event tracking module (`:core:analytics`)
- **Screenshot testing** — Roborazzi-based snapshot tests for Composables
- **Pagination** — `Paging 3` for large product lists
- **Multi-module navigation testing** — `androidx.navigation:navigation-testing`
- **CI pipeline** — GitHub Actions with lint, unit tests, and instrumented test jobs

---

## Tech stack

| Category | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, UDF, Clean Architecture |
| DI | Hilt |
| Navigation | Navigation 3 |
| Local persistence | Room |
| Networking | Retrofit, Kotlinx Serialization |
| Async | Kotlin Coroutines, Flow |
| Images | Coil |
| Testing | JUnit 4, kotlin.test, Turbine, Coroutines Test |

---

## Convention plugins

Build logic is centralized in `build-logic/` using convention plugins, following the Now in Android
approach:

| Plugin | Purpose |
|---|---|
| `sephora.android.library` | Base config for all Android library modules |
| `sephora.android.library.compose` | Adds Compose to a library module |
| `sephora.android.feature.impl` | Feature implementation module config |
| `sephora.android.feature.api` | Feature API module config |
| `sephora.android.room` | Adds Room + KSP to a module |
| `sephora.hilt` | Adds Hilt + KSP to a module |
| `sephora.jvm.library` | Pure Kotlin/JVM module (no Android) |