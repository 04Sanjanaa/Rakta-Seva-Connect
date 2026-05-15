# Architecture Overview - Rakta-Seva Connect

Rakta-Seva Connect is built using **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** design pattern. This ensures the app is scalable, maintainable, and easy to test.

## High-Level Architecture

The app is divided into three main layers:

### 1. Presentation Layer (UI)
- **Framework**: Jetpack Compose (Declarative UI).
- **Components**:
  - **Screens**: Composable functions representing the visual interface.
  - **ViewModels**: Manage UI state and interact with the Domain layer. They use `StateFlow` to emit data to the UI.
  - **Navigation**: Uses `Navigation Compose` to handle transitions between screens.

### 2. Domain Layer (Core Business Logic)
- **Independence**: This layer does not depend on any Android or external libraries (except for core Kotlin).
- **Models**: Plain Kotlin data classes (`User`, `BloodRequest`).
- **Repository Interfaces**: Define the "contract" for data operations. The presentation layer depends on these interfaces, not their implementations (Dependency Inversion).

### 3. Data Layer (Data Sources)
- **Implementation**: Contains the actual code for fetching data from Firebase (Auth, Firestore).
- **Repositories**: Classes like `AuthRepositoryImpl` and `DonorRepositoryImpl` implement the domain interfaces.
- **Dependency Injection**: Hilt provides the concrete implementations of repositories to the ViewModels.

## Data Flow
1. **User Action**: User clicks a button in a Compose Screen.
2. **ViewModel**: The Screen calls a function in the ViewModel.
3. **Repository**: The ViewModel calls a function in the Repository interface.
4. **Data Source**: The Repository implementation interacts with Firebase.
5. **Flow/StateFlow**: The data flows back through the layers using Kotlin `Flow`.
6. **UI Update**: The ViewModel updates its `StateFlow`, which triggers a recomposition in the Screen.

## Why MVVM + Clean Architecture?
- **Separation of Concerns**: Each layer has a single responsibility.
- **Testability**: Business logic (Domain) can be tested without Android dependencies.
- **Flexibility**: If we switch from Firebase to another backend, only the Data layer needs to change; the UI and Domain layers remain untouched.
- **Maintainability**: New features can be added with minimal impact on existing code.
