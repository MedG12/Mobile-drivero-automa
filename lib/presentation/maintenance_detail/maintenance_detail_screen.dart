import 'package:drivero_automa/core/widgets/invoice_dialog.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/presentation/maintenance_detail/maintenance_detail_wrapper.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/utils/date/date_format_utils.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class MaintenanceDetailScreen extends StatelessWidget {
  final MaintenanceEntity maintenance;
  const MaintenanceDetailScreen({super.key, required this.maintenance});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: MaintenanceDetailWrapper(
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(20.0), // p-5
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Back Button
                ElevatedButton.icon(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  icon: const Icon(Icons.arrow_back, size: 18, weight: 1000),
                  label: Text(
                    'Back',
                    style: AppTextTheme.headline5.copyWith(
                      fontWeight: FontWeight.bold,
                      color: MyTheme.color.primary,
                    ), // text-sm
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    foregroundColor: MyTheme.color.primary,
                    elevation: 4, // shadow-md
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(25), // rounded-full
                    ),
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 8,
                    ),
                  ),
                ),
                const SizedBox(height: 20), // mb-5
                // Header
                Text(
                  'Maintenance for ${maintenance.brand}',
                  style: AppTextTheme.headline3.copyWith(color: Colors.white),
                ),
                const SizedBox(height: 4),
                Text(
                  maintenance.enumStatus == "Pending"
                      ? 'MAINTENANCE UPCOMING LIST'
                      : 'MAINTENANCE COMPLETED LIST',
                  style: TextStyle(
                    color: Color(0xFFBFDBFE), // text-blue-200
                    fontSize: 12, // text-xs
                    fontWeight: FontWeight.w500,
                    letterSpacing: 1.2, // tracking-wider
                  ),
                ),
                const SizedBox(height: 20), // mb-5
                // Main Card
                Expanded(
                  child: SingleChildScrollView(
                    child: Column(
                      children: [
                        Container(
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(
                              24,
                            ), // rounded-3xl
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.25),
                                blurRadius: 25,
                                spreadRadius: -5,
                                offset: const Offset(0, 20),
                              ),
                            ],
                          ),
                          padding: const EdgeInsets.all(20), // p-5
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              // Motorcycle Image with Badge
                              Stack(
                                children: [
                                  Container(
                                    height: 176, // h-44
                                    decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(
                                        16,
                                      ), // rounded-2xl
                                      gradient: const LinearGradient(
                                        begin: Alignment.topLeft,
                                        end: Alignment.bottomRight,
                                        colors: [
                                          Color(0xFF9CA3AF), // from-gray-400
                                          Color(0xFF4B5563), // to-gray-600
                                        ],
                                      ),
                                    ),
                                    child: ClipRRect(
                                      borderRadius: BorderRadius.circular(16),
                                      child: Image.network(
                                        'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?w=800&auto=format&fit=crop',
                                        width: double.infinity,
                                        fit: BoxFit.cover,
                                        opacity: const AlwaysStoppedAnimation(
                                          0.9,
                                        ),
                                        errorBuilder:
                                            (context, error, stackTrace) {
                                              return const Center(
                                                child: Icon(
                                                  Icons.motorcycle,
                                                  size: 80,
                                                  color: Colors.white70,
                                                ),
                                              );
                                            },
                                      ),
                                    ),
                                  ),
                                  Positioned(
                                    top: 0, // top-3
                                    left: 0, // left-3
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                        horizontal: 12, // px-3
                                        vertical: 6, // py-1.5
                                      ),
                                      decoration: BoxDecoration(
                                        color:
                                            maintenance.enumStatus == "Pending"
                                            ? Color(0xFFEE6C4D)
                                            : MyTheme
                                                  .color
                                                  .success, // bg-orange-500
                                        borderRadius: BorderRadius.only(
                                          topRight: Radius.circular(16),
                                          bottomRight: Radius.circular(16),
                                        ), // rounded-lg
                                        boxShadow: [
                                          BoxShadow(
                                            color: Colors.black.withOpacity(
                                              0.25,
                                            ),
                                            blurRadius: 10,
                                            offset: const Offset(0, 4),
                                          ),
                                        ],
                                      ),
                                      child: Text(
                                        maintenance.enumStatus!,
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 12, // text-xs
                                          fontWeight: FontWeight.w600,
                                        ),
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 20), // mb-5
                              // Title
                              Text(
                                maintenance.title,
                                style: TextStyle(
                                  color: Color(0xFF1E3A8A), // text-blue-900
                                  fontSize: 18, // text-lg
                                  fontWeight: FontWeight.bold,
                                  height: 1.17, // mb-3
                                ),
                              ),
                              const SizedBox(height: 12),

                              // Description
                              Text(
                                maintenance.description,
                                style: AppTextTheme.bodyText2.copyWith(
                                  color: MyTheme.color.primary,
                                ),
                              ),
                              const SizedBox(height: 20), // mb-5
                              // Details
                              _buildDetailRow(
                                Icons.access_time,
                                DateFormat(
                                  'd MMM yyyy - HH:mm',
                                ).format(maintenance.dateTimes!),
                                context,
                              ),
                              const SizedBox(height: 12), // space-y-3
                              _buildDetailRow(
                                Icons.list_alt_rounded,
                                maintenance.maintenanceType,
                                context,
                              ),
                              const SizedBox(height: 12), // space-y-3
                              _buildDetailRow(
                                Icons.location_on,
                                maintenance.location,
                                context,
                              ),
                              const SizedBox(height: 12),
                              _buildDetailRow(
                                Icons.attach_money,
                                NumberFormat.currency(
                                  locale: 'id_ID',
                                  symbol: 'IDR ',
                                  decimalDigits:
                                      0, // tanpa koma, sesuaikan kebutuhan
                                ).format(
                                  int.tryParse(maintenance.feeString!) ?? 0,
                                ),
                                context,
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 20), // mb-5
                        // Remind Me Button
                        maintenance.enumStatus == "Pending"
                            ? SizedBox(
                                width: double.infinity,
                                child: ElevatedButton(
                                  onPressed: () {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                        content: Text(
                                          'Reminder set successfully!',
                                        ),
                                        duration: Duration(seconds: 2),
                                        behavior: SnackBarBehavior.floating,
                                      ),
                                    );
                                  },
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.white,
                                    foregroundColor: const Color(0xFF1E3A8A),
                                    elevation: 12, // shadow-xl
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(
                                        30,
                                      ), // rounded-full
                                    ),
                                    padding: const EdgeInsets.symmetric(
                                      vertical: 14,
                                    ), // py-3.5
                                  ),
                                  child: const Text(
                                    'Remind Me',
                                    style: TextStyle(
                                      fontSize: 16, // text-base
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              )
                            : SizedBox(
                                width: double.infinity,
                                child: ElevatedButton(
                                  onPressed: () {
                                    showDialog(
                                      context: context,
                                      builder: (context) => InvoiceDialog(
                                        invoiceNumber: maintenance.id
                                            .toString(),
                                        title:
                                            'Maintenance for ${maintenance.brand}',
                                        issueDate: DateFormat(
                                          "d MMM yyyy",
                                        ).format(maintenance.dateTimes!),
                                        dueDate: DateFormat(
                                          "d MMM yyyy",
                                        ).format(maintenance.dateTimes!),
                                        isPaid: true,
                                        items: [
                                          InvoiceItem(
                                            name: maintenance.maintenanceType,
                                            quantity: 1,
                                            price:
                                                NumberFormat.currency(
                                                  locale: 'id_ID',
                                                  symbol: 'IDR ',
                                                  decimalDigits: 0,
                                                ).format(
                                                  int.tryParse(
                                                    maintenance.feeString!,
                                                  ),
                                                ),
                                          ),
                                        ],
                                        totalPrice:
                                            NumberFormat.currency(
                                              locale: 'id_ID',
                                              symbol: 'IDR ',
                                              decimalDigits: 0,
                                            ).format(
                                              int.tryParse(
                                                maintenance.feeString!,
                                              ),
                                            ),
                                      ),
                                    );
                                  },
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.white,
                                    foregroundColor: const Color(0xFF1E3A8A),
                                    elevation: 12, // shadow-xl
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(
                                        30,
                                      ), // rounded-full
                                    ),
                                    padding: const EdgeInsets.symmetric(
                                      vertical: 14,
                                    ), // py-3.5
                                  ),
                                  child: const Text(
                                    'View Invoice',
                                    style: TextStyle(
                                      fontSize: 16, // text-base
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildDetailRow(IconData icon, String text, context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Icon(
          icon,
          color: MyTheme.color.darkBlue, // text-blue-700
          size: MediaQuery.of(context).size.width * 0.06,
        ),
        const SizedBox(width: 12), // gap-3
        Text(
          text,
          style: TextStyle(
            color: MyTheme.color.primary, // text-gray-800
            fontSize: 14, // text-sm
            fontWeight: FontWeight.w500,
          ),
        ),
      ],
    );
  }
}
