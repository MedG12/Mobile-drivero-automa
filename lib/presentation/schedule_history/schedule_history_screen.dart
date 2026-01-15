import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_event.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_state.dart';
import 'package:drivero_automa/presentation/schedule_history/schedule_history_wrapper.dart';
import 'package:drivero_automa/presentation/schedule_history/widgets/addMaintananceModal.dart';
import 'package:drivero_automa/presentation/schedule_history/widgets/cardTask.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:syncfusion_flutter_datepicker/datepicker.dart';

class ScheduleHistoryScreen extends StatefulWidget {
  const ScheduleHistoryScreen({super.key});

  @override
  State<ScheduleHistoryScreen> createState() => _ScheduleHistoryScreenState();
}

class _ScheduleHistoryScreenState extends State<ScheduleHistoryScreen> {
  String selectedFilterTask = 'All Task';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<EventBloc>().add(const LoadTasks());
    });
  }

  Widget build(BuildContext context) {
    // 🔥 HAPUS SCAFFOLD DI SINI.
    // Langsung panggil Wrapper-nya. Wrapper sudah punya Scaffold & resizeToAvoidBottomInset.
    return ScheduleHistoryWrapper(
      child: Container(
        decoration: BoxDecoration(
          gradient: MyTheme.color.darkLightDarkBlueGradientTopToBottom,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: AppSetting.setHeight(20)),

            /// HEADER
            Padding(
              padding: EdgeInsets.symmetric(
                horizontal: AppSetting.setWidth(16),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "Maintenance Schedule",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontWeight: FontWeight.w700,
                      fontSize: AppSetting.setFontSize(18),
                      letterSpacing: AppSetting.setWidth(-0.24),
                      color: MyTheme.color.white,
                    ),
                  ),
                  TextButton(
                    onPressed: () => AddMaintenanceModal.show(context),
                    style: TextButton.styleFrom(
                      padding: const EdgeInsets.symmetric(horizontal: 12),
                      backgroundColor: MyTheme.color.white,
                      minimumSize: Size(0, AppSetting.setHeight(30)),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                    ),
                    child: Text(
                      "Add Maintenance",
                      style: TextStyle(
                        fontSize: AppSetting.setFontSize(12),
                        color: MyTheme.color.primary,
                      ),
                    ),
                  ),
                ],
              ),
            ),

            SizedBox(height: AppSetting.setHeight(20)),

            /// DATE PICKER
            Padding(
              padding: EdgeInsets.symmetric(
                horizontal: AppSetting.setWidth(16),
              ),
              child: BlocBuilder<EventBloc, TaskState>(
                builder: (context, state) {
                  List<DateTime> specialDates = [];
                  if (state is TaskLoaded || state is TaskWithFleetLoaded) {
                    final tasks = state is TaskLoaded
                        ? state.taskDetails
                        : (state as TaskWithFleetLoaded).taskDetails;

                    specialDates = tasks
                        .map(
                          (task) => DateTime(
                            task.date.year,
                            task.date.month,
                            task.date.day,
                          ),
                        )
                        .toList();
                  }

                  return Container(
                    height: AppSetting.setHeight(250),
                    padding: EdgeInsets.all(AppSetting.setWidth(8)),
                    decoration: BoxDecoration(
                      
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(
                        color: MyTheme.color.white,
                        width: 2,
                      ),
                    ),
                    child: SfDateRangePicker(
                      backgroundColor: MyTheme.color.white,
                      view: DateRangePickerView.month,
                      selectionMode: DateRangePickerSelectionMode.single,
                      onSelectionChanged:
                          (DateRangePickerSelectionChangedArgs args) {
                        if (args.value is DateTime) {
                          context.read<EventBloc>().add(
                                FilterByDate(args.value),
                              );
                        }
                      },
                      monthViewSettings: DateRangePickerMonthViewSettings(
                        specialDates: specialDates,
                        showTrailingAndLeadingDates: true,
                        firstDayOfWeek: 7,
                        dayFormat: 'EEE',
                      ),
                      monthCellStyle: DateRangePickerMonthCellStyle(
                        specialDatesDecoration: BoxDecoration(
                          color: MyTheme.color.primary.withOpacity(0.3),
                          shape: BoxShape.circle,
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),

            SizedBox(height: AppSetting.setHeight(20)),

            /// FILTER CHIPS
            Padding(
              padding: EdgeInsets.symmetric(
                horizontal: AppSetting.setWidth(16),
              ),
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children: ['All Task', 'Recent Task'].map((filter) {
                    return Padding(
                      padding: const EdgeInsets.only(right: 8),
                      child: FilterChip(
                        backgroundColor:
                            MyTheme.color.lightDarkBlue.withAlpha(70),
                        label: Text(
                          filter,
                          style: AppTextTheme.bodyText2.copyWith(
                            color: selectedFilterTask == filter
                                ? MyTheme.color.white
                                : MyTheme.color.secondary,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        selected: selectedFilterTask == filter,
                        selectedColor: MyTheme.color.primary,
                        showCheckmark: false,
                        onSelected: (_) {
                          setState(() => selectedFilterTask = filter);
                          context.read<EventBloc>().add(
                                FilterTasks(
                                  filter == 'All Task' ? 'All' : filter,
                                ),
                              );
                        },
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(24),
                          side: BorderSide.none,
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ),

            SizedBox(height: AppSetting.setHeight(12)),

            /// LIST TASK
            Expanded(
              child: Padding(
                padding: EdgeInsets.symmetric(
                  horizontal: AppSetting.setWidth(16),
                ),
                child: BlocBuilder<EventBloc, TaskState>(
                  builder: (context, state) {
                    if (state is TaskLoading) {
                      return const Center(child: CircularProgressIndicator());
                    } else if (state is TaskLoaded ||
                        state is TaskWithFleetLoaded) {
                      final tasks = state is TaskLoaded
                          ? state.taskDetails
                          : (state as TaskWithFleetLoaded).taskDetails;

                      if (tasks.isEmpty) {
                        return const Center(
                          child: Text('No maintenance tasks found'),
                        );
                      }

                      return ListView.builder(
                        itemCount: tasks.length,
                        itemBuilder: (context, index) {
                          return CardTask(taskItem: tasks[index]);
                        },
                      );
                    } else if (state is TaskError) {
                      return Center(
                        child: Text('Failed to load tasks: ${state.message}'),
                      );
                    }
                    return const SizedBox.shrink();
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}