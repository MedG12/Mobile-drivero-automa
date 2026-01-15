import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/datasources/local/FleetImageLocalData.dart';
import 'package:drivero_automa/data/datasources/remote/FleetImageRemoteDataSourceImpl.dart';
import 'package:drivero_automa/data/models/flet_img_respon.dart';
import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/repositories/flet_img_repository.dart';

class FleetImageRepositoryImpl implements FleetImageRepository {
  final FleetImageRemoteDataSource remoteDataSource;
  final FleetImageLocalDataSource localDataSource;
  final AuthLocalDataSource authLocalDataSource;

  FleetImageRepositoryImpl({
    required this.remoteDataSource,
    required this.localDataSource,
    required this.authLocalDataSource,
  });

  @override
  Future<List<FleetImageEntity>> getFleetImages({int? fleetId}) async {
    try {
      final token = await authLocalDataSource.getToken();
      if (token == null) throw Exception("Token tidak ditemukan");

      // =============================================================
      // fleetId NULL → FETCH SEMUA
      // =============================================================
      if (fleetId == null) {
        final response = await remoteDataSource.fetchFleetImages(
          fleetId: null,
          token: token,
        );

        // cache sebagai ALL (id=0)
        await localDataSource.cacheFleetImages(
          0,
          FleetImageResponse(result: response.result),
        );

        return response.result.map((m) => m.toEntity()).toList();
      }

      // =============================================================
      // fleetId ADA → FETCH 1 ID SAJA
      // =============================================================
      final response = await remoteDataSource.fetchFleetImages(
        fleetId: fleetId,
        token: token,
      );

      final filtered = response.result
          .where((e) => e.idFleet == fleetId)
          .toList();

      await localDataSource.cacheFleetImages(
        fleetId,
        FleetImageResponse(result: filtered),
      );

      return filtered.map((m) => m.toEntity()).toList();
    }

    // =============================================================
    // Fallback Cache
    // =============================================================
    catch (e) {
      print("Remote gagal: $e → fallback cache");

      // fleetId = null → ambil cache ALL
      if (fleetId == null) {
        final cached = await localDataSource.getCachedFleetImages(0);
        if (cached != null) {
          return cached.result.map((m) => m.toEntity()).toList();
        }
        throw Exception("Tidak ada cache ALL");
      }

      // fleetId ada → ambil cache ID
      final cached = await localDataSource.getCachedFleetImages(fleetId);
      if (cached != null) {
        return cached.result.map((m) => m.toEntity()).toList();
      }

      throw Exception("Tidak ada cache untuk fleetId $fleetId");
    }
  }
}
