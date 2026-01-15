import 'package:bloc/bloc.dart';
import 'package:drivero_automa/data/models/driver_fleet_model.dart';
import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/usecases/FleetImg.dart';
import 'package:drivero_automa/domain/usecases/get_fleets_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'home_event.dart';
import 'home_state.dart';

class HomeBloc extends Bloc<HomeEvent, HomeState> {
  final GetDriverFleets getDriverFleet;
  final GetFleetImages getFleetImages;
  final GetMaintenancesUsecase getMaintenancesUsecase;

  HomeBloc({
    required this.getDriverFleet,
    required this.getFleetImages,
    required this.getMaintenancesUsecase,
  }) : super(const HomeState()) {
    on<FetchDriverFleet>(_onFetchDriverFleet);
    on<FetchFleetImages>(_onFetchFleetImages);
    on<FetchUpcomingMaintenances>(_fetchUpcomingMaintenances);
    on<RefreshHomeData>(_onRefreshHomeData); // ← tambahkan ini
  }

  // Handler untuk refresh - menggabungkan fleet dan maintenance
  Future<void> _onRefreshHomeData(
    RefreshHomeData event,
    Emitter<HomeState> emit,
  ) async {
    emit(
      state.copyWith(
        isDriverFleetLoading: true,
        isMaintenancesLoading: true,
        error: null,
      ),
    );

    try {
      // Fetch fleet dan maintenance secara bersamaan
      await Future.wait([
        _fetchFleetData(event.userId, emit),
        _fetchMaintenanceData(emit),
      ]);
    } catch (e) {
      emit(
        state.copyWith(
          isDriverFleetLoading: false,
          isMaintenancesLoading: false,
          error: e.toString(),
        ),
      );
    }
  }

  // Helper method untuk fetch fleet data
  Future<void> _fetchFleetData(int userId, Emitter<HomeState> emit) async {
    try {
      final result = await getDriverFleet(userId);
      final fleets = result.getOrElse(() => []);

      emit(state.copyWith(isDriverFleetLoading: false, fleets: fleets));

      // Fetch images
      emit(state.copyWith(isFleetImagesLoading: true));
      final allImages = await getFleetImages();

      final fleetIds = fleets
          .map((f) => f.vehicleId)
          .where((id) => id != null)
          .cast<int>()
          .toList();

      final filtered = allImages
          .where((img) => fleetIds.contains(img.idFleet))
          .toList();

      emit(state.copyWith(isFleetImagesLoading: false, images: filtered));
    } catch (e) {
      rethrow;
    }
  }

  // Helper method untuk fetch maintenance data
  Future<void> _fetchMaintenanceData(Emitter<HomeState> emit) async {
    try {
      final result = await getMaintenancesUsecase();
      result.fold(
        (failure) {
          emit(state.copyWith(isMaintenancesLoading: false, error: failure));
        },
        (data) {
          final upcoming = data
              .where((m) => m.enumStatus == "Pending")
              .toList();
          emit(
            state.copyWith(
              isMaintenancesLoading: false,
              maintenances: upcoming,
            ),
          );
        },
      );
    } catch (e) {
      rethrow;
    }
  }

  // Method yang sudah ada tetap sama...
  Future<void> _onFetchDriverFleet(
    FetchDriverFleet event,
    Emitter<HomeState> emit,
  ) async {
    emit(state.copyWith(isDriverFleetLoading: true, error: null));
    try {
      final result = await getDriverFleet(event.userId);
      final fleets = result.getOrElse(() => []);
      emit(state.copyWith(isDriverFleetLoading: false, fleets: fleets));

      emit(state.copyWith(isFleetImagesLoading: true));
      final allImages = await getFleetImages();

      final fleetIds = fleets
          .map((f) => f.vehicleId)
          .where((id) => id != null)
          .cast<int>()
          .toList();

      final filtered = allImages
          .where((img) => fleetIds.contains(img.idFleet))
          .toList();

      emit(state.copyWith(isFleetImagesLoading: false, images: filtered));
    } catch (e) {
      emit(
        state.copyWith(
          isDriverFleetLoading: false,
          isFleetImagesLoading: false,
          error: e.toString(),
        ),
      );
    }
  }

  Future<void> _onFetchFleetImages(
    FetchFleetImages event,
    Emitter<HomeState> emit,
  ) async {
    emit(state.copyWith(isFleetImagesLoading: true, error: null));
    try {
      final images = await getFleetImages();
      emit(state.copyWith(isFleetImagesLoading: false, images: images));
    } catch (e) {
      emit(state.copyWith(isFleetImagesLoading: false, error: e.toString()));
    }
  }

  Future<void> _fetchUpcomingMaintenances(
    FetchUpcomingMaintenances event,
    Emitter<HomeState> emit,
  ) async {
    emit(state.copyWith(isMaintenancesLoading: true, error: null));
    try {
      final result = await getMaintenancesUsecase();
      result.fold(
        (failure) {
          emit(state.copyWith(isMaintenancesLoading: false, error: failure));
        },
        (data) {
          final upcoming = data
              .where((m) => m.enumStatus == "Pending")
              .toList();
          emit(
            state.copyWith(
              isMaintenancesLoading: false,
              maintenances: upcoming,
            ),
          );
        },
      );
    } catch (e) {
      emit(state.copyWith(isMaintenancesLoading: false, error: e.toString()));
    }
  }
}
