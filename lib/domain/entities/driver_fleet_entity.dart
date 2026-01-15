class DriverFleetEntity {
  final int vehicleId;
  final int userId;
  final String brand;
  final String licensePlat;
  final DateTime? insuredUntil;
  final DateTime? purchasedOn;
  final DateTime? lastMaintenance;

  DriverFleetEntity({
    required this.vehicleId,
    required this.userId,
    required this.brand,
    required this.licensePlat,
    this.insuredUntil,
    this.purchasedOn,
    this.lastMaintenance,
  });
}
