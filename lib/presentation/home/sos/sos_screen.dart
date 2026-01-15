import 'dart:async';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/presentation/home/sos/widgets/sos_content_card.dart';
import 'package:drivero_automa/presentation/home/sos/widgets/sos_reason_section.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';



class SosScreen extends StatefulWidget {
  const SosScreen({super.key});

  @override
  State<SosScreen> createState() => _SosScreenState();
}

class _SosScreenState extends State<SosScreen> {
  int _countdown = 15 * 60;
  Timer? _timer;
  int _selectedReasonIndex = 0;

  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_countdown > 0) {
        setState(() => _countdown--);
      } else {
        timer.cancel();
        print("Emergency call triggered for: ${_getReasonLabel()}");
      }
    });
  }

  String _getReasonLabel() {
    switch (_selectedReasonIndex) {
      case 0:
        return "Accident";
      case 1:
        return "Heart Attack";
      case 2:
        return "Fatigue";
      default:
        return "Unknown";
    }
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  String _formatCountdown() {
    final minutes = (_countdown ~/ 60).toString().padLeft(2, '0');
    final seconds = (_countdown % 60).toString().padLeft(2, '0');
    return "$minutes:$seconds";
  }

  @override
  Widget build(BuildContext context) {
    return BackgroundPageWidget(
      child: SingleChildScrollView(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: AppSetting.setWidth(24)),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(height: AppSetting.setHeight(20)),

              // 🔹 HEADER (digabung di sini)
              Padding(
                padding: EdgeInsets.only(
                  top: AppSetting.setHeight(12),
                  left: AppSetting.setWidth(12),
                  right: AppSetting.setWidth(12),
                ),
                child: Row(
                  children: [
                    GestureDetector(
                      onTap: () {
                        _timer?.cancel();
                        Navigator.pop(context);
                      },
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          horizontal: AppSetting.setWidth(14),
                          vertical: AppSetting.setHeight(6),
                        ),
                        decoration: BoxDecoration(
                          color: MyTheme.color.white,
                          borderRadius: BorderRadius.circular(30),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.15),
                              blurRadius: 4,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                        child: Icon(
                          Icons.arrow_back,
                          color: MyTheme.color.primary,
                          size: AppSetting.setFontSize(18),
                        ),
                      ),
                    ),
                    Expanded(
                      child: Center(
                        child: Text(
                          "SOS Call",
                          style: TextStyle(
                            color: MyTheme.color.white,
                            fontSize: AppSetting.setFontSize(18),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(width: 40),
                  ],
                ),
              ),

              SizedBox(height: AppSetting.setHeight(40)),

              // 🔸 Konten utama
              SosContentCard(
                countdownText: _formatCountdown(),
                onCancel: () {
                  _timer?.cancel();
                  Navigator.pop(context);
                },
              ),

              SizedBox(height: AppSetting.setHeight(40)),

              // 🔹 Bagian “What Happened?”
             SosReasonSection(
                selectedIndex: _selectedReasonIndex,
                onSelected: (index) => setState(() => _selectedReasonIndex = index),
              ),

              SizedBox(height: AppSetting.setHeight(40)),
            ],
          ),
        ),
      ),
    );
  }
}
