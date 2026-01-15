// import 'package:flutter/material.dart';
// import 'package:flutter_bloc/flutter_bloc.dart';
// import 'package:intl/intl.dart'; // ✅ WAJIB: Untuk format tanggal

// // --- SESUAIKAN IMPORT INI DENGAN STRUKTUR FOLDERMU ---
// import 'package:drivero_automa/theme/theme.dart';
// import 'package:drivero_automa/theme/text_theme.dart';
// import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
// import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';
// import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_bloc.dart';
// import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_event.dart';
// import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_state.dart';
// import '../models/driver_fleet_model.dart';

// class AddMaintenanceModal {
//   static void show(BuildContext context) {
//     // 1. Tangkap BLoC dari halaman induk
//     final eventBloc = context.read<EventBloc>();
//     final authBloc = context.read<AuthBloc>();

//     int? userId;
//     final authState = authBloc.state;
//     if (authState is Authenticated) {
//       userId = authState.user.userId;
//     }

//     showDialog(
//       context: context,
//       barrierDismissible: false, // User wajib klik X atau Submit
//       builder: (dialogContext) {
//         // 2. Suntikkan BLoC ke dalam dialog (Jembatan Provider)
//         return MultiBlocProvider(
//           providers: [
//             BlocProvider.value(value: eventBloc),
//             BlocProvider.value(value: authBloc),
//           ],
//           child: const _AddMaintenanceForm(),
//         );
//       },
//     ).then((_) async {
//       // 🔥 3. LOGIC REFRESH (SOLUSI UTAMA) 🔥
//       if (userId != null) {
//         print("Dialog closed. Preparing to refresh tasks...");

//         // ✅ KITA KASIH JEDA SEDIKIT (300ms)
//         // Alasannya: Biarkan proses Add selesai dulu atau animasi tutup dialog kelar.
//         // Tanpa ini, request LoadTasks sering bentrok dengan request Add, 
//         // bikin list jadi kosong/hilang.
//         await Future.delayed(const Duration(milliseconds: 300));

//         print("Refreshing Tasks now...");
//         // Panggil event load task (pastikan event ini benar const LoadTasks())
//         eventBloc.add(const LoadTasks());
//       }
//     });
//   }
// }

// class _AddMaintenanceForm extends StatefulWidget {
//   const _AddMaintenanceForm({Key? key}) : super(key: key);

//   @override
//   State<_AddMaintenanceForm> createState() => _AddMaintenanceFormState();
// }

// class _AddMaintenanceFormState extends State<_AddMaintenanceForm> {
//   final _formKey = GlobalKey<FormState>();

//   // Controllers
//   final TextEditingController eventController = TextEditingController(); // Title
//   final TextEditingController descriptionController = TextEditingController(); // Description
//   final TextEditingController dateController = TextEditingController();
//   final TextEditingController locationController = TextEditingController();
//   final TextEditingController feeController = TextEditingController();

//   // Variables
//   DriverFleetModel? selectedVehicle;
//   String? selectedMaintenance;
//   int? currentUserId;

//   @override
//   void initState() {
//     super.initState();
//     // 4. Load Data Mobil saat dialog dibuka
//     WidgetsBinding.instance.addPostFrameCallback((_) {
//       final authState = context.read<AuthBloc>().state;
//       if (authState is Authenticated && authState.user.userId != null) {
//         currentUserId = authState.user.userId;
//         context.read<EventBloc>().add(LoadFleets(userId: currentUserId!));
//       }
//     });
//   }

//   @override
//   void dispose() {
//     eventController.dispose();
//     descriptionController.dispose();
//     dateController.dispose();
//     locationController.dispose();
//     feeController.dispose();
//     super.dispose();
//   }

//   @override
//   Widget build(BuildContext context) {
//     return BlocBuilder<EventBloc, TaskState>(
//       builder: (context, state) {
//         List<DriverFleetModel> vehicleList = [];
//         bool isLoading = false;

