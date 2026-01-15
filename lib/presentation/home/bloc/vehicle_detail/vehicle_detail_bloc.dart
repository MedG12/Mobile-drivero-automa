// import 'package:bloc/bloc.dart';
// import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
// import 'package:drivero_automa/presentation/home/bloc/vehicle_detail/vehicle_detail_state.dart';
// import 'package:drivero_automa/domain/usecases/fleet_img_by_id.dart';
// import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
// import 'package:equatable/equatable.dart';

// part 'vehicle_detail_event.dart';
// // vehicle_detail_bloc.dart
// class VehicleDetailBloc extends Bloc<VehicleDetailEvent, VehicleDetailState> {
//   final GetFleetImagesByFleetIds getFleetImagesByFleetIds;
//   final GetMaintenancesUsecase getMaintenances;

//   // 🔹 simpan vehicleId global di Bloc
//   int? currentVehicleId;

//   VehicleDetailBloc({
//     required this.getFleetImagesByFleetIds,
//     required this.getMaintenances,
//   }) : super(const VehicleDetailState()) {
//     on<GetVehicleDetailEvent>((event, emit) async {
//       // simpan vehicleId global
//       currentVehicleId = event.vehicleId;

//       // Loading awal
//       emit(state.copyWith(
//         isFleetImagesLoading: true,
//         isMaintenancesLoading: true,
//         error: null,
//       ));

//       try {
//         // Ambil fleet images berdasarkan vehicleId
//         final images = await getFleetImagesByFleetIds(fleetId: event.vehicleId);

//         // Ambil semua maintenances
//         final result = await getMaintenances();

//         MaintenanceEntity? recent;
//         MaintenanceEntity? upcoming;
//         int totalFee = 0;

//         result.fold(
//           (failure) {
//             emit(state.copyWith(
//               isFleetImagesLoading: false,
//               isMaintenancesLoading: false,
//               fleetImages: images,
//               error: failure.toString(),
//             ));
//           },
//           (data) {
//             // Filter maintenance khusus untuk vehicle ini
//             final vehicleMaintenances = data
//                 .where((m) => m.vehicleId == currentVehicleId)
//                 .toList();

//             // 🔹 Filter recent (Completed) berdasarkan modifiedOn
//             final completed = vehicleMaintenances
//                 .where((m) => m.enumStatus == "Completed")
//                 .toList();
//             if (completed.isNotEmpty) {
//               completed.sort((a, b) => b.modifiedOn.compareTo(a.modifiedOn));
//               recent = completed.first;

//               // total fee dari semua completed
//               totalFee = completed.fold(
//                   0, (sum, m) => sum + (int.tryParse(m.feeString ?? '0') ?? 0));
//             }

//             // 🔹 Filter upcoming (Pending) berdasarkan createdOn
//             final pending = vehicleMaintenances
//                 .where((m) => m.enumStatus == "Pending")
//                 .toList();
//             if (pending.isNotEmpty) {
//               pending.sort((a, b) => b.createdOn.compareTo(a.createdOn));
//               upcoming = pending.first;
//             }

//             emit(state.copyWith(
//               isFleetImagesLoading: false,
//               isMaintenancesLoading: false,
//               fleetImages: images,
//               recentMaintenance: recent,
//               upcomingMaintenance: upcoming,
//               totalCompletedFee: totalFee,
//               error: null,
//             ));
//           },
//         );
//       } catch (e) {
//         emit(state.copyWith(
//           isFleetImagesLoading: false,
//           isMaintenancesLoading: false,
//           error: e.toString(),
//         ));
//       }
//     });
//   }
// }



// import 'package:bloc/bloc.dart';
// import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
// import 'package:drivero_automa/presentation/home/bloc/vehicle_detail/vehicle_detail_state.dart';
// import 'package:drivero_automa/domain/usecases/fleet_img_by_id.dart';
// import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
// import 'package:drivero_automa/domain/usecases/get_kms_driver_usecase.dart';
// import 'package:equatable/equatable.dart';

// part 'vehicle_detail_event.dart';

// class VehicleDetailBloc extends Bloc<VehicleDetailEvent, VehicleDetailState> {
//   final GetFleetImagesByFleetIds getFleetImagesByFleetIds;
//   final GetMaintenancesUsecase getMaintenances;
//   final GetKmsDriverUsecase getKmsDriver;

//   int? currentVehicleId;

//   VehicleDetailBloc({
//     required this.getFleetImagesByFleetIds,
//     required this.getMaintenances,
//     required this.getKmsDriver,
//   }) : super(const VehicleDetailState()) {
//     on<GetVehicleDetailEvent>((event, emit) async {
//       currentVehicleId = event.vehicleId;

//       emit(state.copyWith(
//         isFleetImagesLoading: true,
//         isMaintenancesLoading: true,
//         isKmsLoading: true,
//         error: null,
//       ));

//       try {
//         final images =
//             await getFleetImagesByFleetIds(fleetId: event.vehicleId);

//         // 🔹 HIT API KMS_DRIVER
//         final kmsResult = await getKmsDriver(event.vehicleId);
//         int? kmsValue;
//         kmsResult.fold(
//           (failure) => kmsValue = null,
//           (data) => kmsValue = data.totalKms,
//         );

