abstract class HomeEvent {}

/// Ambil semua fleet milik driver
class FetchDriverFleet extends HomeEvent {
  final int userId;
  FetchDriverFleet({required this.userId});
}

/// Ambil semua gambar berdasarkan banyak fleet ID
class FetchFleetImages extends HomeEvent {}

class FetchUpcomingMaintenances extends HomeEvent {}

// home_event.dart - tambahkan event ini
class RefreshHomeData extends HomeEvent {
  final int userId;
  RefreshHomeData({required this.userId});
}