//         // Cek State hanya untuk dropdown mobil
//         if (state is TaskLoading) {
//           isLoading = true;
//         } else if (state is FleetLoaded) {
//           vehicleList = state.fleets;
//         }

//         return Dialog(
//           shape: RoundedRectangleBorder(
//             borderRadius: BorderRadius.circular(20),
//           ),
//           child: Padding(
//             padding: EdgeInsets.only(
//               left: 16,
//               right: 16,
//               top: 20,
//               bottom: MediaQuery.of(context).viewInsets.bottom + 16,
//             ),
//             child: SingleChildScrollView(
//               child: Form(
//                 key: _formKey,
//                 child: Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   mainAxisSize: MainAxisSize.min,
//                   children: [
//                     // --- HEADER ---
//                     Row(
//                       mainAxisAlignment: MainAxisAlignment.spaceBetween,
//                       children: [
//                         Text(
//                           "Add Maintenance",
//                           style: AppTextTheme.headline3.copyWith(
//                             color: MyTheme.color.primary,
//                           ),
//                         ),
//                         IconButton(
//                           icon: const Icon(Icons.close),
//                           onPressed: () => Navigator.pop(context),
//                         ),
//                       ],
//                     ),
//                     Divider(color: MyTheme.color.grey, thickness: 1),
//                     const SizedBox(height: 16),

//                     // --- 1. TITLE (Event Name) ---
//                     _buildLabel("Event Name"),
//                     const SizedBox(height: 6),
//                     TextFormField(
//                       controller: eventController,
//                       decoration: _buildInputDecoration("Ex: Ganti Ban"),
//                       validator: (value) =>
//                           value == null || value.isEmpty ? "Required" : null,
//                     ),
//                     const SizedBox(height: 16),

//                     // --- 2. DESCRIPTION ---
//                     _buildLabel("Description"),
//                     const SizedBox(height: 6),
//                     TextFormField(
//                       controller: descriptionController,
//                       maxLines: 2,
//                       decoration: _buildInputDecoration("Ex: Ganti oli dan filter"),
//                       validator: (value) =>
//                           value == null || value.isEmpty ? "Required" : null,
//                     ),
//                     const SizedBox(height: 16),

//                     // --- 3. VEHICLE TYPE ---
//                     _buildLabel("Vehicle Type"),
//                     const SizedBox(height: 6),
//                     isLoading
//                         ? const Center(child: Padding(
//                             padding: EdgeInsets.all(8.0),
//                             child: CircularProgressIndicator(),
//                           ))
//                         : DropdownButtonFormField<DriverFleetModel>(
//                             value: selectedVehicle,
//                             isExpanded: true,
//                             style: AppTextTheme.bodyText1.copyWith(
//                               color: MyTheme.color.primary,
//                             ),
//                             decoration: _buildInputDecoration("Select Vehicle"),
//                             items: vehicleList.map((fleet) {
//                               return DropdownMenuItem(
//                                 value: fleet,
//                                 child: Text(fleet.name, overflow: TextOverflow.ellipsis),
//                               );
//                             }).toList(),
//                             onChanged: (val) {
//                               setState(() {
//                                 selectedVehicle = val;
//                               });
//                             },
//                             validator: (value) =>
//                                 value == null ? "Please select vehicle" : null,
//                           ),
//                     const SizedBox(height: 16),

//                     // --- 4. MAINTENANCE TYPE ---
//                     _buildLabel("Maintenance Type"),
//                     const SizedBox(height: 6),
//                     DropdownButtonFormField<String>(
//                       value: selectedMaintenance,
//                       style: AppTextTheme.bodyText1.copyWith(
//                         color: MyTheme.color.primary,
//                       ),
//                       decoration: _buildInputDecoration("Select Type"),
//                       items: const [
//                         DropdownMenuItem(
//                           value: "Service Berkala", 
//                           child: Text("Service Berkala")
//                         ),
//                         DropdownMenuItem(
//                           value: "Oil Change",
//                           child: Text("Oil Change"),
//                         ),
//                         DropdownMenuItem(
//                           value: "Tire Replacement",
//                           child: Text("Tire Replacement"),
//                         ),
//                       ],
//                       onChanged: (val) {
//                         setState(() {
//                           selectedMaintenance = val;
//                         });
//                       },
//                       validator: (value) =>
//                           value == null ? "Please select type" : null,
//                     ),
//                     const SizedBox(height: 16),

