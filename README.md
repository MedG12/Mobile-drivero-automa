# Drivero Automa


## 📱 Project Overview

Drivero Automa is a Flutter application that follows clean architecture principles with feature-based folder structure. The project uses BLoC for state management, GoRouter for navigation, and ScreenUtil for responsive design.

**Flutter Version**: This project was developed using Flutter 3.35.4

## 🏗️ Project Structure

```
lib/
├── app.dart                    # Main app widget with theme and routing configuration
├── bootstrap.dart              # App initialization and error handling setup
├── main.dart                  # Entry point of the application
├── config/
│   └── app_config.dart        # App configuration, spacing, and theme utilities
├── core/
│   ├── injector/
│   │   └── service_locator.dart # Dependency injection setup
│   └── widgets/               # Reusable widgets (currently empty)
├── features/                  # Feature-based modules
│   ├── auth/
│   │   ├── data/             # Data layer (repositories, data sources)
│   │   ├── logic/            # Business logic layer (BLoCs)
│   │   └── presentation/     # UI layer (screens, widgets)
│   └── home/
│       ├── data/
│       ├── logic/
│       │   ├── home/         # Home feature BLoC
│       │   └── vehicle_detail/ # Vehicle detail BLoC
│       └── presentation/
├── gen/                      # Generated files (assets, fonts)
│   ├── assets.gen.dart       # Generated asset references
│   └── fonts.gen.dart        # Generated font references
├── routes/
│   └── app_router.dart       # Navigation configuration
├── theme/
│   ├── color_theme.dart      # Color definitions
│   └── theme.dart            # Theme configuration
└── utils/                    # Utility classes and helpers
    ├── date/
    ├── image/
    └── manager/
```

### 📁 Folder Responsibilities

#### **`/config`**
Contains application configuration and utility classes for responsive design.

#### **`/core`**
Contains core functionality that's used across the entire application:
- **`/injector`**: Dependency injection setup using GetIt
- **`/widgets`**: Reusable widgets that can be used across features

#### **`/features`**
Feature-based modules following clean architecture:
- **`/data`**: Data layer containing repositories, data sources, and models
- **`/logic`**: Business logic layer containing BLoCs, events, and states
- **`/presentation`**: UI layer containing screens and feature-specific widgets

#### **`/gen`**
Auto-generated files created by flutter_gen_runner:
- Asset references for type-safe asset access
- Font family constants

#### **`/routes`**
Navigation configuration using GoRouter.

#### **`/theme`**
Theme-related configurations including colors and text styles.

#### **`/utils`**
Utility classes and helper functions:
- **`/date`**: Date formatting utilities
- **`/image`**: Image handling utilities
- **`/manager`**: Shared preferences management

## 🎨 Responsive Design & Spacing

### Using the Space Class

The `Space` class provides convenient methods to create responsive spacing:

```dart
import 'package:drivero_automa/config/app_config.dart';

// Horizontal spacing
Space.w(16), // Creates SizedBox with responsive width
Space.w(24),

// Vertical spacing
Space.h(12), // Creates SizedBox with responsive height
Space.h(20),
```

### Using AppSetting for Responsive Design

The `AppSetting` class provides utilities for responsive design:

```dart
import 'package:drivero_automa/config/app_config.dart';

// Device size information
double screenWidth = AppSetting.deviceWidth;
double screenHeight = AppSetting.deviceHeight;

// Responsive dimensions
double responsiveWidth = AppSetting.setWidth(100); // 100 logical pixels
double responsiveHeight = AppSetting.setHeight(50); // 50 logical pixels

// Responsive font size
double fontSize = AppSetting.setFontSize(16); // 16sp

// Device type checks
bool isLargePhone = AppSetting.isLargePhone(context);
bool isNormalPhone = AppSetting.isNormalPhone(context);
bool isSmallPhone = AppSetting.isSmallPhone(context);
bool isTablet = AppSetting.isTablet(context);

// Height-based checks
bool isNormalHeight = AppSetting.isNormalPhoneHeight(context);
bool isSmallHeight = AppSetting.isSmallPhoneHeight(context);
bool isBigHeight = AppSetting.isBigPhoneHeight(context);
```

### Design System

The app is configured with a design size of:
- **Portrait**: 1080 x 1920 pixels
- **Landscape**: 1920 x 1080 pixels

All dimensions should be based on these design sizes for consistent responsive behavior.

## 🔧 Dependency Injection with Service Locator

### Overview

The project uses GetIt for dependency injection, centralized in `service_locator.dart`.

### How to Use Service Locator

#### 1. Register Dependencies

Add your BLoCs, repositories, or services in `initServiceLocator()`:

```dart
// In service_locator.dart
void initServiceLocator() {
  // Register BLoCs as factories (new instance each time)
  sl.registerFactory(() => HomeBloc());
  sl.registerFactory(() => VehicleDetailBloc());
  
  // Register repositories as singletons
  sl.registerLazySingleton<UserRepository>(() => UserRepositoryImpl());
  
  // Register BLoCs with dependencies
  sl.registerFactory(() => AuthBloc(userRepository: sl()));
}
```

