import 'package:drivero_automa/config/app_config.dart' show AppSetting;
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_state.dart';
import 'package:drivero_automa/features/schedule_history/presentation/models/task_item.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:intl/intl.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class CardTask extends StatefulWidget {
  final TaskItem taskItem;

  const CardTask({super.key, required this.taskItem});

  @override
  State<CardTask> createState() => _CardTaskState();
}

class _CardTaskState extends State<CardTask> {
  bool isExpanded = false;

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<EventBloc, TaskState>(
      builder: (context, state) {
        return Container(
          margin: const EdgeInsets.only(bottom: 12),
          padding: const EdgeInsets.symmetric(vertical: 8),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ListTile(
                contentPadding: EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 6,
                ),
                title: Row(
                  children: [
                    const Icon(
                      Icons.calendar_today,
                      size: 18,
                      color: Colors.blue,
                    ),
                    SizedBox(width: AppSetting.setWidth(12)),
                    Text(
                      DateFormat(
                        "d MMMM yyyy - HH.mm",
                      ).format(widget.taskItem.date),
                      style: const TextStyle(
                        fontSize: 13,
                        color: Colors.black54,
                      ),
                    ),
                  ],
                ),
                subtitle: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      widget.taskItem.title,
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: Colors.black,
                      ),
                    ),
                    TextButton(
                      onPressed: () {
                        setState(() {
                          isExpanded = !isExpanded;
                        });
                      },
                      style: TextButton.styleFrom(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 0,
                        ),
                        backgroundColor: MyTheme.color.primary,
                        minimumSize: Size(0, AppSetting.setHeight(30)),
                        tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                        ),
                      ),
                      child: Text(
                        isExpanded ? "Show Less" : "Show Detail",
                        style: TextStyle(
                          fontSize: 12,
                          color: MyTheme.color.white,
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              // --- EXPANDED CONTENT ---
              if (isExpanded) ...[
                const SizedBox(height: 12),
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(color: MyTheme.color.lightSkyBlue),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        widget.taskItem.description,
                        style: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 4),
                      const Text("Service for Engine Unit"),
                      const SizedBox(height: 6),
                      Row(
                        children: [
                          const Icon(Icons.directions_car, size: 18),
                          const SizedBox(width: 6),
                          Text(widget.taskItem.car),
                        ],
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 12),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.access_time, color: Colors.orange),
                    const SizedBox(width: 6),
                    Text(
                      widget.taskItem.status,
                      style: const TextStyle(color: Colors.orange),
                    ),
                  ],
                ),
              ],
            ],
          ),
        );
      },
    );
  }
}