//                     // --- 5. DATE ---
//                     _buildLabel("Date & Time"),
//                     const SizedBox(height: 6),
//                     TextFormField(
//                       controller: dateController,
//                       readOnly: true,
//                       decoration: _buildInputDecoration("dd/mm/yyyy",
//                           icon: Icons.calendar_today),
//                       validator: (value) =>
//                           value == null || value.isEmpty ? "Required" : null,
//                       onTap: () async {
//                         final date = await showDatePicker(
//                           context: context,
//                           firstDate: DateTime.now(),
//                           lastDate: DateTime(2100),
//                           initialDate: DateTime.now(),
//                         );
//                         if (date != null) {
//                           dateController.text =
//                               "${date.day}/${date.month}/${date.year}";
//                         }
//                       },
//                     ),
//                     const SizedBox(height: 16),

//                     // --- 6. LOCATION ---
//                     _buildLabel("Location Name"),
//                     const SizedBox(height: 6),
//                     TextFormField(
//                       controller: locationController,
//                       decoration: _buildInputDecoration("Ex: Bengkel A"),
//                       validator: (value) =>
//                           value == null || value.isEmpty ? "Required" : null,
//                     ),
//                     const SizedBox(height: 16),

//                     // --- 7. FEE ---
//                     _buildLabel("Maintenance Fee"),
//                     const SizedBox(height: 6),
//                     TextFormField(
//                       controller: feeController,
//                       keyboardType: TextInputType.number,
//                       decoration: _buildInputDecoration("Ex: 150000"),
//                       validator: (value) =>
//                           value == null || value.isEmpty ? "Required" : null,
//                     ),
//                     const SizedBox(height: 20),

//                     // --- SUBMIT BUTTON ---
//                     SizedBox(
//                       width: double.infinity,
//                       child: ElevatedButton(
//                         style: ElevatedButton.styleFrom(
//                           backgroundColor: MyTheme.color.primary,
//                           foregroundColor: MyTheme.color.white,
//                           shape: RoundedRectangleBorder(
//                             borderRadius: BorderRadius.circular(24),
//                           ),
//                           padding: const EdgeInsets.symmetric(vertical: 14),
//                         ),
//                         onPressed: () {
//                           if (_formKey.currentState!.validate()) {
                            
//                             // A. FORMAT TANGGAL (dd/MM/yyyy -> yyyy-MM-dd HH:mm:ss)
//                             String formattedDate = "";
//                             try {
//                               final inputDate = DateFormat('dd/MM/yyyy').parse(dateController.text);
//                               final dateWithTime = DateTime(
//                                   inputDate.year, inputDate.month, inputDate.day, 10, 00, 00);
//                               formattedDate = DateFormat('yyyy-MM-dd HH:mm:ss').format(dateWithTime);
//                             } catch (e) {
//                               print("Date Error: $e");
//                               formattedDate = DateTime.now().toString();
//                             }

//                             // B. KIRIM DATA KE API (BLoC)
//                             context.read<EventBloc>().add(AddMaintenanceTask(
//                               vehicleId: selectedVehicle!.id,
//                               title: eventController.text,
//                               maintenanceType: selectedMaintenance!,
//                               location: locationController.text,
//                               description: descriptionController.text,
//                               feeString: feeController.text,
//                               dateTimes: formattedDate,
//                               enumStatus: "Pending",
//                             ));

