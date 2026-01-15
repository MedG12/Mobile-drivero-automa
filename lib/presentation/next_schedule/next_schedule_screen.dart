import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/core/widgets/invoice_dialog.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_bloc.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_event.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_state.dart';
import 'package:drivero_automa/presentation/next_schedule/next_schedule_screen_wrapper.dart';
import 'package:drivero_automa/presentation/next_schedule/widgets/completed_maintenance_card.dart';
import 'package:drivero_automa/presentation/next_schedule/widgets/next_maintenance_card.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

class NextScheduleScreen extends StatefulWidget {
  const NextScheduleScreen({super.key});

  @override
  State<NextScheduleScreen> createState() => _NextScheduleScreenState();
}

class _NextScheduleScreenState extends State<NextScheduleScreen> {
  bool showAllUpcoming = false;
  bool showAllCompleted = false;

  Future<void> _onRefresh() async {
    // Trigger event untuk refresh data
    context.read<NextScheduleBloc>().add(LoadNextScheduleEvent());

    // Tunggu sampai loading selesai (isLoading menjadi false)
    await context.read<NextScheduleBloc>().stream.firstWhere(
      (state) => !state.isLoading,
    );
  }

  @override
  Widget build(BuildContext context) {
    return NexScheduleWrapper(
      child: RefreshIndicator(
        onRefresh: _onRefresh,
        color: MyTheme.color.primary,
        child: Padding(
          padding: EdgeInsets.fromLTRB(
            AppSetting.setWidth(24),
            AppSetting.setHeight(50),
            AppSetting.setWidth(24),
            AppSetting.setHeight(10),
          ),
          child: SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(height: AppSetting.setHeight(20)),

                /// TITLE
                Text(
                  "Next Maintenance Schedule",
                  style: TextStyle(
                    fontFamily: "Inter",
                    fontWeight: FontWeight.w600,
                    fontSize: AppSetting.setFontSize(18),
                    color: MyTheme.color.white,
                  ),
                ),

                SizedBox(height: AppSetting.setHeight(12)),

                // ----------------------------------------------
                // UPCOMING SECTION
                // ----------------------------------------------
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      "MAINTENANCE UPCOMING LIST",
                      style: TextStyle(
                        fontFamily: "Inter",
                        fontWeight: FontWeight.w600,
                        fontSize: AppSetting.setFontSize(10),
                        color: MyTheme.color.white,
                      ),
                    ),
                    GestureDetector(
                      onTap: () =>
                          setState(() => showAllUpcoming = !showAllUpcoming),
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
                          showAllUpcoming ? "Show Less" : "See All",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(12),
                            color: MyTheme.color.primary,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),

                SizedBox(height: AppSetting.setHeight(12)),

                /// UPCOMING BLOC
                BlocBuilder<NextScheduleBloc, NextScheduleState>(
                  builder: (context, state) {
                    if (state.isLoading) {
                      return const Center(child: CircularProgressIndicator());
                    }

                    if (state.error != null) {
                      return Center(
                        child: Text(
                          "Error: ${state.error}",
                          style: TextStyle(color: MyTheme.color.white),
                        ),
                      );
                    }

                    final schedules = state.upcoming ?? [];

                    // --- EMPTY STATE UPCOMING ---
                    if (schedules.isEmpty) {
                      return _buildEmptyState(
                        message: "No upcoming schedules",
                        icon: Icons.calendar_month_outlined,
                      );
                    }

                    final displayed = showAllUpcoming
                        ? schedules
                        : schedules.take(3).toList();

                    return Column(
                      children: displayed.map((schedule) {
                        return Container(
                          margin: EdgeInsets.only(
                            bottom: AppSetting.setHeight(12),
                          ),
                          child: NextMaintenanceCard(
                            date: DateFormat(
                              'dd MMM yyyy - HH:mm',
                            ).format(schedule.createdOn),
                            title: "Maintenance for ${schedule.brand}",
                            service: "Service: ${schedule.title}",
                            plate: schedule.licensePlat,
                            onTap: () {},
                            onDetailTap: () {
                              context.goNamed(
                                AppRouter.maintenanceDetail,
                                extra: schedule,
                              );
                            },
                          ),
                        );
                      }).toList(),
                    );
                  },
                ),

                SizedBox(height: AppSetting.setHeight(24)),

                // ----------------------------------------------
                // COMPLETED SECTION
                // ----------------------------------------------
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      "COMPLETED MAINTENANCE LIST",
                      style: TextStyle(
                        fontFamily: "Inter",
                        fontWeight: FontWeight.w800,
                        fontSize: AppSetting.setFontSize(15),
                        color: MyTheme.color.white,
                      ),
                    ),
                    GestureDetector(
                      onTap: () =>
                          setState(() => showAllCompleted = !showAllCompleted),
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
                          showAllCompleted ? "Show Less" : "See All",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(12),
                            color: MyTheme.color.primary,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),

                SizedBox(height: AppSetting.setHeight(12)),

                /// COMPLETED BLOC
                BlocBuilder<NextScheduleBloc, NextScheduleState>(
                  builder: (context, state) {
                    if (state.isLoading) {
                      return const Center(child: CircularProgressIndicator());
                    }

                    if (state.error != null) {
                      return Center(
                        child: Text(
                          "Error: ${state.error}",
                          style: TextStyle(color: MyTheme.color.white),
                        ),
                      );
                    }

                    final schedules = state.completed ?? [];

                    // --- EMPTY STATE COMPLETED ---
                    if (schedules.isEmpty) {
                      return _buildEmptyState(
                        message: "No completed schedules",
                        icon: Icons.history_toggle_off,
                      );
                    }

                    final displayed = showAllCompleted
                        ? schedules
                        : schedules.take(3).toList();

                    return Column(
                      children: displayed.map((schedule) {
                        return Container(
                          margin: EdgeInsets.only(
                            bottom: AppSetting.setHeight(12),
                          ),
                          child: CompletedMaintenanceCard(
                            onTap: () {
                              showDialog(
                                context: context,
                                builder: (context) => InvoiceDialog(
                                  invoiceNumber: schedule.id.toString(),
                                  title: 'Maintenance for ${schedule.brand}',
                                  issueDate: DateFormat(
                                    "d MMM yyyy",
                                  ).format(schedule.dateTimes!),
                                  dueDate: DateFormat(
                                    "d MMM yyyy",
                                  ).format(schedule.dateTimes!),
                                  isPaid: true,
                                  items: [
                                    InvoiceItem(
                                      name: schedule.maintenanceType,
                                      quantity: 1,
                                      price: NumberFormat.currency(
                                        locale: 'id_ID',
                                        symbol: 'IDR ',
                                        decimalDigits: 0,
                                      ).format(
                                        int.tryParse(schedule.feeString!),
                                      ),
                                    ),
                                  ],
                                  totalPrice: NumberFormat.currency(
                                    locale: 'id_ID',
                                    symbol: 'IDR ',
                                    decimalDigits: 0,
                                  ).format(int.tryParse(schedule.feeString!)),
                                ),
                              );
                            },
                            onDetailTap: () {
                              context.goNamed(
                                AppRouter.maintenanceDetail,
                                extra: schedule,
                              );
                            },
                            date: DateFormat(
                              'dd MMM yyyy - HH:mm',
                            ).format(schedule.createdOn),
                            title: schedule.title,
                            service: schedule.maintenanceType,
                            plate: schedule.licensePlat,
                          ),
                        );
                      }).toList(),
                    );
                  },
                ),

                SizedBox(height: AppSetting.setHeight(30)),

                /// Button Schedule
                GestureDetector(
                  onTap: () {
                    debugPrint("Schedule button tapped!");
                  },
                  child: Container(
                    width: double.infinity,
                    padding: EdgeInsets.symmetric(
                      vertical: AppSetting.setHeight(14),
                    ),
                    decoration: BoxDecoration(
                      gradient:
                          MyTheme.color.whiteToLightSkyBlueGradientLeftToRight,
                      borderRadius: BorderRadius.circular(
                        AppSetting.setWidth(12),
                      ),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      "Schedule a Maintenance",
                      style: TextStyle(
                        fontFamily: "Inter",
                        fontWeight: FontWeight.w700,
                        fontSize: AppSetting.setFontSize(15),
                        color: MyTheme.color.primary,
                      ),
                    ),
                  ),
                ),

                SizedBox(height: AppSetting.setHeight(30)),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // ---------------------------------------------------------------------------
  // HELPER WIDGET UNTUK TAMPILAN KOSONG (KONSISTENSI UI)
  // ---------------------------------------------------------------------------
  Widget _buildEmptyState({required String message, required IconData icon}) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(
        vertical: AppSetting.setHeight(30),
        horizontal: AppSetting.setWidth(20),
      ),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        children: [
          Icon(
            icon,
            size: AppSetting.setWidth(60),
            color: Colors.grey.shade400,
          ),
          SizedBox(height: AppSetting.setHeight(12)),
          Text(
            message,
            style: TextStyle(
              color: Colors.grey.shade700,
              fontSize: AppSetting.setFontSize(14),
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }
}