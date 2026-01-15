import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/core/widgets/invoice_dialog.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_bloc.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_event.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_state.dart';
import 'package:drivero_automa/presentation/last_maintenance/last_maintenance_screen_wrapper.dart';
import 'package:drivero_automa/presentation/last_maintenance/widgets/CardLastMaintenance.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

class LastMaintenanceScreen extends StatefulWidget {
  const LastMaintenanceScreen({super.key});

  @override
  State<LastMaintenanceScreen> createState() => _LastMaintenanceScreenState();
}

class _LastMaintenanceScreenState extends State<LastMaintenanceScreen> {
  
  // Helper biar UI bersih & aman dari Null
  String _formatPrice(String? priceString) {
    if (priceString == null) return "IDR 0";
    final number = int.tryParse(priceString) ?? 0;
    return NumberFormat.currency(
      locale: 'id_ID',
      symbol: 'IDR ',
      decimalDigits: 0,
    ).format(number);
  }

  String _formatDate(DateTime? date) {
    if (date == null) return "-";
    return DateFormat('dd MMMM yyyy HH:mm').format(date);
  }

  Future<void> _onRefresh() async {
    final bloc = context.read<LastMaintenanceBloc>();
    bloc.add(GetLastMaintenanceEvent());

    // Tunggu sampai state berubah (biar loading indicator muter beneran)
    await bloc.stream.firstWhere((state) =>
        state is LastMaintenanceLoaded || state is LastMaintenanceError);
  }

  @override
  Widget build(BuildContext context) {
    return last_maintenance_screen_wrapper(
      child: RefreshIndicator(
        onRefresh: _onRefresh,
        color: MyTheme.color.primary,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(AppSetting.setWidth(16)),
          physics: const AlwaysScrollableScrollPhysics(),
          child: BlocBuilder<LastMaintenanceBloc, LastMaintenanceState>(
            builder: (context, state) {
              if (state is LastMaintenanceLoaded) {
                // 1. CEK DATA KOSONG
                if (state.lastMaintenances.isEmpty) {
                  return SizedBox(
                    height: MediaQuery.of(context).size.height * 0.7,
                    child: Center(
                      child: Text(
                        "Belum ada riwayat maintenance",
                        style: TextStyle(color: MyTheme.color.white),
                      ),
                    ),
                  );
                }

                return Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(height: AppSetting.setHeight(20)),
                    Text(
                      "Last Maintenance History", // Ganti judul biar sesuai konteks
                      style: TextStyle(
                        fontFamily: "Inter",
                        fontWeight: FontWeight.w700,
                        fontSize: AppSetting.setFontSize(18),
                        color: MyTheme.color.white,
                      ),
                    ),
                    SizedBox(height: AppSetting.setHeight(12)),
                    
                    // 2. LISTVIEW YANG BENAR
                    ListView.builder(
                      shrinkWrap: true,
                      physics: const NeverScrollableScrollPhysics(),
                      // ⚠️ PENTING: Pakai length dari data, JANGAN HARDCODE ANGKA
                      itemCount: state.lastMaintenances.length, 
                      itemBuilder: (context, index) {
                        final item = state.lastMaintenances[index];

                        return CardLastMaintenance(
                          vehicleName: item.brand ?? "-",
                          plateNumber: item.licensePlat ?? "-",
                          title: 'Maintenance for ${item.brand ?? "-"}',
                          description: item.description ?? "-",
                          
                          // Gunakan Helper Function (Aman Null)
                          dateTime: _formatDate(item.dateTimes),
                          serviceDetail: item.maintenanceType,
                          location: item.location ?? "-",
                          price: item.feeString ?? "0", 
                          
                          onViewInvoice: () {
                            showDialog(
                              context: context,
                              builder: (context) => InvoiceDialog(
                                invoiceNumber: item.id.toString(),
                                title: 'Maintenance for ${item.brand}',
                                issueDate: DateFormat("d MMM yyyy")
                                    .format(item.dateTimes ?? DateTime.now()),
                                dueDate: DateFormat("d MMM yyyy")
                                    .format(item.dateTimes ?? DateTime.now()),
                                isPaid: true,
                                items: [
                                  InvoiceItem(
                                    name: item.maintenanceType,
                                    quantity: 1,
                                    price: _formatPrice(item.feeString),
                                  ),
                                ],
                                totalPrice: _formatPrice(item.feeString),
                              ),
                            );
                          },
                          onShowDetail: () => context.pushNamed(
                            AppRouter.maintenanceDetail,
                            extra: item,
                          ),
                        );
                      },
                    ),
                  ],
                );
              }

              if (state is LastMaintenanceError) {
                return SizedBox(
                  height: MediaQuery.of(context).size.height * 0.7,
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                         Icon(Icons.error_outline, color: Colors.red, size: 40),
                         SizedBox(height: 10),
                         Text(
                          state.message, // Tampilkan pesan error asli
                          style: TextStyle(color: MyTheme.color.white),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                );
              }

              // Loading State
              return SizedBox(
                 height: MediaQuery.of(context).size.height * 0.7,
                 child: Center(child: CircularProgressIndicator())
              );
            },
          ),
        ),
      ),
    );
  }
}