//                             // C. TUTUP DIALOG
//                             // Ini akan memicu logic .then() di atas (dengan delay)
//                             Navigator.pop(context);
//                           }
//                         },
//                         child: const Text("Submit"),
//                       ),
//                     ),
//                   ],
//                 ),
//               ),
//             ),
//           ),
//         );
//       },
//     );
//   }

//   // --- HELPER STYLES ---
//   Widget _buildLabel(String text) {
//     return Text(
//       text,
//       style: AppTextTheme.headline4.copyWith(
//         color: MyTheme.color.primary,
//       ),
//     );
//   }

//   InputDecoration _buildInputDecoration(String hint, {IconData? icon}) {
//     return InputDecoration(
//       isDense: true,
//       contentPadding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
//       hintStyle: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
//       hintText: hint,
//       prefixIcon: icon != null ? Icon(icon) : null,
//       border: OutlineInputBorder(
//         borderRadius: BorderRadius.circular(10),
//         borderSide: BorderSide(color: MyTheme.color.grey),
//       ),
//     );
//   }
// }
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';

// --- IMPORT BAWAAN KAMU (JANGAN DIHAPUS) ---
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_bloc.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_event.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_state.dart';
import '../models/driver_fleet_model.dart';

class AddMaintenanceModal {
  static void show(BuildContext context) {
    final eventBloc = context.read<EventBloc>();
    final authBloc = context.read<AuthBloc>();

    int? userId;
    final authState = authBloc.state;
    if (authState is Authenticated) {
      userId = authState.user.userId;
    }

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (dialogContext) {
        return MultiBlocProvider(
          providers: [
            BlocProvider.value(value: eventBloc),
            BlocProvider.value(value: authBloc),
          ],
          child: const _AddMaintenanceForm(),
        );
      },
    ).then((_) async {
      if (userId != null) {
        print("Dialog closed. Refreshing tasks...");
        await Future.delayed(const Duration(milliseconds: 300));
        eventBloc.add(const LoadTasks());
      }
    });
  }
}

class _AddMaintenanceForm extends StatefulWidget {
  const _AddMaintenanceForm({Key? key}) : super(key: key);

  @override
  State<_AddMaintenanceForm> createState() => _AddMaintenanceFormState();
}

class _AddMaintenanceFormState extends State<_AddMaintenanceForm> {
  final _formKey = GlobalKey<FormState>();

  // Controllers
  final TextEditingController eventController = TextEditingController();
  final TextEditingController descriptionController = TextEditingController();
  final TextEditingController dateController = TextEditingController();
  final TextEditingController locationController = TextEditingController();
  final TextEditingController feeController = TextEditingController();