//         // 🔹 Ambil maintenances
//         final result = await getMaintenances();

//         MaintenanceEntity? recent;
//         MaintenanceEntity? upcoming;
//         int totalFee = 0;

//         result.fold(
//           (failure) {
//             emit(state.copyWith(
//               isFleetImagesLoading: false,
//               isMaintenancesLoading: false,
//               isKmsLoading: false,
//               fleetImages: images,
//               error: failure.toString(),
//             ));
//           },
//           (data) {
//             final vehicleMaintenances = data
//                 .where((m) => m.vehicleId == currentVehicleId)
//                 .toList();

//             final completed = vehicleMaintenances
//                 .where((m) => m.enumStatus == "Completed")
//                 .toList();
//             if (completed.isNotEmpty) {
//               completed.sort((a, b) => b.modifiedOn.compareTo(a.modifiedOn));
//               recent = completed.first;

//               totalFee = completed.fold(
//                 0,
//                 (sum, m) => sum + (int.tryParse(m.feeString ?? '0') ?? 0),
//               );
//             }

//             final pending = vehicleMaintenances
//                 .where((m) => m.enumStatus == "Pending")
//                 .toList();
//             if (pending.isNotEmpty) {
//               pending.sort((a, b) => b.createdOn.compareTo(a.createdOn));
//               upcoming = pending.first;
//             }

//             emit(state.copyWith(
//               isFleetImagesLoading: false,
//               isMaintenancesLoading: false,
//               isKmsLoading: false,
//               fleetImages: images,
//               recentMaintenance: recent,
//               upcomingMaintenance: upcoming,
//               totalCompletedFee: totalFee,
//               vehicleKms: kmsValue,
//               error: null,
//             ));
//           },
//         );
//       } catch (e) {
//         emit(state.copyWith(
//           isFleetImagesLoading: false,
//           isMaintenancesLoading: false,
//           isKmsLoading: false,
//           error: e.toString(),
//         ));
//       }
//     });
//   }
// }


import 'package:bloc/bloc.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/presentation/home/bloc/vehicle_detail/vehicle_detail_state.dart';
import 'package:drivero_automa/domain/usecases/fleet_img_by_id.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_kms_driver_usecase.dart';
import 'package:equatable/equatable.dart';

part 'vehicle_detail_event.dart';

class VehicleDetailBloc extends Bloc<VehicleDetailEvent, VehicleDetailState> {
  final GetFleetImagesByFleetIds getFleetImagesByFleetIds;
  final GetMaintenancesUsecase getMaintenances;
  final GetKmsDriverUsecase getKmsDriver;

  int? currentVehicleId;

  VehicleDetailBloc({
    required this.getFleetImagesByFleetIds,
    required this.getMaintenances,
    required this.getKmsDriver,
  }) : super(const VehicleDetailState()) {
    on<GetVehicleDetailEvent>((event, emit) async {
      currentVehicleId = event.vehicleId;

      emit(state.copyWith(
        isFleetImagesLoading: true,
        isMaintenancesLoading: true,
        isKmsLoading: true,
        error: null,
      ));

      try {
        final images =
            await getFleetImagesByFleetIds(fleetId: event.vehicleId);

        // 🔹 HIT API KMS_DRIVER
        final kmsResult = await getKmsDriver(event.vehicleId);
        int? kmsValue;
        kmsResult.fold(
          (failure) => kmsValue = null,
          (data) => kmsValue = data.totalKms,
        );

        // 🔹 Ambil maintenances
        final result = await getMaintenances();

        MaintenanceEntity? recent;
        MaintenanceEntity? upcoming;
        int totalFee = 0;

        result.fold(
          (failure) {
            emit(state.copyWith(
              isFleetImagesLoading: false,
              isMaintenancesLoading: false,
              isKmsLoading: false,
              fleetImages: images,
              error: failure.toString(),
            ));
          },
          (data) {
            final vehicleMaintenances = data
                .where((m) => m.vehicleId == currentVehicleId)
                .toList();

            final completed = vehicleMaintenances
                .where((m) => m.enumStatus == "Completed")
                .toList();
            if (completed.isNotEmpty) {
              completed.sort((a, b) => b.modifiedOn.compareTo(a.modifiedOn));
              recent = completed.first;

              totalFee = completed.fold(
                0,
                (sum, m) => sum + (int.tryParse(m.feeString ?? '0') ?? 0),
              );
            }

            final pending = vehicleMaintenances
                .where((m) => m.enumStatus == "Pending")
                .toList();
            if (pending.isNotEmpty) {
              pending.sort((a, b) => b.createdOn.compareTo(a.createdOn));
              upcoming = pending.first;
            }

            emit(state.copyWith(
              isFleetImagesLoading: false,
              isMaintenancesLoading: false,
              isKmsLoading: false,
              fleetImages: images,
              recentMaintenance: recent,
              upcomingMaintenance: upcoming,
              totalCompletedFee: totalFee,
              vehicleKms: kmsValue,
              error: null,
            ));
          },
        );
      } catch (e) {
        emit(state.copyWith(
          isFleetImagesLoading: false,
          isMaintenancesLoading: false,
          isKmsLoading: false,
          error: e.toString(),
        ));
      }
    });
  }
}
