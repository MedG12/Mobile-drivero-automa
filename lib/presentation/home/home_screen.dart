import 'package:drivero_automa/data/models/flet_img_model.dart';
import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_event.dart';
import 'package:drivero_automa/presentation/home/widgets/Vehicle_List.dart';
import 'package:drivero_automa/presentation/home/widgets/upcoming_maintenance.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_bloc.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_state.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final authState = context.read<AuthBloc>().state;
      if (authState is Authenticated) {
        final userId = authState.user.userId;
        context.read<HomeBloc>().add(FetchUpcomingMaintenances());
        context.read<HomeBloc>().add(FetchDriverFleet(userId: userId));
      }
    });
  }

  Future<void> _onRefresh() async {
    final authState = context.read<AuthBloc>().state;
    if (authState is Authenticated) {
      final userId = authState.user.userId;
      context.read<HomeBloc>().add(RefreshHomeData(userId: userId));

      // Tunggu sampai loading selesai
      await Future.delayed(const Duration(milliseconds: 500));
    }
  }

  @override
  Widget build(BuildContext context) {
    return BackgroundPageWidget(
      child: BlocBuilder<HomeBloc, HomeState>(
        builder: (context, state) {
          return Stack(
            children: [
              RefreshIndicator(
                // ← Wrap dengan RefreshIndicator
                onRefresh: _onRefresh,
                color: MyTheme.color.primary,
                backgroundColor: MyTheme.color.white,
                child: SingleChildScrollView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  child: Padding(
                    padding: EdgeInsets.fromLTRB(
                      AppSetting.setWidth(24),
                      AppSetting.setHeight(50),
                      AppSetting.setWidth(24),
                      AppSetting.setHeight(20),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // ==== HELLO USER ====
                        BlocBuilder<AuthBloc, AuthState>(
                          builder: (context, authState) {
                            if (authState is Authenticated) {
                              return Text(
                                "Hello, ${authState.user.name}",
                                style: TextStyle(
                                  fontSize: AppSetting.setFontSize(18),
                                  color: MyTheme.color.white,
                                  fontWeight: FontWeight.w600,
                                ),
                              );
                            }
                            return const SizedBox();
                          },
                        ),
                        SizedBox(height: AppSetting.setHeight(10)),
                        Text(
                          "LET'S TAKE A LOOK FOR YOUR CAR!",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(10),
                            color: MyTheme.color.white,
                            fontWeight: FontWeight.w400,
                          ),
                        ),
                        SizedBox(height: AppSetting.setHeight(25)),
                        // ==== VEHICLE LIST ====
                        Text(
                          "Vehicle List",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(18),
                            color: MyTheme.color.white,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                        SizedBox(height: AppSetting.setHeight(12)),
                        if (state.isDriverFleetLoading)
                          const Text(
                            "Loading...",
                            style: TextStyle(color: Colors.white),
                          )
                        else if (state.error != null)
                          Text(
                            "Error: ${state.error}",
                            style: const TextStyle(color: Colors.red),
                          )
                        else if (state.fleets.isEmpty)
                          const Text(
                            "No vehicles found",
                            style: TextStyle(color: Colors.white),
                          )
                        else
                          Column(
                            children: state.fleets.map((fleet) {
                              final fleetImage = state.images.firstWhere(
                                (img) => img.idFleet == fleet.vehicleId,
                                orElse: () => FleetImageEntity(
                                  id: 0,
                                  idFleet: fleet.vehicleId,
                                  typeImage: 0,
                                  nameTypeImage: '',
                                  link: '',
                                  desc: '',
                                  createdOn: DateTime.now(),
                                ),
                              );
                              return VehicleCard(
                                fleet: fleet,
                                imageUrl: fleetImage.link,
                              );
                            }).toList(),
                          ),
                        SizedBox(height: AppSetting.setHeight(25)),
                        // ==== UPCOMING MAINTENANCE ====
                        _buildUpcomingMaintenance(state.maintenances),
                        SizedBox(height: AppSetting.setHeight(100)),
                      ],
                    ),
                  ),
                ),
              ),
              // Floating SOS button
              Positioned(
                right: AppSetting.setWidth(10),
                bottom: AppSetting.setHeight(13),
                child: GestureDetector(
                  onTap: () {
                    context.push('${AppRouter.home}${AppRouter.sos}');
                  },
                  child: Container(
                    width: AppSetting.setWidth(66),
                    height: AppSetting.setWidth(66),
                    padding: EdgeInsets.all(AppSetting.setWidth(18)),
                    decoration: BoxDecoration(
                      color: MyTheme.color.primary,
                      shape: BoxShape.circle,
                    ),
                    child: SvgPicture.asset(
                      'assets/icons/perintah.svg',
                      width: AppSetting.setWidth(10),
                      height: AppSetting.setHeight(10),
                      colorFilter: ColorFilter.mode(
                        MyTheme.color.white,
                        BlendMode.srcIn,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );
  }

  Widget _buildUpcomingMaintenance(List<MaintenanceEntity> maintenances) {
    // ... kode yang sama, tidak ada perubahan
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              "Upcoming Maintenance",
              style: TextStyle(
                fontSize: AppSetting.setFontSize(18),
                color: MyTheme.color.white,
                fontWeight: FontWeight.w600,
              ),
            ),
            GestureDetector(
              onTap: () => context.go(AppRouter.nextSchedule),
              child: Container(
                padding: EdgeInsets.symmetric(
                  vertical: AppSetting.setHeight(4),
                  horizontal: AppSetting.setWidth(12),
                ),
                decoration: BoxDecoration(
                  color: MyTheme.color.white,
                  borderRadius: BorderRadius.circular(100),
                ),
                child: Text(
                  "See All",
                  style: TextStyle(
                    fontSize: AppSetting.setFontSize(14),
                    color: MyTheme.color.primary,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ),
            ),
          ],
        ),
        SizedBox(height: AppSetting.setHeight(12)),
        if (maintenances.isEmpty)
          Text(
            "No upcoming maintenance schedules.",
            style: TextStyle(
              fontSize: AppSetting.setFontSize(14),
              color: MyTheme.color.white,
              fontWeight: FontWeight.w400,
            ),
          )
        else
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Row(
              children: maintenances.map((schedule) {
                return Padding(
                  padding: EdgeInsets.only(right: AppSetting.setWidth(12)),
                  child: SizedBox(
                    width: AppSetting.setWidth(250),
                    child: UpcomingMaintenanceCard(
                      date: DateFormat(
                        'd MMM yyyy - HH:mm',
                      ).format(schedule.dateTimes!),
                      title: schedule.title,
                      service: schedule.maintenanceType,
                      plate: schedule.licensePlat,
                    ),
                  ),
                );
              }).toList(),
            ),
          ),
      ],
    );
  }
}