#### 2. Inject Dependencies in Routes

Use service locator in your route configuration:

```dart
// In app_router.dart
GoRoute(
  path: '/home',
  builder: (context, state) {
    return BlocProvider(
      create: (context) => sl<HomeBloc>(), // Get from service locator
      child: const HomeScreen(),
    );
  },
),
```

#### 3. Access Dependencies in Widgets

```dart
// In any widget or class
final homeBloc = sl<HomeBloc>();
final userRepository = sl<UserRepository>();
```

### When to Use Service Locator

- **BLoCs**: Register all BLoCs to manage their lifecycle
- **Repositories**: Register data repositories for dependency injection
- **Services**: Register API services, storage services, etc.
- **Utilities**: Register utility classes that need to be shared

### Registration Types

- **`registerFactory()`**: Creates a new instance every time (use for BLoCs)
- **`registerSingleton()`**: Creates instance immediately and reuses it
- **`registerLazySingleton()`**: Creates instance on first access and reuses it

## 🎯 Asset Generation with flutter_gen_runner

### Overview

The project uses `flutter_gen_runner` to generate type-safe asset references, eliminating typos and providing IDE autocompletion.

### How to Generate Assets

#### 1. Run the Generation Command

```bash
# Generate assets and fonts
flutter packages pub run build_runner build

# Or watch for changes (recommended during development)
flutter packages pub run build_runner watch
```

#### 2. Using Generated Assets

```dart
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';

// Using generated image assets
Assets.images.test.image(), // Returns Image widget
Assets.images.test.path,    // Returns asset path string

// Using generated fonts
TextStyle(
  fontFamily: FontFamily.inter, // Type-safe font family
)
```

### When to Regenerate Assets

Run the generation command when you:
- Add new images to `assets/images/`
- Add new icons to `assets/icons/`
- Add new fonts to `assets/font/`
- Modify `pubspec.yaml` asset declarations

### Asset Organization

```
assets/
├── images/          # App images (.png, .jpg, .jpeg, .gif, .webp)
├── icons/           # App icons (.svg, .png)
└── font/           # Custom fonts (.ttf, .otf)
    ├── Inter-Regular.ttf
    ├── Inter-Bold.ttf
    └── ...
```

## 🎨 Theming

### Color System

Colors are defined in `color_theme.dart` using a singleton pattern:

```dart
import 'package:drivero_automa/theme/theme.dart';

// Access colors
MyTheme.color.primary    // Primary color
MyTheme.color.secondary  // Secondary color
MyTheme.color.success    // Success color
MyTheme.color.danger     // Danger/error color
MyTheme.color.white      // White
MyTheme.color.black      // Black
```

### Text Styles

```dart
import 'package:drivero_automa/theme/theme.dart';

// Using default text style with Inter font
Text(
  'Hello World',
  style: defaultTextStyle(context),
)

// Using theme text styles
Text(
  'Body Large',
  style: Theme.of(context).textTheme.bodyLarge,
)
```

## 🚀 Getting Started

### Prerequisites

- Flutter SDK ^3.8.1
- Dart SDK
- Android Studio / VS Code
- Android/iOS development setup

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd drivero_automa
```

2. **Install dependencies**
```bash
flutter pub get
```

3. **Generate assets**
```bash
flutter packages pub run build_runner build
```

4. **Run the app**
```bash
flutter run
```

### Development Workflow

1. **Adding New Features**
   - Create feature folder in `/features`
   - Follow the data-logic-presentation structure
   - Register BLoCs in service locator
   - Add routes in app router

2. **Adding New Assets**
   - Place assets in appropriate folders
   - Run asset generation
   - Use generated references in code

3. **Styling Components**
   - Use `AppSetting` for responsive dimensions
   - Use `Space` class for consistent spacing
   - Follow the established color system

## 📦 Key Dependencies

- **flutter_bloc**: State management
- **go_router**: Navigation and routing
- **get_it**: Dependency injection
- **flutter_screenutil**: Responsive design
- **google_fonts**: Custom fonts
- **dio**: HTTP client
- **shared_preferences**: Local storage
- **image_picker**: Image selection
- **flutter_svg**: SVG support
- **logger**: Logging utility
- **equatable**: Value equality
- **intl**: Internationalization

## 🧪 Testing

```bash
# Run tests
flutter test

# Run tests with coverage
flutter test --coverage
```

## 🔨 Build

```bash
# Build APK
flutter build apk

# Build iOS
flutter build ios

# Build for release
flutter build apk --release
flutter build ios --release
```

## 📝 Code Style

- Follow Dart/Flutter conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep widgets small and focused
- Use const constructors where possible

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Add tests if applicable
4. Run `flutter analyze` and fix any issues
5. Run `flutter test` to ensure tests pass
6. Submit a pull request
