import 'package:camera/camera.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/models/user_model.dart';
import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'package:drivero_automa/presentation/auth/login_screen.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_bloc.dart';
import 'package:drivero_automa/presentation/home/bloc/vehicle_detail/vehicle_detail_bloc.dart';
import 'package:drivero_automa/presentation/home/home_screen.dart';
import 'package:drivero_automa/presentation/home/home_wrapper.dart';
import 'package:drivero_automa/presentation/home/models/base_screen_item.dart';
import 'package:drivero_automa/presentation/home/sos/sos_screen.dart';
import 'package:drivero_automa/presentation/home/sos/sos_wraper.dart';
import 'package:drivero_automa/presentation/home/vehicle_detail/vehicle_wrapper.dart';
import 'package:drivero_automa/presentation/home/widgets/custom_nav_bar.dart';
import 'package:drivero_automa/presentation/home/vehicle_detail/vehicle_detail_screen.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_bloc.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_event.dart';
import 'package:drivero_automa/presentation/last_maintenance/last_maintenance_screen.dart';
import 'package:drivero_automa/presentation/maintenance_detail/maintenance_detail_screen.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_bloc.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_event.dart';
import 'package:drivero_automa/presentation/next_schedule/next_schedule_screen.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_bloc.dart';
import 'package:drivero_automa/presentation/profile/pages/gallery/camera/camera.dart';
import 'package:drivero_automa/presentation/profile/pages/gallery/gallery.dart';
import 'package:drivero_automa/presentation/profile/pages/profile_screen.dart';
import 'package:drivero_automa/presentation/profile/pages/profile_edit_screen.dart';
import 'package:drivero_automa/presentation/schedule_history/schedule_history_screen.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_event.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:persistent_bottom_nav_bar_v2/persistent_bottom_nav_bar_v2.dart';

class AppRouter {
  // --- Route Paths ---
  static const String login = '/login';
  static const String home = '/home';
  static const vehicleDetail = '/vehicle-detail';

  static const String nextSchedule = '/next-schedule';
  static const String lastMaintenance = '/last-maintenance';
  static const String scheduleHistory = '/schedule-history';
  static const String maintenanceDetail = '/maintenance-detail';
  static const String profile = '/profile';
  static const String editProfile = '/profile/edit';
  static const String sos = '/sos';
  static const String gallery = '/gallery';
  static const String camera = '/camera';

  // --- Navigator Keys ---
  static final parentKey = GlobalKey<NavigatorState>();
  static final shellKey = GlobalKey<NavigatorState>();

  // --- Tabs Configuration ---
  static List<BaseScreenItem> _tabs(BuildContext context) {
    return [
      BaseScreenItem(title: 'Home', iconPath: Assets.icons.home),
      BaseScreenItem(title: 'Next', iconPath: Assets.icons.nextSchedule),
      BaseScreenItem(title: 'Last', iconPath: Assets.icons.lastMaintenance),
      BaseScreenItem(title: 'Schedule', iconPath: Assets.icons.schedule),
      BaseScreenItem(title: 'Profile', iconPath: Assets.icons.userProfile),
    ];
  }

