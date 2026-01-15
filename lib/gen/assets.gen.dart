// dart format width=80

/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
///  FlutterGen
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: deprecated_member_use,directives_ordering,implicit_dynamic_list_literal,unnecessary_import

import 'package:flutter/widgets.dart';

class $AssetsIconsGen {
  const $AssetsIconsGen();

  /// File path: assets/icons/Alarm Clock.svg
  String get alarmClock => 'assets/icons/Alarm Clock.svg';

  /// File path: assets/icons/Carr.svg
  String get carr => 'assets/icons/Carr.svg';

  /// File path: assets/icons/Dollar Coin.svg
  String get dollarCoin => 'assets/icons/Dollar Coin.svg';

  /// File path: assets/icons/Fatigue.svg
  String get fatigue => 'assets/icons/Fatigue.svg';

  /// File path: assets/icons/Home.svg
  String get home => 'assets/icons/Home.svg';

  /// File path: assets/icons/Insurance.svg
  String get insurance => 'assets/icons/Insurance.svg';

  /// File path: assets/icons/Location Pin.svg
  String get locationPin => 'assets/icons/Location Pin.svg';

  /// File path: assets/icons/Task List.svg
  String get taskList => 'assets/icons/Task List.svg';

  /// File path: assets/icons/accident.svg
  String get accident => 'assets/icons/accident.svg';

  /// File path: assets/icons/add_camera.svg
  String get addCamera => 'assets/icons/add_camera.svg';

  /// File path: assets/icons/card.svg
  String get card => 'assets/icons/card.svg';

  /// File path: assets/icons/card_jam.svg
  String get cardJam => 'assets/icons/card_jam.svg';

  /// File path: assets/icons/dollar.svg
  String get dollar => 'assets/icons/dollar.svg';

  /// File path: assets/icons/heart_attack.svg
  String get heartAttack => 'assets/icons/heart_attack.svg';

  /// File path: assets/icons/kms.svg
  String get kms => 'assets/icons/kms.svg';

  /// File path: assets/icons/last_maintenance.svg
  String get lastMaintenance => 'assets/icons/last_maintenance.svg';

  /// File path: assets/icons/next_schedule.svg
  String get nextSchedule => 'assets/icons/next_schedule.svg';

  /// File path: assets/icons/perintah.svg
  String get perintah => 'assets/icons/perintah.svg';

  /// File path: assets/icons/schedule.svg
  String get schedule => 'assets/icons/schedule.svg';

  /// File path: assets/icons/time.svg
  String get time => 'assets/icons/time.svg';

  /// File path: assets/icons/user_profile.svg
  String get userProfile => 'assets/icons/user_profile.svg';

  /// List of all assets
  List<String> get values => [
    alarmClock,
    carr,
    dollarCoin,
    fatigue,
    home,
    insurance,
    locationPin,
    taskList,
    accident,
    addCamera,
    card,
    cardJam,
    dollar,
    heartAttack,
    kms,
    lastMaintenance,
    nextSchedule,
    perintah,
    schedule,
    time,
    userProfile,
  ];
}

class $AssetsImagesGen {
  const $AssetsImagesGen();

  /// File path: assets/images/logo.png
  AssetGenImage get logo => const AssetGenImage('assets/images/logo.png');

  /// File path: assets/images/logo_login.png
  AssetGenImage get logoLogin =>
      const AssetGenImage('assets/images/logo_login.png');

  /// File path: assets/images/mobil_test.jpg
  AssetGenImage get mobilTest =>
      const AssetGenImage('assets/images/mobil_test.jpg');

  /// File path: assets/images/pp_user.jpg
  AssetGenImage get ppUser => const AssetGenImage('assets/images/pp_user.jpg');

  /// File path: assets/images/test.png
  AssetGenImage get test => const AssetGenImage('assets/images/test.png');

  /// List of all assets
  List<AssetGenImage> get values => [logo, logoLogin, mobilTest, ppUser, test];
}

class Assets {
  const Assets._();

  static const $AssetsIconsGen icons = $AssetsIconsGen();
  static const $AssetsImagesGen images = $AssetsImagesGen();
}

class AssetGenImage {
  const AssetGenImage(
    this._assetName, {
    this.size,
    this.flavors = const {},
    this.animation,
  });

  final String _assetName;

  final Size? size;
  final Set<String> flavors;
  final AssetGenImageAnimation? animation;

  Image image({
    Key? key,
    AssetBundle? bundle,
    ImageFrameBuilder? frameBuilder,
    ImageErrorWidgetBuilder? errorBuilder,
    String? semanticLabel,
    bool excludeFromSemantics = false,
    double? scale,
    double? width,
    double? height,
    Color? color,
    Animation<double>? opacity,
    BlendMode? colorBlendMode,
    BoxFit? fit,
    AlignmentGeometry alignment = Alignment.center,
    ImageRepeat repeat = ImageRepeat.noRepeat,
    Rect? centerSlice,
    bool matchTextDirection = false,
    bool gaplessPlayback = true,
    bool isAntiAlias = false,
    String? package,
    FilterQuality filterQuality = FilterQuality.medium,
    int? cacheWidth,
    int? cacheHeight,
  }) {
    return Image.asset(
      _assetName,
      key: key,
      bundle: bundle,
      frameBuilder: frameBuilder,
      errorBuilder: errorBuilder,
      semanticLabel: semanticLabel,
      excludeFromSemantics: excludeFromSemantics,
      scale: scale,
      width: width,
      height: height,
      color: color,
      opacity: opacity,
      colorBlendMode: colorBlendMode,
      fit: fit,
      alignment: alignment,
      repeat: repeat,
      centerSlice: centerSlice,
      matchTextDirection: matchTextDirection,
      gaplessPlayback: gaplessPlayback,
      isAntiAlias: isAntiAlias,
      package: package,
      filterQuality: filterQuality,
      cacheWidth: cacheWidth,
      cacheHeight: cacheHeight,
    );
  }

  ImageProvider provider({AssetBundle? bundle, String? package}) {
    return AssetImage(_assetName, bundle: bundle, package: package);
  }

  String get path => _assetName;

  String get keyName => _assetName;
}

class AssetGenImageAnimation {
  const AssetGenImageAnimation({
    required this.isAnimation,
    required this.duration,
    required this.frames,
  });

  final bool isAnimation;
  final Duration duration;
  final int frames;
}
