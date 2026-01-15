import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_event.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      // Dispatch AppStartedEvent saat app start
      create: (_) => sl<AuthBloc>()..add(AppStartedEvent()),
      child: OrientationBuilder(
        builder: (context, orientation) {
          return ScreenUtilInit(
            designSize: orientation == Orientation.portrait
                ? const Size(390, 844) // Portrait design size
                : const Size(844, 390), // Landscape design size
            minTextAdapt: true,
            splitScreenMode: true,
            builder: (context, _) {
              return MaterialApp.router(
                routerConfig: AppRouter.router,
                theme: AppTheme.lightTheme,
                darkTheme: AppTheme.darkTheme,
                themeMode: ThemeMode.light,
                title: 'Drivero Automa',
                debugShowCheckedModeBanner: false,
                localeResolutionCallback: (locale, supportedLocales) {
                  // Check if device locale is supported
                  for (var supportedLocale in supportedLocales) {
                    if (supportedLocale.languageCode == locale?.languageCode &&
                        supportedLocale.countryCode == locale?.countryCode) {
                      return supportedLocale;
                    }
                  }
                  return supportedLocales.first;
                },
                builder: (ctx, child) {
                  return MediaQuery(
                    data: MediaQuery.of(ctx).copyWith(),
                    child: ScrollConfiguration(
                      behavior: MyBehavior(),
                      child: child!,
                    ),
                  );
                },
              );
            },
          );
        },
      ),
    );
  }
}

class MyBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}