  // --- Main Router ---
  static final router = GoRouter(
    navigatorKey: parentKey,
    initialLocation: AppRouter.login,
    routes: [
      // LOGIN
      GoRoute(
        path: AppRouter.login,
        builder: (context, state) => const LoginScreen(),
      ),

      // MAIN SHELL ROUTE
      StatefulShellRoute.indexedStack(
        builder: (context, state, navigationShell) => PersistentTabView.router(
          tabs: _tabs(context).map((tab) {
            return PersistentRouterTabConfig(
              item: ItemConfig(
                icon: SvgPicture.asset(
                  tab.iconPath,
                  width: AppSetting.setWidth(24),
                  height: AppSetting.setHeight(24),
                  colorFilter: ColorFilter.mode(
                    navigationShell.currentIndex == _tabs(context).indexOf(tab)
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
            );
          }).toList(),
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
          // --- HOME TAB ---
          StatefulShellBranch(
            navigatorKey: shellKey,
            routes: [
              GoRoute(
                name: AppRouter.home,
                path: AppRouter.home,
                builder: (context, state) {
                  return BlocProvider(
                    create: (_) => sl<HomeBloc>(),
                    child: HomeWrapper(child: const HomeScreen()),
                  );
                },
                routes: [
                  /// VEHICLE DETAIL ///
                  GoRoute(
                    path: AppRouter.vehicleDetail,
                    name: AppRouter.vehicleDetail,
                    pageBuilder: (context, state) {
                      final fleet = state.extra as DriverFleetEntity;
                      return NoTransitionPage(
                        key: state.pageKey,
                        child: BlocProvider(
                          create: (_) => sl<VehicleDetailBloc>()
                            ..add(
                              GetVehicleDetailEvent(vehicleId: fleet.vehicleId),
                            ), // ⬅️ EVENT DIJALANKAN DI SINI
                          child: VehicleWrapper(
                            child: VehicleDetailScreen(fleet: fleet),
                          ),
                        ),
                      );
                    },
                  ),

                  ///////////////////

                  //// SOS SCREEN////=================////
                  // --- SOS PAGE ---
                  GoRoute(
                    name: AppRouter.sos,
                    path: AppRouter.sos,
                    pageBuilder: (context, state) => NoTransitionPage(
                      key: state.pageKey,
                      child: SosWrapper(child: const SosScreen()),
                    ),
                  ),
                ],
              ),
            ],
          ),

          // --- NEXT SCHEDULE TAB ---
          StatefulShellBranch(
            routes: [
              GoRoute(
                path: AppRouter.nextSchedule,
                builder: (context, state) {
                  return BlocProvider(
                    create: (_) => NextScheduleBloc(
                      getMaintenances: sl<GetMaintenancesUsecase>(),
                    )..add(LoadNextScheduleEvent()),
                    child: const NextScheduleScreen(),
                  );
                },
                routes: [
                  GoRoute(
                    name: AppRouter.maintenanceDetail,
                    path: AppRouter.maintenanceDetail,
                    builder: (context, state) {
                      final maintenance = state.extra as MaintenanceEntity;
                      return MaintenanceDetailScreen(maintenance: maintenance);
                    },
                  ),
                ],
              ),
            ],
          ),

          // --- LAST MAINTENANCE TAB ---
          StatefulShellBranch(
            routes: [
              GoRoute(
                path: AppRouter.lastMaintenance,
                builder: (context, state) => BlocProvider(
                  create: (context) =>
                      sl<LastMaintenanceBloc>()..add(GetLastMaintenanceEvent()),
                  child: const LastMaintenanceScreen(),
                ),
              ),
            ],
          ),

          // --- SCHEDULE HISTORY TAB ---
          StatefulShellBranch(
            routes: [
              GoRoute(
                path: AppRouter.scheduleHistory,
                builder: (context, state) => BlocProvider(
                  create: (context) => sl<EventBloc>()..add(LoadTasks()),
                  child: const ScheduleHistoryScreen(),
                ),
              ),
            ],
          ),

          // --- PROFILE TAB ---
          StatefulShellBranch(
            routes: [
              GoRoute(
                path: AppRouter.profile,
                builder: (context, state) => const ProfileScreen(),
                routes: [
                  GoRoute(
                    name: AppRouter.editProfile,
                    path: AppRouter.editProfile,
                    builder: (context, state) => BlocProvider(
                      create: (context) => sl<ProfileBloc>(),
                      child: const EditProfilePage(),
                    ),
                  ),
                  GoRoute(
                    name: AppRouter.gallery,
                    path: AppRouter.gallery,
                    pageBuilder: (context, state) => NoTransitionPage(
                      key: state.pageKey,
                      child: const GalleryScreen(),
                    ),
                  ),
      // app_router.dart

GoRoute(
  path: 'camera',       // path url (relatif, tanpa / di depan jika di dalam sub-route)
  name: 'camera',       // <--- PENTING: Ini nama yang dipanggil oleh pushNamed
  builder: (context, state) {
    // Pastikan casting type-nya benar
    List<CameraDescription> cameras = [];
    if (state.extra != null) {
      cameras = state.extra as List<CameraDescription>;
    }
    return CameraPage(cameras: cameras);
  },
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
