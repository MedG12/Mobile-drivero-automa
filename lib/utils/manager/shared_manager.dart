import 'package:shared_preferences/shared_preferences.dart';

/// Generic class to do shared preferences
/// management with dynamic data type
class SharedManager<T> {
  /// Instance shared preferences
  /// and make it private
  late SharedPreferences prefs;

  /// Get type of this generic class
  // ignore: avoid_shadowing_type_parameters
  Type type<T>() => T;

  /// Call the shared preferences
  /// field using keyname and with
  /// the generic return type
  Future<T?> read(String key) async {
    prefs = await SharedPreferences.getInstance();
    if (prefs.containsKey(key)) {
      return prefs.get(key) as T;
    }
    return null;
  }

  /// This function will set value
  /// into shared preferences
  Future store(String key, dynamic value) async {
    prefs = await SharedPreferences.getInstance();
    var types = type<T>();

    if (types == String) {
      prefs.setString(key, value);
    } else if (types == int) {
      prefs.setInt(key, value);
    } else if (types == bool) {
      prefs.setBool(key, value);
    } else if (types == double) {
      prefs.setDouble(key, value);
    } else if (types == List) {
      prefs.setStringList(key, value);
    }
  }

  Future delete(String key) async {
    prefs = await SharedPreferences.getInstance();
    prefs.remove(key);
  }

  /// Clear all stored shared preferences
  Future clear() async {
    prefs = await SharedPreferences.getInstance();
    prefs.clear();
  }
}
