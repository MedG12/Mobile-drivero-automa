import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_event.dart';
import 'package:drivero_automa/features/home/presentation/home_wrapper.dart';
import 'package:drivero_automa/features/home/presentation/models/base_screen_item.dart';
import 'package:drivero_automa/features/home/presentation/widgets/custom_nav_bar.dart';
import 'package:drivero_automa/features/next_schedule/presentation/next_schedule_screen.dart';
import 'package:drivero_automa/features/last_maintenance/presentation/last_maintenance_screen.dart';
import 'package:drivero_automa/features/profile/presentation/pages/profile_screen.dart';
import 'package:drivero_automa/features/profile/presentation/pages/profile_screen%20_edit.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_event.dart';
import 'package:drivero_automa/features/schedule_history/presentation/schedule_history_screen.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_svg/svg.dart';
import 'package:go_router/go_router.dart';

import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:drivero_automa/features/home/presentation/bloc/home/home_bloc.dart';
import 'package:drivero_automa/features/home/presentation/bloc/vehicle_detail/vehicle_detail_bloc.dart';
import 'package:drivero_automa/features/auth/presentation/login_screen.dart';
import 'package:drivero_automa/features/home/presentation/home_screen.dart'
    hide NextScheduleScreen;
import 'package:drivero_automa/features/home/presentation/vehicle_detail_screen.dart';
import 'package:persistent_bottom_nav_bar_v2/persistent_bottom_nav_bar_v2.dart';

class AppRouter {
  static const String base = '/';
  static const String login = '/login';

  static const String home = '/home';
  static const String vehicleDetail = '/vehicle-detail';

  static const String nextSchedule = '/next-schedule';

  static const String lastMaintenance = '/last-maintenance';

  static const String scheduleHistory = '/schedule-history';

  static const String profile = '/profile';

  static const String editProfile = '/profile/edit';

  static final parentKey = GlobalKey<NavigatorState>();
  static final shellKey = GlobalKey<NavigatorState>();

  static List<BaseScreenItem> _tabs(BuildContext context) {
    return [
      BaseScreenItem(title: 'Home', iconPath: Assets.icons.home),
      BaseScreenItem(title: 'Next', iconPath: Assets.icons.nextSchedule),
      BaseScreenItem(title: 'Last', iconPath: Assets.icons.lastMaintenance),
      BaseScreenItem(title: 'Schedule', iconPath: Assets.icons.schedule),
      BaseScreenItem(title: 'Profile', iconPath: Assets.icons.userProfile),
    ];
  }

  static final router = GoRouter(
    initialLocation: AppRouter.login,
    navigatorKey: parentKey,
    routes: [
      GoRoute(
        path: AppRouter.login,
        builder: (context, state) => const LoginScreen(),
        routes: [
          StatefulShellRoute.indexedStack(
            builder: (context, state, navigationShell) =>
                PersistentTabView.router(
                  tabs: _tabs(context)
                      .map(
                        (tab) => PersistentRouterTabConfig(
                          item: ItemConfig(
                            icon: SvgPicture.asset(
                              tab.iconPath,
                              width:
                                  navigationShell.currentIndex ==
                                      _tabs(context).indexOf(tab)
                                  ? AppSetting.setWidth(24)
                                  : AppSetting.setWidth(31),
                              height:
                                  navigationShell.currentIndex ==
                                      _tabs(context).indexOf(tab)
                                  ? AppSetting.setHeight(24)
                                  : AppSetting.setHeight(31),
                              colorFilter: ColorFilter.mode(
                                navigationShell.currentIndex ==
                                        _tabs(context).indexOf(tab)
                                    ? MyTheme.color.white
                                    : MyTheme.color.grey,
                                BlendMode.srcIn,
                              ),
                            ),
                            title: tab.title,
                            activeForegroundColor: MyTheme.color.white,
                            inactiveForegroundColor: MyTheme.color.grey,
                            textStyle: TextStyle(
                              fontFamily: FontFamily.inter,
                              fontSize: AppSetting.setFontSize(12),
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                        ),
                      )
                      .toList(),
                  navBarBuilder: (navBarConfig) => CustomNavBar(
                    navBarConfig: navBarConfig,
                    itemPadding: const EdgeInsets.symmetric(
                      horizontal: 10,
                      vertical: 4,
                    ),
                    height: AppSetting.setHeight(76),
                  ),
                  navigationShell: navigationShell,
                ),
            branches: [
              // The route branch for the 1st Tab
              StatefulShellBranch(
                navigatorKey: shellKey,
                routes: <RouteBase>[
                  GoRoute(
                    name: AppRouter.home,
                    path: AppRouter.home,
                    builder: (context, state) => BlocProvider(
                      create: (context) => sl<HomeBloc>(),
                      child: HomeWrapper(child: HomeScreen()),
                    ),
                    routes: [
                      GoRoute(
                        name: AppRouter.vehicleDetail,
                        path: AppRouter.vehicleDetail,
                        pageBuilder: (context, state) => NoTransitionPage(
                          key: state.pageKey,
                          child: BlocProvider(
                            create: (context) => sl<VehicleDetailBloc>(),
                            child: HomeWrapper(
                              child: const VehicleDetailScreen(),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),

              // The route branch for 2nd Tab
              StatefulShellBranch(
                routes: <RouteBase>[
                  GoRoute(
                    name: AppRouter.nextSchedule,
                    path: AppRouter.nextSchedule,
                    builder: (context, state) => const NextScheduleScreen(),
                    routes: [],
                  ),
                ],
              ),

              // The route branch for 3rd Tab
              StatefulShellBranch(
                routes: <RouteBase>[
                  GoRoute(
                    name: AppRouter.lastMaintenance,
                    path: AppRouter.lastMaintenance,
                    builder: (context, state) => const LastMaintenanceScreen(),
                    routes: [], //
                  ),
                ],
              ),

              // The route branch for 4th Tab
              StatefulShellBranch(
                routes: <RouteBase>[
                  GoRoute(
                    name: AppRouter.scheduleHistory,
                    path: AppRouter.scheduleHistory,
                    builder: (context, state) => BlocProvider(
                      create: (context) => sl<EventBloc>()..add(LoadTasks()),
                      child: const ScheduleHistoryScreen(),
                    ),
                    routes: [], // SubRoutes
                  ),
                ],
              ),

              // The route branch for 5th Tab
              StatefulShellBranch(
                routes: <RouteBase>[
                  GoRoute(
                    name: AppRouter.profile,
                    path: AppRouter.profile,
                    builder: (context, state) => const ProfileScreen(),
                    routes: [
                      GoRoute(
                        name: AppRouter.editProfile,
                        path: AppRouter.editProfile,
                        builder: (context, state) => const EditProfilePage(),
                      ),
                    ], // SubRoutes
                  ),
                ],
              ),
            ],
          ),
        ],
      ),
    ],
  );
}