  // Variables
  DriverFleetModel? selectedVehicle;
  String? selectedMaintenance;
  int? currentUserId;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final authState = context.read<AuthBloc>().state;
      if (authState is Authenticated && authState.user.userId != null) {
        currentUserId = authState.user.userId;
        context.read<EventBloc>().add(LoadFleets(userId: currentUserId!));
      }
    });
  }

  @override
  void dispose() {
    eventController.dispose();
    descriptionController.dispose();
    dateController.dispose();
    locationController.dispose();
    feeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<EventBloc, TaskState>(
      builder: (context, state) {
        List<DriverFleetModel> vehicleList = [];
        bool isLoading = false;

        if (state is TaskLoading) {
          isLoading = true;
        } else if (state is FleetLoaded) {
          vehicleList = state.fleets;
        }

        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
          // Beri jarak pinggir
          insetPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
          child: Container(
            // Batasi tinggi dialog MAX 85% layar.
            // Kita TIDAK pakai perhitungan manual keyboard di sini lagi (penyebab crash).
            constraints: BoxConstraints(
              maxHeight: MediaQuery.of(context).size.height * 0.85,
            ),
            // ClipRRect & Scaffold adalah KUNCI agar keyboard aman
            child: ClipRRect(
              borderRadius: BorderRadius.circular(20),
              child: Scaffold(
                // ✅ FITUR PENTING: Scaffold otomatis mengecil saat keyboard muncul
                resizeToAvoidBottomInset: true,
                backgroundColor: Colors.white, // Sesuaikan warna background dialog
                
                // Gunakan Column agar Header diam, Form yang scroll
                body: Column(
                  mainAxisSize: MainAxisSize.min, 
                  children: [
                    // --- HEADER (Tetap di atas) ---
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            "Add Maintenance",
                            style: AppTextTheme.headline3.copyWith(
                              color: MyTheme.color.primary,
                            ),
                          ),
                          IconButton(
                            icon: const Icon(Icons.close),
                            onPressed: () => Navigator.pop(context),
                            padding: EdgeInsets.zero,
                            constraints: const BoxConstraints(),
                          ),
                        ],
                      ),
                    ),
                    Divider(
                      color: MyTheme.color.grey, 
                      thickness: 1, 
                      height: 1
                    ),

                    // --- SCROLLABLE FORM ---
                    // Gunakan Expanded agar area form mengisi sisa ruang
                    Expanded(
                      child: SingleChildScrollView(
                        padding: const EdgeInsets.all(16),
                        physics: const BouncingScrollPhysics(),
                        child: Form(
                          key: _formKey,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              // --- 1. TITLE ---
                              _buildLabel("Event Name"),
                              const SizedBox(height: 6),
                              TextFormField(
                                controller: eventController,
                                decoration: _buildInputDecoration("Ex: Ganti Ban"),
                                validator: (value) =>
                                    value == null || value.isEmpty ? "Required" : null,
                              ),
                              const SizedBox(height: 16),

                              // --- 2. DESCRIPTION ---
                              _buildLabel("Description"),
                              const SizedBox(height: 6),
                              TextFormField(
                                controller: descriptionController,
                                maxLines: 2,
                                decoration:
                                    _buildInputDecoration("Ex: Ganti oli dan filter"),
                                validator: (value) =>
                                    value == null || value.isEmpty ? "Required" : null,
                              ),
                              const SizedBox(height: 16),

                              // --- 3. VEHICLE TYPE ---
                              _buildLabel("Vehicle Type"),
                              const SizedBox(height: 6),
                              isLoading
                                  ? const Center(
                                      child: Padding(
                                        padding: EdgeInsets.all(8.0),
                                        child: CircularProgressIndicator(),
                                      ),
                                    )
                                  : DropdownButtonFormField<DriverFleetModel>(
                                      value: selectedVehicle,
                                      isExpanded: true,
                                      style: AppTextTheme.bodyText1.copyWith(
                                        color: MyTheme.color.primary,
                                      ),
                                      decoration:
                                          _buildInputDecoration("Select Vehicle"),
                                      items: vehicleList.map((fleet) {
                                        return DropdownMenuItem(
                                          value: fleet,
                                          child: Text(fleet.name,
                                              overflow: TextOverflow.ellipsis),
                                        );
                                      }).toList(),
                                      onChanged: (val) {
                                        setState(() {
                                          selectedVehicle = val;
                                        });
                                      },
                                      validator: (value) => value == null
                                          ? "Please select vehicle"
                                          : null,
                                    ),
                              const SizedBox(height: 16),

                              // --- 4. MAINTENANCE TYPE ---
                              _buildLabel("Maintenance Type"),
                              const SizedBox(height: 6),
                              DropdownButtonFormField<String>(
                                value: selectedMaintenance,
                                style: AppTextTheme.bodyText1.copyWith(
                                  color: MyTheme.color.primary,
                                ),
                                decoration: _buildInputDecoration("Select Type"),
                                items: const [
                                  DropdownMenuItem(
                                      value: "Service Berkala",
                                      child: Text("Service Berkala")),
                                  DropdownMenuItem(
                                      value: "Oil Change",
                                      child: Text("Oil Change")),
                                  DropdownMenuItem(
                                      value: "Tire Replacement",
                                      child: Text("Tire Replacement")),
                                ],
                                onChanged: (val) {
                                  setState(() {
                                    selectedMaintenance = val;
                                  });
                                },
                                validator: (value) =>
                                    value == null ? "Please select type" : null,
                              ),
                              const SizedBox(height: 16),

                              // --- 5. DATE ---
                              _buildLabel("Date & Time"),
                              const SizedBox(height: 6),
                              TextFormField(
                                controller: dateController,
                                readOnly: true,
                                decoration: _buildInputDecoration("dd/mm/yyyy",
                                    icon: Icons.calendar_today),
                                validator: (value) =>
                                    value == null || value.isEmpty ? "Required" : null,
                                onTap: () async {
                                  final date = await showDatePicker(
                                    context: context,
                                    firstDate: DateTime.now(),
                                    lastDate: DateTime(2100),
                                    initialDate: DateTime.now(),
                                  );
                                  if (date != null) {
                                    dateController.text =
                                        "${date.day}/${date.month}/${date.year}";
                                  }
                                },
                              ),
                              const SizedBox(height: 16),

                              // --- 6. LOCATION ---
                              _buildLabel("Location Name"),
                              const SizedBox(height: 6),
                              TextFormField(
                                controller: locationController,
                                decoration: _buildInputDecoration("Ex: Bengkel A"),
                                validator: (value) =>
                                    value == null || value.isEmpty ? "Required" : null,
                              ),
                              const SizedBox(height: 16),

                              // --- 7. FEE ---
                              _buildLabel("Maintenance Fee"),
                              const SizedBox(height: 6),
                              TextFormField(
                                controller: feeController,
                                keyboardType: TextInputType.number,
                                decoration: _buildInputDecoration("Ex: 150000"),
                                validator: (value) =>
                                    value == null || value.isEmpty ? "Required" : null,
                              ),
                              const SizedBox(height: 20),

                              // --- SUBMIT BUTTON ---
                              SizedBox(
                                width: double.infinity,
                                child: ElevatedButton(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: MyTheme.color.primary,
                                    foregroundColor: MyTheme.color.white,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(24),
                                    ),
                                    padding: const EdgeInsets.symmetric(vertical: 14),
                                  ),
                                  onPressed: () {
                                    if (_formKey.currentState!.validate()) {
                                      String formattedDate = "";
                                      try {
                                        final inputDate = DateFormat('dd/MM/yyyy')
                                            .parse(dateController.text);
                                        final dateWithTime = DateTime(
                                            inputDate.year,
                                            inputDate.month,
                                            inputDate.day,
                                            10, 00, 00);
                                        formattedDate =
                                            DateFormat('yyyy-MM-dd HH:mm:ss')
                                                .format(dateWithTime);
                                      } catch (e) {
                                        print("Date Error: $e");
                                        formattedDate = DateTime.now().toString();
                                      }

                                      context.read<EventBloc>().add(
                                          AddMaintenanceTask(
                                              vehicleId: selectedVehicle!.id,
                                              title: eventController.text,
                                              maintenanceType: selectedMaintenance!,
                                              location: locationController.text,
                                              description: descriptionController.text,
                                              feeString: feeController.text,
                                              dateTimes: formattedDate,
                                              enumStatus: "Pending"));

                                      Navigator.pop(context);
                                    }
                                  },
                                  child: const Text("Submit"),
                                ),
                              ),
                              const SizedBox(height: 20),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  // --- HELPER STYLES ---
  Widget _buildLabel(String text) {
    return Text(
      text,
      style: AppTextTheme.headline4.copyWith(
        color: MyTheme.color.primary,
      ),
    );
  }

  InputDecoration _buildInputDecoration(String hint, {IconData? icon}) {
    return InputDecoration(
      isDense: true,
      contentPadding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      hintStyle: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
      hintText: hint,
      prefixIcon: icon != null ? Icon(icon) : null,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(10),
        borderSide: BorderSide(color: MyTheme.color.grey),
      ),
    );
  }
}