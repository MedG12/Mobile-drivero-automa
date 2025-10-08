import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/features/next_schedule/presentation/next_schedule_screen_wrapper.dart';
import 'package:drivero_automa/features/next_schedule/presentation/widgets/completed_maintenance_card.dart';
import 'package:drivero_automa/features/next_schedule/presentation/widgets/next_maintenance_card.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Dummy Data
final List<Map<String, String>> maintenanceSchedules = [
  {
    'date': '27 Sep 2025 - 09:30',
    'title': 'Oil Change',
    'service': 'Scheduled Service - 10,000 KM',
    'plate': 'B 1234 XYZ',
  },
  {
    'date': '30 Sep 2025 - 13:00',
    'title': 'Brake Check',
    'service': 'Scheduled Service - 15,000 KM',
    'plate': 'D 5678 ABC',
  },
  {
    'date': '05 Oct 2025 - 15:00',
    'title': 'General Inspection',
    'service': 'Full Inspection & Tune-up',
    'plate': 'F 9102 DEF',
  },
  {
    'date': '06 Oct 2025 - 09:00',
    'title': 'Tire Replacement',
    'service': 'Scheduled Service - 20,000 KM',
    'plate': 'G 1111 HIJ',
  },
  {
    'date': '07 Oct 2025 - 11:00',
    'title': 'Battery Check',
    'service': 'Full Inspection & Tune-up',
    'plate': 'H 2222 KLM',
  },
];

final List<Map<String, String>> completedSchedules = [
  {
    'date': '15 Aug 2025 - 10:00',
    'title': 'AC Service',
    'service': 'Full AC Cleaning & Freon Top-up',
    'plate': 'B 1234 XYZ',
  },
  {
    'date': '20 Jul 2025 - 14:00',
    'title': 'Suspension Check',
    'service': 'Inspection and Shock Absorber Repair',
    'plate': 'D 5678 ABC',
  },
  {
    'date': '01 Jul 2025 - 09:00',
    'title': 'Annual Service',
    'service': 'Scheduled Service - 5,000 KM',
    'plate': 'F 9102 DEF',
  },
  {
    'date': '10 Jun 2025 - 11:00',
    'title': 'Radiator Flush',
    'service': 'Coolant Replacement',
    'plate': 'G 1111 HIJ',
  },
];

class NextScheduleScreen extends StatefulWidget {
  const NextScheduleScreen({super.key});

  @override
  State<NextScheduleScreen> createState() => _NextScheduleScreenState();
}

class _NextScheduleScreenState extends State<NextScheduleScreen> {
  bool showAllUpcoming = false;
  bool showAllCompleted = false;

  @override
  Widget build(BuildContext context) {
    final displayedUpcomingSchedules = showAllUpcoming
        ? maintenanceSchedules
        : maintenanceSchedules.take(3).toList();

    final displayedCompletedSchedules = showAllCompleted
        ? completedSchedules
        : completedSchedules.take(3).toList();

    return NexScheduleWrapper(
      child: Padding(
        padding: EdgeInsets.fromLTRB(
          AppSetting.setWidth(24),
          AppSetting.setHeight(50),
          AppSetting.setWidth(24),
          AppSetting.setHeight(10),
        ),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Back button
              
              SizedBox(height: AppSetting.setHeight(20)),

              // Title
              Text(
                "Next Maintenance Schedule",
                style: TextStyle(
                  fontFamily: "Inter",
                  fontWeight: FontWeight.w600,
                  fontSize: AppSetting.setFontSize(18),
                  height: AppSetting.setHeight(20) / AppSetting.setFontSize(18),
                  letterSpacing: AppSetting.setWidth(-0.24),
                  color: MyTheme.color.white,
                ),
              ),

              SizedBox(height: AppSetting.setHeight(12)),

              // Upcoming Header + See All
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "MAINTENANCE UPCOMING LIST",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontWeight: FontWeight.w600,
                      fontSize: AppSetting.setFontSize(10),
                      letterSpacing: AppSetting.setWidth(-0.24),
                      color: MyTheme.color.white,
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      setState(() {
                        showAllUpcoming = !showAllUpcoming;
                      });
                    },
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
                          fontFamily: "Inter",
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                    ),
                  ),
                ],
              ),

              SizedBox(height: AppSetting.setHeight(12)),

              // Upcoming Cards
              Column(
                children: displayedUpcomingSchedules.map((schedule) {
                  return Container(
                    margin: EdgeInsets.only(bottom: AppSetting.setHeight(12)),
                    child: NextMaintenanceCard(
                      date: schedule['date']!,
                      title: schedule['title']!,
                      service: schedule['service']!,
                      plate: schedule['plate']!,
                      onTap: () {
                        debugPrint("Upcoming Card tapped: ${schedule['title']}");
                      },
                      onDetailTap: () {
                        debugPrint(
                            "Upcoming Show Detail: ${schedule['title']}");
                      },
                    ),
                  );
                }).toList(),
              ),

              SizedBox(height: AppSetting.setHeight(24)),

              // Completed Header + See All
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "COMPLETED MAINTENANCE LIST",
                    style: TextStyle(
                     fontFamily: "Inter",
                  fontWeight: FontWeight.w800,
                  fontSize: AppSetting.setFontSize(15),
                  height: AppSetting.setHeight(20) / AppSetting.setFontSize(18),
                  letterSpacing: AppSetting.setWidth(-0.24),
                  color: MyTheme.color.white,
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      setState(() {
                        showAllCompleted = !showAllCompleted;
                      });
                    },
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
                          fontFamily: "Inter",
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                    ),
                  ),
                ],
              ),

              SizedBox(height: AppSetting.setHeight(12)),

              // Completed Cards
              Column(
                children: displayedCompletedSchedules.map((schedule) {
                  return Container(
                    margin: EdgeInsets.only(bottom: AppSetting.setHeight(12)),
                    child: CompletedMaintenanceCard(
                      date: schedule['date']!,
                      title: schedule['title']!,
                      service: schedule['service']!,
                      plate: schedule['plate']!,
                    ),
                  );
                }).toList(),
              ),

              SizedBox(height: AppSetting.setHeight(30)),

              // Schedule Maintenance Button
              GestureDetector(
                onTap: () {
                  debugPrint("Schedule button tapped!");
                },
                child: Container(
                  width: AppSetting.setWidth(
                      MediaQuery.of(context).size.width), // full width
                  padding: EdgeInsets.symmetric(
                    vertical: AppSetting.setHeight(14),
                  ),
                  decoration: BoxDecoration(
                      gradient: MyTheme.color.whiteToLightSkyBlueGradientLeftToRight, 
                    borderRadius:
                        BorderRadius.circular(AppSetting.setWidth(12)),
                  ),
                  alignment: Alignment.center,
                  child: Text(
                    "Schedule a Maintenance",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontWeight: FontWeight.w700,
                      fontSize: AppSetting.setFontSize(15),
                      letterSpacing: AppSetting.setWidth(-0.24),
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
    );
  }
}
