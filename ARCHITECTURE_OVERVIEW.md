# Architecture Overview - Rakta-Seva Connect

Rakta-Seva Connect is architected using **MVVM (Model-View-ViewModel)** combined with **Clean Architecture** principles to ensure a modular, testable, and maintainable codebase.

## 1. Architectural Layers

### Presentation Layer (UI)
- **Jetpack Compose**: Handles the declarative UI components.
- **ViewModels**: Manage the UI state using `StateFlow` and interact with the Domain layer. ViewModels are scoped to the screens they serve.
- **Navigation**: Uses `Navigation Compose` with a centralized `NavGraph`.

### Domain Layer (Business Logic)
- **Entities/Models**: Core data classes (`User`, `BloodRequest`) that represent the application's data.
- **Repository Interfaces**: Define the contracts for data operations, ensuring the domain is independent of the data source implementation.

### Data Layer (Infrastructure)
- **Repositories**: Implement the domain interfaces.
- **Data Sources**: Interact directly with external services like Firebase Auth and Firestore.
- **Dependency Injection**: Hilt is used to inject repository implementations into ViewModels.

## 2. Data Flow (Reactive Pattern)
1. **User Interaction**: User triggers an event (e.g., clicking "Request Blood").
2. **ViewModel Action**: The ViewModel receives the event and initiates a request through a Repository.
3. **Repository Execution**: The Repository interacts with Firebase (Firestore/Auth) and returns a `Flow<Resource<T>>`.
4. **State Update**: The ViewModel collects the flow and updates its `StateFlow`.
5. **UI Recomposition**: The Compose UI observes the `StateFlow` and recomposes to show the latest data or state (Loading, Success, Error).

## 3. Dependency Injection
The project uses **Dagger Hilt** for dependency injection. The `AppModule` provides singleton instances of:
- `FirebaseAuth`
- `FirebaseFirestore`
- `AuthRepository` (Implementation)
- `DonorRepository` (Implementation)

## 4. Key Design Patterns
- **Repository Pattern**: Abstracts data access.
- **Sealed Class for Results**: `Resource.kt` handles Loading, Success, and Error states uniformly across the app.
- **Single Source of Truth**: Firestore serves as the real-time source of truth for all shared data.
