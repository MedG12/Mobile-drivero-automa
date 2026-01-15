import 'dart:convert';
import 'package:drivero_automa/data/models/driver_fleet_response.dart';
import 'package:shared_preferences/shared_preferences.dart';

abstract class DriverFleetLocalDataSource {
  Future<void> cacheDriverFleet(DriverFleetResponse  fleet);
  Future<DriverFleetResponse ?> getCachedDriverFleet();
  Future<void> clearCachedDriverFleet();
}

class DriverFleetLocalDataSourceImpl implements DriverFleetLocalDataSource {
  final SharedPreferences prefs;

  static const _cacheKey = 'CACHED_DRIVER_FLEET';
  static const _cacheTimestampKey = 'CACHED_DRIVER_FLEET_TIMESTAMP';
  static const Duration cacheValidity = Duration(hours: 1);

  DriverFleetLocalDataSourceImpl({required this.prefs});

  @override
  Future<void> cacheDriverFleet(DriverFleetResponse  fleet) async {
    final jsonString = jsonEncode(fleet.toJson());
    await prefs.setString(_cacheKey, jsonString);
    await prefs.setInt(_cacheTimestampKey, DateTime.now().millisecondsSinceEpoch);
  }

  @override
  Future<DriverFleetResponse ?> getCachedDriverFleet() async {
    final jsonString = prefs.getString(_cacheKey);
    final timestamp = prefs.getInt(_cacheTimestampKey);

    if (jsonString == null || timestamp == null) return null;

    final cacheTime = DateTime.fromMillisecondsSinceEpoch(timestamp);
    if (DateTime.now().difference(cacheTime) > cacheValidity) {
      await clearCachedDriverFleet();
      return null;
    }

    try {
      final Map<String, dynamic> jsonMap = jsonDecode(jsonString);
      return DriverFleetResponse .fromJson(jsonMap);
    } catch (e) {
      await clearCachedDriverFleet();
      return null;
    }
  }

  @override
  Future<void> clearCachedDriverFleet() async {
    await prefs.remove(_cacheKey);
    await prefs.remove(_cacheTimestampKey);
  }
}
