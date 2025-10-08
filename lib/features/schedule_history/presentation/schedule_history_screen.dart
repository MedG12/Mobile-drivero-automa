import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_event.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_state.dart';
import 'package:drivero_automa/features/schedule_history/presentation/models/task_item.dart';
import 'package:drivero_automa/features/schedule_history/presentation/schedule_history_wrapper.dart';
import 'package:drivero_automa/features/schedule_history/presentation/widgets/addMaintananceModal.dart';
import 'package:drivero_automa/features/schedule_history/presentation/widgets/cardTask.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/cupertino.dart';
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
  Widget build(BuildContext context) {
    return ScheduleHistoryWrapper(
      child: Container(
        decoration: BoxDecoration(
          gradient: MyTheme.color.darkLightDarkBlueGradientTopToBottom,
        ),
        child: SingleChildScrollView(
          padding: EdgeInsets.all(AppSetting.setWidth(16)),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(height: AppSetting.setHeight(20)),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "Maintenance Schedule",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontWeight: FontWeight.w700,
                      fontSize: AppSetting.setFontSize(18),
                      height:
                          AppSetting.setHeight(20) / AppSetting.setFontSize(18),
                      letterSpacing: AppSetting.setWidth(-0.24),
                      color: MyTheme.color.white,
                    ),
                  ),
                  TextButton(
                    onPressed: () => AddMaintenanceModal.show(context),
                    style: TextButton.styleFrom(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 0,
                      ),
                      backgroundColor: MyTheme.color.white,
                      minimumSize: Size(0, AppSetting.setHeight(30)),
                      tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                    ),
                    child: Text(
                      "Add Maintenance",
                      style: TextStyle(
                        fontSize: 12,
                        color: MyTheme.color.primary,
                      ),
                    ),
                  ),

                  // You can add a filter button or icon here if needed
                ],
              ),
              SizedBox(height: AppSetting.setHeight(20)),
              BlocBuilder<EventBloc, TaskState>(
                builder: (context, state) {
                  List<DateTime> specialDates = [];
                  if (state is TaskLoaded) {
                    specialDates = state.taskDetails
                        .map(
                          (task) => DateTime(
                            task.date.year,
                            task.date.month,
                            task.date.day,
                          ),
                        )
                        .toList();
                  }

                  print(specialDates);
                  return Container(
                    padding: EdgeInsets.all(AppSetting.setWidth(8)),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(color: MyTheme.color.white, width: 2),
                    ),
                    child: SfDateRangePicker(
                      backgroundColor: MyTheme.color.white,

                      headerStyle: DateRangePickerHeaderStyle(
                        backgroundColor: MyTheme.color.white,
                        textAlign: TextAlign.left,
                        textStyle: TextStyle(
                          color: MyTheme.color.primary,
                          fontSize: AppSetting.setFontSize(18),
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      view: DateRangePickerView.month,
                      showNavigationArrow: false,
                      selectionMode: DateRangePickerSelectionMode.multiple,
                      monthViewSettings: DateRangePickerMonthViewSettings(
                        specialDates: specialDates,
                        showTrailingAndLeadingDates: true,
                        firstDayOfWeek: 7,
                        dayFormat: 'EEE',
                        viewHeaderStyle: DateRangePickerViewHeaderStyle(
                          textStyle: TextStyle(
                            color: MyTheme.color.grey,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      monthCellStyle: DateRangePickerMonthCellStyle(
                        textStyle: TextStyle(
                          fontSize: 14,
                          color: MyTheme.color.primary,
                        ),
                        specialDatesDecoration: BoxDecoration(
                          color: MyTheme.color.primary.withOpacity(0.3),
                          shape: BoxShape.circle,
                        ),
                        trailingDatesTextStyle: TextStyle(
                          fontSize: 14,
                          color: MyTheme.color.grey,
                        ),
                        leadingDatesTextStyle: TextStyle(
                          fontSize: 14,
                          color: MyTheme.color.grey,
                        ),
                        todayTextStyle: TextStyle(
                          fontSize: 14,
                          color: Colors.black87,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  );
                },
              ),
              SizedBox(height: AppSetting.setHeight(20)),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Maintenance Schedule",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontWeight: FontWeight.w700,
                      fontSize: AppSetting.setFontSize(18),
                      height:
                          AppSetting.setHeight(20) / AppSetting.setFontSize(18),
                      letterSpacing: AppSetting.setWidth(-0.24),
                      color: MyTheme.color.white,
                    ),
                    textAlign: TextAlign.start,
                  ),
                  SizedBox(height: AppSetting.setHeight(6)),
                  Text(
                    "Maintenance History List",
                    style: AppTheme.lightTheme.textTheme.bodyMedium?.copyWith(
                      color: MyTheme.color.white,
                      fontSize: AppSetting.setFontSize(14),
                    ),
                    textAlign: TextAlign.start,
                  ),
                ],
              ),
              SizedBox(height: AppSetting.setHeight(20)),
              TextField(
                decoration: InputDecoration(
                  hintText: 'Search',
                  hintStyle: TextStyle(
                    color: MyTheme.color.grey,
                    fontSize: AppSetting.setFontSize(18),
                    fontWeight: FontWeight.bold,
                  ),
                  prefixIcon: Icon(Icons.search, color: MyTheme.color.grey),
                  filled: true,
                  fillColor: MyTheme.color.white,
                  contentPadding: EdgeInsets.symmetric(
                    vertical: 0,
                    horizontal: 12,
                  ),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(24),
                    borderSide: BorderSide.none,
                  ),
                ),
              ),
              SizedBox(height: AppSetting.setHeight(12)),
              //filter list tab
              SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children: [
                    ...['All Task', 'Recent Task'].map((filter) {
                      return Padding(
                        padding: const EdgeInsets.only(right: 8.0),
                        child: FilterChip(
                          backgroundColor: MyTheme.color.lightDarkBlue
                              .withAlpha(70),
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
                            setState(() {
                              selectedFilterTask = filter;
                            });
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
                  ],
                ),
              ),
              SizedBox(height: AppSetting.setHeight(12)),

              // List of maintenance history cards
              BlocBuilder<EventBloc, TaskState>(
                builder: (context, state) {
                  if (state is TaskLoading) {
                    return Center(child: CircularProgressIndicator());
                  } else if (state is TaskLoaded) {
                    final tasks = state.taskDetails;
                    return ListView.builder(
                      shrinkWrap: true,
                      physics: NeverScrollableScrollPhysics(),
                      itemCount: tasks.length,
                      itemBuilder: (context, index) {
                        return CardTask(taskItem: tasks[index]);
                      },
                    );
                  } else if (state is TaskError) {
                    return Center(child: Text('Failed to load tasks'));
                  }
                  return SizedBox.shrink();
                },
              ),
              SizedBox(height: AppSetting.setHeight(20)),
            ],
          ),
        ),
      ),
    );
  }
}
