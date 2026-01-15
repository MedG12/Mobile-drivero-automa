import 'dart:convert';
import 'package:drivero_automa/data/models/flet_img_respon.dart';
import 'package:shared_preferences/shared_preferences.dart';
abstract class FleetImageLocalDataSource {
  Future<void> cacheFleetImages(int idFleet, FleetImageResponse response);
  Future<FleetImageResponse?> getCachedFleetImages(int idFleet);
  Future<void> clearCachedFleetImages(int idFleet);
}

class FleetImageLocalDataSourceImpl implements FleetImageLocalDataSource {
  final SharedPreferences prefs;

  FleetImageLocalDataSourceImpl({required this.prefs});

  String _cacheKey(int idFleet) => 'CACHED_FLEET_IMAGES_$idFleet';
  String _cacheTimestampKey(int idFleet) => 'CACHED_FLEET_IMAGES_TIMESTAMP_$idFleet';
  static const Duration cacheValidity = Duration(hours: 1);

  @override
  Future<void> cacheFleetImages(int idFleet, FleetImageResponse response) async {
    final jsonString = jsonEncode(response.toJson());
    await prefs.setString(_cacheKey(idFleet), jsonString);
    await prefs.setInt(_cacheTimestampKey(idFleet), DateTime.now().millisecondsSinceEpoch);
  }

  @override
  Future<FleetImageResponse?> getCachedFleetImages(int idFleet) async {
    final jsonString = prefs.getString(_cacheKey(idFleet));
    final timestamp = prefs.getInt(_cacheTimestampKey(idFleet));
    if (jsonString == null || timestamp == null) return null;

    final cacheTime = DateTime.fromMillisecondsSinceEpoch(timestamp);
    if (DateTime.now().difference(cacheTime) > cacheValidity) {
      await clearCachedFleetImages(idFleet);
      return null;
    }

    try {
      final Map<String, dynamic> jsonMap = jsonDecode(jsonString);
      return FleetImageResponse.fromJson(jsonMap);
    } catch (e) {
      await clearCachedFleetImages(idFleet);
      return null;
    }
  }

  @override
  Future<void> clearCachedFleetImages(int idFleet) async {
    await prefs.remove(_cacheKey(idFleet));
    await prefs.remove(_cacheTimestampKey(idFleet));
  }
}